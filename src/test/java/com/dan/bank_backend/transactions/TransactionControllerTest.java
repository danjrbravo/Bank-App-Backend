package com.dan.bank_backend.transactions;

import com.dan.bank_backend.transactions.Model.TransactionType;
import com.dan.bank_backend.transactions.controller.TransactionController;
import com.dan.bank_backend.transactions.dtos.ConsignTransactionRequestDTO;
import com.dan.bank_backend.transactions.dtos.TransactionDTO;
import com.dan.bank_backend.transactions.dtos.TransferTransactionRequestDTO;
import com.dan.bank_backend.transactions.dtos.WithdrawTransactionRequestDTO;
import com.dan.bank_backend.transactions.service.TransactionService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionControllerTest {

    @Mock
    private TransactionService transactionService;

    @InjectMocks
    private TransactionController transactionController;


    // =========================================================
    // GET ALL
    // =========================================================

    @Test
    void shouldGetAllTransactions() {

        TransactionDTO transactionDTO = new TransactionDTO(
                1L,
                TransactionType.CONSIGNACION,
                BigDecimal.valueOf(100000),
                null,
                10L,
                LocalDateTime.now()
        );

        when(transactionService.getAllTransactions())
                .thenReturn(List.of(transactionDTO));


        ResponseEntity<List<TransactionDTO>> response =
                transactionController.getAll();


        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals(transactionDTO, response.getBody().get(0));

        verify(transactionService).getAllTransactions();
    }


    @Test
    void shouldReturnEmptyListWhenThereAreNoTransactions() {

        when(transactionService.getAllTransactions())
                .thenReturn(List.of());


        ResponseEntity<List<TransactionDTO>> response =
                transactionController.getAll();


        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty());

        verify(transactionService).getAllTransactions();
    }


    // =========================================================
    // GET BY ID
    // =========================================================

    @Test
    void shouldGetTransactionById() {

        TransactionDTO transactionDTO = new TransactionDTO(
                1L,
                TransactionType.RETIRO,
                BigDecimal.valueOf(50000),
                10L,
                null,
                LocalDateTime.now()
        );

        when(transactionService.getTransactionById(1L))
                .thenReturn(transactionDTO);


        ResponseEntity<TransactionDTO> response =
                transactionController.getById(1L);


        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(transactionDTO, response.getBody());

        verify(transactionService).getTransactionById(1L);
    }


    // =========================================================
    // CONSIGN
    // =========================================================

    @Test
    void shouldConsignTransaction() {

        ConsignTransactionRequestDTO request =
                new ConsignTransactionRequestDTO(
                        TransactionType.CONSIGNACION,
                        BigDecimal.valueOf(100000),
                        10L
                );

        TransactionDTO transactionDTO = new TransactionDTO(
                1L,
                TransactionType.CONSIGNACION,
                BigDecimal.valueOf(100000),
                null,
                10L,
                LocalDateTime.now()
        );

        when(transactionService.consign(request))
                .thenReturn(transactionDTO);


        ResponseEntity<TransactionDTO> response =
                transactionController.consign(request);


        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(transactionDTO, response.getBody());

        verify(transactionService).consign(request);
    }


    // =========================================================
    // WITHDRAW
    // =========================================================

    @Test
    void shouldWithdrawTransaction() {

        WithdrawTransactionRequestDTO request =
                new WithdrawTransactionRequestDTO(
                        TransactionType.RETIRO,
                        BigDecimal.valueOf(50000),
                        10L
                );

        TransactionDTO transactionDTO = new TransactionDTO(
                1L,
                TransactionType.RETIRO,
                BigDecimal.valueOf(50000),
                10L,
                null,
                LocalDateTime.now()
        );

        when(transactionService.withdraw(request))
                .thenReturn(transactionDTO);


        ResponseEntity<TransactionDTO> response =
                transactionController.withdraw(request);


        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(transactionDTO, response.getBody());

        verify(transactionService).withdraw(request);
    }


    // =========================================================
    // TRANSFER
    // =========================================================

    @Test
    void shouldTransferTransaction() {

        TransferTransactionRequestDTO request =
                new TransferTransactionRequestDTO(
                        TransactionType.TRANSFERENCIA,
                        BigDecimal.valueOf(75000),
                        10L,
                        20L
                );

        TransactionDTO transactionDTO = new TransactionDTO(
                1L,
                TransactionType.TRANSFERENCIA,
                BigDecimal.valueOf(75000),
                10L,
                20L,
                LocalDateTime.now()
        );

        when(transactionService.transfer(request))
                .thenReturn(transactionDTO);


        ResponseEntity<TransactionDTO> response =
                transactionController.transfer(request);


        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(transactionDTO, response.getBody());

        verify(transactionService).transfer(request);
    }
}

