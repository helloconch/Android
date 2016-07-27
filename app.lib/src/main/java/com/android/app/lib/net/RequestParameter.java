package com.android.app.lib.net;


import java.io.Serializable;

public class RequestParameter implements Serializable, Comparable<Object> {

    private String name;
    private String value;

    public RequestParameter(String name, String value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public int compareTo(Object another) {

        int compared;
        final RequestParameter parameter = (RequestParameter) another;
        compared = name.compareTo(parameter.name);
        if (compared == 0) {
            compared = value.compareTo(parameter.value);
        }

        return compared;
    }


    @Override
    public boolean equals(Object o) {

        if (null == o) {
            return false;
        }

        if (this == o) {
            return true;
        }

        if (o instanceof RequestParameter) {
            final RequestParameter parameter = (RequestParameter) o;
            return name.equals(parameter.name) && value.equals(parameter.value);
        }

        return false;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
