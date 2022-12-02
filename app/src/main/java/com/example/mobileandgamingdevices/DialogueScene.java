package com.example.mobileandgamingdevices;

import android.graphics.Canvas;
import android.util.Log;

import com.example.mobileandgamingdevices.graphics.TextureManager;

public class DialogueScene
{
    private int m_backgroundID;
    private int m_characterID;
    private boolean m_dialogueFinished;

    private static final float CHARACTER_SIZE = 700f;

    private float m_debugTimer = 0f;

    public DialogueScene(int backgroundID, int characterID)
    {
        m_backgroundID = backgroundID;
        m_characterID = characterID;
        m_dialogueFinished = false;
    }

    public void update()
    {
        m_debugTimer += 0.016f;
        if (m_debugTimer >= 5f)
        {
            m_dialogueFinished = true;
        }
    }

    public void draw(Canvas canvas)
    {
        TextureManager.getInstance().drawSprite(
                canvas,
                "BACKGROUNDS",
                m_backgroundID,
                new Vector2(),
                GameDisplay.SCREEN_WIDTH
        );

        TextureManager.getInstance().drawSprite(canvas,
                "CHARACTERS",
                m_characterID,
                new Vector2(
                        (GameDisplay.SCREEN_WIDTH / 2) - (CHARACTER_SIZE / 2),
                        (GameDisplay.SCREEN_HEIGHT / 2) - (CHARACTER_SIZE * 0.75f)
                ),
                CHARACTER_SIZE
        );
    }

    public boolean finished()
    {
        return m_dialogueFinished;
    }
}
