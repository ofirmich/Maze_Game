package View;

import Server.Server;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.util.Observable;
import java.util.Properties;
import java.util.ResourceBundle;

public class propViewController implements Initializable {
    ObservableList<String> generateList = FXCollections.observableArrayList("empty","simple","myMaze");
    ObservableList<String> solveList = FXCollections.observableArrayList("DFS","BFS","best");
    @FXML private ChoiceBox generateChoose;
    @FXML private ChoiceBox solveChoose;
    @FXML private Button submit;
    String solVal = "DFS";
    String genVal = "MyMaze";
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Server.Configurations.confi();
        generateChoose.setItems(generateList);
        generateChoose.setValue("myMaze");
        solveChoose.setItems(solveList);
        solveChoose.setValue("DFS");

    }

    public void AfterSubmit(ActionEvent actionEvent) {
        String algoGen = generateChoose.getValue().toString();
        String algoSol = solveChoose.getValue().toString();
        try {
            OutputStream out = new FileOutputStream("config.properties");
            Properties prop = new Properties();

            prop.setProperty("generateAlgo" , algoGen);
            prop.setProperty("solveAlgo" , algoSol);
            prop.store(out,null);
            out.close();


            FXMLLoader loader = new FXMLLoader(getClass().getResource("MyView.fxml"));
            Parent propWindowFXML = loader.load();
            Stage stage = (Stage)submit.getScene().getWindow();//or use any other component in your controller
            Scene propWindow = new Scene (propWindowFXML, 800, 600);

            stage.setScene(propWindow);
            stage.show(); //this line may be unnecessary since you are using the same stage.
        }
        catch (IOException e){}


        //use one of components on your scene to get a reference to your scene object.



    }
}