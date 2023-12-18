package org.core;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.controller.InitialScreenController;
import org.controller.LoginScreenController;
import org.controller.RegisterCarScreenController;

import java.io.IOException;

public class GerenciadorTelas {

    private Stage stage;

    public GerenciadorTelas(Stage stage) {
        this.stage = stage;
    }

    public Stage getStage() {
        return stage;
    }

    public void mostraTelaLogin() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(App.class.getResource("/screens/LoginScreen.fxml"));
        Parent root = loader.load();
        LoginScreenController loginScreenController = loader.getController();
        loginScreenController.setGerenciadorTelas(this);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Login");
        stage.show();
    }

    public void mostraTelaInicial() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/screens/InitialScreen.fxml"));
        Parent root = loader.load();
        InitialScreenController initialScreenController = loader.getController();
        initialScreenController.setGerenciadorTelas(this);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Gerenciador de Estacionamento");
        stage.show();
    }

    public void mostraCadastroDeVeiculos() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/screens/RegisterCarScreen.fxml"));
        Parent root = loader.load();
        RegisterCarScreenController registerCarScreenController = loader.getController();
        registerCarScreenController.setGerenciadorTelas(this);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Cadastro de Veículos");
        stage.show();
    }

    public void close() {
        stage.close();
    }
}
