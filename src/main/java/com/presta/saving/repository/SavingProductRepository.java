package com.presta.saving.repository;

import com.presta.saving.domain.SavingProduct;
import com.presta.saving.dto.SavingProductDto;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface SavingProductRepository extends CrudRepository<SavingProduct,Long> {
    SavingProduct findByProductName(String productName);

    boolean existsByProductName(String productName);

    SavingProduct findByProductId(String productId);

    SavingProductDto findSavingProductByProductId(String productId);

    SavingProductDto findSavingProductById(long id);

    List<SavingProductDto> findSavingProductByOrderByCreatedAt();
}
