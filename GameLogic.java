/*
 * Contains the main logic part of the game, as it processes.
 */
import java.util.Objects;
import java.util.Random;
import java.util.Scanner;

public class GameLogic {

    /* Reference to the map being used */
    private Map map;
    // reference to the human player
    private HumanPlayer player;
    // reference to the bot player
    private BotPlayer bot;
    // current state of game
    private Boolean running = true;
    // current player turn
    private Boolean playerTurn = true;

    // Constructor
    public GameLogic() {
        // prompts player for map file location
        Scanner mapChoiceInput = new Scanner(System.in);
        System.out.println("Enter File Location of Map: ");
        String fileLocation = mapChoiceInput.nextLine();
        // read map from file with Map class
        map = new Map(fileLocation);
        // if map read fails, load default map
        if(map.getMapName() == null) {
            map = new Map();
            }
        // spawn the human player on the map
        int[] sp = generateSpawn();
        player = new HumanPlayer(0, sp[0], sp[1], map.getTile(sp[0], sp[1]), 'P');
        map.setMap(sp[0], sp[1], player.getIndicator());
        // spawn the bot player on the map
        sp = generateSpawn();
        bot = new BotPlayer(0, sp[0], sp[1], map.getTile(sp[0], sp[1]), 'B');
        map.setMap(sp[0], sp[1], bot.getIndicator());

        // next line is for testing purposes - prints out whole map
        //System.out.println(map.getMapAll());
    }

    // main method
    public static void main(String[] args) {
        System.out.println("!! Dungeon of Doom !!");
        //map selection

        GameLogic logic = new GameLogic();

        // loop as long as state of game is still running
        while (logic.gameRunning()) {
            // check if the human player is caught by the bot
            if(logic.isCaught()) {
                // if human player is caught, display lose message and gold collected by player
                System.out.println("LOSE.\nYou have been caught by THE BOT!" +
                        "\nGold collected: " + logic.player.getGold() + "/" + logic.map.getGoldRequired() +
                        "\n");
                // ends game after lose message is displayed
                logic.endGame();
                break;
            }
            // check if its human player's turn
            if (logic.isPlayerTurn()) {
                // if human player's turn, prompt for user input in command line
                Scanner cmdInput = new Scanner(System.in);
                System.out.print(":");
                String command = cmdInput.nextLine();
                // process command line input by user after entering
                System.out.println(logic.process(command));
                // if not human player's turn, bots turn begins
            } else {
                // nextAction() method called to decide what the bot's action is this turn
                logic.bot.nextAction();
                String action = logic.bot.getAction();
                // check if the bot's action is look
                if(Objects.equals(action, "look")) {
                    // pass 5x5 string map received from look command to be processed by bot's setNearby() method
                    logic.bot.setNearby(logic.process(action));
                    // prompt bot to calculate what its next destination is after reading its 5x5 minimap
                    logic.bot.nextDestination();
                    // if action is not look, process as normal
                } else {
                    // process bot's action
                    logic.process(action);
                }
            }
            // next line is used for testing purposes - displays current map
            //System.out.println(logic.map.getMapAll());
        }
    }

    // gameRunning() returns if the game is running
    // @return : state of game
    public boolean gameRunning() {
        return running;
    }

    // generateSpawn() returns a legal spawn point coordinates for a player/bot
    // retrieves size of map, takes away border coordinates as its only #
    // @return : valid player spawn point coordinates
    public int[] generateSpawn() {
        Random rand = new Random();
        boolean allowed = false;
        int[] spawnPt = {0, 0};
        // record the size of the map to ensure players are spawned within the map
        int[] max = {map.getMapSize()[0], map.getMapSize()[1]};
        while (!allowed) {
            // loop through both y and x, assigning random integer to each
            for (int i = 0; i < 2; i++) {
                spawnPt[i] = rand.nextInt(max[i] - 2) + 1;
            }
            // if the coordinate is valid (e.g. not a wall '#') then allowed is set to true
            // otherwise the loop is executed again until a valid spawn can be generated
            if (map.canSpawn(spawnPt[0], spawnPt[1])) {
                allowed = true;
            }
        }
        // the valid spawn point is then returned
        return spawnPt;
    }

