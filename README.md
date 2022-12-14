# Uber strEATs
An arcade-style delivery game where you have to deliver food before it gets too cold to earn your 5* rating. If a delivery arrives piping hot, you get awarded bonus time. The game is endless, with the aim to get as best a rating as you can. This game was made for the Mobile Gaming module in Java for the Android platform, as part of Third Year BSc(Hons) Computer Games Programming at Teesside University.
##  The Game
The game primarily uses the touch screen, with on-screen buttons for forward and brake, as well as an onscreen steering wheel which you have to swipe to steer. 
![The Onscreen GUI](https://github.com/TomDotScott/Android-Heat-Game/blob/main/showcase_images/dropoff.jpg?raw=true)
On the main menu, there is the option to turn on Tilt to Steer, utilising the gyroscope to steer the car.  
The aim of the game is to deliver as much food as you can in the timeframe. You get a higher rating /5 depending on the temperature of the food on arrival. 
Most of the time is spent driving between Restaurants (marked by Yellow on the ground) and DropOffs (marked by Green) 
![A Restaurant](https://github.com/TomDotScott/Android-Heat-Game/blob/main/showcase_images/pickup.jpg?raw=true)
When the player arrives at a Restaurant location, they enter a dialogue with the randomised restaurant owner, who gives them the delivery location as well as the type of food that they are going to deliver.
![Restaurant Dialogue](https://github.com/TomDotScott/Android-Heat-Game/blob/main/showcase_images/restaurant_dialogue.jpg?raw=true)
Once an order has been picked up, a Thermometer appears in the top left of the screen, represented as a gradient going from Red to Blue. The temperature of the food is also shown in the top left. 
![Thermometer](https://github.com/TomDotScott/Android-Heat-Game/blob/main/showcase_images/delivery.jpg?raw=true)
If the player doesn't deliver the food in time, they get a time penalty based on the type of food (Burger, Hot Dog or Pizza)

As well as a Thermometer being in the top left, when the player picks up a delivery, the SatNav in the middle of the HUD points in the direction that the player needs to go in to reach the delivery location, denoted by a blinking green area of the map.
![enter image description here](https://github.com/TomDotScott/Android-Heat-Game/blob/main/showcase_images/dropoff.jpg?raw=true)
Following this, they enter a dialogue with the Customer, where they get their rating based on the temperature of the food upon arrival. 
![enter image description here](https://github.com/TomDotScott/Android-Heat-Game/blob/main/showcase_images/customer_dialogue.jpg?raw=true)
Once food has been delivered, the player's average score gets calculated and shown in the top right of the HUD. 

As well as their average score, when the player runs our of time, they are shown the total time taken as well as the amount of deliveries performed in that playthrough on the Game Over screen
![enter image description here](https://github.com/TomDotScott/Android-Heat-Game/blob/main/showcase_images/game_over.jpg?raw=true)
## Activities Used
Different activities have been utilised, with a main menu activity which can spawn an alertdialog to show the credits menu, listing all the assets used in the game. 
![The Main Menu](https://github.com/TomDotScott/Android-Heat-Game/blob/main/showcase_images/main_menu.jpg?raw=true)
There is also a GameActivity which holds the canvas responsible for rendering the game. 
![The Game Activity](https://github.com/TomDotScott/Android-Heat-Game/blob/main/showcase_images/pickup.jpg?raw=true)
Finally, there is a GameOverActivity, which shows your final stats, as well as the option to go back to either of the other activities.
![Game Over, Man!](https://github.com/TomDotScott/Android-Heat-Game/blob/main/showcase_images/game_over.jpg?raw=true)
## User Experience
In the game, every object drawn on the Android Canvas is scaled and positioned relative to the screen size. This has been tested on both my Oppo Find X5 Pro phone and my Samsung Galaxy A8 Tab, showing it  scaling to both a large and small screen. Of course, using constraints, the other Activities scale to all device screen sizes too.  

The game runs at a stable 40fps, which is pretty good considering the amount of draw calls. This could be optimised by utilising OpenGLES on the Android Platform.

The UI is intuitive, with a large steering wheel in the bottom left and the large action buttons in the bottom right. In the centre of the screen is a mobile phone design with an arrow pointing the player in the direction of the restaurant or the drop-off location.

When the player is delivering food, there is a thermometer shown in the upper left-hand corner of the device, and a temperature reading to the left of it.

In the upper right-hand corner, there is a timer counting down once per second. Below is the user’s average rating out of 5*s.

The player is locked to the centre of the screen. This allows for a good user experience due to the user always knowing where to look at the player, even if they go behind objects on the map.

### Credits
- Level Design ~ [Leon Hirst](leonhirst.myportfolio.com/) 
- Art ~ [Limezu](limezu.itch.io/)    
- Characters ~ [Quipinny](quipinny.itch.io/)    
- Squares Font ~ [Gowl](gowldev.itch.io/)    
- Steering Wheel ~ [Adrien Coquet](thenounproject.com/coquet_adrien)    
- Knife and Fork ~ [PJ Souders](thenounproject.com/axoplasm/)    
- 2D Simple UI Pack ~ [Ariel Oliviera](oarielg.itch.io/)    
- Pixel art Pizza & Plate ~ [Vecteezy](vecteezy.com/)    
