package domain;

import java.time.LocalDate;

public class Articulo {
    private static int contadorId = 1;

    private int id;
    private String titulo;
    private String descripcion;
    private String marca;
    private String talla;
    private double precio;
    private Categoria categoria;
    private Subcategoria subcategoria;
    private EstadoArticulo estado;
    private Usuario vendedor;
    private String rutaImagen;
    private boolean vendido;
    private int likes;
    private LocalDate fechaPublicacion;

    public Articulo(String titulo, String descripcion, String marca, String talla, double precio,
                    Categoria categoria, Subcategoria subcategoria, EstadoArticulo estado,
                    Usuario vendedor, String rutaImagen) {
        this.id = contadorId++;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.marca = marca;
        this.talla = talla;
        this.precio = precio;
        this.categoria = categoria;
        this.subcategoria = subcategoria;
        this.estado = estado;
        this.vendedor = vendedor;
        this.rutaImagen = rutaImagen;
        this.vendido = false;
        this.likes = 0;
        this.fechaPublicacion = LocalDate.now();
    }

    public void incrementarLikes() { 
    	this.likes++; 
    	}
    public void decrementarLikes() { 
    	if (this.likes > 0) 
    		this.likes--; 
    	}

    public int getId() { 
    	return id; 
    	}
    public String getTitulo() { 
    	return titulo; 
    	}
    public String getDescripcion() { 
    	return descripcion; 
    	}
    public String getMarca() { 
    	return marca; 
    	}
    public String getTalla() { 
    	return talla; 
    	}
    public double getPrecio() { 
    	return precio; 
    	}
    public Categoria getCategoria() { 
    	return categoria; 
    	}
    public Subcategoria getSubcategoria() { 
    	return subcategoria; 
    	}
    public EstadoArticulo getEstado() { 
    	return estado;
    	}
    public Usuario getVendedor() { 
    	return vendedor;
    	}
    public String getRutaImagen() { 
    	return rutaImagen; 
    	}
    public boolean isVendido() { 
    	return vendido; 
    	}
    public void setVendido(boolean vendido) { 
    	this.vendido = vendido;
    	}
    public int getLikes() { 
    	return likes; 
    	}
    public LocalDate getFechaPublicacion() { 
    	return fechaPublicacion; 
    	}
}