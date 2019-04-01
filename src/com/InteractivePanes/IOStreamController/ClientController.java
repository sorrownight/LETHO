package com.InteractivePanes.IOStreamController;

import com.ArtifactCore.Item;
import com.ArtifactCore.Menu;
import com.InteractivePanes.Client;
import database.Database_Controller;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.util.LinkedList;

public class ClientController implements EventHandler<ActionEvent>
{
    private int currentTable;
    private int currentItem;
    private int selectedItemIdx;
    private LinkedList<Label>[] itemOfTable;
    private GridPane grid;
    private Label previousLabel;
    private Label total;
    private double totalPrice;
    private double[] tableTotal;
    private Label tableNum;
    private Label orderLabel;

    public ClientController(LinkedList<Label>[] itemOfTable,GridPane grid, Label total,
                            Label tableNum, Label orderLabel)
            throws SQLException, FileNotFoundException
    {
        this.itemOfTable = itemOfTable;
        this.grid = grid;
        this.selectedItemIdx = -1;
        this.total = total;
        this.tableTotal = new double[Client.NUM_OF_TABLE];
        this.tableNum = tableNum;
        this.orderLabel = orderLabel;

        updateTotal();
    }

    public void setCurrentItem(int currentItem)
    {
        this.currentItem = currentItem;
    }

    @Override
    public void handle(ActionEvent event)
    {
        try
        {
            Object source = event.getSource();
            if (source instanceof Button)
            {

                Button bSource = (Button) source;

                switch (bSource.getText())
                {
                    case "Remove Item":
                        if (selectedItemIdx != -1 && currentTable > 0)
                        {
                            Database_Controller.deleteItemFromTable(currentTable, selectedItemIdx + 1);
                            updateTotal();
                            previousLabel = null;
                        }
                        break;
                    case "Add Item":
                        if (currentTable > 0 && currentItem > 0)
                        {
                            if (Menu.getSingleMenu().isInMenu(currentItem))
                            {
                                Database_Controller.addItemToTable(currentTable, currentItem);
                                updateTotal();
                            }
                            else
                            {
                                Alert fileNotFoundAlert = new Alert(Alert.AlertType.WARNING);
                                fileNotFoundAlert.setContentText("ITEM NOT FOUND");
                                fileNotFoundAlert.showAndWait();
                            }

                        }
                        break;
                    case "CHECK OUT":
                        Database_Controller.clearTable(currentTable);
                        updateTotal();
                        break;
                    default:
                        currentTable = Integer.parseInt(bSource.getText());

                        tableNum.setText("Table #" + currentTable);
                        orderLabel.setText("Items of table #" + currentTable);
                        updateTotal();
                        selectedItemIdx = -1;
                        break;
                }
            }

            if (currentTable > 0)
            {
                grid.getChildren().clear();
                int[] items = Database_Controller.getItemOfTable(currentTable);
                for (int i = 0; i < items.length; i++)
                {
                    Item tmpItem = Menu.getSingleMenu().getItem(items[i]);
                    Label tmp = new Label(tmpItem.toString());
                    int finalI = i;
                    tmp.setOnMouseClicked(event1 ->
                    {
                        if (previousLabel != null)
                            previousLabel.setStyle("-fx-background-color: #ffffff");

                        selectedItemIdx = finalI;
                        tmp.setStyle("-fx-background-color: #b3d9ff");
                        previousLabel = tmp;
                    });

                    itemOfTable[currentTable-1].add(tmp);
                    grid.add(tmp,0,i);
                }
                totalPrice = round(tableTotal[currentTable-1],2);
                total.setText("Total: " + totalPrice);

            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            Alert fileNotFoundAlert = new Alert(Alert.AlertType.ERROR);
            fileNotFoundAlert.setContentText("Error: SQL (137)");
            fileNotFoundAlert.showAndWait();
            Platform.exit();
        }
        catch (FileNotFoundException e)
        {
            Alert fileNotFoundAlert = new Alert (Alert.AlertType.ERROR);
            fileNotFoundAlert.setContentText("Error: TXT (144)");
            fileNotFoundAlert.showAndWait();
            Platform.exit();
        }
    }

    public double getChange(double given)
    {
        if (given - totalPrice < 0)
            return 0;
        return round(given - totalPrice,2);
    }

    /**
     * Source: https://stackoverflow.com/questions/2808535/round-a-double-to-2-decimal-places
     * @param value
     * @param places
     * @return
     */
    private static double round(double value, int places)
    {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    private void updateTotal()
            throws SQLException, FileNotFoundException
    {
        if (currentTable > 0)
        {
            for (int n = 0; n < tableTotal.length; n++)
            {
                tableTotal[n] = 0;
                for (int i : Database_Controller.getItemOfTable(n+1))
                {
                    tableTotal[n] += Menu.getSingleMenu().getItem(i).getPrice();
                }
            }
        }
    }
}
