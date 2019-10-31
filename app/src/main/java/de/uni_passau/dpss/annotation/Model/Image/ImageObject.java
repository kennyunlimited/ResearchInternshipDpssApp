package de.uni_passau.dpss.annotation.Model.Image;

/*
This java file is an Entity. It represents one table "image_table".
It's has following columns:
    - id;
    - image_name;
    - image_location;
    - image_width;
    - image_height;
    - obj_label;
    - crop_x_cordinate;
    - crop_y_cordinate;
    - crop_width;
    - crop_height;

1. id will be generated automatically every time a record is created.
2. other variables are passed in constructor.
*/

import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity(tableName = "image_table")
public class ImageObject {




    @PrimaryKey(autoGenerate = true)
    private int id;

    private String image_name;
    private String image_location;
    private int image_width;
    private int image_height;
    private String obj_label;
    private int crop_x_cordinate;
    private int crop_y_cordinate;
    private int crop_width;
    private int crop_height;



//constructor

    public ImageObject(String image_name, String image_location, int image_width,
                       int image_height, String obj_label, int crop_x_cordinate,
                       int crop_y_cordinate, int crop_width, int crop_height) {
        this.image_name = image_name;
        this.image_location = image_location;
        this.image_width = image_width;
        this.image_height = image_height;
        this.obj_label = obj_label;
        this.crop_x_cordinate = crop_x_cordinate;
        this.crop_y_cordinate = crop_y_cordinate;
        this.crop_width = crop_width;
        this.crop_height = crop_height;

    }


// getters and setters

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }


    public String getImage_name() {
        return image_name;
    }

    public void setImage_name(String image_name) {
        this.image_name = image_name;
    }

    public String getImage_location() {
        return image_location;
    }

    public void setImage_location(String image_location) {
        this.image_location = image_location;
    }

    public int getImage_width() {
        return image_width;
    }

    public void setImage_width(int image_width) {
        this.image_width = image_width;
    }

    public int getImage_height() {
        return image_height;
    }

    public void setImage_height(int image_height) {
        this.image_height = image_height;
    }

    public String getObj_label() {
        return obj_label;
    }

    public void setObj_label(String obj_label) {
        this.obj_label = obj_label;
    }

    public int getCrop_x_cordinate() {
        return crop_x_cordinate;
    }

    public void setCrop_x_cordinate(int crop_x_cordinate) {
        this.crop_x_cordinate = crop_x_cordinate;
    }

    public int getCrop_y_cordinate() {
        return crop_y_cordinate;
    }

    public void setCrop_y_cordinate(int crop_y_cordinate) {
        this.crop_y_cordinate = crop_y_cordinate;
    }

    public int getCrop_width() {
        return crop_width;
    }

    public void setCrop_width(int crop_width) {
        this.crop_width = crop_width;
    }

    public int getCrop_height() {
        return crop_height;
    }

    public void setCrop_height(int crop_height) {
        this.crop_height = crop_height;
    }


}