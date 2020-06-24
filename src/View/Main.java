package View;

import Model.MyModel;
import Server.Server;
import Server.ServerStrategyGenerateMaze;
import Server.ServerStrategySolveSearchProblem;
import ViewModel.MyViewModel;
import javafx.application.Application;
import View.MyViewController;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

import java.awt.*;
import java.io.File;
import java.nio.file.Paths;
import java.util.List;

public class Main extends Application {
    @Override

    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("MyView.fxml"));
        Parent root = fxmlLoader.load(getClass().getResource("MyView.fxml").openStream());
        MyViewController viewController=fxmlLoader.getController();
     //   viewController.mazeDisplayer.setAllImg("" , "./resources/jon.png" , "./resources/wall.jpeg" , "./resources/wolf.png");
        MyModel model = MyModel.getInstance();
        MyViewModel viewModel = MyViewModel.getInstance();
        model.addObserver(viewModel);
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        int width = gd.getDisplayMode().getWidth();
        int height = gd.getDisplayMode().getHeight();
        Stage stage = new Stage();
        List<Button> bList = viewController.createCharStage(stage);
        Button jon = bList.get(0);
        Button deanr = bList.get(1);
        jon.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                viewController.mazeDisplayer.setAllImg("J","./resources/ice.png" , "./resources/jon.png" , "./resources/wall.jpg" , "./resources/wolf.png");
                Scene scene2 = new Scene(root, width/2, height/2);
                viewController.jonOrDean = "J";
                primaryStage.setScene(scene2);
                primaryStage.show();
                stage.close();
            }
        });

        deanr.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                viewController.mazeDisplayer.setAllImg("D" , "./resources/flame22.png", "./resources/khalisssi2.png" , "./resources/cloudd.png" ,"./resources/dragonDOWN.png");
                viewController.jonOrDean = "D";
                Scene scene2 = new Scene(root, width/2, height/2);
                primaryStage.setScene(scene2);
                primaryStage.show();
                stage.close();
            }
        });



    }

    public static void main(String[] args) {
        Server mazeGeneratingServer = new Server(5400, 1000, new ServerStrategyGenerateMaze());
        mazeGeneratingServer.start();
        Server solveSearchProblemServer = new Server(5401, 1000, new ServerStrategySolveSearchProblem());
        solveSearchProblemServer.start();


        launch(args);
        mazeGeneratingServer.stop();
        solveSearchProblemServer.stop();
    }

}