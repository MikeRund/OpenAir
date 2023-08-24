package com.mastersproject.openair;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mastersproject.openair.model.Post;
import com.mastersproject.openair.ui.BaseActivity;
import com.mastersproject.openair.util.User;

import java.util.Date;

public class NewPostActivity extends BaseActivity {

    private static final int GALLERY_CODE = 1;
    private Button saveBTN;
    private ProgressBar progressBar;
    private ImageView addPhotoButton, imageIV;
    private EditText titleET, descriptionET;
    private TextView currentUserTextView;
    private Spinner spinner;

    // Activity Checks
    private Boolean isWaterActivity;
    private Boolean isHikeActivity;
    private Boolean isExerciseActivity;
    private Boolean isWalkActivity;
    private Boolean isStreak;

    // User ID & Username
    private String currentUserId;
    private String currentUsername;

    // Firebase Auth
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser user;

    // Firebase Connection
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private StorageReference storageReference;
    private CollectionReference collectionReference = db.collection("Post");
    private Uri imageUri;
    private BottomNavigationView bottomNavigationView;

    public NewPostActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);

        // Initializing
        storageReference = FirebaseStorage.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.postProgressBar);
        progressBar.setVisibility(View.INVISIBLE);
        titleET = findViewById(R.id.postTitleET);
        descriptionET = findViewById(R.id.postDescriptionET);
        currentUserTextView = findViewById(R.id.postUsernameTV);
        imageIV = findViewById(R.id.postIV);
        addPhotoButton = findViewById(R.id.postCameraBTN);
        saveBTN = findViewById(R.id.postSaveBTN);
        spinner = findViewById(R.id.activty_spinner);

        // Action bar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("New Post");
        }

        // Spinner
        String[] activities = {"Surf", "Sport", "Hike", "Swim", "Run", "Walk", "General Water Activity", "General Outdoor Activity",
                                "Workout", "Other"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, activities);
        spinner.setAdapter(adapter);
        //spinner.setVisibility(View.VISIBLE);

        // Nav draw
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.action_add_navDraw);


        // Set title to username of instance
        if (User.getInstance() != null){
            currentUserId = User.getInstance().getUserId();
            currentUsername = User.getInstance().getUsername();

            currentUserTextView.setText(currentUsername);
        }

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();
                if (user != null) {

                } else {

                }
            }
        };

        saveBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SavePost();
            }
        });

        addPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Getting image from gallery
                Intent galleyIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleyIntent.setType("image/*");
                startActivityForResult(galleyIntent, GALLERY_CODE);
            }
        });

    }

    private void SavePost() {
        final String title = titleET.getText().toString().trim();
        final String description = descriptionET.getText().toString().trim();
        final String selectedActivity = spinner.getSelectedItem().toString().trim();

        progressBar.setVisibility(View.VISIBLE);

        if(!TextUtils.isEmpty(title) && !TextUtils.isEmpty(description) && imageUri != null){

            // Saving the path of the images in Storage Firebase
            // .../post_images/our_image.png
            final StorageReference filepath = storageReference
                    .child("post_images")
                    .child("my_image" + Timestamp.now().getSeconds());

            // Uploading the image
            filepath.putFile(imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {

                                    String imageUrl = uri.toString();

                                    // Creating object of Post
                                    Post post = new Post();
                                    post.setTitle(title);
                                    post.setDescription(description);
                                    post.setImageUrl(imageUrl);
                                    post.setTimestamp(new Timestamp(new Date()));
                                    post.setUsername(currentUsername);
                                    post.setUserId(currentUserId);

                                    // Checking Activity Type
                                    isWaterActivity = selectedActivity.equals("Surf") || selectedActivity.equals("Swim")
                                            || selectedActivity.equals("General Water Activity");

                                    isExerciseActivity = selectedActivity.equals("Run") || selectedActivity.equals("Workout")
                                            || selectedActivity.equals("Sport");

                                    isHikeActivity = selectedActivity.equals("Hike");

                                    isWalkActivity = selectedActivity.equals("Walk");

                                    // Incrementing Activity Types
                                    if (isWaterActivity) {
                                        User.getInstance().incrementWaterActivities();
                                        User.getInstance().incrementTotalActivities();
                                    } else if (isWalkActivity) {
                                        User.getInstance().incrementWalkActivities();
                                        User.getInstance().incrementTotalActivities();
                                    } else if (isHikeActivity) {
                                        User.getInstance().incrementHikeActivities();
                                        User.getInstance().incrementTotalActivities();
                                    } else if (isExerciseActivity) {
                                        User.getInstance().incrementExerciseActivities();
                                        User.getInstance().incrementTotalActivities();
                                    } else {
                                        User.getInstance().incrementTotalActivities();
                                    }


                                    // Invoking Collection Reference
                                    collectionReference.add(post)
                                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                @Override
                                                public void onSuccess(DocumentReference documentReference) {
                                                    updateUserFirestoreData();
                                                    progressBar.setVisibility(View.INVISIBLE);
                                                    startActivity(new Intent(NewPostActivity.this, HomePostListActivity.class));
                                                    finish();
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(NewPostActivity.this, "ERROR: " + e.getMessage(), Toast.LENGTH_LONG).show();
                                                }
                                            });

                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    });
        } else {
            progressBar.setVisibility(View.INVISIBLE);
        }
    }

    private void updateUserFirestoreData() {
        DocumentReference userDocRef = db.collection("Users").document(currentUserId);
        
        // Update Activity Data
        userDocRef.update(
                "waterActivities", User.getInstance().getWaterActivities(),
                "walkActivities", User.getInstance().getWalkActivities(),
                "hikeActivities", User.getInstance().getHikeActivities(),
                "exerciseActivities", User.getInstance().getExerciseActivities(),
                "totalActivities", User.getInstance().getTotalActivities()
        ).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(NewPostActivity.this, "Activity statistics Successfully updated", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(NewPostActivity.this, "Database update error: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_CODE && resultCode == RESULT_OK){
            if(data != null) {
                imageUri = data.getData();      // Getting the actual image path
                imageIV.setImageURI(imageUri);  // Showing the image
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        user = firebaseAuth.getCurrentUser();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (firebaseAuth != null) {
            firebaseAuth.removeAuthStateListener(authStateListener);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemID = item.getItemId();
        Intent i;

        if (itemID == R.id.action_home_navDraw){

            i = new Intent(NewPostActivity.this, HomePostListActivity.class);
            startActivity(i);
            return true;

        } else if (itemID == R.id.action_add_navDraw){
            return true;

        } else if (itemID == R.id.action_profile_navDraw){

            i = new Intent(NewPostActivity.this, MyStuffActivity.class);
            startActivity(i);
            return true;

        } else if (itemID == R.id.action_settings_navDraw){

            i = new Intent(NewPostActivity.this, SettingsActivity.class);
            startActivity(i);
            return true;

        } else {
            return false;
        }
    }
}