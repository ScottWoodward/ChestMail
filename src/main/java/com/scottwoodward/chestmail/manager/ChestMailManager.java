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
package com.scottwoodward.chestmail.manager;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import net.milkbowl.vault.economy.Economy;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

import com.scottwoodward.chestmail.ChestMail;
import com.scottwoodward.chestmail.ChestMailConstants;
import com.scottwoodward.chestmail.database.ChestMailDatabaseManager;
import com.scottwoodward.commons.manager.AbstractManager;


/**
 * PlayerManager.java
 * Purpose: 
 *
 * @author Scott Woodward
 */
public class ChestMailManager extends AbstractManager
{

    private static Map<UUID, String> playersSettingMailBox = new HashMap<UUID, String>();
    private static Configuration config;
    private static Economy economy = null;

    public static boolean isPlayerSettingMailBox(Player player)
    {
        return playersSettingMailBox.containsKey(player.getUniqueId());   
    }

    public static String getMailBoxPlayerIsSetting(Player player)
    {
        return playersSettingMailBox.get(player.getUniqueId());
    }

    public static void setPlayerSettingMailBox(Player player, String mailbox)
    {
        playersSettingMailBox.put(player.getUniqueId(), mailbox);
    }

    public static void unsetPlayerSettingMailBox(Player player)
    {
        playersSettingMailBox.remove(player.getUniqueId());
    }

    public static void setPlayersMailBox(Player player, Location location)
    {

        ChestMailDatabaseManager dbManager = new ChestMailDatabaseManager(ChestMail.getPluginName());
        Connection connection = dbManager.getConnection();

        dbManager.setMailBox(connection, getMailBoxPlayerIsSetting(player), player.getUniqueId().toString(),
                location.getWorld().getName(), location.getBlockX(), location.getBlockY(), location.getBlockZ());

        dbManager.closeConnection(connection);
    }

    public static boolean doesMailBoxWithNameExist(String mailbox)
    {
        boolean doesMailBoxExist = false;

        ChestMailDatabaseManager dbManager = new ChestMailDatabaseManager(ChestMail.getPluginName());
        Connection connection = dbManager.getConnection();

        doesMailBoxExist = getChestAtLocation(dbManager.getMailBox(connection, mailbox)) != null;

        dbManager.closeConnection(connection);

        if(!doesMailBoxExist)
        {
            for(String value : playersSettingMailBox.values())
            {
                if(StringUtils.equalsIgnoreCase(value, mailbox))
                {
                    doesMailBoxExist = true;
                    break;
                }
            }
        }
        return doesMailBoxExist;
    }

    public static Chest getChestAtLocation(Location location)
    {
        Chest chest = null;
        if(location != null)
        {
            Block block = location.getBlock();
            if(block != null && (block.getType() == Material.CHEST || block.getType() == Material.TRAPPED_CHEST))
            {
                BlockState state = block.getState();
                if(state != null && state instanceof Chest)
                {
                    chest = (Chest)state;
                }
            }
        }
        return chest;
    }

    public static List<String> getSimilarMailBoxes(String name)
    {
        List<String> list = new ArrayList<String>();


        ChestMailDatabaseManager dbManager = new ChestMailDatabaseManager(ChestMail.getPluginName());
        Connection connection = dbManager.getConnection();

        list = dbManager.getSimilarMailBoxes(connection, name);

        dbManager.closeConnection(connection);

        return list;
    }

    public static Chest getMailBoxByName(String name)
    {
        Chest chest = null;

        ChestMailDatabaseManager dbManager = new ChestMailDatabaseManager(ChestMail.getPluginName());
        Connection connection = dbManager.getConnection();

        chest =  getChestAtLocation(dbManager.getMailBox(connection, name));

        dbManager.closeConnection(connection);

        return chest;
    }

