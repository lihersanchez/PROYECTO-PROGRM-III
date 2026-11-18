package domain;

import java.util.ArrayList;
import java.util.List;

public class GestorTienda {
    private List<Usuario> usuarios;
    private List<Articulo> catalogo;
    private List<Transaccion> transacciones;
    private List<Conversacion> conversaciones;
    private Usuario usuarioActivo;

    public GestorTienda() {
        this.usuarios = new ArrayList<>();
        this.catalogo = new ArrayList<>();
        this.transacciones = new ArrayList<>();
        this.conversaciones = new ArrayList<>();
        cargarDatosIniciales();
    }

    private void cargarDatosIniciales() {
        Usuario u1 = new Usuario("Moreno", "moreno@mail.com", "1234", "Bilbao");
        Usuario u2 = new Usuario("Ignacio", "ignacio@mail.com", "1234", "Madrid");
        Usuario u3 = new Usuario("TechStore", "tech@mail.com", "1234", "Barcelona");
        Usuario admin = new Usuario("admin", "admin@urbandeal.local", "Admin1234", "Bilbao", true);

        usuarios.add(u1);
        usuarios.add(u2);
        usuarios.add(u3);
        usuarios.add(admin);

        publicarArticulo(new Articulo("Chaqueta vaquera", "Corte oversize, perfecta para entretiempo", "Levi's", "M", 25.00,
                Categoria.MUJER, Subcategoria.ABRIGOS, EstadoArticulo.MUY_BUENO, u1, "resources images/chaqueta.jpg"));

        publicarArticulo(new Articulo("Zapatillas Air Max", "Poco uso, incluye caja original", "Nike", "42", 45.00,
                Categoria.HOMBRE, Subcategoria.CALZADO, EstadoArticulo.BUENO, u2, "resources images/zapatillas.jpg"));

        publicarArticulo(new Articulo("Auriculares BT WH-1000", "Cancelación activa de ruido, batería intacta", "Sony", "Única", 60.00,
                Categoria.ELECTRONICA, Subcategoria.AUDIO, EstadoArticulo.NUEVO_CON_ETIQUETA, u3, "resources images/auriculares.jpg"));

        publicarArticulo(new Articulo("Vestido floral verano", "Tejido ligero, puesto una sola vez", "Zara", "S", 15.50,
                Categoria.MUJER, Subcategoria.VESTIDOS, EstadoArticulo.NUEVO_SIN_ETIQUETA, u1, "resources images/vestido.jpg"));

        publicarArticulo(new Articulo("Lámpara minimalista", "Luz cálida, ideal para escritorio", "Ikea", "Única", 12.00,
                Categoria.HOGAR, Subcategoria.DECORACION, EstadoArticulo.MUY_BUENO, u2, "resources images/lampara.jpg"));
    }

    public boolean registrarUsuario(String nombre, String email, String password, String ciudad) {
        for (Usuario u : usuarios) {
            if (u.getNombreUsuario().equalsIgnoreCase(nombre) || u.getEmail().equalsIgnoreCase(email)) {
                return false;
            }
        }
        Usuario nuevo = new Usuario(nombre, email, password, ciudad);
        usuarios.add(nuevo);
        this.usuarioActivo = nuevo;
        return true;
    }

    public boolean iniciarSesion(String identificador, String password) {
        for (Usuario u : usuarios) {
            boolean coincideUsuario = u.getNombreUsuario().equalsIgnoreCase(identificador)
                    || u.getEmail().equalsIgnoreCase(identificador);
            if (coincideUsuario && u.getPassword().equals(password)) {
                this.usuarioActivo = u;
                return true;
            }
        }
        return false;
    }

    public void cerrarSesion() {
        this.usuarioActivo = null;
    }

    public Usuario getUsuarioActivo() {
        return usuarioActivo;
    }

    public boolean esAdministradorActivo() {
        return usuarioActivo != null && usuarioActivo.esAdministrador();
    }

