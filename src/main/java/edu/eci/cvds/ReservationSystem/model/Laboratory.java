package edu.eci.cvds.ReservationSystem.model;


public class Laboratory {

    private String name;
    private String block;
    
    // Constructor vacío necesario para MongoDB
    public Laboratory() {}

    public Laboratory(String name, String block){
        this.name = name;
        this.block = block; 
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
