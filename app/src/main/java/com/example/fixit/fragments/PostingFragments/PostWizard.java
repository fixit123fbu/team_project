package com.example.fixit.fragments.PostingFragments;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.fixit.Models.Issue;
import com.example.fixit.Models.Location;
import com.example.fixit.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PostWizard extends Fragment{

    private List<Fragment> steps;
    private int position;
    private Button btnBack;
    private Button btnNext;
    private Issue issue;
    private String title;
    private String description;
    private Location location;
    private List<Bitmap> images;
    private FirebaseStorage mStorage;
    private FirebaseDatabase mDatabase;

    private final int INFO_POS = 0;
    private final int LOC_POS = 1;
    private final int PIC_POS = 2;
    private final static String POST_ROUTE = "posts";
    private final static String IMAGE_STORAGE_ROUTE = "images/";
    private static final String IMAGE_FORMAT = ".jpg";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.wizard_fragment, container, false);
        btnBack = view.findViewById(R.id.btnBack);
        btnNext = view.findViewById(R.id.btnNext);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        // Add child fragments
        steps = new ArrayList<>();
        steps.add(new InformationFrag());
        steps.add(new LocationFrag());
        steps.add(new PicturesFrag());

        // Initialize Storage
        mStorage = FirebaseStorage.getInstance();

        // Initialize database
        mDatabase = FirebaseDatabase.getInstance();

        // start in location frag
        position = INFO_POS;
        changeChildFrag();

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position = position - 1;
                btnNext.setText("Next");
                changeChildFrag();
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateIssue();
                position = position + 1;
                if(position == PIC_POS)
                    btnNext.setText("Submit");
                changeChildFrag();
            }
        });
    }

    private void changeChildFrag(){
        if(position >= INFO_POS && position <= PIC_POS ){
            FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
            transaction.replace(R.id.flChildFragCont, steps.get(position)).commit();
        }
        else{
            ((OnFinishedPostingListener) getActivity()).backToHome();
        }
    }

    private void updateIssue(){
        switch (position){
            case INFO_POS:
                title = ((InformationFrag)steps.get(position)).getTitle();
                description = ((InformationFrag)steps.get(position)).getDescription();
                break;
            case LOC_POS:
                location = ((LocationFrag)steps.get(position)).getIssueLocation();
                break;
            case PIC_POS:
                this.images = ((PicturesFrag)steps.get(position)).getImages();
                postIssue();
                break;
        }
    }

    private void postIssue(){
        // Create a new issue with a different key in server
        DatabaseReference mPostReference = mDatabase.getReference().child(POST_ROUTE).push();
        // Save new key
        String key = mPostReference.getKey();
        issue = new Issue(title, key, description, location, images.size(), new Date().getTime());
        // Upload issue to real time database
        mPostReference.setValue(issue);
        // Upload images to storage
        for(int i = 0; i < images.size(); i++){
            uploadBytesToStorage(i);
        }
    }

    public void uploadBytesToStorage(int index){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        String key = issue.getIssueID();
        StorageReference mByteseRef;
        mByteseRef = mStorage.getReference().child(IMAGE_STORAGE_ROUTE + key + "/" + index + IMAGE_FORMAT);
        images.get(index).compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();
        UploadTask uploadTask = mByteseRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.d("UploadBytesToStorage", "Uploaded");
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Log.d("UploadBytesToStorage", "Failed when uploading bytes");
            }
        });
    }
    public interface OnFinishedPostingListener{
        void backToHome();
    }

}