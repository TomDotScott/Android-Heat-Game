package com.example.mobileandgamingdevices;

import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;

import com.example.mobileandgamingdevices.graphics.TextureManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class TileMap
{
    public static final float TILE_SIZE = 128f;

    // Each CSV file is a layer to be drawn from bottom to top
    // Each layer will have its own properties for collision etc
    private List<List<Tile>> m_tileMap = new ArrayList<>();
    private List<Tile> m_collidibleTiles = new ArrayList<>();

    public TileMap(Context context)
    {
        try
        {
            m_tileMap.add(openCsvFile(context, R.raw.newstreet_roads, false));

            m_tileMap.add(openCsvFile(context, R.raw.newstreet_pavement, false));

            m_tileMap.add(openCsvFile(context, R.raw.newstreet_buildings, true));

            m_tileMap.add(openCsvFile(context, R.raw.newstreet_decoration, false));
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void draw(Canvas canvas, GameDisplay display)
    {
        for (List<Tile> layer : m_tileMap)
        {
            for (Tile tile : layer)
            {
                TextureManager.getInstance().drawSprite(
                        canvas,
                        "MAP",
                        tile.getID(),
                        display.worldToScreenSpace(tile.getPosition()),
                        TILE_SIZE
                );
            }
        }
    }

    public void checkCollision(Player player)
    {
        // TODO: Make this MUCH more efficient by working out which tile the player is "meant" to be on and checking that
        for (Tile tile : m_collidibleTiles)
        {
            if (tile.checkCollision(player))
            {
                Log.d("TILEMANAGER", "THE PLAYER COLLIDED WITH TILE: " + tile.getID() + " AT POSITION " + tile.getPosition().x + " " + tile.getPosition().y);
            }
        }
    }

    private List<Tile> openCsvFile(Context context, int resourceId, boolean collidableLayer) throws IOException
    {
        InputStream iStream = context.getResources().openRawResource(resourceId);
        BufferedReader reader = new BufferedReader(new InputStreamReader(iStream, Charset.forName("UTF-8")));

        List<Tile> csvContents = new ArrayList<>();
        String line = "";
        int row = 0;

        while ((line = reader.readLine()) != null)
        {
            String[] tileRow = line.split(",");

            Log.d("TILEMANAGER", line);

            for (int i = 0; i < tileRow.length; i++)
            {
                // Work out the data needed to build the tile object
                int ID = Integer.parseInt(tileRow[i]);

                if (ID != -1)
                {
                    Vector2 position = new Vector2(
                            (double) i * TILE_SIZE,
                            (double) row * TILE_SIZE
                    );

                    Tile tile = new Tile(ID, position, collidableLayer);
                    csvContents.add(tile);

                    if(collidableLayer)
                    {
                        m_collidibleTiles.add(tile);
                    }
                }
            }

            row++;
        }

        Log.d("TILEMANAGER", "READ " + row + " ROWS!");
        Log.d("TILEMANAGER", "SIZE OF ROW " + csvContents.size() + " !");

        return csvContents;
    }
}
