package com.viaexpressa.scannerbarcode.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.viaexpressa.scannerbarcode.R;
import com.viaexpressa.scannerbarcode.classes.Usuario;
import com.viaexpressa.scannerbarcode.service.HTTPService;

public class SplashActivity extends AppCompatActivity {

    private ImageView imgLogo, imgPoligon1, imgPoligon3;
    Animation animation;
    public static final String LOGIN_PREFERENCE = "LOGIN_AUTOMATICO";

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
                    loginAutomatico();
                }catch (InterruptedException e){
                    e.printStackTrace();
                }

                super.run();
            }
        };
        thread.start();
    }

    private void loginAutomatico() {
        SharedPreferences preferences = getSharedPreferences(LOGIN_PREFERENCE, MODE_PRIVATE);
        if (preferences.getInt("id",0) > 0){
            Usuario usuario = new Usuario(
                    preferences.getInt("id",0),
                    preferences.getString("apelido", ""),
                    preferences.getString("senha", ""),
                    preferences.getString("nome", ""),
                    preferences.getString("email", ""),
                    preferences.getString("telefone", ""),
                    preferences.getInt("nivel", 0),
                    preferences.getString("tipo", "")
            );
            HTTPService request = new HTTPService(getApplicationContext());
            request.checkLogin(usuario);
        }else{
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            finish();
        }
    }

}
