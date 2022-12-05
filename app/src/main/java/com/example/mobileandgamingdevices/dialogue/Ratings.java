package com.example.mobileandgamingdevices.dialogue;

public class Ratings
{
    public static class RatingPOJO
    {
        public RatingPOJO(int five, int four, int three, int two, int one)
        {
            FIVE_STAR_PERCENTAGE = five;
            FOUR_STAR_PERCENTAGE = four;
            THREE_STAR_PERCENTAGE = three;
            TWO_STAR_PERCENTAGE = two;
            ONE_STAR_PERCENTAGE = one;
        }

        public final int FIVE_STAR_PERCENTAGE;
        public final int FOUR_STAR_PERCENTAGE;
        public final int THREE_STAR_PERCENTAGE;
        public final int TWO_STAR_PERCENTAGE;
        public final int ONE_STAR_PERCENTAGE;
    }

    public static final RatingPOJO PIZZA_RATING = new RatingPOJO(80, 60, 50, 40, 30);
    public static final RatingPOJO BURGER_RATING = new RatingPOJO(75, 70, 50, 45, 20);
    public static final RatingPOJO HOTDOG_RATING = new RatingPOJO(60, 50, 40, 30, 10);

}
