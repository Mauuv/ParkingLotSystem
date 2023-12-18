package org.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.core.GerenciadorTelas;


import javafx.event.ActionEvent;
import org.dao.VehicleDAO;
import org.entities.Vehicle;
import org.holders.InitialScreenControllerHolder;
import org.holders.VehicleHolder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.regex.Pattern;

public class RegisterCarScreenController implements Controller {

    public GerenciadorTelas gerenciadorTelas;

    public GerenciadorTelas getGerenciadorTelas() {
        return gerenciadorTelas;
    }

    public void setGerenciadorTelas(GerenciadorTelas gerenciadorTelas) {
        this.gerenciadorTelas = gerenciadorTelas;
    }

    @FXML
    private Button botaoCadastrar;

    @FXML
    private TextField cor;

    @FXML
    private TextField fabricante;

    @FXML
    private TextField modelo;

    @FXML
    private TextField placa;

    @FXML
    private TableColumn<Vehicle, String> colunaCor;

    @FXML
    private TableColumn<Vehicle, String> colunaFabricante;

    @FXML
    private TableColumn<Vehicle, String> colunaModelo;

    @FXML
    private TableColumn<Vehicle, String> colunaPlaca;

    @FXML
    private TableView<Vehicle> veiculos;

    private ObservableList<Vehicle> veiculosCadastrados = FXCollections.observableArrayList();

    private InitialScreenController initialScreenController;

    public InitialScreenController getInitialScreenController() {
        return initialScreenController;
    }

    public void setInitialScreenController(InitialScreenController initialScreenController) {
        this.initialScreenController = initialScreenController;
    }

    @FXML
    public void initialize() {
        placa.setText(VehicleHolder.getInstance().getVehicle().getPlaca());
        setInitialScreenController(InitialScreenControllerHolder.getInstance().getInitialScreenController());
        configurarTabela();
    }

    @FXML
    void efetuarCadastro(ActionEvent event) {
        String mensagem = "";
        try {
            if(fabricante.getText().isEmpty()
            || modelo.getText().isEmpty()
            || placa.getText().isEmpty()
            || cor.getText().isEmpty()) {
                mensagem = "Todos os campos são obrigatórios";
                throw new NoSuchFieldException();
            } else {
                Pattern pattern = Pattern.compile("[A-Z]{3}-\\d{4}");
                if(pattern.matcher(placa.getText()).matches()) {
                    efetivarCadastro(placa.getText(), fabricante.getText(), modelo.getText(), cor.getText());
                } else {
                    mensagem = "Formato da placa incorreto!" + System.lineSeparator() + "Deve estar no seguinte padrão: \"XXX-1111\" ";
                    throw new InputMismatchException();
                }
            }
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, mensagem.isBlank() ? e.getMessage() : mensagem, ButtonType.CLOSE);
            alert.showAndWait();
        }
    }



    public void efetivarCadastro(String placa, String fabricante, String modelo, String cor) throws IOException {
        if(new VehicleDAO().inserir(new Vehicle(placa, fabricante, modelo, cor))) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Carro " + placa + " inserido com sucesso!");
            alert.showAndWait();
            initialScreenController.verificarCadastro(true);
            gerenciadorTelas.mostraTelaInicial();
        }
    }

    public void configurarTabela() {
        veiculosCadastrados.addAll(new VehicleDAO().buscarTodosEspecifico().get());
        veiculos.getItems().addAll(veiculosCadastrados);

        colunaCor.setCellValueFactory(new PropertyValueFactory<>("cor"));
        colunaPlaca.setCellValueFactory(new PropertyValueFactory<>("placa"));
        colunaModelo.setCellValueFactory(new PropertyValueFactory<>("modelo"));
        colunaFabricante.setCellValueFactory(new PropertyValueFactory<>("fabricante"));
    }
}
