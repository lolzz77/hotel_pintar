package com.example.hotelpintar;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class PaymentActivity extends AppCompatActivity
    implements View.OnClickListener{

    private Button applyBtn,confBtn,backBtn;
    private TextView textOP,textNP;
    private EditText editPromo;
    private String price_before;

    String link1= "http://192.168.56.1/test_android/getPROMO.PHP";
    String link2= "http://192.168.56.1/test_android/insertRES_PAY.PHP";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        price_before = DataHolder.getInstance().getPrice()+"";
        editPromo = (EditText) findViewById(R.id.editPromo);
        textOP = (TextView) findViewById(R.id.textOP);
        textNP = (TextView) findViewById(R.id.textNP);
        textOP.setText("RM "+DataHolder.getInstance().getPrice());
        confBtn = (Button) findViewById(R.id.confBtn);
        applyBtn = (Button) findViewById(R.id.applyBtn);
        backBtn = (Button) findViewById(R.id.backBtn);
        confBtn.setOnClickListener(this);
        applyBtn.setOnClickListener(this);
        backBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view == applyBtn){
            String promo = editPromo.getText().toString().trim();
            Uri.Builder builder = new Uri.Builder()
                    .appendQueryParameter("PROMOTION", promo);
            String query = builder.build().getEncodedQuery();
            Promotion promotion = new Promotion();
            promotion.execute(query);
        }
        else if (view == confBtn){
            Toast.makeText(this,
                    "Payment successful, Room reserved",
                    Toast.LENGTH_SHORT)
                    .show();
            Intent i = new Intent(getApplicationContext(),HomeActivity.class);
            startActivity(i);

            String strFROM = DataHolder.getInstance().getFromDate();
            String strTO = DataHolder.getInstance().getToDate();

            String promo = editPromo.getText().toString().trim();

            Uri.Builder builder = new Uri.Builder()
                    .appendQueryParameter("PROMOTION", promo)
                    .appendQueryParameter("ROOM_TYPE", DataHolder.getInstance().getRoom())
                    .appendQueryParameter("FROM", strFROM)
                    .appendQueryParameter("TO", strTO)
                    .appendQueryParameter("ACC_ID", DataHolder.getInstance().getACC())
                    .appendQueryParameter("P_BEFORE", price_before)
                    .appendQueryParameter("P_AFTER", DataHolder.getInstance().getPrice()+"");
            String query = builder.build().getEncodedQuery();
            Reserves reserves = new Reserves();
            reserves.execute(query);
        }

        else if (view == backBtn)
            this.finish();

    }

    private class Promotion extends AsyncTask<String, Void, String> {
        //Change return type doInBackground needs change 3rd parameter of AsyncTask
        @Override
        protected String doInBackground(String... params) {
            //Store into array and use for Buffered Writer
            String a = params[0];
            String result="";
            try {

                //Create URL and put link
                java.net.URL url = new URL(link1);
                //Create connection
                HttpURLConnection con;
                //Open connection
                con = (HttpURLConnection) url.openConnection();
                //Specify GET/POST method
                con.setRequestMethod("POST");
                //Allow GET/POST method
                con.setDoOutput(true);

                //To be used in BufferedWriter
                OutputStream os = con.getOutputStream();
                //To send variable to destination
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                //Write a - 'query' variable from registerAcc()
                writer.write(a);
                writer.flush();
                writer.close();
                os.close();
                //To execute PHP script
                InputStream is = con.getInputStream();

                //To get PHP data
                InputStream getResp = new BufferedInputStream(con.getInputStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(getResp));
                String line = "";
                StringBuilder stringBuilder = new StringBuilder();
                //Reads all 'echo' line by line
                while ((line = reader.readLine()) != null)
                    stringBuilder.append(line).append("\n");

                result = stringBuilder.toString();

                reader.close();
                getResp.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
            //By default it has return null
            return result;
        }

        protected void onPostExecute(String resp) {
            //After doInBackground, onPostExecute will execute automatically
            //Parameter passing will done automatically
            //Put return value in doInBackground, put data type in onPostExecute argument
            //It will retrieve all the 'echo' - printed on screen messages
            try {
                //PHP echoed {"success":1} json
                //We have to decode the json format
                JSONObject json = new JSONObject(resp);
                String success = json.getString("success");

                if(success.equals("1")) {
                    Toast.makeText(PaymentActivity.this,
                            "Promotion Applied",
                            Toast.LENGTH_SHORT)
                            .show();
                    int p = DataHolder.getInstance().getPrice();
                    p = p/2;
                    DataHolder.getInstance().setPrice(p);
                    textNP.setText("RM "+p);
                } else {
                    Toast.makeText(PaymentActivity.this,
                            "Invalid Code",
                            Toast.LENGTH_SHORT)
                            .show();
                }

            } catch (Exception e) {

            }

        }
    }

    private class Reserves extends AsyncTask<String, Void, String> {
        //Change return type doInBackground needs change 3rd parameter of AsyncTask
        @Override
        protected String doInBackground(String... params) {
            //Store into array and use for Buffered Writer
            String a = params[0];
            String result="";
            try {
                //Create URL and put link
                java.net.URL url = new URL(link2);
                //Create connection
                HttpURLConnection con;
                //Open connection
                con = (HttpURLConnection) url.openConnection();
                //Specify GET/POST method
                con.setRequestMethod("POST");
                //Allow GET/POST method
                con.setDoOutput(true);

                //To be used in BufferedWriter
                OutputStream os = con.getOutputStream();
                //To send variable to destination
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                //Write a - 'query' variable from registerAcc()
                writer.write(a);
                writer.flush();
                writer.close();
                os.close();
                //To execute PHP script
                InputStream is = con.getInputStream();

                //To get PHP data
                InputStream getResp = new BufferedInputStream(con.getInputStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(getResp));
                String line = "";
                StringBuilder stringBuilder = new StringBuilder();
                //Reads all 'echo' line by line
                while ((line = reader.readLine()) != null)
                    stringBuilder.append(line).append("\n");

                result = stringBuilder.toString();

                reader.close();
                getResp.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
            //By default it has return null
            return result;
        }



    }

}
