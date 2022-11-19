package com.quirkygames.heatgame;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Player
{
    Texture m_texture;
    Vector2 m_position;
    Vector2 m_speed;


    public Player(Vector2 position)
    {
        m_texture = new Texture("badlogic.jpg");
        m_speed = new Vector2(0, 5);
        m_position = position;
    }


    public void update()
    {
        m_position = m_position.add(m_speed);
    }

    public void draw(SpriteBatch batch)
    {
        batch.draw(m_texture, m_position.x, m_position.y);
    }

    public void dispose()
    {
        m_texture.dispose();
    }
}
