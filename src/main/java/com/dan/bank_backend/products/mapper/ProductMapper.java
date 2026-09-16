package com.dan.bank_backend.products.mapper;

import com.dan.bank_backend.clients.entity.Client;
import com.dan.bank_backend.products.dtos.CreateProductRequestDTO;
import com.dan.bank_backend.products.dtos.ProductDTO;
import com.dan.bank_backend.products.entity.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {
    public ProductDTO toDTO(Product product){
        return new ProductDTO(
                product.getId(),
                product.getAccountType(),
                product.getProductState(),
                product.getAccountNumber(),
                product.getBalance(),
                product.isGmfExempt(),
                product.getCreatedAt(),
                product.getUpdatedAt(),
                product.getClient().getId()
                );
    }
    public Product toProduct(CreateProductRequestDTO dto, Client client){
        Product product = new Product();
        product.setAccountType(dto.accountType());
        product.setClient(client);
        product.setBalance(dto.balance());
        product.setGmfExempt(dto.gmfExempt());
        return product;
    }
}
