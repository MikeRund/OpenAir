package com.mastersproject.openair;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.mastersproject.openair.util.User;
import com.mastersproject.openair.util.UserDataCallback;


public class MainActivity extends AppCompatActivity {

    // Widgets
    private Button singInBTN, signUpBTN;
    private EditText emailET, passwordET;

    // Firebase Authentication
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser user;

    // User ID & Username
    private String currentUserId;
    private String currentUsername;

    // Fetched user data from firestore
    public static final String DEFAULT_PROFILE_URL = "https://upload.wikimedia.org/wikipedia/commons/2/2c/Default_pfp.svg";
    private String fetchedImageURL;
    private long fetchedExerciseActivities;
    private long fetchedWalkActivities;
    private long fetchedHikeActivities;
    private long fetchedWaterActivities;
    private long fetchedTotalActivities;

    // Firebase Connection
    private DocumentReference mUserRef;
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

        // Action bar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Log In");
        }


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

                            // Set currentUserId and Username to logged in instance
                            assert user != null;
                            currentUserId = user.getUid();

                           // final String currentUserId = user.getUid();

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

    private void FetchUserData(UserDataCallback callback) {
        user = firebaseAuth.getCurrentUser();
        firebaseAuth.addAuthStateListener(authStateListener);
        mUserRef = db.collection("Users").document(currentUserId);

        mUserRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot.exists()){

                        // Fetch the User data from the Users collection
                        fetchedImageURL = documentSnapshot.getString("imageURL");
                        fetchedExerciseActivities = (long) documentSnapshot.get("exerciseActivities");
                        fetchedHikeActivities = (long) documentSnapshot.get("hikeActivities");
                        fetchedWalkActivities = (long) documentSnapshot.get("walkActivities");
                        fetchedWaterActivities = (long) documentSnapshot.get("waterActivities");
                        fetchedTotalActivities = (long) documentSnapshot.get("totalActivities");

                        if (fetchedImageURL == null) {
                            fetchedImageURL = DEFAULT_PROFILE_URL;
                        }
                        Toast.makeText(MainActivity.this, "Total Activities of User: " + fetchedTotalActivities, Toast.LENGTH_SHORT).show();
                        // Notify Callback with user data
                        callback.onUserDataFetched(
                                fetchedImageURL,
                                fetchedExerciseActivities,
                                fetchedHikeActivities,
                                fetchedWalkActivities,
                                fetchedWaterActivities,
                                fetchedTotalActivities
                        );
                    } else {
                        Toast.makeText(MainActivity.this, "No such document", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}