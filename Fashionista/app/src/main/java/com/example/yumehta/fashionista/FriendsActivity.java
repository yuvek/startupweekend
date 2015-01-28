package com.example.yumehta.fashionista;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.PutObjectRequest;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

public class FriendsActivity extends ActionBarActivity {

    //LIST OF ARRAY STRINGS WHICH WILL SERVE AS LIST ITEMS
    ArrayList<String> listItems=new ArrayList<String>();

    //DEFINING A STRING ADAPTER WHICH WILL HANDLE THE DATA OF THE LISTVIEW
    ArrayAdapter<String> adapter;

    String fileName ;
    MyCustomAdapter dataAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        //Reading the tag from previous activity
        Bundle bundle = getIntent().getExtras();
        String tag = bundle.getString("Tag");
        fileName = bundle.getString("FileName");

        displayListView();
        //Toast.makeText(this, "You have tagged " + tag +" in your picture", Toast.LENGTH_LONG).show();

    }

    public void sendClick(View view) {
        //Toast.makeText(this, "Sending the photo to Yuva's server", Toast.LENGTH_LONG).show();

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

        ArrayList<Country> friendList = dataAdapter.countryList;
        String friendsSelected = null;
        for(Country friend:friendList){
            if(friend.isSelected()){
                if(friendsSelected == null){
                    friendsSelected = friend.getName();
                }else{
                    friendsSelected = friendsSelected+ "," +friend.getName();
                }
            }
        }

        Toast.makeText(this, "Your zig is out there !", Toast.LENGTH_LONG).show();

        //Send request to server
        //sendRequest();
        ServerManager.sendRequestToServer(fileName,friendsSelected);

        Intent intent = new Intent(this,MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public void sendRequest() {
        BufferedReader in = null;

        try{
            HttpClient httpclient = new DefaultHttpClient();

            HttpGet request = new HttpGet();
            URI website = new URI("http://ec2-54-69-10-140.us-west-2.compute.amazonaws.com:8080/FashionService/fashion/fashion_request/yuvek/"+fileName+"/Gautam,Yuva/true");
            request.setURI(website);
            HttpResponse response = httpclient.execute(request);
            in = new BufferedReader(new InputStreamReader(
                    response.getEntity().getContent()));

            // NEW CODE
            String line = in.readLine();
            Log.i("AWS", "Send to AWS" + line);
            in.close();

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_friends, menu);
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

    private void displayListView() {

        //Array list of countries
        ArrayList<Country> countryList = new ArrayList<Country>();
        Country country = new Country("Gautam",false);
        countryList.add(country);
        country = new Country("Yuva",false);
        countryList.add(country);
        country = new Country("Vignesh",false);
        countryList.add(country);
        country = new Country("Yuvek",false);
        countryList.add(country);
        country = new Country("Arnold",false);
        countryList.add(country);
        country = new Country("Melinda",false);
        countryList.add(country);
        country = new Country("Pranjali",false);
        countryList.add(country);

        //create an ArrayAdaptar from the String Array
        dataAdapter = new MyCustomAdapter(this,
                R.layout.country_info, countryList);
        ListView listView = (ListView) findViewById(R.id.listView1);

        // Assign adapter to ListView
        listView.setAdapter(dataAdapter);


       /* listView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // When clicked, show a toast with the TextView text
                Country country = (Country) parent.getItemAtPosition(position);
                Toast.makeText(getApplicationContext(),
                        "Clicked on Row: " + country.getName(),
                        Toast.LENGTH_LONG).show();
            }
        });*/

    }

    private class MyCustomAdapter extends ArrayAdapter<Country> {

        private ArrayList<Country> countryList;

        public MyCustomAdapter(Context context, int textViewResourceId,
                               ArrayList<Country> countryList) {
            super(context, textViewResourceId, countryList);
            this.countryList = new ArrayList<Country>();
            this.countryList.addAll(countryList);
        }

        private class ViewHolder {
            //TextView code;
            CheckBox name;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder = null;
            Log.v("ConvertView", String.valueOf(position));

            if (convertView == null) {
                LayoutInflater vi = (LayoutInflater)getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE);
                convertView = vi.inflate(R.layout.country_info, null);

                holder = new ViewHolder();
                //holder.code = (TextView) convertView.findViewById(R.id.code);
                holder.name = (CheckBox) convertView.findViewById(R.id.checkBox1);
                convertView.setTag(holder);
                // This is to get a popup when the checkbox is selected

                holder.name.setOnClickListener( new View.OnClickListener() {
                    public void onClick(View v) {
                        CheckBox cb = (CheckBox) v ;
                        Country country = (Country) cb.getTag();
                        /*Toast.makeText(getApplicationContext(),
                                "Clicked on Checkbox: " + cb.getText() +
                                        " is " + cb.isChecked(),
                                Toast.LENGTH_LONG).show();*/
                        country.setSelected(cb.isChecked());
                    }
                });
            }
            else {
                holder = (ViewHolder) convertView.getTag();
            }

            Country country = countryList.get(position);
            holder.name.setText(country.getName());
            holder.name.setChecked(country.isSelected());
            //holder.code.setText();
            holder.name.setTag(country);

            return convertView;

        }

    }
}
