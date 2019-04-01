package com.InteractivePanes;

import com.ArtifactCore.Menu;
import com.Exceptions.ClassSourceException;
import com.Exceptions.FileSourceNotFoundException;
import com.Exceptions.SQLSourceException;
import com.InteractivePanes.IOStreamController.ClientController;
import com.InteractivePanes.IOStreamController.PickupController;
import database.Database_Controller;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import root.PhoAnApp;

import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.LinkedList;


public class Pickup extends BorderPane
{
    public Pickup()
    {
        try
        {
            Database_Controller.initializePickup(Client.NUM_OF_TABLE);
            Menu.getSingleMenu();

            this.setPadding(new Insets(20,20,20,10));

            Button tables = new Button("TABLES");
            tables.setOnAction(event -> PhoAnApp.swapRoot(this));

            ScrollPane itemScroll = new ScrollPane();
            itemScroll.setStyle("-fx-background-color: #ffffff");
            itemScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
            itemScroll.setFitToHeight(true);
            itemScroll.setFitToWidth(true);
            itemScroll.setVmax(500);
            itemScroll.setVvalue(500);
            itemScroll.setPrefSize(600,500);

            GridPane tmpGrid = new GridPane();
            tmpGrid.setStyle("-fx-background-color: #ffffff");
            tmpGrid.setPrefHeight(500);
            tmpGrid.setPrefWidth(600);

            itemScroll.setContent(tmpGrid);

            ScrollPane orderScroll = new ScrollPane();
            orderScroll.setStyle("-fx-background-color: #ffffff");
            orderScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
            orderScroll.setFitToHeight(true);
            orderScroll.setFitToWidth(true);
            orderScroll.setVmax(500);
            orderScroll.setVvalue(500);
            orderScroll.setPrefSize(600,500);

            GridPane orderGrid = new GridPane();
            tmpGrid.setStyle("-fx-background-color: #ffffff");
            tmpGrid.setPrefHeight(500);
            tmpGrid.setPrefWidth(600);

            orderScroll.setContent(orderGrid);

            BorderPane rightPane = new BorderPane();
            GridPane upperPane = new GridPane();
            Label info1 = new Label("Create new Pickup Order");
            upperPane.add(info1,0,0);

            GridPane insideUpper = new GridPane();

            Label nameLabel = new Label("Customer's name: ");
            Label phoneNumber = new Label("Customer's phone number: ");

            TextField nameTF = new TextField();
            TextField phoneTF = new TextField();

            Label currentPhone = new Label("Phone number: ");
            Label total = new Label("Total: 0.0");
            total.setStyle("-fx-font: 24 arial; -fx-background-color:#ff0000");

            Label change = new Label("Remaining change: 0.0");
            change.setStyle("-fx-font: 24 arial; -fx-background-color:#ff8f00");

            PickupController controller = new PickupController(nameTF,phoneTF,orderGrid,tmpGrid,currentPhone,total);

            nameTF.setOnKeyReleased(event -> controller.setCurrentName(nameTF.getText()));
            phoneTF.setOnKeyReleased(event -> controller.setCurrentPhone(phoneTF.getText()));

            Button createOrder = new Button("CREATE ORDER");
            createOrder.setOnAction(controller);

            Button deleteOrder = new Button("DELETE ORDER");
            deleteOrder.setOnAction(controller);

            GridPane botRightPane = new GridPane();
            GridPane upperBotPane = new GridPane();

            Label info2 = new Label("Add Item to Order");
            Label itemLabel = new Label("Item number");
            TextField itemField = new TextField();

            Button addItem = new Button("ADD ITEM");
            addItem.setDisable(true);
            Button removeItem = new Button("REMOVE ITEM");

            addItem.setOnAction(controller);
            removeItem.setOnAction(controller);


            itemField.setOnKeyReleased(event ->
            {
                try
                {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                            "Please choose the size", new ButtonType("LARGE"),
                            new ButtonType("SMALL"), ButtonType.CANCEL);

                    Alert alert_noodles = new Alert(Alert.AlertType.CONFIRMATION,
                            "Please choose Stir-fried noodles", new ButtonType("Chicken"),
                            new ButtonType("Pork"), new ButtonType("Beef"), new ButtonType("Shrimp"),
                            new ButtonType("Tofu"), new ButtonType("Combo"), ButtonType.CANCEL);

                    if ((Integer.parseInt(itemField.getText()) >= 18 && Integer.parseInt(itemField.getText()) <= 34) ||
                            Integer.parseInt(itemField.getText()) == 76)
                    {
                        alert.showAndWait();
                        if (alert.getResult().getText().equals("SMALL"))
                            changeID(controller,itemField,1);

                        else if(alert.getResult().getText().equals("LARGE"))
                            changeID(controller,itemField,2);
                    }

                    else if (Integer.parseInt(itemField.getText()) >= 100 && Integer.parseInt(itemField.getText()) <= 102)
                    {
                        alert_noodles.showAndWait();
                        switch (alert_noodles.getResult().getText())
                        {
                            case "Chicken":
                                changeID(controller,itemField,1);
                                break;
                            case "Pork":
                                changeID(controller,itemField,2);
                                break;
                            case "Beef":
                                changeID(controller,itemField,3);
                                break;
                            case "Shrimp":
                                changeID(controller,itemField,4);
                                break;
                            case "Tofu":
                                changeID(controller,itemField,5);
                                break;
                            case "Combo":
                                changeID(controller,itemField,6);
                                break;
                        }
                    }
                    else
                        controller.setCurrentItem(Integer.parseInt(itemField.getText()));

                    addItem.setDisable(false);
                }
                catch (NumberFormatException e)
                {
                    itemField.clear();
                    addItem.setDisable(true);
                }
            });

            upperBotPane.add(info2,0,0);
            upperBotPane.add(itemLabel,0,1);
            upperBotPane.add(itemField,1,1);
            upperBotPane.add(addItem,2,1);
            upperBotPane.add(removeItem,3,1);


            currentPhone.setTextFill(Color.web("#FF0000"));

            upperBotPane.add(currentPhone,3,2);
            upperBotPane.add(total,3,3);

            TextField changeTF = new TextField();
            changeTF.setOnKeyReleased(event ->
            {
                try
                {
                    change.setText("Remaining change: "
                            + controller.getChange(Double.parseDouble(changeTF.getText())));
                }
                catch (NumberFormatException e)
                {
                    changeTF.clear();
                }
            });

            upperBotPane.add(changeTF,3,4);
            upperBotPane.add(change,3,5);


            upperBotPane.setStyle("-fx-background-color: rgba(0, 100, 100, 0.5); -fx-background-radius: 10;");

            botRightPane.add(upperBotPane,0,0);
            botRightPane.add(itemScroll,0,1);

            rightPane.setBottom(botRightPane);

            insideUpper.add(nameLabel,0,0);
            insideUpper.add(phoneNumber,0,1);
            insideUpper.add(nameTF,1,0);
            insideUpper.add(phoneTF,1,1);
            insideUpper.add(createOrder,0,2);

            upperPane.add(insideUpper,0,1);
            rightPane.setTop(upperPane);

            GridPane controlBot = new GridPane();
            controlBot.add(tables,0,0);
            controlBot.add(deleteOrder,1,0);

            rightPane.setPadding(new Insets(0,0,0,10));

            this.setRight(rightPane);
            this.setLeft(orderScroll);
            this.setBottom(controlBot);
        }
        catch (FileNotFoundException | FileSourceNotFoundException e)
        {
            Alert fileNotFoundAlert = new Alert (Alert.AlertType.ERROR);
            fileNotFoundAlert.setContentText("Error: TXT (172)");
            fileNotFoundAlert.showAndWait();
            Platform.exit();
        }
        catch (ClassNotFoundException | ClassSourceException e)
        {
            Alert fileNotFoundAlert = new Alert (Alert.AlertType.ERROR);
            fileNotFoundAlert.setContentText("Error: Class (179)");
            fileNotFoundAlert.showAndWait();
            Platform.exit();
        }
        catch (SQLException | SQLSourceException e)
        {
            e.printStackTrace();
            Alert fileNotFoundAlert = new Alert(Alert.AlertType.ERROR);
            fileNotFoundAlert.setContentText("Error: SQL (187)");
            fileNotFoundAlert.showAndWait();
            Platform.exit();
        }


    }
    private void changeID (PickupController c, TextField itemField, int add)
    {
        c.setCurrentItem(Integer.parseInt(itemField.getText() + add));
        itemField.setText(itemField.getText() + add);
    }
}
