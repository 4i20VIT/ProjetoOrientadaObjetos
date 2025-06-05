package vo;

import java.time.LocalDate; 

/**
 *
 * @author vitor
 */
public class Pagamento {
    private String tipo;
    private float valorTotal;
    private LocalDate data;
    

    public Pagamento(String tipo, float valorTotal, LocalDate data) {
        this.tipo = tipo;
        this.valorTotal = valorTotal;
        this.data = data;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public float getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(float valorTotal) {
        this.valorTotal = valorTotal;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }
}
