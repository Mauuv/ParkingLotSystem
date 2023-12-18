package org.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;

import org.core.GerenciadorTelas;
import org.dao.HistoricoDAO;
import org.dao.VehicleDAO;
import org.entities.Estacionamento;
import org.entities.Historico;
import org.entities.Vehicle;
import org.enums.TipoOperacao;
import org.exception.CustomException;
import org.holders.InitialScreenControllerHolder;
import org.holders.UsuarioHolder;
import org.holders.VehicleHolder;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class InitialScreenController implements Controller {

    private GerenciadorTelas gerenciadorTelas;

    @FXML
    private Button botaoConfirma;

    @FXML
    private Button botaoSair;

    @FXML
    private Button botaoFecharEstacionamento;

    @FXML
    private MenuItem menuFecharEstacionamento;

    @FXML
    private MenuItem menuSair;

    @FXML
    private MenuItem menuSobre;

    @FXML
    private Text operador;

    @FXML
    private TextField placa;

    @FXML
    private Text receita;

    @FXML
    private Text ocupacao;

    @FXML
    private TableView<Historico> ocupacaoAtual;

    @FXML
    private TableColumn<Historico, Vehicle> colunaCor;

    @FXML
    private TableColumn<Historico, LocalDateTime> colunaEntrada;

    @FXML
    private TableColumn<Historico, Vehicle> colunaModelo;

    @FXML
    private TableColumn<Historico, Vehicle> colunaPlaca;

    private ObservableList<Historico> operacoes = FXCollections.observableArrayList();

    public void setTextoReceita(BigDecimal receita) {
        this.receita.setText(formatarValor(receita));
    }

    public void setTextoOcupacao(Long ocupacao) {
        this.ocupacao.setText(ocupacao.toString());
    }

    public GerenciadorTelas getGerenciadorTelas() {
        return gerenciadorTelas;
    }

    public void setGerenciadorTelas(GerenciadorTelas gerenciadorTelas) {
        this.gerenciadorTelas = gerenciadorTelas;
    }


    @FXML
    public void initialize() {
        operador.setText(UsuarioHolder.getInstance().getUsuario().getUsername());
        configurarTabela();
    }

    @FXML
    void encerrarAplicacao(ActionEvent event) {
        gerenciadorTelas.close();
    }


    @FXML
    void resumoDoDia(ActionEvent event) {
        try {
            if (!Objects.equals(Estacionamento.ocupacaoAtual(), Long.valueOf(0))) {
                throw new CustomException("Não é possível fechar o estacionamento, ainda há "
                        + Estacionamento.ocupacaoAtual() + " veículo(s) nele");
            }
            LocalDate diaAtual = LocalDateTime.now().toLocalDate();
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Encerramento do dia: " + formatarData(LocalDateTime.now(), false) +
                    System.lineSeparator() + "Valor faturado no dia: " + formatarValor(Estacionamento.calcularReceitaDia(diaAtual)) +
                    System.lineSeparator() + "Quantidade de operações realizadas: " + Estacionamento.calcularQuantidadeOperacoesNoDia(diaAtual)
                    , ButtonType.OK);
            alert.showAndWait();
        } catch (Exception e) {
            mostraErro(e, "");
        }
    }

    @FXML
    void mostrarDesenvolvedor(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Projeto final da disciplina de desenvolvimento de sistemas 1 - IFRS Campus Farroupilha",
            ButtonType.OK);
        alert.setHeaderText("Desenvolvido por: Maurício Tonin Verona");
        alert.showAndWait();
    }

    @FXML
    void registrarMovimentacao(ActionEvent event) {
        try {
            if(validarInformaçoes()) {
                verificarCadastro(false);
            }
        } catch (Exception e) {
            mostraErro(e, "");
        }
    }

    public boolean validarInformaçoes() throws CustomException {
        if (!placa.getText().isEmpty()) {
            Pattern pattern = Pattern.compile("[A-Z]{3}-\\d{4}");
            if(!pattern.matcher(placa.getText()).matches()) {
                throw new CustomException("Formato da placa incorreto!" + System.lineSeparator() + "Deve estar no seguinte padrão: \"XXX-1111\" ");
            }
        } else {
            throw new CustomException("O campo placa é obrigatório");
        }
        return true;
    }

    public void verificarCadastro(boolean chamadoPorOutraTela) {
        try {
            Vehicle vehicle;
            if(chamadoPorOutraTela) {
                vehicle = VehicleHolder.getInstance().getVehicle();
            } else {
                vehicle = new Vehicle(placa.getText());
            }
            if (!vehicle.temCadastro()) {
                VehicleHolder.getInstance().setVehicle(vehicle);
                InitialScreenControllerHolder.getInstance().setInitialScreenController(this);
                gerenciadorTelas.mostraCadastroDeVeiculos();
            } else {
                TipoOperacao operacao = new Historico().registrarOperacao(vehicle);
                if (operacao != null) {
                    mostraConfirmacao(operacao, vehicle);
                    atualizarInformacoesTela();
                } else {
                    mostraErro(new CustomException("Erro ao registar operação"), "");
                }
            }
        } catch (Exception e) {
            mostraErro(e, "");
        }
    }

    public void mostraErro(Exception e, String mensagem) {
        e.printStackTrace();
        Alert alert = new Alert(Alert.AlertType.ERROR, mensagem.isBlank() ? e.getMessage() : mensagem, ButtonType.CLOSE);
        alert.showAndWait();
    }

    public void mostraConfirmacao(TipoOperacao operacao, Vehicle vehicle) {
        String mensagem = "";
        try{
           Optional<Historico> historico = new HistoricoDAO().buscarMaisRecente((Vehicle) new VehicleDAO().buscarPorObjeto(vehicle).get());
           if (!historico.isPresent()) {
               mensagem = "Operação não obteve êxito. Refaça o processo!";
               throw new NoSuchElementException();
           } else {
               Historico sysHistorico = historico.get();
               StringBuilder confirmacao = new StringBuilder(
                   "Operação de: " + operacao.getName().toLowerCase() + " registrada com sucesso!"
                   + System.lineSeparator() + "Veículo: " + sysHistorico.getVeiculo().getPlaca()
                   + System.lineSeparator() + "Data e hora de entrada: " + formatarData(sysHistorico.getDataEntrada(), true)
               );

               if (operacao == TipoOperacao.SAIDA) {
                   confirmacao.append(System.lineSeparator() + "Data e hora de saída: " + formatarData(sysHistorico.getDataSaida(), true));
                   confirmacao.append(System.lineSeparator() + "Valor a ser pago: R$" + formatarValor(sysHistorico.calcularValor()));
               }

               Alert alert = new Alert(Alert.AlertType.INFORMATION, confirmacao.toString(), ButtonType.OK);
               alert.showAndWait();
           }
        } catch (Exception e) {
            mostraErro(e, mensagem);
        }
    }

    public String formatarValor(BigDecimal valor) {
        DecimalFormat df = new DecimalFormat("#,##0.00");
        return df.format(valor);
    }

    public String formatarData(LocalDateTime data, boolean manterHora) {
        DateTimeFormatter dtf;
        if (manterHora) {
            dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        } else {
            dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        }
        return dtf.format(data);
    }

    public void atualizarInformacoesTela() {
        LocalDate dia = LocalDateTime.now().toLocalDate();

        setTextoReceita(Estacionamento.calcularReceitaDia(dia));
        setTextoOcupacao(Estacionamento.ocupacaoAtual());
        operacoes.clear();
        operacoes.addAll(
            new HistoricoDAO().buscarPorPeriodo(
                            dia.atTime(0,0,0), dia.atTime(23,59,59))
                    .get().stream().filter(op -> op.getDataSaida() == null)
                    .collect(Collectors.toList())
        );
        ocupacaoAtual.getItems().clear();
        ocupacaoAtual.getItems().addAll(operacoes);
    }

    public void configurarTabela() {
        atualizarInformacoesTela();

        colunaCor.setCellValueFactory(new PropertyValueFactory<>("veiculo"));
        colunaEntrada.setCellValueFactory(new PropertyValueFactory<>("dataEntrada"));
        colunaModelo.setCellValueFactory(new PropertyValueFactory<>("veiculo"));
        colunaPlaca.setCellValueFactory(new PropertyValueFactory<>("veiculo"));

        colunaCor.setCellFactory(tc -> new TableCell<>() {
            @Override
            protected void updateItem(Vehicle item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getCor());
                }
            }
        });

        colunaModelo.setCellFactory(tc -> new TableCell<>() {
            @Override
            protected void updateItem(Vehicle item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getModelo());
                }
            }
        });

        colunaPlaca.setCellFactory(tc -> new TableCell<>() {
            @Override
            protected void updateItem(Vehicle item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getPlaca());
                }
            }
        });

        colunaEntrada.setCellFactory(tc -> new TableCell<>() {
            @Override
            protected void updateItem(LocalDateTime item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(formatarData(item, true));
                }
            }
        });
    }
}