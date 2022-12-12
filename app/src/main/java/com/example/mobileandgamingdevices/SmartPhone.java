package com.example.mobileandgamingdevices;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.example.mobileandgamingdevices.graphics.TextureManager;

public class SmartPhone
{
    private final Vector2 m_position;
    private final Vector2 m_arrowPosition;

    private float m_angle;
    private final float m_width;
    private final float m_arrowSize;

    private String m_targetName;

    public SmartPhone(Vector2 position)
    {
        m_position = position;
        m_angle = 0f;
        m_width = (float) GameDisplay.getScaledValueToScreenWidth(256);
        m_arrowSize = (float) GameDisplay.getScaledValueToScreenWidth(128);

        m_arrowPosition = new Vector2(
                m_position.x + (double) m_width - (m_arrowSize / 2),
                m_position.y + (m_arrowSize / 2)
        );
    }


    public void draw(Canvas canvas, Vector2 playerPosition, Vector2 targetPosition)
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

        Vector2 textPosition = new Vector2(
                (m_width / 2d) + m_position.x,
                m_arrowPosition.y + GameDisplay.getScaledValueToScreenHeight(150)
        );

        String targetText = String.format(
                "%.2fm to: %s",
                Math.max((targetPosition.sub(playerPosition).magnitude() / 10) - 40, 0),
                m_targetName
        );

        Paint p = new Paint();
        p.setColor(Color.WHITE);
        p.setTextSize((float) GameDisplay.getScaledValueToScreenHeight(20));

        p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth((float) GameDisplay.getScaledValueToScreenHeight(3));
        p.setColor(Color.BLACK);

        canvas.drawText(
                targetText,
                textPosition.x.floatValue(),
                textPosition.y.floatValue(),
                p
        );

        p.setStyle(Paint.Style.FILL);
        p.setColor(Color.WHITE);

        canvas.drawText(
                targetText,
                textPosition.x.floatValue(),
                textPosition.y.floatValue(),
                p
        );
    }

    public void calculateRotation(Vector2 playerDirection, Vector2 targetCentre)
    {
        Vector2 dir = playerDirection.sub(targetCentre);

        dir.normalize();

        double n = Math.atan2(dir.y, dir.x) * (180 / Math.PI);
        if (n < 0) n += 360;

        m_angle = (float) n;
    }

    public void setTargetName(String targetName)
    {
        m_targetName = targetName;
    }
}
