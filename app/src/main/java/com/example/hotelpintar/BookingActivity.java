package com.example.hotelpintar;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class BookingActivity extends AppCompatActivity
        implements View.OnClickListener,
        DatePickerDialog.OnDateSetListener,
        PopupMenu.OnMenuItemClickListener {

    private Button vbut1,vbut2,vbutConfirm,mChooseButton;
    private TextView t1,t2,vdays,vprice,mRoomType;
    private Boolean FROMDateIsSet=false;
    private long time1=0,time2=0;
    private double price;
    private String mID;
    private FirebaseFirestore vfirestore;
    private Calendar mFROMCalendar, mTOCalendar, mCalendar;
    private Boolean canProceed = false;
    private Dialog d1;
    private List<Calendar> blockedDays = new ArrayList<>();
    private Boolean daysInBetween = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        vbut1 = (Button) findViewById(R.id.IDpickDatebut);
        vbut1.setOnClickListener(this);
        vbut2 = (Button) findViewById(R.id.IDpickDatebut2);
        vbut2.setOnClickListener(this);
        vbutConfirm = (Button) findViewById(R.id.IDbutConfirm);
        vbutConfirm.setOnClickListener(this);
        mChooseButton = (Button) findViewById((R.id.IDbuttonChoose));
        mChooseButton.setOnClickListener(this);
        t1 = (TextView) findViewById(R.id.IDtextFromDate);
        t2 = (TextView) findViewById(R.id.IDtextToDate);
        vdays = (TextView) findViewById(R.id.IDtextDays);
        vprice = (TextView) findViewById(R.id.IDtextPrice);
        mRoomType = (TextView) findViewById(R.id.IDtextViewRoomType);
        vfirestore = FirebaseFirestore.getInstance();
        mFROMCalendar = Calendar.getInstance();
        mTOCalendar = Calendar.getInstance();
        mCalendar = Calendar.getInstance();
        d1 = new Dialog (this);
        cleanDate();
    }

    @Override
    public void onClick(View v) {
        if(v==mChooseButton)
            showPopUp(v);
        if(v==vbut1){
            try {
                if(mID.isEmpty()) {
                    throw new Exception();
                } else {
                    FROMDateIsSet = true;
                    createDatePickerDialog(mID);
                }
            } catch (Exception e) {
                Toast.makeText(this,
                        "Please Choose Room Type",
                        Toast.LENGTH_SHORT)
                        .show();
            }

        }if(v==vbut2)
            try {
                if(mID.isEmpty()) {
                    throw new Exception();
                } else {
                    createDatePickerDialog(mID);
                }
            } catch (Exception e) {
                Toast.makeText(this,
                        "Please Choose Room Type",
                        Toast.LENGTH_SHORT)
                        .show();
            }

        if(v==vbutConfirm){
            if (canProceed)
                showPopUp2(v);
            else
                displaySorryMessage();
        }
    }

    //For 'Payment Popup'
    public void showPopUp2 (View v) {
        ImageButton payButton;
        Button doneButton;
        Button closeButton;

        d1.setContentView(R.layout.payment_popup);
        //Caution, it's d1.findViewById, or else app will crash
        //Because the findViewById i'm trying to find is in another xml file
        payButton = (ImageButton) d1.findViewById(R.id.IDPaypalButton2);
        doneButton = (Button) d1.findViewById(R.id.IDButton);
        closeButton = (Button) d1.findViewById(R.id.IDButton3);

        payButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.paypal.com/my/signin"));
                startActivity(i);
            }
        });

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(BookingActivity.this,
                        "Payment Has Been Made Successfully",
                        Toast.LENGTH_SHORT)
                        .show();

                updateDatabaseBetween2Calendar();
                storeDataInDatabase();
                d1.dismiss();
                //This code will brings you back to previous activity
                //Using intent will cause app crash
                finish();
            }
        });

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d1.dismiss();
            }
        });

        d1.show();
    }

    //For 'Room Choosing' Pop-Up
    public void showPopUp (View v) {
        PopupMenu popup = new PopupMenu(this,v);
        popup.setOnMenuItemClickListener(this);
        popup.inflate(R.menu.popup_room_type);
        popup.show();
    }

    @Override
    //For Room Choosing Pop-Up
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()){
            case R.id.IDer:
                mRoomType.setText("Executive Room");
                mID = "ER";
                price = 170.00;
                t1.setText("Please Choose FROM Date");
                t2.setText("Please Choose TO Date");
                vdays.setText("-");
                vprice.setText("0");
                return true;
            case R.id.IDdtr:
                mRoomType.setText("Deluxe Triple Room");
                mID = "DTR";
                price = 140.00;
                t1.setText("Please Choose FROM Date");
                t2.setText("Please Choose TO Date");
                vdays.setText("-");
                vprice.setText("0");
                return true;
            case R.id.IDddr:
                mRoomType.setText("Deluxe Double Room");
                mID = "DDR";
                price = 120.00;
                t1.setText("Please Choose FROM Date");
                t2.setText("Please Choose TO Date");
                vdays.setText("-");
                vprice.setText("0");
                return true;
            case R.id.IDsr:
                mRoomType.setText("Standard Room");
                mID = "SR";
                price = 105.00;
                t1.setText("Please Choose FROM Date");
                t2.setText("Please Choose TO Date");
                vdays.setText("-");
                vprice.setText("0");
            default:
                return false;
        }
    }

    //To create DatePickerDialog when clicked 'PickDate'
    public void createDatePickerDialog (String s) {

        //set max min date
        Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("Asia/Singapore"));
        DatePickerDialog p = DatePickerDialog.newInstance(
                this,
                c.get(Calendar.YEAR),
                c.get(Calendar.MONTH),
                c.get(Calendar.DAY_OF_MONTH)
        );
        p.setMinDate(c);
        c.add(Calendar.MONTH,+2);
        p.setMaxDate(c);

        //Set disable days
        blockedDays.clear();
        Calendar d = Calendar.getInstance();
        d.setTimeZone(TimeZone.getTimeZone("Asia/Singapore"));
        do {
            int year = d.get(Calendar.YEAR);
            int month = d.get(Calendar.MONTH)+1;
            vfirestore.collection(year + "" + month + "")
                    .whereEqualTo(s+"", 0)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    //use getId beucase i used query,
                                    //Then firestore will only show any document ID
                                    //that has 0 value in it, so i use getId to get
                                    //the queried Id
                                    String a = document.getId();
                                    int b = Integer.parseInt(a);
                                    mCalendar.set(Calendar.YEAR,year);
                                    mCalendar.set(Calendar.MONTH,month -1);
                                    mCalendar.set(Calendar.DAY_OF_MONTH, b);
                                    Calendar[] jk;
                                    blockedDays.add(mCalendar);
                                    jk = blockedDays.toArray(new Calendar[blockedDays.size()]);
                                    p.setDisabledDays(jk);
                                }
                            }
                        }
                    });
            d.add(Calendar.MONTH,+1);
        } while (d.getTimeInMillis()<c.getTimeInMillis());
        p.show(getSupportFragmentManager(),"wtf is this for?");
    }

    @Override
    //For DatePickerDialog, to set date once clicked 'ok'
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String s;

        mCalendar.set(Calendar.YEAR, year);
        mCalendar.set(Calendar.MONTH, monthOfYear);
        mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        s = DateFormat
                .getDateInstance(DateFormat.FULL)
                .format(mCalendar.getTime());
        compareTimeAndSetText(mCalendar, s);

        calcNight();
        calcPrice();
    }

    public void compareTimeAndSetText (Calendar c, String s) {
        //If 2 'if' statement without 'else',will crash the app
        canProceed = true;

        long a = c.getTimeInMillis();
        String temp = Long.toString(a);
        if (FROMDateIsSet) {
            DataHolder.getInstance().setTime1(temp);
            time1 = Long.parseLong(DataHolder.getInstance().getTime1());
            t1.setText(s+"");
            FROMDateIsSet = false;
        }
        else {
            DataHolder.getInstance().setTime2(temp);
            time2 = Long.parseLong(DataHolder.getInstance().getTime2());
            t2.setText(s+"");
        }

        //Check if there is disabled dates in between
        //Code below made 2 calendar mFROMCalendar and mTOCalendar,
        //and time1 and time 2 all same time. WTF
        //Solution, save time into text, and use text convert back to int

        if (time2!=0 && time1!=0) {
            Calendar fromCal, toCal;
            fromCal = Calendar.getInstance();
            toCal = Calendar.getInstance();
            long a1 = Long.parseLong(DataHolder.getInstance().getTime1());
            long b2 = Long.parseLong(DataHolder.getInstance().getTime2());
            fromCal.setTimeInMillis(a1);
            toCal.setTimeInMillis(b2);

            while (toCal.getTimeInMillis() > fromCal.getTimeInMillis()) {
                int y = fromCal.get(Calendar.YEAR);
                int m = fromCal.get(Calendar.MONTH)+1;
                int d = fromCal.get(Calendar.DAY_OF_MONTH);
                DocumentReference fireRef = vfirestore
                        .collection(y + "" + m + "")
                        .document(d + "");
                fireRef.get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                if (documentSnapshot.exists()) {
                                    int w;
                                    w = documentSnapshot.getLong(mID + "").intValue();
                                    if (w == 0)
                                        daysInBetween = true;
                                }
                            }
                        });
                fromCal.add(Calendar.DAY_OF_MONTH, +1);
            }

        }

        Runnable r = new Runnable() {
            @Override
            public void run(){
                if (daysInBetween) {
                    Toast.makeText(getApplicationContext(),
                            "WARNING : There are disabled dates in between",
                            Toast.LENGTH_LONG)
                            .show();
                    daysInBetween = false;
                    canProceed = false;
                }
            }
        };

        /*This code is delaying code
        1.It delays the 'r' function declared above
        2.Because the app runs all code, and not waiting for the loop
        to finish first... resulting in rendering 'disabled msg' false
        before even finishes executing the loop
        3.Bug occurs if no delay
        - Set dates with disabled dates in between, once clicked ok
        there's no msg displaying, then you click pick date again, then only
        the msg will be displayed. Then if you chg date with no disabled dates
        in between, there's still msg displayed, then you pick date again, then
        only no msg displayed. This is because system doesn't wait for loop to finish
         4. it seems 500 is perfect value, you can't set to 300, it's still early before
         the loop finishes
         */
        Handler h = new Handler();
        h.postDelayed(r, 500);

        //Compare 2 dates and display warning message
        if ((time2<time1) && (time2>0)) {
            canProceed = false;
            Toast.makeText(getApplicationContext(),
                    "WARNING : TO Date cannot be earlier than FROM Date",
                    Toast.LENGTH_LONG)
                    .show();
        }

    }

    public void calcNight () {
        long dif,daysDif,p;
        if((time2>time1) && (time1>0)){
            dif = time2 - time1 + 1;
            daysDif = TimeUnit.MILLISECONDS.toDays(dif);
            vdays.setText(daysDif+"");
        }
        else {
            vdays.setText("-");
            vprice.setText("0");
        }
    }

    public void calcPrice () {
        try {
            double d, pri;
            d = Double.parseDouble(vdays.getText().toString());
            pri = price * d;
            vprice.setText(pri + "");
        } catch (Exception e) {
            ;
        }
    }

    public void updateDatabaseBetween2Calendar () {
        long from = Long.parseLong(DataHolder.getInstance().getTime1());
        long to = Long.parseLong(DataHolder.getInstance().getTime2());
        if((time2>time1) && (time1>0)){
            Calendar fromCal, toCal;
            fromCal = Calendar.getInstance();
            toCal = Calendar.getInstance();
            fromCal.setTimeInMillis(from);
            toCal.setTimeInMillis(to);
            do {
                int y = fromCal.get(Calendar.YEAR);
                int m = fromCal.get(Calendar.MONTH)+1;
                int d = fromCal.get(Calendar.DAY_OF_MONTH);
                DocumentReference fireRef = vfirestore
                        .collection(y+""+m+"")
                        .document(d+"");
                fireRef.get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                if(documentSnapshot.exists()){
                                    int w;
                                    w = documentSnapshot.getLong(mID+"").intValue();
                                    if (w != 0)
                                        fireRef.update(mID, FieldValue.increment(-1));
                                    else
                                        displaySorryMessage();

                                }
                            }
                        });
                fromCal.add(Calendar.DAY_OF_MONTH,+1);
            } while (fromCal.getTimeInMillis() < toCal.getTimeInMillis()+(1000*60*60*24));
        }
    }

    public void SyncDatabase () {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH,+3);
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        vfirestore.collection(year+""+month+"")
                .document("1")
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            for(int y=1;y<=31;y++) {
                                if (task.getResult().exists())
                                    ;
                                else {
                                    Calendar c = Calendar.getInstance();
                                    //Code below is to set the pop-up calendar current time
                                    c.add(Calendar.MONTH,+3);
                                    int year = c.get(Calendar.YEAR);
                                    int month = c.get(Calendar.MONTH);
                                    int date = c.get(Calendar.DAY_OF_MONTH);
                                    Map<String, Object> a = new HashMap<>();
                                    a.put("ER", 2);
                                    a.put("DTR", 22);
                                    a.put("DDR", 22);
                                    a.put("SR", 9);
                                    vfirestore.collection(year + "" + month + "")
                                            .document(y + "")
                                            .set(a, SetOptions.merge());
                                }
                            }
                        }
                        else {
                            ;
                        }
                    }
                });

    }

    public void resetDatabase () {
        Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("Asia/Singapore"));

        for (int x=1;x<3;x++) {
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH)+1;
            vfirestore.collection(year+""+month+"")
                    .document("1");

            for(int y=1;y<=31;y++) {
                //int date = c.get(Calendar.DAY_OF_MONTH);
                Map<String, Object> a = new HashMap<>();
                a.put("ER", 2);
                a.put("DTR", 22);
                a.put("DDR", 22);
                a.put("SR", 9);
                vfirestore.collection(year+""+month+"")
                        .document(y+"")
                        .set(a);
            }
            c.add(Calendar.MONTH,+1);
        }

    }

    public void displaySorryMessage () {
        Toast.makeText(getApplicationContext(),
                "SORRY : Selected Date is not available",
                Toast.LENGTH_SHORT)
                .show();
    }

    public void storeDataInDatabase () {
        String sFROM, sTO;
        sFROM = t1.getText().toString();
        //dont use code below, it seems using 2 calendars to set
        //will get bug, both sFROM and sTO date will be same
        //sFROM = DateFormat
        //.getDateInstance(DateFormat.FULL)
        //.format(mFROMCalendar.getTime());
        sTO = t2.getText().toString();
        Map<String, Object> a = new HashMap<>();
        a.put("FROM",sFROM);
        a.put("TO", sTO);
        a.put("Total Nights", vdays.getText().toString().trim());
        a.put("Email", DataHolder.getInstance().getEmail());
        a.put("Name", DataHolder.getInstance().getNickname());
        a.put("Room Type", mID);
        a.put("Price", vprice.getText().toString().trim());

        vfirestore.collection("bookings")
                .add(a);
    }

    public void cleanDate() {
        DataHolder.getInstance().setTime1("");
        DataHolder.getInstance().setTime2("");
    }

    public void deleteBookings () {
        //Didn't edit yet
        //propose solution, save time1 and time2 to each 'bookings' -> 'random ID'
        //retrive that time, and convert back to calendar,
        //then add back numbers
        long timeDif,daysDif;
        int daysDifInt;
        if((time2>time1) && (time1>0)){
            int endDate = mTOCalendar.get(Calendar.DAY_OF_MONTH);
            int startDate = mFROMCalendar.get(Calendar.DAY_OF_MONTH);
            mCalendar = mFROMCalendar;
            do {
                int y = mCalendar.get(Calendar.YEAR);
                int m = mCalendar.get(Calendar.MONTH)+1;
                int d = mCalendar.get(Calendar.DAY_OF_MONTH);
                DocumentReference fireRef = vfirestore
                        .collection(y+""+m+"")
                        .document(d+"");
                fireRef.get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                if(documentSnapshot.exists()){
                                    int w;
                                    w = documentSnapshot.getLong(mID+"").intValue();
                                    if (w != 0)
                                        fireRef.update(mID, FieldValue.increment(-1));
                                    else
                                        displaySorryMessage();
                                }
                            }
                        });
                mCalendar.add(Calendar.DAY_OF_MONTH,+1);
                startDate+=1;
            } while (startDate <= endDate);
        }
    }


}
