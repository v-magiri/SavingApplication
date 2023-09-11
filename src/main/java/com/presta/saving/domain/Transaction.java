package com.presta.saving.domain;

import com.presta.saving.domain.Enums.PaymentMethod;
import com.presta.saving.domain.Enums.TransactionType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="tbl_transactions")
public class Transaction {
    @SequenceGenerator(
            name = "transaction_sequence",
            sequenceName = "transaction_sequence",
            allocationSize = 1
    )
    @GeneratedValue(strategy = GenerationType.AUTO,generator = "transaction_sequence")
    @Id
    private Long id;

    @Column(nullable = false,unique = true)
    private String transactionNumber;

    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime transactedAt;

    @Column(nullable = false)
    private double amount;

    @Column(nullable = false)
    private TransactionType transactionType;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    @ManyToOne
    private Account account;


}
