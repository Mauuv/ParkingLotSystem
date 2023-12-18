package org.entities;
import jakarta.persistence.*;
import org.dao.HistoricoDAO;
import org.dao.UsuarioDAO;
import org.dao.VehicleDAO;
import org.enums.TipoOperacao;

import java.util.Objects;
import java.util.Optional;

@Entity
@Table (name = "veiculos")
public class Vehicle {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column
    public Long id;

    @Column
    public String placa;

    @Column
    public String fabricante;

    @Column
    public String modelo;

    @Column
    public String cor;

    public Vehicle() {
    }

    public Vehicle(Long id, String placa, String fabricante, String modelo, String cor) {
        this.id = id;
        this.placa = placa;
        this.fabricante = fabricante;
        this.modelo = modelo;
        this.cor = cor;
    }

    public Vehicle(String placa) {
        this.placa = placa;
    }

    public Vehicle(String placa, String fabricante, String modelo, String cor) {
        this.placa = placa;
        this.fabricante = fabricante;
        this.modelo = modelo;
        this.cor = cor;
    }
    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getFabricante() {
        return fabricante;
    }

    public void setFabricante(String fabricante) {
        this.fabricante = fabricante;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getCor() {
        return cor;
    }

    public void setCor(String cor) {
        this.cor = cor;
    }

    public boolean temCadastro() {
        try {
            VehicleDAO vehicleDAO = new VehicleDAO();
            Optional<Object> sysVehicle = vehicleDAO.buscarPorObjeto(new Vehicle(this.placa));
            if (sysVehicle.isPresent()) {
                return this.placa.equals(((Vehicle) sysVehicle.get()).getPlaca());
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Vehicle vehicle)) return false;

        return placa.equals(vehicle.placa);
    }

    @Override
    public String toString() {
        return "Vehicle{" +
                "placa='" + placa + '\'' +
                ", fabricante='" + fabricante + '\'' +
                ", modelo='" + modelo + '\'' +
                ", cor='" + cor + '\'' +
                '}';
    }
}
