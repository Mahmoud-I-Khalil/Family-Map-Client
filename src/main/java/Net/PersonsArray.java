package Net;

import java.util.ArrayList;
import java.util.List;

import Model.PersonMod;

public class PersonsArray {

    //Class that helps in storing the persons data from the server


    ArrayList<PersonMod> data;
    String message;

    public PersonsArray(ArrayList<PersonMod> data) {
        this.data = data;
    }

    public PersonsArray(String error){
        message = error;
    }

    public ArrayList<PersonMod> getData() {
        return data;
    }

    public void setData(ArrayList<PersonMod> data) {
        this.data = data;
    }

    public String getMessage(){
        return message;
    }
}
