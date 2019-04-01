package com.InteractivePanes;

import com.ArtifactCore.Menu;
import com.InteractivePanes.IOStreamController.ClientController;
import database.Database_Controller;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import root.PhoAnApp;

import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.LinkedList;

public class Client extends BorderPane
{
    public static final int NUM_OF_TABLE = 15;

    /**
     * Each index of the array corresponds to a specific table with the same number as the index + 1
     */
    public Client()
    {
        this.setPadding(new Insets(20,20,20,20));

        Button pickupB = new Button("PICKUP");
        pickupB.setOnAction(event -> PhoAnApp.swapRoot(this));
        pickupB.setPrefSize(180,20);

        try
        {
            Database_Controller.initializeTables(NUM_OF_TABLE);

            FlowPane tableView = new FlowPane(20,20);
            GridPane infoView = new GridPane();
            GridPane topInfo = new GridPane();
            GridPane botInfo = new GridPane();
            GridPane itemView = new GridPane();

            /*
            Items View
             */
            Label menuList = new Label(Menu.getSingleMenu().toString());
            ScrollPane menuScroll = new ScrollPane(menuList);
            menuScroll.setPrefSize(400,500);

            Label orderLabel = new Label("Items of table #");
            Label menuLabel = new Label("Full Menu:");

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

            LinkedList<Label>[] itemOfTable = new LinkedList[NUM_OF_TABLE];
            for (int i = 0; i < itemOfTable.length; i++)
                itemOfTable[i] = new LinkedList<>();

            itemView.setPrefSize(1000,600);
            itemView.add(orderLabel,0,0);
            itemView.add(menuLabel,1,0);
            itemView.add(itemScroll,0,1);
            itemView.add(menuScroll, 1, 1);

            /*
            Top Info
            */
            Label topInfoTitle = new Label("Table Info");
            Label tableNum = new Label("Table # ");
            Label total = new Label("Total: 0.0");
            total.setStyle("-fx-font: 24 arial; -fx-background-color:#ff0000");
            topInfo.setPrefWidth(400);
            topInfo.setStyle("-fx-background-color: rgba(0, 100, 100, 0.5); -fx-background-radius: 10;");
            topInfo.setPadding(new Insets(5,5,5,5));

            TextField moneyGiven = new TextField();
            Label change = new Label("Remaining change: 0.0");
            change.setStyle("-fx-font: 24 arial; -fx-background-color:#ff8f00");

            topInfo.add(topInfoTitle,0,0);
            topInfo.add(tableNum,0,1);
            topInfo.add(total,0,2);
            topInfo.add(moneyGiven,0,3);
            topInfo.add(change,0,4);

            /*
            Bottom Info
             */
            Button addItem = new Button("Add Item");
            Button removeButton = new Button("Remove Item");
            Label itemName = new Label("");
            addItem.setDisable(true);
            TextField itemField = new TextField();


            botInfo.add(itemField,0,0);
            botInfo.add(addItem,0,1);
            botInfo.add(removeButton,1,1);
            botInfo.add(itemName,0,2);

             /*
            Table View
            */
            Button[] tables = new Button[NUM_OF_TABLE];
            tableView.setStyle("-fx-background-color: rgba(0, 100, 100, 0.5); -fx-background-radius: 10;");
            tableView.setPadding(new Insets(8,8,8,8));



             /*
            Check out Button
             */
            Button checkOut = new Button("CHECK OUT");
            GridPane bufferPane = new GridPane();
            GridPane innerBufferPane = new GridPane();
            innerBufferPane.setPrefWidth(450);
            bufferPane.add(innerBufferPane,0,0);
            bufferPane.add(checkOut,1,0);

            this.setLeft(tableView);
            this.setRight(infoView);
            GridPane gPaneBot = new GridPane();
            gPaneBot.add(itemView,0,1);
            gPaneBot.add(bufferPane,0,0);
            gPaneBot.add(pickupB,1,2);
            this.setBottom(gPaneBot);

            infoView.add(topInfo,0,0);
            infoView.add(botInfo,0,1);


            ClientController controller = new ClientController(itemOfTable, tmpGrid, total, tableNum,orderLabel);


            // Initialize Table buttons

            for (int i = 0; i < tables.length; i++)
            {
                tables[i] = new Button(Integer.toString(i+1));

                tables[i].setOnAction(controller);

                tableView.getChildren().add(tables[i]);
            }

            // Initialize Field to input item number

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

            moneyGiven.setOnKeyReleased(event ->
            {
                try
                {
                    change.setText("Remaining change: "
                            + controller.getChange(Double.parseDouble(moneyGiven.getText())));
                }
                catch (NumberFormatException e)
                {
                    moneyGiven.clear();
                }
            });

            // Set Controller

            addItem.setOnAction(controller);
            removeButton.setOnAction(controller);
            checkOut.setOnAction(controller);

        }
        catch (FileNotFoundException e)
        {
            Alert fileNotFoundAlert = new Alert (Alert.AlertType.ERROR);
            fileNotFoundAlert.setContentText("Error: TXT (172)");
            fileNotFoundAlert.showAndWait();
            Platform.exit();
        }
        catch (ClassNotFoundException e)
        {
            Alert fileNotFoundAlert = new Alert (Alert.AlertType.ERROR);
            fileNotFoundAlert.setContentText("Error: Class (179)");
            fileNotFoundAlert.showAndWait();
            Platform.exit();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            Alert fileNotFoundAlert = new Alert(Alert.AlertType.ERROR);
            fileNotFoundAlert.setContentText("Error: SQL (187)");
            fileNotFoundAlert.showAndWait();
            Platform.exit();
        }
    }

    private void changeID (ClientController c, TextField itemField, int add)
    {
        c.setCurrentItem(Integer.parseInt(itemField.getText() + add));
        itemField.setText(itemField.getText() + add);
    }
}


