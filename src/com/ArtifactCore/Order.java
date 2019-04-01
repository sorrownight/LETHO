package com.ArtifactCore;

import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

public class Order
{
    private ArrayList<Item> items;
    private Menu menu;

    public Order()
            throws FileNotFoundException
    {
        this.items = new ArrayList<>();
        this.menu = Menu.getSingleMenu();
    }

    public Item addToOrder(int id)
    {
        items.add(menu.getItem(id));
        return menu.getItem(id);
    }

    public void removeFromOrder(int idx)
    {
        if (idx > items.size() || idx < 0)
            throw new IllegalArgumentException();

        items.remove(idx);
    }

    public double getPrice()
    {
        double total = 0;
        for (Item item : items)
           total += item.getPrice();
        total = total + total*10.2/100;
        return round(total,2);
    }

    public void checkOut()
    {
        items = new ArrayList<>();
    }

    public String toString()
    {
        StringBuilder s = new StringBuilder();
        for (Item item : items)
            s.append(item.toString()).append("\n");

        return s.toString();
    }

    public ArrayList<Item> getItems()
    {
        return items;
    }
    /**
     * Source: https://stackoverflow.com/questions/2808535/round-a-double-to-2-decimal-places
     * @param value
     * @param places
     * @return
     */
    private static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
