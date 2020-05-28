package com.example.explorer.model.spaceResponse;

public class Data {

    private String date_created;
    private String description;
    private String title;
    private String location;
    private String secondary_creator;
    private String nasa_id;

    public String getNasa_id() {
        return nasa_id;
    }

    public void setNasa_id(String nasa_id) {
        this.nasa_id = nasa_id;
    }

    public String getSecondary_creator() {
        return secondary_creator;
    }

    public void setSecondary_creator(String secondary_creator) {
        this.secondary_creator = secondary_creator;
    }

    public String getDate_created() {
        return date_created;
    }

    public void setDate_created(String date_created) {
        this.date_created = date_created;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
