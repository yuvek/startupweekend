package com.example.yumehta.fashionista;

import android.app.IntentService;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by yumehta on 1/24/2015.
 */
public class ExploreService extends IntentService {

    public ExploreService() {
        super("explore_service");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        while(true) {
            Map<String, String> images = ServerManager.GetImages();

            getLikeUpdates();

            SharedPreferences prefs = getApplicationContext().getSharedPreferences("explore", Context.MODE_PRIVATE);

            SharedPreferences.Editor editor = prefs.edit();
            editor.clear();
            editor.commit();
            if (images == null || images.isEmpty()) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                continue;
            }



            for(Map.Entry<String, String> entry: images.entrySet()) {
                editor.putString(entry.getKey(), entry.getValue());
            }

            editor.commit();

        }
    }

    private void getLikeUpdates() {
        final ArrayList<ImageItem> imageItems = new ArrayList<ImageItem>();
        // retrieve String drawable array

        SharedPreferences prefs = getApplicationContext().getSharedPreferences("browse", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = prefs.edit();

        editor.clear();
        editor.commit();

        Map<String, Integer> imageLikes = ServerManager.pollLikes();

        if (imageLikes == null || imageLikes.isEmpty()) {
            return;
        }

        for(Map.Entry<String, Integer> entry: imageLikes.entrySet()) {
            editor.putString(entry.getKey(), entry.getValue() + " Zigs");
        }

        editor.commit();

        Log.d("ARNIE", "in getLikeUpdates. Updated browse table");

//        String absPath = Environment.getExternalStorageDirectory().getAbsolutePath();
//        //TypedArray imgs = getResources().obtaiLnTypedArray(R.array.image_ids);
//        for (Map.Entry<String, Integer> entry: imageLikes.entrySet()) {
//            Bitmap bitmap = null;
//            try {
//                bitmap = BitmapFactory.decodeStream(new FileInputStream(new File(absPath + "/" + entry.getKey())));
//            } catch (Exception ex){
//                ex.printStackTrace();
//            }
//
//            imageItems.add(new ImageItem(bitmap, entry.getValue().toString()));
//        }

        return;

    }
}
