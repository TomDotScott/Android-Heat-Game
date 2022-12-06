package com.example.mobileandgamingdevices;

import android.graphics.Canvas;

import com.example.mobileandgamingdevices.graphics.TextureManager;

public class SmartPhone
{
    private Vector2 m_position;
    private float m_angle;
    private float m_size;

    public SmartPhone(Vector2 position)
    {
        m_position = position;
        m_angle = 0f;
        m_size = (float)GameDisplay.getScaledValueToScreenWidth(192);
    }


    public void draw(Canvas canvas)
    {
        synchronized (canvas)
        {
            canvas.save();

            canvas.rotate(m_angle,
                    m_position.x.floatValue() + m_size / 2f,
                    m_position.y.floatValue() + m_size / 2f
            );

            TextureManager.getInstance().drawSprite(
                    canvas,
                    "UI",
                    3,
                    m_position,
                    m_size
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
