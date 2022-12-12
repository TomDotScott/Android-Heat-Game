package com.example.mobileandgamingdevices.dialogue;

import static com.example.mobileandgamingdevices.Game.GAME_TYPEFACE;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

import com.example.mobileandgamingdevices.Food;
import com.example.mobileandgamingdevices.GameDisplay;
import com.example.mobileandgamingdevices.Vector2;
import com.example.mobileandgamingdevices.graphics.TextureManager;

public abstract class DialogueScene
{
    private final float m_textSize;
    private final int m_backgroundID;
    private final int m_characterID;
    private boolean m_dialogueFinished;

    private static final float CHARACTER_SIZE = 700f;

    private float m_debugTimer = 0f;

    private final RectF m_textBox;

    protected String m_dialogue = "";

    protected Food m_food = null;
    private final float m_padding;

    public DialogueScene(int backgroundID, int characterID)
    {
        m_backgroundID = backgroundID;
        m_characterID = characterID;
        m_dialogueFinished = false;

        Vector2 textboxSize = GameDisplay.getScaledVector2ToScreenSize(new Vector2(1800, 350));

        float textBoxOffsetY = (float) GameDisplay.getScaledValueToScreenHeight(300);

        m_textBox = new RectF(
                GameDisplay.SCREEN_WIDTH / 2f - (float)GameDisplay.getScaledValueToScreenWidth(textboxSize.x / 2d),
                textBoxOffsetY + GameDisplay.SCREEN_HEIGHT / 2f - (float)GameDisplay.getScaledValueToScreenHeight(textboxSize.y / 2d),
                GameDisplay.SCREEN_WIDTH / 2f + (float)GameDisplay.getScaledValueToScreenWidth(textboxSize.x / 2d),
                (float)GameDisplay.getScaledValueToScreenHeight(textBoxOffsetY) + GameDisplay.SCREEN_HEIGHT / 2f + (float)GameDisplay.getScaledValueToScreenHeight(textboxSize.y / 2d)
        );

        m_textSize = (float)GameDisplay.getScaledValueToScreenWidth(80);
        m_padding = (float)GameDisplay.getScaledValueToScreenHeight(1.1);
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

        TextureManager.getInstance().drawSprite(
                canvas,
                "CHARACTERS",
                m_characterID,
                new Vector2(
                        (GameDisplay.SCREEN_WIDTH / 2) - GameDisplay.getScaledValueToScreenWidth(CHARACTER_SIZE) / 2,
                        (GameDisplay.SCREEN_HEIGHT / 2) - GameDisplay.getScaledValueToScreenWidth(CHARACTER_SIZE) * 0.75f
                ),
                (float)GameDisplay.getScaledValueToScreenWidth(CHARACTER_SIZE)
        );

        // Draw the text box
        Paint paint = new Paint();

        paint.setTypeface(GAME_TYPEFACE);

        // The Fill
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.WHITE);
        canvas.drawRect(m_textBox, paint);

        // The Border
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(20f);
        paint.setColor(Color.BLACK);
        canvas.drawRect(m_textBox, paint);

        // The Text
        paint.setStyle(Paint.Style.FILL);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(m_textSize);
        paint.setStrokeWidth(1f);
        paint.setColor(Color.rgb(56, 56, 56));

        String[] lines = m_dialogue.split("#");

        float baseline = -paint.ascent();
        for (int i = 0; i < lines.length; ++i)
        {
            canvas.drawText(
                    lines[i],
                    m_textBox.centerX(),
                    m_textBox.top + baseline + m_textSize * m_padding * i,
                    paint
            );
        }
    }

    public boolean finished()
    {
        return m_dialogueFinished;
    }

    public Food getFood()
    {
        return m_food;
    }
}
