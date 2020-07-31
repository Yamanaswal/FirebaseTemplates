package com.app.firebasetemplates;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;

public class FirebaseUtils {

    private static final String TAG = "FirebaseUtils";
    // Initialize Firebase Authentication
    private static FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    //Getting Firebase Instance(OBJECT)
    private static FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    //Getting Database Reference /ROOT Or BASE_URl/
    private static DatabaseReference databaseReference = firebaseDatabase.getReference();
    //Getting Storage Reference
    private static StorageReference storageReference = FirebaseStorage.getInstance().getReference();

    private static String UserId;

    public static String getUserId() {
        return UserId;
    }

    /* [ Firebase SignUp New-Users - in USERS Model Class] */
    private static void signUpWithEmail(final Context context, final String email, String password) {

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener((Activity) context, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            Users newUser = new Users(email);
                            FirebaseUser currentUser = firebaseAuth.getCurrentUser();
                            FirebaseDatabase.getInstance().getReference("users")
                                    .child(currentUser.getUid())
                                    .setValue(newUser)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(context, "Account Created !!", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(context, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }
    //[ End -> Sign_Up_In_Firebase ]//

    /* [ Firebase Login Existing-Users ] */
    private static void signInWithEmail(final Context context, String email, String password) {

        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener((Activity) context, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            Log.d(TAG, "onComplete: " + user.getUid());
                            UserId = user.getUid();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.d(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(context, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    /*
     Update User's Profile
   */
    private static void updateProfile(FirebaseUser firebaseUser, String displayName, String imagePath) {

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(displayName)
                .setPhotoUri(Uri.parse(imagePath))
                .build();

        firebaseUser.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "User profile updated..");
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: " + e.getMessage());
            }
        });

    }

    //Sign Out Current sUser
    private static void firebaseSignOut() {
        firebaseAuth.signOut();
    }

    /*
     * Firebase Storage
     */

    /* Upload Image into Firebase */
    private static void uploadFile(String ImagePath) {

        String filename = ImagePath.substring(ImagePath.lastIndexOf("/") + 1);
        Uri file = Uri.fromFile(new File(ImagePath));
        StorageReference storage = storageReference.child("images/" + filename + ".jpg");

        storage.putFile(file)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get a URL to the uploaded content
                        Uri downloadUrl = taskSnapshot.getUploadSessionUri();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        // ...

                    }
                });
    }


    /* Download Image from Firebase */
    private static void downloadFile() {

        File localFile = null;
        try {
            localFile = File.createTempFile("firebase_images", "jpg");
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert localFile != null;
        storageReference.getFile(localFile)
                .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        // Successfully downloaded data to local file
                        // ...

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle failed download
                // ...
            }
        });
    }


}
