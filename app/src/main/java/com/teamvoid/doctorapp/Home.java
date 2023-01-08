package com.teamvoid.doctorapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Home extends AppCompatActivity {

    static List<String> User = new ArrayList<>();
    String Status = "Home";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getSupportActionBar().hide();

        TextView profile = findViewById(R.id.profile);
        TextView name = findViewById(R.id.name);
        TextView userType = findViewById(R.id.user);
        TextView exp = findViewById(R.id.exp);
        TextView select = findViewById(R.id.select);
        LinearLayout list = findViewById(R.id.list);
        LayoutInflater inflater = LayoutInflater.from(this);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference Ref;

        if (User.get(1).length() > 2)
            profile.setText(User.get(1).toUpperCase(Locale.ROOT).charAt(0) + "" + User.get(1).toUpperCase(Locale.ROOT).charAt(1));
        else
            profile.setText("--");
        name.setText(User.get(1));
        userType.setText("(" + User.get(4) + ")");
        if (User.get(4).equalsIgnoreCase("Doctor")) {
            exp.setText("Experience: "+User.get(0));
            select.setText("Select Patient Below to Chat");
            Ref = database.getReference("Users");
        } else {
            exp.setText("Date of Birth: "+User.get(0));
            select.setText("Select Doctor Below to Chat");
            Ref = database.getReference("Doctors");
        }


        list.removeAllViews();
        Status = "Home"+User.get(3);
        Ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (Status.equals("Home"+User.get(3))) {
                    Status = "Home";
                    Iterable<DataSnapshot> users = snapshot.getChildren();
                    for (DataSnapshot user: users) {
                        List<String> userDetails = new ArrayList<String>();
                        for (DataSnapshot value : user.getChildren()) {
                            if (value.getValue() != null)
                                userDetails.add(value.getValue().toString());
                        }
                        View listView = inflater.inflate(R.layout.user_item, list, false);
                        TextView name = listView.findViewById(R.id.name);
                        TextView bio = listView.findViewById(R.id.bio);
                        TextView chatBtn = listView.findViewById(R.id.chatBtn);
                        name.setText(userDetails.get(1));
                        if (User.get(4).equalsIgnoreCase("Doctor")) {
                            bio.setText("DOB: " + userDetails.get(0));
                            userDetails.add("Patient");
                        }
                        else{
                            bio.setText("Experience: " + userDetails.get(0));
                            userDetails.add("Doctor");
                        }
                        chatBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Chat.Reciever = userDetails;
                                startActivity(new Intent(getApplicationContext(),Chat.class));
                            }
                        });
                        list.addView(listView);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });


        findViewById(R.id.LogoutBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                finish();
            }
        });

    }
}