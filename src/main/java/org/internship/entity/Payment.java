package org.internship.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long paymentId;


    private LocalDate date;

    private Double amount;

    @OneToOne
    @JoinColumn(name = "fine_id", unique = true)
    private Fine fine;

    public Payment(LocalDate date, Double amount, Fine fine) {
        this.date = date;
        this.amount = amount;
        this.fine = fine;
    }
}

