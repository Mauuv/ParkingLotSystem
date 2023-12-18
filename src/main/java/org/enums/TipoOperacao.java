package org.enums;

public enum TipoOperacao {
    ENTRADA("Entrada"), SAIDA("Saída");

    private String name;

    TipoOperacao(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
