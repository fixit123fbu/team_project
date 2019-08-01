package com.example.fixit.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.fixit.Models.Issue;
import com.example.fixit.R;

import java.io.IOException;

public class PagerAdapter extends androidx.viewpager.widget.PagerAdapter {

    Context context;
    Issue issue;
    LayoutInflater layoutInflater;

    public PagerAdapter(Context context, Issue issue){
        this.context = context;
        this.issue = issue;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        // Returns the total number ov views in the pager
        return this.issue.getImagesCont();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View itemView = layoutInflater.inflate(R.layout.item_pager_adapter, container, false);

        ImageView imageView = itemView.findViewById(R.id.ivItemViewPager);
        try {
            issue.downloadFile(position, imageView);
        } catch (IOException e) {
            e.printStackTrace();
        }

        container.addView(itemView);

        //listening to image click
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "you clicked image " + (position + 1), Toast.LENGTH_LONG).show();
            }
        });

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }

}
