package com.example.fixit.Activities;
// Add an import statement for the client library.

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.fixit.Models.FixitUser;
import com.example.fixit.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;


public class SignInActivity extends AppCompatActivity {

    private static final String SIGN_IN = "signIn";
    private static final String TAG = "GoogleActivity";
    private static final int RC_SIGN_IN = 9001;
    private static final String USER_TAG = "fixitUser";

    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    private EditText usernameSignUp;
    private EditText passwordSignUp;
    private Button signUpbtn;
    private Button btnGoogleSignIn;
    private ImageView ivLogoSignIn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        usernameSignUp = findViewById(R.id.username_signin_et);
        passwordSignUp = findViewById(R.id.password_signin_et);
        signUpbtn = findViewById(R.id.btnSignIn);
        ivLogoSignIn = findViewById(R.id.ivLogoSignIn);
        btnGoogleSignIn = findViewById(R.id.btnGoogleSignIn);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        mAuth = FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser() != null){
            showUserInfo(mAuth.getCurrentUser());
        }

        btnGoogleSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                googleSignIn();
            }
        });
    }



    @Override
    protected void onStart() {
        super.onStart();
    }

    private void emailPassSignIn(String email, String password){
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(SIGN_IN, "signInWithEmail:success");
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            // Name, email address, and profile photo Url
                            String name = user.getDisplayName();
                            String email = user.getEmail();
                            Uri photoUrl = user.getPhotoUrl();


                            // Check if user's email is verified
                            boolean emailVerified = user.isEmailVerified();

                            // The user's ID, unique to the Firebase project. Do NOT use this value to
                            // authenticate with your backend server, if you have one. Use
                            // FirebaseUser.getIdToken() instead.
                            String uid = user.getUid();
                        }
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(SIGN_IN, "signInWithEmail:failure", task.getException());
                    }
                }
            }
        );
    }


    private void emailPassSignUp() {
        String email = usernameSignUp.getText().toString(), password = passwordSignUp.getText().toString();
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.d("SignUp", "createUserWithEmail:success");
                } else {
                    Log.w("SignUp", "createUserWithEmail:failure", task.getException());
                    Toast.makeText(SignInActivity.this, "Authentication failed.",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            showUserInfo(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());

                        }
                    }
                });
    }

    private void googleSignIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void showUserInfo(FirebaseUser user) {
        // Name, email address, and profile photo Url
        FixitUser fixitUser = new FixitUser(user.getDisplayName(), user.getEmail(), String.valueOf(user.getPhotoUrl()));
        Intent intent = new Intent(SignInActivity.this, UserActivity.class);
        intent.putExtra(USER_TAG, fixitUser);
        startActivity(intent);
    }

}