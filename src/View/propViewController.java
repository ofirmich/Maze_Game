package View;

import javafx.application.Platform;
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
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.util.Properties;
import java.util.ResourceBundle;

public class propViewController implements Initializable {
    ObservableList<String> generateList = FXCollections.observableArrayList("easy", "medium", "hard");
    @FXML
    private ChoiceBox generateChoose;
    @FXML
    private ChoiceBox solveChoose;
    @FXML
    private Button submit;
    @FXML
    private Label showGenerator;
    @FXML
    private Label showSolver;
    @FXML
    private Label showThreads;

    @FXML
    private Pane pane;
    @FXML
    private Pane littlePane;

   // private boolean themeMusicOn = MyViewController.themeMusicOn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        updateLable();
        generateChoose.setItems(generateList);
        generateChoose.setValue("hard");
//        littlePane.heightProperty().bind(pane.heightProperty());
//        littlePane.widthProperty().bind(pane.widthProperty());
        //submit.prefWidthProperty().bind(pane.widthProperty().divide(2));
        //submit.setLayoutX(littlePane.widthProperty().doubleValue());

    }

    public void updateLable(){

           // try (InputStream input = propViewController.class.getClassLoader().getResourceAsStream("resources/config.properties")) {
        try(InputStream input = new FileInputStream("resources/config.properties")){
                Properties prop = new Properties();

                if (input == null) {
                    System.out.println("Sorry, unable to find config.properties");
                    return;
                }
                //load a properties file from class path, inside static method
                prop.load(input);

                //get the property value and print it out
                showGenerator.setText(prop.getProperty("generateAlgo"));
                showSolver.setText(prop.getProperty("solveAlgo"));
                showThreads.setText(prop.getProperty("numOfThreads"));

            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }


    public void AfterSubmit(ActionEvent actionEvent) {
        String algoGen = generateChoose.getValue().toString();
        try {
            FileInputStream in = new FileInputStream("resources/config.properties");
            Properties prop = new Properties();
            prop.load(in);
            in.close();

            FileOutputStream out = new FileOutputStream("resources/config.properties");

            if (algoGen == "easy") {
                prop.setProperty("generateAlgo", "empty");
            } else if (algoGen == "medium") {
                prop.setProperty("generateAlgo", "simple");
            } else {
                prop.setProperty("generateAlgo", "MyMaze");
            }
            prop.store(out, null);
            showGenerator.setText(prop.getProperty("generateAlgo"));
            showSolver.setText(prop.getProperty("solveAlgo"));
            showThreads.setText(prop.getProperty("numOfThreads"));
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("MyView.fxml"));
            Parent root = fxmlLoader.load(getClass().getResource("MyView.fxml").openStream());
            MyViewController viewController=fxmlLoader.getController();
            if(viewController.jonOrDean == "D"){
                viewController.mazeDisplayer.setAllImg("D" , "./resources/Images/flame22.png", "./resources/Images/khalisssi2.png" , "./resources/Images/cloudd.png" ,"./resources/Images/dragonDOWN.png");
            }
            else{
                viewController.mazeDisplayer.setAllImg("J","./resources/Images/ice.png" , "./resources/Images/jon.png" , "./resources/Images/wall.jpg" , "./resources/Images/wolf.png");
            }
          //  FXMLLoader loader = new FXMLLoader(getClass().getResource("MyView.fxml"));
         //   Parent propWindowFXML = loader.load();
            Stage stage = (Stage) submit.getScene().getWindow();//or use any other component in your controller
            Scene backScene = new Scene(root, 800, 600);

            stage.setScene(backScene);

            MyViewController.themeMusicOn = true;
            //MyViewController.winOpen = false;
            stage.show(); //this line may be unnecessary since you are using the same stage.



        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
