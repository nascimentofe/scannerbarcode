package com.viaexpressa.scannerbarcode.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.viaexpressa.scannerbarcode.R;

public class SplashActivity extends AppCompatActivity {

    private ImageView imgLogo, imgPoligon1, imgPoligon3;
    Animation animation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        imgLogo = (ImageView) findViewById(R.id.imgLogoSplash);
        imgPoligon1 = (ImageView) findViewById(R.id.imgPoligon1);
        imgPoligon3 = (ImageView) findViewById(R.id.imgPoligon3);

        animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.push_down);
        imgLogo.setAnimation(animation);

        animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.push_right);
        imgPoligon1.setAnimation(animation);

        animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.push_left);
        imgPoligon3.setAnimation(animation);

        Thread thread = new Thread(){
            @Override
            public void run() {

                try {
                    sleep(3000);
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                    finish();
                }catch (InterruptedException e){
                    e.printStackTrace();
                }

                super.run();
            }
        };
        thread.start();
    }
}
