package de.uni_passau.dpss.annotation.Model;

/*
1. This java file is a DAO. It contains all the SQL queries
to be applied on all the 3 entities. There is a single DAO
Image and Word Annotations. Each SQL query is followed by
the calling method name.

2. insert, update, delete methods are pre-defined and hence
requires no SQL query to be defined.
*/

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import de.uni_passau.dpss.annotation.Model.Image.ImageObject;
import de.uni_passau.dpss.annotation.Model.Text.Label;
import de.uni_passau.dpss.annotation.Model.Text.Word;

@androidx.room.Dao
public interface AnnotationDao {

// Text - Word part

    @Insert
    void insert(Word word);

    @Update
    void update(Word word);

    @Delete
    void delete(Word word);

    @Query("DELETE FROM word_table")
    void deleteAllWords();

    @Query("SELECT * FROM word_table ORDER BY LOWER(word) ASC")
    LiveData<List<Word>> getAllWords();


    @Query("SELECT * FROM word_table WHERE label_id = :selected_label_id ORDER BY LOWER(word) ASC")
    LiveData<List<Word>> getLabelWords(int selected_label_id);

    @Query("DELETE FROM word_table WHERE label_id = :selected_label_id")
    void deleteLabelWords(int selected_label_id);


// Text - Label part

    @Query("SELECT * FROM label_table ORDER BY LOWER(label) ASC")
    LiveData<List<Label>> getAllLabels();

    @Insert
    void insert(Label label);

    @Update
    void update(Label label);

    @Delete
    void delete(Label label);

    @Query("DELETE FROM label_table")
    void deleteAllLabels();


// Image Part

    @Insert
    void insert(ImageObject imageObject);

    @Update
    void update(ImageObject imageObject);

    @Delete
    void delete(ImageObject imageObject);

    @Query("DELETE FROM image_table")
    void deleteAllImageObjects();

    @Query("SELECT * FROM image_table ORDER BY id ASC")
    LiveData<List<ImageObject>> getAllLiveImageObjects();

    @Query("SELECT * FROM image_table ORDER BY id ASC")
    List<ImageObject> getAllImageObjects();

    @Query("SELECT COUNT(id) FROM image_table")
    int getImageObjectRecordSize();

    @Query("SELECT * FROM image_table ORDER BY id LIMIT 50 OFFSET :off")
    List<ImageObject> getNImageObjectRecord(int off);



}