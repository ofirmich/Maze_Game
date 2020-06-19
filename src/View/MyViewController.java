package View;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.scene.Node;
import javafx.scene.control.Button;
import ViewModel.MyViewModel;
import algorithms.mazeGenerators.Maze;
import algorithms.search.Solution;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;

public class MyViewController implements Initializable,Observer {
    private MyViewModel viewModel;
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

    @FXML private Button generateButton;
    @FXML private Button solveButton;
    @FXML private MenuItem exit;
    @FXML private MenuItem properties;
    private Main mainApp;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            VBox box = FXMLLoader.load(getClass().getResource("DrawerContent.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }


        lbl_player_row.textProperty().bind(update_player_position_row);
        lbl_player_column.textProperty().bind(update_player_position_col);

        generateButton.setDisable(false);
        solveButton.setDisable(true);
    }



    public void setViewModel(MyViewModel viewModel) {
        this.viewModel = viewModel;
    }


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



    public void generateMaze()
    {
        int rows = Integer.valueOf(textField_mazeRows.getText());
        int cols = Integer.valueOf(textField_mazeColumns.getText());
        viewModel.generateMaze(rows,cols);
        if(solveButton.isDisabled()) {
            solveButton.setDisable(false);
        }
      //  solveButton.setEnabled(true);
        //.setDisable(false);


    }

    public void solveMaze()
    {
        viewModel.solveMaze(this.maze);

        //sol = viewModel.getSolution();
      //  viewModel.getSolution();


    }


    public void showAlert(String message)
    {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(message);;
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
        if(o instanceof MyViewModel)
        {
            if(maze == null)//generateMaze
            {
                this.maze = viewModel.getMaze();
                drawMaze();
            }
            else {
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
                        this.mazeDisplayer.set_player_position(rowFromViewModel,colFromViewModel);
                    if(rowFromViewModel == maze.getGoalPosition().getRowIndex() && colFromViewModel == maze.getGoalPosition().getColumnIndex())//Solve Maze
                    {
                      //  viewModel.getSolution();
                        showAlert("you won!!! "); //do it better!!!!
                    }

                    if(viewModel.getSol() != null){
                        this.sol = viewModel.getSol();
                        drawSol();
                        sol = null;
                    }


                }
                else//GenerateMaze
                {
                    this.maze = maze;
                    drawMaze();
                }
            }

        }
    }

    public void drawMaze()
    {
        mazeDisplayer.drawMaze(maze);
    }

    public void drawSol()
    {
        mazeDisplayer.drawSol(sol);
    }

    public void saveMaze(ActionEvent actionEvent) {

    }

    public void loadMaze(ActionEvent actionEvent) {
    }

    public void opt(ActionEvent actionEvent) {
    }

    public void exitProg(ActionEvent actionEvent) {

        System.exit(0);
    }

    public void help(ActionEvent actionEvent) {
    }

    public void about() {
    }

    public void showProp(ActionEvent event) throws IOException {
        Parent tableViewParent = FXMLLoader.load(getClass().getResource("propView.fxml"));
        Scene tableViewScene = new Scene(tableViewParent);


        //This line gets the Stage information
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();

        window.setScene(tableViewScene);
        window.show();
    }
}


