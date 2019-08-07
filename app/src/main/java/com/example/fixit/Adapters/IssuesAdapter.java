package com.example.fixit.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fixit.Activities.DetailsFragment;
import com.example.fixit.Activities.UserActivity;
import com.example.fixit.Models.Issue;
import com.example.fixit.R;

import java.io.IOException;
import java.util.Date;
import java.util.List;

public class IssuesAdapter extends RecyclerView.Adapter<IssuesAdapter.ViewHolder>{

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
        private final Integer MAX_VOTES = 30;
        ImageView ivIssue;
        TextView tvTitle;
        TextView tvTimestamp;
        TextView tvFixvotes;
        TextView tvAddress;
        CardView cvWholeIssue;
        ImageView btnFix;
        ProgressBar pbFixvotes;


        public ViewHolder(@NonNull View itemView, final IssuesAdapter adapter) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.tvTitleSingle);
            ivIssue = itemView.findViewById(R.id.ivIssueSingle);
            tvTimestamp = itemView.findViewById(R.id.tvTimeStampSingle);
            tvFixvotes = itemView.findViewById(R.id.tvFixVotes);
            tvAddress = itemView.findViewById(R.id.tvAddressSingle);
            cvWholeIssue = itemView.findViewById(R.id.cvWholeIssue);
            btnFix = itemView.findViewById(R.id.btnFixVote);
            pbFixvotes = itemView.findViewById(R.id.pbFixVotes);
            btnFix.setTag(true);
            pbFixvotes.setMax(MAX_VOTES);
           // pbFixvotes.setMin(0);
            cvWholeIssue.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onClick(View v) {
                    Issue tempIssue = issues.get(getAdapterPosition());
                    FragmentTransaction ft = ((UserActivity)context).getSupportFragmentManager().beginTransaction();
                    DetailsFragment detailsFragment = DetailsFragment.newInstance(tempIssue);
                    ft.replace(R.id.flBottomNav, detailsFragment).commit();
                }
            });
            btnFix.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onClick(View v) {
                    Integer position = getAdapterPosition();
                    Issue issue = issues.get(position);
                    if((Boolean)btnFix.getTag()){
                        issue.setFixvotes(issue.getFixvotes() + 1);
                        btnFix.setImageResource(R.drawable.uppblue);
                        btnFix.setTag(false);
                    }
                    else {
                        if(issue.getFixvotes() > 0) {
                            issue.setFixvotes(issue.getFixvotes() - 1);
                        }
                        btnFix.setImageResource(R.drawable.upp);
                        btnFix.setTag(true);
                    }
                    pbFixvotes.setProgress(issue.getFixvotes());
                    tvFixvotes.setText(issue.getFixvotes()+" upvotes");
                    if (issue.getFixvotes() == MAX_VOTES) {
                        onButtonShowPopupWindowClick(v);
                    }
                }
            });
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        public void bind(Issue issue) {
            tvTitle.setText(issue.getTitle());
            tvTimestamp.setText("Reported on " + issue.formarDate(new Date(issue.getFecha())));
            tvFixvotes.setText(issue.getFixvotes()+" upvotes");
            tvAddress.setText(issue.formatAddress());
            pbFixvotes.setProgress(issue.getFixvotes());
           try {
               issue.downloadFile(0, ivIssue);
           } catch (IOException e) {
              e.printStackTrace();
          }
        }

        public void onButtonShowPopupWindowClick(View view) {

            // inflate the layout of the popup window
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View popupView = inflater.inflate(R.layout.popup, null);

            // create the popup window
            int width = LinearLayout.LayoutParams.WRAP_CONTENT;
            int height = LinearLayout.LayoutParams.WRAP_CONTENT;
            boolean focusable = true; // lets taps outside the popup also dismiss it
            final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);
            popupWindow.setBackgroundDrawable(new ColorDrawable(Color.WHITE));


            // show the popup window
            // which view you pass in doesn't matter, it is only used for the window tolken
            popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                popupWindow.setElevation(20);
            }

            // dismiss the popup window when touched
            popupView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    popupWindow.dismiss();
                    return true;
                }
            });
        }
    }
}
