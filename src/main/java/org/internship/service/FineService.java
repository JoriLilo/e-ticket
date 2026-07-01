package org.internship.service;

import org.internship.repository.FineRepo;
import org.internship.repository.PaymentRepo;
import org.internship.entity.*;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public class FineService {
    private final FineRepo fineRepo;
    private final PaymentRepo paymentRepo;

    public FineService(FineRepo fineRepo, PaymentRepo paymentRepo) {
        this.fineRepo = fineRepo;
        this.paymentRepo = paymentRepo;
    }

    public Fine issueFine(Vehicle vehicle, Police officer, String reason, Date date) {

        Fine fine = new Fine(reason, date, Status.UNPAID,vehicle,officer);
        return fineRepo.save(fine);

    }

    public List<Fine> getAllFines() {
        List<Fine> fines = fineRepo.findAll();
        if (fines.isEmpty()) {
            System.out.println("No fines found");
        }
        return fines;
    }

    public List<Fine> getFinesByCitizen(Citizen citizen) {
        List<Fine> fines = fineRepo.findByCitizen(citizen);
        if (fines.isEmpty()) {
            System.out.println("No fine found for citizen: " + citizen);
        }
        return fines;
    }

    public List<Fine> getFinesByPlateNumber(String plateNumber) {
        List<Fine> fines = fineRepo.findByPlateNumber(plateNumber);
        if (fines.isEmpty()) {
            System.out.println("This vehicle has no fines");
        }
        return fines;
    }

    public Fine updateFineReason(Fine fine, String newReason) {
        fine.setReason(newReason);
        return fineRepo.save(fine);
    }

    public Payment payFine(Long fineId, Double amount){
        Fine fine =  fineRepo.findById(fineId);
        if (fine == null ) {
            throw new IllegalStateException("This fine does not exist");
        }else if (fine.getStatus().equals(Status.PAID)){
            throw new IllegalStateException("This fine has already paid");

        }else if (fine.getStatus().equals(Status.CANCELLED)){
            throw new IllegalStateException("This fine has already cancelled");
        }else {

            Payment payment = new Payment(LocalDate.now(), amount, fine);
            fine.setStatus(Status.PAID);
            fineRepo.save(fine);
            return paymentRepo.save(payment);
        }

    }

    public Fine cancelFine(Fine fine) {
        if (fine.getStatus() != Status.UNPAID) {
            throw new IllegalStateException("Only unpaid fines can be cancelled. Current status: " + fine.getStatus());
        }
        fine.setStatus(Status.CANCELLED);
        return fineRepo.save(fine);
    }
}
