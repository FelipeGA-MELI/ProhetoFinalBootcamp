package com.mercadolibre.dambetan01.controller;

import com.mercadolibre.dambetan01.dtos.ProductListDTO;
import com.mercadolibre.dambetan01.dtos.ProductDTO;
import com.mercadolibre.dambetan01.model.Product;
import com.mercadolibre.dambetan01.model.user.EPermission;
import com.mercadolibre.dambetan01.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/fresh-products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService){
        this.productService = productService;
    }

    @GetMapping("/")
    @PreAuthorize("hasAuthority('" + EPermission.Constants.LIST_ALL_PRODUCT_PERMISSION  + "')")
    public List<ProductListDTO> findAllProductsList(){
        return  productService.findAllProductsList();
    }

    @GetMapping("/list")
   @PreAuthorize("hasAuthority('" + EPermission.Constants.LIST_ALL_PRODUCT_PER_CATEGORY_PERMISSION  + "')")
    public List<ProductListDTO> findAllProductsListCategory(@RequestParam String category){
        return  productService.findAllProductsListByCategory(category);
    }

    @PostMapping("/register")
    @PreAuthorize("permitAll()")
    public ResponseEntity registerProduct(@RequestBody @Valid ProductDTO productDTO) {
        ProductDTO product = productService.resgisterProduct(productDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(product);
    }

    @PutMapping("/update/{productId}")
    @PreAuthorize("permitAll()")
    public ResponseEntity updateProduct(@RequestBody @Valid ProductDTO productDTO,@PathVariable Long productId) {
        ProductDTO product = productService.updateProduct(productDTO, productId);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(product);
    }
}
