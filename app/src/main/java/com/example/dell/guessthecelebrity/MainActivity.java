package com.example.dell.guessthecelebrity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {
    Bitmap celebimg;
    Random rand;
    ImageView img;
    Button btn0, btn1, btn2, btn3;
    int optionnum, imgnum;
    ArrayList<String> celebnames = new ArrayList<String>();
    ArrayList<String> celebimages = new ArrayList<String>();
    ArrayList<String> buttonoptions = new ArrayList<String>();

    public void row(View view) {
        if (view.getTag().toString().equals(Integer.toString(optionnum))){
            Toast.makeText(this, "correct Answer", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(this, "wrong Answer, It was" + celebnames.get(imgnum), Toast.LENGTH_SHORT).show();
        }

    }


    public class Imagedownload extends AsyncTask<String, Void, Bitmap>{


        @Override
        protected Bitmap doInBackground(String... strings) {

            try {
                URL url = new URL(strings[0]);
                HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                connection.connect();
                InputStream in = connection.getInputStream();
                Bitmap myimage = BitmapFactory.decodeStream(in);
                return myimage;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            return null;
        }
    }

    public void Getnext(){

    }

    public class Download extends AsyncTask<String, Void, String >{


    @Override
    protected String doInBackground(String... strings){
        String result = "";
        URL url;
        HttpURLConnection connection = null;

        try {
             url = new URL(strings[0]);
             connection = (HttpURLConnection)url.openConnection();
             InputStream input = connection.getInputStream();
             InputStreamReader reader = new InputStreamReader(input);
             int data = reader.read();
             while(data != -1){
                char current = (char)data;
                result += current;
                data = reader.read();
            }
            return result;
        } catch (MalformedURLException e) {
            e.printStackTrace();
          } catch (IOException e) {
            e.printStackTrace();
            }


        return null;
    }
}

    public void change(View view) {
     buttonoptions.clear();
        // img = (ImageView)findViewById(R.id.celeb);
        rand = new Random();
        imgnum = rand.nextInt(85);
        Imagedownload image = new Imagedownload();

        try {
            celebimg = image.execute(celebimages.get(imgnum)).get();
            if (celebimg == null)
                Log.i("its", "null douchebag");
            img.setImageBitmap(celebimg);
        } catch (InterruptedException e) {
            if (celebimg == null)
                Log.i("its", "null douchebag");
            e.printStackTrace();
        } catch (ExecutionException e) {
            if (celebimg == null)
                Log.i("its", "null douchebag");
            e.printStackTrace();
        }

        optionnum = rand.nextInt(4);

        for(int i = 0;i < 4;i++){
            if(i == optionnum)
                buttonoptions.add(celebnames.get(imgnum));
            else {
                int random = rand.nextInt(85);
                while (celebnames.get(imgnum) == celebnames.get(random)){
                    random = rand.nextInt(celebnames.size());
                }
                buttonoptions.add(celebnames.get(random));
            }
        }
        btn0.setText(buttonoptions.get(0));
        btn1.setText(buttonoptions.get(1));
        btn2.setText(buttonoptions.get(2));
        btn3.setText(buttonoptions.get(3));


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         img = (ImageView)findViewById(R.id.celeb);

         btn0 = (Button)findViewById(R.id.btn0);
         btn1 = (Button)findViewById(R.id.btn1);
         btn2 = (Button)findViewById(R.id.btn2);
         btn3 = (Button)findViewById(R.id.btn3);
        String data = "Nothing is here";
        Download load = new Download();
        try {
             data = load.execute("http://www.posh24.se/kandisar").get();
       String[] splitdata = data.split("<div class=\"title\">Lista:</div>");

           // Log.i("The page source : ", splitdata[1]);
            Pattern p = Pattern.compile("src=\"(.*?)\"");
            Matcher M = p.matcher(splitdata[1]);

            while (M.find()){
                celebimages.add(M.group(1));
            }
            Pattern pi = Pattern.compile("alt=\"(.*?)\"");
            Matcher Mi = pi.matcher(splitdata[1]);

            while (Mi.find()){
                celebnames.add(Mi.group(1));
            }


        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        Random rand = new Random();
        imgnum = rand.nextInt(85);
        Imagedownload image = new Imagedownload();
        Bitmap celebimg;
        try {
            celebimg = image.execute(celebimages.get(imgnum)).get();
            img.setImageBitmap(celebimg);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        optionnum = rand.nextInt(4);

        for(int i = 0;i < 4;i++){
            if(i == optionnum)
                buttonoptions.add(celebnames.get(imgnum));
            else {
                int random = rand.nextInt(85);
                while (celebnames.get(imgnum) == celebnames.get(random)){
                    random = rand.nextInt(celebnames.size());
                }
                buttonoptions.add(celebnames.get(random));
            }
        }
        btn0.setText(buttonoptions.get(0));
        btn1.setText(buttonoptions.get(1));
        btn2.setText(buttonoptions.get(2));
        btn3.setText(buttonoptions.get(3));

    }
}
