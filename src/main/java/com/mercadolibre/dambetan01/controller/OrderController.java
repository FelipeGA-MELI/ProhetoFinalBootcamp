package com.mercadolibre.dambetan01.controller;

import com.mercadolibre.dambetan01.dtos.purchase.CreatePurchaseOrderDTO;
import com.mercadolibre.dambetan01.dtos.purchase.EditPurchaseOrderDTO;
import com.mercadolibre.dambetan01.model.user.EPermission;
import com.mercadolibre.dambetan01.service.IPurchaseOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1/fresh-products")
public class OrderController {
    private final IPurchaseOrderService purchaseOrderService;

    @PostMapping("/orders")
    @PreAuthorize("hasAuthority('" + EPermission.Constants.BUY_PRODUCT_PERMISSION + "')")
    public ResponseEntity<?> createPurchaseOrder(@RequestBody @Valid CreatePurchaseOrderDTO createPurchaseOrderDTO, Authentication authentication) {
        Long buyerId = Long.parseLong((String)authentication.getPrincipal());
        return new ResponseEntity<>(purchaseOrderService.createPurchaseOrder(createPurchaseOrderDTO, buyerId), HttpStatus.CREATED);
    }

    @PutMapping("/orders/{purchaseOrderId}")
    @PreAuthorize("hasAuthority('" + EPermission.Constants.EDIT_PURCHASE_ORDER_PERMISSION + "')")
    public ResponseEntity<?> editPurchaseOrder(@PathVariable Long purchaseOrderId,
                                               @RequestBody @Valid EditPurchaseOrderDTO editPurchaseOrderDTO,
                                               Authentication authentication
    ) {
        Long buyerId = Long.parseLong((String)authentication.getPrincipal());
        return new ResponseEntity<>(purchaseOrderService.editPurchaseOrder(editPurchaseOrderDTO, purchaseOrderId, buyerId), HttpStatus.OK);
    }

    @GetMapping("orders/{purchaseOrderId}")
    @PreAuthorize("hasAuthority('" + EPermission.Constants.GET_PRODUCT_PURCHASE_ORDER_PERMISSION  + "')")
    public ResponseEntity<?> getOrderById(@PathVariable Long purchaseOrderId, Authentication authentication){
        Long buyerId = Long.parseLong((String)authentication.getPrincipal());
        return ResponseEntity.status(HttpStatus.OK).body(purchaseOrderService.getOrderById(purchaseOrderId, buyerId));
    }
}
