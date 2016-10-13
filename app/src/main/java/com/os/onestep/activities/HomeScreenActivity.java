package com.os.onestep.activities;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.os.onestep.R;

public class HomeScreenActivity extends AppCompatActivity {

    private ImageView addRoomImage,createProject;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homescreen);
        findViews();
    }

    private void findViews() {
        addRoomImage = (ImageView) findViewById(R.id.addRoomImage);
        createProject = (ImageView) findViewById(R.id.createProject);
        addRoomImage.setColorFilter(ContextCompat.getColor(this,R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
        addRoomImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeScreenActivity.this, AddRoomActivity.class));
            }
        });
        createProject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeScreenActivity.this, NewProjectActivity.class));
            }
        });
    }
}
