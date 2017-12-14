package com.wanplus.customviews;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;

import com.wanplus.scrollingimageview.ScrollingImageView;

public class ScrollingImageViewActivity extends AppCompatActivity {

    ScrollingImageView scrollingImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling_view);

        scrollingImageView = findViewById(R.id.scrollingImageView);

        RadioGroup rgInitialState = findViewById(R.id.radioGroup);
        rgInitialState.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_started:
                        scrollingImageView.start();
                        break;
                    case R.id.rb_stopped:
                        scrollingImageView.stop();
                        break;
                }
            }
        });
        RadioButton rbStarted = findViewById(R.id.rb_started);
        rbStarted.toggle();

        RadioGroup rgOrientation = findViewById(R.id.radioGroup2);
        rgOrientation.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_horizontal:
                        scrollingImageView.setOrientation(ScrollingImageView.HORIZONTAL);
                        break;
                    case R.id.rb_vertical:
                        scrollingImageView.setOrientation(ScrollingImageView.VERTICAL);
                        break;
                }
            }
        });
        RadioButton rbHorizontal = findViewById(R.id.rb_horizontal);
        rbHorizontal.toggle();

        SeekBar sbSpeed = findViewById(R.id.seekBar);
        sbSpeed.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                scrollingImageView.setSpeed((progress - 50) * Utils.dp2px(seekBar.getContext(), 1));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        sbSpeed.setProgress(50);
    }
}
