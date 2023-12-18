package org.entities;

import jakarta.persistence.*;
import org.dao.HistoricoDAO;
import org.dao.VehicleDAO;
import org.enums.TipoOperacao;
import org.exception.CustomException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

@Entity
@Table (name = "chegada_saida")
public class Historico {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column
    public Long id;

    @OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn (name = "veiculo")
    public Vehicle veiculo;

    @Column (name = "hr_entrada")
    public LocalDateTime dataEntrada;

    @Column (name = "hr_saida")
    public LocalDateTime dataSaida;

    public Historico() {
    }

    public Historico(Vehicle veiculo, LocalDateTime dataEntrada, LocalDateTime dataSaida) {
        this.veiculo = veiculo;
        this.dataEntrada = dataEntrada;
        this.dataSaida = dataSaida;
    }

    public Historico(Long id, Vehicle veiculo, LocalDateTime dataEntrada, LocalDateTime dataSaida) {
        this.id = id;
        this.veiculo = veiculo;
        this.dataEntrada = dataEntrada;
        this.dataSaida = dataSaida;
    }

    public Vehicle getVeiculo() {
        return veiculo;
    }

    public void setVeiculo(Vehicle veiculo) {
        this.veiculo = veiculo;
    }

    public LocalDateTime getDataEntrada() {
        return dataEntrada;
    }

    public void setDataEntrada(LocalDateTime dataEntrada) {
        this.dataEntrada = dataEntrada;
    }

    public LocalDateTime getDataSaida() {
        return dataSaida;
    }

    public void setDataSaida(LocalDateTime dataSaida) {
        this.dataSaida = dataSaida;
    }

    public TipoOperacao registrarOperacao(Vehicle vehicle) throws CustomException {
        try {
            HistoricoDAO historicoDAO = new HistoricoDAO();
            Optional<Object> sysHistorico = historicoDAO.buscarPorObjeto(new VehicleDAO().buscarPorObjeto(vehicle).get());
            if (sysHistorico.isPresent()) {
                registrarSaida(vehicle, (Historico) sysHistorico.get());
                return TipoOperacao.SAIDA;
            }
        } catch (Exception e) {
            if (e instanceof NoResultException) {
                registrarEntrada(vehicle);
                return TipoOperacao.ENTRADA;
            } else {
                throw new RuntimeException(e);
            }
        }
        return null;
    }

    private void registrarEntrada(Vehicle vehicle) throws CustomException {
        if (Estacionamento.estaLotado()) {
            throw new CustomException("Estacionamento lotado!");
        } else {
            HistoricoDAO historicoDAO = new HistoricoDAO();
            Historico historico = new Historico();
            historico.setDataEntrada(LocalDateTime.now());
            historico.setVeiculo((Vehicle) new VehicleDAO().buscarPorObjeto(vehicle).get());
            historicoDAO.inserir(historico);
        }
    }

    private void registrarSaida(Vehicle vehicle, Historico historico) {
        HistoricoDAO historicoDAO = new HistoricoDAO();
        historico.setDataSaida(LocalDateTime.now());
        historico.setVeiculo((Vehicle) new VehicleDAO().buscarPorObjeto(vehicle).get());
        historicoDAO.alterar(historico);
    }

    public BigDecimal calcularValor() {
        if(this.getDataSaida() != null) {
            BigDecimal minutosHora = BigDecimal.valueOf(60);
            BigDecimal minutos = BigDecimal.valueOf(Duration.between(this.getDataEntrada(), this.getDataSaida()).toMinutes());
            minutos = minutos.compareTo(minutosHora) != 1   ? minutosHora : minutos;
            return Estacionamento.VALOR_HORA.multiply(minutos.divide(minutosHora, 2, RoundingMode.DOWN)).setScale(2, RoundingMode.HALF_UP);
        } else {
            return BigDecimal.ZERO;
        }
    }
}
