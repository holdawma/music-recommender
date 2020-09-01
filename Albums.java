import ecs100.*;
import java.util.*;
import java.io.*;
import java.awt.Color;
import java.lang.Math;

/** 
 * The class for all albums
 */
public class Albums {
    private String name;
    private String genre;
    private String artist;
    private String liked = "0";
    private HashMap<String, Album> albumStore;
    ArrayList<String> searchAlbumResults = new ArrayList<String>();
    private String uas;
    private String ratedAlbum;
    private boolean AlbumSearched;
    private String newRating;
    private double userX;
    private double userY;
    private String selected;
    private boolean choosing = false;
    private boolean isRating = false;
    
    /**
     * Constructor for class Albums
     * Builds hashmap and calls main menu method
     */
    public Albums() {
        albumStore = new HashMap<String, Album>();
        getNewAlbums();
        UI.initialise();
        UI.setWindowSize(1600, 900);
        mainMenu();
    }
    
    /**
     * Shows main menu options
     */
    public void mainMenu() {
        UI.initialise();
        isRating = false;
        choosing = false;
        UI.setDivider(0.0);
        UI.addButton("Add new Album", this::addAlbumUI);
        UI.addButton("Search", this::searchForUI);
        UI.addButton("View All", this::viewAll);
        UI.addButton("Quit", UI::quit);
    }
    
    /**
     * UI for searching for an album
     */
    
    public void searchForUI() {
        UI.initialise();
        UI.setDivider(0.0);
        UI.setMouseListener(this::doMouse); // Idk why I have to have this here but I do because otherwise the function breaks ¯\_(ツ)_/¯
        uas = "";
        UI.addTextField("Search (Album name, genre, or artist): ", this::searchAlbumProcess);
        UI.addButton("Search", this::searchAlbum);
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
     * Processing and output for searching for albums by all fields
     */
    public void searchAlbum() {
        searchAlbumResults.clear();
        userX = 0.0;
        userY = 0.0;
        //UI.clearText();
        UI.clearGraphics();
        for(String i : albumStore.keySet()) {
            if (albumStore.get(i).getName().equals(uas)
            || albumStore.get(i).getArtist().equals(uas)
            || albumStore.get(i).getGenre().equals(uas)) {
                searchAlbumResults.add(i);
            }
        }
        if (searchAlbumResults.size() > 0) {
            showSearchAlbumResults(searchAlbumResults);
        } else if (uas.equals("")) {
            UI.drawString("Please enter a value", 10, 20);
        } else {
            UI.drawString("No results were found for \"" + uas + "\"", 10, 20);
        }
    }
    
    /**
     * Shows the search results for the user, and lets them choose an album to display full results for
     */
    
    public void showSearchAlbumResults(ArrayList<String> results){
        int counter = 0;
        isRating = false;
        UI.setFontSize(18);
        UI.setLineWidth(2.0);
        UI.drawString("Please click one of the following:", 10, 20);
        UI.drawLine(5, 25+counter, 450, 25+counter);
        UI.setFontSize(13);
        UI.setLineWidth(1.0);
        UI.setMouseListener(this::doMouse);
        
        for(int i = 0 ; i < results.size() ; i++) {
            UI.drawString(albumStore.get(results.get(i)).getName(), 10, 45+counter);
            UI.drawString(albumStore.get(results.get(i)).getArtist(), 300, 45+counter);
            UI.drawLine(5, 50+counter, 450, 50+counter);
            counter += 25;
        }
        choosing = true;
        if (userX >= 10 && userX <= 400 && userY >= 30 && (userY-25)/25  <= counter/25) {
            selected = results.get((int) Math.round(Math.floor((userY-25)/25))); // Converts userY to an int
            userX = 0.0;
            userY = 0.0;
            showAlbumInfo(selected);
        }
    }
    
    /**
     * Sets mouse x and y when clicked
     */
    public void doMouse(String action, double x, double y) {
        if (action.equals("pressed")) {
            userX = x;
            userY = y;
            if (choosing) {
                showSearchAlbumResults(searchAlbumResults);
            } else if (isRating) {
                showAlbumInfo(selected);
            }
        }
    }
    
    /**
     * Processing and output for displaying albums on the graphics pane
     */
    private void showAlbumInfo(String album) {
        choosing = false;
        UI.clearGraphics();
        UI.drawString("Name: " + albumStore.get(album).getName(), 50, 20);
        UI.drawString("Genre: " + albumStore.get(album).getGenre(), 50, 35);
        UI.drawString("Artist: " + albumStore.get(album).getArtist(), 50, 50);
        UI.drawString("Rating: " + albumStore.get(album).getRating(), 50, 65);
        int iRating; // Learned from stackoverflow.com/questions/5585779/
        try {
           iRating = Integer.parseInt(albumStore.get(album).getRating());
        }
        catch (NumberFormatException e) {
            iRating = 0;
        }
        //
        for(int j=0; j < 5; j++) {
            if (j < iRating) {
                UI.fillOval(j*25+50, 75, 20, 20);
            } else {
                UI.drawOval(j*25+50, 75, 20, 20);
            }
        }
        isRating = true;
        if (userY >= 75 && userY <= 95 && (userX - 50)/25 >= 0 && (userX - 45)/25 <= 5) {
            changeAlbumRating((int) Math.round(Math.ceil((userX - 50)/25)), album);
            userY = 0.0;
            userX = 0.0;
            showAlbumInfo(album);
            if (albumStore.get(album).getRating().equals("4")
                || albumStore.get(album).getRating().equals("5")) {
                ratedAlbum = albumStore.get(album).getName();
                getRecommendation(albumStore.get(album).getGenre());
            }
        }
    }
    
    /**
     * Changes the rating of an album
     */
    public void changeAlbumRating(int newRating, String album) {
        String realNewRating = String.valueOf(newRating);
        albumStore.get(album).toLike(realNewRating);
    }
    
    /**
     * Output for getting a recommendation for other albums
     */
    public void getRecommendationUI(String item, int row) {
        UI.drawString(item, 350, row*15+25);
    }

    /**
     * Processing for getting a recommendation for other albums
     */
    public void getRecommendation(String likedGenre) {
        ArrayList<String> recommendedAlbums = new ArrayList<String>();
        
        for (String albumKey : albumStore.keySet()) {
            if (albumStore.get(albumKey).getGenre().equals(likedGenre)
                && !(albumStore.get(albumKey).getName().equals(ratedAlbum))
                && (albumStore.get(albumKey).getRating().equals("4")
                || albumStore.get(albumKey).getRating().equals("5"))) {
                recommendedAlbums.add(albumStore.get(albumKey).getName());
            }
        }
        if (recommendedAlbums.size() > 0) {
            UI.drawString("You might also like: ", 350, 25);
            for (int i = 0; i < recommendedAlbums.size(); i++) {
                getRecommendationUI(recommendedAlbums.get(i), i+1);
            }
        } else {
            for (String albumKey : albumStore.keySet()) {
                if (albumStore.get(albumKey).getGenre().equals(likedGenre)
                    && !(albumStore.get(albumKey).getName().equals(ratedAlbum))) {
                    recommendedAlbums.add(albumStore.get(albumKey).getName());
                }
            }
            if (recommendedAlbums.size() > 0) {
                UI.drawString("You might also like: ", 350, 25);
                for (int i = 0; i < recommendedAlbums.size(); i++) {
                    getRecommendationUI(recommendedAlbums.get(i), i+1);
                }
            }
        }
    }
    
    public static void main(String[] args){
        Albums obj = new Albums();
    }

}

