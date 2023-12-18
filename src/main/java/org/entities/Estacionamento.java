package org.entities;

import org.dao.HistoricoDAO;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class Estacionamento {
    public final static BigDecimal VALOR_HORA = BigDecimal.valueOf(30);

    public final static Long CAPACIDADE_MAXIMA = Long.valueOf(50);

    public static BigDecimal calcularReceitaDia(LocalDate dia) {
        BigDecimal valorTotal = BigDecimal.ZERO;
        Optional<List<Historico>> historico = new HistoricoDAO().buscarPorPeriodo(dia.atTime(0,0,0), dia.atTime(23,59,59));
        if (historico.isPresent() && !historico.get().isEmpty()) {
            List<Historico> operacoesDia = historico.get();
            for (Historico operacao : operacoesDia) {
                valorTotal = valorTotal.add(operacao.calcularValor());
            }
        }
        return valorTotal;
    }

    public static Long calcularQuantidadeOperacoesNoDia(LocalDate dia) {
        Long ocupacao = Long.valueOf(0);
        Optional<List<Historico>> historico = new HistoricoDAO().buscarPorPeriodo(dia.atTime(0,0,0), dia.atTime(23,59,59));
        if (historico.isPresent() && !historico.get().isEmpty()) {
            List<Historico> operacoesDia = historico.get();
            ocupacao = operacoesDia.stream().filter(op -> op.getDataEntrada().toLocalDate().equals(dia)).count();
        }
        return ocupacao;
    }

    public static boolean estaLotado() {
        return Objects.equals(Long.valueOf(0), CAPACIDADE_MAXIMA - new HistoricoDAO().buscarOcupacao());
    }

    public static Long ocupacaoAtual() {
        return new HistoricoDAO().buscarOcupacao();
    }
}
