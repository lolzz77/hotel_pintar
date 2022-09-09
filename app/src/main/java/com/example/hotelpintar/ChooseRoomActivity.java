package com.example.hotelpintar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;

public class ChooseRoomActivity extends AppCompatActivity
        implements View.OnClickListener {

    RadioGroup rg;
    Button confBtn, backBtn;
    int roomID;
    String room_type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_room);

        confBtn = (Button) findViewById(R.id.confBtn);
        confBtn.setOnClickListener(this);
        backBtn = (Button) findViewById(R.id.backBtn);
        backBtn.setOnClickListener(this);
        rg = (RadioGroup) findViewById(R.id.radioRoom);
    }

    @Override
    public void onClick(View view) {
        if(view == confBtn) {
            determineRoom();
            Intent i = new Intent (getApplicationContext(),
                    ChooseDateActivity.class);
            startActivity(i);
        }
        else if (view == backBtn)
            this.finish();
    }

    public void determineRoom() {
        roomID = rg.getCheckedRadioButtonId();

        switch (roomID) {
            case R.id.radioE:
                room_type = "Executive";
                DataHolder.getInstance().setRoomType(room_type);
                break;
            case R.id.radioDT:
                room_type = "Deluxe Triple";
                DataHolder.getInstance().setRoomType(room_type);
                break;
            case R.id.radioDD:
                room_type = "Deluxe Double";
                DataHolder.getInstance().setRoomType(room_type);
                break;
            case R.id.radioS:
                room_type = "Standard";
                DataHolder.getInstance().setRoomType(room_type);
                break;
            default:
                break;
        }

    }
}
