package de.uni_passau.dpss.annotation.View.Image;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import de.uni_passau.dpss.annotation.Model.Image.ImageObject;
import de.uni_passau.dpss.annotation.R;
import de.uni_passau.dpss.annotation.ViewModel.ViewModel;

/*
1. This Activity will display confirmation message
upon successful completion of Image Annotation flow.

2. It will also display the details of the row added
in the image-table.
*/

public class AnnotationConfirmation extends AppCompatActivity {

    private ViewModel viewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_annotation_confirmation);

        ActionBar actionbar = getSupportActionBar ();
        actionbar.setDisplayHomeAsUpEnabled ( true );
        actionbar.setHomeAsUpIndicator ( R.drawable.ic_close);

        Intent mIntent = getIntent();
        String image_name = mIntent.getExtras().getString("IMAGE_NAME");
        String image_location = mIntent.getExtras().getString("IMAGE_LOCATION");
        int image_width = mIntent.getIntExtra("IMAGE_WIDTH", 0);
        int image_height = mIntent.getIntExtra("IMAGE_HEIGHT", 0);
        int crop_x_cordinate = mIntent.getIntExtra("CROP_X_CORDINATE", 0);
        int crop_y_cordinate = mIntent.getIntExtra("CROP_Y_CORDINATE", 0);
        int crop_width = mIntent.getIntExtra("CROP_WIDTH", 0);
        int crop_height = mIntent.getIntExtra("CROP_HEIGHT", 0);
        String obj_label = mIntent.getExtras().getString("CROP_LABEL");


        ImageObject imageObject = new ImageObject(image_name, image_location, image_width, image_height, obj_label, crop_x_cordinate, crop_y_cordinate, crop_width, crop_height);
        viewModel = ViewModelProviders.of(this).get(ViewModel.class);
        viewModel.insert(imageObject);

        TextView title = findViewById(R.id.Title);
        TextView img_name = findViewById(R.id.img_name);
        TextView img_width = findViewById(R.id.img_width);
        TextView img_height = findViewById(R.id.img_height);
        TextView img_label = findViewById(R.id.img_label);
        TextView crop_x_cord = findViewById(R.id.crop_x_cord);
        TextView crop_y_cord = findViewById(R.id.crop_y_cord);
        TextView crp_height = findViewById(R.id.crop_height);
        TextView crp_width = findViewById(R.id.crop_width);
        TextView img_location = findViewById(R.id.img_location);

        img_name.setText("Image: "+image_name);
        img_width.setText("Image Width : "+image_width);
        img_height.setText("Image Height: "+image_height);
        img_label.setText("Image Label: "+obj_label);
        crop_x_cord.setText("Object x cordinate: "+crop_x_cordinate);
        crop_y_cord.setText("Object y cordinate: "+crop_y_cordinate);
        crp_height.setText("Object height: "+crop_height);
        crp_width.setText("Object width: "+crop_width);
        img_location.setText("Image location: "+image_location);


    }


}
