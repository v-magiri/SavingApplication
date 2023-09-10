package com.presta.saving.web;

import com.presta.saving.domain.SavingProduct;
import com.presta.saving.dto.RestResponse;
import com.presta.saving.dto.SavingProductDto;
import com.presta.saving.repository.SavingProductRepository;
import com.presta.saving.services.SavingProductService;
import com.presta.saving.web.Request.ProductRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/products")
public class SavingProductResource {
    private final static String PRODUCT_ALREADY_EXISTS="Product  with product name %s already exists";
    private final SavingProductRepository productRepository;
    private final SavingProductService productService;

    public SavingProductResource(SavingProductRepository productRepository, SavingProductService productService) {
        this.productRepository = productRepository;
        this.productService = productService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createProduct(@RequestBody ProductRequest productRequest){
        try{
            if(productRepository.existsByProductName(productRequest.getProductName())){
                String message=String.format(PRODUCT_ALREADY_EXISTS,productRequest.getProductName());
                return new ResponseEntity<>(new RestResponse(true,message),HttpStatus.BAD_REQUEST);
            }else{
                String message=productService.createProduct(productRequest);
                return new ResponseEntity<>(new RestResponse(false,message),HttpStatus.CREATED);
            }
        }catch(Exception e){
            return new ResponseEntity<>(new RestResponse(true,e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
    @GetMapping
    public ResponseEntity<?> getAllProducts(){
        List<SavingProductDto> productDtoList=productRepository.findSavingProductByOrderByCreatedAt();
        return new ResponseEntity<>(productDtoList,HttpStatus.OK);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<?> getProductById(@PathVariable long id){
        try{
            SavingProductDto savingProductDto= productRepository.findSavingProductById(id);
            if(savingProductDto!=null){
                return new ResponseEntity<>(savingProductDto,HttpStatus.OK);
            }else{
                return new ResponseEntity<>(new RestResponse(true, "Product Does not exist"),HttpStatus.BAD_REQUEST);
            }
        }catch (Exception e){
            String message=String.format("An error Occurred while fetching Product identified by ID: %s", id);
            log.error(e.getMessage());
            return new ResponseEntity<>(new RestResponse(true, message), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(path = "/filter-by-productID")
    public ResponseEntity<?> filterByProductId(@RequestParam("productID") String productId){
        try{
            SavingProductDto savingProductDto= productRepository.findSavingProductByProductId(productId);
            if(savingProductDto!=null){
                return new ResponseEntity<>(savingProductDto,HttpStatus.OK);
            }else{
                return new ResponseEntity<>(new RestResponse(true, "Product Does not exist"),HttpStatus.BAD_REQUEST);
            }
        }catch (Exception e){
            String message=String.format("An error Occurred while fetching Product identified by ID: %s", productId);
            log.error(e.getMessage());
            return new ResponseEntity<>(new RestResponse(true, message), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PutMapping(path = "/update")
    public ResponseEntity<?> updateProduct(@RequestParam("productID") String productId, @RequestBody ProductRequest productRequest){
        try{
            SavingProductDto savingProductDto= productRepository.findSavingProductByProductId(productId);
            if(savingProductDto!=null){
                String message=productService.updateProduct(productRequest,productId);
                return new ResponseEntity<>(new RestResponse(false,message),HttpStatus.OK);
            }else{
                return new ResponseEntity<>(new RestResponse(true,"Product does not Exist"),HttpStatus.BAD_REQUEST);
            }
        }catch (Exception e){
            String message=String.format("An error Occurred while Updating Product identified by ID: %s", productId);
            log.error(e.getMessage());
            return new ResponseEntity<>(new RestResponse(true, message), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping(path = "/delete")
    public ResponseEntity<?> deleteProduct(@RequestParam("productID")  String productId){
        try{
            String message=productService.deleteProduct(productId);
            return new ResponseEntity<>(new RestResponse(false,message),HttpStatus.OK);
        }catch (Exception e){
            String message=String.format("Error occurred while deleting Product %s",productId);
            log.error(e.getMessage());
            return new ResponseEntity<>(new RestResponse(true, message), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
