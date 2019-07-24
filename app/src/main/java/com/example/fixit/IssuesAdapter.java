package com.example.fixit;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.util.List;

public class IssuesAdapter extends RecyclerView.Adapter<IssuesAdapter.ViewHolder>{

    private final static String IMAGE_STORAGE_ROUTE = "images/";
    private static final String IMAGE_FORMAT = ".jpg";

    Context context;
    List<Issue> issues;

   public IssuesAdapter(Context context, List<Issue> issues){
       this.context = context;
       this.issues = issues;
   }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_issue, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Issue issue = issues.get(position);
        holder.bind(issue);
    }

    @Override
    public int getItemCount() {
        return issues.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        String INTENT_ISSUE_EXTRA = DetailsActivity.class.getSimpleName();

        ImageView ivIssue;
        TextView tvTitle;
        TextView tvTimestamp;
        TextView tvStatus;
        TextView tvFixvotes;
        TextView tvAddress;
        ImageButton btnDetails;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.tvTitleSingle);
            ivIssue = itemView.findViewById(R.id.ivIssueSingle);
            tvTimestamp = itemView.findViewById(R.id.tvTimeStampSingle);
            tvStatus = itemView.findViewById(R.id.tvStatusSingle);
            tvFixvotes = itemView.findViewById(R.id.tvFixVotes);
            tvAddress = itemView.findViewById(R.id.tvAddressSingle);
            btnDetails = itemView.findViewById(R.id.btnShareIssue);
            btnDetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Issue tempIssue = issues.get(getAdapterPosition());
                    Intent intent = new Intent(context, DetailsActivity.class);
                    intent.putExtra(INTENT_ISSUE_EXTRA, tempIssue);
                    context.startActivity(intent);
                }
            });
        }

        public void bind(Issue issue) {
            tvTitle.setText(issue.getDescription());
            tvTimestamp.setText(issue.getDate().toString());
            tvStatus.setText("Process");
            tvFixvotes.setText(issue.getFixvotes()+"");
            tvAddress.setText(issue.getLocation().getAddress());
            try {
                issue.downloadFile(ivIssue);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
