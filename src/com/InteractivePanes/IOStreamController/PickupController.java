package com.InteractivePanes.IOStreamController;

import com.ArtifactCore.Menu;
import com.Exceptions.ClassSourceException;
import com.Exceptions.FileSourceNotFoundException;
import com.Exceptions.SQLSourceException;
import database.Database_Controller;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;

public class PickupController implements EventHandler<ActionEvent>
{
    private String currentName;
    private String currentPhone;
    private TextField nameTF;
    private TextField phoneTF;
    private Label previousLabel;
    private Label previousItem;
    private GridPane orderGrid;
    private GridPane itemGrid;
    private int currentItem;
    private int currentID;
    private Label phoneNumber;
    private double totalPrice;
    private double changeRemain;
    private Label total;

    public PickupController(TextField nameTF, TextField phoneTF, GridPane orderGrid, GridPane itemGrid,Label phoneNumber,
                            Label total)
            throws FileNotFoundException
    {
        this.currentName = "";
        this.currentPhone = "";
        this.nameTF = nameTF;
        this.phoneTF = phoneTF;
        this.orderGrid = orderGrid;
        this.itemGrid = itemGrid;
        this.currentItem = -1;
        this.currentID = -1;
        this.phoneNumber = phoneNumber;
        this.total = total;

        handleCreateOrder();
    }

    @Override
    public void handle(ActionEvent event)
    {
        try
        {
            if (event.getSource() instanceof Button)
            {
                Button bSource = (Button) event.getSource();

                switch (bSource.getText())
                {
                    case "CREATE ORDER":
                        Database_Controller.createPickupOrder(currentName, currentPhone);
                        handleCreateOrder();
                        break;
                    case "DELETE ORDER":
                        if (previousLabel != null)
                        {
                            Database_Controller.deleteOrder(previousLabel.getText());
                            previousLabel = null;
                            handleCreateOrder();
                        }
                        break;
                    case "ADD ITEM":
                        if (currentItem != -1 && previousLabel != null)
                        {
                            Database_Controller.addItemToPickupOrder(previousLabel.getText(),currentItem);
                            handleSelectOrder();
                        }
                        break;
                    case "REMOVE ITEM":
                        if (currentID != -1 && previousLabel != null)
                        {
                            Database_Controller.deleteItemFromOrder(previousLabel.getText(),currentID);
                            handleSelectOrder();
                        }
                }
                updateTotal();
                total.setText("Total: " + totalPrice);
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            throw new SQLSourceException();
        }
        catch (ClassNotFoundException e)
        {
            throw new ClassSourceException();
        }
        catch (FileNotFoundException e)
        {
            throw new FileSourceNotFoundException();
        }
        finally
        {
            nameTF.clear();
            phoneTF.clear();
        }
    }

    public void setCurrentName(String currentName)
    {
        this.currentName = currentName;
    }

    public void setCurrentPhone(String currentPhone)
    {
        this.currentPhone = currentPhone;
    }

    public void setCurrentItem(int currentItem)
    {
        this.currentItem = currentItem;
    }

    private void handleCreateOrder()
    {
        orderGrid.getChildren().clear();

        try
        {
            int i = 0;
            for (String s : Database_Controller.getPickupOrders())
            {
                Label tmp = new Label(s);
                tmp.setOnMouseClicked(event ->
                {
                    if (previousLabel != null)
                        previousLabel.setStyle("-fx-background-color: #ffffff");

                    tmp.setStyle("-fx-background-color: #b3d9ff");
                    previousLabel = tmp;

                    try
                    {
                        phoneNumber.setText("Phone number: "
                                + Database_Controller.getPhoneNumber(previousLabel.getText()));
                        handleSelectOrder();
                        updateTotal();
                        total.setText("Total: " + totalPrice);
                    }
                    catch (FileNotFoundException e)
                    {
                        throw new FileSourceNotFoundException();
                    }
                    catch (SQLException e)
                    {
                        throw new SQLSourceException();
                    }
                });

                orderGrid.add(tmp,0,i);
                i++;
            }
        }
        catch (SQLException e)
        {
            throw new SQLSourceException();
        }
    }

    private void handleSelectOrder() throws FileNotFoundException
    {
        itemGrid.getChildren().clear();

        try
        {
            if (previousLabel != null)
            {
                int i = 0;
                for (int n : Database_Controller.getItemOfOrder(previousLabel.getText()))
                {
                    Label tmp = new Label(Menu.getSingleMenu().getItem(n).toString());
                    int finalI = i;
                    tmp.setOnMouseClicked(event ->
                    {
                        if (previousItem != null)
                            previousItem.setStyle("-fx-background-color: #ffffff");

                        tmp.setStyle("-fx-background-color: #b3d9ff");
                        previousItem = tmp;
                        currentID = finalI +1;
                    });

                    itemGrid.add(tmp, 0, i);
                    i++;
                }
            }
        }
        catch (SQLException e)
        {
            throw new SQLSourceException();
        }
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
        totalPrice = 0;
        if (previousLabel != null)
        {
            for (int i : Database_Controller.getItemOfOrder(previousLabel.getText()))
            {
                totalPrice += Menu.getSingleMenu().getItem(i).getPrice();
            }
        }

        totalPrice = round(totalPrice,2);
    }

    public double getChange(double given)
    {
        if (given - totalPrice < 0)
            return 0;
        return round(given - totalPrice,2);
    }
}
