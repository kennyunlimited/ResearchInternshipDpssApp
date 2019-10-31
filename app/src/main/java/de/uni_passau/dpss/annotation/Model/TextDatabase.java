package de.uni_passau.dpss.annotation.Model;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;


import android.content.Context;
import android.os.AsyncTask;

import de.uni_passau.dpss.annotation.Model.Image.ImageObject;
import de.uni_passau.dpss.annotation.Model.Text.Label;
import de.uni_passau.dpss.annotation.Model.Text.Word;


/*
1. This Database is a holder class that uses annotation
to define the list of entities and database version.
This class content defines the list of DAOs.
*/


@Database(entities = {Word.class, Label.class, ImageObject.class}, version = 3)
public abstract class TextDatabase extends RoomDatabase {

    private static TextDatabase instance;

    public abstract AnnotationDao TextDao();

/*
This method will get only one instance of db connection
throughout.
 */

    public static synchronized TextDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    TextDatabase.class, "note_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback).allowMainThreadQueries()
                    .build();
        }
        return instance;
    }

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(instance).execute();
        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {
        private AnnotationDao AnnotationDao;

        private PopulateDbAsyncTask(TextDatabase db) {
            AnnotationDao = db.TextDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            return null;
        }
    }
}