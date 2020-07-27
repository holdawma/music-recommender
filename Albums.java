/* Based on the ecs 100 template
 * Code for ??
 * Name:
 * Date:
 */

import ecs100.*;
import java.util.*;
import java.io.*;
import java.awt.Color;

/** The class for all albums
 * String name
 * String genre
 * String artist
 * boolean liked
 */
public class Albums {

    private String name;
    private String genre;
    private String artist;
    private boolean liked = false;
    private HashMap<String, Album> albumStore; 

    /**
     * Constructor for class Albums
     * Builds hashmap and calls main menu method
     */
    public Albums() {
        albumStore = new HashMap<String, Album>();
        mainMenu();
    }
    
    /**
     * Shows main menu options
     * 
     */
    public void mainMenu() {
        UI.initialise();
        UI.addButton("Add new Album", this::addAlbumUI);
        UI.addButton("View All", this::viewAll);
    }
    
    /**
     * Shows buttons and text fields for adding an album
     */
    public void addAlbumUI(){
        UI.initialise();
        UI.addTextField("Album Name: ", this::addName);
        UI.addTextField("Album Genre: ", this::addGenre);
        UI.addTextField("Album Artist: ", this::addArtist);
        UI.addButton("Like", this::likeAlbum);
        UI.addButton("Add Album", this::addAlbum);
    }

    private void addName(String userName){
        name = userName;
    }

    private void addGenre(String userGenre){
        genre = userGenre;
    }

    private void addArtist(String userArtist){
        artist = userArtist;
    }

    private void likeAlbum(){
        if (liked == true) {
            liked = false;
            UI.println("Disliked");
        } else if (liked == false) {
            liked = true;
            UI.println("Liked");
        }
    }

    public void addAlbum() {
        Album newAlbum = new Album(name, genre, artist, liked);
        albumStore.put(name, new Album(name, genre, artist, liked));
        mainMenu();
    }

    public void viewAll() {
        for (String i : albumStore.keySet()) {
            String gen = albumStore.get(i).getGenre();
            String art = albumStore.get(i).getArtist();
            boolean rat = albumStore.get(i).getRating();
            UI.println("Name: " + i);
            UI.println("Genre: " + gen);
            UI.println("Artist: " + art);
            UI.println("Rating: " + rat);
        }
    }

    public static void main(String[] args){
        Albums obj = new Albums();
    }

}

