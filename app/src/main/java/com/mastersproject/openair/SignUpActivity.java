package com.mastersproject.openair;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mastersproject.openair.util.User;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class SignUpActivity extends AppCompatActivity {

    // Widgets
    private EditText passwordSignUp, emailSignUp, usernameET;
    private Button createBTN;
    //private AutoCompleteTextView emailSignUp;

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
        setContentView(R.layout.activity_sign_up);

        // Firebase Auth
        firebaseAuth = FirebaseAuth.getInstance();

        // Firebase Uth requires

        // Initialize Widgets
        createBTN = findViewById(R.id.createBTN);
        passwordSignUp = findViewById(R.id.passwordSignUp);
        emailSignUp = findViewById(R.id.emailSignUp);
        usernameET = findViewById(R.id.usernameET);

        // Action bar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Sign Up");
        }

        // Authentication
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                currentUser = firebaseAuth.getCurrentUser();

                if (currentUser != null){
                    // User already logged in
                } else {
                    // User not logged in
                }
            }
        };

        createBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(emailSignUp.getText().toString())
                    && !TextUtils.isEmpty(passwordSignUp.getText().toString())){

                    String email = emailSignUp.getText().toString().trim();
                    String password = passwordSignUp.getText().toString().trim();
                    String username = usernameET.getText().toString().trim();

                    CreateUserEmailAccount(email, password, username);

                } else {
                    Toast.makeText(SignUpActivity.this, "Empty Fields Detected", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void CreateUserEmailAccount(String email, String password, final String username) {
        if (!TextUtils.isEmpty(emailSignUp.getText().toString())
                && !TextUtils.isEmpty(passwordSignUp.getText().toString())){

            firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){

                                // We take user to Next Activity: (NewPost)
                                currentUser = firebaseAuth.getCurrentUser();
                                assert currentUser != null;
                                final String currentUserId = currentUser.getUid();

                                // Create a userMap so we can create a user in the User Collection in Firestore
                                Map<String, Object> userObj = new HashMap<>();
                                userObj.put("userId", currentUserId);
                                userObj.put("username", username);
                                userObj.put("waterActivities", 0);
                                userObj.put("walkActivities", 0);
                                userObj.put("hikeActivities", 0);
                                userObj.put("exerciseActivities", 0);
                                userObj.put("totalActivities", 0);

                                // Adding Users to Firestore
                                DocumentReference userDocument = collectionReference.document(currentUserId);
                                userDocument.set(userObj).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        userDocument.get()
                                                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                        if (Objects.requireNonNull(task.getResult().exists())){
//
                                                                    String name = task.getResult().getString("username");

                                                                    // If user successfully registered, then move to NewPostActivity

                                                                    // Getting use of Global USER
                                                                    User user = User.getInstance();
                                                                    user.setUserId(currentUserId);
                                                                    user.setUsername(name);

                                                                    Intent i = new Intent(SignUpActivity.this, NewPostActivity.class);
                                                                    i.putExtra("username", name);
                                                                    i.putExtra("userId", currentUserId);
                                                                    startActivity(i);
                                                                }
                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(SignUpActivity.this, "Sign Up Failed", Toast.LENGTH_LONG).show();
                                                    }
                                                });
                                    }
                                });
                            }
                        }
                    });

        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        currentUser = firebaseAuth.getCurrentUser();
        firebaseAuth.addAuthStateListener(authStateListener);
    }
}