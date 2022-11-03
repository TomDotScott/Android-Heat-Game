package com.example.mobileandgamingdevices;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Button
{
    public enum eButtonType
    {
        Circle,
        Rectangle
    }

    private eButtonType m_buttonType;
    private String m_buttonText;
    private Vector2 m_position;
    private double m_size;
    private int m_colour;
    private boolean m_isPressed;

    private TouchInfo m_activePointer = null;

    public Button(eButtonType type, String buttonText, Vector2 position, double size, int colour)
    {
        m_buttonType = type;
        m_buttonText = buttonText;
        m_position = position;
        m_size = size;
        m_colour = colour;
    }

    public void update()
    {

    }

    public void draw(Canvas canvas)
    {
        canvas.save();

        Paint paint = new Paint();
        paint.setColor(m_colour);

        switch (m_buttonType)
        {
            case Circle:
                Vector2 circleCentre = new Vector2(m_position.x + (m_size / 2d), m_position.y + (m_size / 2d));

                canvas.drawCircle(
                        circleCentre.x.floatValue(),
                        circleCentre.y.floatValue(),
                        (float) m_size / 2f,
                        paint
                );
                break;

            case Rectangle:
                Vector2 bottomRight = new Vector2(m_position.x + m_size, m_position.y + m_size);

                canvas.drawRect(
                        m_position.x.floatValue(),
                        m_position.y.floatValue(),
                        bottomRight.x.floatValue(),
                        bottomRight.y.floatValue(),
                        paint
                );
                break;
        }

        paint.setColor(Color.WHITE);

        paint.setTextSize((float) m_size / 2f);

        canvas.drawText(
                m_buttonText,
                m_position.x.floatValue() + ((float) m_size / 2f),
                m_position.y.floatValue() + ((float) m_size / 2f),
                paint
        );

        canvas.restore();
    }

    public void checkIfPressed(TouchInfo info)
    {
        if (m_activePointer == null)
        {
            switch (m_buttonType)
            {
                case Circle:
                    m_isPressed = isPointInsideCircle(info.TouchPosition);
                    break;
                case Rectangle:
                    m_isPressed = isPointInsideRect(info.TouchPosition);
                    break;
            }

            if(m_isPressed)
            {
                m_activePointer = info;
            }
        }
    }

    public void fingerReleased(TouchInfo info)
    {
        if(m_activePointer == info)
        {
            m_activePointer = null;
            m_isPressed = false;
        }
    }

    // TODO: THESE WILL BE HELPFUL FOR COLLISIONS DOWN THE LINE, THEY SHOULD PROBABLY BE IN SOME SORT
    // OF STATIC HELPER CLASS
    private boolean isPointInsideRect(Vector2 point)
    {
        Vector2 topLeft = m_position;
        Vector2 bottomRight = new Vector2(m_position.x + m_size, m_position.y + m_size);

        return point.x <= bottomRight.x &&
                point.x >= topLeft.x &&
                point.y <= bottomRight.y &&
                point.y >= topLeft.y;
    }

    private boolean isPointInsideCircle(Vector2 point)
    {
        Vector2 circleCentre = new Vector2(m_position.x + (m_size / 2d), m_position.y + (m_size / 2d));
        Vector2 centreToPoint = point.sub(circleCentre);

        return Math.abs(centreToPoint.x) <= (m_size / 2d) && Math.abs(centreToPoint.y) <= (m_size / 2d);
    }

    public boolean isPressed()
    {
        return m_isPressed;
    }
}
