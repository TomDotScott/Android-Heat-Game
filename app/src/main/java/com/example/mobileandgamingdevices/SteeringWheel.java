package com.example.mobileandgamingdevices;

import android.graphics.Canvas;
import android.util.Log;

import com.example.mobileandgamingdevices.graphics.TextureManager;

public class SteeringWheel
{
    private final Vector2 m_centre;
    private final Vector2 m_position;

    private final float m_diameter;
    private boolean m_isPressed = false;
    private float m_centreToPressedDistance;

    private TouchInfo m_activePointer = null;

    // The angle in Degrees that the steering wheel has been turned
    private double m_angle;

    public SteeringWheel(Vector2 position)
    {
        m_diameter = (float)GameDisplay.getScaledValueToScreenWidth(512);
        m_position = position;
        m_centre = new Vector2(position.x + m_diameter / 2, position.y + m_diameter / 2);
    }

    public void update()
    {
        if (m_activePointer != null && m_isPressed)
        {
            calculateAngle();
        } else
        {
            m_angle = 0d;
        }
    }

    public void draw(Canvas canvas)
    {
        synchronized (canvas)
        {
            canvas.save();

            canvas.rotate(
                    (float) m_angle,
                    m_centre.x.floatValue(),
                    m_centre.y.floatValue()
            );

            TextureManager.getInstance().drawSprite(
                    canvas,
                    "UI",
                    0,
                    m_position,
                    m_diameter
            );

            canvas.restore();
        }
    }

    public void checkIfPressed(TouchInfo info)
    {
        if (m_activePointer == null)
        {
            m_centreToPressedDistance = (float) info.TouchPosition.sub(m_centre).sqrMagnitude();

            m_isPressed = m_centreToPressedDistance < Math.pow(m_diameter / 2, 2);

            if (m_isPressed)
            {
                Log.d("STEERING WHEEL", "PRESSED!");
                m_activePointer = info;
            }
        } else
        {
            calculateAngle();
        }
    }

    private void calculateAngle()
    {
        try
        {
            if (m_activePointer == null)
            {
                return;
            }

            m_centreToPressedDistance = (float) m_activePointer.TouchPosition.sub(m_centre).sqrMagnitude();

            // find the angle between the touched point, and the normal of the centre of the steering wheel
            Vector2 centreNormal = new Vector2(0d, 1d);

            m_angle = Vector2.angle(centreNormal, m_activePointer.TouchPosition.sub(m_centre)) * (180 / Math.PI);

            // Limit the angle to be 90 degrees
            if (m_angle > 90d)
            {
                m_angle = 90d;
            }

            // Because vectors are dumb by default and aren't preserving the +/- of the angle,
            // let's reintroduce that
            if (m_activePointer.TouchPosition.sub(m_centre).x > 0)
            {
                m_angle *= -1d;
            }
        } catch (NullPointerException e)
        {
            e.printStackTrace();
        }
    }

    public void fingerReleased(TouchInfo info)
    {
        if (m_activePointer == info)
        {
            m_activePointer = null;
            m_isPressed = false;
            m_angle = 0d;
        }
    }

    public double getAngle()
    {
        return m_angle;
    }
}
