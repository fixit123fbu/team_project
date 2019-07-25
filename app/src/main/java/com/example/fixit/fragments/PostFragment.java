package com.example.fixit.fragments;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.example.fixit.Issue;
import com.example.fixit.Location;
import com.example.fixit.R;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class PostFragment extends Fragment {

    private final static int PICK_PHOTO_CODE = 1046;
    private final static int PERMISSION_CODE = 1001;
    private final static String POST_ROUTE = "posts";
    private final static String IMAGE_STORAGE_ROUTE = "images/";
    private static final String IMAGE_FORMAT = ".jpg";
    static final int REQUEST_IMAGE_CAPTURE = 1;
    public final String APP_TAG = "MyCustomApp";
    public String photoFileName = "photo.jpg";

    private ImageButton btnPickFromGallery;
    private ImageButton btnTakePicture;
    private ImageView ivPreview;
    private EditText etDescription;
    private EditText etTitle;
    private Button btnSubmit;

    private FirebaseStorage mStorage;
    private FirebaseDatabase mDatabase;
    private Issue issue;
    private Uri uriPictureIssue;
    private Bitmap bitmapFormat;
    private File photoFile;
    private Location aux;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_post, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnPickFromGallery = view.findViewById(R.id.btnPickFromGallery);
        btnTakePicture = view.findViewById(R.id.btnTakePicture);
        ivPreview = view.findViewById(R.id.ivPreview);
        etDescription = view.findViewById(R.id.etDescription);
        btnSubmit = view.findViewById(R.id.btnSubmit);
        etTitle = view.findViewById(R.id.etTitle);
        aux = null;


        // Initialize Storage
        mStorage = FirebaseStorage.getInstance();

        // Initialize database
        mDatabase = FirebaseDatabase.getInstance();

        btnPickFromGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPickPhoto();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postIssue();
            }
        });

        btnTakePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });

        if (!Places.isInitialized()) {
            Places.initialize(getContext(), "AIzaSyBR_HirBjq-d46IBvG40f16aqHJ20LHoSw\n");
        }

        // Initialize Places.
        Places.initialize(getContext(), "AIzaSyBR_HirBjq-d46IBvG40f16aqHJ20LHoSw\n");


        // Create a new Places client instance.
        PlacesClient placesClient = Places.createClient(getContext());

        // Initialize the AutocompleteSupportFragment.
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment) getChildFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ADDRESS));


        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                // TODO: Get info about the selected place.
                aux = new Location(place.getLatLng().latitude, place.getLatLng().longitude, place.getAddress(), place.getName());
                Toast.makeText(getContext(), aux.getName() + " Success!!", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(@NonNull Status status) {
                // TODO: Handle the error
                Toast.makeText(getContext(), "Failed to calculate location", Toast.LENGTH_LONG).show();
            }
        });

    }


    private void postIssue() {
        // Create a new issue with a different key
        DatabaseReference mPostReference = mDatabase.getReference().child(POST_ROUTE).push();
        // Save new key
        String key = mPostReference.getKey();
        //Extract information necessary to create the issue
        String description = etDescription.getText().toString();
        String title = etTitle.getText().toString();
        issue = new Issue(title, key, description, aux);
        // Adjust issue values
        mPostReference.setValue(issue);
        // Upload image to storage
//        upLoadFileToStorage(key);
       uploadBytesToStorage(key, bitmapFormat);
    }

    // Trigger gallery selection for a photo
    public void onPickPhoto() {
        // Create intent for picking a photo from the gallery
        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
        // So as long as the result is not null, it's safe to use the intent.
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            // Bring up gallery to select a photo
            startActivityForResult(intent, PICK_PHOTO_CODE);
        }
    }

    private File createImageFile(String fileName) throws IOException {
        // Create an image file name
        File storageDir =new File(getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES), APP_TAG);
        // Create the storage directory if it does not exist
        if (!storageDir.exists() && !storageDir.mkdirs()){
            Toast.makeText(getContext(), "Failed to create directory", Toast.LENGTH_LONG).show();
        }
        // Return the file target for the photo based on filename
        File image = new File(storageDir.getPath() + File.separator + fileName);
        return image;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            // Create the File where the photo should go
            photoFile = null;
            try {
                photoFile = createImageFile(photoFileName);
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                uriPictureIssue = FileProvider.getUriForFile(getContext(),
                        "com.codepath.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uriPictureIssue);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
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
        if(resultCode == Activity.RESULT_OK && data != null) {
            if(requestCode == PICK_PHOTO_CODE) {
                uriPictureIssue = data.getData();
                ivPreview.setImageURI(uriPictureIssue);
                bitmapFormat =((BitmapDrawable)ivPreview.getDrawable()).getBitmap();
                Toast.makeText(getContext(), "Upload from Gallery", Toast.LENGTH_LONG).show();
            }
            else if(requestCode == REQUEST_IMAGE_CAPTURE){
                // by this point we have the camera photo on disk
                bitmapFormat = rotateBitmapOrientation(photoFile.getAbsolutePath());
                // Load the taken image into a preview
                ivPreview.setImageBitmap(bitmapFormat);
//                uriPictureIssue = getImageUri(getContext(), takenImage);
                Toast.makeText(getContext(), "Upload from Camera", Toast.LENGTH_LONG).show();
            }
        }else{
            Toast.makeText(getContext(), "Error selecting image", Toast.LENGTH_LONG).show();
        }
    }

    public void upLoadFileToStorage(String key){
        if(uriPictureIssue != null) {
            StorageReference mImageRef = mStorage.getReference().child(IMAGE_STORAGE_ROUTE + key + IMAGE_FORMAT);
            mImageRef.putFile(uriPictureIssue).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                    // Successfully uploaded
                    Toast.makeText(getContext(), "Uploaded", Toast.LENGTH_LONG).show();
                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle unsuccessful uploads
                            Toast.makeText(getContext(), "Uploading failed", Toast.LENGTH_LONG).show();
                        }
                    });
        }
        else{
            Toast.makeText(getContext(), "Select an image before issueing", Toast.LENGTH_LONG).show();
        }
    }

    public void uploadBytesToStorage(String key, Bitmap bitmap){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();
        StorageReference mByteseRef = mStorage.getReference().child(IMAGE_STORAGE_ROUTE + key + IMAGE_FORMAT);
        UploadTask uploadTask = mByteseRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(getContext(), "Uploading failed", Toast.LENGTH_LONG).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(getContext(), "Uploaded", Toast.LENGTH_LONG).show();

            }
        });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode){
            case PERMISSION_CODE :{
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    onPickPhoto();
                } else {
                    Toast.makeText(getContext(), "Permission denied :(", Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}
