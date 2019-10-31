package de.uni_passau.dpss.annotation.Repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

import de.uni_passau.dpss.annotation.Model.AnnotationDao;
import de.uni_passau.dpss.annotation.Model.Image.ImageObject;
import de.uni_passau.dpss.annotation.Model.Text.Label;
import de.uni_passau.dpss.annotation.Model.TextDatabase;
import de.uni_passau.dpss.annotation.Model.Text.Word;


/*
1. This Repository will act as a way to
join different data source say, local storage
& cloud storage. Right now, it is dealing
only with local storage.

2. It will call methods mentioned in
AnnotationDao using TextDatabase object.
*/


public class TextRepository {
    private AnnotationDao annotationDao;
    private LiveData<List<Label>> all_word_label;



    public TextRepository(Application application) {
        TextDatabase database = TextDatabase.getInstance(application);
        annotationDao = database.TextDao();
        all_word_label = annotationDao.getAllLabels();

    }


// Text - Word :: methods

    public void insert(Word word) {
        new InsertWordAsyncTask(annotationDao).execute(word);
    }

    public void update(Word word) {
        new UpdateWordAsyncTask(annotationDao).execute(word);
    }

    public void delete(Word word) {
        new DeleteWordAsyncTask(annotationDao).execute(word);
    }

    public void deleteAllWords() {
        new DeleteAllWordsAsyncTask(annotationDao).execute();
    }

    public void deleteLabelWords(Label label) {
        new DeleteLabelWordsAsyncTask(annotationDao).execute(label);

    }

    public LiveData<List<Word>> getAllWords() {
        return annotationDao.getAllWords();
    }

    public LiveData<List<Word>> getLabelWords(int selected_label_id) {
        return annotationDao.getLabelWords(selected_label_id);
    }




    private static class InsertWordAsyncTask extends AsyncTask<Word, Void, Void> {
        private AnnotationDao annotationDao;

        private InsertWordAsyncTask(AnnotationDao annotationDao) {
            this.annotationDao = annotationDao;
        }

        @Override
        protected Void doInBackground(Word... words) {
            annotationDao.insert(words[0]);
            return null;
        }
    }

    private static class UpdateWordAsyncTask extends AsyncTask<Word, Void, Void> {
        private AnnotationDao annotationDao;

        private UpdateWordAsyncTask(AnnotationDao annotationDao) {
            this.annotationDao = annotationDao;
        }

        @Override
        protected Void doInBackground(Word... words) {
            annotationDao.update(words[0]);
            return null;
        }
    }

    private static class DeleteWordAsyncTask extends AsyncTask<Word, Void, Void> {
        private AnnotationDao annotationDao;

        private DeleteWordAsyncTask(AnnotationDao annotationDao) {
            this.annotationDao = annotationDao;
        }

        @Override
        protected Void doInBackground(Word... words) {
            annotationDao.delete(words[0]);
            return null;
        }
    }

    private static class DeleteAllWordsAsyncTask extends AsyncTask<Void, Void, Void> {
        private AnnotationDao annotationDao;

        private DeleteAllWordsAsyncTask(AnnotationDao annotationDao) {
            this.annotationDao = annotationDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            annotationDao.deleteAllWords();
            return null;
        }
    }

    private static class DeleteLabelWordsAsyncTask extends AsyncTask<Label, Void, Void> {
        private AnnotationDao annotationDao;

        private DeleteLabelWordsAsyncTask(AnnotationDao annotationDao) {
            this.annotationDao = annotationDao;
        }

        @Override
        protected Void doInBackground(Label... labels) {
            annotationDao.deleteLabelWords(labels[0].getLabel_id());
            return null;
        }
    }




// Text - Label :: methods


    public LiveData<List<Label>> getAllLabels(){
        return annotationDao.getAllLabels();
    }

    public void insert(Label label) {
        new InsertLabelAsyncTask(annotationDao).execute(label);
    }

    public void update(Label label) {
        new UpdateLabelAsyncTask(annotationDao).execute(label);
    }

    public void delete(Label label) {
        new DeleteLabelAsyncTask(annotationDao).execute(label);
    }

    public void deleteAllLabels() {
        new DeleteAllLabelsAsyncTask(annotationDao).execute();
    }




