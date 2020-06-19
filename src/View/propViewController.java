package View;
import Model.MyModel;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SnapshotResult;
import javafx.scene.control.MenuItem;
import javafx.scene.control.MenuBar;
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
import javafx.stage.Stage;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;


public class propViewController implements Initializable,Observer {

    //configure the table


    //These instance variables are used to create new Person objects
    @FXML private TextField firstNameTextField;
    @FXML private TextField lastNameTextField;


    @FXML private Button detailedPersonViewButton;




    /**
     * When this method is called, it will change the Scene to
     * a TableView example
     */
    public void changeScreenButtonPushed(ActionEvent event) throws IOException
    {
        Parent tableViewParent = FXMLLoader.load(getClass().getResource("MyView.fxml"));
        Scene tableViewScene = new Scene(tableViewParent);

        //This line gets the Stage information
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();

        window.setScene(tableViewScene);
        window.show();
    }

    /**
     * When this method is called, it will pass the selected Person object to
     * a the detailed view
     */
    public void changeSceneToMyView(ActionEvent event) throws IOException
    {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("MyView.fxml"));
        Parent tableViewParent = loader.load();

        Scene tableViewScene = new Scene(tableViewParent);

      /*  //access the controller and call a method
        PersonViewController controller = loader.getController();
        controller.initData(tableView.getSelectionModel().getSelectedItem());*/

        //This line gets the Stage information
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();

        window.setScene(tableViewScene);
        window.show();
    }



    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //set up the columns in the table
       /* firstNameColumn.setCellValueFactory(new PropertyValueFactory<Person, String>("firstName"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<Person, String>("lastName"));
        birthdayColumn.setCellValueFactory(new PropertyValueFactory<Person, LocalDate>("birthday"));

        //load dummy data
        tableView.setItems(getPeople());

        //Update the table to allow for the first and last name fields
        //to be editable
        tableView.setEditable(true);
        firstNameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        lastNameColumn.setCellFactory(TextFieldTableCell.forTableColumn());

        //This will allow the table to select multiple rows at once
        tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
*/
        //Disable the detailed person view button until a row is selected
        this.detailedPersonViewButton.setDisable(true);
    }


/*    *//**
     * This method will remove the selected row(s) from the table
     *//*
    public void deleteButtonPushed()
    {
        ObservableList<Person> selectedRows, allPeople;
        allPeople = tableView.getItems();

        //this gives us the rows that were selected
        selectedRows = tableView.getSelectionModel().getSelectedItems();

        //loop over the selected rows and remove the Person objects from the table
        for (Person person: selectedRows)
        {
            allPeople.remove(person);
        }
    }*/



 /*   *//**
     * This method will create a new Person object and add it to the table
     *//*
    public void newPersonButtonPushed()
    {
        Person newPerson = new Person(firstNameTextField.getText(),
                lastNameTextField.getText(),
                birthdayDatePicker.getValue());

        //Get all the items from the table as a list, then add the new person to
        //the list
        tableView.getItems().add(newPerson);
    }*/




    @Override
    public void update(Observable o, Object arg) {

    }
}