package com.example.yumehta.fashionista;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.StrictMode;
import android.util.Log;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by yumehta on 1/24/2015.
 */
public class ServerManager {

    public static String UserName = "Vignesh";
//    public static String UserName = "Yuvek";
//    public static String UserName = "Arnold";

    public static void sendRequestToServer(String fileName ,String friends) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        BufferedReader in = null;

        try{
            HttpClient httpclient = new DefaultHttpClient();

            HttpGet request = new HttpGet();
            URI website = new URI("http://ec2-54-69-10-140.us-west-2.compute.amazonaws.com:8080/FashionService/fashion/fashion_request/"+ UserName +"/"+fileName+"/"+friends+"/true");
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

    public static Map<String, String> GetImages(){

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        BufferedReader in = null;

        try{
            HttpClient httpclient = new DefaultHttpClient();

            HttpGet request = new HttpGet();
            URI website = new URI("http://ec2-54-69-10-140.us-west-2.compute.amazonaws.com:8080/FashionService/fashion/poll_pictures/" + UserName);
            request.setURI(website);
            HttpResponse response = httpclient.execute(request);
            in = new BufferedReader(new InputStreamReader(
                    response.getEntity().getContent()));

            // NEW CODE
            String line = in.readLine();
            Map<String, String> userToImgMap = new HashMap<String, String>();
            if(line!=null) {
                JSONObject object = new JSONObject(line);

                JSONArray array = object.getJSONArray("list");
                for(int index = 0; index < array.length(); index++){
                    String tempStr = array.getString(index);
                    String[] strs = tempStr.split(":");
                    userToImgMap.put(strs[1], strs[0]);
                }
            }
            Log.i("AWS", "Send to AWS" + line);
            in.close();
            return userToImgMap;
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static Bitmap getS3Image(String s3key) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        AmazonS3Client s3Client = new AmazonS3Client(new BasicAWSCredentials("AKIAIHWZDTVAZQZAU4VA", "FsM4tXWasxFpGrz0D9ABUc7fekDShiRGEAqEJYmb"));
        GetObjectRequest getReq = new GetObjectRequest("fashionistapics", s3key);
        S3Object s3Obj = null;
        try {
             s3Obj = s3Client.getObject(getReq);
        } catch (Exception ex){
            ex.printStackTrace();
        }
        if(s3Obj == null){
            return null;
        }

        InputStream is = s3Obj.getObjectContent();
        if(is==null){
            return null;
        }
        Bitmap image = null;
        try {
            image = BitmapFactory.decodeStream(is);
        } finally {
            try {
                is.close();
            } catch(IOException e) {

            }
        }

        return image;
    }

    public static void LikePicture(String key, boolean isLiked){

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        BufferedReader in = null;

        try{
            HttpClient httpclient = new DefaultHttpClient();

            HttpGet request = new HttpGet();
            URI website = new URI("http://ec2-54-69-10-140.us-west-2.compute.amazonaws.com:8080/FashionService/fashion/fashion_response/"+UserName+"/"+key+"/"+isLiked);
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

    public static Map<String, Integer> pollLikes(){

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        BufferedReader in = null;

        try{
            HttpClient httpclient = new DefaultHttpClient();

            HttpGet request = new HttpGet();
            URI website = new URI("http://ec2-54-69-10-140.us-west-2.compute.amazonaws.com:8080/FashionService/fashion/poll_response/"+UserName);
            request.setURI(website);
            HttpResponse response = httpclient.execute(request);
            in = new BufferedReader(new InputStreamReader(
                    response.getEntity().getContent()));

            // NEW CODE

            Map<String, Integer> userToImgMap = new HashMap<String, Integer>();

            String line = in.readLine();
            if(line!=null) {
                JSONObject object = new JSONObject(line);

                JSONArray array = object.getJSONArray("list");
                if (array != null) {
                    for(int index = 0; index < array.length(); index++){
                        JSONObject tempJsonObj = array.getJSONObject(index);
                        userToImgMap.put(tempJsonObj.getString("picId"), Integer.valueOf(tempJsonObj.getString("likes")));
                    }
                }
            }
            Log.i("AWS", "Send to AWS" + line);
            in.close();
            return userToImgMap;
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
