package com.example.mobileandgamingdevices;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Player
{
    private Vector2 m_position;
    private double m_rotation;
    private double m_targetRotation;
    private double m_turningRate = 0.04f;

    private Vector2 m_size;

    public Player(Vector2 position)
    {
        m_position = position;
        m_size = new Vector2(50d, 50d);
    }

    public void update()
    {
        m_rotation += m_targetRotation * m_turningRate;
    }

    public void draw(Canvas canvas)
    {
        Paint paint = new Paint();
        paint.setColor(Color.MAGENTA);

        synchronized (canvas) {
            // Rotate the canvas
            canvas.save();

            canvas.rotate(
                    (float) m_rotation,
                    m_position.x.floatValue() + (m_size.x.floatValue() / 2f),
                    m_position.y.floatValue() + (m_size.y.floatValue() / 2f)
            );

            canvas.drawRect(
                    m_position.x.floatValue(),
                    m_position.y.floatValue(),
                    m_position.x.floatValue() + m_size.x.floatValue(),
                    m_position.y.floatValue() + m_size.y.floatValue(),
                    paint
            );

            // Restore so everything else isn't drawn wonky!
            canvas.restore();
        }
    }

    public void setPosition(Vector2 position)
    {
        m_position = position;
    }

    public void setRotation(double angle)
    {
        m_targetRotation = angle;
    }
}
