package com.example.jayesh123.fotofire.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.jayesh123.fotofire.R;
import com.example.jayesh123.fotofire.adapter.All_Post_Adapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class index_page extends Fragment {

    private RecyclerView recyclerView;
    private ProgressBar progressBar;

    private static final String TAG = "MyActivity";

    private ArrayList<String> profile_pic = new ArrayList<>();
    private ArrayList<String> user_name = new ArrayList<>();

    private ArrayList<String> user_id = new ArrayList<>();
    private ArrayList<String> detail = new ArrayList<>();
    private ArrayList<String> image = new ArrayList<>();
    private ArrayList<String> title = new ArrayList<>();


    private DatabaseReference databserefrence, Username;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_index_page, container, false);

        progressBar = view.findViewById(R.id.Progressbar);
        recyclerView = view.findViewById(R.id.fragment_index_page);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Username = FirebaseDatabase.getInstance().getReference().child("UserLists");
        databserefrence = FirebaseDatabase.getInstance().getReference().child("Post");

        new fetch().execute(databserefrence);
    }

    public class fetch extends AsyncTask<DatabaseReference, Void, Void> {
        @Override
        protected Void doInBackground(DatabaseReference... post_id) {
            final DatabaseReference post_ids = post_id[0];
            post_ids.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        String s, t, r, u;
                        s = String.valueOf(child.child("title").getValue());
                        detail.add(s);
                        t = String.valueOf(child.child("image").getValue());
                        image.add(t);
                        r = String.valueOf(child.child("detail").getValue());
                        title.add(r);
                        u = String.valueOf(child.child("uid").getValue());
                        user_id.add(u);

                    }

                    for (int i = 0; i < user_id.size(); i++) {
                        new userdetails().execute(Username.child(user_id.get(i)));
                    }


                    String s = String.valueOf(user_id.size());
                    Log.d(TAG, "onDataChange: " + s);
                    String p = String.valueOf(user_name.size());
                    Log.d(TAG, "onData: " + p);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Code
                }
            });

            return null;
        }
    }

    public class userdetails extends AsyncTask<DatabaseReference, Void, Void> {
        @Override
        protected Void doInBackground(DatabaseReference... post_id) {
            final DatabaseReference post_ids = post_id[0];
            post_ids.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        if (child.getKey().equalsIgnoreCase("image"))
                            profile_pic.add(child.getValue().toString());

                        if (child.getKey().equalsIgnoreCase("username"))
                            user_name.add(child.getValue().toString());

                    }

                    progressBar.setVisibility(View.GONE);

                    recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),1));
                    All_Post_Adapter AllPostAdapter = new All_Post_Adapter(getActivity(), user_name, profile_pic, detail, image, title);
                    recyclerView.setAdapter(AllPostAdapter);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Code
                }
            });
            return null;
        }
    }

}
