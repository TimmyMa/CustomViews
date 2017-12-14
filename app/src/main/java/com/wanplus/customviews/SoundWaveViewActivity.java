package com.wanplus.customviews;

import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;

import tech.mlaboratory.soundwaveview.SoundWaveView;

public class SoundWaveViewActivity extends AppCompatActivity {

    SoundWaveView soundWaveView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sound_wave_view);

        soundWaveView = findViewById(R.id.soundWaveView);

        RadioGroup rgLineColor = findViewById(R.id.rg_lineColor);
        rgLineColor.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_black:
                        soundWaveView.setLineColor(Color.BLACK);
                        break;
                    case R.id.rb_red:
                        soundWaveView.setLineColor(Color.RED);
                        break;
                    case R.id.rb_green:
                        soundWaveView.setLineColor(Color.GREEN);
                        break;
                    case R.id.rb_blue:
                        soundWaveView.setLineColor(Color.BLUE);
                        break;
                }
            }
        });
        RadioButton rbBlack = findViewById(R.id.rb_black);
        rbBlack.toggle();

        final SeekBar sbLineNum = findViewById(R.id.sb_lineNum);
        sbLineNum.setMax(5);
        sbLineNum.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                soundWaveView.setLineNum(progress == 0 ? 1 :progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        sbLineNum.setProgress(3);

        final int maxOffset = Utils.dp2px(this, 32);
        SeekBar sbLineOffset = findViewById(R.id.sb_lineOffset);
        sbLineOffset.setMax(100);
        sbLineOffset.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                soundWaveView.setLineOffset(progress / 100f * maxOffset);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        sbLineOffset.setProgress(50);

        final int maxWidth = Utils.dp2px(this, 4);
        SeekBar sbLineWidth = findViewById(R.id.sb_lineWidth);
        sbLineWidth.setMax(100);
        sbLineWidth.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                soundWaveView.setLineWidth(progress / 100f * maxWidth);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        sbLineWidth.setProgress(15);

        final int maxFloatSpeed = 3;
        SeekBar sbWaveFloatSpeed = findViewById(R.id.sb_waveFloatSpeed);
        sbWaveFloatSpeed.setMax(100);
        sbWaveFloatSpeed.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                soundWaveView.setWaveFloatSpeed(progress / 100f * maxFloatSpeed);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        sbWaveFloatSpeed.setProgress(20);

        final int maxWaveHeightRatio = 1;
        SeekBar sbWaveHeightRatio = findViewById(R.id.sb_waveHeightRatio);
        sbWaveHeightRatio.setMax(100);
        sbWaveHeightRatio.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                soundWaveView.setWaveHeightRatio(progress / 100f * maxWaveHeightRatio);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        sbWaveHeightRatio.setProgress(50);

        final int maxWaveNum = 2;
        SeekBar sbWaveNum = findViewById(R.id.sb_waveNum);
        sbWaveNum.setMax(100);
        sbWaveNum.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                soundWaveView.setWaveNum(progress / 100f * maxWaveNum);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        sbWaveNum.setProgress(33);
    }
}
