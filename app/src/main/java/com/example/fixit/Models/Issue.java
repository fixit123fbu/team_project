package com.example.fixit.Models;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class Issue implements Parcelable {

    private final static String POST_ROUTE = "posts";
    private final static String IMAGE_STORAGE_ROUTE = "images/";
    private static final String IMAGE_FORMAT = ".jpg";
    private String issueID;
    private Long fecha;
    private String title;
    private String description;
    private Integer fixvotes;
    private Integer imagesCont;
    private Location location;

    public Issue(){}

    public Issue(String title, String key, String description, Location location, Integer imagesCont, Long date){
        this.title = title;
        this.location = location;
        this.fixvotes = 0;
        this.description = description;
        this.issueID = key;
        this.imagesCont = imagesCont;
        this.fecha = date;
    }


    protected Issue(Parcel in) {
        issueID = in.readString();
        title = in.readString();
        description = in.readString();
        if (in.readByte() == 0) {
            fixvotes = null;
        } else {
            fixvotes = in.readInt();
        }
        if (in.readByte() == 0) {
            imagesCont = null;
        } else {
            imagesCont = in.readInt();
        }
        fecha = in.readLong();
        location = in.readParcelable(Location.class.getClassLoader());
    }

    public static final Creator<Issue> CREATOR = new Creator<Issue>() {
        @Override
        public Issue createFromParcel(Parcel in) {
            return new Issue(in);
        }

        @Override
        public Issue[] newArray(int size) {
            return new Issue[size];
        }
    };
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(issueID);
        dest.writeString(title);
        dest.writeString(description);
        if (fixvotes == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(fixvotes);
        }
        if (imagesCont == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(imagesCont);
        }
        dest.writeLong(fecha);
        dest.writeParcelable(location, flags);
    }

    public Integer getFixvotes() {
        return fixvotes;
    }

    public void setFixvotes(Integer fixvotes) {
        this.fixvotes = fixvotes;
        auxSetFixVotes(fixvotes);
    }

    public void auxSetFixVotes(Integer val){
        getReferenceFirebase().child("fixvotes").setValue(val);
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIssueID() {
        return issueID;
    }

    public Double getLatitude() {
        return location.getLatitude();
    }

    public Double getLongitude() {
        return location.getLongitude();
    }

    public Integer getImagesCont(){return imagesCont;}

    public Long getFecha() {
        return fecha;
    }

    public void setFecha(Long fecha) {
        this.fecha = fecha;
    }

    @Exclude
    public void addComment(Comment newComment){
        DatabaseReference mPostReference = getReferenceFirebase().child("comments").push();
        mPostReference.setValue(newComment);
    }

    public void downloadFile(Integer index, final ImageView ivTemp) throws IOException {
        String key = getIssueID();
        final File issueImage = File.createTempFile("images", "jpg");
        StorageReference mImageRef = FirebaseStorage.getInstance().getReference().child(IMAGE_STORAGE_ROUTE + key + "/" + index + IMAGE_FORMAT);;
        mImageRef.getFile(issueImage)
                .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        // Successfully downloaded data to local file
                        // by this point we have the camera photo on disk
                         ivTemp.setImageBitmap(BitmapFactory.decodeFile(issueImage.getAbsolutePath()));
                        // Load the taken image into a preview
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle failed download
            }
        });
    }


    public void downloadBytes(final ImageView imageView){
        final long ONE_MEGABYTE = 1024 * 1024;
        StorageReference mBytesRef = FirebaseStorage.getInstance().getReference().child(IMAGE_STORAGE_ROUTE + getIssueID() + IMAGE_FORMAT);
        mBytesRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                // Data for "images/island.jpg" is returns, use this as needed
                Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                imageView.setImageBitmap(bmp);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
                Log.d("failure", "Error downloading image, do not try again");
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String formarDate(Date date){
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        int year  = localDate.getYear();
        int month = localDate.getMonthValue();
        int day   = localDate.getDayOfMonth();
        return month + "/" + day + "/" + year + " ";
    }

    public String auxFormatAddress(String address){
        int endIndex = address.lastIndexOf(',');
        return address.substring(0, endIndex);
    }

    public String formatAddress(){
        String address = location.getAddress();
        return auxFormatAddress(auxFormatAddress(address));
    }

    @Exclude
    public DatabaseReference getReferenceFirebase() {
        return FirebaseDatabase.getInstance().getReference().child(POST_ROUTE).child(getIssueID());
    }

}