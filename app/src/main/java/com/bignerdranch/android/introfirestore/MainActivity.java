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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity" ;
    private EditText enterTitle;
    private EditText enterThought;
    private Button saveButton, showButton, updateButton, deleteButton;
    private TextView recTitle;

    //Keys
    public static final String KEY_TITLE = "title";
    public static final String KEY_THOUGHT = "thought";

    //Connection to FireStore
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentReference journalRef = db.document("Journal/First thought");
/*    private DocumentReference journalRef =    db.collection("Journal")
            .document("First thought");*/
    private CollectionReference collectionReference = db.collection("Journal");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        saveButton = findViewById(R.id.save_button);
        enterTitle = findViewById(R.id.edit_text_title);
        enterThought = findViewById(R.id.edit_text_thoughts);
        recTitle = findViewById(R.id.rec_title);
        showButton = findViewById(R.id.show_button);
        updateButton = findViewById(R.id.update_button);
        deleteButton = findViewById(R.id.delete_button);

        updateButton.setOnClickListener(this);
        deleteButton.setOnClickListener(this);

        showButton.setOnClickListener(view -> {

            getThoughts();

           /* journalRef.get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if(documentSnapshot.exists()) {

                            Journal journal = documentSnapshot.toObject(Journal.class);

                          *//*
                            String title = documentSnapshot.getString(KEY_TITLE);
                            String thought = documentSnapshot.getString(KEY_THOUGHT);*//*

                            assert journal != null;
                            recTitle.setText(journal.getTitle());
                            recThought.setText(journal.getThought());
                        } else {
                            Toast.makeText(MainActivity.this, "No data", Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnFailureListener(e -> Log.d(TAG, "onFailure: " + e.toString()));*/
        });


        saveButton.setOnClickListener(view -> {

            addThought();

           /* String title = enterTitle.getText().toString().trim();
            String thought = enterThought.getText().toString().trim();

            Journal journal = new Journal();
            journal.setTitle(title);
            journal.setThought(thought);
*/


           /* Map<String, Object> data = new HashMap<>();
            data.put(KEY_TITLE, title);
            data.put(KEY_THOUGHT, thought);*/

                  /*  journalRef.set(journal)
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
                    });*/

        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        collectionReference.addSnapshotListener(this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots,
                                @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.d(TAG, "onEvent: " + error.toString());
                }

                StringBuilder data = new StringBuilder();
                for (QueryDocumentSnapshot snapshot : queryDocumentSnapshots) {
//                        Log.d(TAG, "getThoughts: " + snapshot.getId());
                    Journal journal = snapshot.toObject(Journal.class);
                    data.append("Title: ").append(journal.getTitle()).append(" \n")
                            .append("Thought: ").append(journal.getThought()).append("\n\n");
                }
                recTitle.setText(data.toString());

   /*     journalRef.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot,
                                @Nullable FirebaseFirestoreException error) {
                 if(error != null) {
                     Toast.makeText(MainActivity.this, "Something went wrong",
                             Toast.LENGTH_LONG).show();
                 }
                 if(documentSnapshot != null & documentSnapshot.exists()) {
                     String data = "";
                     Journal journal = documentSnapshot.toObject(Journal.class);
                     assert journal != null;
                     data += "Title: " + journal.getTitle() + " \n" +
                             "Thought: " + journal.getThought();
                     recTitle.setText(data);


                    *//* String title = documentSnapshot.getString(KEY_TITLE);
                     String thought = documentSnapshot.getString(KEY_THOUGHT);

                     recTitle.setText(title);
                     recThought.setText(thought);*//*
                 } else {
                     recTitle.setText("");
                 }
            }
        });*/
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

            private void addThought() {
                String title = enterTitle.getText().toString().trim();
                String thought = enterThought.getText().toString().trim();

                Journal journal = new Journal(title, thought);
       /* journal.setTitle(title);
        journal.setThought(thought);*/

                collectionReference.add(journal)
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(MainActivity.this, "Success", Toast.LENGTH_LONG).show();
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(MainActivity.this, "Something went wrong",
                                    Toast.LENGTH_LONG).show();
                        });
            }

            private void getThoughts() {
                collectionReference.get()
                        .addOnSuccessListener(queryDocumentSnapshots -> {
                            StringBuilder data = new StringBuilder();
                            for (QueryDocumentSnapshot snapshot : queryDocumentSnapshots) {
//                        Log.d(TAG, "getThoughts: " + snapshot.getId());
                                Journal journal = snapshot.toObject(Journal.class);
                                data.append("Title: ").append(journal.getTitle()).append(" \n")
                                        .append("Thought: ").append(journal.getThought()).append("\n\n");
                            }
                            recTitle.setText(data.toString());
                        })
                        .addOnFailureListener(e -> {

                        });
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