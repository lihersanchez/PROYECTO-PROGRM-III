package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import domain.Articulo;
import domain.Categoria;
import domain.Conversacion;
import domain.GestorTienda;
import domain.Usuario;

public class JFramePrincipal extends JFrame {
    private static final long serialVersionUID = 1L;

    public static final Color COLOR_PRIMARIO = new Color(0, 119, 130);
    public static final Color COLOR_FONDO_GRIS = new Color(245, 246, 247);

    private final GestorTienda gestor = new GestorTienda();
    private final JPanel contenido = new JPanel(new BorderLayout());

    private JTextField txtBuscar;
    private JButton btnLogin;
    private JButton btnPerfil;
    private JButton btnEstadisticas;
    private JLabel lblSaldoHeader;

    public JFramePrincipal() {
        configurarVentana();
        inicializarComponentes();
        cargarCatalogoEnSegundoPlano();
    }

    private void configurarVentana() {
        setTitle("UrbanDeal");
        setSize(1280, 800);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        ImageIcon icono = new ImageIcon("resources images/logo.jpg");
        if (icono.getImage() != null) {
            this.setIconImage(icono.getImage());
        }
    }

    // Requisito Entrega 1: Thread independiente de la GUI para tarea costosa de carga
    private void cargarCatalogoEnSegundoPlano() {
        JPanel panelCarga = new JPanel(new BorderLayout(10, 10));
        panelCarga.setBackground(COLOR_FONDO_GRIS);
        panelCarga.setBorder(new EmptyBorder(220, 350, 220, 350));

        JLabel lblCargando = new JLabel("Cargando catálogo de UrbanDeal...", SwingConstants.CENTER);
        lblCargando.setFont(new Font("SansSerif", Font.BOLD, 16));

        JProgressBar barraProgreso = new JProgressBar(0, 100);
        barraProgreso.setStringPainted(true);
        barraProgreso.setForeground(COLOR_PRIMARIO);

        panelCarga.add(lblCargando, BorderLayout.NORTH);
        panelCarga.add(barraProgreso, BorderLayout.CENTER);
        mostrarContenido(panelCarga);

        Thread hiloCarga = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    for (int i = 0; i <= 100; i += 20) {
                        final int progreso = i;
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                barraProgreso.setValue(progreso);
                            }
                        });
                        Thread.sleep(120);
                    }
                    final List<Articulo> iniciales = gestor.getArticulosDisponibles();
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            mostrarCatalogo(iniciales, "Novedades en UrbanDeal");
                        }
                    });
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });
        hiloCarga.start();
    }

    private void inicializarComponentes() {
        JPanel panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.setBackground(Color.WHITE);

        JPanel panelDeNavegacion = new JPanel(new BorderLayout());
        panelDeNavegacion.setBackground(Color.WHITE);
        panelDeNavegacion.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(220, 220, 220)));

        JPanel panelDeNavegacionNorte = new JPanel(new BorderLayout(15, 0));
        panelDeNavegacionNorte.setBackground(Color.WHITE);
        panelDeNavegacionNorte.setBorder(new EmptyBorder(10, 25, 10, 25));

        // Logo con MouseListener
        JLabel lblLogo = new JLabel("UrbanDeal");
        lblLogo.setFont(new Font("SansSerif", Font.BOLD, 26));
        lblLogo.setForeground(COLOR_PRIMARIO);
        File archivoLogo = new File("resources images/logoconnombre.jpg");
        if (archivoLogo.exists()) {
            ImageIcon iconoOriginal = new ImageIcon(archivoLogo.getPath());
            Image imagenEscalada = iconoOriginal.getImage().getScaledInstance(180, 55, Image.SCALE_SMOOTH);
            lblLogo = new JLabel(new ImageIcon(imagenEscalada));
        }

        lblLogo.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        lblLogo.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                txtBuscar.setText("");
                mostrarCatalogo(gestor.getArticulosDisponibles(), "Novedades en UrbanDeal");
            }
        });
        panelDeNavegacionNorte.add(lblLogo, BorderLayout.WEST);

        // Buscador con KeyListener (requisito obligatorio Entrega 1)
        JPanel panelBuscador = new JPanel(new BorderLayout(8, 0));
        panelBuscador.setBackground(new Color(240, 242, 242));
        panelBuscador.setBorder(new EmptyBorder(6, 12, 6, 12));

        txtBuscar = new JTextField();
        txtBuscar.setFont(new Font("SansSerif", Font.PLAIN, 14));
        txtBuscar.setBackground(new Color(240, 242, 242));
        txtBuscar.setBorder(null);
        txtBuscar.setToolTipText("Escribe para buscar en tiempo real o pulsa ESC para limpiar");

        txtBuscar.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    txtBuscar.setText("");
                    mostrarCatalogo(gestor.getArticulosDisponibles(), "Todos los artículos");
                } else {
                    ejecutarBusqueda();
                }
            }
        });

        JButton btnBuscar = new JButton("Buscar");
        btnBuscar.setBackground(Color.WHITE);
        btnBuscar.setForeground(COLOR_PRIMARIO);
        btnBuscar.setFocusable(false);
        btnBuscar.addActionListener(e -> ejecutarBusqueda());

        panelBuscador.add(new JLabel("Buscar:"), BorderLayout.WEST);
        panelBuscador.add(txtBuscar, BorderLayout.CENTER);
        panelBuscador.add(btnBuscar, BorderLayout.EAST);
        panelDeNavegacionNorte.add(panelBuscador, BorderLayout.CENTER);

        // Botones de navegación
        JPanel panelAcciones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 5));
        panelAcciones.setBackground(Color.WHITE);

        lblSaldoHeader = new JLabel("");
        lblSaldoHeader.setFont(new Font("SansSerif", Font.BOLD, 13));
        lblSaldoHeader.setForeground(new Color(60, 60, 60));

        JButton btnTienda = crearBotonNav("Tienda");
        btnTienda.addActionListener(e -> mostrarCatalogo(gestor.getArticulosDisponibles(), "Todos los artículos"));

        JButton btnMensajes = crearBotonNav("Mensajes");
        btnMensajes.addActionListener(e -> mostrarConversaciones());

        btnEstadisticas = crearBotonNav("Estadísticas");
        btnEstadisticas.setVisible(false);
        btnEstadisticas.addActionListener(e -> {
            if (gestor.esAdministradorActivo()) {
                mostrarContenido(new PanelEstadisticas(gestor));
            } else {
                JOptionPane.showMessageDialog(this, "Acceso reservado al administrador.",
                        "Acceso denegado", JOptionPane.WARNING_MESSAGE);
            }
        });

        btnPerfil = crearBotonNav("Mi Perfil");
        btnPerfil.setVisible(false);
        btnPerfil.addActionListener(e -> {
            if (gestor.getUsuarioActivo() != null) {
                DialogoPerfil dialogo = new DialogoPerfil(this, gestor);
                dialogo.setVisible(true);
                actualizarEstadoSesionUI();
            }
        });

        btnLogin = crearBotonNav("Regístrate | Inicia sesión");
        btnLogin.addActionListener(e -> gestionarBotonAuth());

        JButton btnVender = new JButton("Vender ahora");
        btnVender.setBackground(COLOR_PRIMARIO);
        btnVender.setForeground(Color.WHITE);
        btnVender.setFont(new Font("SansSerif", Font.BOLD, 13));
        btnVender.setFocusable(false);
        btnVender.addActionListener(e -> {
            if (asegurarSesion()) {
                DialogoVender dialogo = new DialogoVender(this, gestor);
                dialogo.setVisible(true);
                if (dialogo.isPublicado()) {
                    mostrarCatalogo(gestor.getArticulosDisponibles(), "Novedades en UrbanDeal");
                }
            }
        });

        panelAcciones.add(lblSaldoHeader);
        panelAcciones.add(btnTienda);
        panelAcciones.add(btnMensajes);
        panelAcciones.add(btnEstadisticas);
        panelAcciones.add(btnPerfil);
        panelAcciones.add(btnLogin);
        panelAcciones.add(btnVender);

        panelDeNavegacionNorte.add(panelAcciones, BorderLayout.EAST);

        // Barra de categorías
        JPanel panelDeNavegacionSur = new JPanel(new FlowLayout(FlowLayout.CENTER, 25, 8));
        panelDeNavegacionSur.setBackground(Color.WHITE);

        JButton btnTodo = crearBotonCategoria("Todo");
        btnTodo.addActionListener(e -> mostrarCatalogo(gestor.getArticulosDisponibles(), "Todos los artículos"));
        panelDeNavegacionSur.add(btnTodo);

        for (Categoria cat : Categoria.values()) {
            JButton boton = crearBotonCategoria(cat.toString());
            boton.addActionListener(e -> mostrarCatalogo(gestor.filtrarPorCategoria(cat), "Categoría: " + cat.toString()));
            panelDeNavegacionSur.add(boton);
        }

        panelDeNavegacion.add(panelDeNavegacionNorte, BorderLayout.NORTH);
        panelDeNavegacion.add(panelDeNavegacionSur, BorderLayout.SOUTH);

        panelPrincipal.add(panelDeNavegacion, BorderLayout.NORTH);
        panelPrincipal.add(contenido, BorderLayout.CENTER);

        this.add(panelPrincipal);
    }

    private JButton crearBotonNav(String texto) {
        JButton btn = new JButton(texto);
        btn.setForeground(COLOR_PRIMARIO);
        btn.setBackground(Color.WHITE);
        btn.setFont(new Font("SansSerif", Font.BOLD, 13));
        btn.setFocusable(false);
        return btn;
    }

    private JButton crearBotonCategoria(String texto) {
        JButton boton = new JButton(texto);
        boton.setFocusable(false);
        boton.setFont(new Font("SansSerif", Font.PLAIN, 15));
        boton.setBorderPainted(false);
        boton.setContentAreaFilled(false);
        boton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        boton.setForeground(new Color(80, 80, 80));

        boton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                boton.setForeground(COLOR_PRIMARIO);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                boton.setForeground(new Color(80, 80, 80));
            }
        });
        return boton;
    }

    private void mostrarContenido(Component panel) {
        contenido.removeAll();
        contenido.add(panel, BorderLayout.CENTER);
        contenido.revalidate();
        contenido.repaint();
    }

    private void ejecutarBusqueda() {
        String query = txtBuscar.getText();
        List<Articulo> resultados = gestor.buscarPorTexto(query);
        String titulo = query.trim().isEmpty()
                ? "Todos los artículos"
                : "Resultados para \"" + query + "\" (" + resultados.size() + ")";
        mostrarCatalogo(resultados, titulo);
    }

    public void mostrarCatalogo(List<Articulo> articulos, String titulo) {
        JPanel contenedor = new JPanel(new BorderLayout());
        contenedor.setBackground(COLOR_FONDO_GRIS);
        contenedor.setBorder(new EmptyBorder(20, 40, 20, 40));

        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 22));
        lblTitulo.setForeground(new Color(40, 40, 40));
        lblTitulo.setBorder(new EmptyBorder(0, 0, 15, 0));
        contenedor.add(lblTitulo, BorderLayout.NORTH);

        JPanel grid = new JPanel(new GridLayout(0, 4, 20, 20));
        grid.setBackground(COLOR_FONDO_GRIS);

        if (articulos.isEmpty()) {
            JLabel lblVacio = new JLabel("No hay artículos disponibles en esta categoría o búsqueda.");
            lblVacio.setFont(new Font("SansSerif", Font.ITALIC, 16));
            lblVacio.setForeground(Color.GRAY);
            grid.add(lblVacio);
        } else {
            for (Articulo art : articulos) {
                grid.add(crearTarjetaArticulo(art));
            }
        }

        JPanel panelEnvoltorGrid = new JPanel(new BorderLayout());
        panelEnvoltorGrid.setBackground(COLOR_FONDO_GRIS);
        panelEnvoltorGrid.add(grid, BorderLayout.NORTH);

        JScrollPane scroll = new JScrollPane(panelEnvoltorGrid);
        scroll.setBorder(null);
        contenedor.add(scroll, BorderLayout.CENTER);
        mostrarContenido(contenedor);
    }

    private JPanel crearTarjetaArticulo(Articulo art) {
        JPanel tarjeta = new JPanel(new BorderLayout());
        tarjeta.setBackground(Color.WHITE);
        tarjeta.setBorder(BorderFactory.createLineBorder(new Color(225, 225, 225), 1));

        JPanel topCard = new JPanel(new BorderLayout());
        topCard.setBackground(Color.WHITE);
        topCard.setBorder(new EmptyBorder(8, 10, 8, 10));
        JLabel lblVendedor = new JLabel(art.getVendedor().getNombreUsuario() + " (" + art.getVendedor().getCiudad() + ")");
        lblVendedor.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblVendedor.setForeground(Color.GRAY);
        topCard.add(lblVendedor, BorderLayout.WEST);

        JLabel lblImagen = new JLabel("", SwingConstants.CENTER);
        lblImagen.setPreferredSize(new Dimension(240, 190));
        lblImagen.setBackground(new Color(248, 248, 248));
        lblImagen.setOpaque(true);
        lblImagen.setIcon(cargarIcono(art.getRutaImagen(), 220, 180));
        lblImagen.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        lblImagen.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                abrirDetalleArticulo(art);
            }
        });

        JPanel pieTarjeta = new JPanel();
        pieTarjeta.setLayout(new BoxLayout(pieTarjeta, BoxLayout.Y_AXIS));
        pieTarjeta.setBackground(Color.WHITE);
        pieTarjeta.setBorder(new EmptyBorder(10, 12, 12, 12));

        JPanel filaPrecioLikes = new JPanel(new BorderLayout());
        filaPrecioLikes.setBackground(Color.WHITE);

        JLabel lblPrecio = new JLabel(String.format("%.2f €", art.getPrecio()));
        lblPrecio.setFont(new Font("SansSerif", Font.BOLD, 16));

        Usuario activo = gestor.getUsuarioActivo();
        boolean esFav = activo != null && activo.esFavorito(art);
        JButton btnLike = new JButton((esFav ? "♥ " : "♡ ") + art.getLikes());
        btnLike.setFont(new Font("SansSerif", Font.PLAIN, 13));
        btnLike.setForeground(esFav ? Color.RED : Color.GRAY);
        btnLike.setBorderPainted(false);
        btnLike.setContentAreaFilled(false);
        btnLike.setFocusable(false);
        btnLike.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        btnLike.addActionListener(e -> {
            if (!asegurarSesion()) {
                return;
            }
            gestor.getUsuarioActivo().toggleFavorito(art);
            boolean ahoraFav = gestor.getUsuarioActivo().esFavorito(art);
            btnLike.setText((ahoraFav ? "♥ " : "♡ ") + art.getLikes());
            btnLike.setForeground(ahoraFav ? Color.RED : Color.GRAY);
        });

        filaPrecioLikes.add(lblPrecio, BorderLayout.WEST);
        filaPrecioLikes.add(btnLike, BorderLayout.EAST);

        JLabel lblTitulo = new JLabel(art.getTitulo());
        lblTitulo.setFont(new Font("SansSerif", Font.PLAIN, 14));

        JLabel lblMeta = new JLabel(art.getMarca() + " · Talla " + art.getTalla());
        lblMeta.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblMeta.setForeground(Color.GRAY);

        JPanel filaBotones = new JPanel(new GridLayout(1, 2, 6, 0));
        filaBotones.setBackground(Color.WHITE);
        filaBotones.setBorder(new EmptyBorder(8, 0, 0, 0));

        JButton btnVer = new JButton("Ver / Comprar");
        btnVer.setBackground(COLOR_PRIMARIO);
        btnVer.setForeground(Color.WHITE);
        btnVer.setFont(new Font("SansSerif", Font.BOLD, 11));
        btnVer.setFocusable(false);
        btnVer.addActionListener(e -> abrirDetalleArticulo(art));

        JButton btnChat = new JButton("Mensaje");
        btnChat.setBackground(Color.WHITE);
        btnChat.setForeground(COLOR_PRIMARIO);
        btnChat.setFont(new Font("SansSerif", Font.BOLD, 11));
        btnChat.setFocusable(false);
        btnChat.addActionListener(e -> contactarVendedor(art));

        filaBotones.add(btnVer);
        filaBotones.add(btnChat);

        pieTarjeta.add(filaPrecioLikes);
        pieTarjeta.add(Box.createVerticalStrut(4));
        pieTarjeta.add(lblTitulo);
        pieTarjeta.add(lblMeta);
        pieTarjeta.add(filaBotones);

        tarjeta.add(topCard, BorderLayout.NORTH);
        tarjeta.add(lblImagen, BorderLayout.CENTER);
        tarjeta.add(pieTarjeta, BorderLayout.SOUTH);

        return tarjeta;
    }

    public static ImageIcon cargarIcono(String ruta, int ancho, int alto) {
        ImageIcon icono = null;
        if (ruta != null && new File(ruta).exists()) {
            icono = new ImageIcon(ruta);
        }
        if (icono == null || icono.getIconWidth() <= 0) {
            icono = new ImageIcon("resources images/logo.jpg");
        }
        Image imagenEscalada = icono.getImage().getScaledInstance(ancho, alto, Image.SCALE_SMOOTH);
        return new ImageIcon(imagenEscalada);
    }

    private void abrirDetalleArticulo(Articulo art) {
        DialogoDetalleArticulo dialogo = new DialogoDetalleArticulo(this, gestor, art);
        dialogo.setVisible(true);
        if (dialogo.isComprado()) {
            actualizarEstadoSesionUI();
        } else if (dialogo.isQuiereContactar()) {
            contactarVendedor(art);
        }
    }

    public void contactarVendedor(Articulo articulo) {
        if (!asegurarSesion()) {
            return;
        }
        try {
            Conversacion chat = gestor.contactarVendedor(articulo);
            abrirChat(chat);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void abrirChat(Conversacion conversacion) {
        PanelChat panelChat = new PanelChat(conversacion, gestor.getUsuarioActivo(), this);
        mostrarContenido(panelChat);
    }

    public void mostrarConversaciones() {
        if (!asegurarSesion()) {
            return;
        }

        JPanel contenedor = new JPanel(new BorderLayout(10, 10));
        contenedor.setBackground(COLOR_FONDO_GRIS);
        contenedor.setBorder(new EmptyBorder(20, 40, 20, 40));

        JLabel titulo = new JLabel("Tus mensajes");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 22));
        contenedor.add(titulo, BorderLayout.NORTH);

        JPanel lista = new JPanel();
        lista.setLayout(new BoxLayout(lista, BoxLayout.Y_AXIS));
        lista.setBackground(COLOR_FONDO_GRIS);

        List<Conversacion> conversaciones = gestor.getConversacionesUsuarioActivo();
        if (conversaciones.isEmpty()) {
            JLabel vacio = new JLabel("No tienes conversaciones todavía. Ve a Tienda y pulsa 'Mensaje' en un artículo.");
            vacio.setFont(new Font("SansSerif", Font.ITALIC, 15));
            vacio.setForeground(Color.GRAY);
            lista.add(vacio);
        } else {
            for (Conversacion conv : conversaciones) {
                Usuario otro = conv.getComprador() == gestor.getUsuarioActivo()
                        ? conv.getVendedor() : conv.getComprador();
                String ultimoMsg = conv.getMensajes().isEmpty()
                        ? ""
                        : conv.getMensajes().get(conv.getMensajes().size() - 1).getTexto();

                JPanel fila = new JPanel(new BorderLayout(15, 5));
                fila.setBackground(Color.WHITE);
                fila.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(220, 220, 220)),
                        new EmptyBorder(12, 15, 12, 15)
                ));
                fila.setMaximumSize(new Dimension(Integer.MAX_VALUE, 75));

                JLabel lblInfo = new JLabel("<html><b>" + conv.getArticulo().getTitulo() + "</b> · Chat con <b>"
                        + otro.getNombreUsuario() + "</b><br><font color='gray'>" + ultimoMsg + "</font></html>");

                JPanel botonesChat = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
                botonesChat.setBackground(Color.WHITE);

                JButton btnAbrir = new JButton("Abrir chat");
                btnAbrir.setBackground(COLOR_PRIMARIO);
                btnAbrir.setForeground(Color.WHITE);
                btnAbrir.addActionListener(e -> abrirChat(conv));

                botonesChat.add(btnAbrir);

                fila.add(lblInfo, BorderLayout.CENTER);
                fila.add(botonesChat, BorderLayout.EAST);
                lista.add(fila);
                lista.add(Box.createVerticalStrut(10));
            }
        }

        contenedor.add(new JScrollPane(lista), BorderLayout.CENTER);
        mostrarContenido(contenedor);
    }

    public boolean asegurarSesion() {
        if (gestor.getUsuarioActivo() != null) {
            return true;
        }
        DialogoAuth dialogo = new DialogoAuth(this, gestor);
        dialogo.setVisible(true);
        if (dialogo.isAutenticado()) {
            actualizarEstadoSesionUI();
            return true;
        }
        return false;
    }

    private void gestionarBotonAuth() {
        if (gestor.getUsuarioActivo() != null) {
            int confirmar = JOptionPane.showConfirmDialog(this,
                    "¿Seguro que quieres cerrar sesión?", "Cerrar sesión", JOptionPane.YES_NO_OPTION);
            if (confirmar == JOptionPane.YES_OPTION) {
                gestor.cerrarSesion();
                actualizarEstadoSesionUI();
            }
        } else {
            asegurarSesion();
        }
    }

    public void actualizarEstadoSesionUI() {
        btnEstadisticas.setVisible(gestor.esAdministradorActivo());
        Usuario activo = gestor.getUsuarioActivo();
        if (activo != null) {
            btnLogin.setText("Cerrar sesión");
            btnPerfil.setText(activo.getNombreUsuario());
            btnPerfil.setVisible(true);
            lblSaldoHeader.setText(String.format("Saldo: %.2f €", activo.getSaldo()));
        } else {
            btnLogin.setText("Regístrate | Inicia sesión");
            btnPerfil.setVisible(false);
            lblSaldoHeader.setText("");
        }
        mostrarCatalogo(gestor.getArticulosDisponibles(), "Novedades en UrbanDeal");
    }
}