package org.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Persistence;
import org.entities.Historico;
import org.entities.Vehicle;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Optional;

public class HistoricoDAO implements Dao{

    private static final String PERSISTENCE_UNIT = "ProjetoFinal";

    private EntityManager getEntityManager() {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
        return factory.createEntityManager();
    }


    @Override
    public Optional<List<Object>> buscarTodos() {
        return null;
    }

    @Override
    public Optional<Object> buscarPorObjeto(Object o) {
        if (!(o instanceof Vehicle vehicle)) {
            throw new InputMismatchException();
        }
        try (var em = getEntityManager()) {
            String sql = "SELECT id, veiculo, dataEntrada, dataSaida FROM Historico WHERE veiculo = :p_veiculo AND dataSaida is null";
            var query = em.createQuery(sql, Historico.class).setParameter("p_veiculo", vehicle);
            return Optional.ofNullable(query.getSingleResult());
        } catch (NoResultException e) {
            throw e;
        }
    }

    public Optional<List<Historico>> buscarPorPeriodo(LocalDateTime dataInicio, LocalDateTime dataFim) {
        try (var em = getEntityManager()) {
            String sql = "SELECT id, veiculo, dataEntrada, dataSaida FROM Historico WHERE dataEntrada between :p_data_inicial AND :p_data_final";
            var query = em.createQuery(sql, Historico.class)
                .setParameter("p_data_inicial", dataInicio)
                .setParameter("p_data_final", dataFim);
            return Optional.ofNullable(query.getResultList());
        } catch (NoResultException e) {
            return Optional.of(new ArrayList<>());
        }
    }

    public Optional<Historico> buscarMaisRecente(Vehicle vehicle) {
        try (var em = getEntityManager()) {
            String sql = "SELECT id, veiculo, dataEntrada, dataSaida FROM Historico WHERE veiculo = :p_veiculo ORDER BY id DESC LIMIT 1";
            var query = em.createQuery(sql, Historico.class).setParameter("p_veiculo", vehicle);
            return Optional.ofNullable(query.getSingleResult());
        } catch (NoResultException e) {
            throw e;
        }
    }

    public Long buscarOcupacao() {
        try (var em = getEntityManager()) {
            String sql = "SELECT count(1) FROM Historico WHERE dataSaida is null";
            var query = em.createQuery(sql, Long.class);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return Long.valueOf(0);
        }
    }

    @Override
    public boolean inserir(Object o) {
        if (!(o instanceof Historico historico)) {
            throw new InputMismatchException();
        }
        var em = getEntityManager();
        try {
            em.getTransaction().begin();

            em.persist(historico);

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
        if (!(o instanceof Historico historico)) {
            throw new InputMismatchException();
        }
        var em = getEntityManager();
        try {
            em.getTransaction().begin();

            em.merge(historico);

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
}
