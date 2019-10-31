package de.uni_passau.dpss.annotation.View.Text.Word;

/*
1. This class activity provides user with UI to add or Edit Word
 into the word-table.
*/


import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;


import de.uni_passau.dpss.annotation.R;

public class AddEditWordActivity extends AppCompatActivity {
    public static final String EXTRA_ID =
            "de.uni_passau.dpss.annotation.EXTRA_ID";
    public static final String EXTRA_WORD =
            "de.uni_passau.dpss.annotation.EXTRA_WORD";


    private EditText editTextWord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_word);

        editTextWord = findViewById(R.id.edit_text_word);


        ActionBar actionbar = getSupportActionBar ();
        actionbar.setDisplayHomeAsUpEnabled ( true );
        actionbar.setHomeAsUpIndicator ( R.drawable.ic_close);

        Intent intent = getIntent();

        if (intent.hasExtra(EXTRA_ID)){
            setTitle("Edit Note Word");
            editTextWord.setText(intent.getStringExtra(EXTRA_WORD));
        }else{
            setTitle("Add Note Word");
        }


    }

    private void saveNote() {
        String title = editTextWord.getText().toString();


        if (title.trim().isEmpty()) {
            Toast.makeText(this, "Please insert a Word", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent data = new Intent();
        data.putExtra(EXTRA_WORD, title);

        int id = getIntent().getIntExtra(EXTRA_ID, -1);

        if (id != -1){
            data.putExtra(EXTRA_ID, id);
        }

        setResult(RESULT_OK, data);
        finish();
    }

    private void cancelWord(){
        setResult(RESULT_CANCELED);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_note_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_note:
                saveNote();
                return true;
            default:
                cancelWord();
                return true;
        }
    }
}