package Net;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import Model.EventMod;
import Model.PersonMod;
import Utility.Filter;
import Utility.MapColors;
import Utility.Settings;

public class Datacache {

    private static Datacache datacache;

    // Main 3 that we take from the server
    private PersonMod user;
    private Map<String, EventMod> eventsMap;
    private Map<String, PersonMod> personMap;

    //All Events and specific ones that will be displayed if filtered
    private Map<String, EventMod> displayedEvent;
    private Map<String, List<EventMod>> allPersonEvents;

    //Filter and setting
    private Filter filter;
    private Utility.Settings settings;

    private List<String> eventTypes;
    // Setting color to each event
    private Map<String, MapColors> eventColor;

    //Father and Mother Side and the children of a person
    private Set<String> fatherSide;
    private Set<String> MotherSide;
    private Map<String, PersonMod> children;

    private PersonMod selectedPerson;
    private EventMod selectedEvent;
    private EventMod activityEvent;

    public static Datacache initialize() {
        if (datacache == null) {
            datacache = new Datacache();
        }
        return datacache;
    }

    public void clear(){
        datacache = null;
    }

    public EventMod getActivityEvent() {
        return activityEvent;
    }

    public void setActivityEvent(EventMod activityEvent) {
        this.activityEvent = activityEvent;
    }

    public void setPersonMap(Map<String, PersonMod> map) {
        personMap = map;
    }

    public void setEventsMap(Map<String, EventMod> map) {
        eventsMap = map;
    }

    public Map<String, EventMod> getEventsMap() {
        return eventsMap;
    }

    public Map<String, PersonMod> getPersonMap() {
        return personMap;
    }

    public PersonMod getUser() {
        return user;
    }

    public void setUser(PersonMod user) {
        this.user = user;
    }


    public Map<String, EventMod> getDisplayedEvent() {
        return displayedEvent;
    }

    public void setDisplayedEvent(Map<String, EventMod> displayedEvent) {
        this.displayedEvent = displayedEvent;
    }

    public Map<String, List<EventMod>> getAllPersonEvents() {
        return allPersonEvents;
    }

    public void setAllPersonEvents(Map<String, List<EventMod>> allPersonEvents) {
        this.allPersonEvents = allPersonEvents;
    }

    public Filter getFilter() {
        return filter;
    }

    public void setFilter(Filter filter) {
        this.filter = filter;
    }

    public Utility.Settings getSettings() {
        return settings;
    }

    public void setSettings(Settings settings) {
        this.settings = settings;
    }

    public List<String> getEventTypes() {
        return eventTypes;
    }

    public void setEventTypes(List<String> eventTypes) {
        this.eventTypes = eventTypes;
    }

    public Map<String, MapColors> getEventColor() {
        return eventColor;
    }

    public void setEventColor(Map<String, MapColors> eventColor) {
        this.eventColor = eventColor;
    }

    public Set<String> getFatherSide() {
        return fatherSide;
    }

    public void setFatherSide(Set<String> fatherSide) {
        this.fatherSide = fatherSide;
    }

    public Set<String> getMotherSide() {
        return MotherSide;
    }

    public void setMotherSide(Set<String> motherSide) {
        MotherSide = motherSide;
    }

    public Map<String, PersonMod> getChildren() {
        return children;
    }

    public void setChildren(Map<String, PersonMod> children) {
        this.children = children;
    }

    public PersonMod getSelectedPerson() {
        return selectedPerson;
    }

    public void setSelectedPerson(PersonMod selectedPerson) {
        this.selectedPerson = selectedPerson;
    }

    public EventMod getSelectedEvent() {
        return selectedEvent;
    }

    public void setSelectedEvent(EventMod selectedEvent) {
        this.selectedEvent = selectedEvent;
    }

    // Sorts all events and persons...
    public void sortAllVariables() {


        initializeAllEventTypes();
        initializeFatherSide();
        initializeMotherSide();
        initializeAllPersonEvent();
        initializeChildren();

        if(filter == null){
            filter = new Filter();
        }

        if(settings == null){
            settings = new Settings();
        }

    }

    public List<EventMod> sortEventsByYear(List<EventMod> eventsArray) {
        List<EventMod> sortedEvents = new ArrayList<>();
        List<EventMod> eventArrayList = new ArrayList<>(eventsArray);

        while (eventArrayList.size() > 0) {
            EventMod currentEvent = eventArrayList.get(0);
            int index = 0;
            for (int i = 0; i < eventArrayList.size(); i++) {
                if (currentEvent.getYear() > eventArrayList.get(i).getYear()) {
                    currentEvent = eventArrayList.get(i);
                    index = i;
                }
            }

            sortedEvents.add(currentEvent);
            eventArrayList.remove(index);
        }
        return sortedEvents;
    }

