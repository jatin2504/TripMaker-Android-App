package com.example.tripmaker.activites;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tripmaker.R;
import com.example.tripmaker.adapters.ChatAdapter;
import com.example.tripmaker.models.ChatRoom;
import com.example.tripmaker.models.JoinedTrip;
import com.example.tripmaker.models.Message;
import com.example.tripmaker.models.Trip;
import com.example.tripmaker.models.User;
import com.example.tripmaker.utils.Constants;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class ChatActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private LinearLayoutManager layoutManager;
    private FirebaseFirestore db;
    private static final int CAPTURE_IMAGE_CAMERA_CODE = 1000;
    User currentUser;
    List<Message> messages = new ArrayList<>();

    private TextView messageView;
    private TextView titleTV;
    private ImageView chatBackButtonIV;
    public static Trip currentTrip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        if (getIntent() != null && getIntent().getSerializableExtra(Constants.TRIP_OBJ_EXTRA) != null) {
            currentTrip = (Trip) getIntent().getSerializableExtra(Constants.TRIP_OBJ_EXTRA);
        }

        titleTV = findViewById(R.id.groupTitleTv);
        titleTV.setText(currentTrip.getTitle());
        chatBackButtonIV = findViewById(R.id.chatBackImg);

        SharedPreferences mPrefs = getSharedPreferences("mypref", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = mPrefs.getString("userObj", "");
        currentUser = gson.fromJson(json, User.class);

        db = FirebaseFirestore.getInstance();
        messageView = findViewById(R.id.chatMsgTV);

        recyclerView = findViewById(R.id.chatRecyclerView);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);



        getAllMessages();
        recyclerView.scrollToPosition(messages.size()-1);

        findViewById(R.id.sendMsgBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msgTxt = messageView.getText().toString().trim();

                if(TextUtils.isEmpty(msgTxt))   return;

                if (msgTxt != null && msgTxt != "") {
                    Message msgToSend = new Message();
                    msgToSend.setId(String.valueOf(Calendar.getInstance().getTimeInMillis()));
                    msgToSend.setSender(currentUser.getEmail());
                    msgToSend.setText(msgTxt);
                    msgToSend.setTimeStamp(Timestamp.now());
                    sendMessage(msgToSend);
                    messageView.setText("");

                }
            }
        });


        findViewById(R.id.chatCameraIcon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(takePicture, CAPTURE_IMAGE_CAMERA_CODE);
            }
        });

        chatBackButtonIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void sendMessage(Message message) {
        DocumentReference msgRef = db.collection("chats").document(currentTrip.getId());
        msgRef.update("messages", FieldValue.arrayUnion(message)).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void getAllMessages() {
        final DocumentReference docRef = db.collection("chats").document(currentTrip.getId());
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w("Chat", "Listen failed.", e);
                    return;
                }

                if (snapshot != null && snapshot.exists()) {
                    Log.d("Chat", "Current data: " + snapshot.getData());

                    ChatRoom chatRoom = snapshot.toObject(ChatRoom.class);
                    messages = chatRoom.getMessages();
                    Collections.sort(messages, new Message.MessageSort());
                    JoinedTrip joinedTrip = null;
                    for (JoinedTrip jt:currentUser.getTrips()){
                        if(currentTrip.getId().equals(jt.getTripId()))
                            joinedTrip = jt;
                    }
                    if(joinedTrip!=null){
                        messages = filterVisibleMessages(messages,joinedTrip);
                    }

                    mAdapter = new ChatAdapter(messages, currentUser);
                    recyclerView.setAdapter(mAdapter);
                } else {
                    Log.d("Chat", "Current data: null");
                }
            }
        });

    }

    private static List<Message> filterVisibleMessages(List<Message> messages,JoinedTrip joinedTrip) {
        int position = 0;
        for (int i = 0; i < messages.size(); i++) {
            Message m = messages.get(i);
            if(m.getTimeStamp().getSeconds()>joinedTrip.getJoinedDate().getSeconds()){
                position = i;
                break;
            }
        }
        return messages.subList(position,messages.size());
    }


    private void uploadImage(Bitmap imageBitmap) {
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        StorageReference storageReference = firebaseStorage.getReference();
        String path = "images/" + UUID.randomUUID() + ".png";
        final StorageReference imageRepo = storageReference.child(path);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = imageRepo.putBytes(data);

        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {

                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                return imageRepo.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Log.d("Photo Album", "Image Download URL" + task.getResult());
                    String url = task.getResult().toString();
                    Message msgToSend = new Message();
                    msgToSend.setId(String.valueOf(Calendar.getInstance().getTimeInMillis()));
                    msgToSend.setSender(currentUser.getEmail());
                    msgToSend.setImgUrl(url);
                    msgToSend.setTimeStamp(Timestamp.now());
                    sendMessage(msgToSend);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case CAPTURE_IMAGE_CAMERA_CODE:
                if (resultCode == RESULT_OK) {
                    Bundle extras = data.getExtras();
                    Bitmap imageBitmap = (Bitmap) extras.get("data");
                    uploadImage(imageBitmap);
                }
                break;
        }
    }
}
