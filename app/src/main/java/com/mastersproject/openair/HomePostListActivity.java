package com.mastersproject.openair;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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


public class HomePostListActivity extends BaseActivity {

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_post_list);

        // Auth
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        // Widgets
        noPostText = findViewById(R.id.listNoPosts);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Posts ArrayList
        postList = new ArrayList<>();

        // Action bar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Home");
        }

        // Nav draw
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.action_home_navDraw);

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
            // Going to Add Post Activity
            if (user != null && firebaseAuth != null) {
                startActivity(new Intent(
                        HomePostListActivity.this, NewPostActivity.class
                ));
            }
        } else if (itemId == R.id.action_sign_out) {
            // Signing out the user
            if (user != null && firebaseAuth != null) {
                startActivity(new Intent(
                        HomePostListActivity.this, MainActivity.class
                ));
            }
        }

        return super.onOptionsItemSelected(item);
    }

    // Getting All Posts


    @Override
    protected void onStart() {
        super.onStart();
        collectionReference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                if (!queryDocumentSnapshots.isEmpty()){
                    for (QueryDocumentSnapshot posts : queryDocumentSnapshots) {
                        Post post = posts.toObject(Post.class);
                        postList.add(post);
                        noPostText.setVisibility(View.INVISIBLE);
                    }

                    // Recycler View
                    postRecyclerViewAdapter = new PostRecyclerViewAdapter(
                            HomePostListActivity.this, postList
                    );
                    recyclerView.setAdapter(postRecyclerViewAdapter);
                    postRecyclerViewAdapter.notifyDataSetChanged();

                } else{
                    noPostText.setVisibility(View.VISIBLE);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(HomePostListActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemID = item.getItemId();
        Intent i;

        if (itemID == R.id.action_home_navDraw){
            return true;

        } else if (itemID == R.id.action_add_navDraw){

            i = new Intent(HomePostListActivity.this, NewPostActivity.class);
            startActivity(i);
            return true;

        } else if (itemID == R.id.action_profile_navDraw){

            i = new Intent(HomePostListActivity.this, MyStuffActivity.class);
            startActivity(i);
            return true;

        } else if (itemID == R.id.action_settings_navDraw){

            i = new Intent(HomePostListActivity.this, SettingsActivity.class);
            startActivity(i);
            return true;

        } else {
            return false;
        }
    }
}