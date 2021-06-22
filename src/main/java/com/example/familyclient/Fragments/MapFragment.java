package com.example.familyclient.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.familyclient.Activities.PersonActivity;
import com.example.familyclient.R;
import com.example.familyclient.Activities.SearchActivity;
import com.example.familyclient.Activities.SettingActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Model.EventMod;
import Model.PersonMod;
import Utility.Filter;
import Utility.MapColors;
import Utility.Settings;
import Net.Datacache;

public class MapFragment extends Fragment implements  OnMapReadyCallback {
   private GoogleMap map;
   private Map<Marker, EventMod> markerMap;
   private Map<String, EventMod> displayedEvents;
   private Marker selectedMarker;

   private List<Polyline> polylineList;

   private boolean isEvent;

   private TextView name;
   private TextView eventType;
   private TextView yearOfEvent;
   private ImageView icon;

   private Datacache datacache = Datacache.initialize();

   public MapFragment(){};

   public MapFragment(String eventId){
       isEvent = (eventId!=null);
   }
   
   View.OnClickListener onClickText = new View.OnClickListener() {
       @Override
       public void onClick(View v) {
           clickedText();
       }
   };

    private void clickedText() {

        Intent intent = new Intent(getActivity(), PersonActivity.class);
        PersonMod personMod = datacache.getPersonMap().get(markerMap.get(selectedMarker).getPersonId());
        datacache.setSelectedPerson(personMod);
        startActivity(intent);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_map,container, false);

        // Inflate the layout for this fragment


        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        name = view.findViewById(R.id.personName);
        eventType = view.findViewById(R.id.eventDetails);
        yearOfEvent = view.findViewById(R.id.year);
        icon = view.findViewById(R.id.mapIcon);

        polylineList = new ArrayList<>();

        return view;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(!isEvent);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.settings:
                settingsClicked();
                return true;

            case R.id.search:
                searchClicked();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public void onResume() {
        super.onResume();

        if(markerMap != null && map != null){
            clearMarkers();
            EventMod currentMarkedEvent = markerMap.get(selectedMarker);
            onMapReady(map);

            if(selectedMarker == null){
                if(!markerMap.containsValue(currentMarkedEvent)){
                    removeAllLines();
                }
            }

        }

        if(selectedMarker != null && markerMap != null){
            lineDrawer();
        }
    }

    public void settingsClicked(){
        Intent intent = new Intent(getActivity(), SettingActivity.class);
        startActivity(intent);
    }

