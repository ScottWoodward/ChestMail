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
package com.scottwoodward.chestmail;

import org.bukkit.plugin.java.JavaPlugin;

import com.scottwoodward.chestmail.manager.ChestMailManager;
import com.scottwoodward.commons.commands.DefaultDynamicCommandExecutor;
import com.scottwoodward.commons.database.DatabaseManager;
import com.scottwoodward.commons.eventhandlers.DefaultDynamicEventManager;

/**
 * ChestMail.java
 * Purpose: Main class for ChestMail, handles plugin enabling and disabling.
 *
 * @author Scott Woodward
 * @since 2.0.0
 */
public class ChestMail extends JavaPlugin
{
    private static String PLUGIN_NAME;
       
    /**
     * Called when the plugin enables. Handles
     * <ul>
     * <li>Saves default config</li>
     * <li>Loads config</li>
     * <li>Registers commands</li>
     * <li>Registers listeners</li>
     * <li>Sets up Economy (if enabled)</li>
     * </ul>
     * 
     * @since 2.0.0
     */
    @Override
    public void onEnable()
    {
        PLUGIN_NAME = getName();
        registerCommands();
        registerListeners();
        saveDefaultConfig();
        ChestMailManager.loadConfiguration();
        new DatabaseManager(getName());
        ChestMailManager.setupEconomy();
    }
    
    /**
     * Called when the plugin disables and unregisters events.
     * 
     * @since 2.0.0
     */
    @Override
    public void onDisable()
    {
        new DefaultDynamicEventManager().unregisterEvents(getName());
    }
    
    /**
     * Loads all commands, based on package.
     * 
     * @since 2.0.0
     */
    private void registerCommands()
    {
        new DefaultDynamicCommandExecutor().loadCommands("com.scottwoodward.chestmail.commands", getName());
    }
    
    /**
     * Loads all listeners, based on package.
     * 
     * @since 2.0.0
     */
    private void registerListeners()
    {
        new DefaultDynamicEventManager().registerEvents("com.scottwoodward.chestmail.listeners", getName());
    }
    
    /**
     * Gets the name of this plugin, for referencing elsewhere.
     * 
     * @since 2.0.0
     * @return The name of the plugin.
     */
    public static String getPluginName()
    {
        return PLUGIN_NAME;
    }
}
