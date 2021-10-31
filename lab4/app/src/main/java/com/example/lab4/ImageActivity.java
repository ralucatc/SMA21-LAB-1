package com.example.lab4;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;
import com.example.lab4.MyApplication;
import com.example.lab4.R;

public class ImageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        // check if application has expected payload
        MyApplication myApplication = (MyApplication) getApplicationContext();
        if (myApplication.getBitmap() == null) {
            Toast.makeText(this, "Error transmitting URL.", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            ImageView imageView = (ImageView) findViewById(R.id.imageView);
            imageView.setImageBitmap(myApplication.getBitmap());
        }
    }
}