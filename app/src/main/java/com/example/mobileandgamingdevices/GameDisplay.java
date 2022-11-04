package com.example.mobileandgamingdevices;

import android.app.ActionBar;

public class GameDisplay
{
    private Vector2 coordinateOffset;
    private Vector2 m_gameCentre;
    private Vector2 m_displayCentre;
    private Player m_player;


    public GameDisplay(Player player, Vector2 screenSize)
    {
        m_player = player;

        m_displayCentre = new Vector2(
                screenSize.x / 2d,
                screenSize.y / 2d
        );
    }


    public void update()
    {
        m_gameCentre = m_player.getPosition();


        coordinateOffset = m_gameCentre.sub(m_displayCentre);
    }


    public Vector2 worldToScreenSpace(Vector2 point)
    {
        return new Vector2(
                point.x + coordinateOffset.x,
                point.y + coordinateOffset.y
        );
    }
}
