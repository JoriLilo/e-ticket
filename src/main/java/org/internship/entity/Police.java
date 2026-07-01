
package org.internship.entity;

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
@NoArgsConstructor
@AllArgsConstructor
public class Police extends User {

    @Column(unique = true)
    private String badgeId;

    @OneToMany(mappedBy = "issuedBy")
    private List<Fine> issuedFines;


    public Police(String firstName, String lastName, String username, String password, String badgeID, List<Fine> fines) {
        super(firstName, lastName, username, password);
        this.badgeId = badgeID;
        this.issuedFines = fines;
    }
}
