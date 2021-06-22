package AsyncTasks;

import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import Model.EventMod;
import Model.PersonMod;
import Net.Datacache;
import Net.EventsArray;
import Net.PersonsArray;
import Net.ServerProxy;

public class DataTask extends AsyncTask<String,Boolean,Boolean> {

    private String serverIp;
    private String serverHost;
    private Datacache datacache = Datacache.initialize();
    private DataContext context;


    public interface DataContext{
        void ExecuteComplete(String toastMessage);
    }

    public DataTask(String serverIp, String serverHost, DataContext dataContext){
        this.serverIp = serverIp;
        this.serverHost = serverHost;
        context = dataContext;
    }

    @Override
    protected void onPostExecute(Boolean b) {
        if(b){
            PersonMod user = datacache.getUser();
            String message = "Welcome, " + user.getFirstName() + " " + user.getFirstName();
            context.ExecuteComplete(message);
            datacache.sortAllVariables();
        }

        else{
          context.ExecuteComplete("Error occurred with the User Data");
        }
    }

    @Override
    protected Boolean doInBackground(String... strings) {
        ServerProxy serverProxy = ServerProxy.toInitialize();
        serverProxy.setAuthToken(strings[0]);
        EventsArray eventsArray = serverProxy.getAllEvents();
        PersonsArray personsArray = serverProxy.getAllPeople();
        Boolean success = (storeAllEvents(eventsArray) && storeAllPeople(personsArray));
        return success;
    }

    //Organizing and storing what we got from the server to the Datachache class

    public boolean storeAllEvents(EventsArray eventsArray){
        if(eventsArray.getMessage() == null){
            Map<String, EventMod> eventMap = new HashMap<String, EventMod>();
            ArrayList<EventMod> eventsArrayAll = eventsArray.getData();
            for(int i = 0; i < eventsArrayAll.size(); i++){
                eventMap.put(eventsArrayAll.get(i).getEventId(),eventsArrayAll.get(i));
            }
            datacache.setEventsMap(eventMap);
            return true;
        }
        return false;

    }

    public boolean storeAllPeople(PersonsArray personsArray){
        if(personsArray.getMessage() == null){
            ArrayList<PersonMod> personModArrayList = personsArray.getData();
            Map<String,PersonMod> personsMap = new HashMap<String, PersonMod>();
            datacache.setUser(personModArrayList.get(0));

            for(int i =0; i < personModArrayList.size(); i++){
                personsMap.put(personModArrayList.get(i).getPersonId(),personModArrayList.get(i));
            }
            datacache.setPersonMap(personsMap);
            return true;
        }
        return false;
    }


}
