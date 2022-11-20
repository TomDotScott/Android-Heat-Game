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
    public final float TILE_SIZE = 128f;

    private class TilePOJO
    {
        public TilePOJO(String ID, Vector2 worldPosition)
        {
            this.ID = ID;
            this.Position = worldPosition;
        }

        public String ID;
        public Vector2 Position;
    }

    // Each CSV file is a layer to be drawn from bottom to top
    // Each layer will have its own properties for collision etc
    private List<List<TilePOJO>> m_tileMap = new ArrayList<>();

    public TileMap(Context context)
    {
        for (int i : new int[]{
                R.raw.newstreet_roads,
                R.raw.newstreet_pavement,
                R.raw.newstreet_buildings,
                R.raw.newstreet_decoration
        })
        {
            try
            {
                m_tileMap.add(openCsvFile(context, i));
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    public void draw(Canvas canvas, GameDisplay display)
    {
        for (List<TilePOJO> layer : m_tileMap)
        {
            for (TilePOJO tile : layer)
            {
                TextureManager.getInstance().drawSprite(
                        canvas,
                        "MAP",
                        String.valueOf(tile.ID),
                        display.worldToScreenSpace(tile.Position),
                        TILE_SIZE,
                        0f
                );
            }
        }
    }

    private List<TilePOJO> openCsvFile(Context context, int resourceId) throws IOException
    {
        InputStream iStream = context.getResources().openRawResource(resourceId);
        BufferedReader reader = new BufferedReader(new InputStreamReader(iStream, Charset.forName("UTF-8")));

        List<TilePOJO> csvContents = new ArrayList<>();
        String line = "";
        int row = 0;

        while ((line = reader.readLine()) != null)
        {
            String[] tileRow = line.split(",");

            Log.d("TILEMANAGER", line);

            for (int i = 0; i < tileRow.length; i++)
            {
                // Work out the data needed to build the tile object
                String ID = tileRow[i];

                if (!ID.equals("-1"))
                {
                    Vector2 position = new Vector2(
                            (double) i * TILE_SIZE,
                            (double) row * TILE_SIZE
                    );

                    csvContents.add(new TilePOJO(ID, position));
                }
            }

            row++;
        }

        Log.d("TILEMANAGER", "READ " + row + " ROWS!");
        Log.d("TILEMANAGER", "SIZE OF ROW " + csvContents.size() + " !");

        return csvContents;
    }
}
