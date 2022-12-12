package com.example.mobileandgamingdevices;

import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

public class GameOverActivity extends FragmentActivity
{
    public static String TOTAL_DELIVERIES = "";
    public static String AVERAGE_RATING = "";
    public static String TOTAL_GAME_TIME = "";


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);

        ImageButton m_restartButton = findViewById(R.id.game_over_restart_button);
        m_restartButton.setOnClickListener(view ->
        {
            Intent intent = new Intent(GameOverActivity.this, GameActivity.class);
            startActivity(intent);
        });

        ImageButton m_mainMenuButton = findViewById(R.id.game_over_main_menu_button);
        m_mainMenuButton.setOnClickListener(view ->
        {
            Intent intent = new Intent(GameOverActivity.this, MainMenuActivity.class);
            startActivity(intent);
        });


        ImageButton m_exitButton = findViewById(R.id.game_over_exit_button);
        m_exitButton.setOnClickListener(view -> finishAffinity());

        TextView m_totalDeliveries = findViewById(R.id.total_deliveries);
        m_totalDeliveries.setText(TOTAL_DELIVERIES);

        TextView m_averageRating = findViewById(R.id.average_rating);
        m_averageRating.setText(AVERAGE_RATING);

        TextView m_totalTime = findViewById(R.id.total_time);
        m_totalTime.setText(TOTAL_GAME_TIME);
    }

    @Override
    public void onBackPressed()
    {
    }
}