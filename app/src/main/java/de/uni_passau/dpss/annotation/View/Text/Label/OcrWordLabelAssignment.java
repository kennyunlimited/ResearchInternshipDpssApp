package de.uni_passau.dpss.annotation.View.Text.Label;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import de.uni_passau.dpss.annotation.Model.Text.Label;
import de.uni_passau.dpss.annotation.Model.Text.Word;
import de.uni_passau.dpss.annotation.R;
import de.uni_passau.dpss.annotation.ViewModel.ViewModel;


/*
1. This class activity provides user with UI to select the
Label once text from image is collected using OCR
*/


public class OcrWordLabelAssignment extends AppCompatActivity {


    private ViewModel viewModel;
    private ArrayList label_names = new ArrayList();
    private TextView word_text_view;
    private AutoCompleteTextView label_autocomplete;
    private List<Label> all_labels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ocr_word_label_assignment);

        word_text_view = (TextView)findViewById(R.id.edit_text_word_ocr);
        label_autocomplete = (AutoCompleteTextView)findViewById(R.id.edit_text_label_ocr);
        ImageView label_dropdown = (ImageView)findViewById(R.id.drop_down_auto_complete_button);
        label_autocomplete.setThreshold(1);


        ActionBar actionbar = getSupportActionBar ();
        actionbar.setDisplayHomeAsUpEnabled ( true );
        actionbar.setHomeAsUpIndicator ( R.drawable.ic_close);

        setTitle("Choose a Label");
        Bundle bundle = getIntent().getExtras();
        String word_from_ocr = bundle.getString("word_from_ocr");
        word_text_view.setText(word_from_ocr);

        viewModel = ViewModelProviders.of(this).get(ViewModel.class);
        viewModel.getAllLabels().observe(this, new Observer<List<Label>>(){

            @Override
            public void onChanged(@Nullable List<Label> labels) {
                //update RecyclerView
                all_labels = labels;
                for(int i=0; i<labels.size(); i++){
                    label_names.add(labels.get(i).getLabel());


                }
            }
        });


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(OcrWordLabelAssignment.this,
                android.R.layout.simple_dropdown_item_1line, label_names);
        label_autocomplete.setAdapter(adapter);

        label_dropdown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                label_autocomplete.showDropDown();
            }
        });


    }

    private void saveWord(){
        String word = word_text_view.getText().toString().trim();
        String word_label = label_autocomplete.getText().toString().trim();

        if (word.isEmpty()||word_label.isEmpty()) {
            Toast.makeText(this, "Please enter a Word and Label", Toast.LENGTH_SHORT).show();
            return;
        }
        else if(!label_names.contains(word_label)){
            Toast.makeText(this, "Please select a existing Label", Toast.LENGTH_SHORT).show();
            return;
        }

        for(int i=0; i<all_labels.size(); i++){
            if (all_labels.get(i).getLabel().matches(word_label)){
                Label word_label_1 = new Label();
                word_label_1 = all_labels.get(i);
                int label_id = word_label_1.getLabel_id();

                Word newWord = new Word(word, label_id);
                viewModel.insert(newWord);

                Toast.makeText(this, "Word Saved", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(OcrWordLabelAssignment.this, LabelActivity.class);
                startActivity(intent);

            }
        }






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
                saveWord();
                return true;
            default:
                Toast.makeText(this, "Word not saved", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(OcrWordLabelAssignment.this, LabelActivity.class);
                startActivity(intent);
                return true;
        }
    }

}
