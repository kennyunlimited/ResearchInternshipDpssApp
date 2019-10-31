package de.uni_passau.dpss.annotation.Model.Text;

/*
This java file is an Entity. It represents one table "word_table".
It's has following columns:
    - id;
    - word;
    -label_id;

1. id will be generated automatically every time a record is created.
2. other variables are passed in constructor.
3. label_id links "word_table" & "label_id"
*/

import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity(tableName = "word_table")
public class Word {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String word;
    private int label_id;

// constructor

    public Word(String word, int label_id) {
        this.word = word;
        this.label_id = label_id;
    }

// getters & setters

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getWord() {
        return word;
    }

    public int getLabel_id() {
        return label_id;
    }

    public void setLabel_id(int label_id) {
        this.label_id = label_id;
    }



}