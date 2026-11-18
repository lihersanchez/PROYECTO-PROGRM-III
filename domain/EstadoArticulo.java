package domain;

public enum EstadoArticulo {
    NUEVO_CON_ETIQUETA("Nuevo con etiqueta"),
    NUEVO_SIN_ETIQUETA("Nuevo sin etiqueta"),
    MUY_BUENO("Muy bueno"),
    BUENO("Bueno"),
    SATISFACTORIO("Satisfactorio");

    private final String descripcion;

    EstadoArticulo(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public String toString() {
        return descripcion;
    }
}
