package com.example.mobileandgamingdevices;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;

public class Thermometer
{
    private float m_temperature;
    float m_cooldownPercentage = 100f;
    private final float m_width;
    private final float m_height;

    public Thermometer()
    {
        m_width = (float) GameDisplay.getScaledValueToScreenWidth(768);
        m_height = (float) GameDisplay.getScaledValueToScreenHeight(150);
    }

    public void draw(Canvas canvas)
    {
        Paint p = new Paint();

        RectF thermometerBounds = new RectF(
                (float) GameDisplay.getScaledValueToScreenWidth(200),
                (float) GameDisplay.getScaledValueToScreenHeight(100),
                m_width,
                (float) GameDisplay.getScaledValueToScreenHeight(100) + m_height
        );

        p.setColor(Color.BLACK);
        canvas.drawRect(thermometerBounds, p);

        RectF gradientBounds = new RectF(
                thermometerBounds.left + (float) GameDisplay.getScaledValueToScreenWidth(5),
                thermometerBounds.top + (float) GameDisplay.getScaledValueToScreenHeight(5),
                thermometerBounds.right - ((thermometerBounds.right - thermometerBounds.left) * (1 - m_cooldownPercentage / 100f)) - (float) GameDisplay.getScaledValueToScreenWidth(5),
                thermometerBounds.bottom - (float) GameDisplay.getScaledValueToScreenHeight(5)
        );

        LinearGradient gradient = new LinearGradient(
                thermometerBounds.left,
                thermometerBounds.top,
                thermometerBounds.right,
                thermometerBounds.bottom,
                0xFF89E0FF,
                0xFFFF2626,
                Shader.TileMode.CLAMP
        );

        // p.setDither(true);
        p.setShader(gradient);

        p.setColor(Color.WHITE);
        canvas.drawRect(gradientBounds, p);

        Paint textPaint = new Paint();
        textPaint.setTextSize((float) GameDisplay.getScaledValueToScreenHeight(50));

        textPaint.setStyle(Paint.Style.STROKE);
        textPaint.setStrokeWidth((float) GameDisplay.getScaledValueToScreenHeight(5));
        textPaint.setColor(Color.BLACK);

        Vector2 textPosition = new Vector2(
                thermometerBounds.left - (float) GameDisplay.getScaledValueToScreenHeight(175),
                thermometerBounds.top + (float) GameDisplay.getScaledValueToScreenHeight((m_height / 2))
        );

        canvas.drawText(
                String.format("%.1f°C", m_temperature),
                textPosition.x.floatValue(),
                textPosition.y.floatValue(),
                textPaint);

        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setColor(Color.RED);
        canvas.drawText(
                String.format("%.1f°C", m_temperature),
                textPosition.x.floatValue(),
                textPosition.y.floatValue(),
                textPaint);
    }

    public void setCoolDownPercentage(float percentage)
    {
        m_cooldownPercentage = percentage;
    }

    public void setTemperature(float temperature)
    {
        m_temperature = temperature;
    }
}
