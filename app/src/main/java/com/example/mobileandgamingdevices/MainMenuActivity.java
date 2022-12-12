package com.example.mobileandgamingdevices;

import androidx.fragment.app.FragmentActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class MainMenuActivity extends FragmentActivity
{

    private CheckBox m_tiltToSteerCheckbox;

    private AlertDialog.Builder m_alertBuilder;
    private AlertDialog m_creditsDialogue;
    private TextView m_creditsTextView;
    private ImageButton m_creditsPopupBack;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);


        ImageButton m_playButton = findViewById(R.id.play_button);
        m_playButton.setOnClickListener(view ->
        {
            Intent intent = new Intent(MainMenuActivity.this, GameActivity.class);
            Game.TILT_TO_STEER = m_tiltToSteerCheckbox.isChecked();
            startActivity(intent);
        });

        ImageButton m_optionsButton = findViewById(R.id.options_button);
        m_optionsButton.setOnClickListener(view ->
        {
            m_alertBuilder = new AlertDialog.Builder(this);
            final View creditsPopupView = getLayoutInflater().inflate(R.layout.credits, null);

            m_creditsTextView = creditsPopupView.findViewById(R.id.credits_text_box);
            m_creditsPopupBack = creditsPopupView.findViewById(R.id.credits_back_button);

            try
            {
                populateCreditsList();
            } catch (IOException e)
            {
                e.printStackTrace();
            }

            m_alertBuilder.setView(creditsPopupView);
            m_creditsDialogue = m_alertBuilder.create();
            m_creditsDialogue.show();

            m_creditsPopupBack.setOnClickListener(view1 -> m_creditsDialogue.dismiss());
        });

        ImageButton m_exitButton = findViewById(R.id.exit_button);
        m_exitButton.setOnClickListener(view -> finishAffinity());

        m_tiltToSteerCheckbox = findViewById(R.id.tilt_to_steer);
    }

    private void populateCreditsList() throws IOException
    {
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(getResources().openRawResource(R.raw.credits), StandardCharsets.UTF_8)
        );

        String currentLine;

        while ((currentLine = reader.readLine()) != null)
        {
            m_creditsTextView.setText(m_creditsTextView.getText() + "\n\n" + currentLine);
        }
    }
}