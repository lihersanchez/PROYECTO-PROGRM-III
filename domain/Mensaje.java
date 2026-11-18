package domain;

import java.time.LocalDateTime;

public class Mensaje {

    private Usuario emisor;
    private String texto;
    private LocalDateTime fecha;

    public Mensaje(Usuario emisor, String texto) {
        // Guardo el texto tal como lo he escrito y la fecha del envio
        this.emisor = emisor;
        this.texto = texto;
        this.fecha = LocalDateTime.now();
    }

    public Usuario getEmisor() {
        return emisor;
    }

    public String getTexto() {
        return texto;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }
}
