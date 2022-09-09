package com.example.hotelpintar.ui.myaccount;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.hotelpintar.DataHolder;
import com.example.hotelpintar.DeleteAccountAcitivity;
import com.example.hotelpintar.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class MyAccountFragment extends Fragment
    implements View.OnClickListener{

    private MyAccountViewModel slideshowViewModel;
    private EditText newName;
    private TextView oldName;
    private Button setButton, delButton;
    private FirebaseFirestore fireA;
    private CollectionReference fireColRef;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        slideshowViewModel =
                ViewModelProviders.of(this).get(MyAccountViewModel.class);
        View root = inflater.inflate(R.layout.fragment_myaccount, container, false);
        //Code below is from default one, it display title only
        //final TextView textView = root.findViewById(R.id.text_slideshow);



        slideshowViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                //Code below will display "This is slideshow fragment" on top
                //This code is by default
                //textView.setText(s);
            }
        });

        //My code starts here
        oldName = (TextView) root.findViewById(R.id.IDoldName);
        newName = (EditText) root.findViewById(R.id.IDnewName);
        setButton = (Button) root.findViewById(R.id.IDsetButton);
        setButton.setOnClickListener(this);
        fireA = FirebaseFirestore.getInstance();
        fireColRef = fireA.collection("bookings");
        oldName.setText(DataHolder.getInstance().getNickname()+"");
        delButton = (Button) root.findViewById(R.id.IDdeleteButton);
        delButton.setOnClickListener(this);
        //Til here

        return root;
    }

    @Override
    public void onClick(View v) {
        if (v==delButton) {
            Intent i = new Intent(getActivity(),
                    DeleteAccountAcitivity.class);
            startActivity(i);
        }

        if (v==setButton) {
            String s = newName.getText().toString().trim();
            if(s.isEmpty()) {
                Toast.makeText(getActivity(),
                        "Please insert name",
                        Toast.LENGTH_SHORT)
                        .show();

            } else {

                fireA.collection("users")
                        .document(DataHolder.getInstance().getEmail())
                        .update("name",s);
                DataHolder.getInstance().setNickname(s);
                oldName.setText(s+"");

                //Code below set the nickname display from HomeActivity
                //This is a fragment.xml, but i managed to set text
                //that is from another activity, by using DataHolder Class
                DataHolder.getInstance().getTextViewName().setText(s+"");

                //Code below update username in 'bookings' in database
                fireColRef.whereEqualTo("Email", DataHolder.getInstance().getEmail())
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {

                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        fireA.collection("bookings")
                                                .document(document.getId())
                                                .update("Name",s+"");
                                    }

                                }
                            }
                        });
            }

        }
    }


}