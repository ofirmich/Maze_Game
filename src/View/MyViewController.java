package View;
import Model.MyModel;
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
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
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
    private int counter;

    //Instantiating Media class
    Media media = new Media(new File("./resources/ThemeSong.mp3").toURI().toString());
    Media media2 = new Media(new File("./resources/win.mp3").toURI().toString());


    //Instantiating MediaPlayer class
    MediaPlayer mediaPlayer = new MediaPlayer(media);
    MediaPlayer m = new MediaPlayer(media2);

    @FXML
    private Button generateButton;
    @FXML
    private Button solveButton;

    @FXML
    private Pane pane;
    @FXML
    private BorderPane borderPane;
    private static boolean isStopped;

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
        isStopped = true;
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
        if (isStopped == true) {
            mediaPlayer.setCycleCount(1000);
            mediaPlayer.setAutoPlay(true);
            mediaPlayer.play();
            isStopped = false;
        }

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
        ;
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
                    if (rowFromViewModel == maze.getGoalPosition().getRowIndex() && colFromViewModel == maze.getGoalPosition().getColumnIndex())//Solve Maze
                    {
                        win();
                        //playAgain();
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

    public void win() {

        //Instantiating MediaPlayer class
        //mediaPlayer.setCycleCount(1000);
        Stage stage = new Stage();
        stage.setTitle("winner");
        Pane board = new Pane();
        if(jonOrDean == "D") {
            Image img = new Image("/dragonWin.gif");
            ImageView image = new ImageView(img);
            board.getChildren().add(image);

        }
        else{
            Image img = new Image("/wolfWin.gif");
            ImageView image = new ImageView(img);
            board.getChildren().add(image);
        }
        if(isStopped == false) {
            this.mediaPlayer.pause();
            isStopped = true;
            m.setAutoPlay(true);
            m.play();
        }

       // board.getChildren().add(image);
        Scene scene = new Scene(board, 300, 300);
     //   scene.getStylesheets().addAll(this.getClass().getResource("win.css").toExternalForm());
        stage.setScene(scene);
        stage.show();

        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent we) {
                playAgain();
                stage.close();
                System.out.println("Stage is closing");
            }
        });
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

    public void playAgain() {
        m.pause();
        if(isStopped = true) {
            mediaPlayer.play();
            isStopped = false;
        }
//        }
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
        mazeDisplayer.drawMaze(maze);
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

        //use one of components on your scene to get a reference to your scene object.

        Stage stage = (Stage) textField_mazeRows.getScene().getWindow();//or use any other component in your controller
        Scene propWindow = new Scene(propWindowFXML, 800, 600);

        stage.setScene(propWindow);
        stage.show(); //this line may be unnecessary since you are using the same stage.

    }

    public List<Button> createCharStage(Stage stage){
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        int width = gd.getDisplayMode().getWidth();
        int height = gd.getDisplayMode().getHeight();
        List<Button> bList= new ArrayList<Button>();
        stage.setTitle("Welcome");
        Pane board = new Pane();
        Button jon = new Button();
        Button deanr = new Button();
        board.getChildren().addAll(jon , deanr);
        jon.setLayoutX(200);
        jon.setLayoutY(500);
        deanr.setLayoutX(600);
        deanr.setLayoutY(500);
        jon.setText("Jon Snow");
        deanr.setText("Daenerys Targaryen");
        Scene scene = new Scene(board, width/2, height/2);
        scene.getStylesheets().addAll(this.getClass().getResource("opening.css").toExternalForm());

        stage.setScene(scene);
        stage.widthProperty().addListener((obs, oldVal, newVal) -> {
            //jon.setLayoutX(newVal.doubleValue()-185);
            //deanr.setLayoutX(newVal.doubleValue());
            jon.setLayoutX((newVal.doubleValue()/4)-55);
            deanr.setLayoutX((newVal.doubleValue()/2)+75);

        });

        stage.heightProperty().addListener((obs, oldVal, newVal) -> {
            //jon.setLayoutX(newVal.doubleValue()-185);
            //deanr.setLayoutX(newVal.doubleValue());
            jon.setLayoutY((newVal.doubleValue()/2)+220);
            deanr.setLayoutY((newVal.doubleValue()/2)+220);

        });
        jon.prefWidthProperty().bind(Bindings.divide(stage.widthProperty(),5));
        jon.prefHeightProperty().bind(Bindings.divide(stage.heightProperty(),25));
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
                mazeDisplayer.setAllImg("J","./resources/ice.png" , "./resources/jon.png" , "./resources/wall.jpg" , "./resources/wolf.png");
                jonOrDean = "J";
                stage.close();
            }
        });


        deanr.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                mazeDisplayer.setAllImg("D" , "./resources/flame22.png", "./resources/khalisssi2.png" , "./resources/cloudd.png" ,"./resources/dragonDOWN.png");
                jonOrDean = "D";
                stage.close();
            }
        });

    }
}



