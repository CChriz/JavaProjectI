<h1> Game Rules </h1>

The game’s board is rectangular grid. A human player can move around the map
and pick up gold. The goal is to collect enough gold to meet a win condition 
and then exit the dungeon. A bot player, acting as the opponent, will try to catch
the human player. The game is played in turns. On each player’s turn, that player (human
or bot) sends a command and, if the command is successful, an action takes
place.

Below is a list of all valid commands:

HELLO

The response displays the total amount of gold required for the player to
be eligible to win. This number should not decrease as gold as collected.
Example format: Gold to win: <number>

GOLD

The response displays the current gold owned. Example format: Gold
owned: <number>

PICKUP

Picks up the gold on the player’s current location. The response is Success
and the amount of gold that the player has after picking up the gold on the
square. If there is no gold on the square, the response is Fail and the
amount of gold that the player had before attempting PICKUP. Example
format is: Success. Gold owned: <number>

MOVE <direction>

Moves the player one square in the indicated direction. The direction
must be either N, S, E or W. For example, MOVE S. Players cannot
move into walls. The response should be either Success or Fail
depending on whether the move was successful or not.

LOOK

The response is a 5x5 grid, showing the map around the player. The grid
should show walls, empty tiles, gold, exits, and players with the relevant
character or symbol. The calling player must be shown at the center of
the grid with a P (human) or B (bot). Visible areas outside of the map
should be shown as a wall (‘#’).

QUIT

Quits the game. If the player is standing on the exit tile E and owns
enough gold to win, the response is WIN, followed by an optional winning
message. Otherwise, the response is LOSE and quits the game, losing
all progress.

All commands take up a player’s turn, regardless of whether they were
successful or not. Once a command has been entered, the response
should be printed and the turn is over.

The game ends either:

when the human player has collected enough gold and calls the EXIT
command on the exit square

or

when the bot catches (moves onto the same square as) the human player.

