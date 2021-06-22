package com.example.familyclient.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.example.familyclient.ActivityHelpers.PersonActivityAdapter;
import com.example.familyclient.R;

import java.util.ArrayList;
import java.util.List;

import Model.EventMod;
import Model.PersonMod;
import Net.Datacache;

public class PersonActivity extends AppCompatActivity {

    private TextView firstName;
    private TextView lastName;
    private TextView gender;

    private PersonMod currentPerson;
    private Datacache datacache = Datacache.initialize();

    private ExpandableListView listView;
    private ExpandableListAdapter listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("FamilyMap: Person Details");

        currentPerson = datacache.getSelectedPerson();

        firstName = findViewById(R.id.personFirstName);
        firstName.setText(currentPerson.getFirstName());
        lastName = findViewById(R.id.personLastName);
        lastName.setText(currentPerson.getLastName());
        gender = findViewById(R.id.personGender);
        if(currentPerson.getGender().equalsIgnoreCase("f")){
            gender.setText("Female");
        }
        else {
            gender.setText("Male");
        }

        listView = findViewById(R.id.listPersonExpandableView);

        listView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

                if(groupPosition == 0){
                    Intent intent = new Intent(PersonActivity.this,EventActivity.class);
                    intent.putExtra("Event", "Event");
                    datacache.setSelectedEvent((EventMod) listAdapter.getChild(groupPosition,childPosition));
                    startActivity(intent);
                }

                else{
                    Intent intent = new Intent(PersonActivity.this,PersonActivity.class);
                    datacache.setSelectedPerson((PersonMod) listAdapter.getChild(groupPosition,childPosition));
                    startActivity(intent);

                }

                return false;
            }


        });

        doTheList();
    }

    public boolean onCreateOptionsMenu(Menu menu)
    {
        return true;
    }

    public void doTheList(){

        List<String> parentHeader = new ArrayList<>();
        parentHeader.add("LIFE EVENTS");
        parentHeader.add("FAMILY");

        List<PersonMod> family = new ArrayList<>(datacache.RelativeFinder(currentPerson.getPersonId()));
        List<EventMod> eventsArray = new ArrayList<>(datacache.sortEventsByYear(
                datacache.getAllPersonEvents().get(currentPerson.getPersonId())));

        eventsArray = filterEvents(eventsArray);
        family = filterPersons(family);

        listAdapter = new PersonActivityAdapter(this, parentHeader, family, eventsArray, currentPerson);
        listView.setAdapter(listAdapter);
    }

    public List<EventMod> filterEvents( List<EventMod> eventsArray ){
        List<EventMod> availableEvents = new ArrayList<>();
        for(EventMod event : eventsArray){
            if(datacache.getAllDisplayedEvents().containsValue(event)){
                availableEvents.add(event);
            }
        }

        return availableEvents;
    }

    public List<PersonMod> filterPersons(List<PersonMod> personArray){
        List<PersonMod> availablePersons = new ArrayList<>();
        for(PersonMod person : personArray){
                availablePersons.add(person);
        }

        return availablePersons;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


}