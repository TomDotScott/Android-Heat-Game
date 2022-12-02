package com.example.mobileandgamingdevices;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;

import com.example.mobileandgamingdevices.graphics.TextureManager;

public class DialogueScene
{
    private int m_backgroundID;
    private int m_characterID;
    private boolean m_dialogueFinished;

    private static final float CHARACTER_SIZE = 700f;

    private float m_debugTimer = 0f;

    private static final Vector2 TEXTBOX_SIZE = new Vector2(1800, 350);
    private static float TEXTBOX_Y_OFFSET = 300f;

    private final RectF m_textBox = new RectF(
            GameDisplay.SCREEN_WIDTH / 2 - TEXTBOX_SIZE.x.floatValue() / 2,
            TEXTBOX_Y_OFFSET + GameDisplay.SCREEN_HEIGHT / 2 - TEXTBOX_SIZE.y.floatValue() / 2,
            GameDisplay.SCREEN_WIDTH / 2 + TEXTBOX_SIZE.x.floatValue() / 2,
            TEXTBOX_Y_OFFSET + GameDisplay.SCREEN_HEIGHT / 2 + TEXTBOX_SIZE.y.floatValue() / 2
            );

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

        // Draw the text box
        Paint paint = new Paint();

        // The Fill
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.WHITE);
        canvas.drawRect(m_textBox, paint);

        // The Border
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(20f);
        paint.setColor(Color.BLACK);
        canvas.drawRect(m_textBox, paint);
    }

    public boolean finished()
    {
        return m_dialogueFinished;
    }
}
