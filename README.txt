I considered the following principles of OOP
The aspect of abstraction is explored through only displaying information to the screen that is
required for the user to play the game as intended - prompt for commands, responses to commands,
end of game messages. The user wouldn't need to know how the the bot or logic is implemented so
these are all hidden from the player.
The aspect of data encapsulation is explored through restricting direct access to some components of the
objects, this is done by implementing objects so that its private attributes would only be accessible from
another class through a public accessor, therefore preventing unwanted access to the data. And mutators
to make any changes.
e.g. the coordinates of the player is only accessible via <Player>.getCoord method.
e.g. the tile on a map can only be changed via <Map>.setMap(int,int,char) method.
The aspect of inheritance is explored through letting the HumanPlayer and BotPlayer classes inherit the
properties of the Player class, this improves the code re-usibility, saving time and effort.
The aspect of polymorphism is explored through allowing objects of different types: players (human and bot)
and maps to all be accessed and displayed through the same interface.