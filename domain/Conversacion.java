package domain;

import java.util.ArrayList;
import gui.PanelChat;

public class Conversacion {

    private Articulo articulo;
    private Usuario comprador;
    private Usuario vendedor;
    private ArrayList<Mensaje> mensajes = new ArrayList<>();
    private ArrayList<PanelChat> paneles = new ArrayList<>();

    public Conversacion(Articulo articulo, Usuario comprador) {
        this.articulo = articulo;
        this.comprador = comprador;
        this.vendedor = articulo.getVendedor();
    }

    public void enviarMensaje(Usuario emisor, String texto) {
        // Creo el mensaje y lo guardo en la lista de esta conversacion
        Mensaje mensaje = new Mensaje(emisor, texto);
        mensajes.add(mensaje);

        // Actualizo todos los paneles que tienen abierta esta conversacion
        for (PanelChat panel : paneles) {
            panel.actualizarHistorial();
        }
    }

    public void agregarPanel(PanelChat panel) {
        paneles.add(panel);
    }

    public void quitarPanel(PanelChat panel) {
        paneles.remove(panel);
    }

    public ArrayList<Mensaje> getMensajes() {
        return mensajes;
    }

    public Articulo getArticulo() {
        return articulo;
    }

    public Usuario getComprador() {
        return comprador;
    }

    public Usuario getVendedor() {
        return vendedor;
    }
}
