package com.example.tripmaker.activites;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.tripmaker.R;
import com.example.tripmaker.adapters.ChatAdapter;
import com.example.tripmaker.models.Message;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        recyclerView = (RecyclerView) findViewById(R.id.chatRecyclerView);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        List<Message> messages = new ArrayList<>();
        messages.add(new Message("1","hi",""));
        messages.add(new Message("2","","https://rukminim1.flixcart.com/image/416/416/av-media/movies/e/d/v/gangajal-original-imadb44ctqf58efz.jpeg?q=70"));
        messages.add(new Message("3","hi",""));
        messages.add(new Message("4","","https://rukminim1.flixcart.com/image/416/416/av-media/movies/e/d/v/gangajal-original-imadb44ctqf58efz.jpeg?q=70"));
        messages.add(new Message("1","tuzi aai ghal. next image",""));
        messages.add(new Message("2","tu ghal na mc",""));
        messages.add(new Message("3","choko ghe",""));
        messages.add(new Message("4","","https://rukminim1.flixcart.com/image/416/416/av-media/movies/e/d/v/gangajal-original-imadb44ctqf58efz.jpeg?q=70"));
        messages.add(new Message("1","","https://rukminim1.flixcart.com/image/416/416/av-media/movies/e/d/v/gangajal-original-imadb44ctqf58efz.jpeg?q=70"));
        messages.add(new Message("2","pucchi chatya",""));
        messages.add(new Message("3","yed gandya",""));
        messages.add(new Message("4","baylya bhokachya",""));
        mAdapter = new ChatAdapter(messages);
        recyclerView.setAdapter(mAdapter);
    }
}
