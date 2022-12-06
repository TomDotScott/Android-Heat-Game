package com.example.mobileandgamingdevices;

import android.graphics.Canvas;

import com.example.mobileandgamingdevices.graphics.TextureManager;

public class SmartPhone
{
    private Vector2 m_position;
    private Vector2 m_arrowPosition;

    private float m_angle;
    private float m_width;
    private float m_arrowSize;

    public SmartPhone(Vector2 position)
    {
        m_position = position;
        m_angle = 0f;
        m_width = (float)GameDisplay.getScaledValueToScreenWidth(256);
        m_arrowSize = (float)GameDisplay.getScaledValueToScreenWidth(128);

        m_arrowPosition = new Vector2(
               m_position.x + (double)m_width - (m_arrowSize / 2),
                m_position.y + (m_arrowSize / 2)
        );
    }


    public void draw(Canvas canvas)
    {
        // Draw the phone
        TextureManager.getInstance().drawSprite(
                canvas,
                "UI",
                6,
                m_position,
                m_width
        );

        TextureManager.getInstance().drawSprite(
                canvas,
                "UI",
                7,
                m_position.add(new Vector2(m_width, 0)),
                m_width
        );

        synchronized (canvas)
        {
            canvas.save();

            canvas.rotate(m_angle,
                    m_arrowPosition.x.floatValue() + m_arrowSize / 2f,
                    m_arrowPosition.y.floatValue() + m_arrowSize / 2f
            );

            // We want to draw the arrow in the centre of the phone
            TextureManager.getInstance().drawSprite(
                    canvas,
                    "UI",
                    3,
                    m_arrowPosition,
                    m_arrowSize
            );

            canvas.restore();
        }
    }

    public void calculateRotation(Vector2 playerDirection, Vector2 targetCentre)
    {
        Vector2 dir = playerDirection.sub(targetCentre);

        dir.normalize();

        double n = Math.atan2(dir.y, dir.x) * (180 / Math.PI);
        if (n < 0) n += 360;

        m_angle = (float)n;
    }
}
