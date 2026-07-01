package org.internship.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Citizen extends User {

    @Column(unique = true)
    private String passportID;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
    private List<Vehicle> vehicles;


    public Citizen(String firstName, String lastName, String username, String password, String passportID ,List<Vehicle> vehicles) {
        super(firstName,lastName,username,password);
        this.passportID = passportID;
        this.vehicles = vehicles;
    }
}