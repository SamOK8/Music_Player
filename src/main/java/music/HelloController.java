package music;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.Background;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.paint.Color;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.media.MediaPlayer;

import java.io.File;
import java.net.URL;
import java.util.Random;
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
    private Slider progressSlider;
    @FXML
    private Slider volumeSlider;
    @FXML
    private ScrollPane songs;
    @FXML
    private VBox vbox1;
    @FXML
    private Button randomButton;


    private File directory;
    private File[] files;
    private int currentlyPlaying = Integer.MIN_VALUE;
    private int setIndex;
    Boolean isPlaying = false;
    private double volume = 100;
    private TimerTask task;
    private Timer timer;
    private boolean random = false;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void stop() {
        if (mediaPlayer != null) {
            endTimer();
            mediaPlayer.stop();
            isPlaying = false;
            playPauseButton.setText("▶");
        }
    }

    public void play() {
        if (mediaPlayer != null) {
            startPlaying();
        }
    }

    public void playPause() {
        if (!isPlaying) {
            play();
        } else if (isPlaying) {
            pause();
            playPauseButton.setText("▶");
        }
    }

    public void pause() {
        if (mediaPlayer != null) {
            volume = mediaPlayer.getVolume();
        }
        if (mediaPlayer != null) {
            mediaPlayer.pause();
            isPlaying = false;
            currentlyPlaying = Integer.MIN_VALUE;

        }
    }

    public void open() {
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
            pane.add(button = new Button(files[i].getName()), 1, i);
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

        progressSlider.valueProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (progressSlider.isPressed()) {
                        Duration total = media.getDuration();
                        System.out.println();
                        double value = newValue.doubleValue();
                        Duration position = total.multiply(value/100);
                        System.out.println(position);
                        mediaPlayer.stop();
                        mediaPlayer.setStartTime(position);
                        mediaPlayer.play();
                    }
                }
        );
    }

    public void previous() {
        if (mediaPlayer != null) {
            volume = mediaPlayer.getVolume();

            stop();
            if (0 <= setIndex - 1) {
                changeSong(setIndex - 1);
            }
        }

    }

    public void next() {
        if (mediaPlayer != null) {
            volume = mediaPlayer.getVolume();

            stop();
            if (files.length > setIndex + 1) {
                changeSong(setIndex + 1);
            }
        }
    }

    public void randomNext() {
        if (mediaPlayer != null) {
            volume = mediaPlayer.getVolume();
            Random rd = new Random();
            stop();
            int random = rd.nextInt(files.length);
            changeSong(random);

        }
    }
    //#5ec9ff
    public void randomButton() {
        if (random){
            random = false;
            randomButton.setStyle("-fx-background-color: black; -fx-text-fill: #919191;");

        }else {
            random = true;
            randomButton.setStyle("-fx-background-color: black; -fx-text-fill: #5ec9ff;");
        }

    }

    public void autoplay() {
        if (!random) {
            next();
        } else if (random) {
            randomNext();
        }


    }
    public void changeSong(int index) {
        if (files != null) {
            media = new Media(files[index].toURI().toString());
            mediaPlayer = new MediaPlayer(media);
            setIndex = index;
            startPlaying();

        }
    }

    public void startPlaying() {
        beginTimer();
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
            @Override
            public void run() {
                if (mediaPlayer!=null) {
                    double total = media.getDuration().toSeconds();
                    double current = mediaPlayer.getCurrentTime().toSeconds();
                    if (!progressSlider.isPressed()) {
                        progressSlider.setValue((current / total) * 100);
                    }
                    mediaPlayer.setOnEndOfMedia(() -> autoplay());
                }

            }

        };
        timer.scheduleAtFixedRate(task, 1000, 1000);

    }

    public void endTimer() {
        timer.cancel();
    }


}


