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

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.scottwoodward.chestmail.ChestMailConstants;
import com.scottwoodward.chestmail.manager.ChestMailManager;
import com.scottwoodward.commons.commands.DynamicCommand;
import com.scottwoodward.commons.commands.ExecutableCommand;


/**
 * SendMailCommand.java
 * Purpose: 
 *
 * @author Scott Woodward
 */
@DynamicCommand("sendmail")
public class SendMailCommand implements ExecutableCommand
{
    @Override
    public List<String> validate(CommandSender sender, String[] args)
    {
        List<String> errors = new ArrayList<String>();
        if(!(sender instanceof Player))
        {
            errors.add(ChestMailConstants.ERROR_ONLY_PLAYER_CAN_SEND_MAIL);
        }
        else if(isHandEmpty((Player)sender))
        {
            errors.add(ChestMailConstants.ERROR_MUST_HOLD_ITEM_TO_SEND);
        }
        else if(ArrayUtils.getLength(args) != 1 || StringUtils.isEmpty(args[0]))
        {
            errors.add(ChestMailConstants.ERROR_SENDMAIL_SYNTAX);
        }
        else if(ChestMailManager.getMailBoxByName(args[0]) == null)
        {
            errors.add(ChestMailConstants.ERROR_NO_MAILBOX_WITH_THAT_NAME);
        }
        else if(ChestMailManager.isMailBoxFull(args[0]))
        {
            errors.add(ChestMailConstants.ERROR_MAILBOX_FULL);
        }
        else if(ChestMailManager.getEconomy() != null && !ChestMailManager.hasEnoughToSendMail((Player)sender))
        {
            errors.add(ChestMailConstants.ERROR_SENDMAIL_NOT_ENOUGH_MONEY);
        }
        return errors;
    }

    @Override
    public void execute(CommandSender sender, String[] args)
    {
        Player player = (Player) sender;
        ItemStack item = player.getItemInHand();
        Chest chest = ChestMailManager.getMailBoxByName(args[0]);
        if(chest != null)
        {
            int empty = chest.getInventory().firstEmpty();
            chest.getInventory().setItem(empty, item);
            ChestMailManager.withdrawForSendingMail(player);
            player.setItemInHand(new ItemStack(Material.AIR));
        }
    }
    
    private boolean isHandEmpty(Player player)
    {
        boolean isHandEmpty = true;
        ItemStack items = player.getItemInHand();
        if(items != null && items.getType() != Material.AIR)
        {
            isHandEmpty = false;
        }
        return isHandEmpty;
    }

    @Override
    public List<String> tabComplete(Command command, String[] args)
    {
        String arg = StringUtils.EMPTY;
        if(ArrayUtils.getLength(args) > 0)
        {
            arg = args[0];
        }
        return ChestMailManager.getSimilarMailBoxes(arg);
    }
}
