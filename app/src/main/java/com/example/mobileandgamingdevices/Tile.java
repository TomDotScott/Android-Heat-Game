package com.example.mobileandgamingdevices;

import android.graphics.RectF;
import android.util.Log;

public class Tile
{
    public Tile(int ID, Vector2 position)
    {
        m_ID = ID;
        m_position = position;
    }

    public int getID()
    {
        return m_ID;
    }

    public Vector2 getPosition()
    {
        return m_position;
    }

    private int m_ID;
    private Vector2 m_position;

    // TODO: Give tiles a rendering priority so that they can appear on top of the player
    // TODO: Make the pavement, road and grass tiles make the player move slower when the player is on top of them
}
