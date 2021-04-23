package com.bignerdranch.android.introfirestore;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity" ;
    private EditText enterTitle;
    private EditText enterThought;
    private Button saveButton, showButton, updateButton, deleteButton;
    private TextView recTitle, recThought;

    //Keys
    public static final String KEY_TITLE = "title";
    public static final String KEY_THOUGHT = "thought";

    //Connection to FireStore
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentReference journalRef = db.document("Journal/First thought");
/*    private DocumentReference journalRef =    db.collection("Journal")
            .document("First thought");*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        saveButton = findViewById(R.id.save_button);
        enterTitle = findViewById(R.id.edit_text_title);
        enterThought = findViewById(R.id.edit_text_thoughts);
        recTitle = findViewById(R.id.rec_title);
        recThought = findViewById(R.id.rec_thought);
        showButton = findViewById(R.id.show_button);
        updateButton = findViewById(R.id.update_button);
        deleteButton = findViewById(R.id.delete_button);

        updateButton.setOnClickListener(this);
        deleteButton.setOnClickListener(this);

        showButton.setOnClickListener(view -> {
            journalRef.get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if(documentSnapshot.exists()) {
                            String title = documentSnapshot.getString(KEY_TITLE);
                            String thought = documentSnapshot.getString(KEY_THOUGHT);

                            recTitle.setText(title);
                            recThought.setText(thought);
                        } else {
                            Toast.makeText(MainActivity.this, "No data", Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnFailureListener(e -> Log.d(TAG, "onFailure: " + e.toString()));
        });

        saveButton.setOnClickListener(view -> {
            String title = enterTitle.getText().toString().trim();
            String thought = enterThought.getText().toString().trim();

            Map<String, Object> data = new HashMap<>();
            data.put(KEY_TITLE, title);
            data.put(KEY_THOUGHT, thought);


                    journalRef.set(data)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(MainActivity.this, "Success", Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d(TAG, "onFailure: " + e.toString());
                        }
                    });

        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        journalRef.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot,
                                @Nullable FirebaseFirestoreException error) {
                 if(error != null) {
                     Toast.makeText(MainActivity.this, "Something went wrong",
                             Toast.LENGTH_LONG).show();
                 }
                 if(documentSnapshot != null & documentSnapshot.exists()) {
                     String title = documentSnapshot.getString(KEY_TITLE);
                     String thought = documentSnapshot.getString(KEY_THOUGHT);

                     recTitle.setText(title);
                     recThought.setText(thought);
                 } else {
                     recTitle.setText("");
                     recThought.setText("");
                 }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.update_button:
                updateTitle();
                break;
            case R.id.delete_button:
                deleteData();
                break;
        }
    }

    private void deleteData() {
       /* Map<String, Object> data = new HashMap<>();
        data.put(KEY_THOUGHT, FieldValue.delete());
        journalRef.update(data);*/

        journalRef.update(KEY_THOUGHT, FieldValue.delete()).addOnSuccessListener(aVoid -> {
            Toast.makeText(this, "Delete update", Toast.LENGTH_LONG).show();
        }).addOnFailureListener(e -> {
            Toast.makeText(this, "Failure delete", Toast.LENGTH_LONG).show();
        });

    }

    private void deleteAll() {
        journalRef.delete();
    }

    private void updateTitle() {
        String title = enterTitle.getText().toString().trim();
        String thought = enterThought.getText().toString().trim();
        Map<String, Object> data = new HashMap<>();
        data.put(KEY_TITLE, title);
        data.put(KEY_THOUGHT, thought);
        journalRef.update(data).addOnSuccessListener(aVoid -> {
            Toast.makeText(this, "Success update", Toast.LENGTH_LONG).show();
        }).addOnFailureListener(e -> {
            Toast.makeText(this, "Failure to update", Toast.LENGTH_LONG).show();
        });
    }
}