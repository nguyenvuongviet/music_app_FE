package com.example.music_app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.music_app.R;
import com.example.music_app.activities.auth.LoginActivity;
import com.example.music_app.helpers.DialogHelper;
import com.example.music_app.internals.SharePrefManagerUser;
import com.example.music_app.internals.SharedPrefManagerLanguage;
import com.example.music_app.internals.SharedPrefManagerTheme;
import com.example.music_app.listeners.DialogClickListener;
import com.example.music_app.models.User;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SettingActivity extends AppCompatActivity {
    LinearLayout changePasswordLayout, logoutLayout;
    TextView changePasswordText, logoutText;
    RadioButton english, vietnamese;
    SwitchMaterial darkModeSwitch;
    User user;
    private CompoundButton.OnCheckedChangeListener listener;
    ImageView imageView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user = SharePrefManagerUser.getInstance(this).getUser();
        String language = SharedPrefManagerLanguage.getInstance(getApplicationContext()).getLanguage();
        boolean isDarkMode = SharedPrefManagerTheme.getInstance(this).loadNightModeState();
        setContentView(R.layout.activity_setting);
        mapping();

        if (language.equals("vi")) {
            vietnamese.setChecked(true);
            english.setChecked(false);
        } else {
            vietnamese.setChecked(false);
            english.setChecked(true);
        }

        darkModeSwitch.setChecked(isDarkMode);

        int nightMode = AppCompatDelegate.getDefaultNightMode();
        darkModeSwitch.setChecked(nightMode == AppCompatDelegate.MODE_NIGHT_YES);
        listener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                DialogHelper dialog = new DialogHelper(SettingActivity.this, new DialogClickListener() {
                    @Override
                    public void onPositiveButtonClick() {
                        restartApplication();
                        SharedPrefManagerTheme.getInstance(getApplicationContext()).setNightModeState(isChecked);
                    }

                    @Override
                    public void onNegativeButtonClick() {
                        // Temporarily remove the listener
                        darkModeSwitch.setOnCheckedChangeListener(null);

                        // Set the switch to unchecked
                        darkModeSwitch.setChecked(!isChecked);
                        // Add the listener back
                        darkModeSwitch.post(() -> darkModeSwitch.setOnCheckedChangeListener(listener));
                    }
                });
                dialog.show();
                dialog.setTitle(getString(R.string.dialog_title_confirm_restart));
                dialog.setMessage(getString(R.string.dialog_message_theme_confirm_restart));
                dialog.setPositiveButtonContent(getString(R.string.button_ok));
                dialog.setNegativeButtonContent(getString(R.string.button_cancel));
            }
        };
        darkModeSwitch.setOnCheckedChangeListener(listener);

        changePasswordLayout.setOnClickListener(v -> {
            Intent intent = new Intent(SettingActivity.this, ChangePasswordActivity.class);
            startActivity(intent);
        });

        logoutLayout.setOnClickListener(v -> {
            Intent intent = new Intent(SettingActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            removeToken(user.getId());
            SharePrefManagerUser.getInstance(getApplicationContext()).logout();
            finish();
        });

        english.setOnClickListener(view -> {
            DialogHelper dialog = new DialogHelper(view.getContext(), new DialogClickListener() {
                @Override
                public void onPositiveButtonClick() {
                    restartApplication();
                    SharedPrefManagerLanguage.getInstance(getApplicationContext()).saveLanguage("en");
                }

                @Override
                public void onNegativeButtonClick() {
                    english.setChecked(false);
                    vietnamese.setChecked(true);
                }
            });
            dialog.show();
            dialog.setTitle(getString(R.string.dialog_title_confirm_restart));
            dialog.setMessage(getString(R.string.dialog_message_language_confirm_restart));
            dialog.setPositiveButtonContent(getString(R.string.button_ok));
            dialog.setNegativeButtonContent(getString(R.string.button_cancel));
        });

        vietnamese.setOnClickListener(view -> {
            DialogHelper dialog = new DialogHelper(view.getContext(), new DialogClickListener() {
                @Override
                public void onPositiveButtonClick() {
                    restartApplication();
                    SharedPrefManagerLanguage.getInstance(getApplicationContext()).saveLanguage("vi");
                }

                @Override
                public void onNegativeButtonClick() {
                    vietnamese.setChecked(false);
                    english.setChecked(true);
                }
            });
            dialog.show();
            dialog.setTitle(getString(R.string.dialog_title_confirm_restart));
            dialog.setMessage(getString(R.string.dialog_message_language_confirm_restart));
            dialog.setPositiveButtonContent(getString(R.string.button_ok));
            dialog.setNegativeButtonContent(getString(R.string.button_cancel));
        });

        imageView.setOnClickListener(v -> backActivity());
    }

    private void restartApplication() {
        final Intent intent = getPackageManager().getLaunchIntentForPackage(getPackageName());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    void mapping() {
        changePasswordLayout = findViewById(R.id.ChangePasswordTxt);
        changePasswordText = (TextView) changePasswordLayout.getChildAt(0);
        logoutLayout = findViewById(R.id.LogoutTxt);
        logoutText = (TextView) logoutLayout.getChildAt(0);
        english = findViewById(R.id.rad_english);
        vietnamese = findViewById(R.id.rad_vietnamese);
        imageView = findViewById(R.id.back_icon);
        darkModeSwitch = findViewById(R.id.switch_dark_mode);
    }

    void removeToken(int id) {
        String userId = String.valueOf(id);
        Log.e("removeToken", userId);
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://music-app-967da-default-rtdb.asia-southeast1.firebasedatabase.app/");
        DatabaseReference tokenRef = database.getReference("tokenPhone");
        tokenRef.child(userId).removeValue();
    }

    private void backActivity() {
        finish();
    }
}