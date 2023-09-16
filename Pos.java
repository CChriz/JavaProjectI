// position class, contains a position's y and x coordinates and its previous position
public class Pos {
    // integer y coordinate
    private int ycoord;
    // integer x coordinate
    private int xcoord;
    // position previous to the current position
    private Pos previous_pos;

    // Constructor - consisting of coordinates of current position and previous position
    public Pos(int ycoord, int xcoord, Pos previous_pos) {
        this.ycoord = ycoord;
        this.xcoord = xcoord;
        this.previous_pos = previous_pos;
    }

    // format display of coordinates in the form (y, x)
    @Override
    public String toString() {
        return String.format("(%d, %d)", ycoord, xcoord);
    }

    // check if both coordinates match
    @Override
    public boolean equals(Object o) {
        Pos pos = (Pos) o;
        return ycoord == pos.ycoord && xcoord == pos.xcoord;
    }

    // offset(int, int) returns the position after arithmetic addition with specified integer values
    // @param : the integer amount to add to each coordinate
    // @return : the new position after addition
    public Pos offset(int oy, int ox) {
        return new Pos(ycoord + oy, xcoord + ox, this);
    }

    // getCoord() returns the coordinates as an integer array in the format (y,x)
    // @return : y and x coordinates
    public int[] getCoord() {
        return new int[]{ycoord, xcoord};
    }

    // getPrevious_pos() returns the previous position of the current position
    // @return : previous position
    public Pos getPrevious_pos() {
        return previous_pos;
    }
}
// implementing the bot's search for possible paths:
// https://gamedev.stackexchange.com/questions/197165/java-simple-2d-grid-pathfinding
