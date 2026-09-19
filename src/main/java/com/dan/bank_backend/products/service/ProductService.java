package com.dan.bank_backend.products.service;

import com.dan.bank_backend.products.dtos.CreateProductRequestDTO;
import com.dan.bank_backend.products.dtos.ProductDTO;
import com.dan.bank_backend.products.entity.Product;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface ProductService {
    List<ProductDTO> getAll();
    ProductDTO findProductById(Long productId);
    List<ProductDTO> findProductByClientId(Long clientId);
    ProductDTO saveProduct(CreateProductRequestDTO request);
    void deleteProductBy(Long productId);
    void activateProduct(Long productId);
    void disableProduct(Long productId);
    void cancelProduct(Long productId);
    //Used for transactions
    Product getProductEntityById(Long productId);
    void verifyProductIsActive(Product product);
    void verifyFundsForTransaction(Product product, BigDecimal amount);
    void verifyAmountIsNotZero(BigDecimal amount);
    void addToBalance(Product productDTO,BigDecimal amount);
    void substractFromBalance(Product productDTO,BigDecimal amount);
}
