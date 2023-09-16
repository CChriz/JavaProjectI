import java.util.ArrayList;
import java.util.Random;
import java.util.List;

// class for the bot player, a subclass of player
// inherits all attributes and methods of player
// has private methods limited to the bot player subclass for decision-making
public class BotPlayer extends Player {
    // the action to be entered by the bot this round
    private String action;
    // 2d char array storing the nearby map 'seen' by the bot through look command
    private char[][] nearby;
    // the gold needed to win the map received by 'hello' command
    private int gold_needed;
    // arraylist storing moves to be performed by the bot in following rounds
    private ArrayList<String> moves = new ArrayList<>();
    // round number counter
    private int round = 1;

    // Constructor
    public BotPlayer(int gold, int y, int x, char currentTile, char indicator) {
        super(gold, y, x, currentTile, indicator);
    }

    // Accessors
    // getAction() returns the next bot action
    // @return : bot's action to be carried out
    public String getAction() {
        return action;
    }

    // Mutators
    // setGold_needed(int) sets the gold needed for the bot to win
    public void setGold_needed(int n) {
        gold_needed = n;
    }

    // nextAction() decides what action is to be made by the bot this turn
    public void nextAction() {
        if (round > 2) {
            // if currently standing on exit tile and collected all gold, set action to 'exit'
            // priority is to exit the map if enough gold is collected
            if (getCurrentTile() == 'E' && getGold() == gold_needed) {
                action = "quit";
                return;
            }
            // moves list will be empty after all moves are executed, thus arriving at location of desired entity
            // check which type of entity the bot has moved to then set appropriate action
            // priority is to collect the gold if arriving at a gold tile then locate next tile to move to through look
            if (moves.isEmpty()) {
                // enter pickup if on gold tile
                if (getCurrentTile() == 'G') {
                    action = "pickup";
                    // enter look to locate next entity if on empty tile or exit tile but not yet collected enough gold
                } else if (getCurrentTile() == '.' || getCurrentTile() == 'E')  {
                    action = "look";
                }
                // if moves list is not empty, execute the next move then remove it from list
            } else {
                action = moves.get(0);
                moves.remove(0);
            }
            // first two rounds of game:
        } else {
            // action in the first round of the game would always be "hello" to check how much gold is needed to win
            if (round == 1) {
                action = "hello";
                // action in the second round of the game would always be "look" to view the surrounding
            } else {
                action = "look";
            }
        }
        // increment round number
        round += 1;
    }

    // setSurrounding(String) converts the string map from look into a 2d character array map and stores it
    public void setNearby(String miniMap) {
        // nearby: a temporary 5x5 character array
        nearby = new char[5][5];
        // n: an integer counter that is incremented after each successful execution of the following loops
        // to ensure the correct character is being read from the string map
        int n = 0;
        // outer loop to cycle through the 5 rows - i: y-coordinate
        for (int i = 0; i < 5; i++) {
            // inner loop to cycle through each of the 5 elements within each row - j: x-coordinate
            for (int j = 0; j < 5; j++) {
                // set current i,j (y,x) coordinate of nearby map to the character in equivalent coordinates on minimap
                // as long as current character is not newline
                if (miniMap.charAt(i) != '\n') {
                    nearby[i][j] = miniMap.charAt(n);
                    // increment counter n to move to next character in the string
                }
                n += 1;
            }
            n += 1;
        }
    }

    // findEntity(char) checks if a character exists nearby, if so, return coordinates of that character
    // @param : the character to be found
    // @return : the coordinates of the character if found, otherwise nothing
    private int[] findEntity(char entity) {
        // loops through all 5 rows
        for (int i = 0; i < 5; i++) {
            // loops through all 5 columns within each row
            for (int j = 0; j < 5; j++) {
                // if characters match return the current counts of loops - the coordinates of specified entity
                if (nearby[i][j] == entity) {
                    return new int[]{i, j};
                }
            }
        }
        return null;
    }

    // nextDestination() decides what the bot's next destination is by looking through where
    // all nearby entities within its minimap are: P - human player, G - gold, E - exit
    public void nextDestination() {
        // find coordinates of all 'useful' entities to move to
        int[] player_coord = findEntity('P');
        int[] gold_coord = findEntity('G');
        int[] exit_coord = findEntity('E');

        // priority to move to an exit to the map if all gold is collected
        if (getGold() == gold_needed) {
            if (exit_coord != null) {
                findPathTo(exit_coord[0], exit_coord[1]);
            } else {
                // move to a random location to try and find an exit
                randomDestination();
            }

            // next priority is to collect gold if not gold is collected
        } else if (gold_coord != null) {
            findPathTo(gold_coord[0], gold_coord[1]);

            // if not all gold is collected and no gold is within view, try to catch player
        } else if (player_coord != null) {
            findPathTo(player_coord[0], player_coord[1]);

            // if no entities within view, move to find entities
        } else {
            // move anywhere that isn't a wall to try and find gold/player/exit
            randomDestination();
        }
    }

