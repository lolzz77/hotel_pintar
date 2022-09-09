package com.example.hotelpintar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ChooseDateActivity extends AppCompatActivity
        implements View.OnClickListener,
        DatePickerDialog.OnDateSetListener{

    Button confBtn, backBtn, fromBtn, toBtn;
    TextView textFrom,textTo;
    String room_type;
    Calendar today,maxDate;
    DatePickerDialog dpd;
    List<Calendar> blockedDaysArr = new ArrayList<>();
    int price;
    boolean FROM = true;

    String link= "http://192.168.56.1/test_android/setBlockedDays.PHP";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_date);

        room_type = DataHolder.getInstance().getRoom();

        confBtn = (Button) findViewById(R.id.confBtn);
        backBtn = (Button) findViewById(R.id.backBtn);
        fromBtn = (Button) findViewById(R.id.fromBtn);
        toBtn = (Button) findViewById(R.id.toBtn);
        confBtn.setOnClickListener(this);
        backBtn.setOnClickListener(this);
        fromBtn.setOnClickListener(this);
        toBtn.setOnClickListener(this);

        textFrom = (TextView) findViewById(R.id.textViewFrom);
        textTo = (TextView) findViewById(R.id.textViewTo);
        textFrom.setText("");
        textTo.setText("");
        today = Calendar.getInstance();
        maxDate = Calendar.getInstance();
        maxDate.add(Calendar.MONTH,+2);
        dpd = DatePickerDialog.newInstance(
                ChooseDateActivity.this,
                today.get(Calendar.YEAR),
                today.get(Calendar.MONTH),
                today.get(Calendar.DAY_OF_MONTH)
        );
        dpd.setMinDate(today);
        dpd.setMaxDate(maxDate);
    }



    @Override
    public void onClick(View view) {
        if(view == fromBtn)
            dpd.show(getSupportFragmentManager(),"?");

        else if(view == toBtn)
            dpd.show(getSupportFragmentManager(),"?");

        else if(view == confBtn){
            //A reservation is only made when payment is completed
            try {
                calculate();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Intent i = new Intent (getApplicationContext(),
                    PaymentActivity.class);
            startActivity(i);
        }
        else if (view == backBtn)
            this.finish();
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String strDate = year+"-"+(monthOfYear+1)+"-"+dayOfMonth;
        setDate(strDate);
    }

    public void setDate(String s) {
        if(FROM){
            FROM = false;
            textFrom.setText(s);
            DataHolder.getInstance().setFromDate(s);
        } else {
            textTo.setText(s);
            DataHolder.getInstance().setToDate(s);
        }
    }

    public void calculate() throws ParseException {
        String strFROM = DataHolder.getInstance().getFromDate();
        String strTO = DataHolder.getInstance().getToDate();
        Date FROM = new SimpleDateFormat("yyyy-MM-dd").parse(strFROM);
        Date TO = new SimpleDateFormat("yyyy-MM-dd").parse(strTO);
        long diff = TO.getTime() - FROM.getTime();
        long diffDays = diff / (24 * 60 * 60 * 1000);
        int diffDaysInt = (int) diffDays;

        switch(room_type){
            case "Executive":
                price = 170*diffDaysInt;
                break;
            case "Deluxe Triple":
                price = 140*diffDaysInt;
                break;
            case "Deluxe Double":
                price = 120*diffDaysInt;
                break;
            case "Standard":
                price = 105*diffDaysInt;
                break;
            default:
                break;
        }

        DataHolder.getInstance().setPrice(price);
    }
    /*
    private class getVacancy extends AsyncTask<String, Void, String> {
        //Change return type doInBackground needs change 3rd parameter of AsyncTask
        @Override
        protected String doInBackground(String... params) {
            //Store into array and use for Buffered Writer
            String a = params[0];
            String result="";
            try {

                //Create URL and put link
                java.net.URL url = new URL(link);
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

            try {
                if(resp.equals("")){

                }else {
                    JSONObject json = new JSONObject(resp);
                    String d = json.getString("disabledDate");
                    Date date=new SimpleDateFormat("dd-MM-yyyy").parse(d);
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(date);
                    blockedDaysArr.add(calendar);
                    Calendar[] bd;
                    bd = blockedDaysArr.toArray(new Calendar[blockedDaysArr.size()]);
                    dpd.setDisabledDays(bd);
                }

            } catch (Exception e) {

            }

        }
    }
    */
}


