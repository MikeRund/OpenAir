package com.mastersproject.openair;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.mastersproject.openair.util.User;


public class MainActivity extends AppCompatActivity {

    // Widgets
    private Button singInBTN, signUpBTN;
    private EditText emailET, passwordET;

    // Firebase Authentication
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser currentUser;

    // Firebase Connection
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = db.collection("Users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize widgets
        singInBTN = findViewById(R.id.signInBTN);
        signUpBTN = findViewById(R.id.signUpBTN);
        emailET = findViewById(R.id.email);
        passwordET = findViewById(R.id.password);

        // Initialize Firebase Auth
        firebaseAuth = FirebaseAuth.getInstance();

        // Functionality
        signUpBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, SignUpActivity.class);
                startActivity(i);
            }
        });

        singInBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginEmailPasswordUser(
                        emailET.getText().toString().trim(),
                        passwordET.getText().toString().trim()
                );
            }
        });
    }

    private void LoginEmailPasswordUser(String email, String password) {

        // Checking for empty text boxes
        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)){
            firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            assert user != null;
                            final String currentUserId = user.getUid();

                            collectionReference.whereEqualTo("userId", currentUserId)
                                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                        @Override
                                        public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                                            if (error != null) {

                                            }
                                            assert value != null;
                                            if (!value.isEmpty()){

                                                // Getting all the QueryDocSnapShots
                                                for (QueryDocumentSnapshot snapshot : value){
                                                    User user = User.getInstance();
                                                    user.setUsername(snapshot.getString("username"));
                                                    user.setUserId(snapshot.getString("userId"));

                                                    // Go to ListActivity after successful login
                                                    //startActivity(new Intent(MainActivity.this, AddJournalActivity.class));
                                                    startActivity(new Intent(MainActivity.this, HomePostListActivity.class));
                                                }
                                            }
                                        }
                                    });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(MainActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
        } else {
            Toast.makeText(MainActivity.this, "Please complete all text fields", Toast.LENGTH_SHORT).show();
        }
    }
}