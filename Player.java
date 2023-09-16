// class for players, contains gold owned, y and x coordinates and the tile stood on by the player
public class Player {
    // gold owned by player
    private int gold;
    // y coordinate of player
    private int y;
    // x coordinate of player
    private int x;
    // current tile stood on by the player
    private char currentTile;
    // player's indicator
    private char indicator;

    // constructor
    // @param : initial starting gold, starting x and y coordinates, and current tile it is on
    public Player(int gold, int y, int x, char currentTile, char indicator) {
        this.gold = gold;
        this.y = y;
        this.x = x;
        this.currentTile = currentTile;
        this.indicator = indicator;
    }

    // ACCESSORS
    // getGold() returns the number of gold currently owned by the player
    // @return : gold owned
    public int getGold() {
        return gold;
    }

    // getCoord() returns the coordinates of the player (y,x)
    // @return : player coordinates
    public int[] getCoord() {
        return new int[]{y,x};
    }

    // getCurrentTile() returns the current tile the player is standing on
    // @return : character of the tile stood on by the player
    public char getCurrentTile() {
        return currentTile;
    }

    // getIndicator() returns the indicator used by the player
    // @return : the player's indicator
    public char getIndicator() {
        return indicator;
    }

    // MUTATORS
    // setCoord(y,x) assign specified values to player's coordinates
    // @param : y and x coordinates
    public void setCoord(int ycoord, int xcoord) {
        y = ycoord;
        x = xcoord;
    }

    // setCurrentTile(char) assigns specified character to the player's current tile
    public void setCurrentTile(char tile) {
        currentTile = tile;
    }

    // collectedGold() increments the gold count of the player by one
    public void collectedGold() {
        gold += 1;
    }


}
