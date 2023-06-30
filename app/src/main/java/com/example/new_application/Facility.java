package com.example.new_application;

import java.util.List;

public class Facility {
    private String name;
    private List<FacilityOption> options;
    private String iconUrl;
    private String selectedOption;

    public Facility(String name, List<FacilityOption> options, String iconUrl) {
        this.name = name;
        this.options = options;
        this.iconUrl = iconUrl;
    }

    public String getName() {
        return name;
    }

    public List<FacilityOption> getOptions() {
        return options;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public String getSelectedOption() {
        return selectedOption;
    }

    public void setSelectedOption(String selectedOption) {
        this.selectedOption = selectedOption;
    }
}
