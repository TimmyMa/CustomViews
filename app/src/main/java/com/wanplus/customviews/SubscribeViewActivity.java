package com.wanplus.customviews;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;

import tech.mlaboratory.subscribeview.SubscribeView;

public class SubscribeViewActivity extends AppCompatActivity {

    SubscribeView subscribeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscribe_view);
        subscribeView = findViewById(R.id.view);


        RadioGroup scene = findViewById(R.id.scene_radio_group);
        scene.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.scene_action_bar:
                        subscribeView.setScene(SubscribeView.SCENE_ACTION_BAR);
                        break;
                    case R.id.scene_home:
                        subscribeView.setScene(SubscribeView.SCENE_HOME);
                        break;
                    case R.id.scene_column:
                        subscribeView.setScene(SubscribeView.SCENE_COLUMN);
                        break;
                }
            }
        });

        RadioGroup status = findViewById(R.id.status_radio_group);
        status.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.status_not_subscribe:
                        subscribeView.setStatus(SubscribeView.STATUS_NOT_SUBSCRIBED, true);
                        break;
                    case R.id.status_subscribing:
                        subscribeView.setStatus(SubscribeView.STATUS_SUBSCRIBING, true);
                        break;
                    case R.id.status_subscribed:
                        subscribeView.setStatus(SubscribeView.STATUS_SUBSCRIBED, true);
                        break;
                }
            }
        });

        RadioGroup nightMode = findViewById(R.id.night_mode_radio_group);
        nightMode.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.night_mode_day:
                        subscribeView.setNightMode(false);
                        break;
                    case R.id.night_mode_night:
                        subscribeView.setNightMode(true);
                        break;
                }
            }
        });
    }

    public void notSubScribe(View view) {
        subscribeView.setStatus(SubscribeView.STATUS_NOT_SUBSCRIBED, true);
    }

    public void subscribing(View view) {
        subscribeView.setStatus(SubscribeView.STATUS_SUBSCRIBING, true);
    }

    public void subScribed(View view) {
        subscribeView.setStatus(SubscribeView.STATUS_SUBSCRIBED, true);
    }
}
