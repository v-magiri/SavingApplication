package com.presta.saving.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="tbl_saving_products")
public class SavingProduct {
    @SequenceGenerator(
            name = "product_sequence",
            sequenceName = "product_sequence",
            allocationSize = 1
    )
    @GeneratedValue(strategy = GenerationType.AUTO,generator = "product_sequence")
    @Id
    private Long id;

    @Column(nullable = false,unique = true)
    private String productName;

    @Column(nullable = false)
    private double minimumOperatingBalance;

    @Column(nullable = false)
    private double interestRate;

    @Column(nullable = false,unique = true)
    private String productId;

    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "savingsProduct")
    private List<Account> accounts;

    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private String createdBy;

    @Column(name = "updatedAt")
    private LocalDateTime updateAt;
    @Column(name = "updatedBy")
    private String updatedBy;

}
