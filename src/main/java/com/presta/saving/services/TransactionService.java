package com.presta.saving.services;

import com.presta.saving.domain.Account;
import com.presta.saving.domain.Enums.TransactionType;
import com.presta.saving.domain.Transaction;
import com.presta.saving.dto.AccountDto;
import com.presta.saving.repository.AccountRepository;
import com.presta.saving.repository.CustomerRepository;
import com.presta.saving.repository.SavingProductRepository;
import com.presta.saving.repository.TransactionRepository;
import com.presta.saving.web.Request.TransactionRequest;
import com.presta.saving.web.Request.TransferRequest;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
public class TransactionService {
    private final TransactionRepository transactionRepository;

    private final AccountRepository accountRepository;
    private final SavingProductRepository productRepository;
    private final CustomerRepository customerRepository;

    public TransactionService(TransactionRepository transactionRepository,
                              AccountRepository accountRepository,
                              SavingProductRepository productRepository,
                              CustomerRepository customerRepository) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
        this.productRepository = productRepository;
        this.customerRepository = customerRepository;
    }

    @Transactional
    public String createCreditTransaction(TransactionRequest transactionRequest){
        try{
            String accountNumber=transactionRequest.getAccountNumber();
            boolean isTransactionComplete=creditAccount(accountNumber,transactionRequest.getAmount());
            if(isTransactionComplete){
                Account account=accountRepository.findAccountByAccountNumber(accountNumber);
                Transaction transaction=new Transaction();
                transaction.setTransactedAt(LocalDateTime.now());
                transaction.setAmount(transactionRequest.getAmount());
                transaction.setPaymentMethod(transactionRequest.getPaymentMethod());
                String transactionNumber=generateTransactionNumber();
                transaction.setTransactionNumber(transactionNumber);
                transaction.setTransactionType(transactionRequest.getTransactionType());
                transaction.setAccount(account);

                transactionRepository.save(transaction);
                return "Transaction Successful";
            }else{
                return "Could not Complete Transaction";
            }
        }catch (Exception e){
            log.error(e.getMessage());
            throw new IllegalStateException("Could not complete transaction");
        }
    }

    @Transactional
    public String createDebitTransaction(TransactionRequest transactionRequest){
        try{
            if(isTransactionValid(transactionRequest.getAccountNumber(),
                    transactionRequest.getAmount())){
                String accountNumber=transactionRequest.getAccountNumber();
                AccountDto accountDto=accountRepository.getAccountByAccountNumber(accountNumber);
                boolean isTransactionComplete=debitAccount(accountNumber,transactionRequest.getAmount());
                if(isTransactionComplete){
                    Account account=accountRepository.findAccountByAccountNumber(accountNumber);
                    Transaction transaction=new Transaction();
                    transaction.setTransactedAt(LocalDateTime.now());
                    transaction.setAmount(transactionRequest.getAmount());
                    transaction.setPaymentMethod(transactionRequest.getPaymentMethod());
                    String transactionNumber=generateTransactionNumber();
                    transaction.setTransactionNumber(transactionNumber);
                    transaction.setAccount(account);
                    transaction.setTransactionType(transactionRequest.getTransactionType());

                    transactionRepository.save(transaction);
                    return "Transaction Successful";
                }else {
                    String message=String.format("Please define a lower amount the %s saving has a" +
                            "minimum operating balance of %s",accountDto.getSavingsProduct().getProductName(),
                            accountDto.getSavingsProduct().getMinimumOperatingBalance());
                    throw new IllegalStateException(message);
                }
            }else{
                throw new IllegalStateException("Insufficient Funds in Account");
            }
        }catch (Exception e){
            log.error(e.getMessage());
            throw new IllegalStateException("Could not complete transaction");
        }
    }

    //check if transaction is valid if the account balance is greater than
    private boolean isTransactionValid(String accountNumber,double debitAmount){
        boolean isTransactionAllowed=false;

        AccountDto accountDto=accountRepository.getAccountByAccountNumber(accountNumber);
        if(accountDto.getBalance() > debitAmount){
            double accountBalance=accountDto.getBalance();
            isTransactionAllowed=true;
        }
        return isTransactionAllowed;
    }

    //generate a transaction Number
    private String generateTransactionNumber(){
        int randNo = (int) ((Math.random() * (99999 - 1)) + 1);
        String  transactionL= String.format("%06d", randNo);
        return transactionL+String.valueOf(System.currentTimeMillis()).substring(0,6);
    }

    @Transactional
    //transact between two accounts
    public String transactBetweenVariousAccount(TransferRequest transferRequest){
        try{
            String senderAccount=transferRequest.getTransferAccountNumber();
            double amount=transferRequest.getAmount();
            String receiverAccount=transferRequest.getReceiverAccountNumber();
            if(isTransactionValid(senderAccount,amount)){
                boolean isDebitComplete=debitAccount(senderAccount,amount);
                if(isDebitComplete){
                    Account account=accountRepository.findAccountByAccountNumber(senderAccount);
                    Transaction debitTransaction=new Transaction();
                    debitTransaction.setTransactedAt(LocalDateTime.now());
                    debitTransaction.setAmount(amount);
                    debitTransaction.setPaymentMethod(transferRequest.getPaymentMethod());
                    String debitTransactionNumber=generateTransactionNumber();
                    debitTransaction.setTransactionNumber(debitTransactionNumber);
                    debitTransaction.setTransactionType(TransactionType.BANK_TRANSFER);
                    debitTransaction.setAccount(account);
                    transactionRepository.save(debitTransaction);

                    boolean isCreditComplete=creditAccount(receiverAccount,amount);
                    if(isCreditComplete){
                        Account beneficiallyAccount=accountRepository.findAccountByAccountNumber(receiverAccount);
                        Transaction creditTransaction=new Transaction();
                        creditTransaction.setTransactedAt(LocalDateTime.now());
                        creditTransaction.setAmount(amount);
                        creditTransaction.setPaymentMethod(transferRequest.getPaymentMethod());
                        String transactionNumber=generateTransactionNumber();
                        creditTransaction.setTransactionNumber(transactionNumber);
                        creditTransaction.setTransactionType(TransactionType.BANK_TRANSFER);
                        creditTransaction.setAccount(beneficiallyAccount);

                        transactionRepository.save(creditTransaction);
                    }
                }
                return "Transaction completed Successfully";
            }else{
                throw new IllegalStateException("Insufficient Funds in account");
            }
        }catch (Exception e){
            log.error(e.getMessage());
            throw new IllegalStateException("Could not complete Transaction.Please try again later");
        }
    }

    @Transactional
    private boolean debitAccount(String accountNumber,double amount){
        try{
            AccountDto accountDto=accountRepository.getAccountByAccountNumber(accountNumber);
            double accountBalance=accountDto.getBalance();
            double accountMinimumOperatingBalance=accountDto.getSavingsProduct().getMinimumOperatingBalance();
            boolean isTransactionComplete=false;
            accountBalance=accountBalance-amount;
            if(accountBalance>accountMinimumOperatingBalance){
                Account account=accountRepository.findAccountByAccountNumber(accountNumber);
                account.setBalance(accountBalance);
                accountRepository.save(account);

                isTransactionComplete=true;
            }
            return isTransactionComplete;
        }catch (Exception e){
            log.error(e.getMessage());
            throw new IllegalStateException("Could not Complete Transaction.Try Again Later");
        }
    }

    @Transactional
    private boolean creditAccount(String accountNumber,double amount){
        try{
            AccountDto accountDto=accountRepository.getAccountByAccountNumber(accountNumber);
            double accountBalance=accountDto.getBalance();
            double accountMinimumOperatingBalance=accountDto.getSavingsProduct().getMinimumOperatingBalance();
            boolean isTransactionComplete=false;
            accountBalance=accountBalance+amount;
            if(accountBalance>accountMinimumOperatingBalance){
                Account account=accountRepository.findAccountByAccountNumber(accountNumber);
                account.setBalance(accountBalance);
                accountRepository.save(account);

                isTransactionComplete=true;
            }
            return isTransactionComplete;
        }catch (Exception e){
            log.error(e.getMessage());
            throw new IllegalStateException("Could not Complete Transaction.Try Again Later");
        }
    }


}
