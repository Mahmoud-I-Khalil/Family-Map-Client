package com.example.familyclient.ActivityHelpers;

import android.app.Person;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.familyclient.R;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

import java.util.List;

import Model.EventMod;
import Model.PersonMod;
import Net.Datacache;

public class PersonActivityAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<String> parentHeader;
    private List<PersonMod> family;
    private List<EventMod> eventsArray;
    private PersonMod currentPerson;

    private Datacache datacache = Datacache.initialize();

    private TextView topInfo;
    private TextView buttomInfo;
    private ImageView icon;

    public PersonActivityAdapter(Context context, List<String> parentHeader, List<PersonMod> family,
                                 List<EventMod> eventsArray, PersonMod currentPerson) {
        this.context = context;
        this.parentHeader = parentHeader;
        this.family = family;
        this.eventsArray = eventsArray;
        this.currentPerson = currentPerson;
    }

    @Override
    public int getGroupCount() {
        return parentHeader.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        if(groupPosition == 0){
            return eventsArray.size();
        }

        else {
            return family.size();
        }
    }

    @Override
    public Object getGroup(int groupPosition) {
        if(groupPosition == 0){
            return eventsArray;
        }

        else {
            return family;
        }
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        if(groupPosition == 0){
            return eventsArray.get(childPosition);
        }

        else {
            return family.get(childPosition);
        }
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String title = parentHeader.get(groupPosition);

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_parent, null);
        }

        TextView header = convertView.findViewById(R.id.parentHeader);
        header.setText(title);

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_child, null);
        }

        topInfo = convertView.findViewById(R.id.topInfo);
        buttomInfo = convertView.findViewById(R.id.lowerInfo);
        icon = convertView.findViewById(R.id.list_icon);

        if(groupPosition == 0){
            EventMod currentEvent = (EventMod) getChild(groupPosition,childPosition);
            icon.setImageDrawable(new IconDrawable(convertView.getContext(),FontAwesomeIcons.fa_map_marker).
                    colorRes(R.color.black));
            infoWriter(currentEvent,null);
        }

        else{
            PersonMod currPerson = (PersonMod) getChild(groupPosition,childPosition);

            if(currPerson.getGender().equalsIgnoreCase("f")){
                icon.setImageDrawable(new IconDrawable(convertView.getContext(),FontAwesomeIcons.fa_female).
                        colorRes(R.color.pink));
            }

            else{
                icon.setImageDrawable(new IconDrawable(convertView.getContext(),FontAwesomeIcons.fa_male).
                        colorRes(R.color.darkBlue));
            }

            infoWriter(null, currPerson);

        }

        return convertView;
    }

    private void infoWriter(EventMod event, PersonMod person) {

        if(person == null){

            String eventText = event.getEventType().toUpperCase() + ": " + event.getCity()+ ", " +
                    event.getCountry() + " (" + event.getYear() + ")";
            topInfo.setText(eventText);

            PersonMod currentPer = datacache.getPersonMap().get(event.getPersonId());
            String name = currentPer.getFirstName() + " " + currentPer.getLastName();
            buttomInfo.setText(name);
        }

        else{
            String name = person.getFirstName() + " " + person.getLastName();
            topInfo.setText(name);
            buttomInfo.setText(relationship(person));
        }

    }

    private String relationship(PersonMod person) {

        if(currentPerson.getSpouseId() != null) {
            if (currentPerson.getSpouseId().equals(person.getPersonId())) {
                return "Spouse";
            }
        }

        if(currentPerson.getMotherId() != null && currentPerson.getFatherId() != null){
            if(currentPerson.getFatherId().equals(person.getPersonId())){
                return "Father";
            }

            else if(currentPerson.getMotherId().equals(person.getPersonId())){
                return "Mother";

            }
        }

        if(person.getMotherId() != null && person.getFatherId() != null){
            if(currentPerson.getPersonId().equals(person.getFatherId()) ||
                    currentPerson.getPersonId().equals(person.getMotherId())){
                return "Child";
            }
        }

        return "Error";
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
