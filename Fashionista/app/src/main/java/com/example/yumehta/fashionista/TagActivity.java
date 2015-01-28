package com.example.yumehta.fashionista;

import android.content.Intent;
import android.os.Environment;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.PutObjectRequest;

import java.io.File;


public class TagActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tag, menu);
        return true;
    }

    public void tagClick(View view) {

       // Bundle bundle = getIntent().getExtras();
       // String fileName = bundle.getString("FileName");

       // Button tagButton = (Button)view;
        //Toast.makeText(this, "Button clicked is"+ tagButton.getText().toString(), Toast.LENGTH_LONG).show();
        //Intent intent = new Intent(this,FriendsActivity.class);
        //intent.putExtra("Tag",tagButton.getText().toString());
        //intent.putExtra("FileName",fileName );
        //startActivity(intent);
        //Button tagButton = (Button)view;
        //Toast.makeText(this, "Button clicked is"+ tagButton.getText().toString(), Toast.LENGTH_LONG).show();
    }

    public void sendClick(View view) {
        Bundle bundle = getIntent().getExtras();
        String fileName = bundle.getString("FileName");

        Button tagButton = (Button)view;
        //Toast.makeText(this, "Button clicked is"+ tagButton.getText().toString(), Toast.LENGTH_LONG).show();
        if(tagButton.getText().toString().equals("Send Private")){
            Intent intent = new Intent(this,FriendsActivity.class);
            intent.putExtra("Tag",tagButton.getText().toString());
            intent.putExtra("FileName",fileName );
            startActivity(intent);
        }else{

            Toast.makeText(this, "Your zig is out there !", Toast.LENGTH_LONG).show();

            String mediaStorageDir = Environment.getExternalStorageDirectory().getAbsolutePath();
            String uploadFileName = mediaStorageDir + File.separator +
                    fileName;

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            //Upload the file to s3
            File file = new File(uploadFileName);
            AmazonS3Client s3Client = new AmazonS3Client(new BasicAWSCredentials("AKIAIHWZDTVAZQZAU4VA", "FsM4tXWasxFpGrz0D9ABUc7fekDShiRGEAqEJYmb"));
            PutObjectRequest por = new PutObjectRequest("fashionistapics" , file.getName(), file );
            s3Client.putObject( por );

            //Send request to server
            //sendRequest();
            ServerManager.sendRequestToServer(fileName,"Arnold,Vignesh");

            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
        }
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
}
