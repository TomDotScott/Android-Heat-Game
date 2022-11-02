package com.example.mobileandgamingdevices;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

public class SteeringWheel
{
    private Vector2 m_outerCirclePos;
    private Vector2 m_innerCirclePos;

    private float m_outerCircleRadius = 150f;
    private float m_innerCircleRadius = 80f;
    private boolean m_isPressed = false;
    private float m_centreToPressedPosition;

    // The angle in Degrees that the steering wheel has been turned
    private double m_angle;

    public SteeringWheel(Vector2 centrePos)
    {
        m_outerCirclePos = centrePos;
        m_innerCirclePos = centrePos;
    }

    public void update()
    {

    }

    public void draw(Canvas canvas)
    {
        Paint paint = new Paint();

        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setColor(Color.RED);

        canvas.drawCircle(
                m_outerCirclePos.x.floatValue(),
                m_outerCirclePos.y.floatValue(),
                m_outerCircleRadius,
                paint
        );

        paint.setColor(Color.BLUE);

        canvas.drawCircle(
                m_innerCirclePos.x.floatValue(),
                m_innerCirclePos.y.floatValue(),
                m_innerCircleRadius,
                paint
        );
    }

    public void checkIfPressed(Vector2 pressedPosition)
    {
        // the steering wheel is pressed if the pressed position is inside the circle...
        setPressedPosition(pressedPosition);
        m_isPressed = m_centreToPressedPosition < m_outerCircleRadius * m_outerCircleRadius;
    }

    public boolean isPressed()
    {
        return m_isPressed;
    }

    public void setPressedPosition(Vector2 pressedPosition)
    {
        m_centreToPressedPosition = (float)pressedPosition.sub(m_outerCirclePos).sqrMagnitude();

        // find the angle between the touched point, and the normal of the centre of the steering wheel
        Vector2 centreNormal = new Vector2(0d, 1d);

        m_angle = Vector2.angle(centreNormal, pressedPosition.sub(m_outerCirclePos)) * (180 / Math.PI);

        // Limit the angle to be 90 degrees
        if(m_angle > 90d)
        {
            m_angle = 90d;
        }

        // Because vectors are dumb by default and aren't preserving the +/- of the angle,
        // let's reintroduce that
        if(pressedPosition.sub(m_outerCirclePos).x > 0)
        {
            m_angle *= -1d;
        }

        Log.d("ANGLE", String.format("Angle: %s", m_angle));
        Log.d("DISTANCE", String.format("CtoP: %s", m_centreToPressedPosition));
    }

    public void fingerReleased()
    {
        m_isPressed = false;
    }

    public double getAngle()
    {
        return m_angle;
    }
}
