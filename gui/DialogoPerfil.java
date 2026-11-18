package gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.border.EmptyBorder;

import domain.Articulo;
import domain.GestorTienda;
import domain.Transaccion;
import domain.Usuario;

public class DialogoPerfil extends JDialog {
    private static final long serialVersionUID = 1L;

    public DialogoPerfil(JFrame padre, GestorTienda gestor) {
        super(padre, "Perfil de usuario", true);
        Usuario u = gestor.getUsuarioActivo();
        setTitle("Perfil de " + u.getNombreUsuario());
        setSize(620, 460);
        setLocationRelativeTo(padre);

        JPanel root = new JPanel(new BorderLayout(10, 10));
        root.setBorder(new EmptyBorder(15, 15, 15, 15));

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 5));
        JLabel lblInfo = new JLabel(String.format("Usuario: %s (%s) | Saldo: %.2f €",
                u.getNombreUsuario(), u.getCiudad(), u.getSaldo()));
        lblInfo.setFont(new Font("SansSerif", Font.BOLD, 14));

        JButton btnRecargar = new JButton("+ Recargar 50 €");
        btnRecargar.addActionListener(e -> {
            u.ingresarSaldo(50.0);
            lblInfo.setText(String.format("Usuario: %s (%s) | Saldo: %.2f €",
                    u.getNombreUsuario(), u.getCiudad(), u.getSaldo()));
            JOptionPane.showMessageDialog(this, "Se han añadido 50,00 € a tu monedero.");
        });
        top.add(lblInfo);
        top.add(btnRecargar);

        JTabbedPane tabs = new JTabbedPane();

        DefaultListModel<String> modArmario = new DefaultListModel<>();
        for (Articulo a : u.getArticulosEnVenta()) {
            modArmario.addElement(a.getTitulo() + " - " + String.format("%.2f €", a.getPrecio())
                    + (a.isVendido() ? " [VENDIDO]" : " [EN VENTA]"));
        }
        tabs.addTab("Mi Armario (" + u.getArticulosEnVenta().size() + ")", new JScrollPane(new JList<>(modArmario)));

        DefaultListModel<String> modFavs = new DefaultListModel<>();
        for (Articulo a : u.getFavoritos()) {
            modFavs.addElement(a.getTitulo() + " (" + a.getMarca() + ") - " + String.format("%.2f €", a.getPrecio()));
        }
        tabs.addTab("Favoritos (" + u.getFavoritos().size() + ")", new JScrollPane(new JList<>(modFavs)));

        DefaultListModel<String> modCompras = new DefaultListModel<>();
        for (Transaccion t : u.getHistorialCompras()) {
            modCompras.addElement(String.format("Pedido #%d: %s - Total: %.2f €",
                    t.getCodigoPedido(), t.getArticulo().getTitulo(), t.getTotalPagado()));
        }
        tabs.addTab("Mis Compras (" + u.getHistorialCompras().size() + ")", new JScrollPane(new JList<>(modCompras)));

        root.add(top, BorderLayout.NORTH);
        root.add(tabs, BorderLayout.CENTER);
        add(root);
    }
}