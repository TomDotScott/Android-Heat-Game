package com.example.mobileandgamingdevices;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.example.mobileandgamingdevices.graphics.TextureManager;

public class Button
{
    public enum eButtonType
    {
        AccelerateButton,
        BrakeButton
    }

    private eButtonType m_buttonType;
    private Vector2 m_position;
    private double m_size;
    private boolean m_isPressed;

    private TouchInfo m_activePointer = null;

    public Button(eButtonType type, Vector2 position, double size)
    {
        m_buttonType = type;
        m_position = position;
        m_size = size;
    }

    public void update()
    {

    }

    public void draw(Canvas canvas)
    {
        int spriteID = 0;
        switch (m_buttonType)
        {
            case AccelerateButton:
                spriteID = 1;
                break;
            case BrakeButton:
                spriteID = 2;
                break;
        }

        TextureManager.getInstance().drawSprite(
                canvas,
                "UI",
                spriteID,
                m_position,
                (float) m_size
        );
    }

    public void checkIfPressed(TouchInfo info)
    {
        if (m_activePointer == null)
        {
            m_isPressed = isPointInsideCircle(info.TouchPosition);

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
