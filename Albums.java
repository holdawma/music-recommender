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
    private String uas;
    private String ratedAlbum;
    private boolean AlbumSearched;

    /**
     * Constructor for class Albums
     * Builds hashmap and calls main menu method
     */
    public Albums() {
        albumStore = new HashMap<String, Album>();
        albumStore.put("AM", new Album("AM", "Rock", "Arctic Monkeys", true));
        albumStore.put("Fine Line", new Album("Fine Line", "Pop", "Harry Styles", true));
        mainMenu();
    }
    
    /**
     * Shows main menu options
     * 
     */
    public void mainMenu() {
        UI.initialise();
        UI.addButton("Add new Album", this::addAlbumUI);
        UI.addTextField("Search: ", this::searchAlbumProcess);
        UI.addButton("Search", this::searchAlbumUI);
        UI.addButton("Rate Album", this::rateAlbumUI);
        UI.addButton("View All", this::viewAll);
        UI.addButton("Quit", UI::quit);
    }
    
    /**
     * Shows buttons and text fields for adding an album
     */
    public void addAlbumUI(){
        UI.initialise();
        UI.addTextField("Album Name: ", this::addName);
        UI.addTextField("Album Genre: ", this::addGenre);
        UI.addTextField("Album Artist: ", this::addArtist);
        UI.addButton("Add Album", this::addAlbum);
    }
        
    public void rateAlbumUI() {
        UI.initialise();
        UI.addTextField("Album Name: ", this::rateAlbum);
        UI.addButton("Change Rating", this::likeAlbumUI);
        UI.addButton("Back", this::mainMenu);
    }
    
    public void likeAlbumUI(){
        UI.clearGraphics();
        if (AlbumSearched == true) {
            String gen = albumStore.get(ratedAlbum).getGenre();
            String art = albumStore.get(ratedAlbum).getArtist();
            boolean rat = albumStore.get(ratedAlbum).toLike();
            if (albumStore.get(ratedAlbum).getRating() == true){
                UI.println("Rating: Liked");
                getRecommendation(gen);
            } else {
                UI.println("Rating: Disliked");
            }
            
        }
    }
    
    private void searchAlbumProcess(String userAlbumSearch){
        uas = userAlbumSearch;
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
    
    private void rateAlbum(String toRate){
        ratedAlbum = toRate;
        AlbumSearched = true;
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
            if (rat == true){
                UI.println("Rating: Liked");
            } else {
                UI.println("Rating: Disliked");
            }
            UI.println(" ");
        }
    }
    
    public void searchAlbumUI() {
        String gen = albumStore.get(uas).getGenre();
        String art = albumStore.get(uas).getArtist();
        boolean rat = albumStore.get(uas).getRating();
        UI.println("Name: " + uas);
        UI.println("Genre: " + gen);
        UI.println("Artist: " + art);
        if (rat == true){
            UI.println("Rating: Liked");
        } else {
            UI.println("Rating: Disliked");
        }
    }
    
    public void getRecommendationUI(String item, int row) {
        UI.drawString(item, 100, row*15+100);
    }

    public void getRecommendation(String likedGenre) {
        ArrayList<String> recommendedAlbums = new ArrayList<String>();
        for (String i : albumStore.keySet()) {
            if (albumStore.get(i).getGenre().equals(likedGenre) && !(i.equals(ratedAlbum))) {
                recommendedAlbums.add(i);
            }
        }
        if (recommendedAlbums.size() > 0) {
            UI.drawString("You might also like: ", 100, 100);
            for (int i = 0; i < recommendedAlbums.size(); i++) {
                getRecommendationUI(recommendedAlbums.get(i), i+1);
            }
        }
    }
    
    public static void main(String[] args){
        Albums obj = new Albums();
    }

}

