package com.example.hotelpintar.ui.mybookings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.hotelpintar.DataHolder;
import com.example.hotelpintar.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class MyBookingsFragment extends Fragment {

    private MyBookingsViewModel galleryViewModel;
    private TextView list, message;
    private DocumentReference vfireRef;
    private CollectionReference fireColRef;
    private FirebaseFirestore vfireDB;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        galleryViewModel =
                ViewModelProviders.of(this).get(MyBookingsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_gallery, container, false);


        //My code starts here
        list = root.findViewById(R.id.textView8);
        vfireDB = FirebaseFirestore.getInstance();
        fireColRef = vfireDB.collection("bookings");
        //Til here

        galleryViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                ;
            }
        });

        //My 2nd code starts here, before the 'return root'
        query();
        //Til here

        return root;
    }

    public void query () {
        fireColRef.whereEqualTo("Email", DataHolder.getInstance().getEmail())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            String data = "";
                            int number = 1;
                            QuerySnapshot m = task.getResult();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //document.getData() will get all fieldpath and field data
                                //data += document.getData();

                                data += number + "\n";
                                String temp = document.getString("Room Type");
                                if (temp.equals("ER"))
                                    temp = "Executive Room";
                                else if (temp.equals("DTR"))
                                    temp = "Deluxe Triple Room";
                                else if (temp.equals("DDR"))
                                    temp = "Deluxe Double Room";
                                else if (temp.equals("SR"))
                                    temp = "Standard Room";

                                data += "Room Type : " + temp + "\n";
                                temp = document.getString("FROM");
                                data += "FROM : " + temp + "\n";
                                temp = document.getString("TO");
                                data += "TO : " + temp + "\n";
                                temp = document.getString("Total Nights");
                                data += "Total Nights : " + temp + "\n";
                                temp = document.getString("Price");
                                data += "Price : " + temp + "\n";
                                data += "\n\n";
                                number ++;
                            }
                            list.setText(data);
                        }
                    }
                });

    }
}