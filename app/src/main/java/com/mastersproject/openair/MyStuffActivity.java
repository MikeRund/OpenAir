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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;
import com.mastersproject.openair.model.Post;
import com.mastersproject.openair.ui.BaseActivity;
import com.mastersproject.openair.ui.PostRecyclerViewAdapter;
import com.mastersproject.openair.util.User;

import java.util.ArrayList;
import java.util.List;


public class MyStuffActivity extends BaseActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser user;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = db.collection("Post");
    private StorageReference storageReference;
    private List<Post> postList;

    private RecyclerView recyclerView;
    private PostRecyclerViewAdapter postRecyclerViewAdapter;
    private TextView noPostText;
    private BottomNavigationView bottomNavigationView;

    // Badges
    final private int THRESHOLD_VALUE = 2;
    private int userWaterActivities;
    private int userWalkActivities;
    private int userHikeActivities;
    private int userExerciseActivities;
    private int userTotalActivities;
    private ImageView waterBadge, waterStreak, outdoorBadge, outdoorStreak,
        walkBadge, walkStreak, exerciseBadge, exerciseStreak, hikeBadge, hikeStreak;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mystuff_post_list);

        // Auth
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

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
        waterStreak = findViewById(R.id.waveStreakBadge);
        outdoorBadge = findViewById(R.id.outdoorBadge);
        outdoorStreak = findViewById(R.id.outdoorStreakBadge);
        walkBadge = findViewById(R.id.walkBadge);
        walkStreak = findViewById(R.id.walkStreakBadge);
        exerciseBadge = findViewById(R.id.exerciseBadge);
        exerciseStreak = findViewById(R.id.exerciseStreakBadge);
        hikeBadge = findViewById(R.id.hikeBadge);
        hikeStreak = findViewById(R.id.hikeStreakBadge);
        ImageView[] badges = {waterBadge, waterStreak, outdoorBadge, outdoorStreak, walkBadge, walkStreak,
                                exerciseBadge, exerciseStreak, hikeBadge, hikeStreak};
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

        if (itemId == R.id.action_add) {
            // Going to New Post Activity
            if (user != null && firebaseAuth != null) {
                startActivity(new Intent(
                        MyStuffActivity.this, NewPostActivity.class
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

        } else if (itemID == R.id.action_settings_navDraw){

            i = new Intent(MyStuffActivity.this, SettingsActivity.class);
            startActivity(i);
            return true;

        } else {
            return false;
        }
    }
}