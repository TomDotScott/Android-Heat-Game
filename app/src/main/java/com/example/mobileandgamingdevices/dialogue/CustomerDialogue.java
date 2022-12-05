package com.example.mobileandgamingdevices.dialogue;

import static com.example.mobileandgamingdevices.dialogue.Ratings.BURGER_RATING;
import static com.example.mobileandgamingdevices.dialogue.Ratings.HOTDOG_RATING;
import static com.example.mobileandgamingdevices.dialogue.Ratings.PIZZA_RATING;

import com.example.mobileandgamingdevices.Food;
import com.example.mobileandgamingdevices.Game;
import com.example.mobileandgamingdevices.StringTable;

public class CustomerDialogue extends DialogueScene
{
    public CustomerDialogue(Food deliveredFood)
    {
        super(1, Game.RandomInt(0, 5));

        m_food = new Food(deliveredFood);

        String stringTableTitle = "CustomerRating_X_Star".replace("X", String.valueOf(calculateRating())) + Game.RandomInt(1, 5);

        // Get some dialogue from the stringtables
        m_dialogue = StringTable.getInstance().getStringEntry(stringTableTitle);

        // Format the string to remove the formatters
        m_dialogue = m_dialogue.replaceAll("%FOOD%", m_food.getType().toString());
        m_dialogue = m_dialogue.replaceAll("%RESTAURANT%", m_food.getRestaurant());
        m_dialogue = m_dialogue.replaceAll("%NUM%", String.valueOf(m_food.getQuantity()));
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

