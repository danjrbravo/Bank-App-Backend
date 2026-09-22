package com.dan.bank_backend.products;

import com.dan.bank_backend.products.controller.ProductController;
import com.dan.bank_backend.products.dtos.CreateProductRequestDTO;
import com.dan.bank_backend.products.dtos.ProductDTO;
import com.dan.bank_backend.products.model.AccountState;
import com.dan.bank_backend.products.model.AccountType;
import com.dan.bank_backend.products.service.ProductService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductControllerTest {

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController productController;


    // =========================
    // GET /api/product
    // =========================

    @Test
    void shouldGetAllProducts() {

        ProductDTO productDTO = new ProductDTO(
                1L,
                AccountType.AHORROS,
                AccountState.ACTIVE,
                "5312345678",
                BigDecimal.valueOf(100000),
                false,
                null,
                null,
                1L
        );

        when(productService.getAll())
                .thenReturn(List.of(productDTO));


        ResponseEntity<List<ProductDTO>> response =
                productController.getAll();


        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals(productDTO, response.getBody().get(0));

        verify(productService).getAll();
    }


    // =========================
    // GET /api/product/{id}
    // =========================

    @Test
    void shouldGetProductById() {

        ProductDTO productDTO = new ProductDTO(
                1L,
                AccountType.AHORROS,
                AccountState.ACTIVE,
                "5312345678",
                BigDecimal.valueOf(100000),
                false,
                null,
                null,
                1L
        );

        when(productService.findProductById(1L))
                .thenReturn(productDTO);


        ResponseEntity<ProductDTO> response =
                productController.getById(1L);


        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(productDTO, response.getBody());

        verify(productService).findProductById(1L);
    }


    // =========================
    // GET /api/product/client/{id}
    // =========================

    @Test
    void shouldGetProductsByClientId() {

        ProductDTO productDTO = new ProductDTO(
                1L,
                AccountType.AHORROS,
                AccountState.ACTIVE,
                "5312345678",
                BigDecimal.valueOf(100000),
                false,
                null,
                null,
                1L
        );

        when(productService.findProductByClientId(1L))
                .thenReturn(List.of(productDTO));


        ResponseEntity<List<ProductDTO>> response =
                productController.getClientProductsById(1L);


        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals(productDTO, response.getBody().get(0));

        verify(productService).findProductByClientId(1L);
    }


    // =========================
    // POST /api/product
    // =========================

    @Test
    void shouldCreateProduct() {

        CreateProductRequestDTO request =
                new CreateProductRequestDTO(
                        AccountType.AHORROS,
                        1L,
                        BigDecimal.valueOf(100000),
                        false
                );

        ProductDTO productDTO = new ProductDTO(
                1L,
                AccountType.AHORROS,
                AccountState.ACTIVE,
                "5312345678",
                BigDecimal.valueOf(100000),
                false,
                null,
                null,
                1L
        );

        when(productService.saveProduct(request))
                .thenReturn(productDTO);


        ResponseEntity<ProductDTO> response =
                productController.createProduct(request);


        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(productDTO, response.getBody());

        verify(productService).saveProduct(request);
    }


    // =========================
    // DELETE /api/product/{id}
    // =========================

    @Test
    void shouldDeleteProduct() {

        doNothing()
                .when(productService)
                .deleteProductBy(1L);


        ResponseEntity<Void> response =
                productController.deleteProductById(1L);


        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());

        verify(productService).deleteProductBy(1L);
    }


    // =========================
    // PUT /api/product/{id}/activate
    // =========================

    @Test
    void shouldActivateProduct() {

        doNothing()
                .when(productService)
                .activateProduct(1L);


        ResponseEntity<Void> response =
                productController.activateProduct(1L);


        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());

        verify(productService).activateProduct(1L);
    }


    // =========================
    // PUT /api/product/{id}/deactivate
    // =========================

    @Test
    void shouldDeactivateProduct() {

        doNothing()
                .when(productService)
                .disableProduct(1L);


        ResponseEntity<Void> response =
                productController.disableProduct(1L);


        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());

        verify(productService).disableProduct(1L);
    }


    // =========================
    // PUT /api/product/{id}/cancel
    // =========================

    @Test
    void shouldCancelProduct() {

        doNothing()
                .when(productService)
                .cancelProduct(1L);


        ResponseEntity<Void> response =
                productController.cancelProduct(1L);


        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());

        verify(productService).cancelProduct(1L);
    }
}