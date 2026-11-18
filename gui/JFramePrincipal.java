package gui;

import java.awt.*;
import java.net.URL;
import java.util.Vector;
import javax.swing.*;

public class JFramePrincipal extends JFrame {

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
        btnVender.addActionListener(e -> abrirDialogoVender());

        JComboBox<String> comboIdioma = new JComboBox<>(new String[]{"ES", "EN", "FR"});
        comboIdioma.setBackground(Color.WHITE);

        panelAcciones.add(btnLogin);
        panelAcciones.add(btnVender);
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

        panelPrincipal.add(panelDeNavegacion, BorderLayout.NORTH);
        panelPrincipal.add(panelWEST, BorderLayout.WEST);
        panelPrincipal.add(panelEAST, BorderLayout.EAST);

        this.add(panelPrincipal);
    }

    private Object abrirDialogoVender() {
		
		return null;
	}

	private void configurarEventos() {
        
    }

    private void accionBotonCategoria(String categoria) {
        System.out.println("Se ha seleccionado la categoría: " + categoria);
    }
}