package de.uni_passau.dpss.annotation.View.Text.Label;

/*
1. This class activity provides user with UI to view
 all the Label(text) in database in Recycle view.
*/


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import de.uni_passau.dpss.annotation.Model.Text.Label;
import de.uni_passau.dpss.annotation.R;
import de.uni_passau.dpss.annotation.View.Text.Word.WordActivity;
import de.uni_passau.dpss.annotation.ViewModel.ViewModel;

public class LabelActivity extends AppCompatActivity {
    public static final int ADD_LABEL_REQUEST = 1;
    public static final int VIEW_LABEL_REQUEST = 2;
    public static final int EDIT_LABEL_REQUEST = 3;


    private ViewModel viewModel;
    private LabelAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_label);


        RecyclerView recyclerView = findViewById(R.id.recycler_view_text_label);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        ActionBar actionbar = getSupportActionBar ();
        actionbar.setDisplayHomeAsUpEnabled ( true );
        actionbar.setHomeAsUpIndicator ( R.drawable.ic_close);


        adapter = new LabelAdapter();
        recyclerView.setAdapter(adapter);

        viewModel = ViewModelProviders.of(this).get(ViewModel.class);
        viewModel.getAllLabels().observe(this, new Observer<List<Label>>() {
            @Override
            public void onChanged(@Nullable List<Label> labels) {
                //update RecyclerView
                adapter.setlabels(labels);
            }
        });


        FloatingActionButton buttonAddLabel = findViewById(R.id.button_add_label);
        buttonAddLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LabelActivity.this, AddEditLabelActivity.class);
                startActivityForResult(intent, ADD_LABEL_REQUEST);
            }
        });


        adapter.setOnItemClickListener(new LabelAdapter.onItemClickListener() {
            @Override
            public void onItemClick(Label label) {
                Intent intent = new Intent(LabelActivity.this, WordActivity.class);
                intent.putExtra("Selected Label", label);
                startActivityForResult(intent, VIEW_LABEL_REQUEST);

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
                viewModel.delete(adapter.getLabelAt(viewHolder.getAdapterPosition()));
                Toast.makeText(LabelActivity.this,"Label deleted", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(recyclerView);


        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                Intent intent = new Intent(LabelActivity.this, AddEditLabelActivity.class);
                intent.putExtra(AddEditLabelActivity.EXTRA_LABEL, adapter.getLabelAt(viewHolder.getAdapterPosition()).getLabel());
                intent.putExtra(AddEditLabelActivity.EXTRA_LABEL_ID, adapter.getLabelAt(viewHolder.getAdapterPosition()).getLabel_id());

                startActivityForResult(intent, EDIT_LABEL_REQUEST);
            }
        }).attachToRecyclerView(recyclerView);


    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == ADD_LABEL_REQUEST && resultCode == RESULT_OK){
            String label_name = data.getStringExtra(AddEditLabelActivity.EXTRA_LABEL);

            Label new_label = new Label(label_name);
            viewModel.insert(new_label);

            Toast.makeText(this, "Label saved", Toast.LENGTH_SHORT).show();
        }else if(requestCode == EDIT_LABEL_REQUEST && resultCode == RESULT_OK) {
            int label_id = data.getIntExtra(AddEditLabelActivity.EXTRA_LABEL_ID, -1);

            if (label_id == -1){
                Toast.makeText(this, "Label can't be updated", Toast.LENGTH_SHORT).show();
                return;
            }

            String label_name = data.getStringExtra(AddEditLabelActivity.EXTRA_LABEL);

            Label edit_label = new Label(label_name);
            edit_label.setLabel_id(label_id);
            viewModel.update(edit_label);

            Toast.makeText(this, "Label updated", Toast.LENGTH_SHORT).show();

        } else {
            adapter.cancelModification();
            Toast.makeText(this, "Label not saved", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.label_menu,menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.delete_all_labels:
                viewModel.deleteAllLabels();
                Toast.makeText(this, "All Labels deleted", Toast.LENGTH_SHORT).show();
                return true;
            default:
                finish();
                return super.onOptionsItemSelected(item);

        }
    }

}
