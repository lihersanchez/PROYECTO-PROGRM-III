package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import domain.EstadisticaUsuario;
import domain.GestorTienda;

public class PanelEstadisticas extends JPanel {
    private static final long serialVersionUID = 1L;

    private final GestorTienda gestor;
    private final JLabel resumen = new JLabel();
    private final DefaultTableModel modelo;
    private final JTable tabla;

    public PanelEstadisticas(GestorTienda gestor) {
        super(new BorderLayout(12, 12));
        this.gestor = gestor;
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel cabecera = new JPanel(new BorderLayout(10, 10));
        JLabel titulo = new JLabel("Estadísticas de compraventa | Administración");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 20));
        cabecera.add(titulo, BorderLayout.NORTH);
        cabecera.add(resumen, BorderLayout.CENTER);

        JButton actualizar = new JButton("Actualizar datos");
        actualizar.addActionListener(e -> cargarDatos());
        JPanel acciones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        acciones.add(actualizar);
        cabecera.add(acciones, BorderLayout.EAST);
        add(cabecera, BorderLayout.NORTH);

        String[] columnas = {
            "Usuario", "Publicados", "En venta", "Compras", "Ventas", "Gastado (€)", "Ingresos (€)"
        };
        modelo = new DefaultTableModel(columnas, 0) {
            private static final long serialVersionUID = 1L;
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };

        tabla = new JTable(modelo);
        tabla.setRowHeight(28);
        tabla.setFillsViewportHeight(true);

        // Renderizado personalizado para la JTable (requisito obligatorio Entrega 1)
        tabla.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            private static final long serialVersionUID = 1L;
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(242, 248, 249));
                }
                if (column == 0) {
                    setHorizontalAlignment(SwingConstants.LEFT);
                    setFont(getFont().deriveFont(Font.BOLD));
                } else {
                    setHorizontalAlignment(SwingConstants.RIGHT);
                }
                if (value instanceof Double) {
                    setText(String.format("%.2f €", (Double) value));
                }
                return c;
            }
        });

        // Gestión de eventos en la JTable (requisito obligatorio Entrega 1)
        tabla.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && tabla.getSelectedRow() != -1) {
                    int fila = tabla.getSelectedRow();
                    String usuario = (String) modelo.getValueAt(fila, 0);
                    int publicados = (int) modelo.getValueAt(fila, 1);
                    int compras = (int) modelo.getValueAt(fila, 3);
                    int ventas = (int) modelo.getValueAt(fila, 4);
                    double gastado = (double) modelo.getValueAt(fila, 5);
                    double ingresos = (double) modelo.getValueAt(fila, 6);

                    JOptionPane.showMessageDialog(PanelEstadisticas.this,
                            String.format("Resumen de %s:\n- Artículos publicados: %d\n- Compras: %d (%.2f €)\n- Ventas: %d (%.2f €)",
                                    usuario, publicados, compras, gastado, ventas, ingresos),
                            "Detalle de usuario", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        add(new JScrollPane(tabla), BorderLayout.CENTER);
        add(new JLabel("Haz doble clic sobre un usuario de la tabla para ver su ficha detallada."), BorderLayout.SOUTH);
        cargarDatos();
    }

    private void cargarDatos() {
        modelo.setRowCount(0);
        List<EstadisticaUsuario> datos = gestor.getEstadisticasUsuarios();
        int compras = 0;
        double articulos = 0, envios = 0, tasas = 0;
        for (EstadisticaUsuario dato : datos) {
            modelo.addRow(new Object[] {
                dato.getUsuario(), dato.getPublicados(), dato.getDisponibles(),
                dato.getCompras(), dato.getVentas(), dato.getGastado(), dato.getIngresos()
            });
            compras += dato.getCompras();
            articulos += dato.getImporteArticulos();
            envios += dato.getEnvios();
            tasas += dato.getTasas();
        }
        resumen.setText(String.format("Usuarios: %d | Operaciones: %d | Artículos: %.2f € | Envíos: %.2f € | Protección: %.2f €",
                datos.size(), compras, articulos, envios, tasas));
    }
}