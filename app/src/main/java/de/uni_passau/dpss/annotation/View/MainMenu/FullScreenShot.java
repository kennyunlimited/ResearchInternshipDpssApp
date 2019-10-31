package de.uni_passau.dpss.annotation.View.MainMenu;

import android.app.Activity;
import android.content.Intent;
import android.media.projection.MediaProjectionManager;
import android.os.Bundle;
import android.util.Log;



/*
1. This class takes screenshot of whole screen
(foreground+background) using media Projection
manager.

*/

public class FullScreenShot extends Activity {

    private static final int REQUEST_SCREENSHOT=59706;
    MediaProjectionManager mediaProjectionManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mediaProjectionManager=(MediaProjectionManager)getSystemService(MEDIA_PROJECTION_SERVICE);
        startActivityForResult(mediaProjectionManager.createScreenCaptureIntent(), REQUEST_SCREENSHOT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode==REQUEST_SCREENSHOT) {
            if (resultCode==RESULT_OK) {
                Intent i=
                        new Intent(this, ScreenshotService.class)
                                .putExtra(ScreenshotService.EXTRA_RESULT_CODE, resultCode)
                                .putExtra(ScreenshotService.EXTRA_RESULT_INTENT, data);
                Log.e("HERE==>>:","Call Screenshot from service");

                startService(i);
            }
        }

        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        setVisible(true);
    }
}
