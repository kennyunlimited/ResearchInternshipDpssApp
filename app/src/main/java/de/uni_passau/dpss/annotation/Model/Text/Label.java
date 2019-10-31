package de.uni_passau.dpss.annotation.Model.Text;

/*
This java file is an Entity. It represents one table "label_table".
It's has following columns:
    - label_id;
    - label;

1. label_id will be generated automatically every time a record is created.
2. label is passed in constructor.
*/


import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "label_table")
public class Label implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    private int label_id;

    private String label;


// constructors

    @Ignore
    public Label(){

    }

    public Label(String label) {
        this.label = label;
    }

/*
Parcelling Label object to transfer to Word table.
label_id is foreign key in word table to link both tables
*/
    protected Label(Parcel in) {
        label_id = in.readInt();
        label = in.readString();
    }

    public static final Creator<Label> CREATOR = new Creator<Label>() {
        @Override
        public Label createFromParcel(Parcel in) {
            return new Label(in);
        }

        @Override
        public Label[] newArray(int size) {
            return new Label[size];
        }
    };


// getters & setters

    public void setLabel_id(int label_id) {
        this.label_id = label_id;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public int getLabel_id() {
        return label_id;
    }


/*
Parcelling Label object to transfer to Word table.
label_id is foreign key in word table to link both tables
*/

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(label_id);
        parcel.writeString(label);
    }


}
