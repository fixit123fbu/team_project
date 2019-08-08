package com.example.fixit.fragments.PostingFragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.fixit.Adapters.PagerAdapter;
import com.example.fixit.R;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PicturesFrag extends Fragment {

    private final int SELECT_PICTURES = 2;
    private final int TAKE_PICTURE = 3;
    private final String photoFileName = "photo.jpg";
    private final String APP_TAG = "MyCustomApp";
    private File photoFile;
    private List<Bitmap> images;

    private ImageView ivPreviewPictures;
    private ViewPager vpPicturesFrag;
    private PagerAdapter pagerAdapter;
    private ImageButton btnPickFromGalleryPictures;
    private ImageButton btnTakePicturePictures;
    private TextView tvImageCounterPicturesFrag;

    PicturesFrag(){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.pictures_fragment, container, false);
        ivPreviewPictures = view.findViewById(R.id.ivHeaderPictures);
        btnPickFromGalleryPictures = view.findViewById(R.id.btnPickFromGalleryPictures);
        btnTakePicturePictures = view.findViewById(R.id.btnTakePicturePictures);
        vpPicturesFrag = view.findViewById(R.id.vpPicturesFrag);
        tvImageCounterPicturesFrag = view.findViewById(R.id.tvImageCounterPicturesFrag);
        tvImageCounterPicturesFrag.setVisibility(View.GONE);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setPagerAdapter();

        btnTakePicturePictures.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePicture();
            }
        });

        btnPickFromGalleryPictures.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickMultiplePics();
            }
        });

    }

    private void setPagerAdapter() {
        pagerAdapter = new PagerAdapter(getContext(), false);
        vpPicturesFrag.setAdapter(pagerAdapter);
    }

    public void pickMultiplePics(){
        // Trigger gallery selection for a photo
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        // Bring up gallery to select a photo
        startActivityForResult(Intent.createChooser(intent, "android.intent.action.SEND_MULTIPLE"), SELECT_PICTURES);
    }

    public void takePicture(){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            // Create the File where the photo should go
            photoFile = createImageFile(photoFileName);
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri uriPictureIssue = FileProvider.getUriForFile(getContext(),
                        "com.codepath.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uriPictureIssue);
                startActivityForResult(takePictureIntent, TAKE_PICTURE);
            }
        }
    }

    private File createImageFile(String fileName) {
        // Create an image file name
        File storageDir = new File(getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES), APP_TAG);
        // Create the storage directory if it does not exist
        if (!storageDir.exists() && !storageDir.mkdirs()){
            Toast.makeText(getContext(), "Failed to create directory", Toast.LENGTH_LONG).show();
        }
        // Return the file target for the photo based on filename
        File image = new File(storageDir.getPath() + File.separator + fileName);
        return image;
    }

    public Bitmap rotateBitmapOrientation(String photoFilePath) {
        // Create and configure BitmapFactory
        BitmapFactory.Options bounds = new BitmapFactory.Options();
        bounds.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(photoFilePath, bounds);
        BitmapFactory.Options opts = new BitmapFactory.Options();
        Bitmap bm = BitmapFactory.decodeFile(photoFilePath, opts);
        // Read EXIF Data
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(photoFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String orientString = exif.getAttribute(ExifInterface.TAG_ORIENTATION);
        int orientation = orientString != null ? Integer.parseInt(orientString) : ExifInterface.ORIENTATION_NORMAL;
        int rotationAngle = 0;
        if (orientation == ExifInterface.ORIENTATION_ROTATE_90) rotationAngle = 90;
        if (orientation == ExifInterface.ORIENTATION_ROTATE_180) rotationAngle = 180;
        if (orientation == ExifInterface.ORIENTATION_ROTATE_270) rotationAngle = 270;
        // Rotate Bitmap
        Matrix matrix = new Matrix();
        matrix.setRotate(rotationAngle, (float) bm.getWidth() / 2, (float) bm.getHeight() / 2);
        Bitmap rotatedBitmap = Bitmap.createBitmap(bm, 0, 0, bounds.outWidth, bounds.outHeight, matrix, true);
        // Return result
        return rotatedBitmap;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bitmap bitImage = null;
        images = new ArrayList<>();
        if(resultCode == Activity.RESULT_OK) {
            if(requestCode == SELECT_PICTURES) {
                if(data.getClipData() != null) {
                    int count = data.getClipData().getItemCount(); //evaluate the count before the for loop --- otherwise, the count is evaluated every loop.
                    // Iterate to get all the pictures selected
                    for(int i = 0; i < count; i++) {
                        Uri imageUri = data.getClipData().getItemAt(i).getUri();
                        try {
                            bitImage = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), imageUri);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        images.add(bitImage);
                        pagerAdapter.addImage(bitImage);
                    }
                }
                else if(data.getData() != null) {
                    try {
                        bitImage = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), data.getData());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    images.add(bitImage);
                    pagerAdapter.addImage(bitImage);
                }
            }
            else if(requestCode == TAKE_PICTURE){
                // by this point we have the camera photo on disk
                bitImage = rotateBitmapOrientation(photoFile.getAbsolutePath());
                images.add(bitImage);
                pagerAdapter.addImage(bitImage);
                Toast.makeText(getContext(), "To add another picture click de button again", Toast.LENGTH_LONG).show();
            }
        }
        if(images.size() > 0) {
            tvImageCounterPicturesFrag.setVisibility(View.VISIBLE);
            tvImageCounterPicturesFrag.setText(images.size() + " Pictures");
        }
        else{
            tvImageCounterPicturesFrag.setVisibility(View.GONE);
        }
    }

    public List<Bitmap> getImages(){
        return images;
    }
}