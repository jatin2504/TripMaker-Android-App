package com.example.tripmaker.adapters;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tripmaker.R;
import com.example.tripmaker.models.Message;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<Message> messages;
    private static int TYPE_SENT_TEXT = 1;
    private static int TYPE_SENT_IMAGE = 2;
    private static int TYPE_RECEIVED_TEXT = 3;
    private static int TYPE_RECEIVED_IMAGE = 4;

    public ChatAdapter(List<Message> messages) {
        this.messages = messages;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;

        if (viewType == TYPE_SENT_TEXT) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_message_sent_layout, parent, false);
            return new SentTextViewHolder(view);

        } else if(viewType == TYPE_RECEIVED_TEXT) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_message_received_layout, parent, false);
            return new ReceivedTextViewHolder(view);
        }else if(viewType == TYPE_SENT_IMAGE) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_image_sent_layout, parent, false);
            return new SentImageViewHolder(view);
        }else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_image_received_layout, parent, false);
            return new ReceivedImageViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if (getItemViewType(position) == TYPE_RECEIVED_TEXT) {
            ((ReceivedTextViewHolder) holder).setDetails(messages.get(position));
        }else if (getItemViewType(position) == TYPE_SENT_TEXT) {
            ((SentTextViewHolder) holder).setDetails(messages.get(position));
        }else if (getItemViewType(position) == TYPE_RECEIVED_IMAGE) {
            ((ReceivedImageViewHolder) holder).setDetails(messages.get(position));
        }else {
            ((SentImageViewHolder) holder).setDetails(messages.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    @Override
    public int getItemViewType(int position) {
        //
        //
        //      TODO: User info from fragments
        //
        //
        //
        String userID="1";
        if (TextUtils.isEmpty(messages.get(position).getMessageText()) && TextUtils.equals(messages.get(position).getSenderID(),userID)) {
            return TYPE_SENT_IMAGE;
        } else if(TextUtils.isEmpty(messages.get(position).getImageUrl()) && TextUtils.equals(messages.get(position).getSenderID(),userID)){
            return TYPE_SENT_TEXT;
        } else if(TextUtils.isEmpty(messages.get(position).getMessageText()) && !TextUtils.equals(messages.get(position).getSenderID(),userID)){
            return TYPE_RECEIVED_IMAGE;
        }
        else return TYPE_RECEIVED_TEXT;
    }

//    @NonNull
//    @Override
//    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = (TextView) LayoutInflater.from(parent.getContext())
//                .inflate(R.layout.chat_message_received_layout, parent, false);
//
//        ChatViewHolder chatViewHolder = new ChatViewHolder(view);
//        return chatViewHolder;
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
//
//        Message message = messages.get(position);
//        holder.titleTv.setText(message.getTitle());
//        holder.authorTv.setText(message.getAuthor());
//        holder.dateTv.setText(message.getPublishedAt());
//        Picasso.get().load(message.getUrlToImage()).into(holder.newsImage);
//        holder.url = message.getUrl();
//    }
//
//    @Override
//    public int getItemCount() {
//        return 0;
//    }

    public static class ReceivedTextViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;
        public ReceivedTextViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.receivedTextView);
        }

        public void setDetails(Message message){
            textView.setText(" "+message.getMessageText()+" ");
        }
    }
    public static class SentTextViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;
        public SentTextViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.sentTextView);
        }
        public void setDetails(Message message){
            textView.setText(" "+message.getMessageText()+" ");
        }
    }
    public static class ReceivedImageViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public ReceivedImageViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.receivedImageView);
        }
        public void setDetails(Message message){
            Picasso.get().load(message.getImageUrl()).into(imageView);
        }
    }
    public static class SentImageViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public SentImageViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.sentImageView);
        }
        public void setDetails(Message message){
            Picasso.get().load(message.getImageUrl()).into(imageView);
        }
    }
}
