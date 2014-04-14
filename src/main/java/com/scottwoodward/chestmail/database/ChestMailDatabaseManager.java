/**
 * Copyright 2013 - 2014 Scott Woodward
 *
 * This file is part of ChestMail
 *
 * ChestMail is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * ChestMail is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with ChestMail.  If not, see <http://www.gnu.org/licenses/>. 
 */
package com.scottwoodward.chestmail.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import com.scottwoodward.chestmail.ChestMailConstants;
import com.scottwoodward.commons.database.DatabaseManager;

/**
 * ChestMailDatabaseManager.java
 * Purpose: 
 *
 * @author Scott Woodward
 */
public class ChestMailDatabaseManager extends DatabaseManager
{

    public ChestMailDatabaseManager(String name) 
    {
        super(name);
        setupDatabase();
    }

    public Location getMailBox(Connection connection, String mailbox)
    {
        Location location = null;
        try
        {
            PreparedStatement query = connection.prepareStatement(ChestMailConstants.QUERY_GET_MAILBOX);
            query.setString(1, mailbox);
            ResultSet rs = query.executeQuery();
            while(rs.next())
            {
                World world = Bukkit.getWorld(rs.getString(1));
                location = new Location(world, rs.getDouble(2), rs.getDouble(3), rs.getDouble(4));
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return location;
    }

    private void setupDatabase()
    {
        Connection connection = getConnection();
        try
        {
            PreparedStatement query = connection.prepareStatement(ChestMailConstants.QUERY_CREATE_TABLE);
            query.executeUpdate();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        closeConnection(connection);
    }

    public void setMailBox(Connection connection, String mailbox, String playerUUID, String world, int x, int y, int z)
    {
        try
        {
            PreparedStatement query = connection.prepareStatement(ChestMailConstants.QUERY_SET_MAILBOX);
            query.setString(1, mailbox);
            query.setString(2, playerUUID);
            query.setString(3, world);
            query.setInt(4, x);
            query.setInt(5, y);
            query.setInt(6, z);
            query.executeUpdate();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public List<String> getSimilarMailBoxes(Connection connection, String name)
    {
        List<String> list = new ArrayList<String>();
        try
        {
            PreparedStatement query = connection.prepareStatement(ChestMailConstants.QUERY_GET_SIMILAR_MAILBOX_NAMES);
            query.setString(1, name + "%");
            ResultSet rs = query.executeQuery();
            while(rs.next())
            {
                list.add(rs.getString(1));
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return list;
    }

    public String getOwnerOfMailbox(Connection connection, String name)
    {
        String uuid = null;
        try
        {
            PreparedStatement query = connection.prepareStatement(ChestMailConstants.QUERY_GET_MAILBOX_OWNER);
            query.setString(1, name);
            ResultSet rs = query.executeQuery();
            while(rs.next())
            {
                uuid = rs.getString(1);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return uuid;
    }

    public void deleteMailBox(Connection connection, String name)
    {
        try
        {
            PreparedStatement query = connection.prepareStatement(ChestMailConstants.QUERY_DELETE_MAILBOX);
            query.setString(1, name);
            query.execute();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}
