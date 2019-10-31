package de.uni_passau.dpss.annotation.View.MainMenu;


/*
1. This service class creates overlay menu
which can be present on the screen until
destroyed.

2. It is the main menu and contains buttons
linked to different functionality.
*/


import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;


import de.uni_passau.dpss.annotation.R;
import de.uni_passau.dpss.annotation.View.Image.ExportCsv;
import de.uni_passau.dpss.annotation.View.Image.ImageAnnotation;

import de.uni_passau.dpss.annotation.View.Text.Label.LabelActivity;

public class FloatingViewService extends Service implements View.OnClickListener {


    private WindowManager mWindowManager;
    private View mFloatingView;
    private View collapsedView;
    private View expandedView;
    private View collapsedView1;
    private View expandedView1;
    private View collapsedView2;
    private View expandedView2;

    private ImageButton camera;
    private ImageButton text1;

    private Button openCamera;
    private Button openGallery;
    private Button ocr;
    private Button ViewClass;

    public FloatingViewService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();



        //getting the widget layout from xml using layout inflater
        mFloatingView = LayoutInflater.from(this).inflate(R.layout.layout_floating_widget, null);

        // For supporting latest android versions (type_phone not supported in latest)
        final WindowManager.LayoutParams params;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            params = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    PixelFormat.TRANSLUCENT);
        } else {
            params = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.TYPE_PHONE,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    PixelFormat.TRANSLUCENT);
        }




        //getting windows services and adding the floating view to it
        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        mWindowManager.addView(mFloatingView, params);

        camera = (ImageButton) mFloatingView.findViewById(R.id.play_btnn);
        text1 = (ImageButton) mFloatingView.findViewById(R.id.play_btno);

        openCamera =(Button) mFloatingView.findViewById(R.id.play_btno1);
        openGallery = (Button) mFloatingView.findViewById(R.id.play_btno2);
        ocr = (Button) mFloatingView.findViewById(R.id.play_btno3);

        ViewClass = (Button)mFloatingView.findViewById(R.id.play_btno5);

        //getting the collapsed and expanded view from the floating view
        collapsedView = mFloatingView.findViewById(R.id.layoutCollapsed);
        expandedView = mFloatingView.findViewById(R.id.layoutExpanded);

        expandedView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                collapsedView.setVisibility(View.VISIBLE);
                expandedView.setVisibility(View.GONE);
            }
        });

        //getting the collapsed1 and expanded1 view from the floating view
        collapsedView1 = expandedView.findViewById(R.id.layoutCollapsed1);
        expandedView1 = expandedView.findViewById(R.id.layoutExpanded1);

        //getting the collapsed2 and expanded2 view from the floating view
        collapsedView2 = expandedView.findViewById(R.id.layoutCollapsed2);
        expandedView2 = expandedView.findViewById(R.id.layoutExpanded2);

        //adding click listener to close button and expanded view
        mFloatingView.findViewById(R.id.buttonClose).setOnClickListener(this);
        expandedView.setOnClickListener(this);

        //adding an touchlistener to make drag movement of the floating widget
        mFloatingView.findViewById(R.id.relativeLayoutParent).setOnTouchListener(new View.OnTouchListener() {
            private int initialX;
            private int initialY;
            private float initialTouchX;
            private float initialTouchY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        initialX = params.x;
                        initialY = params.y;
                        initialTouchX = event.getRawX();
                        initialTouchY = event.getRawY();
                        return true;

                    case MotionEvent.ACTION_UP:
                        //when the drag is ended switching the state of the widget
                        collapsedView.setVisibility(View.GONE);
                        expandedView.setVisibility(View.VISIBLE);
                        return true;

                    case MotionEvent.ACTION_MOVE:
                        //this code is helping the widget to move around the screen with fingers
                        params.x = initialX + (int) (event.getRawX() - initialTouchX);
                        params.y = initialY + (int) (event.getRawY() - initialTouchY);
                        mWindowManager.updateViewLayout(mFloatingView, params);
                        return true;
                }
                return false;
            }
        });

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                collapsedView1.setVisibility(View.GONE);
                expandedView1.setVisibility(View.VISIBLE);
                expandedView2.setVisibility(View.GONE);
                collapsedView2.setVisibility(View.VISIBLE);
                Toast.makeText(FloatingViewService.this, "Image annotation", Toast.LENGTH_SHORT).show();
            }
        });

        text1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                collapsedView2.setVisibility(View.GONE);
                expandedView2.setVisibility(View.VISIBLE);
                expandedView1.setVisibility(View.GONE);
                collapsedView1.setVisibility(View.VISIBLE);
                Toast.makeText(FloatingViewService.this, "Text annotation", Toast.LENGTH_SHORT).show();
            }
        });

        openCamera.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view){
                collapsedView.setVisibility(View.VISIBLE);
                expandedView.setVisibility(View.GONE);
                Toast.makeText(FloatingViewService.this, "Choose Source", Toast.LENGTH_SHORT).show();
                Intent mintent = new Intent(FloatingViewService.this, ImageAnnotation.class);
                startActivity(mintent);

            }
        });
        openGallery.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view){
                Toast.makeText(FloatingViewService.this, "Export records", Toast.LENGTH_SHORT).show();
                Intent mintent = new Intent(FloatingViewService.this, ExportCsv.class);
                startActivity(mintent);

            }
        });



        ocr.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view){
                collapsedView.setVisibility(View.VISIBLE);
                expandedView.setVisibility(View.GONE);
                Toast.makeText(FloatingViewService.this, "Crop Text", Toast.LENGTH_SHORT).show();

                Intent mainIntent = new Intent(getApplicationContext(), FullScreenShot.class);
                mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(mainIntent);




            }
        });

        ViewClass.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view){
                collapsedView.setVisibility(View.VISIBLE);
                expandedView.setVisibility(View.GONE);
                Toast.makeText(FloatingViewService.this, "Word Labels", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(FloatingViewService.this, LabelActivity.class);
                startActivity(intent);
            }
        });

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mFloatingView != null) mWindowManager.removeView(mFloatingView);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layoutExpanded:
                //switching views
                collapsedView.setVisibility(View.VISIBLE);
                expandedView.setVisibility(View.GONE);
                switch (v.getId()){
                    case R.id.layoutExpanded1:
                        collapsedView1.setVisibility(View.VISIBLE);
                        expandedView1.setVisibility(View.GONE);

                        break;
                    case R.id.layoutExpanded2:

                        break;
                }
                break;

            case R.id.buttonClose:
                //closing the widget
                stopSelf();
                break;
        }
    }
}