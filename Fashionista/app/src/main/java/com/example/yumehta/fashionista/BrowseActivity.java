package com.example.yumehta.fashionista;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Map;


public class BrowseActivity extends ActionBarActivity {

    private GridView gridView;
    private GridViewAdapter gridViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse);

        Bundle bundle = getIntent().getExtras();
        //String fileName = bundle.getString("FileName");

        gridView = (GridView) findViewById(R.id.gridView);
        gridViewAdapter = new GridViewAdapter(this, R.layout.row_grid, getData());
        gridView.setAdapter(gridViewAdapter);
//        ImageView imageView = (ImageView) findViewById(R.id.imageView);
//        Bitmap myBitmap = BitmapFactory.decodeFile(fileName);
//        imageView.setImageBitmap(myBitmap);
//
//        Map<String, Integer> imageLikes = ServerManager.pollLikes();
//        String[] splitStr = fileName.split("/");
//        Integer likes = imageLikes.get(splitStr[splitStr.length - 1]);
//        if (likes != null) {
//            TextView textView = (TextView) findViewById(R.id.likeCount);
//            textView.setText(likes.toString());
//        }


    }

    private ArrayList getData() {

        ArrayList<ImageItem> imageItems = new ArrayList<ImageItem>();

        SharedPreferences prefs = getApplicationContext().getSharedPreferences("browse", Context.MODE_PRIVATE);

        if (prefs == null || prefs.getAll().isEmpty()) {
            Toast.makeText(this, "No images of yours to show", Toast.LENGTH_LONG).show();
            return imageItems;
        }

        Log.w("Prefs in getdata", prefs.getAll().size() + "");

        Map<String, String> map = (Map<String, String>) prefs.getAll();

        String absPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        //TypedArray imgs = getResources().obtaiLnTypedArray(R.array.image_ids);
        for (Map.Entry<String, String> entry: map.entrySet()) {
            //Bitmap bitmap = null;
            //try {
            //bitmap = BitmapFactory.decodeStream(new FileInputStream(new File(absPath + "/" + entry.getKey())));
            //} catch (Exception ex){
            //    ex.printStackTrace();
            //}

            imageItems.add(new ImageItem(absPath + "/" + entry.getKey(), entry.getValue()));
        }

        return imageItems;

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_browse, menu);
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

    class GridViewAdapter extends ArrayAdapter {
        private Context context;
        private int layoutResourceId;
        private ArrayList<ImageItem> data = new ArrayList<ImageItem>();

        public GridViewAdapter(Context context, int layoutResourceId,
                               ArrayList data) {
            super(context, layoutResourceId, data);
            this.layoutResourceId = layoutResourceId;
            this.context = context;
            this.data = data;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View row = convertView;
            ViewHolder holder = null;

            Log.w("PEREIRA","called getView in GridViewAdapter");

            if (row == null) {
                LayoutInflater inflater = ((Activity) context).getLayoutInflater();
                row = inflater.inflate(layoutResourceId, parent, false);
                holder = new ViewHolder();
                holder.imageTitle = (TextView) row.findViewById(R.id.text);
                holder.image = (ImageView) row.findViewById(R.id.image);
                row.setTag(holder);
            } else {
                holder = (ViewHolder) row.getTag();
            }

            ImageItem item = data.get(position);
            holder.imageTitle.setText(item.getTitle());
            Bitmap bitmap = null;
            try {
                bitmap = BitmapFactory.decodeStream(new FileInputStream(new File(data.get(position).getFilePath())));
            } catch (Exception ex){
                ex.printStackTrace();
            }

            holder.image.setImageBitmap(bitmap);
            bitmap = null;
            return row;
        }

        private class ViewHolder {
            TextView imageTitle;
            ImageView image;
        }
    }
}

