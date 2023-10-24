package com.pocketpay.transaction.entity;

import com.pocketpay.transaction.enums.Status;
import com.pocketpay.transaction.enums.TransferType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "transfer")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private BigDecimal amountSent;
    private BigDecimal amountReceived;
    private BigDecimal transferRate;
    private String paymentPurpose;
    private String fromCurrency;
    private String toCurrency;
    @Enumerated(EnumType.STRING)
    private TransferType transferType;
    @Enumerated(EnumType.STRING)
    private Status status;
    @ManyToOne
    @JoinColumn(name = "payment_method_id")
    private PaymentMethod paymentMethod;
    @ManyToOne
    @JoinColumn(name = "recipient_id")
    private Recipient recipient;
    @Column(name = "user_id")
    private int userId;
}
