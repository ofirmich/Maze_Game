package View;
import algorithms.mazeGenerators.Position;
import javafx.beans.binding.Bindings;
import javafx.scene.image.Image;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import ViewModel.MyViewModel;
import algorithms.mazeGenerators.Maze;
import algorithms.search.Solution;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.image.*;
import javafx.stage.WindowEvent;

import java.awt.*;
import java.io.*;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.List;


public class MyViewController implements Initializable,Observer {
    //  private MyViewModel viewModel;
    protected MyViewModel viewModel = MyViewModel.getInstance();
    public static int isOpen;
    //Scene scene = Main.getInstance();
    //Stage stage = Main.getInstance2();
    @FXML
    public MenuBar menuBar;
    @FXML
    public TextField textField_mazeRows;
    @FXML
    public TextField textField_mazeColumns;
    @FXML
    public MazeDisplayer mazeDisplayer;
    @FXML
    public Label lbl_player_row;
    @FXML
    public Label lbl_player_column;
    StringProperty update_player_position_row = new SimpleStringProperty();
    StringProperty update_player_position_col = new SimpleStringProperty();
    private Maze maze;
    private Solution sol;
    private Position start;
    private Position goal;
    private int counter;
    //Instantiating Media class
    Media media = new Media(new File("./resources/ThemeSong.mp3").toURI().toString());
    Media media2 = new Media(new File("./resources/win.mp3").toURI().toString());
    //Instantiating MediaPlayer class
    public static MediaPlayer mediaPlayer;// = new MediaPlayer(media);
    MediaPlayer m = new MediaPlayer(media2);
    //protected static boolean winOpen=false;
    protected static boolean propHappend=false;


    @FXML
    private Button generateButton;
    @FXML
    private Button solveButton;

    @FXML
    private Pane pane;
    @FXML
    private BorderPane borderPane;
    protected static boolean themeMusicOn;


    public MyViewModel getView() {
        return this.viewModel;
    }
    protected static String jonOrDean;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        lbl_player_row.textProperty().bind(update_player_position_row);
        lbl_player_column.textProperty().bind(update_player_position_col);
        this.scroll(borderPane);
        generateButton.setDisable(false);
        solveButton.setDisable(true);
    //    isStopped = true;
        counter = 1;
        //mazeDisplayer.heightProperty().bind(pane.heightProperty());
        //mazeDisplayer.widthProperty().bind(pane.widthProperty());

//        if (isStopped == true) {
//            mediaPlayer.setCycleCount(1000);
//            mediaPlayer.setAutoPlay(true);
//            mediaPlayer.play();
//            isStopped = false;
//        }

        adjustDisplaySize();


        //by setting this property to true, the audio will be played


    }
