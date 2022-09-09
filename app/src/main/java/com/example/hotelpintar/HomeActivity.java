package com.example.hotelpintar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class HomeActivity extends LoginActivity {

    private AppBarConfiguration mAppBarConfiguration;
    //My variables
    private TextView vnickName;
    private FirebaseFirestore vfireDB;
    private DocumentReference vfireRef;
    private String mEmail;
    private CollectionReference fireColRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Code below for the floating button at bottom right corner
        /*FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow,
                R.id.nav_tools, R.id.nav_share, R.id.nav_send)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        //1 Code below is the "=" Navigation Bar
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        //My Custom Code starts here
        vfireDB = FirebaseFirestore.getInstance();
        fireColRef = vfireDB.collection("bookings");
        displayNickName();

        //Til here
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    //Function below is for "=" Navigation Bar
    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    //My user-defined function
    public void displayNickName() {
        Intent i = getIntent();
        //Getting passed item from previous screen
        //"passEmail" is Key Value
        mEmail = i.getStringExtra("passEmail");
        //Retriving 'name' data from Database
        vfireRef = vfireDB
                .collection("users")
                .document(mEmail);
        vfireRef.get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot m = task.getResult();
                            //You have to include if( != null) like that
                            //if not will give out warning 'Method invocation...'
                            if (m != null) {
                                //Because this involves Navigation Drawer Layout
                                //Then have to code this way
                                NavigationView n = findViewById(R.id.nav_view);
                                View header = n.getHeaderView(0);
                                TextView text = (TextView) header.findViewById(R.id.nickNameID);
                                //"name" refers to 'Field' in database
                                text.setText(m.getString("name"));
                                String s = text.getText().toString();
                                DataHolder.getInstance().setNickname(s);
                                DataHolder.getInstance().storeTextViewObject(text);
                            }
                        }
                    }
                });
    }
}



//Note
//fragment_home_drawer.xml is the xml file to edit
//the drawer items, i.e. Gallery, Settings, etc

//What i leanred
//1. Locate their layout ID
//-There are many layout ID here, you can right-click on them and 'Go to'->'Declaration'
//-to locate their xml code

//2. Putting image
//-When putting image into 'drawable' folder
//-image name cannot contain capital letters!
//-and for this case, put 'ImageView' in 'fragment_home.xml' for HomePage

//3. Coding for 'Home_Fragment'
//A-You may see 'fragment_home.xml'
//-And you want to put button or anything in there, but then where you code?
//-You should code at 'Java'->'Package name'->'ui'->'home'->'HomeFragment'
//B-How to declare widgets in Fragment?
//-When I go to 'HomeFragment' as said above, i notice the way to code
//-for widget is slightly different, below are the code provided by system
//View root = inflater
//            .inflate(R.layout.fragment_home, container, false);
// final TextView textView = root
//            .findViewById(R.id.text_home);
//-First must declare 'View root', then use 'root' to findViewByID
//-if you wonder where that 'R.id.text_home' is from, you can right
//-click on 'text_home'->'go to'->'declaration'->choose the pop up
//-it will bring you to .xml file
//-And remember, you have to put the abovementioned code before
//-the 'return root', meaning you cannot put the code after the
//-'return root' code.
//-And finally, when using Intent in fragment, in the parameter,
//-Usually we use 'GetApplicationContext', but in fragment,
//-we use 'GetActivity'

//4. Adding '1' to Calendar.Month
//Using c.get(Calendar.Month) will return one less value
//Because month starts at index of 0
//But i accidentally put c.get(Calendar.Month +1); which result in
//wrong value. You should put c.get(Calendar.Month) +1;