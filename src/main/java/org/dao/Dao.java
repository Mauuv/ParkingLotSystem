package org.dao;

import java.util.List;
import java.util.Optional;

public interface Dao {

    Optional<List<Object>> buscarTodos();

    Optional<Object> buscarPorObjeto(Object o);

    boolean inserir(Object o);

    boolean deletar(Object o);

    boolean alterar(Object o);
}
