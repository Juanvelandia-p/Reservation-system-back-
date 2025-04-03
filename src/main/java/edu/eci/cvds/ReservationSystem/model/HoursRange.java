package edu.eci.cvds.ReservationSystem.model;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Representa un rango de horas disponibles para realizar reservas.
 * Se almacena en la colección "hourranges" de MongoDB.
 */
@Document(collection = "hourranges")
public class HoursRange {
    @Id
    private String id;
    private String aviableHours;

    /**
     * Constructor vacío.
     */
    public HoursRange() {}

     /**
     * Obtiene las horas disponibles.
     *
     * @return Cadena con las horas de apertura disponibles.
     */
    public String getHoraApertura() { return aviableHours; }

    /**
     * Establece las horas disponibles.
     *
     * @param horaApertura Cadena que indica las horas de apertura.
     */
    public void setHoraApertura(String horaApertura) { this.aviableHours = horaApertura; }

}
