package com.mastersproject.openair;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;
import com.mastersproject.openair.model.Post;
import com.mastersproject.openair.ui.PostRecyclerViewAdapter;
import com.mastersproject.openair.util.User;

import java.util.ArrayList;
import java.util.List;


public class PostListActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser user;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = db.collection("Journal");
    private StorageReference storageReference;
    private List<Post> postList;

    private RecyclerView recyclerView;
    private PostRecyclerViewAdapter postRecyclerViewAdapter;
    private TextView noPostText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_list);

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
            // Going to Add Journal Activity
            if (user != null && firebaseAuth != null) {
                startActivity(new Intent(
                        PostListActivity.this, NewPostActivity.class
                ));
            }
        } else if (itemId == R.id.action_sign_out) {
            // Signing out the user
            if (user != null && firebaseAuth != null) {
                startActivity(new Intent(
                        PostListActivity.this, MainActivity.class
                ));
            }
        }

        return super.onOptionsItemSelected(item);
    }

    // Getting All Posts


    @Override
    protected void onStart() {
        super.onStart();
        collectionReference.whereEqualTo("userId", User.getInstance()
                .getUserId()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                if (!queryDocumentSnapshots.isEmpty()){
                    for (QueryDocumentSnapshot journals : queryDocumentSnapshots) {
                        Post post = journals.toObject(Post.class);
                        postList.add(post);
                    }

                    // Recycler View
                    postRecyclerViewAdapter = new PostRecyclerViewAdapter(
                            PostListActivity.this, postList
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
                Toast.makeText(PostListActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}