package com.example.mobileandgamingdevices;

import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

public class MainMenuActivity extends FragmentActivity
{
    ImageButton m_playButton;
    ImageButton m_optionsButton;
    ImageButton m_exitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);


        m_playButton = findViewById(R.id.play_button);
        m_playButton.setOnClickListener(view ->
        {
            Intent intent = new Intent(MainMenuActivity.this, GameActivity.class);
            startActivity(intent);
        });

        m_optionsButton = findViewById(R.id.options_button);
        m_optionsButton.setOnClickListener(view ->
        {
//            Intent intent = new Intent(MainMenuActivity.this, GameActivity.class);
//            startActivity(intent);

            Log.d("MAIN MENU", "OPTIONS PRESSED!");
        });


        m_exitButton = findViewById(R.id.exit_button);
        m_exitButton.setOnClickListener(view -> finishAffinity());
    }
}