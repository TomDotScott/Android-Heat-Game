package com.example.mobileandgamingdevices;

import android.graphics.RectF;
import android.util.Log;

public class Tile
{
    public Tile(int ID, Vector2 position, boolean isCollidable)
    {
        m_ID = ID;
        m_position = position;

        m_collider = new RectF(
                position.x.floatValue(),
                position.y.floatValue(),
                position.x.floatValue() + TileMap.TILE_SIZE,
                position.y.floatValue() + TileMap.TILE_SIZE
        );

        m_canCollide = isCollidable;
    }

    public int getID()
    {
        return m_ID;
    }

    public Vector2 getPosition()
    {
        return m_position;
    }

    public boolean checkCollision(Player player)
    {
        if(!m_canCollide)
        {
            return false;
        }

        // Because Java is a stupid programming language, .intersect() overrides the value stored in the
        // Rectangle to the amount they intersect by... Creating a copy to work on
        RectF collider = new RectF(m_collider);
        RectF playerCollider = new RectF(player.getCollider());

//        if(playerCollider.left < collider.right &&
//                playerCollider.right > collider.left &&
//                playerCollider.top < collider.bottom &&
//                playerCollider.bottom > collider.top)
        if(collider.intersect(playerCollider))
        {
            // work out where the player is in relation to the tile
            Vector2 playerPosition = player.getPosition();
            Vector2 resolution = new Vector2();
            if(playerPosition.x < m_position.x)
            {
                // Player is on the left
                resolution.x = Double.valueOf(playerCollider.right - collider.left);
            }
            else
            {
                // Player is on the right
                resolution.x = Double.valueOf(playerCollider.left - collider.right);
            }

            if(playerPosition.y < m_position.y)
            {
                // Player is above
                Log.d("TILE", "Collision Detected above");
                resolution.y = Double.valueOf(playerCollider.bottom - collider.top);
            }
            else
            {
                // Player is below
                Log.d("TILE", "Collision Detected below");
                resolution.y = Double.valueOf(playerCollider.top - collider.bottom);
            }


            // Resolve the collision by moving the player back by x amount
            player.resolveCollision(resolution);
            return true;
        }
        return false;
    }

    private int m_ID;
    private Vector2 m_position;
    private RectF m_collider;
    private boolean m_canCollide;

    // TODO: Give tiles a rendering priority so that they can appear on top of the player
    // TODO: Make the pavement, road and grass tiles make the player move slower when the player is on top of them
}