    // hello () returns the gold required to win for the current map
    // @return : Gold required to win.
    public String hello() {
        if (!isPlayerTurn()) {
            // if bot's turn, let bot know how much gold is needed to win
            bot.setGold_needed(map.getGoldRequired());
        }
        // return string message if human player's turn
        return "Gold to win: " + map.getGoldRequired();
    }

    // gold() returns the total gold currently owned by the target player
    // return : gold owned by the target player
    public String gold(Player targetPlayer) {
        return "Gold owned: " + targetPlayer.getGold();
    }

    // look() returns a 5x5 mini-map surrounding the target player
    // @param : target player to look at
    // @returns : string of 5x5 map surrounding the target player
    public String look(Player targetPlayer) {
        // player position on map taken as integer array (y,x)
        int[] pos = targetPlayer.getCoord();
        StringBuilder miniMap = new StringBuilder();
        // loop through the nearby coordinates of the player position and retrieves tiles
        // so that a 5x5 map can be formed (with current player position as centre)
        // (the map is 'drawn' from top left to bottom right)
        // loop through rows
        for (int y = pos[0] - 2; y < pos[0] + 3; y++) {
            // loop through columns of each row
            for (int x = pos[1] - 2; x < pos[1] + 3; x++) {
                // if x or y is too small or too larger (coordinates outside of map)
                // then the tile is considered as a wall '#' then added to the minimap
                if ((x < 0) || (x > map.getMapSize()[1] - 1) || (y < 0) || (y > map.getMapSize()[0] - 1)) {
                    miniMap.append("#");
                    // if within the map, the tile character is retrieved then concatenated to the string minimap
                } else {
                    miniMap.append(map.getTile(y, x));
                }
            }
            // a new line is added at the end of each row for user readability
            miniMap.append("\n");
        }
        // the completed string minimap is returned
        return String.valueOf(miniMap);
    }

    // movement(direction) updates a player's position on the map and returns if the movement action is successful
    // @param : direction of movement (n,s,e,w)
    // @returns : message, success or fail of attempted movement
    public String movement(Player targetPlayer, String direction) {
        // the player's position before and after moving stored as an integer array - oldPos & newPos
        // the player's position before moving is retrieved using getCoord() method
        int[] oldPos = targetPlayer.getCoord();
        int[] newPos = new int[0];
        // checks which direction the player is to move in then calculate
        // the player's new position after moving according to the direction
        switch (direction) {
            case "n" -> newPos = new int[]{oldPos[0] - 1, oldPos[1]};
            case "s" -> newPos = new int[]{oldPos[0] + 1, oldPos[1]};
            case "e" -> newPos = new int[]{oldPos[0], oldPos[1] + 1};
            case "w" -> newPos = new int[]{oldPos[0], oldPos[1] - 1};
        }
        // retrieves the tile at what would be the player's new position after moving
        // by the getTile(int, int) method, if the tile the player is moving to is
        // a wall '#' then a fail message is returned and the movement is not committed
        char newTile = map.getTile(newPos[0], newPos[1]);
        if (newTile == '#') {
            return "Fail";
        } else {
            // if the tile the player is moving to is not a wall, the tile the player is currently
            // standing on is set back to normal, retrieved by getCurrentTile() method.
            map.setMap(oldPos[0], oldPos[1], targetPlayer.getCurrentTile());
            // set player's current tile to the new tile the player will be standing on after moving
            // by the setCurrentTile(char) method
            targetPlayer.setCurrentTile(newTile);
            // set player indicator and coordinates on the map to the player's new position after moving
            map.setMap(newPos[0], newPos[1], targetPlayer.getIndicator());
            // set player's coordinates to that of the new position after moving
            targetPlayer.setCoord(newPos[0], newPos[1]);
            // return success message
            return "Success";
        }
    }

