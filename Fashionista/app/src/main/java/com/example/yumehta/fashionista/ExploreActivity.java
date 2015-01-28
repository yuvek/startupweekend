package com.example.yumehta.fashionista;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static android.view.GestureDetector.OnGestureListener;


public class ExploreActivity extends ActionBarActivity{

    public int imageCount;

    public List<Bitmap> imageList;

    public ImageView imageView;

    private ArrayList<String> keyList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore);

        SharedPreferences prefs = getApplicationContext().getSharedPreferences("explore", Context.MODE_PRIVATE);
        Log.i("Prefs", prefs.getAll().size() + "");

        Map<String, String> map = (Map<String, String>) prefs.getAll();
        keyList = new ArrayList<>();
        imageList = new ArrayList<Bitmap>();
        for (Map.Entry<String, String> entry: map.entrySet()) {
            Bitmap tempMap = ServerManager.getS3Image(entry.getKey());
            if(tempMap != null){
                imageList.add(tempMap);
                keyList.add(entry.getValue()+":"+entry.getKey());
            }
        }
        if (imageList == null || imageList.isEmpty()) {
            //TODO default
        } else {
            imageCount = imageList.size();

            imageView = (ImageView)findViewById(R.id.exploreImgView);
            imageView.setImageBitmap(imageList.get(--imageCount));
            // Gesture detection
            gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                    //Log.i("UI", "onfling");
                    try {
                        if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
                            return false;
                        // right to left swipe
                        if(e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                            //Toast.makeText(ExploreActivity.this, "Left Swipe", Toast.LENGTH_SHORT).show();
                            ServerManager.LikePicture(keyList.get(imageCount),false);
                        }  else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                            //Toast.makeText(ExploreActivity.this, "Right Swipe", Toast.LENGTH_SHORT).show();
                            ServerManager.LikePicture(keyList.get(imageCount),true);
                        }
                        setNextImage();
                    } catch (Exception e) {
                        // nothing
                    }
                    return false;
                }

                @Override
                public boolean onDown(MotionEvent e) {
                    return true;
                }
            });
            gestureListener = new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    return gestureDetector.onTouchEvent(event);
                }
            };

            imageView.setOnTouchListener(gestureListener);
        }

    }

    public  void setNextImage(){
        if(imageCount > 0){
            imageView.setImageBitmap(imageList.get(--imageCount));
        }else{
            Intent intent = new Intent(this,MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_explore, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private static final int SWIPE_MIN_DISTANCE = 60;
    private static final int SWIPE_MAX_OFF_PATH = 250;
    private static final int SWIPE_THRESHOLD_VELOCITY = 50;
    private GestureDetector gestureDetector;
    View.OnTouchListener gestureListener;

    /*class MyGestureDetector extends GestureDetector.SimpleOnGestureListener {


        public MyGestureDetector(String sendingUser,String fileName){

        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            Log.i("UI", "onfling");
            try {
                if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
                    return false;
                // right to left swipe
                if(e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    Toast.makeText(ExploreActivity.this, "Left Swipe", Toast.LENGTH_SHORT).show();
                    ServerManager.
                }  else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    Toast.makeText(ExploreActivity.this, "Right Swipe", Toast.LENGTH_SHORT).show();
                }
                ExploreActivity.setNextImage();
            } catch (Exception e) {
                // nothing
            }
            return false;
        }

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }
    }*/
}
