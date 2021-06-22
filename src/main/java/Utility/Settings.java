package Utility;

import android.graphics.Color;

import java.util.ArrayList;

public class Settings {

    private boolean familyLines;
    private boolean storyLines;
    private boolean spouseLines;
    private int familyLinesColor;
    private int storyLinesColor;
    private int spouseLinesColor;
    private ArrayList<Integer> settingsSelections;

    public Settings(){
        familyLines = true;
        storyLines = true;
        spouseLines = true;
        familyLinesColor = Color.GREEN;
        storyLinesColor = Color.RED;
        spouseLinesColor = Color.YELLOW;
        settingsSelections = new ArrayList<Integer>();
        for(int i = 0; i < 4; i++){
            settingsSelections.add(0);
        }
    }

    public boolean isFamilyLines() {
        return familyLines;
    }

    public void setFamilyLines(boolean familyLines) {
        this.familyLines = familyLines;
    }

    public boolean isStoryLines() {
        return storyLines;
    }

    public void setStoryLines(boolean storyLines) {
        this.storyLines = storyLines;
    }

    public boolean isSpouseLines() {
        return spouseLines;
    }

    public void setSpouseLines(boolean spouseLines) {
        this.spouseLines = spouseLines;
    }

    public int getFamilyLinesColor() {
        return familyLinesColor;
    }

    public void setFamilyLinesColor(int familyLinesColor) {
        this.familyLinesColor = familyLinesColor;
    }

    public int getStoryLinesColor() {
        return storyLinesColor;
    }

    public void setStoryLinesColor(int storyLinesColor) {
        this.storyLinesColor = storyLinesColor;
    }

    public int getSpouseLinesColor() {
        return spouseLinesColor;
    }

    public void setSpouseLinesColor(int spouseLinesColor) {
        this.spouseLinesColor = spouseLinesColor;
    }

    public ArrayList<Integer> getSettingsSelections() {
        return settingsSelections;
    }

    public void setSettingsSelections(int selection, int index) {
        settingsSelections.set(index,selection);
    }
}
