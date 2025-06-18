package com.example.upibudgettracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

public class InfoActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_info, findViewById(R.id.contentContainer), true);

        Window window = getWindow();
        window.getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        );
        window.setStatusBarColor(Color.TRANSPARENT);
    }

    @Override
    protected int getNavBarItemId() {
        return R.id.nav_qr;
    }


    public void contactCompany() {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:dvsv2004@gmail.com"));
        intent.putExtra(Intent.EXTRA_SUBJECT, "Feedback about the app");
        intent.putExtra(Intent.EXTRA_TEXT, "Hi, I wanted to share the following feedback...");

        try {
            startActivity(Intent.createChooser(intent, "Send Email"));
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "No email app found.", Toast.LENGTH_SHORT).show();
        }
    }
}