package domain;

public class EstadisticaUsuario {
    private String usuario;
    private int publicados;
    private int disponibles;
    private int compras;
    private int ventas;
    private double gastado;
    private double ingresos;
    private double importeArticulos;
    private double envios;
    private double tasas;

    public EstadisticaUsuario(String usuario, int publicados, int disponibles, int compras, int ventas,
                              double gastado, double ingresos, double importeArticulos, double envios, double tasas) {
        this.usuario = usuario;
        this.publicados = publicados;
        this.disponibles = disponibles;
        this.compras = compras;
        this.ventas = ventas;
        this.gastado = gastado;
        this.ingresos = ingresos;
        this.importeArticulos = importeArticulos;
        this.envios = envios;
        this.tasas = tasas;
    }

    public String getUsuario() { 
    	return usuario; 
    	}
    public int getPublicados() { 
    	return publicados; 
    	}
    public int getDisponibles() { 
    	return disponibles; 
    	}
    public int getCompras() { 
    	return compras; 
    	}
    public int getVentas() { 
    	return ventas; 
    	}
    public double getGastado() { 
    	return gastado; 
    	}
    public double getIngresos() { 
    	return ingresos; 
    	}
    public double getImporteArticulos() { 
    	return importeArticulos; 
    	}
    public double getEnvios() { 
    	return envios; 
    	}
    public double getTasas() { 
    	return tasas; 
    	}
}