package de.uni_passau.dpss.annotation.View.MainMenu;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.util.Log;
import android.view.WindowManager;

import androidx.annotation.Nullable;

import de.uni_passau.dpss.annotation.View.Others.CropForOcr;


/*
1. This service class helps media projection manager.
ImageTransmogrifier class uses it to call for MediaProjectionManager session.

*/

public class ScreenshotService extends Service {

    static final String EXTRA_RESULT_CODE="resultCode";
    static final String EXTRA_RESULT_INTENT="resultIntent";

    byte[] newPng;

    MediaProjectionManager mediaProjectionManager;

    WindowManager wm;

    private MediaProjection projection;
    private VirtualDisplay vdisplay;

    static final int VIRT_DISPLAY_FLAGS=
            DisplayManager.VIRTUAL_DISPLAY_FLAG_OWN_CONTENT_ONLY |
                    DisplayManager.VIRTUAL_DISPLAY_FLAG_PUBLIC;

    final private HandlerThread handlerThread=
            new HandlerThread(getClass().getSimpleName(),
                    android.os.Process.THREAD_PRIORITY_BACKGROUND);

    private Handler handler;

    @Override
    public void onCreate() {
        super.onCreate();
        mediaProjectionManager = (MediaProjectionManager)getApplicationContext().getSystemService(MEDIA_PROJECTION_SERVICE);
        wm = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);

        handlerThread.start();
        handler=new Handler(handlerThread.getLooper());
    }

    @Override
    public int onStartCommand(Intent i, int flags, int startId) {

        Log.e("HERE==>>:","Start Screenshot from service");

        int resultCode=i.getIntExtra(EXTRA_RESULT_CODE, 1337);
        Intent data=i.getParcelableExtra(EXTRA_RESULT_INTENT);

        startCapture(resultCode, data);
        return(START_NOT_STICKY);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        throw new IllegalStateException("Binding not supported. Go away.");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopCapture();

        Log.e("===>","Starting crop");
        Intent intent = new Intent(getApplicationContext(), CropForOcr.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("byteArray", newPng);
        startActivity(intent);
        Log.e("===>","Crop Started");
    }

    public void processImage(final byte[] png){

        newPng = png;

        stopSelf();
    }

    private void startCapture(int resultCode, Intent resultData) {
        projection=mediaProjectionManager.getMediaProjection(resultCode, resultData);
        ImageTransmogrifier it=new ImageTransmogrifier(this);

        MediaProjection.Callback cb=new MediaProjection.Callback() {
            @Override
            public void onStop() {
                vdisplay.release();
            }
        };

        vdisplay=projection.createVirtualDisplay("andshooter",
                it.getWidth(), it.getHeight(),
                getResources().getDisplayMetrics().densityDpi,
                VIRT_DISPLAY_FLAGS, it.getSurface(), null, handler);
        projection.registerCallback(cb, handler);
    }

    private void stopCapture() {

        Log.e("===>","Stopping projection");
        if (projection!=null) {
            projection.stop();
            vdisplay.release();
            projection=null;
        }
        Log.e("===>","Projection Stopped");
    }

//    WindowManager getWindowManager() {
//        return(wm);
//    }

    Handler getHandler() {
        return(handler);
    }
}