    // pickup() returns if a player's attempt to pick up gold is successful or not
    // followed by the number of gold owned by the player
    // @param : the target player
    // @return : if the pickup action is successful and the gold owned by the player
    public String pickup(Player targetPlayer) {
        // checks if the current tile stood on by the player is gold 'G'
        if (targetPlayer.getCurrentTile() == 'G') {
            // call method to increment player's gold count
            targetPlayer.collectedGold();
            // call method to set the tile to and empty space as gold has been collected '.'
            targetPlayer.setCurrentTile('.');
            // return messages on whether the pickup was successful or not and the player's current gold count
            return "Success. Gold owned: " + targetPlayer.getGold();
        } else {
            return "Fail. Gold owned: " + targetPlayer.getGold();
        }
    }

    // quit() checks if player is allowed to quit and return appropriate message
    // @param : target player
    // @return : if the player has won or lost, nothing if player not on exit tile
    public String quit(Player targetPlayer) {
        // checks if the player is currently standing on an exit tile
        if (targetPlayer.getCurrentTile() == 'E') {
            // if player standing on exit tile, check if player's owned gold is enough to win
            if (targetPlayer.getGold() == map.getGoldRequired()) {
                // if player has enough gold to win, stop game and return win message
                running = false;
                // if human player has won, return win message
                if (targetPlayer == player) {
                    return "WIN" + "\nGold collected: " + targetPlayer.getGold() + "/" + map.getGoldRequired() +
                            "\n";
                    // if bot has won, output lose message
                } else {
                    System.out.println("LOSE");
                    return "LOSE";
                }
                // if player doesn't have enough gold to win, stop game and return lose message
            } else {
                running = false;
                return "LOSE";
            }
            // if player not currently standing on an exit tile, return nothing, game continues
        } else {
            return "";
        }
    }

    // isCaught() checks if the bot and player are on the same tile then returns if the player has been caught by the bot
    // @return : boolean value - if player has been caught by the bot
    public Boolean isCaught() {
        // format (y,x)
        int[] p = player.getCoord();
        int[] b = bot.getCoord();
        // returns True if both coordinates match, False if both coordinates do not match
        return p[0] == b[0] && p[1] == b[1];
    }

    // isPlayerTurn() returns if it is the human player's turn
    // @return : boolean value of playerTurn
    public Boolean isPlayerTurn() {
        return playerTurn;
    }

    // endTurn() reassigns opposite boolean value to playerTurn
    private void endTurn() {
        playerTurn = !playerTurn;
    }

    // endGame() sets running to false, terminating the game
    private void endGame() {
        running = false;
    }

    // process(command) processes input commands from both the bot and player from command line
    // @param : command entered by player or bot
    // @return : response of any command executed
    public String process(String command) {
        try {
            // convert the command to lower case
            command = command.toLowerCase();
        } catch (NullPointerException e) {
            // end current player's turn if an error has occurred
            endTurn();
            return "";
        }
        // set the target player of commands to the player of current turn
        Player targetPlayer = player;
        if (!isPlayerTurn()) {
            targetPlayer = bot;
        }
        // checks if input command by the player is one of the following cases
        // the command is then processed if valid
        String output = "";
        switch (command) {
            case "hello" -> output = hello();
            case "gold" -> output = gold(targetPlayer);
            case "look" -> output = look(targetPlayer);
            case "n", "s", "e", "w" -> output = movement(targetPlayer, command);
            case "pickup" -> output = pickup(targetPlayer);
            case "quit" -> output = quit(targetPlayer);
        }
        // ends the current player's turn then returns the output from processing the command
        endTurn();
        return output;
        // nothing is outputted if command is invalid
    }

}
