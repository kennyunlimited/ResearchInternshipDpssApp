package de.uni_passau.dpss.annotation.View.Image;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProviders;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.util.List;

import de.uni_passau.dpss.annotation.Model.Image.ImageObject;
import de.uni_passau.dpss.annotation.R;
import de.uni_passau.dpss.annotation.ViewModel.ViewModel;

public class ExportCsv extends AppCompatActivity {


/*
1. This Activity will export all the records of
image-table into a csv file with time-stamp in name.

2. It will be exported locally.

3. The location of the stored csv will be
shown in the toast.

4. The success or failure will also be
shown in the toast.

5. It will write the records in batches. 50 records are
written in each I/O operation.
*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_export_csv);

        ActionBar actionbar = getSupportActionBar ();
        actionbar.setDisplayHomeAsUpEnabled ( true );
        actionbar.setHomeAsUpIndicator ( R.drawable.ic_close);

        ActivityCompat.requestPermissions(ExportCsv.this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);




    }







    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission granted and now can proceed
                    CsvUpdate();

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(ExportCsv.this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
                }
                return;
            }
            // add other cases for more permissions
        }
    }



    public void CsvUpdate(){


        ViewModel viewModel = ViewModelProviders.of(this).get(ViewModel.class);

        int TOTAL_RECORD = viewModel.getImageObjectRecordSize();
        int offset = 0;
        while (offset < TOTAL_RECORD){
            List<ImageObject> nImageObject = viewModel.getNImageObjectRecord(offset);
            writeCsv(nImageObject);
            offset += 50;

        }

    }

    public void writeCsv(List<ImageObject> nImageObejectRecord) {


        String baseDir = android.os.Environment.getExternalStorageDirectory().getAbsolutePath();
        String fileName = System.currentTimeMillis() + "_ImageData.csv";
        String filePath = baseDir + File.separator + fileName;
        File f = new File(filePath);
        CSVWriter writer;
        try {
            // File exist
            if (f.exists() && !f.isDirectory()) {
                FileWriter mFileWriter = new FileWriter(filePath, true);
                writer = new CSVWriter(mFileWriter);
            } else {
                writer = new CSVWriter(new FileWriter(filePath));
                String[] heading = {"id","image_name","image_location","image_width","image_height",
                                    "obj_label","crop_x_cordinate","crop_y_cordinate","crop_width",
                                    "crop_height"};
                writer.writeNext(heading);
            }

            for (int i=0; i<nImageObejectRecord.size(); i++) {

                int id = nImageObejectRecord.get(i).getId();
                String image_name = nImageObejectRecord.get(i).getImage_name();
                String image_location = nImageObejectRecord.get(i).getImage_location();
                int image_width = nImageObejectRecord.get(i).getImage_width();
                int image_height = nImageObejectRecord.get(i).getImage_height();
                String obj_label = nImageObejectRecord.get(i).getObj_label();
                int crop_x_cordinate = nImageObejectRecord.get(i).getCrop_x_cordinate();
                int crop_y_cordinate = nImageObejectRecord.get(i).getCrop_y_cordinate();
                int crop_width = nImageObejectRecord.get(i).getCrop_width();
                int crop_height = nImageObejectRecord.get(i).getCrop_height();

                String[] data = {String.valueOf(id), image_name, image_location, String.valueOf(image_width),
                                    String.valueOf(image_height), obj_label, String.valueOf(crop_x_cordinate),
                                    String.valueOf(crop_y_cordinate), String.valueOf(crop_width),
                                    String.valueOf(crop_height)};
                writer.writeNext(data);
            }

            writer.close();
            Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();
            Toast.makeText(this, "Exported at"+baseDir+fileName, Toast.LENGTH_SHORT).show();


            this.finish();





        } catch (Exception e) {
            Toast.makeText(this, "CSV can't be accessed", Toast.LENGTH_SHORT).show();
        }
        ;
    }
}
