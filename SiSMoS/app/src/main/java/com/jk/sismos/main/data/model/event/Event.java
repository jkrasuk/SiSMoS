package com.jk.sismos.main.data.model.event;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

class Event {

    @SerializedName("type_events")
    @Expose
    private String typeEvents;

    @SerializedName("state")
    @Expose
    private String state;

    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("group")
    @Expose
    private Integer group;

    public String getTypeEvents() {
        return typeEvents;
    }

    public void setTypeEvents(String typeEvents) {
        this.typeEvents = typeEvents;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getGroup() {
        return group;
    }

    public void setGroup(Integer group) {
        this.group = group;
    }

    @Override
    public String toString() {
        return "Event{" +
                "typeEvents='" + typeEvents + '\'' +
                ", state='" + state + '\'' +
                ", description='" + description + '\'' +
                ", group=" + group +
                '}';
    }
}
