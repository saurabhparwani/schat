package com.example.sonu3239.lepitchatapp;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class Profile extends AppCompatActivity {
    CircleImageView circleImageView;
    TextView name,status;
    Button btn;
    String uid,currentid;
    String S="not_friends";
    String requesttype;
    String n;
    DatabaseReference databaseReference,myref,notification,dataref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        circleImageView=findViewById(R.id.profilepic);
        name=findViewById(R.id.profileusername);
        status=findViewById(R.id.profilestatus);
        btn=findViewById(R.id.button);
        uid=getIntent().getExtras().getString("uid");
        dataref=FirebaseDatabase.getInstance().getReference().child("Users");
        dataref.child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                 n =dataSnapshot.child("name").getValue().toString();
                String s=dataSnapshot.child("status").getValue().toString();
                String image=dataSnapshot.child("image").getValue().toString();
                name.setText(n);
                status.setText(s);
                Picasso.get().load(image).
                        resize(200,200).placeholder(R.drawable.avatar).into(circleImageView);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        currentid= FirebaseAuth.getInstance().getCurrentUser().getUid();
        databaseReference= FirebaseDatabase.getInstance().getReference().child("FriendRequets");
        notification= FirebaseDatabase.getInstance().getReference().child("Notifications");
        databaseReference.child(currentid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(uid))
                {
                    requesttype=dataSnapshot.child(uid).child("request_type").getValue().toString();
                    if(requesttype.equals("sent"))
                    {  S="request_sent";btn.setText("Cancel Friend Request"); }
                    else if(requesttype.equals("received"))
                    {   S="request_received";btn.setText("Accept Friend Request"); }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        myref=FirebaseDatabase.getInstance().getReference().child("Friends");
        myref.child(currentid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(uid))
                {
                    S="is_friend";btn.setText("UnFriend "+n);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(S.equals("not_friends"))
                    sendfriendrequest();
                else if(S.equals("request_sent"))
                    cancelfriendrequest();
                else if(S.equals("is_friend"))
                    unfriend();
            }
        });
    }

    private void unfriend() {
        myref.child(currentid).child(uid).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                myref.child(uid).child(currentid).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        btn.setText("Send Friend Request");
                        S="not_friends";
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),"Failed To Unfriend this Person",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void cancelfriendrequest() {
        databaseReference.child(currentid).child(uid).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                databaseReference.child(uid).child(currentid).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        btn.setText("Send Friend Request");
                        S="not_friends";
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),"Failed to Cancel Request",Toast.LENGTH_SHORT).show();
                btn.setText("Cancel Friend Request");
            }
        });
    }

    private void sendfriendrequest() {
        databaseReference.child(currentid).child(uid).child("request_type").setValue("sent").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
               databaseReference.child(uid).child(currentid).child("request_type").setValue("received").addOnCompleteListener(new OnCompleteListener<Void>() {
                   @Override
                   public void onComplete(@NonNull Task<Void> task) {
                       HashMap<String,String> notificationmap = new HashMap<>();
                       notificationmap.put("from",currentid);
                       notificationmap.put("type","request");
                       notification.child(uid).push().setValue(notificationmap).addOnCompleteListener(new OnCompleteListener<Void>() {
                           @Override
                           public void onComplete(@NonNull Task<Void> task) {
                               Toast.makeText(getApplicationContext(),"Friend Request Sent",Toast.LENGTH_SHORT).show();
                               btn.setText("Cancel Friend Request");
                               S="request_sent";
                           }
                       });

                   }
               }) ;
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),"Failed to Send Request",Toast.LENGTH_SHORT).show();
                btn.setText("Send Friend Request");
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        dataref.child(currentid).child("online").setValue("True");
    }

    @Override
    protected void onStop() {
        super.onStop();
        dataref.child(currentid).child("online").setValue("True");
    }
}
