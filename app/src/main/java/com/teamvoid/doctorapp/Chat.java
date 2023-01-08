package com.teamvoid.doctorapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.print.PrintAttributes;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Chat extends AppCompatActivity {

    static List<String> Reciever = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        getSupportActionBar().hide();


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference DocRef = database.getReference("Doctors");
        DatabaseReference UserRef = database.getReference("Users");
        DatabaseReference ChatRef = database.getReference("Chats");
        LinearLayout list = findViewById(R.id.chatList);
        LayoutInflater inflater = LayoutInflater.from(this);
        EditText msgField = findViewById(R.id.msg);
        TextView Rname = findViewById(R.id.Rname);
        TextView Rprofile = findViewById(R.id.Rprofile);
        TextView RuserType = findViewById(R.id.RuserType);
        list.removeAllViews();

        ChatRef.child(Reciever.get(3)+">"+Home.User.get(3)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    View listView = inflater.inflate(R.layout.chat_item, list, false);
                    TextView msg = listView.findViewById(R.id.msgContent);
                    TextView date = listView.findViewById(R.id.date);
                    msg.setText(snapshot.getValue().toString().trim());
                    date.setText(new Date().toLocaleString());
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
                    params.gravity = Gravity.START;
                    listView.setLayoutParams(params);
                    list.addView(listView);
                    listView.requestFocus();
                    msgField.setText("");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        ChatRef.child(Home.User.get(3)+">"+Reciever.get(3)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    View listView = inflater.inflate(R.layout.chat_item, list, false);
                    TextView msg = listView.findViewById(R.id.msgContent);
                    TextView date = listView.findViewById(R.id.date);
                    msg.setText(snapshot.getValue().toString().trim());
                    date.setText(new Date().toLocaleString());
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
                    params.gravity = Gravity.END;
                    listView.setLayoutParams(params);
                    list.addView(listView);
                    date.requestFocus();
                    msgField.setText("");
                    msgField.requestFocus();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        if (Reciever.get(1).length() > 2)
            Rprofile.setText(Reciever.get(1).toUpperCase(Locale.ROOT).charAt(0) + "" + Reciever.get(1).toUpperCase(Locale.ROOT).charAt(1));
        else
            Rprofile.setText("--");
        Rname.setText(Reciever.get(1));
        RuserType.setText("(" + Reciever.get(4) + ")");


        findViewById(R.id.sendBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (msgField.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(), "Enter Message", Toast.LENGTH_SHORT).show();
                }
                else
                    ChatRef.child(Home.User.get(3)+">"+Reciever.get(3)).setValue(msgField.getText().toString().trim());
            }
        });
    }
}