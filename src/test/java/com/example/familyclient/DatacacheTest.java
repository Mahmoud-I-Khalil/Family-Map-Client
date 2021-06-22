package com.example.familyclient;

import org.junit.Test;

import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import Model.EventMod;
import Model.PersonMod;
import Net.Datacache;
import Net.EventsArray;
import Net.PersonsArray;
import Net.ServerProxy;
import Request.LoginRequest;
import Request.RegisterRequest;
import Result.LoginResult;
import Result.RegisterResult;
import Utility.Filter;


public class DatacacheTest {

    @Before
    public void setUp() {
        //Initializing events
        EventMod event1 = new EventMod("1", "roo2 ya faroo2", "1", 52, 92,"m","Utah", "el3ab b ayre", 1960);
        EventMod event2 = new EventMod("2", "roo2 ya faroo2", "1", 17, 21,"stuff","Chiliii", "more el3ab b ayre", 2010);
        EventMod event3 = new EventMod("3", "roo2 ya faroo2", "1", 873, 67,"Emak","Zimbabiyywe kis emoo", "birth", 1990);
        EventMod event4 = new EventMod("4", "roo2 ya faroo2", "1", 32, 41,"SaltLake","ayre fik", "el3ab b ayre", 2000);

        PersonMod person1 = new PersonMod("1", "roo2 ya faroo2", "false", "Kisemakbayre", "f","4","3", "2");
        PersonMod person2 = new PersonMod("2", "roo2 ya faroo2","Jack","Frost","f",null,null,"1");
        PersonMod person3 = new PersonMod("3", "roo2 ya faroo2","jenny","5ara","m","2",null,"4");
        PersonMod person4 = new PersonMod("4", "roo2 ya faroo2","jenny","5ara","m",null,null,"3");


        PersonMod MOM = new PersonMod("mom", "roo2 ya faroo2", "khadija", "fatma", "f",null,null,null);
        PersonMod user = new PersonMod("roo2 ya faroo2", "kamn", "false", "Kisemakbayre", "doe","1","mom", null);

        EventMod[] eventsArray = new EventMod[]{event1, event2, event3, event4};
        PersonMod[] personArray = new PersonMod[]{user, person1, person2, person3, person4, MOM};

        Datacache datacache = Datacache.initialize();
        Map<String, PersonMod> personsMap = new HashMap<String, PersonMod>();
        datacache.setUser(user);

        for(int i = 0; i < personArray.length; i++){
            String personID = personArray[i].getPersonId();
            personsMap.put(personID, personArray[i]);
        }

        Map<String, EventMod> eventsMap = new HashMap<String, EventMod>();

        for(int i = 0; i < eventsArray.length; i++){
            String eventID = eventsArray[i].getEventId();
            eventsMap.put(eventID, eventsArray[i]);
        }

        datacache.setPersonMap(personsMap);
        datacache.setEventsMap(eventsMap);
        datacache.sortAllVariables();
    }

    // Check if we got all people
    @Test
    public void getPeople() {
        Datacache model = Datacache.initialize();
        Map<String, PersonMod> mapTest = model.getPersonMap();

        Assert.assertNotNull(mapTest);
        Assert.assertEquals(6, mapTest.size());


        PersonMod person1 = new PersonMod("1", "roo2 ya faroo2", "false", "Kisemakbayre", "f", "4", "3", "2");
        Assert.assertEquals(person1.getFatherId(), mapTest.get("1").getFatherId());
        PersonMod person2 = new PersonMod("2", "roo2 ya faroo2", "Jack", "Frost", "f", "2", "4", "1");
        PersonMod person3 = new PersonMod("3", "roo2 ya faroo2", "jenny", "5ara", "m", "2", "2", "4");


        PersonMod personTwo = new PersonMod("2", "roo2 ya faroo2","Jack","Frost","f",null,null,null);
        Assert.assertEquals(personTwo.getGender(), mapTest.get("2").getGender());

        PersonMod personThree = new PersonMod("3", "roo2 ya faroo2","jenny","5ara","m",null,"yup",null);
        Assert.assertEquals(personThree.getLastName(), mapTest.get("3").getLastName());

        PersonMod personFour = new PersonMod("4", "roo2 ya faroo2","jenny","5ara","m",null,"yup",null);
        Assert.assertEquals(personFour.getFirstName(), mapTest.get("4").getFirstName());

        Assert.assertNotEquals(personFour.getFirstName(), mapTest.get("2").getFirstName());
    }

