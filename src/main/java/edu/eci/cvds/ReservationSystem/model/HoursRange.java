package edu.eci.cvds.ReservationSystem.model;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "hourranges")
public class HoursRange {
    @Id
    private String id;
    private String aviableHours;

    // Constructor vacío
    public HoursRange() {}

    // Getters y Setters
    public String getHoraApertura() { return aviableHours; }
    public void setHoraApertura(String horaApertura) { this.aviableHours = horaApertura; }

}
