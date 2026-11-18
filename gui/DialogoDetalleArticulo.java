package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

import domain.Articulo;
import domain.GestorTienda;
import domain.Transaccion;

public class DialogoDetalleArticulo extends JDialog {
    private static final long serialVersionUID = 1L;
    private boolean comprado = false;
    private boolean quiereContactar = false;

    public DialogoDetalleArticulo(JFramePrincipal padre, GestorTienda gestor, Articulo art) {
        super(padre, art.getTitulo(), true);
        setSize(700, 460);
        setLocationRelativeTo(padre);

        JPanel root = new JPanel(new BorderLayout(20, 20));
        root.setBackground(Color.WHITE);
        root.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel lblImg = new JLabel(JFramePrincipal.cargarIcono(art.getRutaImagen(), 280, 320));
        root.add(lblImg, BorderLayout.WEST);

        JPanel info = new JPanel();
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
        info.setBackground(Color.WHITE);

        JLabel lblTit = new JLabel(art.getTitulo());
        lblTit.setFont(new Font("SansSerif", Font.BOLD, 22));

        JLabel lblPrec = new JLabel(String.format("%.2f €", art.getPrecio()));
        lblPrec.setFont(new Font("SansSerif", Font.BOLD, 20));
        lblPrec.setForeground(JFramePrincipal.COLOR_PRIMARIO);

        double tasa = 0.70 + (art.getPrecio() * 0.05);
        JLabel lblTasa = new JLabel(String.format("+ %.2f € Protección al comprador", tasa));
        lblTasa.setForeground(Color.GRAY);

        JTextArea txtDesc = new JTextArea(art.getDescripcion());
        txtDesc.setWrapStyleWord(true);
        txtDesc.setLineWrap(true);
        txtDesc.setEditable(false);
        txtDesc.setBackground(new Color(248, 248, 248));
        txtDesc.setBorder(new EmptyBorder(8, 8, 8, 8));

        JComboBox<String> cbEnvio = new JComboBox<>(new String[] {
            "Punto Pack - 2.99 €",
            "Envío a domicilio - 4.99 €"
        });

        JPanel panelBotones = new JPanel(new GridLayout(1, 2, 10, 0));
        panelBotones.setBackground(Color.WHITE);

        JButton btnComprar = new JButton("Comprar ahora");
        btnComprar.setBackground(JFramePrincipal.COLOR_PRIMARIO);
        btnComprar.setForeground(Color.WHITE);
        btnComprar.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnComprar.addActionListener(e -> {
            if (!padre.asegurarSesion()) {
                return;
            }
            double costeEnvio = cbEnvio.getSelectedIndex() == 0 ? 2.99 : 4.99;
            try {
                Transaccion t = gestor.comprarArticulo(art, costeEnvio);
                comprado = true;
                JOptionPane.showMessageDialog(this,
                        String.format("¡Compra realizada!\nPedido #%d\nTotal pagado: %.2f €",
                                t.getCodigoPedido(), t.getTotalPagado()),
                        "Confirmación de compra", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } catch (IllegalStateException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(),
                        "No se pudo completar la compra", JOptionPane.WARNING_MESSAGE);
            }
        });

        JButton btnChat = new JButton("Contactar vendedor");
        btnChat.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnChat.addActionListener(e -> {
            quiereContactar = true;
            dispose();
        });

        panelBotones.add(btnComprar);
        panelBotones.add(btnChat);

        info.add(lblTit);
        info.add(Box.createVerticalStrut(5));
        info.add(lblPrec);
        info.add(lblTasa);
        info.add(Box.createVerticalStrut(12));
        info.add(new JLabel("Marca: " + art.getMarca() + " | Talla: " + art.getTalla()));
        info.add(new JLabel("Estado: " + art.getEstado()));
        info.add(new JLabel("Vendedor: " + art.getVendedor().getNombreUsuario() + " (" + art.getVendedor().getCiudad() + ")"));
        info.add(Box.createVerticalStrut(10));
        info.add(txtDesc);
        info.add(Box.createVerticalStrut(10));
        info.add(cbEnvio);
        info.add(Box.createVerticalStrut(12));
        info.add(panelBotones);

        root.add(info, BorderLayout.CENTER);
        add(root);
    }

    public boolean isComprado() { 
    	return comprado; 
    	}
    public boolean isQuiereContactar() { 
    	return quiereContactar; 
    	}
}