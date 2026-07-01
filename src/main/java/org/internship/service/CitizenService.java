package org.internship.service;

import org.internship.repository.CitizenRepo;
import org.internship.entity.Citizen;

import java.util.List;

public class CitizenService {

    private final CitizenRepo citizenRepo;

    public CitizenService(CitizenRepo citizenRepo) {
        this.citizenRepo = citizenRepo;
    }

    public Citizen createCitizen(String firstName, String lastName, String username,
                                 String password, String passportID) {
        Citizen citizen = new Citizen(firstName, lastName, username, password, passportID, null);
        return citizenRepo.save(citizen);
    }

    public Citizen getCitizenById(Long id) {
        return citizenRepo.findById(id);
    }

    public List<Citizen> getAllCitizens() {
        return citizenRepo.findAll();
    }
}