    public List<PersonMod> RelativeFinder(String personID) {
        PersonMod currentPerson = getPersonMap().get(personID);
        List<PersonMod> personModList = new ArrayList<>();

        if (getPersonMap().get(currentPerson.getFatherId()) != null) {
            personModList.add(getPersonMap().get(currentPerson.getFatherId()));
        }

        if (getPersonMap().get(currentPerson.getMotherId()) != null) {
            personModList.add(getPersonMap().get(currentPerson.getMotherId()));
        }

        if (getPersonMap().get(currentPerson.getSpouseId()) != null) {
            personModList.add(getPersonMap().get(currentPerson.getSpouseId()));
        }

        if (getChildren().get(currentPerson.getPersonId()) != null) {
            personModList.add(getChildren().get(currentPerson.getPersonId()));
        }

        return personModList;
    }

    private void initializeChildren() {
        children = new HashMap<>();
        for (PersonMod person : personMap.values()) {
            if (person.getFatherId() != null) {
                children.put(person.getFatherId(), person);

            }

            if (person.getMotherId() != null) {
                children.put(person.getMotherId(), person);
            }
        }
    }

    private void initializeFatherSide() {
        fatherSide = new HashSet<>();
        ancestorCreator(user.getFatherId(), fatherSide);

    }

    private void initializeMotherSide() {
        MotherSide = new HashSet<>();
        ancestorCreator(user.getMotherId(), MotherSide);
    }

    private void ancestorCreator(String personID, Set<String> ancestors) {
        if (personID == null) {
            return;
        }

        ancestors.add(personID);
        PersonMod personMod = personMap.get(personID);

        if (personMod.getMotherId() != null) {
            ancestorCreator(personMod.getMotherId(), ancestors);
        }

        if (personMod.getFatherId() != null) {
            ancestorCreator(personMod.getFatherId(), ancestors);
        }
    }

    private void initializeAllPersonEvent() {

        allPersonEvents = new HashMap<>();
        for (PersonMod personMod : personMap.values()) {
            ArrayList<EventMod> eventList = new ArrayList<EventMod>();
            for (EventMod eventMod : eventsMap.values()) {
                if (personMod.getPersonId().equals(eventMod.getPersonId())) {
                    eventList.add(eventMod);
                }
            }
            allPersonEvents.put(personMod.getPersonId(), eventList);
        }
    }

    private void initializeAllEventTypes() {
        ArrayList<EventMod> eventModArrayList = new ArrayList<EventMod>();

        for (EventMod eventMod : eventsMap.values()) {
            eventModArrayList.add(eventMod);
        }
        eventColor = new HashMap<>();
        eventTypes = new ArrayList<>();

        for (int i = 0; i < eventModArrayList.size(); i++) {
            if (!eventColor.containsKey(eventModArrayList.get(i).getEventType())) {
                // Change colors depending on the type of event
                eventColor.put(eventModArrayList.get(i).getEventType().toLowerCase(), new MapColors(eventModArrayList.get(i).getEventType().toLowerCase()));
                eventTypes.add(eventModArrayList.get(i).getEventType());
            }
        }

        datacache.setEventTypes(eventTypes);
    }

    public boolean isPersonOnMap(PersonMod person) {
        if (!filter.isFemales() && person.getGender().equalsIgnoreCase("f")) {
            return false;
        }
        else if (!filter.isMales() && person.getGender().equalsIgnoreCase("m")) {
            return false;
        }
        else if (!filter.isFathersSide() && fatherSide.contains(person.getPersonId())) {
            return false;
        }
        else return filter.isMothersSide() || !MotherSide.contains(person.getPersonId());

    }

    public Map<String, EventMod> getAllDisplayedEvents(){

        displayedEvent = new HashMap<>();

        for (EventMod eventMod : eventsMap.values()) {
            PersonMod personEvent = getPersonMap().get(eventMod.getPersonId());
            if (!isPersonOnMap(personEvent)) {

            } else if (!filter.containsEventType(eventMod.getEventType())) {

            } else {
                displayedEvent.put(eventMod.getEventId(), eventMod);
            }
        }

        return displayedEvent;
    }





}
