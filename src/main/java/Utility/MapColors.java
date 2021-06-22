package Utility;

import android.graphics.Color;

public class MapColors extends Color {

    //Storing the event marker color in this class


    private float color;

    //Generating marker color according to event type
    private float ColorMaker(String type){
        if(type.equalsIgnoreCase("birth")){
            return 180;
        }

        if(type.equalsIgnoreCase("death")){
            return 255;
        }

        if(type.equalsIgnoreCase("marriage")){
            return 68;
        }

        return Math.abs((type.length()*53)%360);
    }

    public MapColors(String event){
        color = Math.abs(ColorMaker(event));
    }

    public float getColor() {
        return color;
    }

    public void setColor(float color) {
        this.color = color;
    }
}