    public static boolean isMailBoxFull(String name)
    {
        boolean isMailBoxFull = true;
        Chest chest = ChestMailManager.getMailBoxByName(name);
        if(chest != null)
        {
            isMailBoxFull = chest.getInventory().firstEmpty() == -1;
        }
        return isMailBoxFull;
    }

    public static void loadConfiguration()
    {
        config = Bukkit.getPluginManager().getPlugin(ChestMail.getPluginName()).getConfig();
    }

    public static Configuration getConfiguration()
    {
        if(config == null)
        {
            loadConfiguration();
        }
        return config;
    }

    public static boolean isMailBoxOwner(Player player, String name)
    {
        boolean isMailBoxOwner = false;

        ChestMailDatabaseManager dbManager = new ChestMailDatabaseManager(ChestMail.getPluginName());
        Connection connection = dbManager.getConnection();

        String uuidString =  dbManager.getOwnerOfMailbox(connection, name);
        UUID uuid = convertStringToUUID(uuidString);
        System.out.println(uuid);
        System.out.println(player.getUniqueId());
        isMailBoxOwner = (uuid.equals(player.getUniqueId()));

        dbManager.closeConnection(connection);

        return isMailBoxOwner;
    }
    
    public static void removeMailBox(String name)
    {
        ChestMailDatabaseManager dbManager = new ChestMailDatabaseManager(ChestMail.getPluginName());
        Connection connection = dbManager.getConnection();
        dbManager.deleteMailBox(connection, name);
        dbManager.closeConnection(connection);
    }
    
    public static void setupEconomy()
    {
        economy = null;
        if (Bukkit.getPluginManager().getPlugin("Vault") != null && config.getBoolean("EconomyEnabled"))
        {
            RegisteredServiceProvider<Economy> rsp = Bukkit.getServicesManager().getRegistration(Economy.class);
            if (rsp != null) {
                economy = rsp.getProvider();
            }
        }
    }
    
    public static Economy getEconomy()
    {
        return economy;
    }


    public static boolean hasEnoughToSetMailBox(Player player)
    {
        boolean hasEnoughMoney = true;
        if(economy != null)
        {
            double balance = economy.getBalance(player.getName());
            double cost = config.getDouble("CostToSetMailbox");
            hasEnoughMoney = (balance >= cost);
        }
        return hasEnoughMoney;
    }
    
    public static boolean hasEnoughToSendMail(Player player)
    {
        boolean hasEnoughMoney = true;
        if(economy != null)
        {
            double balance = economy.getBalance(player.getName());
            double cost = config.getDouble("FlatCostToSendMail");
            cost += (config.getDouble("CostPerItemSent") * player.getItemInHand().getAmount());
            hasEnoughMoney = (balance >= cost);
        }
        return hasEnoughMoney;
    }
    
    public static void withdrawForSettingMailBox(Player player)
    {
        if(economy != null)
        {
            double cost = config.getDouble("CostToSetMailbox");
            economy.withdrawPlayer(player.getName(), cost);
            player.sendMessage(ChestMailConstants.MESSAGE_WITHDRAW_SETMAILBOX + economy.format(cost));
        }
    }
    
    public static void reimburseForSettingMailBox(Player player)
    {
        if(economy != null)
        {
            double cost = config.getDouble("CostToSetMailbox");
            economy.depositPlayer(player.getName(), cost);
            player.sendMessage(ChestMailConstants.MESSAGE_DEPOSIT_SETMAILBOX + economy.format(cost) + ChatColor.YELLOW + economy.getName());
        }
    }
    
    public static void withdrawForSendingMail(Player player)
    {
        if(economy != null)
        {
            double cost = config.getDouble("FlatCostToSendMail");
            cost += (config.getDouble("CostPerItemSent") * player.getItemInHand().getAmount());
            economy.withdrawPlayer(player.getName(), cost);
            player.sendMessage(ChestMailConstants.MESSAGE_WITHDRAW_SENDMAIL + economy.format(cost));
        }
    }

}
