package com.example.mobileandgamingdevices;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
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

    // ROTATION IN DEGREES : PLAYER_CAR SPRITESHEET INDEX
    private HashMap<Integer, Integer> m_spriteIndices = new HashMap<Integer, Integer>()
    {
        {
            this.put(0, 19);
            this.put(15, 18);
            this.put(30, 17);
            this.put(45, 16);
            this.put(60, 15);
            this.put(75, 14);
            this.put(90, 0);
            this.put(105, 1);
            this.put(120, 2);
            this.put(135, 3);
            this.put(150, 4);
            this.put(165, 5);
            this.put(180, 6);
            this.put(195, 7);
            this.put(210, 8);
            this.put(225, 9);
            this.put(240, 10);
            this.put(255, 11);
            this.put(270, 12);
            this.put(285, 24);
            this.put(300, 23);
            this.put(315, 22);
            this.put(330, 21);
            this.put(345, 20);
        }
    };

    public Player(Vector2 position)
    {
        m_position = position;
        m_size = 320f;
        m_velocity = new Vector2(0d, m_speed);
    }

    public void update()
    {
        if (m_canMove)
        {
            m_rotation += m_targetRotation * m_turningRate;
            if (m_rotation <= 0)
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

            // Work out the closest multiple of 15 to the current rotation
            int currentRotation = closestMultiple((int) m_rotation, 15);

            m_velocity = Vector2.rotate(m_velocity, currentRotation);
            m_position = m_position.add(m_velocity);
        }
    }

    public void draw(Canvas canvas, GameDisplay display)
    {
        synchronized (canvas)
        {
            // Move the top left so the centre of the car is at the centre of the screen
            Vector2 topLeft = new Vector2(
                    m_size / 2d,
                    m_size / 2d).sub(
                    display.worldToScreenSpace(m_position)
            );

            int spriteIndex = m_spriteIndices.get(getSpriteID());

            TextureManager.getInstance().drawSprite(
                    canvas,
                    "PLAYER",
                    spriteIndex,
                    topLeft,
                    m_size
            );
        }
    }

    int closestMultiple(int n, int x)
    {
        if (x > n)
        {
            return 0;
        }

        return x * ((x * n) / (x * x));
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

    private int getSpriteID()
    {
        int spriteID = closestMultiple((int) m_rotation, 15);

        if (spriteID == 360)
        {
            spriteID = 0;
        }

        return spriteID;
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

    public RectF getCollider()
    {
        float scaleFactor = m_size / 64;
        int spriteID = closestMultiple((int) m_rotation, 90);

        RectF r;
        switch (spriteID)
        {
            case 90:
            case 270:
                r = new RectF(
                        2f * scaleFactor,
                        13f * scaleFactor,
                        m_size - scaleFactor,
                        m_size - 14 * scaleFactor
                );
                break;
            default:
                r = new RectF(
                        17f * scaleFactor,
                        3f * scaleFactor,
                        m_size - 16 * scaleFactor,
                        m_size - 4 * scaleFactor
                );
                break;
        }

        r.offset(m_position.x.floatValue(), m_position.y.floatValue());
        return r;
    }

    public void resolveCollision(Vector2 resolutionAmount)
    {
        Log.d("PLAYER", "Resolving collision by: " + resolutionAmount.x + " " + resolutionAmount.y);
        m_position = resolutionAmount.sub(m_position);
    }
}
