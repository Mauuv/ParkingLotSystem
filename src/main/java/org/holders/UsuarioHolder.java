package org.holders;


import org.entities.Usuario;

public class UsuarioHolder {

    private Usuario usuario;
    private final static UsuarioHolder INSTANCE = new UsuarioHolder();

    private UsuarioHolder() {}

    public static UsuarioHolder getInstance() {
        return INSTANCE;
    }

    public void setUsuario(Usuario U) {
        this.usuario = U;
    }

    public Usuario getUsuario() {
        return this.usuario;
    }

}
