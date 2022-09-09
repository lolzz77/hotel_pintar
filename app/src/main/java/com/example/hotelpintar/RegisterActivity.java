package com.example.hotelpintar;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText vemailR;
    private EditText vpasswordR;
    private EditText vnickname;
    private Button vregisterBtn;
    private FirebaseFirestore vfireDB;
    private Boolean can=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        vemailR = (EditText) findViewById(R.id.emailRegister);
        vpasswordR = (EditText) findViewById(R.id.passwordRegister);
        vnickname = (EditText) findViewById(R.id.nicknameRegister);
        vregisterBtn = (Button) findViewById(R.id.registerBtn);
        vregisterBtn.setOnClickListener(this);

        vfireDB = FirebaseFirestore.getInstance();
    }

    @Override
    public void onClick(View v) {
        if (v == vregisterBtn)
            registerUser();
            saveUserData();
    }

    public void registerUser () {
        final String e = vemailR.getText().toString().trim();
        final String p = vpasswordR.getText().toString().trim();
        //Create User Account
        //vfireAuth.createUserWithEmailAndPassword(e, p);
        //Code above is already enough to create account
        if(e.isEmpty() || p.isEmpty()) {
            ; //Do nothing becuase the msg will displayed
            //in next function
        } else {
            /*By declaring fireauth at here, whenever account registration rejected
            It can reset, without coding like this, you have to back press, and come
            back to this page and re-enter a valid email and password in order to work
             */
            FirebaseAuth vfireAuth;
            vfireAuth = FirebaseAuth.getInstance();
            vfireAuth.createUserWithEmailAndPassword(e, p)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()) {
                                Toast.makeText(RegisterActivity.this,
                                        "Account has been created successfully",
                                        Toast.LENGTH_SHORT)
                                        .show();
                                //Code below will go back to previous activity
                                //Any code after this code will still be executed
                                finish();
                            } else {
                                Toast.makeText(RegisterActivity.this,
                                        "The entered email has already been taken",
                                        Toast.LENGTH_SHORT)
                                        .show();
                            }
                        }
                    });
        }

    }

    public void saveUserData() {
        String u = vnickname.getText().toString().trim();
        String e = vemailR.getText().toString().trim();
        //save data to another class
        //But rmb, once you shut off the app, all these will be gone
        if (u.isEmpty()) {
            Toast.makeText(this,
                    "Some field is still blank",
                    Toast.LENGTH_SHORT)
                    .show();
        } else {

            DataHolder.getInstance().setEmail(e);
            DataHolder.getInstance().setNickname(u);

            //Save user nickname
            Map<String, Object> m = new HashMap<>();
            m.put("name", u);
            vfireDB.collection("users")
                    .document(e)
                    .set(m);
        }

    }
}
