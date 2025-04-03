package edu.eci.cvds.ReservationSystem.model;

/**
 * Representa un usuario en el sistema.
 */
public class User {
    private String name;
    private String email;
    private String password;

     /**
     * Constructor que inicializa el usuario.
     *
     * @param name Nombre del usuario.
     * @param email Correo electrónico del usuario.
     * @param password Contraseña del usuario.
     */
    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
