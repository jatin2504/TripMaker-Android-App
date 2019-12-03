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
import com.example.tripmaker.models.User;
import com.google.firebase.Timestamp;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<Message> messages;
    User user;
    private static int TYPE_SENT_TEXT = 1;
    private static int TYPE_SENT_IMAGE = 2;
    private static int TYPE_RECEIVED_TEXT = 3;
    private static int TYPE_RECEIVED_IMAGE = 4;

    public ChatAdapter(List<Message> messages, User user) {
        this.messages = messages;
        this.user = user;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;

        if (viewType == TYPE_SENT_TEXT) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_message_sent_layout, parent, false);
            return new SentTextViewHolder(view);

        } else if (viewType == TYPE_RECEIVED_TEXT) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_message_received_layout, parent, false);
            return new ReceivedTextViewHolder(view);
        } else if (viewType == TYPE_SENT_IMAGE) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_image_sent_layout, parent, false);
            return new SentImageViewHolder(view);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_image_received_layout, parent, false);
            return new ReceivedImageViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if (getItemViewType(position) == TYPE_RECEIVED_TEXT) {
            ((ReceivedTextViewHolder) holder).setDetails(messages.get(position));
        } else if (getItemViewType(position) == TYPE_SENT_TEXT) {
            ((SentTextViewHolder) holder).setDetails(messages.get(position));
        } else if (getItemViewType(position) == TYPE_RECEIVED_IMAGE) {
            ((ReceivedImageViewHolder) holder).setDetails(messages.get(position));
        } else {
            ((SentImageViewHolder) holder).setDetails(messages.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    @Override
    public int getItemViewType(int position) {
        String userID = user.getEmail();
        if (TextUtils.isEmpty(messages.get(position).getText()) && TextUtils.equals(messages.get(position).getSender(), userID)) {
            return TYPE_SENT_IMAGE;
        } else if (TextUtils.isEmpty(messages.get(position).getImgUrl()) && TextUtils.equals(messages.get(position).getSender(), userID)) {
            return TYPE_SENT_TEXT;
        } else if (TextUtils.isEmpty(messages.get(position).getText()) && !TextUtils.equals(messages.get(position).getText(), userID)) {
            return TYPE_RECEIVED_IMAGE;
        } else return TYPE_RECEIVED_TEXT;
    }

    private static String timeStampStringRepresentation(Timestamp timeStamp) {
        StringBuilder result = new StringBuilder();
        String suffix = "";
        result.append(new SimpleDateFormat("MMM").format(timeStamp.toDate())+" "+timeStamp.toDate().getDate()+" - ");
        if(timeStamp.toDate().getHours()>12)
        {
            result.append(timeStamp.toDate().getHours() - 12);
            suffix = "PM";
        }
        else
        {
            suffix = "AM";
        }
        result.append(":"+timeStamp.toDate().getMinutes()).append(" "+suffix);
        return result.toString();
    }

    public static class ReceivedTextViewHolder extends RecyclerView.ViewHolder {
        public TextView message;
        public TextView userName;
        public TextView time;

        public ReceivedTextViewHolder(@NonNull View itemView) {
            super(itemView);
            message = itemView.findViewById(R.id.recvMsgTextView);
            userName = itemView.findViewById(R.id.chatRecvMsgUser);
            time = itemView.findViewById(R.id.chatRecvMsgTime);
        }

        public void setDetails(Message item) {
            message.setText(item.getText());
            userName.setText(item.getSender());
            time.setText(timeStampStringRepresentation(item.getTimeStamp()));
        }
    }

    public static class SentTextViewHolder extends RecyclerView.ViewHolder {
        public TextView message;
        public TextView time;

        public SentTextViewHolder(@NonNull View itemView) {
            super(itemView);
            message = itemView.findViewById(R.id.sentTextView);
            time = itemView.findViewById(R.id.chatMsgTime);
        }

        public void setDetails(Message item) {
            message.setText(item.getText());
            time.setText(timeStampStringRepresentation(item.getTimeStamp()));
        }

    }

    public static class ReceivedImageViewHolder extends RecyclerView.ViewHolder {
        public TextView senderNameTV;
        public ImageView imageView;

        public ReceivedImageViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.receivedImageView);
            senderNameTV = itemView.findViewById(R.id.senderNameTV);
        }

        public void setDetails(Message message) {
            Picasso.get().load(message.getImgUrl()).into(imageView);
            senderNameTV.setText(message.getSender());
        }
    }

    public static class SentImageViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;

        public SentImageViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.sentImageView);
        }

        public void setDetails(Message message) {
            Picasso.get().load(message.getImgUrl()).into(imageView);
        }
    }
}
