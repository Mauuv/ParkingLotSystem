package org.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.core.GerenciadorTelas;
import org.entities.Usuario;
import org.holders.UsuarioHolder;
import org.holders.VehicleHolder;

import java.util.NoSuchElementException;

public class LoginScreenController implements Controller {

    private GerenciadorTelas gerenciadorTelas;

    @FXML
    private PasswordField password;

    @FXML
    private TextField username;

    public GerenciadorTelas getGerenciadorTelas() {
        return gerenciadorTelas;
    }

    public void setGerenciadorTelas(GerenciadorTelas gerenciadorTelas) {
        this.gerenciadorTelas = gerenciadorTelas;
    }

    @FXML
    public void initialize() {}

    @FXML
    void login(ActionEvent event) {
        if (password.getText().isEmpty()
        || username.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Os campos usuário e senha são obrigatórios!", ButtonType.CLOSE);
            alert.showAndWait();
        } else {
            validaLogin();
        }
    }

    public void loginInvalido() {
        Alert alert = new Alert(Alert.AlertType.ERROR, "Usuário ou senha inválido", ButtonType.CLOSE);
        alert.setHeaderText("Erro ao realizar login");
        alert.showAndWait();
    }

    public void validaLogin() {
        try {
            Usuario usuarioAtual = new Usuario(username.getText(), password.getText());
            if (usuarioAtual.validaLogin()) {
                UsuarioHolder.getInstance().setUsuario(usuarioAtual);
                gerenciadorTelas.mostraTelaInicial();
            } else {
                throw new NoSuchElementException();
            }
        } catch (Exception e) {
            e.printStackTrace();
            loginInvalido();
        }
    }

}
