package de.uni_passau.dpss.annotation.View.Text.Word;

/*
1. This class activity provides user with UI to view
 all the Word(text) in database in Recycle view.
*/

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import de.uni_passau.dpss.annotation.Model.Text.Label;
import de.uni_passau.dpss.annotation.Model.Text.Word;
import de.uni_passau.dpss.annotation.View.Text.Label.LabelActivity;
import de.uni_passau.dpss.annotation.ViewModel.ViewModel;
import de.uni_passau.dpss.annotation.R;

public class WordActivity extends AppCompatActivity {
    public static final String EXTRA_LABEL =
            "de.uni_passau.dpss.annotation.EXTRA_LABEL";


    public static final int ADD_NOTE_REQUEST = 1;
    public static final int EDIT_NOTE_REQUEST = 2;


    private ViewModel viewModel;

    Label selectedLabel1 = new Label();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word);

        FloatingActionButton buttonAddNote = findViewById(R.id.button_add_note);
        buttonAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WordActivity.this, AddEditWordActivity.class);
                startActivityForResult(intent, ADD_NOTE_REQUEST);
            }
        });

        ActionBar actionbar = getSupportActionBar ();
        actionbar.setDisplayHomeAsUpEnabled ( true );
        actionbar.setHomeAsUpIndicator ( R.drawable.ic_back_arrow);

        Intent intent = getIntent();
        Label selectedLabel = intent.getParcelableExtra("Selected Label");
        setTitle(selectedLabel.getLabel());

        selectedLabel1.setLabel(selectedLabel.getLabel());
        selectedLabel1.setLabel_id(selectedLabel.getLabel_id());


        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        final WordAdapter adapter = new WordAdapter();
        recyclerView.setAdapter(adapter);


        viewModel = ViewModelProviders.of(this).get(ViewModel.class);

        viewModel.getLabelWords(selectedLabel).observe(this, new Observer<List<Word>>() {
            @Override
            public void onChanged(@Nullable List<Word> words) {
                //update RecyclerView
                adapter.setWords(words);
            }
        });



        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                viewModel.delete(adapter.getWordAt(viewHolder.getAdapterPosition()));
                Toast.makeText(WordActivity.this,"Word deleted", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(recyclerView);


        adapter.setOnItemClickListener(new WordAdapter.onItemClickListener() {
            @Override
            public void onItemClick(Word word) {
                Intent intent = new Intent(WordActivity.this, AddEditWordActivity.class);
                intent.putExtra(AddEditWordActivity.EXTRA_ID, word.getId());
                intent.putExtra(AddEditWordActivity.EXTRA_WORD, word.getWord());
                startActivityForResult(intent, EDIT_NOTE_REQUEST);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == ADD_NOTE_REQUEST && resultCode == RESULT_OK){
            String word = data.getStringExtra(AddEditWordActivity.EXTRA_WORD);
            int label_id = selectedLabel1.getLabel_id();

            Word new_word = new Word(word, label_id);
            viewModel.insert(new_word);

            Toast.makeText(this, "Word saved", Toast.LENGTH_SHORT).show();
        }else if(requestCode == EDIT_NOTE_REQUEST && resultCode == RESULT_OK) {
            int id = data.getIntExtra(AddEditWordActivity.EXTRA_ID, -1);

            if (id == -1){
                Toast.makeText(this, "Word can't be updated", Toast.LENGTH_SHORT).show();
                return;
            }

            String word = data.getStringExtra(AddEditWordActivity.EXTRA_WORD);
            int label_id = selectedLabel1.getLabel_id();

            Word edit_word = new Word(word, label_id);
            edit_word.setId(id);
            viewModel.update(edit_word);

            Toast.makeText(this, "Word updated", Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(this, "Word not saved", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.delete_all_words:
                viewModel.deleteLabelWords(selectedLabel1);
                Toast.makeText(this, "All words deleted", Toast.LENGTH_SHORT).show();
                return true;

            default:
                Intent intent = new Intent(WordActivity.this, LabelActivity.class);
                startActivity(intent);
                return true;
//                return super.onOptionsItemSelected(item);
        }
    }
}
