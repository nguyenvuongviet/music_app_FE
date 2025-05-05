package com.example.music_app.activities.auth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;

import com.example.music_app.R;
import com.example.music_app.internals.SharedPrefManagerLanguage;
import com.example.music_app.internals.SharedPrefManagerTheme;
import com.google.android.material.button.MaterialButton;

import java.util.Locale;

public class GetStartedActivity extends AppCompatActivity {

    private MaterialButton signUpBtn, signInBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String language = SharedPrefManagerLanguage.getInstance(getApplicationContext()).getLanguage();
        setLocale(language);
        boolean isDarkMode = SharedPrefManagerTheme.getInstance(this).loadNightModeState();
        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        setContentView(R.layout.activity_get_started);

        signUpBtn = (MaterialButton) findViewById(R.id.signUpBtn);
        signInBtn = (MaterialButton) findViewById(R.id.signInBtn);

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GetStartedActivity.this, SignUpActivity.class);
                startActivity(intent);
                finish();
            }
        });

        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GetStartedActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void setLocale(String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());
    }
}