package com.example.mycabshare;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class LoginActivity extends AppCompatActivity {

    private ImageView marker;
    private Button phoneButton,googleButton;
    Animation phoneAnimate,googleAnimate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        phoneButton=(Button) findViewById(R.id.phbutton);
        googleButton=(Button)findViewById(R.id.gbutton);

        phoneAnimate= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.upanimation);
        googleAnimate= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.upanimation);

        phoneButton.setAnimation(phoneAnimate);
        googleButton.setAnimation(googleAnimate);
        init();
    }
    //////code for animating gif/////
    private void init(){
        marker= (ImageView) findViewById(R.id.markergif);
        Glide.with(this).load(R.drawable.marker).into(marker);
    }
    //////code for animating gif/////

/////////directs to phone login page/////////////
    public void phoneloginclick(View view) {

        Intent intent = new Intent(LoginActivity.this,PhoneLoginActivity.class);
        startActivity(intent);
    }
    /////////directs to phone login page/////////////
}