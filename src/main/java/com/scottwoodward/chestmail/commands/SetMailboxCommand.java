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
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.scottwoodward.chestmail.ChestMailConstants;
import com.scottwoodward.chestmail.manager.ChestMailManager;
import com.scottwoodward.commons.commands.DynamicCommand;
import com.scottwoodward.commons.commands.ExecutableCommand;

/**
 * SetMailboxCommand.java
 * Purpose: 
 *
 * @author Scott Woodward
 */
@DynamicCommand("setmailbox")
public class SetMailboxCommand implements ExecutableCommand
{

    public void execute(CommandSender sender, String[] args)
    {
        ChestMailManager.setPlayerSettingMailBox((Player)sender, args[0]);
        sender.sendMessage(ChestMailConstants.PROMPT_SELECT_A_CHEST);
        ChestMailManager.withdrawForSettingMailBox((Player)sender);
    }

    public List<String> validate(CommandSender sender, String[] args)
    {
        List<String> errors = new ArrayList<String>();
        if(!(sender instanceof Player))
        {
            errors.add(ChestMailConstants.ERROR_ONLY_PLAYER_CAN_SET_MAILBOX);
        }
        else if(ArrayUtils.getLength(args) != 1 || StringUtils.isEmpty(args[0]))
        {
            errors.add(ChestMailConstants.ERROR_MUST_PROVIDE_MAILBOX_NAME);
        }
        else if(ChestMailManager.doesMailBoxWithNameExist(args[0]))
        {
            errors.add(ChestMailConstants.ERROR_MAILBOX_ALREADY_EXISTS_WITH_NAME);
        }
        else if(ChestMailManager.getEconomy() != null && !ChestMailManager.hasEnoughToSetMailBox((Player)sender))
        {
            errors.add(ChestMailConstants.ERROR_SETMAILBOX_NOT_ENOUGH_MONEY);
        }
        return errors;
    }

    @Override
    public List<String> tabComplete(Command command, String[] args) {
        return null;
    }
}
