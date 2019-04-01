package com.InteractivePanes;

import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;

public class Admin extends GridPane
{
    private GridPane options;

    public Admin()
    {
        options = new GridPane();
        setUpOptions();

        this.add(options,0,0);
    }

    private void setUpOptions()
    {
        Button addItem = new Button("Thêm Món");
        Button removeItem = new Button("Xóa Món");
        Button setPrice = new Button("Thay đổi giá");
        Button changeNum = new Button("Đổi ID Món");

        addItem.setOnAction(event ->
        {

        });

        options.add(addItem,0,0);
        options.add(removeItem,0,1);
        options.add(setPrice,0,2);
        options.add(changeNum,0,3);

        options.setVgap(30);

    }
}
