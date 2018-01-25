/**
***********************************************************
* Name:  Janila Khan                                      *
* Project : Project 2 / Longana Game			          *
* Class : CMPS 366 Organization of Programming Languages  *
* Date : December 5, 2017                                 *
***********************************************************
*/

package com.longana.model;

import java.util.Vector;
import java.util.Collections;

/* ****************************************************************************
This class creates the stock and initializes the 28 tiles needed for the game
**************************************************************************** */

public class Stock {
    // Class Variable
    private Vector<Tile> stockSet;

    /**
     In the Constructor the 28 tiles are created and added to the stockSet
     */
    public Stock() {
        stockSet = new Vector<>(28);

        // Local Variables
        int x, y;
        Tile tile;

        // Add tiles to the stockSet
        for (x = 0; x <= 6; x++)
        {
            for (y = 0; y <= 6; y++)
            {
                // Before adding the tile, must check to see if the tile is already initialized
                if (!searchStock(x, y))
                {
                    tile  = new Tile(x,y);
                    stockSet.addElement(tile);
                }
            }
        }

        // Shuffle the stockSet
        Collections.shuffle(stockSet);
    }

    /**
     Get a pointer to the the stockset
     @return Returns a pointer to the stockset
     */
    public Vector<Tile> getStock() { return stockSet; }

    /**
     For a saved game this will set the stockset
     @param temp, a vector of tiles that holds the temporary values of the stock from a saved game file
     */
    public void setStock(Vector<Tile> temp){stockSet = temp;}

    /**
     Searches through the stockSet to see if the tile is already initialized
     @param m, an integer that holds the first or left side value of the tile
     @param n, an integer that holds the second or right side value of the tile
     @return true if the tile is found, false otherwise
     */
    private boolean searchStock(int m, int n) {
        // Initialize a temp tile with the values of m and n
        Tile tempTile =  new Tile(n,m);

        // Search through the vector for the tile
        for (int i = 0; i < stockSet.size(); i++)
        {
            // Return true if found
            if(stockSet.elementAt(i).equals(tempTile)){
                return true;
            }
        }

        // If not found, return false
        return false;
    }

    /**
     Draw Tiles from the stock to a players hand
     @param player, a vector of tiles that holds a player''s hand
     @param num, an integer that holds the number of tiles that will be draw from the stock
     */
    public void drawTiles(Vector<Tile> player, int num){
        // Copy a certain number elements to another vector
        for(int i = 0; i < num; i++)
        {
            player.addElement(stockSet.elementAt(i));
        }

        // Erase elements from the stockSet
        for(int i = 0; i < num; i++)
        {
            stockSet.remove(stockSet.firstElement());
        }
    }
}
