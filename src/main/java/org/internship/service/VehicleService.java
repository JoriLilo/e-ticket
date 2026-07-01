package org.internship.service;

import org.internship.repository.CitizenRepo;
import org.internship.repository.VehicleRepo;
import org.internship.entity.Citizen;
import org.internship.entity.Vehicle;

import java.util.List;

public class VehicleService {

     private final VehicleRepo vehicleRepo;
     private final CitizenRepo citizenRepo;
     public VehicleService(VehicleRepo vehicleRepo, CitizenRepo citizenRepo) {
         this.vehicleRepo = vehicleRepo;
         this.citizenRepo = citizenRepo;
     }


     public Vehicle registerVehicle(String NID, String plateNumber, String make, String model, int year, String color, Long citizenID) {
         Citizen owner = citizenRepo.findById(citizenID);
         if (owner == null) {
             throw new RuntimeException("Citizen not found");
         }
         Vehicle vehicle = new Vehicle(NID,plateNumber,make,model,year,color,owner,null);

         return vehicleRepo.save(vehicle);
     }

    public List<Vehicle> getVehiclesByCitizen(Long citizenID) {
        Citizen citizen = citizenRepo.findById(citizenID);
        if (citizen == null) {
            throw new IllegalArgumentException("No citizen found with id: " + citizenID);
        }
        return vehicleRepo.findByCitizen(citizen);
    }

    public Vehicle getVehicleByPlateNumber(String plateNumber) {
        Vehicle vehicle = vehicleRepo.findByPlateNumber(plateNumber);
        if (vehicle == null) {
            System.out.println("No vehicle found with plateNumber: " + plateNumber);
        }
        return vehicle;
    }


}
