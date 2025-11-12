package com.msvc.orders.web;


import com.msvc.orders.domain.ApproveOrderRequest;
import com.msvc.orders.dto.OrderResponse;
import com.msvc.orders.dto.UpdateOrderItemRequest;
import com.msvc.orders.mapper.OrderMapper;
import com.msvc.orders.repository.OrderItemRepository;
import com.msvc.orders.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final OrderService service;
    private final OrderItemRepository itemRepo;


    public OrderController(OrderService service, OrderItemRepository itemRepo) {
        this.service = service;
        this.itemRepo = itemRepo;
    }

    @PostMapping("/from-cart/{cartId}")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<OrderResponse> createFromCart(@PathVariable String cartId) {
        return service.createFromConfirmedCart(cartId)
                .flatMap(order -> itemRepo.findByOrderId(order.getId()).collectList()
                        .map(items -> OrderMapper.toOrderResponse(order, items)));
    }


    @GetMapping("/{orderId}")
    public Mono<OrderResponse> getById(@PathVariable String orderId) {
        return service.getById(orderId)
                .flatMap(order -> itemRepo.findByOrderId(order.getId()).collectList()
                        .map(items -> OrderMapper.toOrderResponse(order, items)));
    }

    @PatchMapping("/{orderId}/items/{itemId}")
    public Mono<OrderResponse> updateItem(@PathVariable String orderId,
                                          @PathVariable String itemId,
                                          @RequestBody UpdateOrderItemRequest req) {
        return service.updateOrderItem(orderId, itemId,
                        req.getQuantity(), req.getUnitPrice())
                .flatMap(order -> itemRepo.findByOrderId(order.getId()).collectList()
                        .map(items -> OrderMapper.toOrderResponse(order, items)));
    }

    @DeleteMapping("/{orderId}/items/{itemId}")
    public Mono<OrderResponse> deleteItem(@PathVariable String orderId,
                                          @PathVariable String itemId) {
        return service.deleteOrderItem(orderId, itemId)
                .flatMap(order -> itemRepo.findByOrderId(order.getId()).collectList()
                        .map(items -> OrderMapper.toOrderResponse(order, items)));
    }

    @PatchMapping("/{id}/approve")
    public Mono<ResponseEntity<String>> approveOrder(
            @PathVariable String id,
            @RequestBody(required = false) ApproveOrderRequest request
    ) {
        return service.approve(id, request)
                .thenReturn(ResponseEntity.ok("Orden " + id + " aprobada y correo enviado"));
    }



}
