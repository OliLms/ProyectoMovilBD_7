package POJO;

public class contactos{
    private String nombre;
    private String telefono;
    private String direccion;
    private String Correo;
    private int edad;
    private String genero;
    private String tipo;
    private String descripcion;


    // Constructor
    public contactos(String nombre, String telefono, String direccion, String Correo, int edad, String genero, String tipo, String descripcion) {
        this.nombre = nombre;
        this.telefono = telefono;
        this.direccion = direccion;
        this.Correo = Correo;
        this.edad = edad;
        this.genero = genero;
        this.tipo= tipo;
        this.descripcion = descripcion;


    }

    // Getters y setters
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getCorreo() {
        return Correo;
    }

    public void setCorreo(String email) {
        this.Correo = Correo;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    public String getTipo() {
        return tipo;
    }
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}