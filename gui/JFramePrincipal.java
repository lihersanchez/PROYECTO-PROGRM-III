package gui;

import java.awt.*;
import java.net.URL;
import java.util.Vector;
import javax.swing.*;

import domain.Articulo;
import domain.Conversacion;
import domain.GestorTienda;
import domain.Mensaje;
import domain.Usuario;

public class JFramePrincipal extends JFrame {

    public static final Color COLOR_PRIMARIO = new Color(0, 119, 130);

    // Uso el mismo gestor para la sesion del usuario y sus conversaciones
    private GestorTienda gestor = new GestorTienda();
    private JPanel panelContenido = new JPanel(new BorderLayout());

    private JPanel panelPrincipal;
    private JPanel panelDeNavegacion;
    private JPanel panelDeNavegacionNorte;
    private JPanel panelDeNavegacionSur;
    private JTextField txtBuscar;
    private Vector<String> categoriasNavegacion;

    public JFramePrincipal() {
        configurarVentana();
        inicializarComponentes();
        configurarEventos();
    }

    private void configurarVentana() {
        setTitle("UrbanDeal");
        setSize(1920, 1080);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        URL iconUrl = getClass().getResource("/fire/images/logo.jpg");
        if (iconUrl != null) {
            ImageIcon icono = new ImageIcon(iconUrl);
            this.setIconImage(icono.getImage());
        }
    }

