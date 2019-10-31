package de.uni_passau.dpss.annotation.View.Others;


/*
1. This class activity provides user with UI to convert
text of Image into String via OCR

2. It uses "com.theartofdev.edmodo.cropper.CropImageActivity"
library for crop activity.
*/


import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.SparseArray;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import de.uni_passau.dpss.annotation.R;
import de.uni_passau.dpss.annotation.View.Text.Label.OcrWordLabelAssignment;


public class OcrActivity extends AppCompatActivity {

    ImageView imageView;
    EditText editText;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ocr);

        final ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled ( true );
        actionbar.setHomeAsUpIndicator ( R.drawable.ic_close);


        final FloatingActionButton buttonAddLabel = findViewById(R.id.button_add_label);
        buttonAddLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(OcrActivity.this,"Choose Label", Toast.LENGTH_SHORT).show();
                String word_from_ocr = editText.getText().toString().trim();
                Intent intent = new Intent(OcrActivity.this, OcrWordLabelAssignment.class);
                intent.putExtra("word_from_ocr", word_from_ocr);
                startActivity(intent);

            }
        });

        imageView = (ImageView)findViewById(R.id.ImageToConvert);
        editText = (EditText)findViewById(R.id.convertedText);
        button = (Button)findViewById(R.id.btn_convertImage2Text);

        Intent intent = getIntent();
        String image_path= intent.getStringExtra("imagePath");
        final Uri fileUri = Uri.parse(image_path);
        imageView.setImageURI(fileUri);



        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getTextFromImage(view, fileUri);

                String ocr_text = editText.getText().toString().trim();
                if (ocr_text.isEmpty()){
                    Toast.makeText(OcrActivity.this,"Unable to receive the text", Toast.LENGTH_SHORT).show();
                }
                else {
                    buttonAddLabel.show();
                }
            }
        });
    }

    public void getTextFromImage(View view, Uri uri){
//        Bitmap bitmap = BitmapFactory.decodeResource(getApplicationContext().getResources(),R.drawable.screenshot);

        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);

            TextRecognizer textRecognizer = new TextRecognizer.Builder(getApplicationContext()).build();

            if(!textRecognizer.isOperational()){
                Toast.makeText(getApplicationContext(),"Could not get the Text", Toast.LENGTH_SHORT).show();
            }
            else {
                Frame frame = new Frame.Builder().setBitmap(bitmap).build();
                SparseArray<TextBlock> items = textRecognizer.detect(frame);
                StringBuilder sb = new StringBuilder();

                for(int i=0; i<items.size(); ++i){
                    TextBlock myItem = items.valueAt(i);
                    sb.append(myItem.getValue());
                    sb.append("\n");
                }
                editText.setText(sb.toString());

            }
        }catch (Exception e){

        };

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            default:
                finish();
                return super.onOptionsItemSelected(item);
        }
    }


}
