package com.example.mobileandgamingdevices;

import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class GameOverActivity extends FragmentActivity
{
    private ImageButton m_restartButton;
    private ImageButton m_mainMenuButton;
    private ImageButton m_exitButton;

    private TextView m_totalDeliveries;
    private TextView m_averageRating;
    private TextView m_totalTime;

    public static String TOTAL_DELIVERIES = "";
    public static String AVERAGE_RATING = "";
    public static String TOTAL_GAME_TIME = "";


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);

        m_restartButton = findViewById(R.id.game_over_restart_button);
        m_restartButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(GameOverActivity.this, GameActivity.class);
                startActivity(intent);
            }
        });

        m_mainMenuButton = findViewById(R.id.game_over_main_menu_button);
        m_mainMenuButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                // TODO: SET AN INTENT TO GO TO THE MAIN MENU XML
            }
        });


        m_exitButton = findViewById(R.id.game_over_exit_button);
        m_exitButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                finishAffinity();
            }
        });

        m_totalDeliveries = findViewById(R.id.total_deliveries);
        m_totalDeliveries.setText(TOTAL_DELIVERIES);

        m_averageRating = findViewById(R.id.average_rating);
        m_averageRating.setText(AVERAGE_RATING);

        m_totalTime = findViewById(R.id.total_time);
        m_totalTime.setText(TOTAL_GAME_TIME);
    }
}