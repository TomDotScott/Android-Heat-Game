package com.example.mobileandgamingdevices;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import java.util.LinkedList;

public class Player
{
    private static final double MAX_ACCELERATION = 5d;
    private Vector2 m_position;
    private Vector2 m_velocity;
    private double m_speed = 2.f;

    private double m_rotation;
    private double m_targetRotation;
    private double m_turningRate = 0.04f;

    private boolean m_canMove = false;

    private boolean m_isAccelerating = false;

    private Vector2 m_size;
    private Vector2 m_acceleration = new Vector2();
    private double m_accelerationRate = 0.05d;

    public Player(Vector2 position)
    {
        m_position = position;
        m_size = new Vector2(50d, 50d);
        m_velocity = new Vector2(0d, m_speed);
    }

    public void update()
    {
        if (m_canMove)
        {
            m_rotation += m_targetRotation * m_turningRate;

            m_velocity = new Vector2(0d, m_speed);

            if (m_isAccelerating)
            {
                m_velocity = m_velocity.add(m_acceleration);
            } else
            {
                m_acceleration = new Vector2(0d, m_accelerationRate / 4d).sub(m_acceleration);

                m_velocity = m_velocity.sub(m_acceleration);

                // Make sure we don't move backwards!
                if (m_velocity.y < 0d)
                {
                    m_velocity.y = 0d;
                }
            }

            Log.d("Acceleration  ", String.format("%f   %f", m_acceleration.x.floatValue(), m_acceleration.y.floatValue()));

            m_velocity = Vector2.rotate(m_velocity, m_rotation);
            m_position = m_position.add(m_velocity);
        }
    }

    public void draw(Canvas canvas, GameDisplay display)
    {
        Paint paint = new Paint();
        paint.setColor(Color.MAGENTA);

        synchronized (canvas)
        {
            // Rotate the canvas
            canvas.save();

            final Vector2 topLeft = display.worldToScreenSpace(m_position);

            final Vector2 bottomRight = display.worldToScreenSpace(new Vector2(
                    m_position.x + m_size.x,
                    m_position.y + m_size.y
            ));

            final Vector2 centre = display.worldToScreenSpace(new Vector2(
                    m_position.x + (m_size.x / 2d),
                    m_position.y + (m_size.y / 2d)
            ));


            canvas.rotate(
                    (float) m_rotation,
                    centre.x.floatValue(),
                    centre.y.floatValue()
            );

            canvas.drawRect(
                    topLeft.x.floatValue(),
                    topLeft.y.floatValue(),
                    bottomRight.x.floatValue(),
                    bottomRight.y.floatValue(),
                    paint
            );

            // Restore so everything else isn't drawn wonky!
            canvas.restore();
        }
    }

    public Vector2 getPosition()
    {
        return m_position;
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
        if (!m_isAccelerating)
        {
            Log.d("RESET", "Resetting acceleration");
            m_acceleration = new Vector2();
        }

        m_canMove = true;
        m_isAccelerating = true;
        m_acceleration = m_acceleration.add(new Vector2(0d, m_accelerationRate));

        // Limit the acceleration vector
        if (m_acceleration.sqrMagnitude() > MAX_ACCELERATION * MAX_ACCELERATION)
        {
            // Log.d("Acceleration", "HIT THE MAX ACCELERATION!");
            m_acceleration.normalize();
            m_acceleration = m_acceleration.mult(MAX_ACCELERATION);
        }
    }

    public void accelerateReleased()
    {
        // m_canMove = false;
        m_isAccelerating = false;
    }

    public void brake()
    {
        m_acceleration = new Vector2(0d, m_accelerationRate).sub(m_acceleration);
        if (m_acceleration.y < 0)
        {
            m_acceleration.y = 0d;
            m_canMove = false;
        }
    }

    public void brakeReleased()
    {
    }
}
