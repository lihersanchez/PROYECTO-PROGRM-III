package domain;

public enum Categoria {
    MUJER("Mujer"),
    HOMBRE("Hombre"),
    NINOS("Niños"),
    HOGAR("Hogar"),
    ELECTRONICA("Electrónica");

    private final String nombreVisible;

    Categoria(String nombreVisible) {
        this.nombreVisible = nombreVisible;
    }

    @Override
    public String toString() {
        return nombreVisible;
    }
}