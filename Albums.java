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
        mainMenu();
    }
    
    /**
     * Shows main menu options
     * 
     */
    public void mainMenu() {
        UI.initialise();
        getNewAlbums();
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
        name = null;
        genre = null;
        artist = null;
        UI.addTextField("Album Name: ", this::addName);
        UI.addTextField("Album Genre: ", this::addGenre);
        UI.addTextField("Album Artist: ", this::addArtist);
        UI.addButton("Add Album", this::addAlbum);
        UI.addButton("Back", this::mainMenu);
    }
    
    /**
     * Shows buttons and text fields for rating an album
     */
    public void rateAlbumUI() {
        UI.initialise();
        UI.addTextField("Album Name: ", this::rateAlbum);
        UI.addButton("Change Rating", this::likeAlbum);
        UI.addButton("Back", this::mainMenu);
    }
    
    /**
     * Adds albums from text file to hashmap
     */
    
    public void getNewAlbums() {
        File newAlbumsRaw = new File("albums.txt");
        try {
            Scanner scan = new Scanner(newAlbumsRaw);
            while(scan.hasNextLine()) { // loops each line
                ArrayList<String> newAlbum = new ArrayList<String>();
                String scanLine = scan.nextLine();
                char[] albumLine = scanLine.toCharArray();
                String entryField = "";
                for(int i=0; i<albumLine.length;i++){ // loops each character
                    char c = albumLine[i];
                    if (c == 0x2c) {
                        newAlbum.add(entryField);
                        entryField = "";
                    } else {
                        entryField += c;
                    }
                }
                albumStore.put(newAlbum.get(0)+newAlbum.get(1)+newAlbum.get(2), new Album(newAlbum.get(0), newAlbum.get(1), newAlbum.get(2), false));
            }
        } catch (Exception e) {
            UI.println(e);
        }
    }
    /**
     * Processing and output for liking an album
     */
    public void likeAlbum(){    
        UI.clearGraphics();
        if (AlbumSearched == true) {
            for(String i : albumStore.keySet()) {
                if (albumStore.get(i).getName().equals(ratedAlbum)) {
                    String iGenre = albumStore.get(i).getGenre();
                    String iArtist = albumStore.get(i).getArtist();
                    albumStore.get(i).toLike();
                    if (albumStore.get(i).getRating() == true){
                        UI.println("Rating: Liked");
                        getRecommendation(iGenre);
                    } else {
                        UI.println("Rating: Disliked");
                    }
                }
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

    /**
     * Processing for adding an album to hashmap
     */
    public void addAlbum() {
        try {
            if (name.equals(null) || genre.equals(null) || artist.equals(null)) {
                throw new NullPointerException("Please make sure all fields are filled");
            }
            Album newAlbum = new Album(name, genre, artist, liked);
            albumStore.put(name+genre+artist, new Album(name, genre, artist, liked));
            mainMenu();
        } catch (Exception InvalidAddition) {
            UI.println("Something went wrong");
        }
    }

    /**
     * Processing and output for viewing all albums
     */
    public void viewAll() {
        UI.clearText();
        for (String i : albumStore.keySet()) {
            boolean rat = albumStore.get(i).getRating();
            UI.println("Name: " + albumStore.get(i).getName());
            UI.println("Genre: " + albumStore.get(i).getGenre());
            UI.println("Artist: " + albumStore.get(i).getArtist());
            if (rat == true){
                UI.println("Rating: Liked");
            } else {
                UI.println("Rating: Disliked");
            }
            UI.println(" ");
        }
    }
    
    /**
     * Processing and output for searching for a specific album
     */
    public void searchAlbumUI() {
        UI.clearText();
        for(String i : albumStore.keySet()) {
            if (albumStore.get(i).getName().equals(uas)) {
                boolean rat = albumStore.get(i).getRating();
                UI.println("Name: " + albumStore.get(i).getName());
                UI.println("Genre: " + albumStore.get(i).getGenre());
                UI.println("Artist: " + albumStore.get(i).getArtist());
                if (rat == true){
                    UI.println("Rating: Liked");
                } else {
                    UI.println("Rating: Disliked");
                }
            }
        }
    }
    
    /**
     * Output for getting a recommendation for other albums
     */
    public void getRecommendationUI(String item, int row) {
        UI.drawString(item, 100, row*15+100);
    }

    /**
     * Processing for getting a recommendation for other albums
     */
    public void getRecommendation(String likedGenre) {
        ArrayList<String> recommendedAlbums = new ArrayList<String>();
        for (String albumKey : albumStore.keySet()) {
            if (albumStore.get(albumKey).getGenre().equals(likedGenre) && !(albumStore.get(albumKey).getName().equals(ratedAlbum))) {
                recommendedAlbums.add(albumStore.get(albumKey).getName());
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

