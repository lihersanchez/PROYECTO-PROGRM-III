package domain;

import java.time.LocalDateTime;

public class Transaccion {
    private static int contadorPedidos = 1000;

    private int codigoPedido;
    private Articulo articulo;
    private Usuario comprador;
    private Usuario vendedor;
    private double precioArticulo;
    private double tasaProteccionComprador;
    private double costeEnvio;
    private LocalDateTime fechaCompra;

    public Transaccion(Articulo articulo, Usuario comprador, double costeEnvio) {
        this.codigoPedido = contadorPedidos++;
        this.articulo = articulo;
        this.comprador = comprador;
        this.vendedor = articulo.getVendedor();
        this.precioArticulo = articulo.getPrecio();
        this.tasaProteccionComprador = 0.70 + (precioArticulo * 0.05);
        this.costeEnvio = costeEnvio;
        this.fechaCompra = LocalDateTime.now();
        this.articulo.setVendido(true);
    }

    public double getTotalPagado() {
        return precioArticulo + tasaProteccionComprador + costeEnvio;
    }

    public double getPrecioArticulo() { 
    	return precioArticulo;
    	}
    public double getCosteEnvio() { 
    	return costeEnvio;
    	}
    public double getTasaProteccionComprador() { 
    	return tasaProteccionComprador; 
    	}
    public LocalDateTime getFechaCompra() { 
    	return fechaCompra; 
    	}
    public int getCodigoPedido() { 
    	return codigoPedido; 
    	}
    public Articulo getArticulo() { 
    	return articulo; 
    	}
    public Usuario getComprador() { 
    	return comprador; 
    	}
    public Usuario getVendedor() { 
    	return vendedor; 
    	}
}