package com.ArtifactCore;

public class Item
{
    private String name;
    private double price;
    private int itemNum;

    public Item(int itemNum, String name, double price)
    {
        if (name == null || name.isEmpty())
            throw new IllegalArgumentException("Invalid item name");
        if (price < 0)
            throw new IllegalArgumentException("Negative price");
        if (itemNum < 0)
            throw new IllegalArgumentException("Invalid item number");

        this.name = name;
        this.price = price;
        this.itemNum = itemNum;
    }

    public double getPrice()
    {
        return price + price*10.3/100;
    }

    public int getItemNum()
    {
        return itemNum;
    }

    public String getName()
    {
        return name;
    }

    @Override
    public String toString()
    {
        return "#"+itemNum + " " + name +" $" +price;
    }
}
