package com.example.shurik.mobilesklad.view;

public class ContainerProperty {
    String value;
    String name;

    public ContainerProperty(String name, String value) {
        this.value = value;
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
