package com.example.fixit.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fixit.R;

import java.util.List;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.ViewHolder> {

    Context context;
    List<String> comments;

    public CommentsAdapter(Context context, List<String> comments){
        this.context = context;
        this.comments =  comments;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_comment, parent, false);
        return new ViewHolder(view, this);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvComment.setText(comments.get(position));
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView tvComment;

        public ViewHolder(@NonNull View itemView, final CommentsAdapter adapter) {
            super(itemView);
            tvComment = itemView.findViewById(R.id.tvSingleComment);
        }

    }

}
