package com.example.mobileandgamingdevices;

public class Tile
{
    private final int m_ID;
    private final Vector2 m_position;

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
}
