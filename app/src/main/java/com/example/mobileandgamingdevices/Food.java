package com.example.mobileandgamingdevices;

import android.util.Log;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Food
{
    public enum eFoodType
    {
        Pizza("Pizza"),
        Burger("Burger"),
        HotDog("Hot Dog"); // TODO : Maybe swap with or add Parmo?

        private eFoodType(String name)
        {
            this.m_name = name;
        }

        private String m_name;
        private static final List<eFoodType> VALUES = Collections.unmodifiableList(Arrays.asList(values()));
        private static final int SIZE = VALUES.size();
        private static final Random RANDOM = new Random();

        public static eFoodType randomFood()
        {
            return VALUES.get(RANDOM.nextInt(SIZE));
        }
        public String toString() { return this.m_name; }
    }

    private eFoodType m_type;
    private int m_quantity;
    private String m_restaurantName;
    private String m_destinationName;


    private static final FoodStatPOJO PIZZA_STATS = new FoodStatPOJO(70f, 180f);
    private static final FoodStatPOJO BURGER_STATS = new FoodStatPOJO(50f, 80f);
    private static final FoodStatPOJO HOTDOG_STATS = new FoodStatPOJO(40f, 60f);

    private float m_cooldownDuration;

    public Food(eFoodType type, int quantity, String restaurantName, String destinationName)
    {
        switch (type)
        {
            case Pizza:
                m_cooldownDuration = PIZZA_STATS.DURATION;
                break;
            case Burger:
                m_cooldownDuration = BURGER_STATS.DURATION;
                break;
            case HotDog:
                m_cooldownDuration = HOTDOG_STATS.DURATION;
                break;
            default:
                Log.e("FOOD", "UNHANDLED CASE!");
                break;
        }

        m_type = type;
        m_quantity = quantity;
        m_restaurantName = restaurantName;
        m_destinationName = destinationName;
    }

    public Food(Food food)
    {
        m_cooldownDuration = food.m_cooldownDuration;
        m_type = food.m_type;
        m_quantity = food.m_quantity;
        m_restaurantName = food.m_restaurantName;
        m_destinationName = food.m_destinationName;
    }

    public void update()
    {
        m_cooldownDuration -= 0.016f; // TODO: Make it countdown in real-time... For now hardcoding to 60ups
    }

    public float getCooldownPercentage()
    {
        switch (m_type)
        {
            case Pizza:
                return m_cooldownDuration / PIZZA_STATS.DURATION * 100f;
            case Burger:
                return m_cooldownDuration / BURGER_STATS.DURATION * 100f;
            case HotDog:
                return m_cooldownDuration / HOTDOG_STATS.DURATION * 100f;
        }

        Log.e("FOOD", "UNHANDLED CASE! RETURNING -1");
        return -1f;
    }

    public float getTemperature()
    {
        float percentage = getCooldownPercentage() / 100f;

        switch (m_type)
        {
            case Pizza:
                return PIZZA_STATS.TEMPERATURE * percentage;
            case Burger:
                return BURGER_STATS.TEMPERATURE * percentage;
            case HotDog:
                return HOTDOG_STATS.TEMPERATURE * percentage;
        }

        return -1f;
    }

    public eFoodType getType()
    {
        return m_type;
    }

    public int getQuantity()
    {
        return m_quantity;
    }

    public String getRestaurant()
    {
        return m_restaurantName;
    }
}
