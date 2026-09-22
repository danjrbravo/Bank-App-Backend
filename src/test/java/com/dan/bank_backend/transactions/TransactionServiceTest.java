package com.dan.bank_backend.transactions;

import com.dan.bank_backend.products.entity.Product;
import com.dan.bank_backend.products.service.ProductService;

import com.dan.bank_backend.transactions.Model.TransactionType;
import com.dan.bank_backend.transactions.dtos.ConsignTransactionRequestDTO;
import com.dan.bank_backend.transactions.dtos.TransactionDTO;
import com.dan.bank_backend.transactions.dtos.TransferTransactionRequestDTO;
import com.dan.bank_backend.transactions.dtos.WithdrawTransactionRequestDTO;
import com.dan.bank_backend.transactions.entity.Transaction;
import com.dan.bank_backend.transactions.exception.TransactionNotFoundException;
import com.dan.bank_backend.transactions.mapper.TransactionMapper;
import com.dan.bank_backend.transactions.repository.TransactionRepository;
import com.dan.bank_backend.transactions.service.imp.TransactionServiceImp;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepo;

    @Mock
    private ProductService productService;

    @Mock
    private TransactionMapper transactionMapper;

    @InjectMocks
    private TransactionServiceImp transactionService;


    // =========================================================
    // GET ALL TRANSACTIONS
    // =========================================================

    @Test
    void shouldGetAllTransactions() {

        Transaction transaction = new Transaction();

        TransactionDTO transactionDTO = new TransactionDTO(
                1L,
                TransactionType.CONSIGNACION,
                BigDecimal.valueOf(100000),
                null,
                10L,
                LocalDateTime.now()
        );

        when(transactionRepo.findAll())
                .thenReturn(List.of(transaction));

        when(transactionMapper.toDTO(transaction))
                .thenReturn(transactionDTO);


        List<TransactionDTO> result =
                transactionService.getAllTransactions();


        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(transactionDTO, result.get(0));

        verify(transactionRepo).findAll();
        verify(transactionMapper).toDTO(transaction);
    }


    @Test
    void shouldReturnEmptyListWhenThereAreNoTransactions() {

        when(transactionRepo.findAll())
                .thenReturn(List.of());


        List<TransactionDTO> result =
                transactionService.getAllTransactions();


        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(transactionRepo).findAll();
        verifyNoInteractions(transactionMapper);
    }


    // =========================================================
    // GET TRANSACTION BY ID
    // =========================================================

    @Test
    void shouldGetTransactionById() {

        Transaction transaction = new Transaction();

        TransactionDTO transactionDTO = new TransactionDTO(
                1L,
                TransactionType.RETIRO,
                BigDecimal.valueOf(50000),
                10L,
                null,
                LocalDateTime.now()
        );

        when(transactionRepo.findById(1L))
                .thenReturn(Optional.of(transaction));

        when(transactionMapper.toDTO(transaction))
                .thenReturn(transactionDTO);


        TransactionDTO result =
                transactionService.getTransactionById(1L);


        assertNotNull(result);
        assertEquals(transactionDTO, result);

        verify(transactionRepo).findById(1L);
        verify(transactionMapper).toDTO(transaction);
    }


    @Test
    void shouldThrowExceptionWhenTransactionDoesNotExist() {

        when(transactionRepo.findById(1L))
                .thenReturn(Optional.empty());


        assertThrows(
                TransactionNotFoundException.class,
                () -> transactionService.getTransactionById(1L)
        );


        verify(transactionRepo).findById(1L);
        verify(transactionMapper, never()).toDTO(any());
    }


    // =========================================================
    // CONSIGN
    // =========================================================

    @Test
    void shouldConsignMoney() {

        BigDecimal amount = BigDecimal.valueOf(100000);

        ConsignTransactionRequestDTO request =
                new ConsignTransactionRequestDTO(
                        TransactionType.CONSIGNACION,
                        amount,
                        10L
                );

        Product destinyProduct = new Product();
        Transaction transaction = new Transaction();

        TransactionDTO expectedDTO = new TransactionDTO(
                1L,
                TransactionType.CONSIGNACION,
                amount,
                null,
                10L,
                LocalDateTime.now()
        );

        when(productService.getProductEntityById(10L))
                .thenReturn(destinyProduct);

        when(transactionMapper.toEntity(request, destinyProduct))
                .thenReturn(transaction);

        when(transactionRepo.save(transaction))
                .thenReturn(transaction);

        when(transactionMapper.toDTO(transaction))
                .thenReturn(expectedDTO);


        TransactionDTO result =
                transactionService.consign(request);


        assertNotNull(result);
        assertEquals(expectedDTO, result);
        assertNotNull(transaction.getTransactionDate());

        verify(productService).verifyAmountIsNotZero(amount);
        verify(productService).getProductEntityById(10L);
        verify(productService).verifyProductIsActive(destinyProduct);
        verify(transactionMapper).toEntity(request, destinyProduct);
        verify(productService).addToBalance(destinyProduct, amount);
        verify(transactionRepo).save(transaction);
        verify(transactionMapper).toDTO(transaction);
    }


    @Test
    void shouldNotConsignWhenAmountIsInvalid() {

        BigDecimal amount = BigDecimal.ZERO;

        ConsignTransactionRequestDTO request =
                new ConsignTransactionRequestDTO(
                        TransactionType.CONSIGNACION,
                        amount,
                        10L
                );

        doThrow(new RuntimeException())
                .when(productService)
                .verifyAmountIsNotZero(amount);


        assertThrows(
                RuntimeException.class,
                () -> transactionService.consign(request)
        );


        verify(productService).verifyAmountIsNotZero(amount);
        verify(productService, never()).getProductEntityById(any());
        verify(transactionRepo, never()).save(any());
    }


    @Test
    void shouldNotConsignWhenProductIsInactive() {

        BigDecimal amount = BigDecimal.valueOf(100000);

        ConsignTransactionRequestDTO request =
                new ConsignTransactionRequestDTO(
                        TransactionType.CONSIGNACION,
                        amount,
                        10L
                );

        Product destinyProduct = new Product();

        when(productService.getProductEntityById(10L))
                .thenReturn(destinyProduct);

        doThrow(new RuntimeException())
                .when(productService)
                .verifyProductIsActive(destinyProduct);


        assertThrows(
                RuntimeException.class,
                () -> transactionService.consign(request)
        );


        verify(productService).verifyAmountIsNotZero(amount);
        verify(productService).getProductEntityById(10L);
        verify(productService).verifyProductIsActive(destinyProduct);
        verify(transactionRepo, never()).save(any());
    }


    // =========================================================
    // WITHDRAW
    // =========================================================

    @Test
    void shouldWithdrawMoney() {

        BigDecimal amount = BigDecimal.valueOf(50000);

        WithdrawTransactionRequestDTO request =
                new WithdrawTransactionRequestDTO(
                        TransactionType.RETIRO,
                        amount,
                        10L
                );

        Product originProduct = new Product();
        Transaction transaction = new Transaction();

        TransactionDTO expectedDTO = new TransactionDTO(
                1L,
                TransactionType.RETIRO,
                amount,
                10L,
                null,
                LocalDateTime.now()
        );

        when(productService.getProductEntityById(10L))
                .thenReturn(originProduct);

        when(transactionMapper.toEntity(request, originProduct))
                .thenReturn(transaction);

        when(transactionRepo.save(transaction))
                .thenReturn(transaction);

        when(transactionMapper.toDTO(transaction))
                .thenReturn(expectedDTO);


        TransactionDTO result =
                transactionService.withdraw(request);


        assertNotNull(result);
        assertEquals(expectedDTO, result);
        assertNotNull(transaction.getTransactionDate());

        verify(productService).verifyAmountIsNotZero(amount);
        verify(productService).getProductEntityById(10L);
        verify(productService).verifyProductIsActive(originProduct);
        verify(productService).verifyFundsForTransaction(
                originProduct,
                amount
        );
        verify(productService).substractFromBalance(
                originProduct,
                amount
        );
        verify(transactionMapper).toEntity(request, originProduct);
        verify(transactionRepo).save(transaction);
        verify(transactionMapper).toDTO(transaction);
    }


    @Test
    void shouldNotWithdrawWhenAmountIsInvalid() {

        BigDecimal amount = BigDecimal.ZERO;

        WithdrawTransactionRequestDTO request =
                new WithdrawTransactionRequestDTO(
                        TransactionType.RETIRO,
                        amount,
                        10L
                );

        doThrow(new RuntimeException())
                .when(productService)
                .verifyAmountIsNotZero(amount);


        assertThrows(
                RuntimeException.class,
                () -> transactionService.withdraw(request)
        );


        verify(productService).verifyAmountIsNotZero(amount);
        verify(productService, never()).getProductEntityById(any());
        verify(transactionRepo, never()).save(any());
    }


    @Test
    void shouldNotWithdrawWhenThereAreInsufficientFunds() {

        BigDecimal amount = BigDecimal.valueOf(500000);

        WithdrawTransactionRequestDTO request =
                new WithdrawTransactionRequestDTO(
                        TransactionType.RETIRO,
                        amount,
                        10L
                );

        Product originProduct = new Product();

        when(productService.getProductEntityById(10L))
                .thenReturn(originProduct);

        doThrow(new RuntimeException())
                .when(productService)
                .verifyFundsForTransaction(
                        originProduct,
                        amount
                );


        assertThrows(
                RuntimeException.class,
                () -> transactionService.withdraw(request)
        );


        verify(productService).verifyAmountIsNotZero(amount);
        verify(productService).getProductEntityById(10L);
        verify(productService).verifyProductIsActive(originProduct);
        verify(productService).verifyFundsForTransaction(
                originProduct,
                amount
        );

        verify(productService, never())
                .substractFromBalance(any(), any());

        verify(transactionRepo, never()).save(any());
    }


    // =========================================================
    // TRANSFER
    // =========================================================

    @Test
    void shouldTransferMoney() {

        BigDecimal amount = BigDecimal.valueOf(75000);

        TransferTransactionRequestDTO request =
                new TransferTransactionRequestDTO(
                        TransactionType.TRANSFERENCIA,
                        amount,
                        10L,
                        20L
                );

        Product originProduct = new Product();
        Product destinyProduct = new Product();

        Transaction transaction = new Transaction();

        TransactionDTO expectedDTO = new TransactionDTO(
                1L,
                TransactionType.TRANSFERENCIA,
                amount,
                10L,
                20L,
                LocalDateTime.now()
        );

        when(productService.getProductEntityById(10L))
                .thenReturn(originProduct);

        when(productService.getProductEntityById(20L))
                .thenReturn(destinyProduct);

        when(transactionMapper.toEntity(
                request,
                originProduct,
                destinyProduct
        )).thenReturn(transaction);

        when(transactionRepo.save(transaction))
                .thenReturn(transaction);

        when(transactionMapper.toDTO(transaction))
                .thenReturn(expectedDTO);


        TransactionDTO result =
                transactionService.transfer(request);


        assertNotNull(result);
        assertEquals(expectedDTO, result);
        assertNotNull(transaction.getTransactionDate());

        verify(productService).verifyAmountIsNotZero(amount);

        verify(productService)
                .getProductEntityById(10L);

        verify(productService)
                .getProductEntityById(20L);

        verify(productService)
                .verifyProductIsActive(originProduct);

        verify(productService)
                .verifyProductIsActive(destinyProduct);

        verify(productService)
                .verifyFundsForTransaction(
                        originProduct,
                        amount
                );

        verify(productService)
                .substractFromBalance(
                        originProduct,
                        amount
                );

        verify(productService)
                .addToBalance(
                        destinyProduct,
                        amount
                );

        verify(transactionMapper)
                .toEntity(
                        request,
                        originProduct,
                        destinyProduct
                );

        verify(transactionRepo).save(transaction);

        verify(transactionMapper).toDTO(transaction);
    }


    @Test
    void shouldNotTransferWhenAmountIsInvalid() {

        BigDecimal amount = BigDecimal.ZERO;

        TransferTransactionRequestDTO request =
                new TransferTransactionRequestDTO(
                        TransactionType.TRANSFERENCIA,
                        amount,
                        10L,
                        20L
                );

        doThrow(new RuntimeException())
                .when(productService)
                .verifyAmountIsNotZero(amount);


        assertThrows(
                RuntimeException.class,
                () -> transactionService.transfer(request)
        );


        verify(productService)
                .verifyAmountIsNotZero(amount);

        verify(productService, never())
                .getProductEntityById(any());

        verify(transactionRepo, never())
                .save(any());
    }


    @Test
    void shouldNotTransferWhenOriginProductHasInsufficientFunds() {

        BigDecimal amount = BigDecimal.valueOf(500000);

        TransferTransactionRequestDTO request =
                new TransferTransactionRequestDTO(
                        TransactionType.TRANSFERENCIA,
                        amount,
                        10L,
                        20L
                );

        Product originProduct = new Product();
        Product destinyProduct = new Product();

        when(productService.getProductEntityById(10L))
                .thenReturn(originProduct);

        when(productService.getProductEntityById(20L))
                .thenReturn(destinyProduct);

        doThrow(new RuntimeException())
                .when(productService)
                .verifyFundsForTransaction(
                        originProduct,
                        amount
                );


        assertThrows(
                RuntimeException.class,
                () -> transactionService.transfer(request)
        );


        verify(productService)
                .verifyAmountIsNotZero(amount);

        verify(productService)
                .getProductEntityById(10L);

        verify(productService)
                .getProductEntityById(20L);

        verify(productService)
                .verifyProductIsActive(originProduct);

        verify(productService)
                .verifyProductIsActive(destinyProduct);

        verify(productService)
                .verifyFundsForTransaction(
                        originProduct,
                        amount
                );

        verify(productService, never())
                .substractFromBalance(any(), any());

        verify(productService, never())
                .addToBalance(any(), any());

        verify(transactionRepo, never())
                .save(any());
    }


    @Test
    void shouldNotTransferWhenOriginProductIsInactive() {

        BigDecimal amount = BigDecimal.valueOf(75000);

        TransferTransactionRequestDTO request =
                new TransferTransactionRequestDTO(
                        TransactionType.TRANSFERENCIA,
                        amount,
                        10L,
                        20L
                );

        Product originProduct = new Product();
        Product destinyProduct = new Product();

        when(productService.getProductEntityById(10L))
                .thenReturn(originProduct);

        when(productService.getProductEntityById(20L))
                .thenReturn(destinyProduct);

        doThrow(new RuntimeException())
                .when(productService)
                .verifyProductIsActive(originProduct);


        assertThrows(
                RuntimeException.class,
                () -> transactionService.transfer(request)
        );


        verify(productService)
                .verifyAmountIsNotZero(amount);

        verify(productService)
                .getProductEntityById(10L);

        verify(productService)
                .getProductEntityById(20L);

        verify(productService)
                .verifyProductIsActive(originProduct);

        verify(productService, never())
                .verifyFundsForTransaction(any(), any());

        verify(transactionRepo, never())
                .save(any());
    }
}