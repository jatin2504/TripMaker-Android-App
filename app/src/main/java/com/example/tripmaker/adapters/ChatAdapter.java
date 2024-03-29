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
import com.example.tripmaker.activites.ChatActivity;
import com.example.tripmaker.models.Message;
import com.example.tripmaker.models.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    static List<Message> messages;
    User user;
    static FirebaseFirestore db = FirebaseFirestore.getInstance();
    ;
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
            ((SentTextViewHolder) holder).setDetails(messages.get(position), position, this);
        } else if (getItemViewType(position) == TYPE_RECEIVED_IMAGE) {
            ((ReceivedImageViewHolder) holder).setDetails(messages.get(position));
        } else {
            ((SentImageViewHolder) holder).setDetails(messages.get(position), position, this);
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
        Date date = timeStamp.toDate();
        SimpleDateFormat formatter = new SimpleDateFormat("dd MMM hh:mm a");
        return formatter.format(date);
//        StringBuilder result = new StringBuilder();
//        String suffix = "";
//        result.append(new SimpleDateFormat("MMM").format(timeStamp.toDate()) + " " + timeStamp.toDate().getDate() + " - ");
//        if (timeStamp.toDate().getHours() > 12) {
//            result.append(timeStamp.toDate().getHours() - 12);
//            suffix = "PM";
//        } else {
//            suffix = "AM";
//        }
//        result.append(":" + timeStamp.toDate().getMinutes()).append(" " + suffix);
//        return result.toString();
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
        public int position;
        public ChatAdapter adapter;

        public SentTextViewHolder(@NonNull View itemView) {
            super(itemView);
            message = itemView.findViewById(R.id.sentTextView);
            time = itemView.findViewById(R.id.chatMsgTime);
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    Message messageToDelete = messages.get(position);
                    db.collection("chats").document(ChatActivity.currentTrip.getId()).update("messages", FieldValue.arrayRemove(messageToDelete)).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            messages.remove(position - 1);
                            adapter.notifyDataSetChanged();
                        }
                    });
                    return false;
                }
            });
        }

        public void setDetails(Message message, int position, ChatAdapter adapter) {
            this.adapter = adapter;
            this.position = position;
            this.message.setText(message.getText());
            time.setText(timeStampStringRepresentation(message.getTimeStamp()));
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
        public int position;
        public ChatAdapter adapter;

        public SentImageViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.sentImageView);

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    Message messageToDelete = messages.get(position);
                    db.collection("chats").document(ChatActivity.currentTrip.getId()).update("messages", FieldValue.arrayRemove(messageToDelete)).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            messages.remove(position - 1);
                            adapter.notifyDataSetChanged();
                        }
                    });
                    return false;
                }
            });
        }

        public void setDetails(Message message, int position, ChatAdapter adapter) {
            this.adapter = adapter;
            this.position = position;
            Picasso.get().load(message.getImgUrl()).into(imageView);
        }
    }
}
