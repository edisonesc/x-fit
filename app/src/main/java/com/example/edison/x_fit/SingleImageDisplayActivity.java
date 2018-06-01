package com.example.edison.x_fit;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.module.GlideModule;
import com.klinker.android.sliding.SlidingActivity;
import com.victor.loading.rotate.RotateLoading;

public class SingleImageDisplayActivity extends SlidingActivity implements GlideModule{

    private String image, info;
    private ImageView imageView;
    private RotateLoading rotateLoading;
    @Override
    public void init(Bundle savedInstanceState) {

        setPrimaryColors(
                getResources().getColor(R.color.semi_black_overlay),
                getResources().getColor(R.color.colorPrimaryDark)
        );

        setContent(R.layout.activity_single_image_display);
        rotateLoading = findViewById(R.id.rotateloading);
        rotateLoading.start();
        imageView = findViewById(R.id.singleImage);

        imageView.setVisibility(View.INVISIBLE);
        Bundle imagePrime = getIntent().getExtras();

        if(imagePrime != null){
            image = imagePrime.getString("ImageLink");
            info = imagePrime.getString("Info");
        }
        Glide.clear(imageView);

        imageView.setVisibility(View.VISIBLE);
        Glide.with(getApplicationContext()).load(image).dontTransform().into(imageView);


        setTitle(info);
        rotateLoading.stop();

    }

    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        builder.setDecodeFormat(DecodeFormat.PREFER_ARGB_8888);
    }

    @Override
    public void registerComponents(Context context, Glide glide) {

    }
}