    // Retreival events
    @Test
    public void getEvents(){
        
        Datacache datacache = Datacache.initialize();
        Map<String, EventMod> mapTest = datacache.getAllDisplayedEvents();

        Assert.assertNotNull(mapTest);
        Assert.assertEquals(4, mapTest.size());

        EventMod event1 = new EventMod("1", "roo2 ya faroo2", "1", 52, 92,"m","Utah", "el3ab b ayre", 2010);
        Assert.assertEquals(event1.getCity(), mapTest.get("1").getCity());

        EventMod event2 = new EventMod("2", "roo2 ya faroo2", "1", 17, 21,"stuff","Chiliii", "more el3ab b ayre", 2002);
        Assert.assertEquals(event2.getCountry(), mapTest.get("2").getCountry());

        EventMod event3 = new EventMod("3", "roo2 ya faroo2", "1", 873, 67,"Emak","Zimbabiyywe kis emoo", "birth", 1990);
        Assert.assertEquals(event3.getEventId(), mapTest.get("3").getEventId());

        EventMod event4 = new EventMod("4", "roo2 ya faroo2", "1", 32, 41,"SaltLake","ayre fik", "el3ab b ayre", 2000);
        Assert.assertEquals(event4.getLatitude(), mapTest.get("4").getLatitude(), .05);

        Assert.assertNotEquals(event4.getEventId(), mapTest.get("2").getEventId());
    }
    
    //Checing MaternalAncestors
    @Test
    public void MaternalAncestors()        
    {
        Datacache da = Datacache.initialize();
        Set<String> motherSide = da.getMotherSide();

        Assert.assertNotNull(motherSide);
        Assert.assertEquals(1, motherSide.size());
        Assert.assertTrue(motherSide.contains("mom"));
        Assert.assertFalse(motherSide.contains("4"));
    }

    // Paternal Checking Test
    @Test
    public void getPaternalAncestors()
    {
        Datacache model = Datacache.initialize();
        Set<String> fatherRelatives = model.getFatherSide();

        Assert.assertNotNull(fatherRelatives);
        Assert.assertEquals(4, fatherRelatives.size());
        Assert.assertTrue(fatherRelatives.contains("1"));
        Assert.assertTrue(fatherRelatives.contains("2"));
        Assert.assertTrue(fatherRelatives.contains("3"));
        Assert.assertTrue(fatherRelatives.contains("4"));
        Assert.assertFalse(fatherRelatives.contains("mom"));
    }

    //Filtering of people
    @Test
    public void filterPeople()            
    {
        Datacache datacache = Datacache.initialize();
        Filter filter = datacache.getFilter();

        Map<String, EventMod> mapTest = datacache.getAllDisplayedEvents();
        Assert.assertNotNull(mapTest);
        Assert.assertEquals(4, mapTest.size());

        EventMod event1 = new EventMod("1", "roo2 ya faroo2", "1", 52, 92,"m","Utah", "el3ab b ayre", 2010);
        Assert.assertEquals(event1.getCity(), mapTest.get("1").getCity());

        EventMod event2 = new EventMod("2", "roo2 ya faroo2", "1", 17, 21,"stuff","Chiliii", "more el3ab b ayre", 2002);
        Assert.assertEquals(event2.getCountry(), mapTest.get("2").getCountry());

        EventMod event3 = new EventMod("3", "roo2 ya faroo2", "1", 873, 67,"Emak","Zimbabiyywe kis emoo", "birth", 1990);
        Assert.assertEquals(event3.getEventId(), mapTest.get("3").getEventId());

        EventMod event4 = new EventMod("4", "roo2 ya faroo2", "1", 32, 41,"SaltLake","ayre fik", "el3ab b ayre", 2000);

        Assert.assertEquals(event4.getLatitude(), mapTest.get("4").getLatitude(), .05);
        Assert.assertNotEquals(event4.getEventId(), mapTest.get("2").getEventId());

        filter.setFemales(false);

        mapTest = datacache.getAllDisplayedEvents();
        Assert.assertNotNull(mapTest);
        Assert.assertEquals(0, mapTest.size());
    }

