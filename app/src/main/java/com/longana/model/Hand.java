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

/* ********************************************************************
This class implements some of the functions needed for a player's hand
******************************************************************** */

public class Hand {

    /**
     Find where the tile is in a player's hand
     @param player, a vector of tiles which represents a players hand
     @param temp, a tile object which is the tile the program is searching for
     @return the value of where the tile is in the player's hand
     */
    public int getIndex(Vector<Tile> player, Tile temp) {
        int index = -1;

        // Search through the vector for the tile
        for (int i = 0; i < player.size(); i++)
        {
            // Return true if found
            if(player.elementAt(i).equals(temp)){
                index = i;
            }
        }

        // Return the value of where the tile is in the player's hand
        return index;
    }

    /**
     Count the tiles in a player's hand
     @param player, a vector of tiles which represents a players hand
     @return sum of the tiles in a player's hand
     */
    public int countHand(Vector<Tile> player) {
        // Local Variables
        int count = 0;
        int sum = 0;

        // Iterate through the vector and add up all the tiles
        for (int i = 0; i < player.size(); i++)
        {
            count = player.elementAt(i).getFirst() + player.elementAt(i).getSecond();
            sum = sum + count;
        }

        return sum;
    }
}
