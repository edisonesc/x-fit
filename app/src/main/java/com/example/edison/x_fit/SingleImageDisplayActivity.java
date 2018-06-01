package com.example.edison.x_fit;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.module.GlideModule;
import com.klinker.android.sliding.SlidingActivity;

public class SingleImageDisplayActivity extends SlidingActivity implements GlideModule{

    private String image, info;
    private ImageView imageView;
    @Override
    public void init(Bundle savedInstanceState) {

        setPrimaryColors(
                getResources().getColor(R.color.semi_black_overlay),
                getResources().getColor(R.color.colorPrimaryDark)
        );

        setContent(R.layout.activity_single_image_display);
        imageView = findViewById(R.id.singleImage);
        Bundle imagePrime = getIntent().getExtras();
        if(imagePrime != null){
            image = imagePrime.getString("ImageLink");
            info = imagePrime.getString("Info");
        }
        Glide.clear(imageView);
        Glide.with(getApplicationContext()).load(image).dontTransform().into(imageView);
        setTitle(info);

    }

    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        builder.setDecodeFormat(DecodeFormat.PREFER_ARGB_8888);
    }

    @Override
    public void registerComponents(Context context, Glide glide) {

    }
}
