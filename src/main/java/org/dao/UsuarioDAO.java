package org.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Persistence;
import org.entities.Usuario;

import java.util.Collections;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Optional;

public class UsuarioDAO implements Dao{

    private static final String PERSISTENCE_UNIT = "ProjetoFinal";

    private EntityManager getEntityManager() {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
        return factory.createEntityManager();
    }

    @Override
    public Optional<List<Object>> buscarTodos() {
        try (var em = getEntityManager()) {
            String sql = "SELECT id, username, password FROM Usuario";
            var query = em.createQuery(sql, Usuario.class);
            return Optional.ofNullable(Collections.singletonList(query.getResultList()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Object> buscarPorObjeto(Object o) {
        if (!(o instanceof Usuario usuario)) {
            throw new InputMismatchException();
        }
        try (var em = getEntityManager()) {
            String sql = "SELECT id, username, password FROM Usuario WHERE username = :p_user";
            var query = em.createQuery(sql, Usuario.class).setParameter("p_user", usuario.getUsername());
            return Optional.ofNullable(query.getSingleResult());
        } catch (NoResultException e) {
            throw e;
        }
    }

    @Override
    public boolean inserir(Object o) {
        return false;
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
