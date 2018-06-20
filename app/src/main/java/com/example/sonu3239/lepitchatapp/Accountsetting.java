package com.example.sonu3239.lepitchatapp;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sonu3239.lepitchatapp.Models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;

public class Accountsetting extends AppCompatActivity {
  CircleImageView profilepic;
  TextView useraname,userstatus;
  Button statuschange,imagechange;
  DatabaseReference databaseReference;
  String uid;
  User user;
  ProgressDialog progressDialog;
  private  static int Request_Code=100;
    byte[] thumb_data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accountsetting);
        profilepic=findViewById(R.id.profilepic);
        useraname=findViewById(R.id.profileusername);
        userstatus=findViewById(R.id.profilestatus);
        useraname.setVisibility(View.GONE);
        userstatus.setVisibility(View.GONE);
        imagechange=findViewById(R.id.changeimage);
        progressDialog=new ProgressDialog(Accountsetting.this);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setTitle("Updating Profile Pic....");
        progressDialog.setMessage("Please Wait a While");
        statuschange=findViewById(R.id.changestatus);
        uid= FirebaseAuth.getInstance().getCurrentUser().getUid();
        databaseReference= FirebaseDatabase.getInstance().getReference().child("Users").child(uid);
        databaseReference.keepSynced(true);
        imagechange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), Request_Code);
            }
        });
        statuschange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder=new AlertDialog.Builder(Accountsetting.this);
                View view1=getLayoutInflater().inflate(R.layout.statuschange,null);
                builder.setCancelable(false);
                builder.setTitle("Change Status..");
                builder.setMessage("Change Your Status Here");
                builder.setView(view1);
                final EditText editText=view1.findViewById(R.id.status);
                editText.setText(user.getStatus());
                builder.setNegativeButton("Cancel",null);
                builder.setPositiveButton("Change", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String status=editText.getText().toString();
                        if(!TextUtils.isEmpty(status))
                        {
                            databaseReference.child("status").setValue(status).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(getApplicationContext(),"Status Changed Successfully..",Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                });
                AlertDialog alertDialog=builder.create();
                alertDialog.show();
            }
        });

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user=dataSnapshot.getValue(User.class);
                if(user.getImage().equals("Default"))
                    profilepic.setImageDrawable(getResources().getDrawable(R.drawable.avatar));
                else
                  Picasso.get().load(user.getImage()).resize(150,150).into(profilepic);
                useraname.setText(user.getName());
                userstatus.setText(user.getStatus());
                userstatus.setVisibility(View.VISIBLE);
                useraname.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==Request_Code&&resultCode==RESULT_OK&&data.getData()!=null)
        {
            Uri uri=data.getData();
            CropImage.activity(uri).setAspectRatio(1,1)
                    .start(this);

        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                progressDialog.show();
                Uri resultUri = result.getUri();
                File thumb_file=new File(resultUri.getPath());
                try {
                    Bitmap thumb_bitmap = new Compressor(this).setMaxWidth(640).setMaxHeight(480)
                            .setQuality(75).compressToBitmap(thumb_file);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    thumb_bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    thumb_data = baos.toByteArray();

                } catch (IOException e) {
                    e.printStackTrace();
                }
                StorageReference storageReference= FirebaseStorage.getInstance().getReference("Profile_pics").child(uid);
                final StorageReference storageReferencethumb= FirebaseStorage.getInstance().getReference("Profile_pics").child("Thumb").child(uid);
                storageReference.putFile(resultUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        final  String uri=taskSnapshot.getDownloadUrl().toString();
                        UploadTask uploadTask = storageReferencethumb.putBytes(thumb_data);
                        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                String thumb_uri=taskSnapshot.getDownloadUrl().toString();
                                databaseReference.child("thumbnail").setValue(thumb_uri);
                                databaseReference.child("image").setValue(uri).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        progressDialog.cancel();
                                        Toast.makeText(getApplicationContext(),"Profile Pic Updated Successfully",Toast.LENGTH_SHORT).show();

                                    }
                                });
                            }
                        });

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.cancel();
                     Toast.makeText(getApplicationContext(),"Profile Could not Updated",Toast.LENGTH_SHORT).show();
                    }
                });
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        databaseReference.child("online").setValue("True");
    }

    @Override
    protected void onStop() {
        super.onStop();
        databaseReference.child("online").setValue("True");

    }
}
