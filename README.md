Extra Implementation for the Bot Player:
- mimics the player, will actively look for and pick up gold, with the priority of chasing down the player
- pathing calculated each time after 'look' command to view surroundings


I considered the following principles of **Object Oriented Programming**

The aspect of abstraction is explored by only displaying information to the screen that is
required for the user to play the game as intended - prompt for commands, responses to commands,
end of game messages. The user wouldn't need to know how the bot or logic is implemented so
these are all hidden from the player.

The aspect of data encapsulation is explored through restricting direct access to some components of the
objects, this is done by implementing objects so that their private attributes would only be accessible from
another class through a public accessor, therefore preventing unwanted access to the data. And mutators
to make any changes.

e.g. the coordinates of the player are only accessible via <Player>.getCoord method.

e.g. the tile on a map can only be changed via <Map>.setMap(int,int,char) method.

The aspect of inheritance is explored through letting the HumanPlayer and BotPlayer classes inherit the
properties of the Player class, which improves the code re-usability.

The aspect of polymorphism is explored through allowing objects of different types: players (human and bot)
and maps to all be accessed and displayed through the same interface.
