package org.internship.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import org.internship.entity.Citizen;
import org.internship.entity.Vehicle;

import java.util.List;

public class VehicleRepo {

    private final EntityManagerFactory emf;

    public VehicleRepo(EntityManagerFactory emf) {
        this.emf = emf;
    }

  public Vehicle save(Vehicle vehicle){
      EntityManager em = emf.createEntityManager();
      EntityTransaction tx = em.getTransaction();
      try {
          tx.begin();

          if (vehicle.getNID() == null) {
              throw new IllegalArgumentException("Vehicle NID (chassis number) cannot be null");
          }

          if(em.find(Vehicle.class, vehicle.getNID()) != null){
              vehicle = em.merge(vehicle);
          } else {
              em.persist(vehicle);
          }
          tx.commit();
          return vehicle;
      } catch (RuntimeException e){
          if (tx.isActive()) {
              tx.rollback();
          }
          throw e;
      } finally {
          em.close();
      }
  }

  public Vehicle findByNID(String nid) {
        EntityManager em = emf.createEntityManager();

        try {
            return em.find(Vehicle.class, nid);
        }catch (RuntimeException e){
           throw e;
        }finally {
            em.close();
        }

  }

    public Vehicle findByPlateNumber(String plateNumber) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT v FROM Vehicle v WHERE v.plateNumber = :plateNumber", Vehicle.class)
                    .setParameter("plateNumber", plateNumber)
                    .getResultStream()
                    .findFirst()
                    .orElse(null);
        } finally {
            em.close();
        }
    }

  public List<Vehicle> findByCitizen(Citizen citizen){
        EntityManager em = emf.createEntityManager();

        try {
            return em.createQuery("SELECT v FROM Vehicle v WHERE v.owner = :citizen", Vehicle.class)
                    .setParameter("citizen", citizen)
                    .getResultList();
        }catch (RuntimeException e){
            throw e;
        }finally {
            em.close();
        }
  }

  public List<Vehicle> findAll(){
        EntityManager em = emf.createEntityManager();

        try {
            TypedQuery<Vehicle> query = em.createQuery("SELECT v FROM Vehicle v", Vehicle.class);
            return query.getResultList();
        }catch (RuntimeException e){
            throw e;
        }finally {
            em.close();
        }
  }



}
