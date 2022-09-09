package com.example.hotelpintar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class DeleteAccountAcitivity extends AppCompatActivity
    implements View.OnClickListener{

    private Button yes;
    private Button no;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_account_acitivity);

        yes = (Button) findViewById(R.id.IDyesButton);
        no = (Button) findViewById(R.id.IDnoButton);
        yes.setOnClickListener(this);
        no.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v==yes) {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

            user.delete()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()) {
                                Toast.makeText(DeleteAccountAcitivity.this,
                                        "Account Deleted",
                                        Toast.LENGTH_SHORT)
                                        .show();
                                deleteRecords();
                                //Code below will restart the app
                                Intent i = getBaseContext().getPackageManager()
                                        .getLaunchIntentForPackage(getBaseContext().getPackageName());
                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(i);
                            }
                        }
                    });
        }
        if (v==no) {
            finish();
        }
    }
    //Delete bookings records and user nickname record
    public void deleteRecords () {
        FirebaseFirestore vfirestore;
        vfirestore = FirebaseFirestore.getInstance();
        String e = DataHolder.getInstance().getEmail();

        vfirestore.collection("bookings")
                .whereEqualTo("Email",e)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String id;
                                id = document.getId();
                                vfirestore.collection("bookings")
                                        .document(id)
                                        .delete();
                            }
                        }
                    }
                });

        vfirestore.collection("users")
                .document(e)
                .delete();
    }
}
