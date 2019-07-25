package com.example.fixit;

import android.os.Bundle;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

public class DetailsActivity extends AppCompatActivity {

    String INTENT_ISSUE_EXTRA = DetailsActivity.class.getSimpleName();
    String INTENT_DATE_EXTRA = "date";

    SeekBar sbUrgency;
    SeekBar sbDanger;
    SeekBar sbUtility;
    Issue issue;
    TextView tvTitleDetails;
    TextView tvTimestampDetails;
    ViewPager viewPager;
    int images[] = {R.drawable.camaro, R.drawable.datsun, R.drawable.corvette, R.drawable.mustang};
    PagerAdapter pagerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        viewPager = findViewById(R.id.vpImageDetails);
        sbUrgency =findViewById(R.id.sbUrgency);
        sbDanger = findViewById(R.id.sbDanger);
        sbUtility = findViewById(R.id.sbUtility);
        tvTitleDetails = findViewById(R.id.tvTitleDetails);
        tvTimestampDetails = findViewById(R.id.tvTimestampDetails);

        pagerAdapter = new PagerAdapter(DetailsActivity.this, images);
        viewPager.setAdapter(pagerAdapter);

        issue = getIntent().getParcelableExtra(INTENT_ISSUE_EXTRA);
//        try {
//            issue.downloadFile(ivIssueDetails);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        tvTitleDetails.setText(issue.getTitle());
        tvTimestampDetails.setText(getIntent().getStringExtra(INTENT_DATE_EXTRA));
    }
}
