
package vo;

import java.time.LocalDate;

/**
 *
 * @author vitor
 */
public class Checkin {
    private int id;
    private int quartoNumero;
    private String clienteNome;
    private LocalDate dataCheckin;
    private double diaria;
    private LocalDate dataCheckout;
    private String status;

    public Checkin(int id, int quartoNumero, String clienteNome, LocalDate dataCheckin, double diaria) {
        this.id = id;
        this.quartoNumero = quartoNumero;
        this.clienteNome = clienteNome;
        this.dataCheckin = dataCheckin;
        this.diaria = diaria;
    }

    public int getId() {
        return id;
    }

    public String getClienteNome() {
        return clienteNome;
    }

    public int getQuartoNumero() {
        return quartoNumero;
    }
    
    public LocalDate getDataCheckin() {
        return dataCheckin;
    }

    public void setDataCheckin(LocalDate dataCheckin) {
        this.dataCheckin = dataCheckin;
    }
    
    public LocalDate getDataCheckout() {
        return dataCheckout;
    }

    public void setDataCheckout(LocalDate dataCheckout) {
        this.dataCheckout = dataCheckout;
    }
    
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getDiaria() {
        return diaria;
    }
}

