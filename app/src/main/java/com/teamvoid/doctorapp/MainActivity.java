package com.teamvoid.doctorapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    CardView HomeCard;
    CardView PlCard;
    CardView PsCard;
    CardView DlCard;
    CardView DsCard;

    String Status = "Home";
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference DocRef = database.getReference("Doctors");
    DatabaseReference UserRef = database.getReference("Users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        HomeCard = findViewById(R.id.homeCard);
        PlCard = findViewById(R.id.pLoginCard);
        PsCard = findViewById(R.id.pSignUpCard);
        DlCard = findViewById(R.id.dLoginCard);
        DsCard = findViewById(R.id.dSignUpCard);


        ProgressDialog loading = new ProgressDialog(MainActivity.this);
        loading.setCancelable(false);
        loading.setMessage("Please Wait.");
        loading.setProgressStyle(ProgressDialog.STYLE_SPINNER);


        findViewById(R.id.dsBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoDSignUp();
            }
        });
        findViewById(R.id.dlBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoDLogin();
            }
        });
        findViewById(R.id.plBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoPLogin();
            }
        });
        findViewById(R.id.psBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoPSignUp();
            }
        });
        findViewById(R.id.pSignUpLogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoPLogin();
            }
        });
        findViewById(R.id.pLoginCreateAcc).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoPSignUp();
            }
        });
        findViewById(R.id.dLoginCreateAcc).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoDSignUp();
            }
        });
        findViewById(R.id.dSignUpLogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoDLogin();
            }
        });

        // Doc Sign Up
        findViewById(R.id.dSignUpBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText username = findViewById(R.id.dSignUpUsername);
                EditText pass = findViewById(R.id.dSignUpPass);
                EditText name = findViewById(R.id.dSignUpName);
                EditText exp = findViewById(R.id.dSignUpExp);

                if ((!username.getText().toString().trim().isEmpty())&&(!pass.getText().toString().trim().isEmpty())&&(!name.getText().toString().trim().isEmpty())&&(!exp.getText().toString().trim().isEmpty())) {

                    loading.show();
                    Map<String, String> Data = new HashMap<String, String>();
                    Data.put("Name", name.getText().toString().trim());
                    Data.put("Exp", exp.getText().toString().trim());
                    Data.put("Pass", pass.getText().toString().trim());
                    Data.put("username", username.getText().toString().trim());
                    DocRef.child(username.getText().toString().trim()).setValue(Data);

                    // Listener
                    Status = "Ds"+username.getText().toString().trim();
                    DocRef.child(username.getText().toString().trim()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (Status.equals("Ds"+username.getText().toString().trim())) {
                                Status = "Ds";
                                Toast.makeText(getApplicationContext(), "Sign Up Successful", Toast.LENGTH_SHORT).show();
                                GoDLogin();
                                loading.dismiss();
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            loading.dismiss();
                        }
                    });


                } else {
                    Toast.makeText(getApplicationContext(), "Please fill in All Details", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //Doc Login
        findViewById(R.id.dLoginBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText username = findViewById(R.id.dLoginUsername);
                EditText pass = findViewById(R.id.dLoginPass);
                if ((!username.getText().toString().trim().isEmpty())&&(!pass.getText().toString().trim().isEmpty())) {
                    loading.show();
                    // Listener
                    Status = "Dl"+username.getText().toString().trim();
                    DocRef.child(username.getText().toString().trim()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (Status.equals("Dl" + username.getText().toString().trim())) {
                                Status = "Dl";
                                Home.User.clear();
                                if (snapshot.exists()) {
                                    for (DataSnapshot detail : snapshot.getChildren()) {
                                        Home.User.add(detail.getValue().toString());
                                    }
                                    if (pass.getText().toString().trim().equals(Home.User.get(2))) {
                                        Home.User.add("Doctor");
                                        Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(getApplicationContext(),Home.class));
                                        finish();
                                    } else {
                                        Toast.makeText(getApplicationContext(), "Incorrect Password", Toast.LENGTH_SHORT).show();
                                    }
                                    loading.dismiss();
                                } else {
                                    Toast.makeText(getApplicationContext(), "No User Found", Toast.LENGTH_SHORT).show();
                                    loading.dismiss();
                                }
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            loading.dismiss();
                        }
                    });


                } else {
                    Toast.makeText(getApplicationContext(), "Please fill in All Details", Toast.LENGTH_SHORT).show();
                }
            }
        });


        // Patient Sign Up
        findViewById(R.id.pSignUpBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText username = findViewById(R.id.pSignUpUsername);
                EditText pass = findViewById(R.id.pSignUpPass);
                EditText name = findViewById(R.id.pSignUpName);
                EditText dob = findViewById(R.id.pSignUpAge);

                if ((!username.getText().toString().trim().isEmpty())&&(!pass.getText().toString().trim().isEmpty())&&(!name.getText().toString().trim().isEmpty())&&(!dob.getText().toString().trim().isEmpty())) {

                    loading.show();
                    Map<String, String> Data = new HashMap<String, String>();
                    Data.put("Name", name.getText().toString().trim());
                    Data.put("Dob", dob.getText().toString().trim());
                    Data.put("Pass", pass.getText().toString().trim());
                    Data.put("username", username.getText().toString().trim());
                    UserRef.child(username.getText().toString().trim()).setValue(Data);

                    // Listener
                    Status = "Ps"+username.getText().toString().trim();
                    UserRef.child(username.getText().toString().trim()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (Status.equals("Ps"+username.getText().toString().trim())) {
                                Status = "Ps";
                                Toast.makeText(getApplicationContext(), "Sign Up Successful", Toast.LENGTH_SHORT).show();
                                GoPLogin();
                                loading.dismiss();
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            loading.dismiss();
                        }
                    });


                } else {
                    Toast.makeText(getApplicationContext(), "Please fill in All Details", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //Patient Login
        findViewById(R.id.pLoginBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText username = findViewById(R.id.pLoginUsername);
                EditText pass = findViewById(R.id.pLoginPass);
                if ((!username.getText().toString().trim().isEmpty())&&(!pass.getText().toString().trim().isEmpty())) {
                    loading.show();
                    // Listener
                    Status = "Pl"+username.getText().toString().trim();
                    UserRef.child(username.getText().toString().trim()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (Status.equals("Pl" + username.getText().toString().trim())) {
                                Status = "Pl";
                                Home.User.clear();
                                if (snapshot.exists()) {
                                    for (DataSnapshot detail : snapshot.getChildren()) {
                                        Home.User.add(detail.getValue().toString());
                                    }
                                    if (pass.getText().toString().trim().equals(Home.User.get(2))) {
                                        Home.User.add("Patient");
                                        Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(getApplicationContext(),Home.class));
                                        finish();
                                    } else {
                                        Toast.makeText(getApplicationContext(), "Incorrect Password", Toast.LENGTH_SHORT).show();
                                    }
                                    loading.dismiss();
                                } else {
                                    Toast.makeText(getApplicationContext(), "No User Found", Toast.LENGTH_SHORT).show();
                                    loading.dismiss();
                                }
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            loading.dismiss();
                        }
                    });


                } else {
                    Toast.makeText(getApplicationContext(), "Please fill in All Details", Toast.LENGTH_SHORT).show();
                }
            }
        });



    }

    @Override
    public void onBackPressed() {
        if (!Status.equals("Home"))
            GoHome();
        else
            super.onBackPressed();
    }

    void GoHome(){
        HomeCard.setVisibility(View.VISIBLE);
        PsCard.setVisibility(View.GONE);
        PlCard.setVisibility(View.GONE);
        DsCard.setVisibility(View.GONE);
        DlCard.setVisibility(View.GONE);
    }

    void GoPLogin (){
        GoHome();
        HomeCard.setVisibility(View.GONE);
        PlCard.setVisibility(View.VISIBLE);
        Status = "Pl";
    }
    void GoPSignUp (){
        GoHome();
        HomeCard.setVisibility(View.GONE);
        PsCard.setVisibility(View.VISIBLE);
        Status = "Ps";
    }
    void GoDSignUp (){
        GoHome();
        HomeCard.setVisibility(View.GONE);
        DsCard.setVisibility(View.VISIBLE);
        Status = "Ds";
    }
    void GoDLogin (){
        GoHome();
        HomeCard.setVisibility(View.GONE);
        DlCard.setVisibility(View.VISIBLE);
        Status = "Dl";
    }
}