package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.time.format.DateTimeFormatter;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import domain.Conversacion;
import domain.Mensaje;
import domain.Usuario;

public class PanelChat extends JPanel {
    private static final long serialVersionUID = 1L;

    private Conversacion conversacion;
    private Usuario usuario;
    private JTextArea historial = new JTextArea();
    private JTextField entrada = new JTextField();

    public PanelChat(Conversacion conversacion, Usuario usuario, JFramePrincipal ventanaPrincipal) {
        setLayout(new BorderLayout(10, 10));
        this.conversacion = conversacion;
        this.usuario = usuario;

        setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        Usuario otro;
        if (usuario == conversacion.getComprador()) {
            otro = conversacion.getVendedor();
        } else {
            otro = conversacion.getComprador();
        }

        JPanel cabecera = new JPanel(new BorderLayout(10, 0));
        if (ventanaPrincipal != null) {
            JButton atras = new JButton("Volver a mensajes");
            atras.addActionListener(e -> {
                ventanaPrincipal.mostrarConversaciones();
            });
            cabecera.add(atras, BorderLayout.WEST);
        }

        JLabel lblTitulo = new JLabel(conversacion.getArticulo().getTitulo()
                + " (" + String.format("%.2f €", conversacion.getArticulo().getPrecio()) + ")"
                + " | Chat con " + otro.getNombreUsuario());
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 15));
        lblTitulo.setForeground(JFramePrincipal.COLOR_PRIMARIO);
        cabecera.add(lblTitulo, BorderLayout.CENTER);
        add(cabecera, BorderLayout.NORTH);

        historial.setEditable(false);
        historial.setLineWrap(true);
        historial.setWrapStyleWord(true);
        historial.setFont(new Font("SansSerif", Font.PLAIN, 14));
        add(new JScrollPane(historial), BorderLayout.CENTER);

        JPanel redaccion = new JPanel(new BorderLayout(8, 0));
        JButton enviar = new JButton("Enviar");
        enviar.setBackground(JFramePrincipal.COLOR_PRIMARIO);
        enviar.setForeground(Color.WHITE);
        // Tanto el boton como Enter llaman al mismo metodo
        enviar.addActionListener(e -> {
            enviarMensaje();
        });
        entrada.addActionListener(e -> {
            enviarMensaje();
        });
        redaccion.add(entrada, BorderLayout.CENTER);
        redaccion.add(enviar, BorderLayout.EAST);
        add(redaccion, BorderLayout.SOUTH);

        actualizarHistorial();
    }

    private void enviarMensaje() {
        // Leo el texto, lo envio y limpio el campo
        String texto = entrada.getText();
        conversacion.enviarMensaje(usuario, texto);
        entrada.setText("");
    }

    public void actualizarHistorial() {
        // Borro lo que se ve y vuelvo a mostrar los mensajes de la lista
        historial.setText("");
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM HH:mm");

        for (Mensaje mensaje : conversacion.getMensajes()) {
            String nombre = mensaje.getEmisor().getNombreUsuario();
            String fecha = mensaje.getFecha().format(formato);
            historial.append(nombre + " [" + fecha + "]\n");
            historial.append(mensaje.getTexto() + "\n\n");
        }
    }

    // Al entrar el panel en la interfaz, lo guardo en la conversacion
    @Override
    public void addNotify() {
        super.addNotify();
        conversacion.agregarPanel(this);
        actualizarHistorial();
    }

    // Al salir del chat, quito este panel de la lista
    @Override
    public void removeNotify() {
        conversacion.quitarPanel(this);
        super.removeNotify();
    }
}
