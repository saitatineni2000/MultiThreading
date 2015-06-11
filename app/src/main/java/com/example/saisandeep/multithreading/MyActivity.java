package com.example.saisandeep.multithreading;

import android.app.Activity;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class MyActivity extends Activity implements AdapterView.OnItemClickListener {

    EditText editText;
    ListView listView;
    String[] listofImages;
    ProgressBar progressBar;
    LinearLayout loadingSection=null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        editText= (EditText) findViewById(R.id.edit);
        listView= (ListView) findViewById(R.id.urlList);
        listView.setOnItemClickListener(this);
        listofImages=getResources().getStringArray(R.array.imageURL);
        progressBar= (ProgressBar) findViewById(R.id.downloadProgress);
    }


    public void downloadImage(View v)
    {

        //File file=Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        //String url=listofImages[0];
        //Uri uri= Uri.parse(url);
        //L.s(this,uri.getLastPathSegment());
        //L.s(this,file.getAbsolutePath());

        String url=editText.getText().toString();
        Thread mythread=new Thread(new downloadImagesUsingWorkerThread(url));
        mythread.start();
       // downloadImagesUsingThreads(listofImages[0]);//must not do this downloading on main thread
    }

    public boolean downloadImagesUsingThreads(String url)
    {
        boolean success=false;
        URL downloadurl=null;
        HttpURLConnection conn=null;
        InputStream ips=null;
        FileOutputStream fops=null;
        File file=null;
        try {
            downloadurl=new URL(url);
            conn= (HttpURLConnection) downloadurl.openConnection();
            ips=conn.getInputStream();

            file=new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()+" /"+Uri.parse(url).getLastPathSegment());
            fops=new FileOutputStream(file);
            L.m(" "+file.getAbsolutePath());

            int read=-1;
            byte[] buffer=new byte[1024];

            while((read=ips.read(buffer))!=-1)
            {
               fops.write(buffer,0,read);
               //L.m(" "+read);
               // Log.d("Sandeep", "line is " + read);
            }
            success=true;

        } catch (MalformedURLException e) {

        } catch (IOException e) {

        }

        finally {
            this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    loadingSection.setVisibility(View.GONE);
                }
            });

            if(conn!=null)
            {
                conn.disconnect();
            }
            if(ips!=null)
            {
                try {
                    ips.close();
                } catch (IOException e) {

                }
            }
            if(fops!=null)
            {
                try {
                    fops.close();
                } catch (IOException e) {

                }
            }

        }

        return success;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        editText.setText(listofImages[i]);
    }

    private class downloadImagesUsingWorkerThread implements Runnable
    {

        private String url;
        downloadImagesUsingWorkerThread(String url)
        {
            this.url=url;
        }

        @Override
        public void run() {

            MyActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    loadingSection.setVisibility(View.VISIBLE);
                }
            });

            downloadImagesUsingThreads(url);
        }
    }
}
