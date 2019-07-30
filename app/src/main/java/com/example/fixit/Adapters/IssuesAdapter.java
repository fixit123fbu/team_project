package com.example.fixit.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fixit.Activities.DetailsActivity;
import com.example.fixit.Models.Issue;
import com.example.fixit.R;

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
        return new ViewHolder(view, this);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
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
        String INTENT_DATE_EXTRA = "date";
        ImageView ivIssue;
        TextView tvTitle;
        TextView tvTimestamp;
        TextView tvFixvotes;
        TextView tvAddress;
        CardView cvWholeIssue;
        ImageButton btnFix;



        public ViewHolder(@NonNull View itemView, final IssuesAdapter adapter) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.tvTitleSingle);
            ivIssue = itemView.findViewById(R.id.ivIssueSingle);
            tvTimestamp = itemView.findViewById(R.id.tvTimeStampSingle);
            tvFixvotes = itemView.findViewById(R.id.tvFixVotes);
            tvAddress = itemView.findViewById(R.id.tvAddressSingle);
            cvWholeIssue = itemView.findViewById(R.id.cvWholeIssue);
            btnFix = itemView.findViewById(R.id.btnFixVote);
            cvWholeIssue.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onClick(View v) {
                    Issue tempIssue = issues.get(getAdapterPosition());
                    Intent intent = new Intent(context, DetailsActivity.class);
                    intent.putExtra(INTENT_ISSUE_EXTRA, tempIssue);
                    intent.putExtra(INTENT_DATE_EXTRA, tempIssue.formarDate());
                    context.startActivity(intent);
                }
            });
            btnFix.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onClick(View v) {
                    Integer position = getAdapterPosition();
                    Issue issue = issues.get(position);
                    issue.setFixvotes(issue.getFixvotes()+1);
                    adapter.onBindViewHolder(ViewHolder.this, position);
                }
            });
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        public void bind(Issue issue) {
            tvTitle.setText(issue.getTitle());
            tvTimestamp.setText(issue.formarDate());
            tvFixvotes.setText(issue.getFixvotes()+"");
            tvAddress.setText(issue.formatAddress());
//            try {
//                issue.downloadFile(0, ivIssue);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
        }
    }
}
