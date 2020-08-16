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
    private String liked;
    
    public Album(String userName, String userGenre, String userArtist, String userLikeAlbum) {
        this.name = userName;
        this.genre = userGenre;
        this.artist = userArtist;
        this.liked = userLikeAlbum;
    }
    
    /**
     * Method to return name of album
     */
    public String getName() {
        return this.name;
    }
    
    /**
     * Method to return genre of album
     */
    public String getGenre() {
        return this.genre;
    }
    
    /**
     * Method to return artist of album
     */
    public String getArtist() {
        return this.artist;
    }
    
    /**
     * Method to return rating of album
     */
    public String getRating() {
        if (this.liked.equals("0")) {
            return "Unrated";
        } else {
            return this.liked;
        }
    }
    
    /**
     * Method to change the rating of the album
     */
    public void toLike(String userRating) {
        this.liked = userRating;
    }
    
    public String returnAll() {
        return "Name: " + this.name + "\nGenre: " + this.genre + "\nArtist: " + this.artist + "\nRating: " + this.liked;
    }
}

