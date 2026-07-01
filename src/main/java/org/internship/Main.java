package org.internship;

import org.internship.entity.*;
import org.internship.repository.*;
import org.internship.service.*;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.Date;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpa-example-pu");

        CitizenRepo citizenRepo = new CitizenRepo(emf);
        PoliceRepo policeRepo = new PoliceRepo(emf);
        VehicleRepo vehicleRepo = new VehicleRepo(emf);
        FineRepo fineRepo = new FineRepo(emf);
        PaymentRepo paymentRepo = new PaymentRepo(emf);

        CitizenService citizenService = new CitizenService(citizenRepo);
        PoliceService policeService = new PoliceService(policeRepo);
        VehicleService vehicleService = new VehicleService(vehicleRepo, citizenRepo);
        FineService fineService = new FineService(fineRepo, paymentRepo);

        try {
            // 1. Create two officers
            Police officer1 = policeService.createPolice("Arben", "Krasniqi", "akrasniqi", "pass123", "BADGE-001");
            Police officer2 = policeService.createPolice("Elira", "Hoxha", "ehoxha", "pass123", "BADGE-002");
            System.out.println("Created officers: " + officer1.getBadgeId() + ", " + officer2.getBadgeId());

            // 2. Create three citizens
            Citizen citizen1 = citizenService.createCitizen("Jori", "Rama", "jrama", "pass123", "PASS-001");
            Citizen citizen2 = citizenService.createCitizen("Dea", "Berisha", "dberisha", "pass123", "PASS-002");
            Citizen citizen3 = citizenService.createCitizen("Klaus", "Meta", "kmeta", "pass123", "PASS-003");
            System.out.println("Created citizens: " + citizen1.getPassportID() + ", "
                    + citizen2.getPassportID() + ", " + citizen3.getPassportID());

            // 3. Register vehicles for citizens
            Vehicle vehicle1 = vehicleService.registerVehicle("NID-001", "AA111BB", "Toyota", "Corolla", 2019, "White", citizen1.getId());
            Vehicle vehicle2 = vehicleService.registerVehicle("NID-002", "AA222CC", "VW", "Golf", 2020, "Black", citizen2.getId());
            Vehicle vehicle3 = vehicleService.registerVehicle("NID-003", "AA333DD", "BMW", "320i", 2018, "Blue", citizen3.getId());
            System.out.println("Registered vehicles: " + vehicle1.getPlateNumber() + ", "
                    + vehicle2.getPlateNumber() + ", " + vehicle3.getPlateNumber());

            // 4. Create multiple fines
            Fine fine1 = fineService.issueFine(vehicle1, officer1, "Illegal parking", new Date());
            Fine fine2 = fineService.issueFine(vehicle2, officer2, "Speeding", new Date());
            Fine fine3 = fineService.issueFine(vehicle3, officer1, "Running a red light", new Date());
            System.out.println("Created fines: " + fine1.getFineID() + ", " + fine2.getFineID() + ", " + fine3.getFineID());

            // 5. Print all fines
            System.out.println("\n--- All fines ---");
            printFines(fineService.getAllFines());

            // 6. Search fines by citizen
            System.out.println("\n--- Fines for citizen1 ---");
            printFines(fineService.getFinesByCitizen(citizen1));

            // 7. Search fines by plate number
            System.out.println("\n--- Fines for plate " + vehicle2.getPlateNumber() + " ---");
            printFines(fineService.getFinesByPlateNumber(vehicle2.getPlateNumber()));

            // 8. Update one fine reason
            fineService.updateFineReason(fine1, "Illegal parking near school zone");
            System.out.println("\nUpdated fine1 reason to: " + fineRepo.findById(fine1.getFineID()).getReason());

            // 9. Pay one fine
            Payment payment = fineService.payFine(fine2.getFineID(), 50.0);
            System.out.println("\nPaid fine2, payment id: " + payment.getPaymentId()
                    + ", fine status: " + fineRepo.findById(fine2.getFineID()).getStatus());

            // 10. Try to pay the same fine again
            System.out.println("\n--- Attempting to pay fine2 again ---");
            try {
                fineService.payFine(fine2.getFineID(), 50.0);
                System.out.println("BUG: second payment was allowed, this should not happen");
            } catch (IllegalStateException e) {
                System.out.println("Expected failure: " + e.getMessage());
            }

            // 11. Cancel one unpaid fine
            fineService.cancelFine(fine3);
            System.out.println("\nCancelled fine3, status: " + fineRepo.findById(fine3.getFineID()).getStatus());

            // 12. Try to pay the cancelled fine
            System.out.println("\n--- Attempting to pay cancelled fine3 ---");
            try {
                fineService.payFine(fine3.getFineID(), 50.0);
                System.out.println("BUG: payment on a cancelled fine was allowed, this should not happen");
            } catch (IllegalStateException e) {
                System.out.println("Expected failure: " + e.getMessage());
            }

            // 13. Print final fine statuses
            System.out.println("\n--- Final fine statuses ---");
            printFines(fineService.getAllFines());

        } finally {
            // 14. Shutdown Hibernate
            emf.close();
            System.out.println("\nEntityManagerFactory closed.");
        }
    }

    private static void printFines(List<Fine> fines) {
        if (fines == null || fines.isEmpty()) {
            System.out.println("No fines to display.");
            return;
        }
        for (Fine f : fines) {
            System.out.println("Fine #" + f.getFineID()
                    + " | reason: " + f.getReason()
                    + " | status: " + f.getStatus()
                    + " | plate: " + f.getVehicle().getPlateNumber()
                    + " | officer: " + f.getIssuedBy().getBadgeId());
        }
    }
}