    // canMove(Pos) returns if a bot can move to a specified tile
    // @param : position of tile
    // @return : boolean value of whether the bot can move to the specified tile
    private boolean canMove(Pos pos) {
        // retrieve y and x coordinates of the position by getCoord() method
        int ycoord = pos.getCoord()[0];
        int xcoord = pos.getCoord()[1];
        // check if either x or y coordinates are out of bounds (beyond 5x5 minimap)
        // if so return false
        if (ycoord < 0 || ycoord > 4) {
            return false;
        }
        if (xcoord < 0 || xcoord > 4) {
            return false;
        }
        // return boolean value of whether the specified tile is a wall '#'
        return nearby[ycoord][xcoord] != '#';
    }

    // posConnected(Pos) returns a list of connected positions on the minimap that the bot can move to
    // @return : list of positions on the minimap that can be moved to
    private List<Pos> posConnected(Pos pos) {
        List<Pos> connected = new ArrayList<>();
        // position to the north of current position
        Pos n = pos.offset(1, 0);
        // position to the east of current position
        Pos e = pos.offset(0, 1);
        // position to the south of current position
        Pos s = pos.offset(-1, 0);
        // position to the west of current position
        Pos w = pos.offset(0, -1);
        // check if each direction is movable from current position
        // if so, add to list of connected positions
        if (canMove(n)) {
            connected.add(n);
        }
        if (canMove(e)) {
            connected.add(e);
        }
        if (canMove(s)) {
            connected.add(s);
        }
        if (canMove(w)) {
            connected.add(w);
        }
        // return the list of connected positions
        return connected;
    }

    // pathFromTo(Pos, Pos) returns a possible path between two positions within the minimap
    // @param : the starting and ending positions
    // @return : a possible path of coordinates, or nothing if no path is found
    private List<Pos> pathFromTo(Pos start, Pos end) {
        // complete set to false at the start as no possible path has yet been found
        boolean complete = false;
        List<Pos> tried = new ArrayList<>();
        // add starting position to list of tried positions
        tried.add(start);
        // try to find path between start and end positions as long as one has not been found
        while (!complete) {
            List<Pos> newOpen = new ArrayList<>();
            // loop through positions in tried list and find its connected positions
            for (int i = 0; i < tried.size(); i++) {
                Pos pos = tried.get(i);
                for (Pos connected : posConnected(pos)) {
                    if (!tried.contains(connected) && !newOpen.contains(connected)) {
                        newOpen.add(connected);
                    }
                }
            }
            for (Pos pos : newOpen) {
                tried.add(pos);
                if (end.equals(pos)) {
                    complete = true;
                    break;
                }
            }

            if (!complete && newOpen.isEmpty()) {
                return null;
            }
        }
        List<Pos> path = new ArrayList<>();
        Pos pos = tried.get(tried.size() - 1);
        while (pos.getPrevious_pos() != null) {
            path.add(0, pos);
            pos = pos.getPrevious_pos();
        }

        return path;
    }

    // translatePath() takes a list of connected coordinates and translates them to a movement direction
    // n, e, s, w - then assign them to a private array moves to be later processed
    // for the bot to move through the path indicated
    // @param : the path to be taken by the bot from current position to destination
    private void translatePath(List<Pos> path) {
        // set current position to (2,2) - format (y,x) - as player at centre of 5x5 minimap
        int[] current = {2, 2};
        // for each position within path, compare with last coordinates
        for (Pos pos : path) {
            int[] next = pos.getCoord();
            // next y > current y, meaning it is down - s
            if (next[0] > current[0]) {
                moves.add("s");
                // next y < current y, meaning it is up - n
            } else if (next[0] < current[0]) {
                moves.add("n");
                // next x > current x, meaning it is right - e
            } else if (next[1] > current[1]) {
                moves.add("e");
                // next x < current x, meaning it is left - w
            } else if (next[1] < current[1]) {
                moves.add("w");
            }
            // set current position to next position, for comparing with the position after
            current = next;
        }
    }

    // findPathTo() finds a possible path from a specified y and x coordinate on the 5x5 minimap
    // passes starting values to pathFromTo(), retrieving a list of coordinates of the path to be taken
    // the list of path coordinates are passed to translatePath() for directions
    // @param : starting y coordinate & starting x coordinate
    private void findPathTo(int endy, int endx) {
        // starting position on 5x5 minimap (2,2) as player is at centre
        Pos start = new Pos(2, 2, null);
        Pos end = new Pos(endy, endx, null);
        List<Pos> path = pathFromTo(start, end);
        // translates path coordinates to directions if a possible path exists (if isn't empty)
        if (path != null) {
            translatePath(path);
        }
    }


    // randomDestination() generates a random path for the bot within the 5x5 minimap
    // used when no entity is left within the minimap after looking
    private void randomDestination() {
        Random rand = new Random();
        // loop until valid move(s) are generated
        while (moves.isEmpty()) {
            // generate integer y and x coordinates between 0-5
            int ry = rand.nextInt(0, 6);
            int rx = rand.nextInt(0, 6);
            // pass coordinates to findPath(int,int) method to generate path and moves
            findPathTo(ry, rx);
        }
    }

}
