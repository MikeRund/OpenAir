package com.mastersproject.openair;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;
import com.mastersproject.openair.model.Post;
import com.mastersproject.openair.ui.BaseActivity;
import com.mastersproject.openair.ui.PostRecyclerViewAdapter;
import com.mastersproject.openair.util.User;
import com.mastersproject.openair.util.UserDataCallback;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class MyStuffActivity extends BaseActivity {

    // Firebase connection
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser user;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentReference mUserRef;
    private CollectionReference collectionReference = db.collection("Post");
    private StorageReference storageReference;
    private List<Post> postList;

    private RecyclerView recyclerView;
    private PostRecyclerViewAdapter postRecyclerViewAdapter;
    private TextView noPostText;
    private BottomNavigationView bottomNavigationView;

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

    // Badges
    final private int THRESHOLD_VALUE = 2;
    private long userWaterActivities;
    private long userWalkActivities;
    private long userHikeActivities;
    private long userExerciseActivities;
    private long userTotalActivities;
    private ImageView waterBadge, waterStreak, outdoorBadge, outdoorStreak,
        walkBadge, walkStreak, exerciseBadge, exerciseStreak, hikeBadge, hikeStreak;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mystuff_post_list);

        // Auth
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        // Set currentUserId and Username to logged in instance
        if (User.getInstance() != null){
            currentUserId = User.getInstance().getUserId();
            currentUsername = User.getInstance().getUsername();
        }

        // Widgets
        noPostText = findViewById(R.id.listNoPostsMyStuff);
        recyclerView = findViewById(R.id.recyclerViewMyStuff);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Action bar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Profile");
        }

        // Bottom navigation draw
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.action_profile_navDraw);

        // Badges
        waterBadge = findViewById(R.id.waterBadge);
//        waterStreak = findViewById(R.id.waveStreakBadge);
        outdoorBadge = findViewById(R.id.outdoorBadge);
//        outdoorStreak = findViewById(R.id.outdoorStreakBadge);
        walkBadge = findViewById(R.id.walkBadge);
//        walkStreak = findViewById(R.id.walkStreakBadge);
        exerciseBadge = findViewById(R.id.exerciseBadge);
//        exerciseStreak = findViewById(R.id.exerciseStreakBadge);
        hikeBadge = findViewById(R.id.hikeBadge);
