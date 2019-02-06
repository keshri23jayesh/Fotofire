package com.example.jayesh123.fotofire;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

public class ProfileSetup extends AppCompatActivity {
    private StorageReference mStorage;
    public ProgressDialog mProgressDialog;
    EditText username, mobileno;
    Button setok, Firstpost,GOTO;
    private FirebaseUser mAuth;
    private DatabaseReference mDatabase;
    private ImageButton propic;
    private Uri uri = null;
    private Uri imageUri = null;
    private static final int GALLERY_INTENT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_setup);
        username = (EditText) findViewById(R.id.username);
        mobileno = (EditText) findViewById(R.id.mobileno);
        propic = (ImageButton) findViewById(R.id.propic);
        setok = (Button) findViewById(R.id.setok);
        mStorage = FirebaseStorage.getInstance().getReference();
        GOTO = (Button) findViewById(R.id.GOTO);

        GOTO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ProfileSetup.this, home_page.class);
                startActivity(i);
            }
        });

        propic.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent galleryintent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryintent.setType("image/*");
                startActivityForResult(galleryintent, GALLERY_INTENT);
            }
        });
        setok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Accountsetup();
            }
        });

        // [START initialize_auth]
        mAuth = FirebaseAuth.getInstance().getCurrentUser();
        // [END initialize_auth]
    }


    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }


    private void Accountsetup()
    {
        final String uname = username.getText().toString().trim();
        final String mobile = mobileno.getText().toString().trim();
        final String uid = mAuth.getUid();
        showProgressDialog();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("UserLists");
        StorageReference mref = mStorage.child("profilepic/" + uri.getLastPathSegment());
        mref.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                String downloaduri = taskSnapshot.getDownloadUrl().toString();
                mDatabase.child(uid).child("image").setValue(downloaduri);
                mDatabase.child(uid).child("phoneno").setValue(mobile);
                mDatabase.child(uid).child("username").setValue(uname);
                Toast.makeText(ProfileSetup.this, "Upload successful...", Toast.LENGTH_LONG).show();
                hideProgressDialog();
                Intent i = new Intent(ProfileSetup.this, Create_post.class);
                startActivity(i);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                hideProgressDialog();
                Toast.makeText(ProfileSetup.this, "Upload Unsuccessful...", Toast.LENGTH_LONG).show();
            }
        });
    }


    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth;
        updateUI(currentUser);
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            int somthing = 0;

        } else {
            Intent i = new Intent(ProfileSetup.this, MainActivity.class);
            startActivity(i);
        }
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


