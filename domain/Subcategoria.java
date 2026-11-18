package domain;

public enum Subcategoria {
    ROPA("Ropa", Categoria.HOMBRE),
    CALZADO("Calzado", Categoria.HOMBRE),
    ACCESORIOS("Accesorios", Categoria.HOMBRE),
    VAQUEROS("Vaqueros", Categoria.HOMBRE),
    CAMISETAS("Camisetas y camisas", Categoria.HOMBRE),
    ABRIGOS("Abrigos y chaquetas", Categoria.HOMBRE),
    VESTIDOS("Vestidos", Categoria.MUJER),
    BOLSOS("Bolsos", Categoria.MUJER),
    MOVILES("Móviles y tablets", Categoria.ELECTRONICA),
    CONSOLAS("Consolas y videojuegos", Categoria.ELECTRONICA),
    AUDIO("Auriculares y audio", Categoria.ELECTRONICA),
    DECORACION("Decoración", Categoria.HOGAR),
    JUGUETES("Juguetes", Categoria.NINOS);

    private final String nombreVisible;
    private final Categoria categoriaPadre;

    Subcategoria(String nombreVisible, Categoria categoriaPadre) {
        this.nombreVisible = nombreVisible;
        this.categoriaPadre = categoriaPadre;
    }

    public Categoria getCategoriaPadre() {
        return categoriaPadre;
    }

    @Override
    public String toString() {
        return nombreVisible;
    }
}