package Net;

import java.util.ArrayList;
import java.util.List;

import Model.EventMod;

public class EventsArray {

    //Class that helps in storing the events data from the server

    ArrayList<EventMod> data;
    String message;
    boolean success;

    public EventsArray(ArrayList<EventMod> events, boolean success) {
        data = events;
        this.success = success;
        message = null;
    }

    public EventsArray(String error) {
        message = error;
    }

    public String getMessage(){
        return message;
    }

    public ArrayList<EventMod> getData() {
        return data;
    }

    public void setData(ArrayList<EventMod> data) {
        this.data = data;
    }
}
