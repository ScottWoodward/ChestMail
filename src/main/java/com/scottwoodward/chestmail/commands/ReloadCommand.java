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
package com.scottwoodward.chestmail.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.scottwoodward.chestmail.ChestMail;
import com.scottwoodward.chestmail.database.ChestMailDatabaseManager;
import com.scottwoodward.chestmail.manager.ChestMailManager;
import com.scottwoodward.commons.commands.DynamicCommand;
import com.scottwoodward.commons.commands.ExecutableCommand;
import com.scottwoodward.commons.eventhandlers.DefaultDynamicEventManager;
import com.scottwoodward.commons.eventhandlers.DynamicEventManager;

/**
 * ReloadCommand.java
 * Purpose: Command to reload the plugin independently of the server.
 *
 * @author Scott Woodward
 * @since 2.0.0
 */
@DynamicCommand("reload")
public class ReloadCommand implements ExecutableCommand
{

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> validate(CommandSender sender, String[] args) {
        return new ArrayList<String>();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> tabComplete(Command command, String[] args) {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(CommandSender sender, String[] args) {
        ChestMailManager.loadConfiguration();
        DynamicEventManager eventManager = new DefaultDynamicEventManager();
        eventManager.unregisterEvents(ChestMail.getPluginName());
        eventManager.registerEvents("com.scottwoodward.chestmail.listeners", ChestMail.getPluginName());
        ChestMailManager.setupEconomy();
        ChestMailDatabaseManager.reloadProperties();
        
    }

}
