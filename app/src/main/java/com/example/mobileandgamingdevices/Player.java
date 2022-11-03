package com.example.mobileandgamingdevices;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Player
{
    private Vector2 m_position;
    private Vector2 m_velocity;
    private double m_speed = 2.f;

    private double m_rotation;
    private double m_targetRotation;
    private double m_turningRate = 0.04f;

    private boolean m_canMove = false;

    private Vector2 m_size;

    public Player(Vector2 position)
    {
        m_position = position;
        m_size = new Vector2(50d, 50d);
    }

    public void update()
    {
        if(m_canMove)
        {
            m_rotation += m_targetRotation * m_turningRate;
            m_velocity = Vector2.rotate(new Vector2(0d, m_speed), m_rotation);
            m_position = m_position.add(m_velocity);
        }
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

    public void accelerate()
    {
        m_canMove = true;
    }

    public void accelerateReleased()
    {
        m_canMove = false;
    }

    public void brake()
    {
    }

    public void brakeReleased()
    {
    }
}