/*    public void setViewModel(MyViewModel viewModel) {
        this.viewModel = viewModel;
    }*/

    public String get_update_player_position_row() {
        return update_player_position_row.get();
    }

    public void set_update_player_position_row(String update_player_position_row) {
        this.update_player_position_row.set(update_player_position_row);
    }

    public String get_update_player_position_col() {
        return update_player_position_col.get();
    }

    public void set_update_player_position_col(String update_player_position_col) {
        this.update_player_position_col.set(update_player_position_col);
    }


    public void generateMaze() {
        if(themeMusicOn == false)
        {
            mediaPlayer = new MediaPlayer(media);
            mediaPlayer.setVolume(0.3);
            mediaPlayer.setCycleCount(1000);
            mediaPlayer.setAutoPlay(true);
            mediaPlayer.play();
            themeMusicOn = true;
        }
        if(mediaPlayer!=null && mediaPlayer.isMute()){
            mediaPlayer.setMute(false);
            mediaPlayer.play();
            themeMusicOn = true;
        }



            // if (isStopped == true) {


        viewModel.addObserver(this);
        int rows = Integer.valueOf(textField_mazeRows.getText());
        int cols = Integer.valueOf(textField_mazeColumns.getText());
        viewModel.generateMaze(rows, cols);
        if (solveButton.isDisabled()) {
            solveButton.setDisable(false);
        }

    }
    public void solveMaze() {
        viewModel.solveMaze(this.maze);
    }


    public void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(message);
        alert.show();
    }


    public void keyPressed(KeyEvent keyEvent) {
        viewModel.moveCharacter(keyEvent);
        keyEvent.consume();
    }

    public void mouseClicked(MouseEvent mouseEvent) {
        mazeDisplayer.requestFocus();
    }


    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof MyViewModel) {
            if (maze == null)//generateMaze
            {
                this.maze = viewModel.getMaze();
                this.start= viewModel.getStart();
                this.goal= viewModel.getGoal();
               // this.mazeDisplayer.set_player_position(start.getRowIndex(), start.getColumnIndex());

                drawMaze();
            } else {
                Maze maze = viewModel.getMaze();

                if (maze == this.maze)//Not generateMaze
                {

                    int rowChar = mazeDisplayer.getRow_player();
                    int colChar = mazeDisplayer.getCol_player();
                    int rowFromViewModel = viewModel.getRowChar();
                    int colFromViewModel = viewModel.getColChar();

                    // else//Update location
                    set_update_player_position_row(rowFromViewModel + "");
                    set_update_player_position_col(colFromViewModel + "");
                    this.mazeDisplayer.set_player_position(rowFromViewModel, colFromViewModel);
                    if (rowFromViewModel == maze.getGoalPosition().getRowIndex() && colFromViewModel == maze.getGoalPosition().getColumnIndex() &&isOpen==0 /*&& winOpen ==false*/)//Solve Maze
                    {
                        if(mediaPlayer != null) {
                            mediaPlayer.setMute(true);
                        }
                        //  isStopped = true;
                        m.setAutoPlay(true);
                        m.setVolume(0.4);
                        m.play();
                        Stage stage = new Stage();
                        stage.setTitle("winner");
                        Pane board = new Pane();
                        if(jonOrDean == "D" && isOpen==0) {
                            Image img = new Image("/Images/dragonWin.gif");
                            ImageView image = new ImageView(img);
                            board.getChildren().add(image);
                            isOpen=1;
                            Scene scene = new Scene(board, 220, 123);
                            stage.setScene(scene);
                            stage.show();
                        }
                        else if(isOpen==0){
                            Image img = new Image("/Images/wolfWin.gif");
                            ImageView image = new ImageView(img);
                            board.getChildren().add(image);
                            isOpen=1;
                            Scene scene = new Scene(board, 220, 123);
                            stage.setScene(scene);
                            stage.show();

                        }


                        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                            public void handle(WindowEvent we) {

                                themeMusicOn = false;
                                m.pause();
                                mediaPlayer.setMute(true);
                                mediaPlayer.pause();
                                isOpen=0;
                                stage.close();
                            }
                        });
                    }

                    if (viewModel.getSol() != null) {
                        this.sol = viewModel.getSol();
                        drawSol();
                    }

                } else//GenerateMaze
                {
                    this.maze = maze;
                    drawMaze();
                }
            }
        }
    }


    private void scroll(Pane pane) {
        //handles mouse scrolling
        pane.setOnScroll(new EventHandler<ScrollEvent>() {
            @Override
            public void handle(ScrollEvent event) {
                double zoomFactor = 1.05;
                double deltaY = event.getDeltaY();
                if (deltaY < 0) {
                    zoomFactor = 2.0 - zoomFactor;
                }
                System.out.println(zoomFactor);
                pane.setScaleX(pane.getScaleX() * zoomFactor);
                pane.setScaleY(pane.getScaleY() * zoomFactor);
                event.consume();
            }
        });

    }

    private void adjustDisplaySize() {
        borderPane.widthProperty().addListener((obs, oldVal, newVal) -> {
            pane.setMinHeight(borderPane.getWidth());
            //mazeDisplayer.setWidth(newVal.doubleValue()-185);
            if (viewModel.getMaze() != null)
                mazeDisplayer.draw();
        });
        borderPane.heightProperty().addListener((obs, oldVal, newVal) -> {
            pane.setMinHeight(borderPane.getHeight());
            //mazeDisplayer.setHeight(newVal.doubleValue()-35);
            if (viewModel.getMaze() != null)
                mazeDisplayer.draw();
        });
        pane.widthProperty().addListener((obs, oldVal, newVal) -> {
            mazeDisplayer.setWidth(pane.getWidth() - 10);
            if (viewModel.getMaze() != null)
                mazeDisplayer.draw();
        });
        pane.heightProperty().addListener((obs, oldVal, newVal) -> {
            mazeDisplayer.setHeight(pane.getHeight() - 35);
            if (viewModel.getMaze() != null)
                mazeDisplayer.draw();
        });

    }

    public void drawMaze() {
        mazeDisplayer.drawMaze(maze , start , goal);
    }

    public void drawSol() {
        mazeDisplayer.drawSol(sol);
    }

    public void saveMaze(ActionEvent actionEvent) {
        FileChooser projectDirectory = new FileChooser();
        projectDirectory.setTitle("MazeHistory");
        File file = new File("./MazeHistory");
        if (file.exists() == false) {
            file.mkdir();
        }
        projectDirectory.setInitialDirectory(file);
        projectDirectory.setInitialFileName("maze" + counter);//default name
        counter++;
        File file2 = projectDirectory.showSaveDialog(textField_mazeRows.getScene().getWindow());
        viewModel.saveMaze(file2);
    }


    public void loadMaze(ActionEvent actionEvent) {
        FileChooser projectDirectory = new FileChooser();
        projectDirectory.setTitle("MazeHistory");
        File file = new File("./MazeHistory");
        if (file.exists() == false) {
            file.mkdir();
        }
        projectDirectory.setInitialDirectory(file);
        File file2 = projectDirectory.showOpenDialog(textField_mazeRows.getScene().getWindow());
        viewModel.loadMaze(file2);
    }

    public void exitProg(ActionEvent actionEvent) {

        System.exit(0);
    }

    public void help() throws IOException {
        Stage stage = new Stage();
        stage.setTitle("Help");
        Pane board = new Pane();
        Button close = new Button();
        board.getChildren().addAll(close);
        close.setLayoutX(20);
        close.setLayoutY(20);
        close.setText("Go Back");

        close.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                stage.close();
            }
        });
        Scene scene = new Scene(board, 900, 614);
        scene.getStylesheets().addAll(this.getClass().getResource("help.css").toExternalForm());

        stage.setScene(scene);
        stage.show();

    }


    public void about() throws IOException {
        Stage stage = new Stage();
        stage.setTitle("About");
        Pane board = new Pane();
        Button close = new Button();
        board.getChildren().addAll(close);
        close.setLayoutX(20);
        close.setLayoutY(20);
        close.setText("Go Back");

        close.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                stage.close();
            }
        });
        Scene scene = new Scene(board, 900, 614);
        scene.getStylesheets().addAll(this.getClass().getResource("windows.css").toExternalForm());

        stage.setScene(scene);
        stage.show();

    }

    public void showProp(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("MyViewProp.fxml"));
        Parent propWindowFXML = loader.load();
        Stage stage = (Stage) textField_mazeRows.getScene().getWindow();//or use any other component in your controller
        Scene propWindow = new Scene(propWindowFXML, 800, 600);
        stage.setScene(propWindow);
        stage.show(); //this line may be unnecessary since you are using the same stage.

    }

    public List<Button> createCharStage(Stage stage){
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        int width = gd.getDisplayMode().getWidth();
        int height = gd.getDisplayMode().getHeight();
        GridPane gridPane = new GridPane();

//        gridPane.setGridLinesVisible(true);
        final int numCols = 50 ;
        final int numRows = 50 ;
        for (int i = 0; i < numCols; i++) {
            ColumnConstraints colConst = new ColumnConstraints();
            colConst.setPercentWidth(100.0 / numCols);
            gridPane.getColumnConstraints().add(colConst);
        }
        for (int i = 0; i < numRows; i++) {
            RowConstraints rowConst = new RowConstraints();
            rowConst.setPercentHeight(100.0 / numRows);
            gridPane.getRowConstraints().add(rowConst);
        }


        List<Button> bList= new ArrayList<Button>();
        stage.setTitle("Welcome");
//        Pane board = new Pane();
        Button jon = new Button();
        Button deanr = new Button();
        gridPane.add(jon, 13, 37, 10, 10);
        gridPane.add(deanr, 32, 37, 10, 10);

        jon.setText("Jon Snow");
        deanr.setText("Daenerys Targaryen");
        Scene scene = new Scene(gridPane, width/2, height/2);
        scene.getStylesheets().addAll(this.getClass().getResource("opening.css").toExternalForm());

        stage.setScene(scene);
        bList.add(jon);
        bList.add(deanr);
        stage.show();
        return bList;
    }


    public void changeChar(ActionEvent event) throws IOException {
        Stage stage = new Stage();
        List<Button> bList = createCharStage(stage);
        Button jon = bList.get(0);
        Button deanr = bList.get(1);
        jon.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                mazeDisplayer.setAllImg("J","./resources/Images/ice.png" , "./resources/Images/jon.png" , "./resources/Images/wall.jpg" , "./resources/Images/wolf.png");
                jonOrDean = "J";
                stage.close();
            }
        });

        deanr.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                mazeDisplayer.setAllImg("D" , "./resources/Images/flame22.png", "./resources/Images/khalisssi2.png" , "./resources/Images/cloudd.png" ,"./resources/Images/dragonDOWN.png");
                jonOrDean = "D";
                stage.close();
            }
        });

    }
}



