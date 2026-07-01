package org.internship.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import org.internship.entity.Citizen;
import org.internship.entity.Fine;

import java.util.List;

public class FineRepo{

    private final EntityManagerFactory emf;

    public FineRepo(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public Fine save(Fine fine) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            if (fine.getFineID() == null) {
                em.persist(fine);
            } else {
                fine = em.merge(fine);
            }
            tx.commit();
            return fine;
        } catch (RuntimeException e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    public Fine findById(Long id) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.find(Fine.class, id);
        } finally {
            em.close();
        }
    }

    public List<Fine> findAll() {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("SELECT f FROM Fine f", Fine.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public List<Fine> findByCitizen(Citizen citizen) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT f FROM Fine f WHERE f.vehicle.owner = :citizen", Fine.class)
                    .setParameter("citizen", citizen)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public List<Fine> findByPlateNumber(String plateNumber) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT f FROM Fine f WHERE f.vehicle.plateNumber = :plateNumber", Fine.class)
                    .setParameter("plateNumber", plateNumber)
                    .getResultList();
        } finally {
            em.close();
        }
    }
}