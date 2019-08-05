package com.example.fixit.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fixit.Models.Comment;
import com.example.fixit.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.ViewHolder> {

    Context context;
    List<Comment> comments = new ArrayList<>();

    public CommentsAdapter(Context context, String key){
        this.context = context;
        Query recentPostsQuery = FirebaseDatabase.getInstance().getReference().child("posts").child(key).child("comments").orderByKey().limitToLast(5);
        recentPostsQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                comments = new ArrayList<>();
                for (DataSnapshot commentSnapshot: dataSnapshot.getChildren()) {
                    comments.add(commentSnapshot.getValue(Comment.class));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_comment, parent, false);
        return new ViewHolder(view, this);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvMessage.setText(comments.get(position).getMessage());
        holder.tvUsername.setText(comments.get(position).getUser().getName());
        Picasso.get().load(comments.get(position).getUser().getPhotoUrl()).into(holder.ivProfile);
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView tvMessage;
        TextView tvUsername;
        ImageView ivProfile;

        public ViewHolder(@NonNull View itemView, final CommentsAdapter adapter) {
            super(itemView);
            tvMessage = itemView.findViewById(R.id.tvSingleComment);
            tvUsername = itemView.findViewById(R.id.tvUsernameComments);
            ivProfile = itemView.findViewById(R.id.ivProfileComments);
        }

    }

}
