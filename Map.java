// map class, contains 2d char array for map representation, map name and gold required to win current map
import java.io.*;
import java.util.ArrayList;

/*
 * Reads and contains in memory the map of the game.
 */
public class Map {

    /* Representation of the map */
    private char[][] map;

    /* Map name */
    private String mapName;

    /* Gold required for the player to win */
    private int goldRequired;

    // Default constructor, creates the default map "Very small Labyrinth of doom".
    // used when a player chosen map cannot be found
    public Map() {
        mapName = "Very small Labyrinth of Doom";
        goldRequired = 2;
        map = new char[][]{
                {'#','#','#','#','#','#','#','#','#','#','#','#','#','#','#','#','#','#','#','#'},
                {'#','.','.','.','.','.','.','.','.','.','.','.','.','.','.','.','.','.','.','#'},
                {'#','.','.','.','.','.','.','G','.','.','.','.','.','.','.','.','.','E','.','#'},
                {'#','.','.','.','.','.','.','.','.','.','.','.','.','.','.','.','.','.','.','#'},
                {'#','.','.','E','.','.','.','.','.','.','.','.','.','.','.','.','.','.','.','#'},
                {'#','.','.','.','.','.','.','.','.','.','.','.','G','.','.','.','.','.','.','#'},
                {'#','.','.','.','.','.','.','.','.','.','.','.','.','.','.','.','.','.','.','#'},
                {'#','.','.','.','.','.','.','.','.','.','.','.','.','.','.','.','.','.','.','#'},
                {'#','#','#','#','#','#','#','#','#','#','#','#','#','#','#','#','#','#','#','#'}
        };
    }

    // Constructor that accepts a map to read in from.
    public Map(String fileLocation) {
        readMap(fileLocation);
    }

    // readMap(string) reads a map from specified string file location and sets up the map
    // @param : file location of map
    public void readMap(String fileLocation) {
        String line;
        ArrayList<String> tempMap = new ArrayList<>();
        // reads each line of the text file
        try (BufferedReader br = new BufferedReader(new FileReader(fileLocation))) {
            // loop as long as end of the file has not yet been reached
            while ((line = br.readLine()) != null) {
                // if first character of the line is a wall '#', add line to temporary string map - tempMap
                if (line.charAt(0) == '#') {
                    tempMap.add(line);
                    // if first character of the line is 'n' then trim the first 5 characters of the line
                    // to get rid of text "name ", then assign the rest (name of the map) to mapName
                } else if (line.charAt(0) == 'n') {
                    mapName = (line.substring(5)).trim();
                    System.out.println(mapName);
                    // if first character of the line is 'w' then trim the first 4 characters of the line
                    // to get rid of the text 'win ', then convert the rest (number of gold to win) to integer
                    // then assign to goldRequired
                } else if (line.charAt(0) == 'w') {
                    String gr = (line.substring(4)).trim();
                    goldRequired = Integer.parseInt(gr);
                }
            }
            // close BufferedReader
            br.close();

            // create a new map as a 2d character array with the size of the temporary string map read from text file
            int rows = tempMap.size();
            int cols = tempMap.get(0).length();
            map = new char[rows][cols];
            // loop through rows
            for (int i = 0; i < rows; i++) {
                // loop through columns of each row
                for (int j = 0; j < cols; j++) {
                    // each character from the temporary string map is read and added to the map
                    char tile = tempMap.get(i).charAt(j);
                    map[i][j] = tile;
                }
            }
            // in the case where a map has failed to load: either not found or other errors
            // the default map is loaded, message is displayed here
        } catch (IOException e) {
            System.out.println("Map failed to load, loading default map...");
        }

    }

    // ACCESSORS
    // getMapSize() returns the size of the map (Y x X) - border inclusive
    // @return : the integer size of the map
    public int[] getMapSize() {
        return new int[]{map.length, map[0].length};
    }

    // canSpawn(x,y) returns boolean value of if a player or bot can spawn at given coordinates at the start of the game
    // @param : integer coordinates of y and x
    // @return : boolean value of whether the player is allowed to at specified coordinates
    public Boolean canSpawn(int y, int x) {
        // return true if the tile to be spawned on is empty or an exit, otherwise false
        return (map[y][x] == '.') || (map[y][x] == 'E');
    }

    // getGoldRequired() returns the total gold required for winning the map
    // @return : gold required to win
    public int getGoldRequired() {
        return goldRequired;
    }

    // getMap() returns the current status of the entire map - NOT accessible by ANY players
    // @return : the entire map as a string
    // (for testing purposes ONLY)
    /*
    public String getMapAll() {
        String strMap = null;
        for (int i = 0; i < 1; i++) {
            strMap = Arrays.deepToString(map);
            strMap = strMap.replace("[","");
            strMap = strMap.replace("], ", "\n");
            strMap = strMap.replace("]]", "");
            strMap = strMap.replace(", ", "");
        }
        return strMap;
    }
    */

    // getTile(y, x) returns the tile of chosen coordinates on the map as a character
    // @param : integer values of y and x coordinates
    // @return : a character of the tile at specified coordinates
    public char getTile(int y, int x) {
        return map[y][x];
    }

    // getMapName() returns the name of the map as a string
    // @return : name of the map
    public String getMapName() {
        return mapName;
    }

    // MUTATORS
    // setMap() sets the tile of a chosen coordinate on the map to a specified character
    // (used when a player/bot is spawned or moved, or a gold coin is collected)
    // @param : integer coordinates y, x and a character indicating the entity
    public void setMap(int y, int x, char entity) {
        map[y][x] = entity;
        }

}