    private static class InsertLabelAsyncTask extends AsyncTask<Label, Void, Void> {
        private AnnotationDao annotationDao;

        private InsertLabelAsyncTask(AnnotationDao annotationDao) {
            this.annotationDao = annotationDao;
        }

        @Override
        protected Void doInBackground(Label... labels) {
            annotationDao.insert(labels[0]);
            return null;
        }
    }

    private static class UpdateLabelAsyncTask extends AsyncTask<Label, Void, Void> {
        private AnnotationDao annotationDao;

        private UpdateLabelAsyncTask(AnnotationDao annotationDao) {
            this.annotationDao = annotationDao;
        }

        @Override
        protected Void doInBackground(Label... labels) {
            annotationDao.update(labels[0]);
            return null;
        }
    }

    private static class DeleteLabelAsyncTask extends AsyncTask<Label, Void, Void> {
        private AnnotationDao annotationDao;

        private DeleteLabelAsyncTask(AnnotationDao annotationDao) {
            this.annotationDao = annotationDao;
        }

        @Override
        protected Void doInBackground(Label... labels) {
            annotationDao.delete(labels[0]);
            annotationDao.deleteLabelWords(labels[0].getLabel_id());
            return null;
        }
    }

    private static class DeleteAllLabelsAsyncTask extends AsyncTask<Void, Void, Void> {
        private AnnotationDao annotationDao;

        private DeleteAllLabelsAsyncTask(AnnotationDao annotationDao) {
            this.annotationDao = annotationDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            annotationDao.deleteAllLabels();
            annotationDao.deleteAllWords();
            return null;
        }
    }



// Image :: methods

    public int getImageObjectRecordSize(){
        return annotationDao.getImageObjectRecordSize();
    }

    public List<ImageObject> getNImageObjectRecord(int off){
        return annotationDao.getNImageObjectRecord(off);
    }

    public List<ImageObject> getAllImageObjects(){
        return annotationDao.getAllImageObjects();
    }


    public LiveData<List<ImageObject>> getAllLiveImageObjects(){
        return annotationDao.getAllLiveImageObjects();
    }

    public void insert(ImageObject imageObject) {
        new InsertImageObjectAsyncTask(annotationDao).execute(imageObject);
    }

    public void update(ImageObject imageObject) {
        new UpdateImageObjectAsyncTask(annotationDao).execute(imageObject);
    }

    public void delete(ImageObject imageObject) {
        new deleteImageObjectAsyncTask(annotationDao).execute(imageObject);
    }

    public void deleteAllImageObjects() {
        new deleteAllImageObjectsAsyncTask(annotationDao).execute();
    }


    private static class InsertImageObjectAsyncTask extends AsyncTask<ImageObject, Void, Void> {
        private AnnotationDao annotationDao;

        private InsertImageObjectAsyncTask(AnnotationDao annotationDao) {
            this.annotationDao = annotationDao;
        }

        @Override
        protected Void doInBackground(ImageObject... imageObjects) {
            annotationDao.insert(imageObjects[0]);
            return null;
        }
    }

    private static class UpdateImageObjectAsyncTask extends AsyncTask<ImageObject, Void, Void> {
        private AnnotationDao annotationDao;

        private UpdateImageObjectAsyncTask(AnnotationDao annotationDao) {
            this.annotationDao = annotationDao;
        }

        @Override
        protected Void doInBackground(ImageObject... imageObjects) {
            annotationDao.update(imageObjects[0]);
            return null;
        }
    }

    private static class deleteImageObjectAsyncTask extends AsyncTask<ImageObject, Void, Void> {
        private AnnotationDao annotationDao;

        private deleteImageObjectAsyncTask(AnnotationDao annotationDao) {
            this.annotationDao = annotationDao;
        }

        @Override
        protected Void doInBackground(ImageObject... imageObjects) {
            annotationDao.delete(imageObjects[0]);
            return null;
        }
    }

    private static class deleteAllImageObjectsAsyncTask extends AsyncTask<Void, Void, Void> {
        private AnnotationDao annotationDao;

        private deleteAllImageObjectsAsyncTask(AnnotationDao annotationDao) {
            this.annotationDao = annotationDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            annotationDao.deleteAllImageObjects();
            return null;
        }
    }



}