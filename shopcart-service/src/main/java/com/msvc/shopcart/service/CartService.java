package com.msvc.shopcart.service;


import com.msvc.shopcart.domain.Cart;
import com.msvc.shopcart.domain.CartItem;
import com.msvc.shopcart.dto.AddItemRequest;
import com.msvc.shopcart.dto.UpdateQtyRequest;
import com.msvc.shopcart.mapper.CartMapper;
import com.msvc.shopcart.repository.CartItemRepository;
import com.msvc.shopcart.repository.CartRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import reactor.core.publisher.Mono;

@Service
@Validated
public class CartService {

    private final CartRepository cartRepo;
    private final CartItemRepository itemRepo;

    public CartService(CartRepository cartRepo, CartItemRepository itemRepo) {
        this.cartRepo = cartRepo;
        this.itemRepo = itemRepo;
    }

    /** Adjunta los ítems del carrito (en memoria) antes de responder. */
    private Mono<Cart> attachItems(Cart cart) {
        return itemRepo.findByCartId(cart.getId())
                .collectList()
                .map(list -> { cart.setItems(list); return cart; });
    }

    /** Obtiene el carrito activo; si no existe, lo crea (id nulo → INSERT). */
    public Mono<Cart> getActiveCart(String customerId) {
        return cartRepo.findFirstByCustomerIdAndCheckedOutFalseOrderByCreatedAtDesc(customerId)
                .switchIfEmpty(cartRepo.save(new Cart(customerId)))
                .flatMap(this::attachItems);
    }

    /** Agrega o incrementa un ítem. */
    public Mono<Cart> addItem(String customerId, @Valid AddItemRequest request) {
        return getActiveCart(customerId)
                .flatMap(cart ->
                        itemRepo.findByCartIdAndProductId(cart.getId(), request.getProductId())
                                .flatMap(existing -> {
                                    existing.setQuantity(existing.getQuantity() + request.getQuantity());
                                    return itemRepo.save(existing).thenReturn(cart);
                                })
                                .switchIfEmpty(
                                        Mono.defer(() -> {
                                            CartItem item = CartMapper.toItem(request); // id==null (INSERT)
                                            item.setCartId(cart.getId());               // set cartId aquí
                                            return itemRepo.save(item).thenReturn(cart);
                                        })
                                )
                                .then(Mono.fromRunnable(cart::touch))
                                .then(cartRepo.save(cart))
                                .flatMap(this::attachItems)
                );
    }

    /** Actualiza la cantidad de un ítem existente. */
    public Mono<Cart> updateQty(String customerId, @Valid UpdateQtyRequest request) {
        return getActiveCart(customerId)
                .flatMap(cart ->
                        itemRepo.findByCartIdAndProductId(cart.getId(), request.getProductId())
                                .switchIfEmpty(Mono.error(new IllegalArgumentException("Item not found in cart")))
                                .flatMap(item -> {
                                    item.setQuantity(request.getQuantity());
                                    return itemRepo.save(item);
                                })
                                .then(Mono.fromRunnable(cart::touch))
                                .then(cartRepo.save(cart))
                                .flatMap(this::attachItems)
                );
    }

    /** Elimina un ítem del carrito. */
    public Mono<Cart> removeItem(String customerId, String productId) {
        return getActiveCart(customerId)
                .flatMap(cart ->
                        itemRepo.deleteByCartIdAndProductId(cart.getId(), productId)
                                .then(Mono.fromRunnable(cart::touch))
                                .then(cartRepo.save(cart))
                                .flatMap(this::attachItems)
                );
    }

    /** Vacía el carrito. */
    public Mono<Cart> clear(String customerId) {
        return getActiveCart(customerId)
                .flatMap(cart ->
                        itemRepo.deleteByCartId(cart.getId())
                                .then(Mono.fromRunnable(cart::touch))
                                .then(cartRepo.save(cart))
                                .flatMap(this::attachItems)
                );
    }

    /** Marca el carrito como cerrado (checkout). */
    public Mono<Cart> checkout(String customerId) {
        return getActiveCart(customerId)
                .flatMap(cart -> {
                    cart.setCheckedOut(true);
                    cart.touch();
                    return cartRepo.save(cart);
                })
                .flatMap(this::attachItems);
    }
}
