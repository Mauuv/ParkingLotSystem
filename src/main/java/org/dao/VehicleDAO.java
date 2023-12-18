package org.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Persistence;
import org.entities.Vehicle;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Optional;

public class VehicleDAO implements Dao{

    private static final String PERSISTENCE_UNIT = "ProjetoFinal";
    private EntityManager getEntityManager() {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
        return factory.createEntityManager();
    }

    @Override
    public Optional<List<Object>> buscarTodos() {
        return null;
    }

    public Optional<List<Vehicle>> buscarTodosEspecifico() {
        try (var em = getEntityManager()) {
            String sql = "SELECT id, placa, fabricante, modelo, cor FROM Vehicle";
            var query = em.createQuery(sql, Vehicle.class);
            return Optional.ofNullable(query.getResultList());
        } catch (NoResultException e) {
            return Optional.of(new ArrayList<>());
        }
    }

    @Override
    public Optional<Object> buscarPorObjeto(Object o) {
        if (!(o instanceof Vehicle vehicle)) {
            throw new InputMismatchException();
        }
        try (var em = getEntityManager()) {
            String sql = "SELECT id, placa, fabricante, modelo, cor FROM Vehicle WHERE placa = :p_placa";
            var query = em.createQuery(sql, Vehicle.class).setParameter("p_placa", vehicle.getPlaca());
            return Optional.ofNullable(query.getSingleResult());
        } catch (NoResultException e) {
            throw e;
        }
    }

    @Override
    public boolean inserir(Object o) {
        if (!(o instanceof Vehicle vehicle)) {
            throw new InputMismatchException();
        }
        var em = getEntityManager();
        try {
            em.getTransaction().begin();

            em.persist(vehicle);

            em.getTransaction().commit();

            em.close();

            return true;
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw new RuntimeException(e);
        } finally {
            em.close();
        }
    }

    @Override
    public boolean deletar(Object o) {
        return false;
    }

    @Override
    public boolean alterar(Object o) {
        return false;
    }
}