//        hikeStreak = findViewById(R.id.hikeStreakBadge);
        ImageView[] badges = {waterBadge, outdoorBadge, walkBadge, exerciseBadge, hikeBadge};
        for (ImageView badge : badges) {
            badge.setImageResource(R.drawable.lock); // Set all badges to locked
        }


        // Posts ArrayList
        postList = new ArrayList<>();
    }

    // Adding the Menu

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.action_edit_profile) {
            // Going to New Post Activity
            if (user != null && firebaseAuth != null) {
                startActivity(new Intent(
                        MyStuffActivity.this, EditProfileActivity.class
                ));
            }
        } else if (itemId == R.id.action_sign_out) {
            // Signing out the user
            if (user != null && firebaseAuth != null) {
                startActivity(new Intent(
                        MyStuffActivity.this, MainActivity.class
                ));
            }
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onStart() {
        super.onStart();

        // Fetch user data and set User singleton fields
//        FetchUserData(new UserDataCallback() {
//            @Override
//            public void onUserDataFetched(String profileImageUrl, long exerciseActivities, long hikeActivities, long walkActivities, long waterActivities, long totalActivities) {
//                User.getInstance().setImageURL(profileImageUrl);
//                User.getInstance().setExerciseActivities(exerciseActivities);
//                User.getInstance().setHikeActivities(hikeActivities);
//                User.getInstance().setWalkActivities(walkActivities);
//                User.getInstance().setWaterActivities(waterActivities);
//                User.getInstance().setTotalActivities(totalActivities);
//
//                // Continue with your badge unlocking logic here
//                userWaterActivities = User.getInstance().getWaterActivities();
//                userWalkActivities = User.getInstance().getWalkActivities();
//                userExerciseActivities = User.getInstance().getExerciseActivities();
//                userHikeActivities = User.getInstance().getHikeActivities();
//                userTotalActivities = User.getInstance().getTotalActivities();
//
//                if (userWaterActivities >= THRESHOLD_VALUE) {
//                    waterBadge.setImageResource(R.drawable.wave);
//                }
//                if (userWalkActivities >= THRESHOLD_VALUE) {
//                    walkBadge.setImageResource(R.drawable.walk);
//                }
//                if (userHikeActivities >= THRESHOLD_VALUE) {
//                    hikeBadge.setImageResource(R.drawable.hiking);
//                }
//                if (userExerciseActivities >= THRESHOLD_VALUE) {
//                    exerciseBadge.setImageResource(R.drawable.shoes);
//                }
//                if (userTotalActivities >= THRESHOLD_VALUE) {
//                    outdoorBadge.setImageResource(R.drawable.mountain);
//                }
//            }
//        });


        // Set the currentUserId
        currentUserId = user != null ? user.getUid() : null;

        collectionReference.whereEqualTo("userId", User.getInstance()
                .getUserId()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                // Getting All Posts

                if (!queryDocumentSnapshots.isEmpty()){
                    for (QueryDocumentSnapshot posts : queryDocumentSnapshots) {
                        Post post = posts.toObject(Post.class);
                        postList.add(post);
                        noPostText.setVisibility(View.INVISIBLE);
                    }

                    Collections.reverse(postList);

                    // Recycler View
                    postRecyclerViewAdapter = new PostRecyclerViewAdapter(
                            MyStuffActivity.this, postList
                    );
                    recyclerView.setAdapter(postRecyclerViewAdapter);
                    postRecyclerViewAdapter.notifyDataSetChanged();

                } else{
                    noPostText.setVisibility(View.VISIBLE);
                }

                // Badge Unlock logic
                userWaterActivities = User.getInstance().getWaterActivities();
                userWalkActivities = User.getInstance().getWalkActivities();
                userExerciseActivities = User.getInstance().getExerciseActivities();
                userHikeActivities = User.getInstance().getHikeActivities();
                userTotalActivities = User.getInstance().getTotalActivities();

                if(userWaterActivities >= THRESHOLD_VALUE) {
                    waterBadge.setImageResource(R.drawable.wave);
                }
                if(userWalkActivities >= THRESHOLD_VALUE) {
                    walkBadge.setImageResource(R.drawable.walk);
                }
                if(userHikeActivities>= THRESHOLD_VALUE) {
                    hikeBadge.setImageResource(R.drawable.hiking);
                }
                if(userExerciseActivities >= THRESHOLD_VALUE) {
                    exerciseBadge.setImageResource(R.drawable.shoes);
                }
                if(userTotalActivities >= THRESHOLD_VALUE) {
                    outdoorBadge.setImageResource(R.drawable.mountain);
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MyStuffActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemID = item.getItemId();
        Intent i;

        if (itemID == R.id.action_home_navDraw){

            i = new Intent(MyStuffActivity.this, HomePostListActivity.class);
            startActivity(i);
            return true;

        } else if (itemID == R.id.action_add_navDraw){

            i = new Intent(MyStuffActivity.this, NewPostActivity.class);
            startActivity(i);
            return true;

        } else if (itemID == R.id.action_profile_navDraw){
            return true;

//        } else if (itemID == R.id.action_settings_navDraw){
//
//            i = new Intent(MyStuffActivity.this, SettingsActivity.class);
//            startActivity(i);
//            return true;

        } else {
            return false;
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
                        Toast.makeText(MyStuffActivity.this, "Total Activities of User: " + fetchedTotalActivities, Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(MyStuffActivity.this, "No such document", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MyStuffActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}