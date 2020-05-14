package com.jk.sismos.main.data.model;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

@Root(name = "xml", strict = false)
public class Feed {

    @ElementList(name = "item", inline = true)
    private List<Earthquake> earthquakeList;

    public List<Earthquake> getEarthquakeList() {
        return earthquakeList;
    }

    public void setEarthquakeList(List<Earthquake> earthquakeList) {
        this.earthquakeList = earthquakeList;
    }
}