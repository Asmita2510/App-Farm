package com.example.appfarm;

public class SliderModel {

    private String banner;
    private String backgroundColour;

    public SliderModel(String banner, String backgroundColour) {
        this.banner = banner;
        this.backgroundColour = backgroundColour;
    }

    public String getBanner() {
        return banner;
    }

    public void setBanner(String banner) {
        this.banner = banner;
    }

    public String getBackgroundColour() {
        return backgroundColour;
    }

    public void setBackgroundColour(String backgroundColour) {
        this.backgroundColour = backgroundColour;
    }
}
