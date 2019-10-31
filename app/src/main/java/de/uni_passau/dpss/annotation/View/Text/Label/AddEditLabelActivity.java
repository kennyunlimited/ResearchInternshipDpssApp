package de.uni_passau.dpss.annotation.View.Text.Label;

/*
1. This class activity provides user with UI to add or Edit Label
 into the label-table.
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

public class AddEditLabelActivity extends AppCompatActivity {
    public static final String EXTRA_LABEL_ID =
            "de.uni_passau.dpss.annotation.EXTRA_ID";
    public static final String EXTRA_LABEL =
            "de.uni_passau.dpss.annotation.EXTRA_LABEL";


    private EditText editTextLabel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_label);

        editTextLabel = findViewById(R.id.edit_text_label);


        ActionBar actionbar = getSupportActionBar ();
        actionbar.setDisplayHomeAsUpEnabled ( true );
        actionbar.setHomeAsUpIndicator ( R.drawable.ic_close);




        Intent intent = getIntent();

        if (intent.hasExtra(EXTRA_LABEL_ID)){
            setTitle("Edit Label Name");
            editTextLabel.setText(intent.getStringExtra(EXTRA_LABEL));
        }else{
            setTitle("Add New Label");
        }


    }

    private void saveNote() {
        String title = editTextLabel.getText().toString();


        if (title.trim().isEmpty()) {
            Toast.makeText(this, "Please insert a Word", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent data = new Intent();
        data.putExtra(EXTRA_LABEL, title);

        int id = getIntent().getIntExtra(EXTRA_LABEL_ID, -1);

        if (id != -1){
            data.putExtra(EXTRA_LABEL_ID, id);
        }

        setResult(RESULT_OK, data);
        finish();
    }

    private void cancelLabel(){

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
                cancelLabel();
                return super.onOptionsItemSelected(item);
        }
    }
}