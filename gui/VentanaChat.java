package gui;

import java.awt.Dimension;
import javax.swing.JFrame;

import domain.Conversacion;
import domain.Usuario;

public class VentanaChat extends JFrame {
    private static final long serialVersionUID = 1L;

    public VentanaChat(Conversacion conversacion, Usuario usuario) {
        Usuario otro;
        if (usuario == conversacion.getComprador()) {
            otro = conversacion.getVendedor();
        } else {
            otro = conversacion.getComprador();
        }

        setTitle("UrbanDeal | Chat con " + otro.getNombreUsuario());
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(500, 520);
        setMinimumSize(new Dimension(350, 300));
        setLocationRelativeTo(null);

        // Reutilizo el panel para no repetir el codigo del chat
        PanelChat panel = new PanelChat(conversacion, usuario, null);
        setContentPane(panel);
    }
}