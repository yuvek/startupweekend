/**
 * Created by yumehta on 1/25/2015.
 */
package com.example.yumehta.fashionista;

import android.graphics.Bitmap;

/**
 * @author javatechig {@link http://javatechig.com}
 *
 */
public class ImageItem {
    //private Bitmap image;
    private String filePath;
    private String title;

    public ImageItem(String filepath, String title) {
        super();
        //this.image = image;
        this.filePath = filepath;
        this.title = title;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filepath) {
        this.filePath = filepath;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
