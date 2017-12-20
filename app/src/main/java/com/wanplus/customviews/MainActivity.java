package com.wanplus.customviews;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void scrollingImageView(View view) {
        startActivity(new Intent(this, ScrollingImageViewActivity.class));
    }

    public void soundWaveView(View view) {
        startActivity(new Intent(this, SoundWaveViewActivity.class));
    }

    public void subscribeView(View view) {
        startActivity(new Intent(this, SubscribeViewActivity.class));
    }
}
