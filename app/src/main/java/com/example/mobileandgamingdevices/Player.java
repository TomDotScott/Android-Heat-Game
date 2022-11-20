package com.example.mobileandgamingdevices;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import com.example.mobileandgamingdevices.graphics.TextureManager;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

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

    private float m_size;
    private Vector2 m_acceleration = new Vector2();
    private double m_accelerationRate = 0.05d;

    // Enum values are the sprite index on the player_car spritesheet
    private enum SpriteDirection
    {
        Left(0),
        Right(12),
        Up(6),
        Down(19);

        public final int m_value;

        SpriteDirection(int value)
        {
            m_value = value;
        }
    }

    private SpriteDirection m_currentSpriteDirection;

    public Player(Vector2 position)
    {
        m_position = position;
        m_size = 256f;
        m_velocity = new Vector2(0d, m_speed);
    }

    public void update()
    {
        if (m_canMove)
        {
            m_rotation += m_targetRotation * m_turningRate;
            if (m_rotation <= -360)
            {
                m_rotation += 360;
            } else if (m_rotation >= 360)
            {
                m_rotation -= 360;
            }

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

            // Log.d("Acceleration  ", String.format("%f   %f", m_acceleration.x.floatValue(), m_acceleration.y.floatValue()));

            m_velocity = Vector2.rotate(m_velocity, m_rotation);
            m_position = m_position.add(m_velocity);
        }
    }

    public void draw(Canvas canvas, GameDisplay display)
    {
        synchronized (canvas)
        {
            // Rotate the canvas
            canvas.save();

            /*
             *             UP
             *        315  360
             *       -45   0    45
             *          \  |  /
             *           \ |/
             *LEFT -90____ | ____90 RIGHT
             *     270    /|\
             *          /  | \
             *     -135   180 \
             *     225        135
             *           DOWN
             */

            Vector2 topLeft = display.worldToScreenSpace(m_position);

            //TODO: CENTRE OF CAR WILL CHANGE DEPENDING ON WHICH SPRITE ITS USING
            final Vector2 centre = display.worldToScreenSpace(new Vector2(
                    m_position.x + (m_size / 2d),
                    m_position.y + (m_size / 2d)
            ));

            canvas.rotate(
                    (float) m_rotation,
                    centre.x.floatValue(),
                    centre.y.floatValue()
            );

            float spriteRotation = 0f;


            // Depending on the rotation, assign a different sprite
            if ((m_rotation <= 45 && m_rotation >= -45) ||
                    m_rotation >= 315)
            {
                spriteRotation = 0f;
                m_currentSpriteDirection = SpriteDirection.Down;
            }
            else if ((m_rotation <= -45 && m_rotation >= -135) ||
                    (m_rotation <= 315 && m_rotation >= 225))
            {
                spriteRotation = 90f;
                m_currentSpriteDirection = SpriteDirection.Right;
            } else if ((m_rotation >= 45 && m_rotation <= 135) ||
                    m_rotation <= -270 && m_rotation >= -360)
            {
                spriteRotation = -90f;
                m_currentSpriteDirection = SpriteDirection.Left;
            } else
            {
                spriteRotation = 180f;
                m_currentSpriteDirection = SpriteDirection.Up;
            }

            TextureManager.getInstance().drawSprite(
                    canvas,
                    "PLAYER",
                    String.valueOf(m_currentSpriteDirection.m_value),
                    topLeft,
                    m_size,
                    spriteRotation
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
