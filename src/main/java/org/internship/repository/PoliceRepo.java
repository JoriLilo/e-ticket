package org.internship.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import org.internship.entity.Police;

import java.util.List;

public class PoliceRepo {

    private final EntityManagerFactory emf;

    public PoliceRepo(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public Police save(Police police) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            if (police.getId() == null) {
                em.persist(police);
            } else {
                police = em.merge(police);
            }
            tx.commit();
            return police;
        } catch (RuntimeException e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    public Police findById(Long id) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.find(Police.class, id);
        } finally {
            em.close();
        }
    }

    public List<Police> findAll() {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Police> query = em.createQuery("SELECT p FROM Police p", Police.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
}