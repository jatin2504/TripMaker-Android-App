package com.example.tripmaker.activites;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.tripmaker.R;
import com.example.tripmaker.adapters.ChatAdapter;
import com.example.tripmaker.models.ChatRoom;
import com.example.tripmaker.models.Message;
import com.example.tripmaker.models.Trip;
import com.example.tripmaker.models.User;
import com.example.tripmaker.utils.Constants;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ChatActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private FirebaseFirestore db;
    User currentUser;
    List<Message> messages = new ArrayList<>();

    private TextView messageView;
    private TextView titleTV;
    Trip currentTrip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        if (getIntent() != null && getIntent().getSerializableExtra(Constants.TRIP_OBJ_EXTRA) != null) {
            currentTrip = (Trip) getIntent().getSerializableExtra(Constants.TRIP_OBJ_EXTRA);
        }

        titleTV = findViewById(R.id.groupTitleTv);
        titleTV.setText(currentTrip.getTitle());

        SharedPreferences mPrefs = getSharedPreferences("mypref", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = mPrefs.getString("userObj", "");
        currentUser = gson.fromJson(json, User.class);

        db = FirebaseFirestore.getInstance();
        messageView = findViewById(R.id.chatMsgTV);

        recyclerView = findViewById(R.id.chatRecyclerView);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


        getAllMessages();

        findViewById(R.id.sendMsgBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msgTxt = messageView.getText().toString().trim();
                if (msgTxt != null && msgTxt != "") {
                    Message msgToSend = new Message();
                    msgToSend.setId(String.valueOf(Calendar.getInstance().getTimeInMillis()));
                    msgToSend.setSender(currentUser.getEmail());
                    msgToSend.setText(msgTxt);
                    msgToSend.setTimeStamp(new Timestamp(new Date()));

                    //TODO: Replace Hard Coded Trip ID
                    DocumentReference msgRef = db.collection("chats").document(currentTrip.getId());
                    msgRef.update("messages", FieldValue.arrayUnion(msgToSend)).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            e.printStackTrace();
                        }
                    });
                    messageView.setText("");

                }
            }
        });


        findViewById(R.id.chatCameraIcon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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
                    mAdapter = new ChatAdapter(messages, currentUser);
                    recyclerView.setAdapter(mAdapter);
                } else {
                    Log.d("Chat", "Current data: null");
                }
            }
        });

    }
}
