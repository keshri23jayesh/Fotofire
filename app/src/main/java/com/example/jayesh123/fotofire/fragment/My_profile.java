package com.example.jayesh123.fotofire.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.jayesh123.fotofire.adapter.My_Profile_Adapter;
import com.example.jayesh123.fotofire.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class My_profile extends Fragment {

    RecyclerView recyclerView;

    ProgressBar progressBar;

    private static final String TAG = "MyActivity";
    private Activity mActivity;
    String uid;
    String Profile_picture, User_name;

    private ArrayList<String> detail = new ArrayList<>();
    private ArrayList<String> image = new ArrayList<>();
    private ArrayList<String> title = new ArrayList<>();


    Context context;

    private DatabaseReference databserefrence, Username;
    private FirebaseUser mAuth;

    public void getContextMain(Context context) {
        this.context = context;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        mActivity = getActivity();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mActivity = null;
    }

    private void doAction() {
        if (mActivity == null)
            return;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mAuth = FirebaseAuth.getInstance().getCurrentUser();
        uid = mAuth.getUid();
        Username = FirebaseDatabase.getInstance().getReference().child("UserLists").child(uid);
        databserefrence = FirebaseDatabase.getInstance().getReference().child("Post");
        new userdetails().execute(Username);
        progressBar = (ProgressBar) getView().findViewById(R.id.Progresssbar);
        recyclerView = (RecyclerView) getView().findViewById(R.id.fragment_my_profile);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my_profile, container, false);
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
                            Profile_picture = child.getValue().toString();
                        if (child.getKey().equalsIgnoreCase("username"))
                            User_name = child.getValue().toString();
                    }
                    Log.d(TAG, "Profile_picture: " + Profile_picture);
                    Log.d(TAG, "User_name: " + User_name);
                    new thisuser().execute(databserefrence);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Code
                }
            });
            return null;
        }
    }

    public class thisuser extends AsyncTask<DatabaseReference, Void, Void> {
        @Override
        protected Void doInBackground(DatabaseReference... post_id) {
            final DatabaseReference post_ids = post_id[0];
            post_ids.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        String u;
                        u = String.valueOf(child.child("uid").getValue());
                        if (uid.equals(u)) {
                            String s, t, r;
                            t = String.valueOf(child.child("image").getValue());
                            image.add(t);

                        }
                    }

                    String s = String.valueOf(image.size());
                    Log.d(TAG, "onChange: " + s);
                    progressBar.setVisibility(View.GONE);
                    My_Profile_Adapter My_Profile_Adapter = new My_Profile_Adapter(context, User_name, Profile_picture, image);
                    if (mActivity == null) {
                        return;
                    }
                    recyclerView.setAdapter(My_Profile_Adapter);
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
