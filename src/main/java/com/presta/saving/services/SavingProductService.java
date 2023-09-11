package com.presta.saving.services;

import com.presta.saving.domain.SavingProduct;
import com.presta.saving.dto.SavingProductDto;
import com.presta.saving.repository.SavingProductRepository;
import com.presta.saving.web.Request.ProductRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class SavingProductService {
    private final SavingProductRepository productRepository;

    public SavingProductService(SavingProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    //creating a  saving product
    public String createProduct(ProductRequest productRequest){
        try{
            SavingProduct product=new SavingProduct();
            product.setProductName(productRequest.getProductName());
            product.setDescription(productRequest.getDescription());
            product.setInterestRate(productRequest.getInterestRate());
            product.setCreatedAt(LocalDateTime.now());
            product.setMinimumOperatingBalance(product.getMinimumOperatingBalance());
            product.setCreatedBy("SYSTEM");

            int randNo = (int) ((Math.random() * (99999 - 1)) + 1);
            String  productL= String.format("%03d", randNo);
            String  productId="S-"+productL;

            product.setProductId(productId);

            SavingProduct newProduct=productRepository.save(product);
            return "Product created successfully with product Id " + newProduct.getProductId();

        }catch(Exception e){
            log.error(e.getMessage());
            throw  new IllegalStateException(e.getMessage());
        }
    }

    //updating a saving product
    public String updateProduct(ProductRequest productRequest,String productId){
        try {
            log.info("Updating Product");
            SavingProduct savingProduct=productRepository.findByProductId(productId);
            //update product Name
            if(productRequest.getProductName()!=null && productRequest.getProductName().length()>0 &&
                !productRequest.getProductName().equals(savingProduct.getProductName())) {
                savingProduct.setProductName(productRequest.getProductName());
            }
            //update product description
            if(productRequest.getDescription()!=null && productRequest.getDescription().length()>0 &&
                !productRequest.getDescription().equals(savingProduct.getDescription())){
                savingProduct.setDescription(productRequest.getDescription());
            }
            //update the minimum operating balance for a saving product
            if(productRequest.getMinimumOperatingBalance() >0 &&
                    (productRequest.getMinimumOperatingBalance() != savingProduct.getMinimumOperatingBalance())){
                savingProduct.setMinimumOperatingBalance(productRequest.getMinimumOperatingBalance());
            }

            //update the interest rate for a saving product
            if(productRequest.getInterestRate() >0 &&
                    (productRequest.getInterestRate() != savingProduct.getInterestRate())){
                savingProduct.setInterestRate(productRequest.getInterestRate());
            }

            //set the time that the product was updated
            savingProduct.setUpdatedBy("SYSTEM");
            savingProduct.setUpdateAt(LocalDateTime.now());

            productRepository.save(savingProduct);

            return "Product Updated Successfully";

        }catch (Exception e){
            log.error(e.getMessage());
            String updateErrorMessage=String.format("Update of Product %s Failed",productId);
            throw new IllegalStateException(updateErrorMessage);
        }
    }

    public String deleteProduct(String productId){
        try{
            SavingProduct savingProduct=productRepository.findByProductId(productId);
            productRepository.delete(savingProduct);
            return String.format("Deletion of product identitied by ID: %s Successful",productId);
        }catch (Exception e){
            log.error(e.getMessage());
            String deleteError=String.format("Deletion of product identified by ID: %s failed",productId);
            throw new IllegalStateException(deleteError);
        }
    }

    public List<SavingProductDto> getProducts(){
        return productRepository.findSavingProductByOrderByCreatedAt();
    }

    public SavingProductDto getProductDtoByProductId(String productId){
        return productRepository.findSavingProductByProductId(productId);
    }


}
