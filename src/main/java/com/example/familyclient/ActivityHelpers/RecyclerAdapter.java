package com.example.familyclient.ActivityHelpers;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.familyclient.Activities.EventActivity;
import com.example.familyclient.Activities.PersonActivity;
import com.example.familyclient.R;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import Model.EventMod;
import Model.PersonMod;
import Net.Datacache;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> implements Filterable {

    private static final String TAG = "RecyclerAdapter";

    List<EventMod> allEvents;
    List<PersonMod> allPersons;
    Context context;
    List<Object> everythingCombined = new ArrayList<>();
    List<Object> objectList = starterSearch(everythingCombined);

    public RecyclerAdapter(List<PersonMod> person, List<EventMod> event, Context c){
        allPersons = person;
        allEvents = event;
        everythingCombined.addAll(allPersons);
        everythingCombined.addAll(allEvents);
        objectList.addAll(everythingCombined);
        context = c;
    }


    // Filtering the events
    @Override
    public Filter getFilter() {
        return filter;
    }

    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Object> filteredList = new ArrayList<>();
            if(constraint.toString().isEmpty()){
                filteredList.addAll(starterSearch(everythingCombined));
            }

            else{
                for(Object personOrEvent : everythingCombined){
                    if(personOrEvent instanceof PersonMod){
                        PersonMod person = (PersonMod)personOrEvent;
                        if(person.getFirstName().toLowerCase().contains(constraint.toString().toLowerCase())
                                || person.getLastName().toLowerCase().contains(constraint.toString().toLowerCase())){
                            filteredList.add(person);
                        }

                    }

                    else{
                        EventMod event = (EventMod)personOrEvent;
                        String year = ""+ event.getYear();
                        String longitude = ""+ event.getLongitude();
                        String latitude = ""+event.getLatitude();
                        PersonMod  person = Datacache.initialize().getPersonMap().get(event.getPersonId());
                        String name = person.getFirstName()+person.getLastName();
                        String eventType = event.getEventType();
                        String city = event.getCity();
                        String country = event.getCountry();
                        String everythingInEvent = year + latitude +longitude+name+eventType+city+country;

                        if(Datacache.initialize().getAllDisplayedEvents().containsValue(event)) {
                            if (everythingInEvent.toLowerCase().contains(constraint.toString().toLowerCase())) {
                                filteredList.add(event);
                            }
                        }


                    }

                }

            }

            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredList;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            objectList.clear();
            objectList.addAll((Collection<?>) results.values);
            notifyDataSetChanged();

        }
    };

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Log.i(TAG, "onCreateViewHolder: ");
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.list_child, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        if(objectList.get(position) instanceof EventMod){

            EventMod event = (EventMod)objectList.get(position);
            String eventText = event.getEventType().toUpperCase() + ": " + event.getCity()+ ", " +
                    event.getCountry() + " (" + event.getYear() + ")";
            holder.topInfo.setText(eventText);
            holder.icon.setImageDrawable(new IconDrawable(context,FontAwesomeIcons.fa_map_marker).
                    colorRes(R.color.black));

            PersonMod currentPer = Datacache.initialize().getPersonMap().get(event.getPersonId());
            String name = currentPer.getFirstName() + " " + currentPer.getLastName();
            holder.lowerInfo.setText(name);
        }

        else{

            PersonMod currentPerson = (PersonMod) objectList.get(position);
            holder.topInfo.setText(currentPerson.getFirstName() + " " + currentPerson.getLastName());
            if(currentPerson.getGender().equalsIgnoreCase("f")){
                holder.icon.setImageDrawable(new IconDrawable(context, FontAwesomeIcons.fa_female).
                        colorRes(R.color.pink));
            }

            else{
                holder.icon.setImageDrawable(new IconDrawable(context, FontAwesomeIcons.fa_male).
                        colorRes(R.color.blue));
            }
            holder.lowerInfo.setText("   ");

        }
    }

    @Override
    public int getItemCount() {
        return objectList.size();
    }

    class  ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView icon;
        TextView topInfo;
        TextView lowerInfo;

        public ViewHolder(@NonNull View itemView) {

            super(itemView);
            icon = itemView.findViewById(R.id.list_icon);
            topInfo = itemView.findViewById(R.id.topInfo);
            lowerInfo = itemView.findViewById(R.id.lowerInfo);

            itemView.setOnClickListener(this);

        }


        @Override
        public void onClick(View v) {
            if(objectList.get(getAdapterPosition()) instanceof PersonMod){
                PersonMod person = (PersonMod) objectList.get(getAdapterPosition());
                Intent intent = new Intent(context, PersonActivity.class);
                Datacache.initialize().setSelectedPerson(person);
                context.startActivity(intent);
            }

            else{
                EventMod event = (EventMod) objectList.get(getAdapterPosition());
                Intent intent = new Intent(context, EventActivity.class);
                intent.putExtra("Event", "Event");
                Datacache.initialize().setSelectedEvent(event);
                context.startActivity(intent);
            }
        }
    }

    public List<Object> starterSearch(List<Object> array){
        List<Object> starter = new ArrayList<>();
        for(Object obj : array){
            if(obj instanceof PersonMod){
                starter.add(obj);
            }

            else{
                if(Datacache.initialize().getAllDisplayedEvents().containsValue((EventMod) obj)){
                    starter.add(obj);
                }
            }
        }

        return starter;
    }


}
