package com.example.mobileandgamingdevices.dialogue;

import static com.example.mobileandgamingdevices.dialogue.Ratings.BURGER_RATING;
import static com.example.mobileandgamingdevices.dialogue.Ratings.HOTDOG_RATING;
import static com.example.mobileandgamingdevices.dialogue.Ratings.PIZZA_RATING;

import android.graphics.Canvas;

import com.example.mobileandgamingdevices.Food;
import com.example.mobileandgamingdevices.Game;
import com.example.mobileandgamingdevices.GameDisplay;
import com.example.mobileandgamingdevices.StringTable;
import com.example.mobileandgamingdevices.Vector2;
import com.example.mobileandgamingdevices.graphics.TextureManager;

public class CustomerDialogue extends DialogueScene
{
    private int m_rating = 5;

    public CustomerDialogue(Food deliveredFood)
    {
        super(1, Game.RandomInt(0, 9));

        m_food = new Food(deliveredFood);

        m_rating = calculateRating();

        String stringTableTitle = "CustomerRating_X_Star".replace("X", String.valueOf(m_rating)) + Game.RandomInt(1, 5);

        // Get some dialogue from the stringtables
        m_dialogue = StringTable.getInstance().getStringEntry(stringTableTitle);

        // Format the string to remove the formatters
        m_dialogue = m_dialogue.replaceAll("%FOOD%", m_food.getType().toString());
        m_dialogue = m_dialogue.replaceAll("%RESTAURANT%", m_food.getRestaurant());
        m_dialogue = m_dialogue.replaceAll("%NUM%", String.valueOf(m_food.getQuantity()));
    }

    @Override
    public void draw(Canvas canvas)
    {
        super.draw(canvas);

        // Draw the stars depending on the rating
        float starSize = (float) GameDisplay.getScaledValueToScreenHeight(128);

        for(int i = 0; i < 5; i++)
        {
            float padding = (float) GameDisplay.getScaledValueToScreenWidth(133f);

            Vector2 starPosition = GameDisplay.getScaledVector2ToScreenSize(new Vector2(
                    GameDisplay.getScaledValueToScreenWidth(768) + (i * padding),
                    GameDisplay.SCREEN_HEIGHT - GameDisplay.getScaledValueToScreenHeight(128)
                    ));

            // Draw the empty star
            TextureManager.getInstance().drawSprite(
                    canvas,
                    "UI",
                    5,
                    starPosition,
                    starSize
            );

            if(m_rating > i)
            {
                // Draw the filled in star
                TextureManager.getInstance().drawSprite(
                        canvas,
                        "UI",
                        4,
                        starPosition,
                        starSize
                );
            }
        }
    }

    public int calculateRating()
    {
        int percentage = (int) m_food.getCooldownPercentage();

        Ratings.RatingPOJO pojo = null;

        switch (m_food.getType())
        {
            case Pizza:
                pojo = PIZZA_RATING;
                break;
            case Burger:
                pojo = BURGER_RATING;
                break;
            case HotDog:
                pojo = HOTDOG_RATING;
                break;
        }

        if (percentage >= pojo.FIVE_STAR_PERCENTAGE)
        {
            return 5;
        } else if (percentage >= pojo.FOUR_STAR_PERCENTAGE)
        {
            return 4;
        } else if (percentage >= pojo.THREE_STAR_PERCENTAGE)
        {
            return 3;
        } else if (percentage >= pojo.TWO_STAR_PERCENTAGE)
        {
            return 2;
        }

        return 1;
    }
}

