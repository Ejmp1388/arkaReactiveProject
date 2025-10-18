package com.msvc.shopcart.mapper;


import com.msvc.shopcart.domain.CartItem;
import com.msvc.shopcart.dto.AddItemRequest;

public final class CartMapper {
    private CartMapper() {}

    /** Crea un CartItem a partir del request.
     *  - id: null (lo genera la BD)
     *  - cartId: lo setea el servicio cuando conozca el cart.getId()
     */
    public static CartItem toItem(AddItemRequest req) {
        CartItem item = new CartItem();
        item.setProductId(req.getProductId());
        item.setName(req.getName());
        item.setQuantity(req.getQuantity());
        item.setUnitPriceCents(req.getUnitPriceCents());
        return item;
    }
}
