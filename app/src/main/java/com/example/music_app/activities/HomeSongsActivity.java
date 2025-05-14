package com.example.music_app.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.music_app.R;
import com.example.music_app.internals.SharePrefManagerUser;
import com.example.music_app.models.User;

public class HomeSongsActivity extends AppCompatActivity {
    TextView title;
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_songs);
        User user = SharePrefManagerUser.getInstance(this).getUser();
        AnhXa();
        title.setText("Chào " + user.getFirstName() + " " + user.getLastName() + " 👋");
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            int type = bundle.getInt("Keyy", 0);
            Log.d("Keyy", String.valueOf(type));
            Toast.makeText(HomeSongsActivity.this, String.valueOf(type) , Toast.LENGTH_SHORT).show();
        }
    }

    private void AnhXa() {
        title = findViewById(R.id.title_appbar_home);

    }
}