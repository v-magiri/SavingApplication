package com.presta.saving.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="tbl_customers")
public class Customer {
    @SequenceGenerator(
            name = "customer_sequence",
            sequenceName = "customer_sequence",
            allocationSize = 1
    )
    @GeneratedValue(strategy = GenerationType.AUTO,generator = "customer_sequence")
    @Id
    private Long id;
    @Column(nullable = false,unique = true)
    private String memberNumber;
    @Column(nullable = false)
    private String firstName;
    @Column(nullable = false)
    private String middleName;
    @Column(nullable = false)
    private String lastName;
    @Column(nullable = false,unique = true)
    private String idNumber;
    @Column(nullable = false)
    private String emailAddress;
    @Column(nullable = false)
    private String Address;

    @Column(nullable = false)
    private String phoneNumber;
    @OneToMany(mappedBy = "customer")
    private List<Account> savingsAccounts=new ArrayList<>();

    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private String createdBy;

    private LocalDateTime updateAt;

    private String updatedBy;

}
