/* Based on the ecs 100 template
 * Code for all albums in in the program
 * Name:
 * Date:
 */


import ecs100.*;
import java.util.*;
import java.io.*;
import java.awt.Color;


/** 
 * Class for each album, puts all values into variables, can return each value
 * String name
 * String genre
 * String artist
 * Boolean liked
 */
public class Album {
    // Set variables
    private String name;
    private String genre;
    private String artist;
    private boolean liked;
    
    public Album(String userName, String userGenre, String userArtist, boolean userLikeAlbum) {
        this.name = userName;
        this.genre = userGenre;
        this.artist = userArtist;
        this.liked = userLikeAlbum;
    }
    
    public String getName() {
        return this.name;
    }
    
    public String getGenre() {
        return this.genre;
    }
    
    public String getArtist() {
        return this.artist;
    }
    
    public boolean getRating() {
        return this.liked;
    }
}

