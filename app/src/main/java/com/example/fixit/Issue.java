package com.example.fixit;

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
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class Issue implements Parcelable {

    private final static String IMAGE_STORAGE_ROUTE = "images/";
    private static final String IMAGE_FORMAT = ".jpg";

    String issueID;
    Date date;
    String title;
    String description;
    Integer fixvotes;
    Location location;

    public Issue(){}

    public Issue(String title, String key, String description, Location location){
        this.title = title;
        this.location = location;
        this.fixvotes = 0;
        this.date = new Date();
        this.description = description;
        this.issueID = key;
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
        dest.writeParcelable(location, flags);
    }

    public Integer getFixvotes() {
        return fixvotes;
    }

    public void setFixvotes(Integer fixvotes) {
        this.fixvotes = fixvotes;
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getIssueID() {
        return issueID;
    }

    public void setIssueID(String issueID) {
        this.issueID = issueID;
    }


    public Double getLatitude() {
        return location.getLatitude();
    }


    public Double getLongitude() {
        return location.getLongitude();
    }

    public void downloadFile(final ImageView auxImage) throws IOException {
        String key = getIssueID();
        final File issueImage = File.createTempFile("images", "jpg");
        StorageReference mImageRef = FirebaseStorage.getInstance().getReference().child(IMAGE_STORAGE_ROUTE + key + IMAGE_FORMAT);
        mImageRef.getFile(issueImage)
                .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        // Successfully downloaded data to local file
                        // by this point we have the camera photo on disk
                        Bitmap imageFile = BitmapFactory.decodeFile(issueImage.getAbsolutePath());
                        // Load the taken image into a preview
                        auxImage.setImageBitmap(imageFile);
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
    public String formarDate(){
        Date date = new Date();
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        int year  = localDate.getYear();
        int month = localDate.getMonthValue();
        int day   = localDate.getDayOfMonth();
        return month + "/" + day + "/" + year + " ";
    }
}