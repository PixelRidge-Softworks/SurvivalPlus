# **ENERGY** ![](https://camo.githubusercontent.com/6f6b6b58796227b55bd88daeee7467d4d471af50419acc1c409b7d99d558713a/68747470733a2f2f692e696d6775722e636f6d2f617945413069422e706e67)

A player's energy is a double value maxed at 20.0.
Their energy will rise and drop based on different conditions
When the player's energy is at max level, they might get some good effects.
When it's at lower levels, the player will suffer some not-so-nice effects.


## **ENERGY DECREASE**

* Throughout the day a player's energy will naturally decrease slowly
* Harder tasks, such as jumping, running and mining, will decrease a player's energy a little faster


## **ENERGY INCREASE**

* When a player sleeps thru the night (ex: all players sleep at the same time and night is skipped) the player's energy will be increased to the maximum value
* If night can't be skipped a player can lay in their bed and their energy will slowly climb
* Drinking [coffee](https://github.com/VetheonGames/SurvivalPlus/wiki/Hydration#coffee-) will increase a player's energy (handy if a player is mining, busy doing a task or not close to a bed)


## **ENERGY EFFECTS**

* When a player's energy is moderately low they will have a harder time mining.
* When a player's energy gets dangerously low they will start to feel sick and might lose vision.
* When a player's energy is at max level, they are rewarded with a little mining buff and some absorption hearts.


## **ENERGY CONFIG**

Many values for the energy system can be modified in the config:

```YAML
# Players will have an energy level out of 20, as it drops, players will get weak and suffer some bad effects
  Energy:
    # With this enabled, players will need to sleep often or risk losing energy
    enabled: true
    # Warn the player via message when energy starts dropping below 10
    warning: true
    # The amount of energy a player loses every 5 seconds of gameplay
    drain-rate: 0.015 # Default (0.015) equivalent to 3.6 levels in 1 MC day (20 minutes)
    # Every 5 seconds a player spends in bed, the level of energy to increase
    sleeping-refresh-rate: 0.83 # Default (0.83) equivalent to 10 levels in 1 minute
    # Players doing exhaustive tasks (such as mining, running, jumping) will lose energy quicker
    # Amount to drop per exhaustion reset (when the player's hunger bar drops) (set to 0 to disable)
    exhaustion: 0.15
    # Enables coffee recipes and effects (Drink coffee to regain energy)
    coffee: true
```

Check out [Status](https://github.com/VetheonGames/SurvivalPlus/wiki/Status) for info on how to check your energy level.