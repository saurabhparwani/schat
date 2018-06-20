package com.example.sonu3239.lepitchatapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sonu3239.lepitchatapp.Models.Message;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;

public class Chatuser extends AppCompatActivity {
      Toolbar toolbar;
      DatabaseReference databaseReference,myref;
      String user,currentuser;
      MaterialEditText smessage;
      ImageButton sadd,ssend;
      CircleImageView imageView;
      TextView textView,textView1;
      ListView listView;
      List<Message> list=new ArrayList<>();
      MessageAdapter messageAdapter;
      ListAdapter listAdapter;
      DatabaseReference ref;
      private static final int numerofmessage=10;
      private int times=1;
      private SwipeRefreshLayout swipeRefreshLayout;
    private  static int Request_Code=100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatuser);
        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);
        user=getIntent().getExtras().getString("uid");
        currentuser= FirebaseAuth.getInstance().getCurrentUser().getUid();
        LayoutInflater inflater=this.getLayoutInflater();
        smessage=findViewById(R.id.message);
        sadd=findViewById(R.id.add);
        ssend=findViewById(R.id.send);
        swipeRefreshLayout=findViewById(R.id.swipelayout);
        listView=findViewById(R.id.messages);
        listAdapter=new ListAdapter(this,R.layout.message,list);
        listView.setAdapter(listAdapter);
        View view=inflater.inflate(R.layout.customchatview,null);
        imageView=view.findViewById(R.id.chatpic);
        textView=view.findViewById(R.id.chatname);
        textView1=view.findViewById(R.id.chatonline);
        actionBar.setCustomView(view);
        databaseReference= FirebaseDatabase.getInstance().getReference().child("Users").child(user);
        myref=FirebaseDatabase.getInstance().getReference();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name=dataSnapshot.child("name").getValue().toString();
                String image=dataSnapshot.child("thumbnail").getValue().toString();
                String online=dataSnapshot.child("online").getValue().toString();
                textView.setText(name);
                Picasso.get().load(image).placeholder(R.drawable.avatar).networkPolicy(NetworkPolicy.OFFLINE).into(imageView);
                if(online.equals("True"))
                {
                    textView1.setText("Online");
                    textView1.setVisibility(View.VISIBLE);
                }
                else
                {
                    textView1.setText(timeago(online));
                    textView1.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        ref=myref.child("Messages").child(user).child(currentuser);
        loadMessage();
        ssend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage();
            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                times++;
                list.clear();
                loadMessage();
            }
        });
       sadd.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               CharSequence options[]=new CharSequence[]{"Image","Video","Other"};
               AlertDialog.Builder builder=new AlertDialog.Builder(Chatuser.this);
               builder.setTitle("Select Item to Send");
               builder.setItems(options, new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialogInterface, int i) {
                       switch (i)
                       {
                           case 0:
                               Intent intent = new Intent();
                               intent.setType("image/*");
                               intent.setAction(Intent.ACTION_GET_CONTENT);
                               startActivityForResult(Intent.createChooser(intent, "Select Picture"), Request_Code);
                               break;
                           case 1:
                               Toast.makeText(getApplicationContext(),"Video",Toast.LENGTH_SHORT).show();
                               break;
                           case 2:
                               Toast.makeText(getApplicationContext(),"Other",Toast.LENGTH_SHORT).show();
                               break;
                       }
                   }
               });
               builder.create().show();
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
                Uri resultUri = result.getUri();
                StorageReference storageReference=FirebaseStorage.getInstance().getReference().child("Message_Images").child(resultUri.toString());
                storageReference.putFile(resultUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        String sendmessage=taskSnapshot.getDownloadUrl().toString();
                        SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd G 'at' HH:mm:ss");
                        Date date = new Date();
                        final Message message=new Message("image",sendmessage,format.format(date),"False",currentuser);
                        String one="Messages/"+currentuser+"/"+user;
                        String two="Messages/"+user+"/"+currentuser;
                        String chat1="Chat/"+currentuser+"/"+user;
                        String chat2="Chat/"+user+"/"+currentuser;
                        DatabaseReference ref=myref.child("Messages").child(currentuser).child(user).push();
                        String key=ref.getKey();
                        final Map data=new HashMap();
                        data.put("lastmessgae",sendmessage);
                        data.put("time", ServerValue.TIMESTAMP);
                        smessage.setText("");
                        Map map=new HashMap<>();
                        map.put(one+"/"+key,message);
                        map.put(two+"/"+key,message);
                        final Map mymap=new HashMap<>();
                        mymap.put(chat1,data);
                        mymap.put(chat2,data);
                        myref.updateChildren(map, new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                smessage.setText("");
                                myref.updateChildren(mymap, new DatabaseReference.CompletionListener() {
                                    @Override
                                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                                    }
                                });
                            }
                        });

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(),"Message Could not send",Toast.LENGTH_SHORT).show();
                    }
                });

            }
        }
    }

    private void loadMessage() {
        Query query=ref.limitToLast(times*numerofmessage);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                list.clear();
                for(DataSnapshot p:dataSnapshot.getChildren())
                {
                    Message message=p.getValue(Message.class);
                    list.add(message);
                    listAdapter.notifyDataSetChanged();
                    listView.smoothScrollToPosition(list.size()-1);
                    swipeRefreshLayout.setRefreshing(false);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void sendMessage() {
        String sendmessage=smessage.getText().toString();
        if(!TextUtils.isEmpty(sendmessage))
        {
            SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd G 'at' HH:mm:ss");
            Date date = new Date();
            final Message message=new Message("text",sendmessage,format.format(date),"False",currentuser);
            String one="Messages/"+currentuser+"/"+user;
            String two="Messages/"+user+"/"+currentuser;
            String chat1="Chat/"+currentuser+"/"+user;
            String chat2="Chat/"+user+"/"+currentuser;
            DatabaseReference ref=myref.child("Messages").child(currentuser).child(user).push();
            String key=ref.getKey();
            final Map data=new HashMap();
            data.put("lastmessgae",sendmessage);
            data.put("time", ServerValue.TIMESTAMP);
            smessage.setText("");
            Map map=new HashMap<>();
            map.put(one+"/"+key,message);
            map.put(two+"/"+key,message);
            final Map mymap=new HashMap<>();
            mymap.put(chat1,data);
            mymap.put(chat2,data);
            myref.updateChildren(map, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                    smessage.setText("");
                    myref.updateChildren(mymap, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                        }
                    });
                }
            });

        }
    }

    private String timeago(String online) {
        try
        {
            SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd G 'at' HH:mm:ss");
            Date past = format.parse(online);
            Date now = new Date();
            long seconds=TimeUnit.MILLISECONDS.toSeconds(now.getTime() - past.getTime());
            long minutes= TimeUnit.MILLISECONDS.toMinutes(now.getTime() - past.getTime());
            long hours=TimeUnit.MILLISECONDS.toHours(now.getTime() - past.getTime());
            long days=TimeUnit.MILLISECONDS.toDays(now.getTime() - past.getTime());
            if(seconds<60)
            {
                return "just now";
            }
            else if(minutes<60)
            {
                return  minutes+" minutes ago";
            }
            else if(hours<24)
            {
                return hours+" hours ago";
            }
            else
            {
                return days+" days ago";
            }
        }
        catch (Exception j){
            j.printStackTrace();
        }
        return  null;
    }

}