    //filter People
    @Test
    public void filterEvents()              //filter by event type
    {
        Datacache datacache = Datacache.initialize();
        Filter filter = datacache.getFilter();

        Map<String, EventMod> map = datacache.getAllDisplayedEvents();
        Assert.assertNotNull(map);
        Assert.assertEquals(4, map.size());

        EventMod event1 = new EventMod("1", "roo2 ya faroo2", "1", 52, 92,"m","Utah", "el3ab b ayre", 2010);
        Assert.assertEquals(event1.getCity(), map.get("1").getCity());

        EventMod event2 = new EventMod("2", "roo2 ya faroo2", "1", 17, 21,"stuff","Chiliii", "more el3ab b ayre", 2002);
        Assert.assertEquals(event2.getCountry(), map.get("2").getCountry());

        EventMod event3 = new EventMod("3", "roo2 ya faroo2", "1", 873, 67,"Emak","Zimbabiyywe kis emoo", "birth", 1990);
        Assert.assertEquals(event3.getEventId(), map.get("3").getEventId());

        EventMod event4 = new EventMod("4", "roo2 ya faroo2", "1", 32, 41,"SaltLake","ayre fik", "el3ab b ayre", 2000);
        Assert.assertEquals(event4.getLatitude(), map.get("4").getLatitude(), .05);

        Assert.assertNotEquals(event4.getEventId(), map.get("2").getEventId());

        filter.getDisplayedEvents().remove("el3ab b ayre");

        map = datacache.getAllDisplayedEvents();
        Assert.assertNotNull(map);
        Assert.assertEquals(2, map.size());

        Assert.assertEquals(event2.getCity(), map.get("2").getCity());
        Assert.assertEquals(event3.getCountry(), map.get("3").getCountry());
        Assert.assertFalse(map.containsKey("1"));
        Assert.assertFalse(map.containsKey("4"));

        // Adding the filter back so it doesnme mess up the other test
        filter.getDisplayedEvents().add("el3ab b ayre");


    }


    @Test
    public void SuccessfulSortEventsByYear()              //sort by year
    {
        Datacache datacache = Datacache.initialize();
        List<EventMod> events = datacache.getAllPersonEvents().get("1");
        Assert.assertNotNull(events);

        EventMod event1= new EventMod("1", "roo2 ya faroo2", "1", 52, 92,"m","Utah", "el3ab b ayre", 1960);
        EventMod event2 = new EventMod("2", "roo2 ya faroo2", "1", 17, 21,"stuff","Chiliii", "more el3ab b ayre", 2010);
        EventMod event3 = new EventMod("3", "roo2 ya faroo2", "1", 873, 67,"Emak","Zimbabiyywe kis emoo", "birth", 1990);
        EventMod event4 = new EventMod("4", "roo2 ya faroo2", "1", 32, 41,"SaltLake","ayre fik", "el3ab b ayre", 2000);

        Assert.assertEquals(event1, events.get(0));
        Assert.assertEquals(event4, events.get(3));

        events = datacache.sortEventsByYear(events);

        Assert.assertEquals(event1, events.get(0));
        Assert.assertEquals(event3, events.get(1));
        Assert.assertEquals(event4, events.get(2));
        Assert.assertEquals(event2, events.get(3));
    }

    
    

    

}
