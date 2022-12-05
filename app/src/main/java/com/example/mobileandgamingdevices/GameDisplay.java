package com.example.mobileandgamingdevices;

public class GameDisplay
{
    private Vector2 coordinateOffset;
    private Vector2 m_gameCentre;
    private Vector2 m_displayCentre;
    private Player m_player;

    public static int SCREEN_HEIGHT;
    public static int SCREEN_WIDTH;
    private static final int REFERENCE_SCREEN_WIDTH = 2176;
    private static final int REFERENCE_SCREEN_HEIGHT = 1080;


    public GameDisplay(Vector2 screenSize)
    {
        SCREEN_WIDTH = screenSize.x.intValue();
        SCREEN_HEIGHT = screenSize.y.intValue();

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

    public void setPlayerReference(Player player)
    {
        m_player = player;
    }

    public Vector2 worldToScreenSpace(Vector2 point)
    {
        return new Vector2(
                point.x + coordinateOffset.x,
                point.y + coordinateOffset.y
        );
    }

    public static double getScaledValueToScreenWidth(double value)
    {
        return value / REFERENCE_SCREEN_WIDTH * SCREEN_WIDTH;
    }

    public static double getScaledValueToScreenHeight(double value)
    {
        return value / REFERENCE_SCREEN_HEIGHT * SCREEN_HEIGHT;
    }

    public static Vector2 getScaledVector2ToScreenSize(Vector2 value)
    {
        return new Vector2(getScaledValueToScreenWidth(value.x), getScaledValueToScreenHeight(value.y));
    }
}
