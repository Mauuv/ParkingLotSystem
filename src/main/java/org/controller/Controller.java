package org.controller;

import javafx.stage.Stage;
import org.core.GerenciadorTelas;

public interface Controller {

    void setGerenciadorTelas(GerenciadorTelas gerenciadorTelas);

    GerenciadorTelas getGerenciadorTelas();
}
