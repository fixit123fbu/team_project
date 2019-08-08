package com.example.fixit.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.fixit.Models.Issue;
import com.example.fixit.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PagerAdapter extends androidx.viewpager.widget.PagerAdapter {

    Context context;
    Issue issue;
    List<Bitmap> images = new ArrayList<>();
    Boolean detailsPost;
    LayoutInflater layoutInflater;

    public PagerAdapter(Context context, Boolean deatilsPost){
        this.context = context;
        this.detailsPost = deatilsPost;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setIssue(Issue issue){
        this.issue = issue;
    }

    public void addImage(Bitmap image){
        this.images.add(image);
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        // Returns the total number ov views in the pager
        if(detailsPost)
            return this.issue.getImagesCont();
        return images.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View itemView = layoutInflater.inflate(R.layout.item_pager_adapter, container, false);

        ImageView imageView = itemView.findViewById(R.id.ivItemViewPager);
        if (detailsPost){
            try {
                issue.downloadFile(position, imageView);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else if(images.size() > 0){
            imageView.setImageBitmap(images.get(position));
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