    public void searchClicked(){
        Intent intent = new Intent(getActivity(), SearchActivity.class);
        startActivity(intent);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map  = googleMap;

        selectedMarker = null;
        markerMap = new HashMap<>();
        Datacache.initialize();
        displayedEvents = datacache.getAllDisplayedEvents();

        Map<String, MapColors> allMapColors = datacache.getEventColor();

        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                clickedMarker(marker);
                return true;
            }

        });


        for(EventMod event : displayedEvents.values()){
            LatLng coordinates = new LatLng(event.getLatitude(), event.getLongitude());
            MapColors mapColor = allMapColors.get(event.getEventType().toLowerCase());

            Marker marker = map.addMarker(new MarkerOptions().position(coordinates)
                    .icon(BitmapDescriptorFactory.defaultMarker(mapColor.getColor()))
                    .title(event.getEventType()));
            markerMap.put(marker,event);

            if(datacache.getSelectedEvent() == event){
                selectedMarker = marker;
            }
        }

        if(selectedMarker != null && isEvent){
            map.animateCamera(CameraUpdateFactory.newLatLng(selectedMarker.getPosition()));
            clickedMarker(selectedMarker);
        }
    }

    private void clickedMarker(Marker marker) {
       EventMod currentEvent = markerMap.get(marker);
       PersonMod currentPerson = datacache.getPersonMap().get(currentEvent.getPersonId());

       String nameOfPerson = currentPerson.getFirstName()+ " "+ currentPerson.getLastName();
       String eventTypeDetails = currentEvent.getEventType().toUpperCase()+ ": " + currentEvent.getCity() +", " + currentEvent.getCountry();
       String yearEvent =  "(" + currentEvent.getYear() + ")";

       name.setText(nameOfPerson);
       name.setOnClickListener(onClickText);
       eventType.setText(eventTypeDetails);
       eventType.setOnClickListener(onClickText);
       yearOfEvent.setText(yearEvent);
       yearOfEvent.setOnClickListener(onClickText);

       if(currentPerson.getGender().equalsIgnoreCase("f")){
           icon.setImageDrawable(new IconDrawable(getActivity(), FontAwesomeIcons.fa_female).colorRes(R.color.purple_200).sizeDp(40));
       }

       else{
           icon.setImageDrawable(new IconDrawable(getActivity(), FontAwesomeIcons.fa_male).colorRes(R.color.blue).sizeDp(40));
       }

       icon.setOnClickListener(onClickText);
       selectedMarker = marker;
       map.animateCamera(CameraUpdateFactory.newLatLng(selectedMarker.getPosition()));
       datacache.setSelectedEvent(currentEvent);
       lineDrawer();
    }

    private void lineDrawer(){
        Settings settings = Datacache.initialize().getSettings();

        removeAllLines();

        if(settings.isStoryLines()){
            drawStoryLine();
        }

        if(settings.isFamilyLines()){
            drawFamilyLines();
        }

        if(settings.isSpouseLines()){
            drawSpouseLines();
        }
    }

    public void removeAllLines(){
        for(Polyline currentLine : polylineList){
            currentLine.remove();
        }

        polylineList = new ArrayList<Polyline>();
    }

    public void clearMarkers(){
        for(Marker marker : markerMap.keySet()){
            marker.remove();
        }
    }

    public void drawStoryLine(){
        Datacache datacache = Datacache.initialize();
        EventMod currentEvent = markerMap.get(selectedMarker);
        PersonMod personMod = datacache.getPersonMap().get(currentEvent.getPersonId());

        List<EventMod> eventList = datacache.sortEventsByYear(datacache.getAllPersonEvents()
                .get(personMod.getPersonId()));

        if(datacache.getFilter().containsEventType(currentEvent.getEventType())){

            int index = 0;
            while(index < eventList.size()-1){

                if(datacache.getAllDisplayedEvents().containsValue(eventList.get(index))){
                    EventMod firstEvent = eventList.get(index);
                    index++;
                    nextStoryLine(firstEvent,eventList,index);
                }

                else{
                    index++;
                }
            }
        }

        else {
            return;
        }
    }

    public void nextStoryLine(EventMod firstEvent, List<EventMod> eventModList, int index){
        while ( index < eventModList.size()){

            if(datacache.getAllDisplayedEvents().containsValue(eventModList.get(index))){
                EventMod secondEvent = eventModList.get(index);

                Polyline polyline = map.addPolyline(new PolylineOptions().
                        add(new LatLng(firstEvent.getLatitude(),firstEvent.getLongitude()),
                                new LatLng(secondEvent.getLatitude(),secondEvent.getLongitude()))
                        .color(datacache.getSettings().getStoryLinesColor()));
                polylineList.add(polyline);
                return;

            }
            index++;
        }

    }

    public void drawFamilyLines(){

        EventMod currentEvent = markerMap.get(selectedMarker);
        PersonMod personMod = datacache.getPersonMap().get(currentEvent.getPersonId());
        RecursiveFamilyLines(personMod,currentEvent,12);

    }

    public void RecursiveFamilyLines(PersonMod personMod, EventMod currentEvent, int lineLength){

        if(personMod.getFatherId()!= null){
            familyLines(personMod.getFatherId(), currentEvent, lineLength);
        }

        if(personMod.getMotherId()!=null){
            familyLines(personMod.getMotherId(), currentEvent, lineLength);
        }

    }

    public void familyLines(String motherOrFatherID, EventMod event, int lineLength){

        List<EventMod> eventModList = datacache.sortEventsByYear(datacache.getAllPersonEvents().get(motherOrFatherID));

        for(int i = 0; i < eventModList.size(); i++){
            if(displayedEvents.containsValue(eventModList.get(i))){
                EventMod validEvent = eventModList.get(i);

                Polyline newPolyLine = map.addPolyline(new PolylineOptions().add(new LatLng(event.getLatitude(),event.getLongitude()),
                        new LatLng(validEvent.getLatitude(),validEvent.getLongitude()))
                        .color(datacache.getSettings().getFamilyLinesColor()).width(lineLength));
                polylineList.add(newPolyLine);

                PersonMod currentPerson = datacache.getPersonMap().get(motherOrFatherID);
                RecursiveFamilyLines(currentPerson,validEvent,lineLength/2);
                return;
            }

        }

    }

    public void drawSpouseLines(){

        Filter filter = datacache.getFilter();

        EventMod currentEvent = markerMap.get(selectedMarker);
        PersonMod currentPerson = datacache.getPersonMap().get(currentEvent.getPersonId());
        if(currentPerson.getSpouseId() == null){
            return;
        }
        List<EventMod> eventModList = datacache.sortEventsByYear(datacache.getAllPersonEvents().get(currentPerson.getSpouseId()));

        if(filter.containsEventType(currentEvent.getEventType())){
            for(int i = 0; i < eventModList.size(); i++){
                if(datacache.getAllDisplayedEvents().containsValue(eventModList.get(i))){
                    EventMod eventSpouse = eventModList.get(i);
                    Polyline polyline = map.addPolyline(new PolylineOptions().add(new LatLng(currentEvent.getLatitude(),currentEvent.getLongitude()),
                            new LatLng(eventSpouse.getLatitude(),eventSpouse.getLongitude()))
                            .color(datacache.getSettings().getSpouseLinesColor()));
                    polylineList.add(polyline);
                    return;
                }
            }
        }

    }


}