package com.nadim.csedashboard.loadimage;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.io.File;

/**
 * Created by D3str0yeR on 6/23/2018.
 */
public class ImageDownloadHelper {


    public void downloadImage(Activity activity, String url, String filename, ImageView i)

    {
        File file = new File(activity.getFilesDir() + File.separator + filename);
        Log.d("ImageService", String.valueOf(file));

        if (file.exists()) {
            Glide.with(activity).load(file).into(i);
            Log.d("ImagedoService", "PhotodownLoaded " + filename);
        } else {
            Log.d("ImagedoService", "PhotodownLoading.... " + filename);
            // Construct our Intent specifying the Service
            Intent intent = new Intent(activity, ImageDownloader.class);
            // Add extras to the bundle
            intent.putExtra("url", url);
            intent.putExtra("filename", filename);
            // Start the service
            activity.startService(intent);

        }

    }


    public void downloadImage(Activity activity, String url, String filename) {


        try {
            File file = new File(activity.getFilesDir() + File.separator + filename);
            Log.d("ImageService", String.valueOf(file));

            if (!file.exists()) {
                Log.d("ImageService", "PhotoLoading.... " + filename);
                // Construct our Intent specifying the Service
                Intent intent = new Intent(activity, ImageDownloader.class);
                // Add extras to the bundle
                intent.putExtra("url", url);
                intent.putExtra("filename", filename);
                // Start the service
                activity.startService(intent);

            } else {

                Log.d("ImageService", "PhotoLoaded " + filename);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void downloadImageinDir(Activity activity, String url, String filename, String dir) {


        try {
            File file = new File(activity.getFilesDir() + File.separator + filename);
            File directory = new File(activity.getFilesDir() + File.separator + dir);
            Log.d("ImageService", String.valueOf(file));

            if (!directory.exists()) {
                if (directory.mkdirs()) {
                    Log.d("dirinfo", dir + " has been created successfully");
                }
            }

            if (!file.exists()) {
                Log.d("ImageService", "PhotoLoading.... " + filename);
                // Construct our Intent specifying the Service
                Intent intent = new Intent(activity, ImageDownloader.class);
                // Add extras to the bundle
                intent.putExtra("url", url);
                intent.putExtra("filename", filename);
                // Start the service
                activity.startService(intent);

            } else {

                Log.d("ImageService", "PhotoLoaded " + filename);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
