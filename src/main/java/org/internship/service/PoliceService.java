package org.internship.service;

import org.internship.repository.PoliceRepo;
import org.internship.entity.Police;

import java.util.List;

public class PoliceService {

    private final PoliceRepo policeRepo;

    public PoliceService(PoliceRepo policeRepo) {
        this.policeRepo = policeRepo;
    }

    public Police createPolice( String firstName, String lastName, String username,
                                String password, String badgeID) {

        Police police= new Police(firstName,lastName,username,password ,badgeID, null);
        return policeRepo.save(police);
    }

    public List<Police> getAllPolices() {
        List<Police> polices = policeRepo.findAll();
        if (polices.isEmpty()) {
            System.out.println("No polices found");
        }
        return polices;
    }

}
