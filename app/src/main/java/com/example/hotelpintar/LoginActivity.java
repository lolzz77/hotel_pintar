package com.example.hotelpintar;

import android.content.Intent;
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

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private Button vregisterBtn;
    private Button vloginBtn;
    private EditText vemailI;
    private EditText vpasswordI;
    private FirebaseAuth vfireAuth;

    //onCreate is to declare variable
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        vregisterBtn = (Button) findViewById(R.id.registerBtn);
        vloginBtn = (Button) findViewById(R.id.loginBtn);
        vemailI = (EditText) findViewById(R.id.emailInput);
        vpasswordI = (EditText) findViewById(R.id.passwordInput);

        vregisterBtn.setOnClickListener(this);
        vloginBtn.setOnClickListener(this);

        vfireAuth = FirebaseAuth.getInstance();
    }
    //onClick function is for buttons
    @Override
    public void onClick(View v) {
        if (v == vregisterBtn){
            Intent i = new Intent(getApplicationContext(), RegisterActivity.class);
            startActivity(i);
        }
        else if (v == vloginBtn) {
            final String e = vemailI.getText().toString().trim();
            final String p = vpasswordI.getText().toString().trim();
            if(e.isEmpty() || p.isEmpty()){
                Toast.makeText(this,
                        "Please enter email and password",
                        Toast.LENGTH_SHORT)
                        .show();
            } else {

                DataHolder.getInstance().setEmail(e);
                signIn(e,p);
            }

        }
    }
    //user-defined function for logging in user
    public void signIn (final String e, String p) {
        vfireAuth.signInWithEmailAndPassword(e,p)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            //Passing the 'email' input to other screen
                            //"passEmail" is Key Value
                            Toast.makeText(LoginActivity.this,
                                    "Account exists, Logging-in",
                                    Toast.LENGTH_SHORT)
                                    .show();
                            Intent j = new Intent(getApplicationContext(), HomeActivity.class);
                            j.putExtra("passEmail",e);
                            //I get error variable e has to be declared final
                            //solution is declare final at the parameter
                            startActivity(j);
                        } else {
                            Toast.makeText(LoginActivity.this,
                                    "Account does not exists or incorrect Password",
                                    Toast.LENGTH_SHORT)
                                    .show();
                        }
                    }
                });
    }


}

//Websites for assistance
//https://docs.oracle.com/javase/8/docs/api/index.html?java/util/List.html
//https://www.javatpoint.com/android-toast-example
//https://github.com/firebase/quickstart-android/tree/master/auth/app/src/main?utm_source=studio
//firebase(manage user) - https://firebase.google.com/docs/auth/web/manage-users

//I have encountered numerous of errors
//1. Cannot fit the class in single dex file
//Explanation-This is because the android can only store 65k references, exceeds it will cause error
//Solution-Enable multiDEX,if minSDKVersion is 21 and above, no need to enable, by default it already enabled
//How-Tools->SDK Manager->Install Adnroid 5.0 with API version 21
//-Then right click on the 'app' folder at the left side navigator, click 'Open Module Setting'
//-Go to 'Default Config' and set minSDKVersion to 21

//2. App crashed right after start-up
//Problem-implementation 'com.google.firebase:firebase-firestore:21.2.1'
//-When attempting to do FirebaseFireStore, this automatically implemented in build.gradle(module)
//-This implementation is what caused the app to crash right after start-up
//Solution-That time, when i attempted to do FirebaseFireStore, i already attempted to do
//-FirebaseAuthentication before that, so, now it has 2 dependencies added in build.gradle(module)
//-but then, i notice both dependency version is vastly defferent, one is v16 and other is v19
//-and there's a highlighted message telling me, that the v16 has a newer version v19
//-follow that message, update that v16 to v19 and problem solved.
//-according to google, this happens because both version not same

//3. Cloud FirebaseFireStore didn't save any data
//Problem-This probably because you set 'Locked Mode' when creating Cloud Firebase
//-There are 2 modes 'Locked'-Only server-side language can modify, 'Test'-Everyone can modify
//Solution-If you accidentally clicked 'Locked Mode', go to 'Rules' tab at the Firebase 'Database'
//-'Locked Mode' coding will be 'allow read, write: if false;'
//-change it to 'allow read,wirte;' and vuola, done

//4. Unable to write data into Cloud Firestore
//I have encountered a problem where the code is running perfectly
//But the data can't be written in LCloud Firestore
//Then i realized I had written some rules in Cloud Firestore
//Rmb check the rules

//5. Close activity
//If you're looking for how to clear editText once you pressed
//'back button', i found 2, First is to clear editText
//But that method wont work. Second is to put 'finish()'
//To kill activity, but that will end up activity destroyed
//So, no answer yet, try to search on your own