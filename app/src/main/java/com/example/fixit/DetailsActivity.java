package com.example.fixit;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Date;

public class DetailsActivity extends AppCompatActivity {

    String INTENT_ISSUE_EXTRA = DetailsActivity.class.getSimpleName();
    String INTENT_DATE_EXTRA = "date";

    SeekBar sbUrgency;
    SeekBar sbDanger;
    SeekBar sbUtility;
    ImageView ivIssueDetails;
    Issue issue;
    TextView tvTitleDetails;
    TextView tvTimestampDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        sbUrgency =findViewById(R.id.sbUrgency);
        sbDanger = findViewById(R.id.sbDanger);
        sbUtility = findViewById(R.id.sbUtility);
        ivIssueDetails = findViewById(R.id.ivDetails);
        tvTitleDetails = findViewById(R.id.tvTitleDetails);
        tvTimestampDetails = findViewById(R.id.tvTimestampDetails);
        issue = getIntent().getParcelableExtra(INTENT_ISSUE_EXTRA);
        issue.setDate(new Date(getIntent().getLongExtra(INTENT_DATE_EXTRA, 0)));
//        try {
//            issue.downloadFile(ivIssueDetails);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        tvTitleDetails.setText(issue.getTitle());
        tvTimestampDetails.setText(issue.getDate().toString());
    }
}
