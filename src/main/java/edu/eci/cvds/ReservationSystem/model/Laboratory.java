package edu.eci.cvds.ReservationSystem.model;

import javax.persistence.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "laboratories")
public class Laboratory {
    @Id
    private String id;
    private String name;
    private String block;
    
    // Constructor vacío necesario para MongoDB
    public Laboratory() {}

    public Laboratory(String name, String block){
        this.name = name;
        this.block = block; 
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBlock() {
        return block;
    }

    public void setBlock(String block) {
        this.block = block;
    }

}
