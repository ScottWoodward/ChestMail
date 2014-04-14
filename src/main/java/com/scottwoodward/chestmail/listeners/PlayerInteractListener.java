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
package com.scottwoodward.chestmail.listeners;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import com.scottwoodward.chestmail.ChestMailConstants;
import com.scottwoodward.chestmail.manager.ChestMailManager;

/**
 * PlayerInteractListener.java
 * Purpose: 
 *
 * @author Scott Woodward
 */
public class PlayerInteractListener implements Listener
{

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event)
    {
        if(!event.isCancelled() && ChestMailManager.isPlayerSettingMailBox(event.getPlayer()))
        {
            boolean success = validate(event);
            if(success)
            {
                success = setMailBox(event);
            }
            if(!success)
            {
                ChestMailManager.reimburseForSettingMailBox(event.getPlayer());
            }
            ChestMailManager.unsetPlayerSettingMailBox(event.getPlayer());
            event.setCancelled(true);
        }
    }
    
    private boolean validate(PlayerInteractEvent event)
    {
        boolean success = true;
        String error = null;
        
        if(!isPlayerClickingChest(event))
        {
            error = ChestMailConstants.ERROR_INVALID_MAILBOX_SELECTION;
            success = false;
        }
        
        if(error != null)
        {
            event.getPlayer().sendMessage(error);
        }
        return success;
    }
    
    private boolean isPlayerClickingChest(PlayerInteractEvent event)
    {
        boolean isPlayerClickingChest = false;
        if(event.getAction() == Action.LEFT_CLICK_BLOCK)
        {
            Block block = event.getClickedBlock();
            if(block != null)
            {
                if(block.getType() == Material.CHEST || block.getType() == Material.TRAPPED_CHEST)
                {
                    isPlayerClickingChest = true;
                }
            }
        }
        return isPlayerClickingChest;
    }
    
    private boolean setMailBox(PlayerInteractEvent event)
    {
        boolean success = true;
        Location location = event.getClickedBlock().getLocation();
        ChestMailManager.setPlayersMailBox(event.getPlayer(), location);
        return success;
    }
}
