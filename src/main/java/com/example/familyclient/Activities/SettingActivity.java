package com.example.familyclient.Activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.example.familyclient.R;

import Utility.Filter;
import Utility.Settings;
import Net.Datacache;

public class SettingActivity extends AppCompatActivity {

    private Datacache datacache = Datacache.initialize();
    private Switch lifeStoryLines;
    private Switch familyLines;
    private Switch spouseLines;
    private Switch fatherLines;
    private Switch motherLines;
    private Switch maleEvents;
    private Switch femaleEvents;
    private Button logout;

    private Filter filter;
    private Settings setting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        filter = datacache.getFilter();
        setting = datacache.getSettings();

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        lifeStoryLines = findViewById(R.id.life_story_lines);
        lifeStoryLines.setChecked(setting.isStoryLines());
        lifeStoryLines.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setting.setStoryLines(isChecked);
            }
        });

        familyLines = findViewById(R.id.family_tree_lines);
        familyLines.setChecked(setting.isFamilyLines());
        familyLines.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setting.setFamilyLines(isChecked);
            }
        });

        spouseLines = findViewById(R.id.spouse_lines);
        spouseLines.setChecked(setting.isSpouseLines());
        spouseLines.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setting.setSpouseLines(isChecked);
            }
        });

        fatherLines = findViewById(R.id.father_side_lines);
        fatherLines.setChecked(filter.isFathersSide());
        fatherLines.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                filter.setFathersSide(isChecked);
            }
        });

        motherLines = findViewById(R.id.mother_side_lines);
        motherLines.setChecked(filter.isMothersSide());
        motherLines.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                filter.setMothersSide(isChecked);
            }
        });

        maleEvents = findViewById(R.id.male_events);
        maleEvents.setChecked(filter.isMales());
        maleEvents.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                filter.setMales(isChecked);
            }
        });

        femaleEvents = findViewById(R.id.female_events);
        femaleEvents.setChecked(filter.isFemales());
        femaleEvents.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                filter.setFemales(isChecked);
            }
        });

        logout = findViewById(R.id.loginOut);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(SettingActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}