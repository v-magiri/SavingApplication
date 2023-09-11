package com.presta.saving.web;

import com.presta.saving.domain.Enums.TransactionType;
import com.presta.saving.dto.RestResponse;
import com.presta.saving.repository.TransactionRepository;
import com.presta.saving.services.TransactionService;
import com.presta.saving.web.Request.TransactionRequest;
import com.presta.saving.web.Request.TransferRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/v1/transactions")
@Slf4j
public class TransactionResource {
    private final TransactionService transactionService;
    private final TransactionRepository transactionRepository;

    public TransactionResource(TransactionService transactionService,
                               TransactionRepository transactionRepository) {
        this.transactionService = transactionService;
        this.transactionRepository = transactionRepository;
    }

    @PostMapping("transfer")
    public ResponseEntity<?> transfer(@RequestBody TransactionRequest transactionRequest){
        try{
            if(transactionRequest != null){
                if(transactionRequest.getTransactionType().equals(TransactionType.DEPOSIT)){
                    String message= transactionService.createCreditTransaction(transactionRequest);
                    return new ResponseEntity<>(new RestResponse(false,message),HttpStatus.OK);
                }else{
                    String message=transactionService.createDebitTransaction(transactionRequest);
                    return new ResponseEntity<>(new RestResponse(false,message),HttpStatus.OK);
                }
            }else {
                return new ResponseEntity<>(new RestResponse(true,"Request can not be Null"),HttpStatus.BAD_REQUEST);
            }
        }catch (Exception e){
            String message=String.format("Error occurred while conducting the transaction");
            log.error(e.getMessage());
            return new ResponseEntity<>(new RestResponse(true, message), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("customerTransfer")
    public ResponseEntity<?> transferFundBetweenCustomers(@RequestBody TransferRequest transferRequest){
        try{
            if(transferRequest!=null){
                String message=transactionService.transactBetweenVariousAccount(transferRequest);
                return new ResponseEntity<>(new RestResponse(false,message),HttpStatus.OK);
            }else{
                return new ResponseEntity<>(new RestResponse(true,"Request can not be Null"),HttpStatus.OK);
            }
        }catch (Exception e){
            String message="Could not complete Transaction";
            log.error(e.getMessage());
            return new ResponseEntity<>(new RestResponse(true, message), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

//    @GetMapping()
//    public ResponseEntity<?> fetchAllTransaction(){
//
//    }
//
//    @GetMapping(path = "{customerID}")
//    public ResponseEntity<?> fetchCustomerTransaction(){
//
//    }
//
//    @GetMapping(path ="/between/date")
//    public ResponseEntity<?> fetchTransactionsByDate(@RequestParam("startDate") String startDate,
//                                                     @RequestParam("endDate") String endDate){
//
//    }
//
//    @GetMapping(path = "filter/date")
//    public ResponseEntity<?> filterTransactionByDate(){
//
//    }
}
