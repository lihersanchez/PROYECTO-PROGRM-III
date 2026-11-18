package domain;

import java.util.ArrayList;
import java.util.List;

public class Usuario {
    private String nombreUsuario;
    private String email;
    private String password;
    private String ciudad;
    private boolean administrador;
    private double saldo;
    private List<Articulo> articulosEnVenta;
    private List<Articulo> favoritos;
    private List<Transaccion> historialCompras;

    public Usuario(String nombreUsuario, String email, String password, String ciudad) {
        this(nombreUsuario, email, password, ciudad, false);
    }

    public Usuario(String nombreUsuario, String email, String password, String ciudad, boolean administrador) {
        this.nombreUsuario = nombreUsuario;
        this.email = email;
        this.password = password;
        this.ciudad = ciudad;
        this.administrador = administrador;
        this.saldo = 100.0;
        this.articulosEnVenta = new ArrayList<>();
        this.favoritos = new ArrayList<>();
        this.historialCompras = new ArrayList<>();
    }

    public void publicarArticulo(Articulo articulo) {
        articulosEnVenta.add(articulo);
    }

    public void agregarCompra(Transaccion t) {
        historialCompras.add(t);
    }

    public void ingresarSaldo(double cantidad) {
        if (cantidad > 0) {
            this.saldo += cantidad;
        }
    }

    public boolean descontarSaldo(double cantidad) {
        if (cantidad > 0 && this.saldo >= cantidad) {
            this.saldo -= cantidad;
            return true;
        }
        return false;
    }

    public void toggleFavorito(Articulo articulo) {
        if (favoritos.contains(articulo)) {
            favoritos.remove(articulo);
            articulo.decrementarLikes();
        } else {
            favoritos.add(articulo);
            articulo.incrementarLikes();
        }
    }

    public boolean esFavorito(Articulo articulo) {
        return favoritos.contains(articulo);
    }

    public String getNombreUsuario() { 
    	return nombreUsuario; 
    	}
    public boolean esAdministrador() { 
    	return administrador; 
    	}
    public String getEmail() { 
    	return email; 
    	}
    public String getPassword() { 
    	return password;
    	}
    public String getCiudad() { 
    	return ciudad; 
    	}
    public double getSaldo() { 
    	return saldo; 
    	}
    public void setSaldo(double saldo) { 
    	this.saldo = saldo; 
    	}
    public List<Articulo> getArticulosEnVenta() { 
    	return articulosEnVenta; 
    	}
    public List<Articulo> getFavoritos() { 
    	return favoritos; 
    	}
    public List<Transaccion> getHistorialCompras() { 
    	return historialCompras; 
    	}
}