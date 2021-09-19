package com.example.cloneorganizze1.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.cloneorganizze1.R;
import com.example.cloneorganizze1.activity.LoginActivity;
import com.example.cloneorganizze1.activity.RegisterActivity;
import com.example.cloneorganizze1.config.ConfigurateFirebase;
import com.google.firebase.auth.FirebaseAuth;
import com.heinrichreimersoftware.materialintro.app.IntroActivity;
import com.heinrichreimersoftware.materialintro.slide.FragmentSlide;

public class MainActivity extends IntroActivity {

    FirebaseAuth authStateUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        checkUserState();
        openIntroSlider();
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkUserState();
    }

    public void btnEnter(View view){
        startActivity(new Intent(this, LoginActivity.class));
    }

    public void btnRegister(View view){
        startActivity(new Intent(this, RegisterActivity.class));
    }

    public void checkUserState(){

        authStateUser = ConfigurateFirebase.getFirebaseAuth();

        if(authStateUser.getCurrentUser() != null){

            openAppMainActivity();
        }
    }

    public void openAppMainActivity (){
        startActivity(new Intent(this, AppMainActivity.class));
    }

    public void openIntroSlider(){

        setButtonBackVisible(false);
        setButtonNextVisible(false);

        addSlide(new FragmentSlide.Builder()
                .background(android.R.color.white)
                .fragment(R.layout.intro_slide_1)
                .build());

        addSlide(new FragmentSlide.Builder()
                .background(android.R.color.white)
                .fragment(R.layout.intro_slide_2)
                .build());

        addSlide(new FragmentSlide.Builder()
                .background(android.R.color.white)
                .fragment(R.layout.intro_slide_3)
                .build());

        addSlide(new FragmentSlide.Builder()
                .background(android.R.color.white)
                .fragment(R.layout.intro_slide_4)
                .build());

        addSlide(new FragmentSlide.Builder()
                .background(android.R.color.white)
                .fragment(R.layout.intro_cadastro)
                .canGoForward(false)
                .build());
    }
}

