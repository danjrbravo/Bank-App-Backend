package com.dan.bank_backend.products.service;

import com.dan.bank_backend.products.dtos.CreateProductRequestDTO;
import com.dan.bank_backend.products.dtos.ProductDTO;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    List<ProductDTO> getAll();
    ProductDTO findProductById(Long productId);
    List<ProductDTO> findProductByClientId(Long clientId);
    ProductDTO saveProduct(CreateProductRequestDTO request);
    void deleteProductBy(Long productId);
}
