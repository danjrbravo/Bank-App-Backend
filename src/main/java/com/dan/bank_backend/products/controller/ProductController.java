package com.dan.bank_backend.products.controller;

import com.dan.bank_backend.products.dtos.CreateProductRequestDTO;
import com.dan.bank_backend.products.dtos.ProductDTO;
import com.dan.bank_backend.products.service.ProductService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/product")
public class ProductController {
    private final ProductService productService;

    @GetMapping
    public ResponseEntity<List<ProductDTO>> getAll(){
        return ResponseEntity.ok(productService.getAll());
    }
    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getById(@PathVariable Long id){
        return ResponseEntity.ok(productService.findProductById(id));
    }
    @GetMapping("/client/{id}")
    public ResponseEntity<List<ProductDTO>> getClientProductsById(@PathVariable Long id){
        return ResponseEntity.ok(productService.findProductByClientId(id));
    }
    @PostMapping
    public ResponseEntity<ProductDTO> createProduct(@Valid @RequestBody CreateProductRequestDTO productDTO){
        ProductDTO newProduct = productService.saveProduct(productDTO);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(newProduct);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProductById(@PathVariable Long id){
        productService.deleteProductBy(id);
        return ResponseEntity.noContent().build();
    }
    @PutMapping("/{id}/activate")
    public ResponseEntity<Void> activateProduct(@PathVariable Long id){
        productService.activateProduct(id);
        return ResponseEntity.noContent().build();
    }
    @PutMapping("/{id}/deactivate")
    public ResponseEntity<Void> disableProduct(@PathVariable Long id){
        productService.disableProduct(id);
        return ResponseEntity.noContent().build();
    }
    @PutMapping("/{id}/cancel")
    public ResponseEntity<Void> cancelProduct(@PathVariable Long id){
        productService.cancelProduct(id);
        return ResponseEntity.noContent().build();  
    }
}
