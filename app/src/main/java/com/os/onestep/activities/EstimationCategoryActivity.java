package com.os.onestep.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.os.onestep.R;

/**
 * Created by dhruvishah on 11/10/16.
 */
public class EstimationCategoryActivity extends AppCompatActivity implements View.OnClickListener {
    Button btn_internalnotes;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        setContentView(R.layout.activity_estimationcategory);
        getContentID();
        super.onCreate(savedInstanceState);
    }

    private void getContentID()
    {
        btn_internalnotes= (Button)findViewById(R.id.btn_internalnotes);
        btn_internalnotes.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.btn_internalnotes:
                startActivity(new Intent(EstimationCategoryActivity.this, InternalNotesActivity.class));
                break;
        }
    }
}
