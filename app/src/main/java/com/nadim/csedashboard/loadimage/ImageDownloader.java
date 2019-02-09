package com.nadim.csedashboard.loadimage;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by D3str0yeR on 6/13/2018.
 */
public class ImageDownloader extends IntentService {
    String dirname = "CSE DashBoard";
    public ImageDownloader() {
        super("ImageDownloader");
    }


    public Boolean downloadImage(Context context, String surl, String filename) throws IOException {

        URL url = new URL("https://google.com");
        try {
            url = new URL(surl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        InputStream in = null;
        try {

            in = new BufferedInputStream(url.openStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buf = new byte[1024];

        int n = 0;
        if (in != null) {
            n = in.read(buf);
            while (n != -1) {
                out.write(buf, 0, n);
                n = in.read(buf);
            }
            in.close();
            out.close();

            byte[] response = out.toByteArray();
            String filePath = Environment.getExternalStorageDirectory()+ File.separator + dirname + File.separator + filename;

            Log.d("wtf", filePath);
            FileOutputStream fos = new FileOutputStream(filePath);
            fos.write(response);
            fos.close();
            return true;
        } else {
            Log.d("wtf", "do nothing!! :D ");
            return false;

        }

    }

    @Override
    public void onCreate() {
        super.onCreate(); // if you override onCreate(), make sure to call super().
        // If a Context object is needed, call getApplicationContext() here.
        getApplicationContext();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d("ImageService", "ServiceCalled");
        String surl = intent.getStringExtra("url");
        String filename = intent.getStringExtra("filename");
        try {

            if (downloadImage(getApplicationContext(), surl, filename)) {
                Toast.makeText(getApplicationContext(), "Download Completed!!", Toast.LENGTH_LONG).show();
                Log.d("ImageService", "PhotoLoading.... Completed+" + filename);
                Intent in = new Intent("finish_activity");
                sendBroadcast(in);

            } else
                Log.d("ImageService", "PhotoLoading.... Failed");

        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
