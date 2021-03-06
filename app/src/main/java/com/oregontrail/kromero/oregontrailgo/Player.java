package com.oregontrail.kromero.oregontrailgo;

import android.util.JsonReader;

import org.json.JSONException;
import org.json.JSONObject;

public class Player {
    private String id;
    private float percentComplete;
    private int eventId;
    private String eventRecipient;
    private boolean isAlive;
    private int food;
    private int water;
    private int bullets;
    private int supplies;

    public Player (String id) {
        this.id = id;
        percentComplete = 0;
        eventId = -1;
        eventRecipient = "";
        isAlive = true;
        food = 0;
        water = 0;
        bullets = 0;
        supplies = 0;
    }

    public String getId() {
        return id;
    }

    public float getPercentComplete () {
        return percentComplete;
    }

    public int getEventId () {
        return eventId;
    }

    public String getEventRecipient() {
        return eventRecipient;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public int getFood() {
        return food;
    }

    public int getWater () {
        return water;
    }

    public int getBullets() {
        return bullets;
    }

    public int getSupplies() {
        return supplies;
    }

    public String getEventLine() {
        switch (eventId) {
            case 0:
                return "died of dysentery.\n";
            case 1:
                return "been attacked by bandits.\n";
            case 2:
                return "stumbled upon plentiful hunting grounds.\n";
            case 3:
                return "collected bad water.\n";
            case 4:
                return "broken a wheel on your wagon.\n";
            case 5:
                return "feeling the effects of starvation.\n";
            case 6:
                return "stumbled upon a lovely town.\n";
            case 7:
                return "died of a snake bite.\n";
        }
        return null;
    }

    public boolean eventHasChoice () {
        switch (eventId) {
            case 0: case 7: case 6:
                return false;
            case 1: case 2: case 3: case 4: case 5:
                return true;
        }
        return false;
    }

    public String getEventPrompt() {
        switch (eventId) {
            case 0: case 7:
                return "Nothing can be done.";
            case 1:
                return "Use 1 bullet to fend off the bandits?";
            case 2:
                return "Use 1 bullet to hunt for the party?";
            case 3:
                return "Use 1 clean water to remedy the situation?";
            case 4:
                return "Use 1 spare supplies to remedy the situation?";
            case 5:
                return "Use 1 extra food to remedy the situation?";
            case 6:
                return "What good luck!";
        }
        return null;
    }

    public void clearEvent () {
        eventId = -1;
    }

    public void updatePlayerData(String json) {
        try {
            JSONObject jsonObj = new JSONObject(json);
            JSONObject player = new JSONObject(jsonObj.getString("client"));
            if (player.getString("id").equals(id)) {
                percentComplete = Float.parseFloat(jsonObj.getString("percent_complete"));
                eventId = Integer.parseInt(jsonObj.getString("event"));
                eventRecipient = jsonObj.getString("event_client");
                isAlive = Boolean.parseBoolean(player.getString("is_alive"));
                food = Integer.parseInt(player.getString("food"));
                water = Integer.parseInt(player.getString("water"));
                bullets = Integer.parseInt(player.getString("bullets"));
                supplies = Integer.parseInt(player.getString("supplies"));
            }
            else {
                System.out.println("The message wasn't for me");
            }
        } catch (final JSONException e) {
            e.printStackTrace();
            System.out.println("The player got an f'ed up JSON string");
        }
    }
}