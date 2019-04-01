package com.ArtifactCore;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * A Singleton instance that allows only one instance of this object at Runtime
 */

public class Menu
{
    private ArrayList<Item> items;
    private static Menu singleMenu = null;

    private Menu()
            throws FileNotFoundException
    {
        items = new ArrayList<>();

        Scanner inStream = new Scanner(new File(
                "MenuOut.txt"));
        while (inStream.hasNext())
            items.add(new Item(inStream.nextInt(),inStream.next(),inStream.nextDouble()));

    }

    public static Menu getSingleMenu()
            throws FileNotFoundException
    {
        if (singleMenu == null)
            singleMenu = new Menu();

        return singleMenu;
    }

    public Item getItem (int id)
    {
        /*if (id > items.size())
            throw new IllegalArgumentException();*/
        for (Item item : items)
        {
            if (item.getItemNum() == id)
                return item;
        }
        throw new IllegalArgumentException();
    }

    public int getMenuSize()
    {
        return items.size();
    }

    @Override
    public String toString()
    {
        StringBuilder s = new StringBuilder();
        for (Item item : items)
            s.append(item.toString()).append("\n");
        return s.toString();
    }

    public boolean isInMenu (int id)
    {
        for (Item item : items)
        {
            if (item.getItemNum() == id)
                return true;
        }
        return false;
    }
}
