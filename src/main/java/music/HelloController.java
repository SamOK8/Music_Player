package music;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.media.MediaPlayer;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;


public class HelloController implements Initializable {

    private MediaPlayer mediaPlayer;
    private Media media;

    @FXML
    private Label songLabel;
    @FXML
    private Button playPauseButton;
    @FXML
    private Label openButton;
    @FXML
    private Slider volumeSlider;
    @FXML
    private ProgressBar progressBar;
    @FXML
    private ScrollPane songs;
    @FXML
    private VBox vbox1;
    @FXML
    private Pane pane1;


    private File directory;
    private File[] files;
    private int currentlyPlaying = Integer.MIN_VALUE;
    private int setIndex;
    Boolean isPlaying = false;
    private double volume = 100;
    private Timer timer;
    private TimerTask task;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


    }
    public void stop(){
        if(mediaPlayer != null) {
            mediaPlayer.stop();
            isPlaying = false;
            playPauseButton.setText("▶");
        }
    }
    public void play(){
        if(mediaPlayer != null){
            startPlaying();
        }
    }

    public void playPause(){
        if(!isPlaying){
            play();
        }else if(isPlaying) {
            pause();
            playPauseButton.setText("▶");
        }
    }
    public void pause(){
        if (mediaPlayer != null) {
            volume = mediaPlayer.getVolume();
        }
        if (mediaPlayer != null) {
            mediaPlayer.pause();
            isPlaying = false;
            currentlyPlaying = Integer.MIN_VALUE;

        }
    }

    public void open(){
        if (mediaPlayer != null) {
            stop();
        }
        DirectoryChooser directoryChooser = new DirectoryChooser();
        Stage stage = (Stage) playPauseButton.getScene().getWindow();
        directory = directoryChooser.showDialog(stage);
        files = directory.listFiles();

        GridPane pane = new GridPane();
        for (int i = 0; i < files.length; i++) {
            System.out.println(files[i]);
            Button button;
            pane.add(button = new Button(files[i].getName()),1,i);
            int I = i;
            button.setOnAction(actionEvent -> {
                stop();
                volume = mediaPlayer.getVolume();
                changeSong(I);
                startPlaying();
            });
        }
        songs.setContent(pane);
        changeSong(0);
        volumeSlider.valueProperty().addListener((observable, oldValue, newValue) ->
                mediaPlayer.setVolume(newValue.doubleValue() / 100));
        volumeSlider.setValue(100);
    }

    public void previous(){
        if (mediaPlayer != null) {
            volume = mediaPlayer.getVolume();
        }
        stop();
        if (0 <= setIndex - 1 ) {
            changeSong(setIndex - 1);
        }

    }
    public void next(){
        if (mediaPlayer != null) {
            volume = mediaPlayer.getVolume();
        }
        stop();
        if (files.length > setIndex + 1 ) {
            changeSong(setIndex + 1);
        }


    }

    public void changeSong(int index){
        if (files != null) {
            media = new Media(files[index].toURI().toString());
            mediaPlayer = new MediaPlayer(media);
            setIndex = index;
            startPlaying();

        }
    }
    public void startPlaying() {
        mediaPlayer.play();
        currentlyPlaying = setIndex;
        mediaPlayer.setVolume(volume);
        songLabel.setText(files[currentlyPlaying].getName());
        isPlaying = true;
        playPauseButton.setText("⏸");


    }
    public void beginTimer() {
        timer = new Timer();

        task = new TimerTask() {
            public void run() {

            }
        };
    }

}