    private void inicializarComponentes() {
        panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.setBackground(Color.WHITE);

        panelDeNavegacion = new JPanel(new BorderLayout());
        panelDeNavegacion.setBackground(Color.WHITE);
        panelDeNavegacion.setBorder(BorderFactory.createLineBorder(new Color(0, 119, 130), 2));

        panelDeNavegacionNorte = new JPanel(new BorderLayout(15, 0));
        panelDeNavegacionNorte.setBackground(Color.WHITE);
        panelDeNavegacionNorte.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JLabel lblLogo = new JLabel("UrbanDeal");
        URL logoUrl = getClass().getResource("/fire/images/logoconnombre.jpg");
        if (logoUrl != null) {
            ImageIcon iconoOriginal = new ImageIcon(logoUrl);
            Image imagenEscalada = iconoOriginal.getImage().getScaledInstance(200, 70, Image.SCALE_SMOOTH);
            lblLogo = new JLabel(new ImageIcon(imagenEscalada));
        }
        panelDeNavegacionNorte.add(lblLogo, BorderLayout.WEST);

        JPanel panelBuscador = new JPanel(new BorderLayout());
        panelBuscador.setBackground(new Color(240, 240, 240));
        panelBuscador.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        txtBuscar = new JTextField("Buscar artículos...");
        txtBuscar.setForeground(Color.GRAY);
        txtBuscar.setBackground(new Color(240, 240, 240));
        txtBuscar.setBorder(null);
        panelBuscador.add(txtBuscar, BorderLayout.CENTER);

        panelDeNavegacionNorte.add(panelBuscador, BorderLayout.CENTER);

        JPanel panelAcciones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        panelAcciones.setBackground(Color.WHITE);

        JButton btnLogin = new JButton("Iniciar sesión");
        btnLogin.setFocusable(false);

        JButton btnVender = new JButton("Vender ahora");
        btnVender.setBackground(new Color(0, 119, 130));
        btnVender.setForeground(Color.WHITE);
        btnVender.setFocusable(false);

        JButton btnMensajes = new JButton("Mensajes");
        btnMensajes.setFocusable(false);
        btnMensajes.addActionListener(e -> {
            mostrarConversaciones();
        });

        JComboBox<String> comboIdioma = new JComboBox<>(new String[]{"ES", "EN", "FR"});
        comboIdioma.setBackground(Color.WHITE);

        panelAcciones.add(btnLogin);
        panelAcciones.add(btnVender);
        panelAcciones.add(btnMensajes);
        panelAcciones.add(comboIdioma);

        panelDeNavegacionNorte.add(panelAcciones, BorderLayout.EAST);

        panelDeNavegacionSur = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 8));
        panelDeNavegacionSur.setBackground(Color.WHITE);

        categoriasNavegacion = new Vector<>();
        categoriasNavegacion.add("Todo");
        categoriasNavegacion.add("Mujer");
        categoriasNavegacion.add("Hombre");
        categoriasNavegacion.add("Niños");
        categoriasNavegacion.add("Hogar");
        categoriasNavegacion.add("Electrónica");

        for (String textoBoton : categoriasNavegacion) {
            JButton boton = new JButton(textoBoton);
            boton.setFocusable(false);
            boton.setBorderPainted(false);
            boton.setContentAreaFilled(false);
            boton.setForeground(new Color(80, 80, 80));
            boton.addActionListener(e -> accionBotonCategoria(textoBoton));
            panelDeNavegacionSur.add(boton);
        }

        panelDeNavegacion.add(panelDeNavegacionNorte, BorderLayout.NORTH);
        panelDeNavegacion.add(panelDeNavegacionSur, BorderLayout.SOUTH);

        JPanel panelWEST = new JPanel();
        panelWEST.setBackground(Color.WHITE);

        JPanel panelEAST = new JPanel();
        panelEAST.setBackground(Color.WHITE);

        // En el centro muestro la lista de conversaciones o el chat abierto
        panelContenido.setBackground(Color.WHITE);
        panelPrincipal.add(panelContenido, BorderLayout.CENTER);
        panelPrincipal.add(panelDeNavegacion, BorderLayout.NORTH);
        panelPrincipal.add(panelWEST, BorderLayout.WEST);
        panelPrincipal.add(panelEAST, BorderLayout.EAST);

        this.add(panelPrincipal);
    }

    private void configurarEventos() {
        
    }

    private void accionBotonCategoria(String categoria) {
        System.out.println("Se ha seleccionado la categoría: " + categoria);
    }

    public GestorTienda getGestor() {
        return gestor;
    }

    // Desde el boton de un articulo llamo a este metodo para contactar al vendedor
    public void contactarVendedor(Articulo articulo) {
        if (gestor.getUsuarioActivo() == null) {
            JOptionPane.showMessageDialog(this, "Inicia sesion para enviar mensajes");
            return;
        }

        try {
            Conversacion conversacion = gestor.contactarVendedor(articulo);
            abrirChat(conversacion);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }

    private void abrirChat(Conversacion conversacion) {
        PanelChat chat = new PanelChat(conversacion, gestor.getUsuarioActivo(), this);
        mostrarContenido(chat);
    }

    // Este metodo tambien se llama al pulsar Volver a mensajes dentro del chat
    public void mostrarConversaciones() {
        Usuario usuario = gestor.getUsuarioActivo();
        if (usuario == null) {
            JOptionPane.showMessageDialog(this, "Inicia sesion para ver tus mensajes");
            return;
        }

        JPanel mensajes = new JPanel(new BorderLayout(10, 10));
        mensajes.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mensajes.add(new JLabel("Tus mensajes"), BorderLayout.NORTH);

        JPanel lista = new JPanel();
        lista.setLayout(new BoxLayout(lista, BoxLayout.Y_AXIS));
        boolean hayConversaciones = false;

        // El gestor devuelve las conversaciones en las que participa el usuario
        for (Conversacion conversacion : gestor.getConversacionesUsuarioActivo()) {
            hayConversaciones = true;
            Usuario otro;
            if (usuario == conversacion.getComprador()) {
                otro = conversacion.getVendedor();
            } else {
                otro = conversacion.getComprador();
            }

            JPanel fila = new JPanel(new BorderLayout(10, 10));
            fila.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            fila.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));

            JPanel informacion = new JPanel(new GridLayout(0, 1));
            informacion.add(new JLabel(conversacion.getArticulo().getTitulo()));
            informacion.add(new JLabel("Chat con " + otro.getNombreUsuario()));

            String ultimoMensaje = "Sin mensajes todavia";
            for (Mensaje mensaje : conversacion.getMensajes()) {
                ultimoMensaje = mensaje.getTexto();
            }
            informacion.add(new JLabel(ultimoMensaje));
            fila.add(informacion, BorderLayout.CENTER);

            JPanel botones = new JPanel();
            JButton abrir = new JButton("Abrir chat");
            abrir.addActionListener(e -> {
                abrirChat(conversacion);
            });
            botones.add(abrir);

            JButton ventana = new JButton("Ventana aparte");
            ventana.addActionListener(e -> {
                VentanaChat chat = new VentanaChat(conversacion, usuario);
                chat.setVisible(true);
            });
            botones.add(ventana);
            fila.add(botones, BorderLayout.EAST);
            lista.add(fila);
        }

        if (!hayConversaciones) {
            lista.add(new JLabel("Todavia no tienes conversaciones"));
        }

        mensajes.add(new JScrollPane(lista), BorderLayout.CENTER);
        mostrarContenido(mensajes);
    }

    // Cambio solo el panel central para mantener la barra de navegacion
    private void mostrarContenido(JPanel panel) {
        panelContenido.removeAll();
        panelContenido.add(panel, BorderLayout.CENTER);
        panelContenido.revalidate();
        panelContenido.repaint();
    }
}
