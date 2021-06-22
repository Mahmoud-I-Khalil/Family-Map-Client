package Utility;

import java.util.ArrayList;
import java.util.List;

import Net.Datacache;

public class Filter {

    //Filter class for settings

    private boolean fathersSide;
    private boolean mothersSide;
    private boolean males;
    private boolean females;
    private List<String> allEvents;
    private List<String> displayedEvents;


    public Filter() {
        fathersSide = true;
        mothersSide = true;
        males = true;
        females = true;
        allEvents = new ArrayList<>(Datacache.initialize().getEventTypes());
        displayedEvents = new ArrayList<>(Datacache.initialize().getEventTypes());

    }

    public boolean containsEventType(String eventType) {
        eventType = eventType.toLowerCase();

        for (String event: displayedEvents) {
            if (event.toLowerCase().equals(eventType)){
                return true;
            }
        }

        return false;
    }

    public void deleteEventType(String eventType) {

        eventType = eventType.toLowerCase();
        for (int i = 0; i < displayedEvents.size(); i++) {
            if (displayedEvents.get(i).toLowerCase().equals(eventType)){
                displayedEvents.remove(i);
            }
        }
    }

    public void addEvent(String eventType) {

        int index = 0;
        eventType = eventType.toLowerCase();
        for (int i = 0; i < allEvents.size(); i++) {
            if (allEvents.get(i).toLowerCase().equals(eventType)){
                index = i;
            }
        }
        displayedEvents.add(index, eventType);
    }

    public boolean isFathersSide() {
        return fathersSide;
    }

    public void setFathersSide(boolean fathersSide) {
        this.fathersSide = fathersSide;
    }

    public boolean isMothersSide() {
        return mothersSide;
    }

    public void setMothersSide(boolean mothersSide) {
        this.mothersSide = mothersSide;
    }

    public boolean isMales() {
        return males;
    }

    public void setMales(boolean males) {
        this.males = males;
    }

    public boolean isFemales() {
        return females;
    }

    public void setFemales(boolean females) {
        this.females = females;
    }

    public List<String> getAllEvents() {
        return allEvents;
    }

    public void setAllEvents(List<String> allEvents) {
        this.allEvents = allEvents;
    }

    public List<String> getDisplayedEvents() {
        return displayedEvents;
    }

    public void setDisplayedEvents(List<String> displayedEvents) {
        this.displayedEvents = displayedEvents;
    }
}