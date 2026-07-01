package org.internship.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import org.internship.entity.Citizen;

import java.util.List;

public class CitizenRepo {

    private final EntityManagerFactory emf;

    public CitizenRepo(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public Citizen save(Citizen citizen) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            if (citizen.getId() == null) {
                em.persist(citizen);
            } else {
                citizen = em.merge(citizen);
            }
            tx.commit();
            return citizen;
        } catch (RuntimeException e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }


    public Citizen findById(Long id) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.find(Citizen.class, id);
        } finally {
            em.close();
        }
    }


    public List<Citizen> findAll() {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Citizen> query = em.createQuery("SELECT c FROM Citizen c", Citizen.class);
            return query.getResultList();
        }catch (RuntimeException e) {

            throw e;
        }finally {
            em.close();
        }
    }
}
