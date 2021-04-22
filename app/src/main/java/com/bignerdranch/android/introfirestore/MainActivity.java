package com.bignerdranch.android.introfirestore;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {
    private EditText enterName;
    private EditText enterThought;
    private Button saveButton;

    //Keys
    public static final String KEY_TITLE = "title";
    public static final String KEY_THOUGHT = "thought";

    //Connection to FireStore
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        saveButton.setOnClickListener(view -> {

        });

    }
}