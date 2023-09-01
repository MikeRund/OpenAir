package com.mastersproject.openair;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mastersproject.openair.ui.BaseActivity;
import com.mastersproject.openair.util.User;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfileActivity extends BaseActivity {

    private static final int GALLERY_CODE = 1;
    private CircleImageView profileImage;
    private EditText usernameET, descriptionET;
    private Button saveBTN;

    // User ID & Username
    private String currentUserId;
    private String currentUsername;
    private String currentDescription;
    private String currentImageURL;
    private Uri imageURI;

    // Firebase Auth
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser mUser;

    // Firebase Connection
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private StorageReference storageReference;
    private CollectionReference collectionReference = db.collection("Users");
    private DocumentReference mUserRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        // Initializing
        storageReference = FirebaseStorage.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        profileImage = findViewById(R.id.profile_image);
        usernameET = findViewById(R.id.usernameET);
        descriptionET = findViewById(R.id.descriptionET);
        saveBTN = findViewById(R.id.saveBTN);




        // Action bar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Edit Profile");
        }

//         Set to existing data
        if (User.getInstance() != null){
            currentUserId = User.getInstance().getUserId();

//            currentUsername = User.getInstance().getUsername();
//            currentDescription = collectionReference
//            currentImageURL = User.getInstance().getImageURL();
//            System.out.println("Url: " + currentImageURL);
//
//            usernameET.setText(currentUsername);
//            descriptionET.setText(currentDescription);
//            Glide.with(this).load(currentImageURL).into(profileImage);


        }

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                mUser = firebaseAuth.getCurrentUser();
                if (mUser != null) {

                } else {

                }
            }
        };

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Getting image from gallery
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, GALLERY_CODE);
            }
        });

        saveBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveChanges();
            }
        });


    }

    private void SaveChanges() {
        final String username = usernameET.getText().toString().trim();
        final String description = descriptionET.getText().toString().trim();

        if(!TextUtils.isEmpty(username) && !TextUtils.isEmpty(description) && imageURI != null){

            // Saving the path of the image in Storage Firebase
            // .. /profile_images/my_profile_pic.png

            final StorageReference filepath = storageReference
                    .child("profile_images")
                    .child("my_profile_pic" + User.getInstance().getUsername() + Timestamp.now().getSeconds());

            // Uploading the  image
            filepath.putFile(imageURI)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String imageURL = uri.toString();

                                    // Saving the data to User
                                    User.getInstance().setUsername(username);
                                    User.getInstance().setDescription(description);
                                    User.getInstance().setImageURL(imageURL);

                                    // Update the ImageView
                                    Glide.with(EditProfileActivity.this).load(imageURL).into(profileImage);

                                    // Updating data
                                    UpdateUserFirestoreData();
                                    startActivity(new Intent(EditProfileActivity.this, HomePostListActivity.class));
                                    finish();

                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(EditProfileActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
        } else {
            Toast.makeText(this, "Please complete all fields", Toast.LENGTH_SHORT).show();
        }
    }

    private void UpdateUserFirestoreData() {
        DocumentReference userDocRef = db.collection("Users").document(currentUserId);

        // Update User data
        userDocRef.update(
                "username", User.getInstance().getUsername(),
                "description", User.getInstance().getDescription(),
                "imageURL", User.getInstance().getImageURL()
        ).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(EditProfileActivity.this, "Profile Updated Successfully", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(EditProfileActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Toast.makeText(this, "OnActResultActivated", Toast.LENGTH_SHORT).show();

        if (requestCode == GALLERY_CODE && resultCode == RESULT_OK){
            if(data != null) {
                imageURI = data.getData();      // Getting the actual image path
                profileImage.setImageURI(imageURI);  // Showing the image

            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mUser = firebaseAuth.getCurrentUser();
        firebaseAuth.addAuthStateListener(authStateListener);

        mUserRef = db.collection("Users").document(currentUserId);
        mUserRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot.exists()){
                        // Use the data from the document snapshot
                        currentUsername = documentSnapshot.getString("username");
                        currentDescription = documentSnapshot.getString("description");
                        currentImageURL = documentSnapshot.getString("imageURL");
                        Toast.makeText(EditProfileActivity.this, "Url: " + currentImageURL, Toast.LENGTH_SHORT).show();

                        // Use the retrieved data here
                        Glide.with(EditProfileActivity.this).load(currentImageURL).into(profileImage);
                        usernameET.setText(currentUsername);
                        descriptionET.setText(currentDescription);
                    }
                }
            }
        });


//        collectionReference.whereEqualTo("userId", User.getInstance().getUserId()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//            @Override
//            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//
//                // Loading exisiting data
//                if(!queryDocumentSnapshots.isEmpty()) {
//                    currentUsername = User.getInstance().getUsername();
//                    currentDescription = User.getInstance().getDescription();
//                    currentImageURL = User.getInstance().getImageURL();
//                    Toast.makeText(EditProfileActivity.this, "Url: " + currentImageURL, Toast.LENGTH_SHORT).show();
//
//                    // Use the retrieved data here
//                    Glide.with(EditProfileActivity.this).load(currentImageURL).into(profileImage);
//                    usernameET.setText(currentUsername);
//                    descriptionET.setText(currentDescription);
//                }
//            }
//        });
    }


    @Override
    protected void onStop() {
        super.onStop();
        if (firebaseAuth != null) {
            firebaseAuth.removeAuthStateListener(authStateListener);
        }
    }
}