package com.example.saisandeep.multithreading;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by saisandeep on 2/12/2015.
 */
public class L {

    public static void m(String message)
    {
        Log.d("Sandeep",message);
    }

    public static void s(Context context,String message)
    {
        Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
    }
}
