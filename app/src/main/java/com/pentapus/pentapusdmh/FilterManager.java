package com.pentapus.pentapusdmh;

import java.util.Observable;

/**
 * Created by konrad.fellmann on 19.05.2016.
 */
public class FilterManager extends Observable {
    private String query;

    public void setQuery(String query) {
        this.query = query;
        setChanged();
        notifyObservers();
    }

    public String getQuery() {
        return query;
    }
}
