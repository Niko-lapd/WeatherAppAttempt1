package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.AsyncTaskLoader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.Visibility;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {


    String City = "London";
    String key = "c4bba74388213396aa1f9186cb658f7b";

    String url = "http://api.openweathermap.org/data/2.5/weather?q=" + City +" &appid=" + key;

    public class DownloadJSON extends AsyncTask<String, Void, String> {
        protected String doInBackGround(String... strings){
            URL url;

            HttpURLConnection httpURLConnection;
            InputStream inputStream;
            InputStreamReader inputStreamReader;

            String result = "" ;

            try {
                url = new URL(strings[0]);
                httpURLConnection = (HttpURLConnection) url.openConnection();

                inputStream = httpURLConnection.getInputStream();

                inputStreamReader = new InputStreamReader(inputStream);

                int data = inputStreamReader.read();

                while (data != -1){
                result += (char) data;

                data = inputStreamReader.read();
                }


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            return result;
        }

        @Override
        protected String doInBackground(String... strings) {
            return null;
        }
    }

    public class DownloadIcon extends AsyncTask<String, Void, Bitmap>{

        @Override
        protected Bitmap doInBackground(String... strings) {

            Bitmap bitmap = null;

            URL url;

            HttpURLConnection httpURLConnection = null;

            InputStream inputStream;


            try {


                url = new URL(strings[0]);

                inputStream = httpURLConnection.getInputStream();

                bitmap = BitmapFactory.decodeStream(inputStream);

                httpURLConnection = (HttpURLConnection) url.openConnection();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            return bitmap;
        }
    }

    TextView txtCity,txtTime,txtValueFeelLike,txtValueHumidity,txtVision,txtTemp;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

                txtCity = findViewById(R.id.txtCity);
                txtTime = findViewById(R.id.txtTime);
                txtValueFeelLike = findViewById(R.id.txtValueFeelLike);
                txtValueHumidity = findViewById(R.id.txtValueHumidity);
                txtVision = findViewById(R.id.txtValueVision);
                txtTemp = findViewById(R.id.txtValue);
                imageView = findViewById(R.id.imgIcon);

        DownloadJSON downloadJSON = new DownloadJSON();

        try {
            String result = downloadJSON.execute(url).get();

            JSONObject jsonObject = new JSONObject(result);

            String temp = jsonObject.getJSONObject("main").getString("temp");

            String humidity = jsonObject.getJSONObject("main").getString("humidity");

            String feel_Like = jsonObject.getJSONObject("main").getString("feels_like");

            String visibility = jsonObject.getString("visibility");

            Long time = jsonObject.getLong("dt");

            String sTime = new SimpleDateFormat("dd-m-yyyy hh:mm:ss", Locale.ENGLISH)
                    .format(new Date(time* 1000));

            txtTime.setText(sTime);
            txtCity.setText(City);
            txtVision.setText(visibility);
            txtValueFeelLike.setText(feel_Like);
            txtValueHumidity.setText(humidity);
            txtTemp.setText(temp + "??");

            String nameIcon = "10d";

            nameIcon = jsonObject.getJSONArray("weather").getJSONObject(0).getString("icon");

            String urlIcon = "http://openweathermap.org/img/wn/"+ nameIcon +"@2x.png";

            DownloadIcon downloadIcon = new DownloadIcon();

            Bitmap bitmap = downloadIcon.execute(urlIcon).get();

            imageView.setImageBitmap(bitmap);

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}