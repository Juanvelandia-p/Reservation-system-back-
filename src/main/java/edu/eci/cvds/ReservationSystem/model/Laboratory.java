package edu.eci.cvds.ReservationSystem.model;

import java.time.LocalDate;
import java.util.HashMap;

public class Laboratory {

    private String name;
    private HashMap<,Reservation > schedule; //LocalDate for the date and boolean for disponibility per day
    private
    
    public Laboratory(String name){
        this.name = name;
        this.schedule = new HashMap<>(); 
    }

    public void updateSchedule(LocalDate date, int time, boolean status){
        if(schedule.containsKey(date)){
            boolean[] daySchedule = schedule.get(date);
            if(time >= 1 && time <= TIME_DAY){
                daySchedule[time - 1] = status; 
            }
        }
    }

    public void createDaySchedule(LocalDate date){
        boolean[] newShedule = new boolean[TIME_DAY];
        schedule.put(date, newShedule);
    }
}
