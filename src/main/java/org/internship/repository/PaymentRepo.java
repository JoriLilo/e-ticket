package org.internship.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import org.internship.entity.Fine;
import org.internship.entity.Payment;

public class PaymentRepo {

    private final EntityManagerFactory emf;

    public PaymentRepo(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public Payment save(Payment payment) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            if (payment.getPaymentId() == null) {
                em.persist(payment);
            } else {
                payment = em.merge(payment);
            }
            tx.commit();
            return payment;
        } catch (RuntimeException e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    public Payment findById(Long id) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.find(Payment.class, id);
        } finally {
            em.close();
        }
    }

    public Payment findByFine(Fine fine) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT p FROM Payment p WHERE p.fine = :fine", Payment.class)
                    .setParameter("fine", fine)
                    .getResultStream()
                    .findFirst()
                    .orElse(null);
        } finally {
            em.close();
        }
    }
}