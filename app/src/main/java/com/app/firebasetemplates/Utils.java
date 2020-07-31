package com.app.firebasetemplates;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;
import java.util.Objects;

public class Utils {

    public static void setToolbar(Context context, String toolbarTitle) {
        Toolbar toolbar = ((AppCompatActivity) context).findViewById(R.id.toolbar);
        ((AppCompatActivity) context).setSupportActionBar(toolbar);
        TextView bar_text = toolbar.findViewById(R.id.bar_text);
        bar_text.setText(toolbarTitle);
        Objects.requireNonNull(((AppCompatActivity) context).getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        Objects.requireNonNull(((AppCompatActivity) context).getSupportActionBar()).setDisplayShowHomeEnabled(true);
    }

    /*****************
     Android ShareSheet
     ******************/

    public static void androidShareSheetSendText(Context context, String[] text) {

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, text);
        sendIntent.setType("text/*");

        Intent shareIntent = Intent.createChooser(sendIntent, null);
        context.startActivity(shareIntent);
    }


    public static void androidShareSheetShareImage(Context context, Uri uriToImage) {

        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, uriToImage);
        shareIntent.setType("image/*");

        context.startActivity(Intent.createChooser(shareIntent, context.getResources().getText(R.string.send_to)));
    }


    public static void androidShareSheetShareMultipleImages(Context context, ArrayList<Uri> imageUris) {

        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND_MULTIPLE);
        shareIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, imageUris);
        shareIntent.setType("image/*");
        context.startActivity(Intent.createChooser(shareIntent, "Share images to.."));
    }


    public static void androidShareSheetShareTextAndImage(Context context, String[] text, Uri contentUri) {

        Intent sendIntent = new Intent(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, text);

        // (Optional) Here we're setting the title of the content
        sendIntent.putExtra(Intent.EXTRA_TITLE, "Send message");

        // (Optional) Here we're passing a content URI to an image to be displayed
        sendIntent.setData(contentUri);
        sendIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        // Show the ShareSheet
        context.startActivity(Intent.createChooser(sendIntent, null));
    }


}
