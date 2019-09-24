package com.firatnet.wst.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.firatnet.wst.R;
import com.firatnet.wst.entities.Post;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class PostDetailsActivity extends AppCompatActivity {

    TextView title_tv,desc_tv,created_date_tv,category_tv,price_tv,contact_no_tv;
    ImageView image;
    ImageLoader imageLoader = ImageLoader.getInstance();
    ImageLoaderConfiguration config;
    DisplayImageOptions options;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_details);
        title_tv=findViewById(R.id.title_tv);
        desc_tv=findViewById(R.id.desc_tv);
        created_date_tv=findViewById(R.id.created_date_tv);
        category_tv=findViewById(R.id.category_tv);
        price_tv=findViewById(R.id.price_tv);
        contact_no_tv=findViewById(R.id.contact_no_tv);
        image=findViewById(R.id.image);
        Intent intent=getIntent();
        Post post=intent.getParcelableExtra("DETAILS");
        context=this;

        title_tv.setText(post.getTitle());
        desc_tv.setText(post.getDescription());
        created_date_tv.setText(post.getCreated_date());
        category_tv.setText(post.getCategory());
        price_tv.setText(post.getPrice());
        contact_no_tv.setText(post.getContact_no());

        config = new ImageLoaderConfiguration.Builder(context)
                .build();
        ImageLoader.getInstance().init(config);
        options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
        if (!post.getPost_image_url().isEmpty() && !post.getPost_image_url().equals("null"))
        {
            imageLoader.displayImage(post.getPost_image_url(), image, options);
            image.setVisibility(View.VISIBLE);
        }

    }
}
