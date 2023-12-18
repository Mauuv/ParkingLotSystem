package org.entities;

import jakarta.persistence.*;
import org.dao.UsuarioDAO;

import java.util.Optional;

@Entity
@Table (name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column
    public Long id;

    @Column
    public String username;

    @Column
    public String password;

    public Usuario() {
    }

    public Usuario(Long id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    public Usuario(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean validaLogin() {
        UsuarioDAO usuarioDAO = new UsuarioDAO();
        Optional<Object> sysUser = usuarioDAO.buscarPorObjeto(new Usuario(this.getUsername(), this.getPassword()));
        if (sysUser.isPresent()) {
            return this.password.equals(((Usuario) sysUser.get()).getPassword());
        }
        return false;
    }
}
