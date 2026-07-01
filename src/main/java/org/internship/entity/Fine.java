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
public class Fine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fineID;

    @Column(length = 160, nullable = false)
    private String reason;


    private Date date;

    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne
    @JoinColumn(name = "vehicle_id")
    private Vehicle vehicle;

    @ManyToOne
    @JoinColumn(name = "officer_id")
    private Police issuedBy;

    public Fine(String reason, Date date, Status status, Vehicle vehicle, Police officer) {
            this.reason = reason;
            this.status = status;
            this.vehicle = vehicle;
            this.issuedBy = officer;
            this.date= date;
    }
}
