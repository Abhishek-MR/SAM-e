package com.example.anuj.e_co.BottomSheet;

/**
 * Created by abhi on 21/10/17.
 */

public class BottomSheetItemObject {

    private String name;
    private int photo;

    public BottomSheetItemObject(String name, int photo) {
        this.name = name;
        this.photo = photo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPhoto() {
        return photo;
    }

    public void setPhoto(int photo) {
        this.photo = photo;
    }
}