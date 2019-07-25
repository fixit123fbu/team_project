package com.example.fixit;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
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

        ImageView ivIssue;
        TextView tvTitle;
        TextView tvTimestamp;
        TextView tvFixvotes;
        TextView tvAddress;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.tvTitleSingle);
            ivIssue = itemView.findViewById(R.id.ivIssueSingle);
            tvTimestamp = itemView.findViewById(R.id.tvTimeStampSingle);
            tvFixvotes = itemView.findViewById(R.id.tvFixVotes);
            tvAddress = itemView.findViewById(R.id.tvAddressSingle);
//            btnDetails.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Issue tempIssue = issues.get(getAdapterPosition());
//                    Intent intent = new Intent(context, DetailsActivity.class);
//                    intent.putExtra(INTENT_ISSUE_EXTRA, tempIssue);
//                    intent.putExtra(INTENT_DATE_EXTRA, tempIssue.getDate().getTime());
//                    context.startActivity(intent);
//                }
//            });
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        public void bind(Issue issue) {
            tvTitle.setText(issue.getDescription());
            tvTimestamp.setText(issue.formarDate());
            tvFixvotes.setText(issue.getFixvotes()+"");
            tvAddress.setText(issue.getLocation().getAddress());
//            issue.downloadBytes(ivIssue);
            try {
                issue.downloadFile(ivIssue);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
