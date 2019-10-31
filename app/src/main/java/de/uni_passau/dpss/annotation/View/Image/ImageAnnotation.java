package de.uni_passau.dpss.annotation.View.Image;


/*
1. This Activity will let user select the source of
image to be annotated. Once choosen, it will redirect to crop
screen.

2. This activity will help to store the image at local storage.

3. It will also recore the crop points, image size, image name.

4. "com.theartofdev.edmodo.cropper.CropImageActivity" library is
used for crop activity and image source selection.

5. Reference: https://gist.github.com/yccheok/405726623df261dfd648bbd941b0995c
*/


import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.system.ErrnoException;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import de.uni_passau.dpss.annotation.R;

public class ImageAnnotation extends AppCompatActivity {

    private CropImageView mCropImageView;

    private Uri mCropImageUri;


    String[] img_location = new String[2];
    int width;
    int height;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_annotation);
        mCropImageView = (CropImageView) findViewById(R.id.CropImageView);
    }

    /**
     * On load image button click, start pick image chooser activity.
     */
    public void onLoadImageClick(View view) {
        startActivityForResult(getPickImageChooserIntent(), 200);
    }

    /**
     * Crop the image and set it back to the cropping view.
     */
    public void onCropImageClick(View view) {
        Bitmap cropped = mCropImageView.getCroppedImage(500, 500);
        float[] cropPoints = mCropImageView.getCropPoints();

        String image_name = img_location[0];
        String image_location = img_location[1];
        int image_width = width;
        int image_height = height;
        int crop_x_cordinate = (int)cropPoints[0];
        int crop_y_cordinate = (int)cropPoints[1];
        int crop_width = (int)cropPoints[2];
        int crop_height = (int)cropPoints[5];
        GetLabel(image_name, image_location, image_width, image_height, crop_x_cordinate, crop_y_cordinate, crop_width, crop_height);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            Uri imageUri = getPickImageResultUri(data);
            img_location = saveBitmapIntoSDCardImage(this, getContactBitmapFromURI(this, imageUri));


            try {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;

                BitmapFactory.decodeStream(
                        getApplicationContext().getContentResolver().openInputStream(imageUri),
                        null,
                        options);
                width = options.outWidth;
                height = options.outHeight;
            }catch (Exception e){}


            // For API >= 23 we need to check specifically that we have permissions to read external storage,
            // but we don't know if we need to for the URI so the simplest is to try open the stream and see if we get error.
            boolean requirePermissions = false;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                    checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                    isUriRequiresPermissions(imageUri)) {

                // request permissions and handle the result in onRequestPermissionsResult()
                requirePermissions = true;
                mCropImageUri = imageUri;
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
            }

            if (!requirePermissions) {
                mCropImageView.setImageUriAsync(imageUri);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (mCropImageUri != null && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            mCropImageView.setImageUriAsync(mCropImageUri);
        } else {
            Toast.makeText(this, "Required permissions are not granted", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Create a chooser intent to select the source to get image from.<br/>
     * The source can be camera's (ACTION_IMAGE_CAPTURE) or gallery's (ACTION_GET_CONTENT).<br/>
     * All possible sources are added to the intent chooser.
     */
    public Intent getPickImageChooserIntent() {

        // Determine Uri of camera image to save.
        Uri outputFileUri = getCaptureImageOutputUri();

        List<Intent> allIntents = new ArrayList<>();
        PackageManager packageManager = getPackageManager();

        // collect all camera intents
        Intent captureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);
        for (ResolveInfo res : listCam) {
            Intent intent = new Intent(captureIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(res.activityInfo.packageName);
            if (outputFileUri != null) {
                intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            }
            allIntents.add(intent);
        }

        // collect all gallery intents
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        List<ResolveInfo> listGallery = packageManager.queryIntentActivities(galleryIntent, 0);
        for (ResolveInfo res : listGallery) {
            Intent intent = new Intent(galleryIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(res.activityInfo.packageName);
            allIntents.add(intent);
        }

        // the main intent is the last in the list so pickup the useless one
        Intent mainIntent = allIntents.get(allIntents.size() - 1);
        for (Intent intent : allIntents) {
            if (intent.getComponent().getClassName().equals("com.android.documentsui.DocumentsActivity")) {
                mainIntent = intent;
                break;
            }
        }
        allIntents.remove(mainIntent);

        // Create a chooser from the main intent
        Intent chooserIntent = Intent.createChooser(mainIntent, "Select source");

        // Add all other intents
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, allIntents.toArray(new Parcelable[allIntents.size()]));

        return chooserIntent;
    }

    /**
     * Get URI to image received from capture by camera.
     */
    private Uri getCaptureImageOutputUri() {
        Uri outputFileUri = null;
        File getImage = getExternalCacheDir();
        if (getImage != null) {
            outputFileUri = Uri.fromFile(new File(getImage.getPath(), "pickImageResult.jpeg"));
        }
        return outputFileUri;
    }

    /**
     * Get the URI of the selected image from {@link #getPickImageChooserIntent()}.<br/>
     * Will return the correct URI for camera and gallery image.
     *
     * @param data the returned data of the activity result
     */
    public Uri getPickImageResultUri(Intent data) {
        boolean isCamera = true;
        if (data != null && data.getData() != null) {
            String action = data.getAction();
            isCamera = action != null && action.equals(MediaStore.ACTION_IMAGE_CAPTURE);
        }
        return isCamera ? getCaptureImageOutputUri() : data.getData();
    }

    /**
     * Test if we can open the given Android URI to test if permission required error is thrown.<br>
     */
    public boolean isUriRequiresPermissions(Uri uri) {
        try {
            ContentResolver resolver = getContentResolver();
            InputStream stream = resolver.openInputStream(uri);
            stream.close();
            return false;
        } catch (FileNotFoundException e) {
            if (e.getCause() instanceof ErrnoException) {
                return true;
            }
        } catch (Exception e) {
        }
        return false;
    }





    public  Bitmap getContactBitmapFromURI(Context context, Uri uri) {
        try {

            InputStream input = context.getContentResolver().openInputStream(uri);
            if (input == null) {
                return null;
            }
            return BitmapFactory.decodeStream(input);
        }
        catch (FileNotFoundException e)
        {

        }
        return null;

    }

    public  String[] saveBitmapIntoSDCardImage(Context context, Bitmap finalBitmap) {

        String[] file_details = new String[2];

        String root = Environment.getExternalStorageDirectory().toString();

        File path = context.getExternalFilesDir("AnnotateImage");
        if(!path.isDirectory()){
            path.mkdirs();
        }


        String fname = System.currentTimeMillis() + ".jpg";
        File file = new File (path, fname);

        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();

            file_details[0] = fname;
            file_details[1] = file.getParent();

        } catch (Exception e) {
            e.printStackTrace();
        }



        return file_details;
    }

    public String GetLabel(final String image_name, final String image_location, final int image_width, final int image_height, final int crop_x_cordinate, final int crop_y_cordinate, final int crop_width, final int crop_height){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(ImageAnnotation.this);
        alertDialog.setTitle("Image Label");
        alertDialog.setMessage("Enter object name");


        final EditText input = new EditText(ImageAnnotation.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        alertDialog.setView(input);

        alertDialog.setPositiveButton("Next",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String crop_label = input.getText().toString();
                        if (!crop_label.isEmpty()) {

                            Intent myIntent = new Intent(ImageAnnotation.this, AnnotationConfirmation.class);

                            myIntent.putExtra("IMAGE_NAME", image_name);
                            myIntent.putExtra("IMAGE_LOCATION", image_location);
                            myIntent.putExtra("IMAGE_WIDTH", image_width);
                            myIntent.putExtra("IMAGE_HEIGHT", image_height);
                            myIntent.putExtra("CROP_X_CORDINATE", crop_x_cordinate);
                            myIntent.putExtra("CROP_Y_CORDINATE", crop_y_cordinate);
                            myIntent.putExtra("CROP_WIDTH", crop_width);
                            myIntent.putExtra("CROP_HEIGHT", crop_height);
                            myIntent.putExtra("CROP_LABEL", crop_label);

                            startActivity(myIntent);


                        } else {
                            Toast.makeText(getApplicationContext(),
                                    "Label not entered", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        alertDialog.setNegativeButton("NO",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        alertDialog.show();

        return null;
    }



}
