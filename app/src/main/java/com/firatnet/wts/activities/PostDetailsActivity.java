package com.firatnet.wts.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.firatnet.wts.R;
import com.firatnet.wts.entities.Post;

public class PostDetailsActivity extends AppCompatActivity {

    TextView title_tv,desc_tv,created_date_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_details);
        title_tv=findViewById(R.id.title_tv);
        desc_tv=findViewById(R.id.desc_tv);
        created_date_tv=findViewById(R.id.created_date_tv);
        Intent intent=getIntent();
        Post post=intent.getParcelableExtra("DETAILS");

        title_tv.setText(post.getTitle());
        desc_tv.setText(post.getDescription());
        created_date_tv.setText(post.getCreated_date());

    }
}
