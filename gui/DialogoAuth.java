package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import domain.GestorTienda;

public class DialogoAuth extends JDialog {
    private static final long serialVersionUID = 1L;
    private boolean autenticado = false;

    public DialogoAuth(JFrame padre, GestorTienda gestor) {
        super(padre, "Acceso a UrbanDeal", true);
        setSize(420, 380);
        setLocationRelativeTo(padre);

        JTabbedPane tabs = new JTabbedPane();

        // Pestaña 1: Iniciar sesión
        JPanel pLogin = new JPanel(new GridLayout(6, 1, 8, 8));
        pLogin.setBorder(new EmptyBorder(15, 25, 15, 25));

        JLabel lblHint = new JLabel("Prueba: Laura_98, CarlosM, admin (clave: 1234 / Admin1234)");
        lblHint.setFont(new Font("SansSerif", Font.ITALIC, 11));
        lblHint.setForeground(Color.GRAY);

        JTextField txtUser = new JTextField();
        JPasswordField txtPass = new JPasswordField();

        JButton btnEntrar = new JButton("Iniciar sesión");
        btnEntrar.setBackground(JFramePrincipal.COLOR_PRIMARIO);
        btnEntrar.setForeground(Color.WHITE);

        JButton btnDemo = new JButton("Entrar como Laura_98");
        btnDemo.addActionListener(e -> {
            gestor.iniciarSesion("Laura_98", "1234");
            autenticado = true;
            dispose();
        });

        pLogin.add(lblHint);
        pLogin.add(new JLabel("Usuario o Email:"));
        pLogin.add(txtUser);
        pLogin.add(new JLabel("Contraseña:"));
        pLogin.add(txtPass);

        JPanel pBotonesLogin = new JPanel(new GridLayout(1, 2, 8, 0));
        pBotonesLogin.add(btnEntrar);
        pBotonesLogin.add(btnDemo);
        pLogin.add(pBotonesLogin);

        ActionListener accionLogin = e -> {
            if (gestor.iniciarSesion(txtUser.getText().trim(), new String(txtPass.getPassword()))) {
                autenticado = true;
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Usuario o contraseña incorrectos.",
                        "Error de acceso", JOptionPane.ERROR_MESSAGE);
            }
        };
        btnEntrar.addActionListener(accionLogin);
        txtPass.addActionListener(accionLogin);

        // Pestaña 2: Registro
        JPanel pReg = new JPanel(new GridLayout(9, 1, 5, 5));
        pReg.setBorder(new EmptyBorder(12, 25, 12, 25));

        JTextField rUser = new JTextField();
        JTextField rEmail = new JTextField();
        JTextField rCiudad = new JTextField();
        JPasswordField rPass = new JPasswordField();

        JButton btnRegistrar = new JButton("Crear cuenta");
        btnRegistrar.setBackground(JFramePrincipal.COLOR_PRIMARIO);
        btnRegistrar.setForeground(Color.WHITE);

        pReg.add(new JLabel("Nombre de usuario:"));
        pReg.add(rUser);
        pReg.add(new JLabel("Correo electrónico:"));
        pReg.add(rEmail);
        pReg.add(new JLabel("Ciudad:"));
        pReg.add(rCiudad);
        pReg.add(new JLabel("Contraseña:"));
        pReg.add(rPass);
        pReg.add(btnRegistrar);

        btnRegistrar.addActionListener(e -> {
            String nombre = rUser.getText().trim();
            String email = rEmail.getText().trim();
            String ciudad = rCiudad.getText().trim();
            String pass = new String(rPass.getPassword());

            if (nombre.isEmpty() || email.isEmpty() || pass.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Rellena usuario, email y contraseña.",
                        "Campos incompletos", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (!email.contains("@")) {
                JOptionPane.showMessageDialog(this, "Introduce un correo electrónico válido.",
                        "Email inválido", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (gestor.registrarUsuario(nombre, email, pass, ciudad.isEmpty() ? "Sin especificar" : ciudad)) {
                autenticado = true;
                JOptionPane.showMessageDialog(this,
                        "¡Bienvenido a UrbanDeal, " + nombre + "! Tienes 100,00 € de saldo inicial.");
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Ese nombre de usuario o email ya existe.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        tabs.addTab("Iniciar sesión", pLogin);
        tabs.addTab("Registrarse", pReg);
        add(tabs);
    }

    public boolean isAutenticado() {
        return autenticado;
    }
}