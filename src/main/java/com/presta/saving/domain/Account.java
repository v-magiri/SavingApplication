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
@Table(name="tbl_accounts")
public class Account {
    @SequenceGenerator(
            name = "account_sequence",
            sequenceName = "account_sequence",
            allocationSize = 1
    )
    @GeneratedValue(strategy = GenerationType.AUTO,generator = "account_sequence")
    @Id
    private Long id;

    @Column(nullable = false)
    private String accountNumber;

    @ManyToOne
    private Customer customer;

    @ManyToOne
    private SavingProduct savingsProduct;

    private double balance;

    @OneToMany(mappedBy = "account")
    private List<Transaction> transactions;

    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private String createdBy;

}
