package gui;

import java.awt.Color;
import java.awt.GridLayout;
import java.io.File;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import domain.Articulo;
import domain.Categoria;
import domain.EstadoArticulo;
import domain.GestorTienda;
import domain.Subcategoria;

public class DialogoVender extends JDialog {
    private static final long serialVersionUID = 1L;
    private boolean publicado = false;
    private String rutaSeleccionada = "resources images/logo.jpg";

    public DialogoVender(JFrame padre, GestorTienda gestor) {
        super(padre, "Subir un artículo", true);
        setSize(480, 540);
        setLocationRelativeTo(padre);

        JPanel panel = new JPanel(new GridLayout(10, 2, 10, 10));
        panel.setBorder(new EmptyBorder(20, 25, 20, 25));

        JTextField txtTitulo = new JTextField();
        JTextField txtDesc = new JTextField();
        JTextField txtMarca = new JTextField();
        JTextField txtTalla = new JTextField();
        JTextField txtPrecio = new JTextField();

        JComboBox<Categoria> cbCategoria = new JComboBox<>(Categoria.values());
        JComboBox<Subcategoria> cbSubcategoria = new JComboBox<>();
        JComboBox<EstadoArticulo> cbEstado = new JComboBox<>(EstadoArticulo.values());

        cbCategoria.addActionListener(e -> actualizarSubcategorias(cbCategoria, cbSubcategoria));
        actualizarSubcategorias(cbCategoria, cbSubcategoria);

        JButton btnFoto = new JButton("Seleccionar foto...");
        JLabel lblFoto = new JLabel("Imagen por defecto");
        btnFoto.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            chooser.setFileFilter(new FileNameExtensionFilter("Imágenes (JPG, PNG)", "jpg", "jpeg", "png"));
            if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                File f = chooser.getSelectedFile();
                rutaSeleccionada = f.getAbsolutePath();
                lblFoto.setText(f.getName());
            }
        });

        panel.add(new JLabel("Título:")); panel.add(txtTitulo);
        panel.add(new JLabel("Descripción:")); panel.add(txtDesc);
        panel.add(new JLabel("Marca:")); panel.add(txtMarca);
        panel.add(new JLabel("Talla:")); panel.add(txtTalla);
        panel.add(new JLabel("Precio (€):")); panel.add(txtPrecio);
        panel.add(new JLabel("Categoría:")); panel.add(cbCategoria);
        panel.add(new JLabel("Subcategoría:")); panel.add(cbSubcategoria);
        panel.add(new JLabel("Estado:")); panel.add(cbEstado);
        panel.add(btnFoto); panel.add(lblFoto);

        JButton btnPublicar = new JButton("Publicar artículo");
        btnPublicar.setBackground(JFramePrincipal.COLOR_PRIMARIO);
        btnPublicar.setForeground(Color.WHITE);
        btnPublicar.addActionListener(e -> {
            try {
                String titulo = txtTitulo.getText().trim();
                double precio = Double.parseDouble(txtPrecio.getText().replace(",", "."));
                if (titulo.isEmpty() || precio <= 0) {
                    throw new IllegalArgumentException();
                }

                Articulo nuevo = new Articulo(
                        titulo,
                        txtDesc.getText().trim(),
                        txtMarca.getText().trim().isEmpty() ? "Sin marca" : txtMarca.getText().trim(),
                        txtTalla.getText().trim().isEmpty() ? "Única" : txtTalla.getText().trim(),
                        precio,
                        (Categoria) cbCategoria.getSelectedItem(),
                        (Subcategoria) cbSubcategoria.getSelectedItem(),
                        (EstadoArticulo) cbEstado.getSelectedItem(),
                        gestor.getUsuarioActivo(),
                        rutaSeleccionada
                );
                gestor.publicarArticulo(nuevo);
                publicado = true;
                JOptionPane.showMessageDialog(this, "¡Artículo publicado con éxito!");
                dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Revisa el título e introduce un precio numérico válido.",
                        "Datos incorrectos", JOptionPane.ERROR_MESSAGE);
            }
        });

        panel.add(new JLabel());
        panel.add(btnPublicar);

        add(panel);
    }

    private void actualizarSubcategorias(JComboBox<Categoria> cbCat, JComboBox<Subcategoria> cbSub) {
        Categoria sel = (Categoria) cbCat.getSelectedItem();
        cbSub.removeAllItems();
        for (Subcategoria sub : Subcategoria.values()) {
            if (sub.getCategoriaPadre() == sel) {
                cbSub.addItem(sub);
            }
        }
        if (cbSub.getItemCount() == 0) {
            for (Subcategoria sub : Subcategoria.values()) {
                cbSub.addItem(sub);
            }
        }
    }

    public boolean isPublicado() {
        return publicado;
    }
}