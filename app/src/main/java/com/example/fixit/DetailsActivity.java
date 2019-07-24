package com.example.fixit;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

public class DetailsActivity extends AppCompatActivity {

    String INTENT_ISSUE_EXTRA = DetailsActivity.class.getSimpleName();
    String INTENT_BITMAP_EXTRA = "images";
    private final static String IMAGE_STORAGE_ROUTE = "images/";
    private static final String IMAGE_FORMAT = ".jpg";

    SeekBar sbUrgency;
    SeekBar sbDanger;
    SeekBar sbUtility;
    ImageView ivIssueDetails;
    Issue issue;
    TextView tvTitleDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        sbUrgency =findViewById(R.id.sbUrgency);
        sbDanger = findViewById(R.id.sbDanger);
        sbUtility = findViewById(R.id.sbUtility);
        ivIssueDetails = findViewById(R.id.ivDetails);
        tvTitleDetails = findViewById(R.id.tvTitleDetails);
        issue = getIntent().getParcelableExtra(INTENT_ISSUE_EXTRA);
        try {
            issue.downloadFile(ivIssueDetails);
        } catch (IOException e) {
            e.printStackTrace();
        }
        tvTitleDetails.setText(issue.getTitle());
    }
}
