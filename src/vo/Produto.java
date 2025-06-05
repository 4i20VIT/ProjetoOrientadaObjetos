package vo;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.DoubleProperty;

public class Produto {
    private final StringProperty nome;
    private final DoubleProperty valor;

    public Produto(String nome, double valor) {
        this.nome = new SimpleStringProperty(nome);
        this.valor = new SimpleDoubleProperty(valor);
    }

    public String getNome() {
        return nome.get();
    }

    public void setNome(String nome) {
        this.nome.set(nome);
    }

    public StringProperty nomeProperty() {
        return nome;
    }

    public double getValor() {
        return valor.get();
    }

    public void setValor(double valor) {
        this.valor.set(valor);
    }

    public DoubleProperty valorProperty() {
        return valor;
    }
}