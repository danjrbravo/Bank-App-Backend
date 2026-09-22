package com.dan.bank_backend.products;

import com.dan.bank_backend.clients.entity.Client;
import com.dan.bank_backend.clients.exception.ClientNotFoundException;
import com.dan.bank_backend.clients.repository.ClientRepository;
import com.dan.bank_backend.products.dtos.CreateProductRequestDTO;
import com.dan.bank_backend.products.dtos.ProductDTO;
import com.dan.bank_backend.products.entity.Product;
import com.dan.bank_backend.products.exceptions.ProductAccountNotActiveException;
import com.dan.bank_backend.products.exceptions.ProductNotFoundException;
import com.dan.bank_backend.products.mapper.ProductMapper;
import com.dan.bank_backend.products.model.AccountState;
import com.dan.bank_backend.products.model.AccountType;
import com.dan.bank_backend.products.repository.ProductRepository;
import com.dan.bank_backend.products.service.imp.ProductServiceImp;
import com.dan.bank_backend.transactions.exception.InsufficientFundsException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepo;

    @Mock
    private ClientRepository clientRepo;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private ProductServiceImp productService;


    // =========================
    // GET ALL PRODUCTS
    // =========================

    @Test
    void shouldGetAllProducts() {

        Product product = new Product();

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

        when(productRepo.findAll())
                .thenReturn(List.of(product));

        when(productMapper.toDTO(product))
                .thenReturn(productDTO);

        List<ProductDTO> result =
                productService.getAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(productDTO, result.get(0));

        verify(productRepo).findAll();
        verify(productMapper).toDTO(product);
    }


    // =========================
    // GET PRODUCT BY ID
    // =========================

    @Test
    void shouldFindProductById() {

        Product product = new Product();

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

        when(productRepo.findById(1L))
                .thenReturn(Optional.of(product));

        when(productMapper.toDTO(product))
                .thenReturn(productDTO);

        ProductDTO result =
                productService.findProductById(1L);

        assertNotNull(result);
        assertEquals(productDTO, result);

        verify(productRepo).findById(1L);
        verify(productMapper).toDTO(product);
    }


    @Test
    void shouldThrowExceptionWhenProductDoesNotExist() {

        when(productRepo.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(
                ProductNotFoundException.class,
                () -> productService.findProductById(1L)
        );

        verify(productRepo).findById(1L);
        verify(productMapper, never()).toDTO(any());
    }


    // =========================
    // GET PRODUCTS BY CLIENT
    // =========================

    @Test
    void shouldFindProductsByClientId() {

        Client client = new Client();
        Product product = new Product();

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

        when(clientRepo.findById(1L))
                .thenReturn(Optional.of(client));

        when(productRepo.findByClientId(1L))
                .thenReturn(List.of(product));

        when(productMapper.toDTO(product))
                .thenReturn(productDTO);

        List<ProductDTO> result =
                productService.findProductByClientId(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(productDTO, result.get(0));

        verify(clientRepo).findById(1L);
        verify(productRepo).findByClientId(1L);
        verify(productMapper).toDTO(product);
    }


    @Test
    void shouldThrowExceptionWhenClientDoesNotExist() {

        when(clientRepo.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(
                ClientNotFoundException.class,
                () -> productService.findProductByClientId(1L)
        );

        verify(clientRepo).findById(1L);
        verify(productRepo, never()).findByClientId(any());
    }


    // =========================
    // EXISTS BY CLIENT
    // =========================

    @Test
    void shouldCheckIfClientHasProducts() {

        when(productRepo.existsByClientId(1L))
                .thenReturn(true);

        boolean result =
                productService.existsByClientId(1L);

        assertTrue(result);

        verify(productRepo).existsByClientId(1L);
    }


    // =========================
    // CREATE PRODUCT
    // =========================

    @Test
    void shouldCreateSavingsProduct() {

        Client client = new Client();

        CreateProductRequestDTO request =
                new CreateProductRequestDTO(
                        AccountType.AHORROS,
                        1L,
                        BigDecimal.valueOf(100000),
                        false
                );

        Product product = new Product();

        ProductDTO expectedDTO = new ProductDTO(
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

        product.setAccountType(AccountType.AHORROS);
        product.setBalance(BigDecimal.valueOf(100000));

        when(clientRepo.findById(1L))
                .thenReturn(Optional.of(client));

        when(productMapper.toProduct(request, client))
                .thenReturn(product);

        when(productRepo.save(product))
                .thenReturn(product);

        when(productMapper.toDTO(product))
                .thenReturn(expectedDTO);

        ProductDTO result =
                productService.saveProduct(request);

        assertNotNull(result);
        assertEquals(expectedDTO, result);

        assertEquals(AccountState.ACTIVE, product.getProductState());
        assertNotNull(product.getAccountNumber());
        assertTrue(product.getAccountNumber().startsWith("53"));

        verify(clientRepo).findById(1L);
        verify(productMapper).toProduct(request, client);
        verify(productRepo).save(product);
        verify(productMapper).toDTO(product);
    }


    @Test
    void shouldCreateCurrentAccountAsInactive() {

        Client client = new Client();

        CreateProductRequestDTO request =
                new CreateProductRequestDTO(
                        AccountType.CORRIENTE,
                        1L,
                        BigDecimal.valueOf(100000),
                        false
                );

        Product product = new Product();

        ProductDTO expectedDTO = new ProductDTO(
                1L,
                AccountType.CORRIENTE,
                AccountState.INACTIVE,
                "3312345678",
                BigDecimal.valueOf(100000),
                false,
                null,
                null,
                1L
        );

        product.setAccountType(AccountType.CORRIENTE);
        product.setBalance(BigDecimal.valueOf(100000));

        when(clientRepo.findById(1L))
                .thenReturn(Optional.of(client));

        when(productMapper.toProduct(request, client))
                .thenReturn(product);

        when(productRepo.save(product))
                .thenReturn(product);

        when(productMapper.toDTO(product))
                .thenReturn(expectedDTO);

        ProductDTO result =
                productService.saveProduct(request);

        assertNotNull(result);
        assertEquals(expectedDTO, result);

        assertEquals(AccountState.INACTIVE, product.getProductState());
        assertNotNull(product.getAccountNumber());
        assertTrue(product.getAccountNumber().startsWith("33"));

        verify(productRepo).save(product);
    }


    @Test
    void shouldNotCreateProductWhenClientDoesNotExist() {

        CreateProductRequestDTO request =
                new CreateProductRequestDTO(
                        AccountType.AHORROS,
                        1L,
                        BigDecimal.valueOf(100000),
                        false
                );

        when(clientRepo.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(
                ClientNotFoundException.class,
                () -> productService.saveProduct(request)
        );

        verify(clientRepo).findById(1L);
        verify(productRepo, never()).save(any());
        verify(productMapper, never()).toProduct(any(), any());
    }


    @Test
    void shouldNotCreateProductWhenBalanceIsNull() {

        Client client = new Client();

        CreateProductRequestDTO request =
                new CreateProductRequestDTO(
                        AccountType.AHORROS,
                        1L,
                        null,
                        false
                );

        Product product = new Product();

        product.setAccountType(AccountType.AHORROS);
        product.setBalance(null);

        when(clientRepo.findById(1L))
                .thenReturn(Optional.of(client));

        when(productMapper.toProduct(request, client))
                .thenReturn(product);

        assertThrows(
                IllegalArgumentException.class,
                () -> productService.saveProduct(request)
        );

        verify(productRepo, never()).save(any());
    }


    @Test
    void shouldNotCreateProductWhenBalanceIsNegative() {

        Client client = new Client();

        CreateProductRequestDTO request =
                new CreateProductRequestDTO(
                        AccountType.AHORROS,
                        1L,
                        BigDecimal.valueOf(-100),
                        false
                );

        Product product = new Product();

        product.setAccountType(AccountType.AHORROS);
        product.setBalance(BigDecimal.valueOf(-100));

        when(clientRepo.findById(1L))
                .thenReturn(Optional.of(client));

        when(productMapper.toProduct(request, client))
                .thenReturn(product);

        assertThrows(
                IllegalArgumentException.class,
                () -> productService.saveProduct(request)
        );

        verify(productRepo, never()).save(any());
    }


    // =========================
    // DELETE PRODUCT
    // =========================

    @Test
    void shouldDeleteProduct() {

        when(productRepo.existsById(1L))
                .thenReturn(true);

        productService.deleteProductBy(1L);

        verify(productRepo).existsById(1L);
        verify(productRepo).deleteById(1L);
    }


    @Test
    void shouldNotDeleteProductWhenProductDoesNotExist() {

        when(productRepo.existsById(1L))
                .thenReturn(false);

        assertThrows(
                ProductNotFoundException.class,
                () -> productService.deleteProductBy(1L)
        );

        verify(productRepo).existsById(1L);
        verify(productRepo, never()).deleteById(any());
    }


    // =========================
    // ACTIVATE PRODUCT
    // =========================

    @Test
    void shouldActivateProduct() {

        Product product = new Product();
        product.setProductState(AccountState.INACTIVE);

        when(productRepo.findById(1L))
                .thenReturn(Optional.of(product));

        productService.activateProduct(1L);

        assertEquals(
                AccountState.ACTIVE,
                product.getProductState()
        );

        verify(productRepo).findById(1L);
        verify(productRepo).save(product);
    }


    @Test
    void shouldNotActivateAlreadyActiveProduct() {

        Product product = new Product();
        product.setProductState(AccountState.ACTIVE);

        when(productRepo.findById(1L))
                .thenReturn(Optional.of(product));

        assertThrows(
                RuntimeException.class,
                () -> productService.activateProduct(1L)
        );

        verify(productRepo, never()).save(any());
    }


    @Test
    void shouldNotActivateProductWhenProductDoesNotExist() {

        when(productRepo.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(
                ProductNotFoundException.class,
                () -> productService.activateProduct(1L)
        );

        verify(productRepo, never()).save(any());
    }


    // =========================
    // DISABLE PRODUCT
    // =========================

    @Test
    void shouldDisableProduct() {

        Product product = new Product();
        product.setProductState(AccountState.ACTIVE);

        when(productRepo.findById(1L))
                .thenReturn(Optional.of(product));

        productService.disableProduct(1L);

        assertEquals(
                AccountState.INACTIVE,
                product.getProductState()
        );

        verify(productRepo).save(product);
    }


    @Test
    void shouldNotDisableAlreadyInactiveProduct() {

        Product product = new Product();
        product.setProductState(AccountState.INACTIVE);

        when(productRepo.findById(1L))
                .thenReturn(Optional.of(product));

        assertThrows(
                RuntimeException.class,
                () -> productService.disableProduct(1L)
        );

        verify(productRepo, never()).save(any());
    }


    // =========================
    // CANCEL PRODUCT
    // =========================

    @Test
    void shouldCancelProduct() {

        Product product = new Product();
        product.setProductState(AccountState.ACTIVE);

        when(productRepo.findById(1L))
                .thenReturn(Optional.of(product));

        productService.cancelProduct(1L);

        assertEquals(
                AccountState.CANCELLED,
                product.getProductState()
        );

        verify(productRepo).save(product);
    }


    @Test
    void shouldNotCancelAlreadyCancelledProduct() {

        Product product = new Product();
        product.setProductState(AccountState.CANCELLED);

        when(productRepo.findById(1L))
                .thenReturn(Optional.of(product));

        assertThrows(
                RuntimeException.class,
                () -> productService.cancelProduct(1L)
        );

        verify(productRepo, never()).save(any());
    }


    // =========================
    // GET PRODUCT ENTITY
    // =========================

    @Test
    void shouldGetProductEntityById() {

        Product product = new Product();

        when(productRepo.findById(1L))
                .thenReturn(Optional.of(product));

        Product result =
                productService.getProductEntityById(1L);

        assertNotNull(result);
        assertEquals(product, result);

        verify(productRepo).findById(1L);
    }


    // =========================
    // VERIFY ACTIVE
    // =========================

    @Test
    void shouldVerifyProductIsActive() {

        Product product = new Product();

        product.setProductState(AccountState.ACTIVE);

        assertDoesNotThrow(
                () -> productService.verifyProductIsActive(product)
        );
    }


    @Test
    void shouldThrowExceptionWhenProductIsNotActive() {

        Product product = new Product();

        product.setProductState(AccountState.INACTIVE);

        assertThrows(
                ProductAccountNotActiveException.class,
                () -> productService.verifyProductIsActive(product)
        );
    }


    // =========================
    // VERIFY FUNDS
    // =========================

    @Test
    void shouldVerifyThereAreEnoughFunds() {

        Product product = new Product();

        product.setBalance(BigDecimal.valueOf(100000));

        assertDoesNotThrow(
                () -> productService.verifyFundsForTransaction(
                        product,
                        BigDecimal.valueOf(50000)
                )
        );
    }


    @Test
    void shouldThrowExceptionWhenThereAreNotEnoughFunds() {

        Product product = new Product();

        product.setBalance(BigDecimal.valueOf(10000));

        assertThrows(
                InsufficientFundsException.class,
                () -> productService.verifyFundsForTransaction(
                        product,
                        BigDecimal.valueOf(20000)
                )
        );
    }


    // =========================
    // VERIFY AMOUNT
    // =========================

    @Test
    void shouldVerifyAmountIsGreaterThanZero() {

        assertDoesNotThrow(
                () -> productService.verifyAmountIsNotZero(
                        BigDecimal.valueOf(100)
                )
        );
    }


    @Test
    void shouldThrowExceptionWhenAmountIsZero() {

        assertThrows(
                RuntimeException.class,
                () -> productService.verifyAmountIsNotZero(
                        BigDecimal.ZERO
                )
        );
    }


    @Test
    void shouldThrowExceptionWhenAmountIsNegative() {

        assertThrows(
                RuntimeException.class,
                () -> productService.verifyAmountIsNotZero(
                        BigDecimal.valueOf(-100)
                )
        );
    }


    // =========================
    // ADD BALANCE
    // =========================

    @Test
    void shouldAddAmountToBalance() {

        Product product = new Product();

        product.setBalance(BigDecimal.valueOf(100000));

        productService.addToBalance(
                product,
                BigDecimal.valueOf(50000)
        );

        assertEquals(
                BigDecimal.valueOf(150000),
                product.getBalance()
        );

        verify(productRepo).save(product);
    }


    // =========================
    // SUBTRACT BALANCE
    // =========================

    @Test
    void shouldSubtractAmountFromBalance() {

        Product product = new Product();

        product.setBalance(BigDecimal.valueOf(100000));

        productService.substractFromBalance(
                product,
                BigDecimal.valueOf(30000)
        );

        assertEquals(
                BigDecimal.valueOf(70000),
                product.getBalance()
        );

        verify(productRepo).save(product);
    }
}