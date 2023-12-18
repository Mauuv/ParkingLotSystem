package org.core;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage stage) {
        try{
            GerenciadorTelas gerenciadorTelas = new GerenciadorTelas(stage);
            gerenciadorTelas.mostraTelaLogin();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    public static void main(String[] args) {
        launch();
    }

}