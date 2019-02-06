package com.example.jayesh123.fotofire;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.dd.processbutton.iml.ActionProcessButton;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import de.hdodenhof.circleimageview.CircleImageView;

public class Create_post extends AppCompatActivity {

    private CircleImageView post_img;
    private EditText post_title;
    private EditText post_details;

    private StorageReference mStorage;

    private ActionProcessButton Post;
    private FirebaseUser mAuth;
    private DatabaseReference mDatabase;
    private Uri uri = null;
    private Uri imageUri = null;
    private static final int GALLERY_INTENT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);

        post_title = findViewById(R.id.post_title);
        post_details = findViewById(R.id.post_details);
        post_img = findViewById(R.id.post_img);
        Post = findViewById(R.id.Post);
        mStorage = FirebaseStorage.getInstance().getReference();


        post_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryintent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryintent.setType("image/*");
                startActivityForResult(galleryintent, GALLERY_INTENT);
            }
        });

        Post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreatePost();
            }
        });

        mAuth = FirebaseAuth.getInstance().getCurrentUser();

    }

    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth;
        updateUI(currentUser);

    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {

        } else {
            Intent i = new Intent(Create_post.this, MainActivity.class);
            startActivity(i);
        }
    }

    private void CreatePost() {
        final String Title = post_title.getText().toString().trim();
        final String Details = post_details.getText().toString().trim();
        final String uid = mAuth.getUid();
        if(Title == null || Details == null || uri == null){
            Toast.makeText(Create_post.this,"Please fill all field !!!",Toast.LENGTH_SHORT).show();
            return;
        }
        Post.setProgress(1);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Post").push();
        StorageReference mref = mStorage.child("Post/" + uri.getLastPathSegment());
        mref.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                String downloaduri = taskSnapshot.getDownloadUrl().toString();
                mDatabase.child("uid").setValue(uid);
                mDatabase.child("title").setValue(Title);
                mDatabase.child("detail").setValue(Details);
                mDatabase.child("image").setValue(downloaduri);
                Toast.makeText(Create_post.this, "Upload successful...", Toast.LENGTH_LONG).show();
                Post.setProgress(100);
                Intent i = new Intent(Create_post.this, home_page.class);
                startActivity(i);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Post.setProgress(-1);
                Toast.makeText(Create_post.this, "Upload Unsuccessful...", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_INTENT && resultCode == RESULT_OK) {

            imageUri = data.getData();
            CropImage.activity(imageUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1, 1)
                    .start(this);
            Glide.with(this).load(imageUri).into(post_img);
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                imageUri = result.getUri();
                uri = imageUri;
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }
}
