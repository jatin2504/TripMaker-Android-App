package com.example.tripmaker.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.tripmaker.R;
import com.example.tripmaker.models.User;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.List;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UsersViewHolder> {
    private List<User> userList;

    public UsersAdapter(List<User> userList) {
        this.userList = userList;
    }

    @NonNull
    @Override
    public UsersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.users_list_layout, parent, false);
        UsersAdapter.UsersViewHolder viewHolder = new UsersViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull UsersViewHolder holder, int position) {
        holder.userNameTV.setText(userList.get(position).getFirstName()+" "+userList.get(position).getLastName());
        Picasso.get().load(userList.get(position).getImageUrl()).into(holder.userDpIv);
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class UsersViewHolder extends RecyclerView.ViewHolder {

        private ImageView userDpIv;
        private TextView userNameTV;

        public UsersViewHolder(@NonNull View itemView) {
            super(itemView);

            userDpIv = itemView.findViewById(R.id.user_dp_IV);
            userNameTV = itemView.findViewById(R.id.user_name_TV);
        }
    }
}
