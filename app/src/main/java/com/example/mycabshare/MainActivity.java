package com.example.mycabshare;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private TextView animatingText;
    private ImageView animatingImage;
    Animation animateNow;
    Animation animateimg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //////////hide status bar///////////
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //////////hide status bar///////////

        ////////handler to redirect on next page on given time//////
       new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent= new Intent(MainActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();

            }
        },3000);
        init();
    }
    private void init(){
        animatingText = (TextView) findViewById(R.id.cabshare);
        animateNow= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.upanimation);
        animatingText.setAnimation(animateNow);
        animatingImage = (ImageView) findViewById(R.id.imagecab);
        animateimg = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.right_animation);
        animatingImage.setAnimation(animateimg);

    }
}