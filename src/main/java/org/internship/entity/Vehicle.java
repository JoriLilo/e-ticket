package org.internship.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Vehicle {
    @Id
    private String NID;
    private String plateNumber;
    private String make;
    private String model;
    private int year;
    private String color;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private Citizen owner;

    @OneToMany(mappedBy = "vehicle", cascade = CascadeType.ALL)
    private List<Fine> fines;


}
