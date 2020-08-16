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
    private String liked = "0";
    private HashMap<String, Album> albumStore;
    private String uas;
    private String ratedAlbum;
    private boolean AlbumSearched;
    private String newRating;
    
    /**
     * Constructor for class Albums
     * Builds hashmap and calls main menu method
     */
    public Albums() {
        albumStore = new HashMap<String, Album>();
        getNewAlbums();
        mainMenu();
    }
    
    /**
     * Shows main menu options
     * 
     */
    public void mainMenu() {
        UI.initialise();
        UI.addButton("Add new Album", this::addAlbumUI);
        UI.addButton("Search", this::searchForUI);
        UI.addButton("Rate Album", this::rateAlbumUI);
        UI.addButton("View All", this::viewAll);
        UI.addButton("Quit", UI::quit);
    }
    
    public void searchForUI() {
        UI.initialise();
        UI.addTextField("Search: ", this::searchAlbumProcess);
        UI.addButton("Search Name", this::searchAlbumName);
        UI.addButton("Search Genre", this::searchAlbumGenre);
        UI.addButton("Search Artist", this::searchAlbumArtist);
        UI.addButton("Back", this::mainMenu);
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
        newRating = "-1";
        UI.addTextField("Album Name: ", this::rateAlbum);
        UI.addTextField("New rating: (1-5)", this::newAlbumRating);
        UI.addButton("Set", this::likeAlbum);
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
                char[] albumLine = scanLine.toCharArray(); // Learned from javatpoint.com/java-string-to-char
                String entryField = "";
                for(int i=0; i<albumLine.length;i++){ // loops each character
                    char c = albumLine[i];
                    if (c == 0x2c) {
                        newAlbum.add(entryField.strip());
                        entryField = "";
                    } else {
                        entryField += c;
                    }
                }
                albumStore.put(newAlbum.get(0)+newAlbum.get(1)+newAlbum.get(2), new Album(newAlbum.get(0), newAlbum.get(1), newAlbum.get(2), newAlbum.get(3)));
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
            try {
                if (!(newRating.equals("1") || newRating.equals("2") || newRating.equals("3") || newRating.equals("4") || newRating.equals("5"))) {
                    throw new ArithmeticException("Please enter a rating between 1 and 5");
                }
                for(String i : albumStore.keySet()) {
                    if (albumStore.get(i).getName().equals(ratedAlbum)) {
                        String iGenre = albumStore.get(i).getGenre();
                        String iArtist = albumStore.get(i).getArtist();
                        albumStore.get(i).toLike(newRating);
                        UI.println("Rating: " + albumStore.get(i).getRating());
                        if (newRating.equals("4") || newRating.equals("5")){
                            getRecommendation(iGenre);
                        }
                    }
                }
            } catch (Exception e) {
                UI.println("Something went wrong");
            }
        }
    }
    
    private void searchAlbumProcess(String userAlbumSearch){
        uas = userAlbumSearch.strip();
    }
    
    private void addName(String userName){
        name = userName.strip();
    }

    private void addGenre(String userGenre){
        genre = userGenre.strip();
    }

    private void addArtist(String userArtist){
        artist = userArtist.strip();
    }
    
    private void rateAlbum(String toRate){
        ratedAlbum = toRate.strip();
        AlbumSearched = true;
    }
    
    public void newAlbumRating(String userNewRating) {
        newRating = userNewRating.strip();
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
            albumStore.put(name+genre+artist, new Album(name, genre, artist, "0"));
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
            UI.println(albumStore.get(i).returnAll());
            UI.println(" ");
        }
    }
    
    /**
     * Processing and output for searching for a specific album by name
     */
    public void searchAlbumName() {
        UI.clearText();
        UI.clearGraphics();
        int counter = 0;
        for(String i : albumStore.keySet()) {
            if (albumStore.get(i).getName().equals(uas)) {
                showAlbumInfo(i, counter);
                counter+=115;
            }
        }
    }
    
    /**
     * Processing and output for searching for a specific album by genre
     */
    public void searchAlbumGenre() {
        UI.clearText();
        UI.clearGraphics();
        int counter = 0;
        for(String i : albumStore.keySet()) {
            if (albumStore.get(i).getGenre().equals(uas)) {
                showAlbumInfo(i, counter);
                counter+=115;
            }
        }
    }
    
    /**
     * Processing and output for searching for a specific album by Artist
     */
    public void searchAlbumArtist() {
        UI.clearText();
        UI.clearGraphics();
        int counter = 0;
        for(String i : albumStore.keySet()) {
            if (albumStore.get(i).getArtist().equals(uas)) {
                showAlbumInfo(i, counter);
                counter+=115;
            }
        }
    }
    
    /**
     * Processing and output for displaying albums on the graphics pane
     */
    private void showAlbumInfo(String album, int counter) {
        UI.drawString("Name: " + albumStore.get(album).getName(), 50, 20+counter);
        UI.drawString("Genre: " + albumStore.get(album).getGenre(), 50, 35+counter);
        UI.drawString("Artist: " + albumStore.get(album).getArtist(), 50, 50+counter);
        UI.drawString("Rating: " + albumStore.get(album).getRating(), 50, 65+counter);
        int iRating; // Learned from stackoverflow.com/questions/5585779/
        try {
           iRating = Integer.parseInt(albumStore.get(album).getRating());
        }
        catch (NumberFormatException e)
        {
           iRating = 0;
        }
        //
        for(int j=0; j < 5; j++) {
            if (j < iRating) {
                UI.fillOval(j*25+50, 75+counter, 20, 20);
            } else {
                UI.drawOval(j*25+50, 75+counter, 20, 20);
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
            if (albumStore.get(albumKey).getGenre().equals(likedGenre) && !(albumStore.get(albumKey).getName().equals(ratedAlbum)) && (albumStore.get(albumKey).getRating().equals("4") || albumStore.get(albumKey).getRating().equals("5"))) {
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

