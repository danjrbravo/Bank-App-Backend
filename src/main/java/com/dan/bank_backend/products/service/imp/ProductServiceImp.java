package com.dan.bank_backend.products.service.imp;

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
import com.dan.bank_backend.products.service.ProductService;
import com.dan.bank_backend.transactions.exception.InsufficientFundsException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Service
public class ProductServiceImp implements ProductService {
    private final ProductRepository productRepo;
    private final ClientRepository clientRepo;
    private final ProductMapper productMapper;
    public ProductServiceImp(ProductRepository productRepo,ClientRepository clientRepo, ProductMapper productMapper){
        this.productRepo = productRepo;
        this.clientRepo = clientRepo;
        this.productMapper = productMapper;
    }
    @Override
    public List<ProductDTO> getAll() {
        return productRepo.findAll()
                .stream()
                .map(productMapper::toDTO)
                .toList();
    }

    @Override
    public ProductDTO findProductById(Long productId) {
        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException());
        return productMapper.toDTO(product);
    }

    @Override
    public List<ProductDTO> findProductByClientId(Long clientId) {
        clientRepo.findById(clientId).
                orElseThrow(() -> new ClientNotFoundException(clientId));
        return productRepo.findByClientId(clientId)
                .stream()
                .map(productMapper::toDTO)
                .toList();
    }

    @Override
    public ProductDTO saveProduct(CreateProductRequestDTO request) {
        Client client = clientRepo.findById(request.clientId())
                .orElseThrow(() -> new ClientNotFoundException(request.clientId()));
        Product newproduct = productMapper.toProduct(request,client);
        newproduct.setAccountNumber(generateAccountNumber(request.accountType()));
        newproduct.setCreatedAt(LocalDateTime.now());
        newproduct.setUpdatedAt(LocalDateTime.now());
        if(newproduct.getAccountType() == AccountType.AHORROS){
            newproduct.setProductState(AccountState.ACTIVE);
        }else{
            newproduct.setProductState(AccountState.INACTIVE);
        }
        validateAccount(newproduct);
        productRepo.save(newproduct);
        return productMapper.toDTO(newproduct);
    }

    @Override
    public void deleteProductBy(Long productId) {
        if(!productRepo.existsById(productId)){
            throw new ProductNotFoundException();
        }
        productRepo.deleteById(productId);
    }

    @Override
    public void activateProduct(Long productId) {
        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException());
        if(product.getProductState().equals(AccountState.ACTIVE)){
            throw new RuntimeException("Product has already been activated");
        }
        product.setProductState(AccountState.ACTIVE);
        product.setUpdatedAt(LocalDateTime.now());
        productRepo.save(product);
    }

    @Override
    public void disableProduct(Long productId) {
        Product product = productRepo.findById(productId)
                .orElseThrow(()-> new ProductNotFoundException());
        if (product.getProductState().equals(AccountState.INACTIVE)){
            throw new RuntimeException("Product has already been inactive");
        }
        product.setProductState(AccountState.INACTIVE);
        product.setUpdatedAt(LocalDateTime.now());
        productRepo.save(product);
    }

    @Override
    public void cancelProduct(Long productId) {
        Product product = productRepo.findById(productId)
                .orElseThrow(()-> new ProductNotFoundException());
        if (product.getProductState().equals(AccountState.CANCELLED)){
            throw new RuntimeException("Product has already been cancelled");
        }
        product.setProductState(AccountState.CANCELLED);
        product.setUpdatedAt(LocalDateTime.now());
        productRepo.save(product);
    }

    @Override
    public Product getProductEntityById(Long productId) {
        return productRepo.findById(productId).orElseThrow(() -> new ProductNotFoundException());
    }

    @Override
    public void verifyProductIsActive(Product product){
        if(!product.getProductState().equals(AccountState.ACTIVE)){
            throw new ProductAccountNotActiveException();
        }
    }
    @Override
    public void verifyFundsForTransaction(Product product,BigDecimal amount){
        if(product.getBalance().subtract(amount).compareTo(BigDecimal.ZERO) < 0 ){
            throw new InsufficientFundsException();
        }
    }
    @Override
    public void verifyAmountIsNotZero(BigDecimal amount){
        if(!(amount.compareTo(BigDecimal.ZERO) > 0)){
            throw new RuntimeException("Amount must be greater than 0");
        }
    }

    @Override
    public void addToBalance(Product product, BigDecimal amount) {
        product.setBalance(product.getBalance().add(amount));
        product.setUpdatedAt(LocalDateTime.now());
        productRepo.save(product);
    }

    @Override
    public void substractFromBalance(Product product,BigDecimal amount) {
        product.setBalance(product.getBalance().subtract(amount));
        product.setUpdatedAt(LocalDateTime.now());
        productRepo.save(product);
    }

    private String generateAccountNumber(AccountType type){
        Random random = new Random();
        int number = 10000000 + random.nextInt(90000000);
        return (type == AccountType.AHORROS? "53":"33") + number;
    }
    private void validateAccount(Product product){
        if(product.getBalance() == null){
            throw new IllegalArgumentException("Balance cannot be null");
        }
        if(product.getBalance().compareTo(BigDecimal.ZERO) < 0){
            throw new IllegalArgumentException("Balance cannot be less than 0");
        }

    }

}