    public List<EstadisticaUsuario> getEstadisticasUsuarios() {
        List<EstadisticaUsuario> resultado = new ArrayList<>();
        for (Usuario usuario : usuarios) {
            int publicados = 0;
            int disponibles = 0;
            int compras = 0;
            int ventas = 0;
            double gastado = 0;
            double ingresos = 0;
            double importeArticulos = 0;
            double envios = 0;
            double tasas = 0;

            for (Articulo articulo : catalogo) {
                if (articulo.getVendedor() == usuario) {
                    publicados++;
                    if (!articulo.isVendido()) {
                        disponibles++;
                    }
                }
            }

            for (Transaccion transaccion : transacciones) {
                if (transaccion.getComprador() == usuario) {
                    compras++;
                    gastado += transaccion.getTotalPagado();
                    importeArticulos += transaccion.getPrecioArticulo();
                    envios += transaccion.getCosteEnvio();
                    tasas += transaccion.getTasaProteccionComprador();
                }
                if (transaccion.getVendedor() == usuario) {
                    ventas++;
                    ingresos += transaccion.getPrecioArticulo();
                }
            }

            resultado.add(new EstadisticaUsuario(usuario.getNombreUsuario(), publicados, disponibles,
                    compras, ventas, gastado, ingresos, importeArticulos, envios, tasas));
        }
        return resultado;
    }

    public void publicarArticulo(Articulo art) {
        catalogo.add(art);
        art.getVendedor().publicarArticulo(art);
    }

    public List<Articulo> getArticulosDisponibles() {
        List<Articulo> disponibles = new ArrayList<>();
        for (Articulo a : catalogo) {
            if (!a.isVendido()) {
                disponibles.add(a);
            }
        }
        return disponibles;
    }

    public List<Articulo> filtrarPorCategoria(Categoria cat) {
        List<Articulo> filtrados = new ArrayList<>();
        for (Articulo a : catalogo) {
            if (!a.isVendido() && a.getCategoria() == cat) {
                filtrados.add(a);
            }
        }
        return filtrados;
    }

    public List<Articulo> buscarPorTexto(String texto) {
        String q = texto.toLowerCase().trim();
        if (q.isEmpty()) {
            return getArticulosDisponibles();
        }
        List<Articulo> resultados = new ArrayList<>();
        for (Articulo a : catalogo) {
            if (!a.isVendido() && (a.getTitulo().toLowerCase().contains(q)
                    || a.getMarca().toLowerCase().contains(q)
                    || a.getDescripcion().toLowerCase().contains(q))) {
                resultados.add(a);
            }
        }
        return resultados;
    }

    public Transaccion comprarArticulo(Articulo articulo, double costeEnvio) throws IllegalStateException {
        if (usuarioActivo == null) {
            throw new IllegalStateException("Debes iniciar sesión para comprar.");
        }
        if (articulo.isVendido()) {
            throw new IllegalStateException("Este artículo ya ha sido vendido.");
        }
        if (articulo.getVendedor() == usuarioActivo) {
            throw new IllegalStateException("No puedes comprar tu propio artículo.");
        }

        double tasaProteccion = 0.70 + (articulo.getPrecio() * 0.05);
        double total = articulo.getPrecio() + tasaProteccion + costeEnvio;

        if (!usuarioActivo.descontarSaldo(total)) {
            throw new IllegalStateException(String.format("Saldo insuficiente. Necesitas %.2f € y tienes %.2f €.",
                    total, usuarioActivo.getSaldo()));
        }

        articulo.getVendedor().ingresarSaldo(articulo.getPrecio());

        Transaccion transaccion = new Transaccion(articulo, usuarioActivo, costeEnvio);
        transacciones.add(transaccion);
        usuarioActivo.agregarCompra(transaccion);
        return transaccion;
    }

    public Conversacion contactarVendedor(Articulo articulo) {
        if (usuarioActivo == null) {
            throw new IllegalStateException("Inicia sesión para contactar al vendedor.");
        }
        for (Conversacion conversacion : conversaciones) {
            if (conversacion.getArticulo() == articulo && conversacion.getComprador() == usuarioActivo) {
                return conversacion;
            }
        }
        Conversacion nueva = new Conversacion(articulo, usuarioActivo);
        conversaciones.add(nueva);
        return nueva;
    }

    public List<Conversacion> getConversacionesUsuarioActivo() {
        List<Conversacion> resultado = new ArrayList<>();
        if (usuarioActivo == null) {
            return resultado;
        }
        for (Conversacion c : conversaciones) {
            if (c.getComprador() == usuarioActivo || c.getVendedor() == usuarioActivo) {
                resultado.add(c);
            }
        }
        return resultado;
    }
}