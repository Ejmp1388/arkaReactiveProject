package com.msvc.shopcart.web;
import com.msvc.shopcart.domain.Cart;
import com.msvc.shopcart.dto.AddItemRequest;
import com.msvc.shopcart.dto.CheckoutResponse;
import com.msvc.shopcart.dto.RemoveItemRequest;
import com.msvc.shopcart.dto.UpdateQtyRequest;
import com.msvc.shopcart.service.CartService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/carts")
@Validated
public class CartController {

    private final CartService service;

    public CartController(CartService service) {
        this.service = service;
    }

    @GetMapping("/{customerId}")
    public Mono<Cart> getCart(@PathVariable String customerId) {
        return service.getActiveCart(customerId);
    }

    @PostMapping("/{customerId}/items")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Cart> addItem(@PathVariable String customerId, @Valid @RequestBody AddItemRequest request) {
        return service.addItem(customerId, request);
    }

    @PutMapping("/{customerId}/items")
    public Mono<Cart> updateQuantity(@PathVariable String customerId, @Valid @RequestBody UpdateQtyRequest request) {
        return service.updateQty(customerId, request);
    }

    @DeleteMapping("/{customerId}/items")
    public Mono<Cart> removeItem(@PathVariable String customerId, @Valid @RequestBody RemoveItemRequest request) {
        return service.removeItem(customerId, request.getProductId());
    }

    @DeleteMapping("/{customerId}")
    public Mono<Cart> clear(@PathVariable String customerId) {
        return service.clear(customerId);
    }

    @PostMapping("/{customerId}/checkout")
    public Mono<CheckoutResponse> checkout(@PathVariable String customerId) {
        return service.checkout(customerId)
                // Aquí orderId podría venir de un evento/respuesta; por ahora null.
                .map(cart -> new CheckoutResponse(cart, null));
    }
}
