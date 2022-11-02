package com.example.mobileandgamingdevices;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Player
{
    private Vector2<Double> m_position;

    public Player(Vector2<Double> position)
    {
        m_position = position;
    }

    public void update()
    {

    }

    public void draw(Canvas canvas)
    {
        Paint paint = new Paint();
        paint.setColor(Color.MAGENTA);

        canvas.drawRect(
                m_position.x.floatValue(),
                m_position.y.floatValue(),
                m_position.x.floatValue() + 50f,
                m_position.y.floatValue() + 50f,
                paint
        );
    }

    public void setPosition(Vector2<Double> position)
    {
        m_position = position;
    }
}
