package de.uni_passau.dpss.annotation.ViewModel;

/*
1. This class provides data to UI from Repository.

2. It helps to sustain configuration changes.

3. It contains all the methods to deal with database.
*/


import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import android.app.Application;


import java.util.List;

import de.uni_passau.dpss.annotation.Model.Image.ImageObject;
import de.uni_passau.dpss.annotation.Model.Text.Label;
import de.uni_passau.dpss.annotation.Model.Text.Word;
import de.uni_passau.dpss.annotation.Repository.TextRepository;

public class ViewModel extends AndroidViewModel {
    private TextRepository repository;
    private LiveData<List<Label>> all_word_label;



    public ViewModel(@NonNull Application application){
        super(application);
        repository = new TextRepository(application);
        all_word_label = repository.getAllLabels();
    }

// Word(text) part

    public void insert(Word word) {
        repository.insert(word);
    }

    public void update(Word word) {
        repository.update(word);
    }

    public void delete(Word word) {
        repository.delete(word);
    }

    public void deleteAllWords() {
        repository.deleteAllWords();
    }

    public void deleteLabelWords(Label label) {
        repository.deleteLabelWords(label);
    }


    public LiveData<List<Word>> getAllWords() {
        return repository.getAllWords();
    }

    public LiveData<List<Word>> getLabelWords(Label label){
        return repository.getLabelWords(label.getLabel_id());
    }

// Label(text) part

    public void insert(Label label) {
        repository.insert(label);
    }

    public void update(Label label) {
        repository.update(label);
    }

    public void delete(Label label) {
        repository.delete(label);
    }

    public void deleteAllLabels() {
        repository.deleteAllLabels();
    }

    public LiveData<List<Label>> getAllLabels() {
        return repository.getAllLabels();
    }


// Image part

    public void insert(ImageObject imageObject) {
        repository.insert(imageObject);
    }

    public void update(ImageObject imageObject) {
        repository.update(imageObject);
    }

    public void delete(ImageObject imageObject) {
        repository.delete(imageObject);
    }

    public void deleteAllImageObjects() {
        repository.deleteAllImageObjects();
    }

    public LiveData<List<ImageObject>> getAllLiveImageObjects() {
        return repository.getAllLiveImageObjects();
    }

    public List<ImageObject> getAllImageObjects() {
        return repository.getAllImageObjects();
    }

    public List<ImageObject> getNImageObjectRecord(int off) {
        return repository.getNImageObjectRecord(off);
    }

    public int getImageObjectRecordSize() {
        return repository.getImageObjectRecordSize();
    }

}