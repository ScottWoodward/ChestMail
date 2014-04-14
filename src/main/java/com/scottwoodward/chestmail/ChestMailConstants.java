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

import org.bukkit.ChatColor;

import com.scottwoodward.commons.Constants;

/**
 * ChestMailConstants.java
 * Purpose: Contains constants for use throughout the plugin. 
 *
 * @author Scott Woodward
 * @since 2.0.0
 */
public class ChestMailConstants extends Constants
{
    //////////////////
    // ERROR MESSAGES
    //////////////////
    public static final String ERROR_ONLY_PLAYER_CAN_SET_MAILBOX = ChatColor.RED + "Only a player may set a mailbox.";
    public static final String ERROR_MUST_PROVIDE_MAILBOX_NAME = ChatColor.RED + "Usage: /setmailbox <MailBox Name>";
    public static final String ERROR_MAILBOX_ALREADY_EXISTS_WITH_NAME = ChatColor.RED + "A mailbox already exists with that name.";
    public static final String ERROR_MAILBOX_FULL = ChatColor.RED + "That mailbox is full!";
    public static final String ERROR_NO_MAILBOX_WITH_THAT_NAME = ChatColor.RED + "There is no mailbox with that name";
    public static final String ERROR_ONLY_PLAYER_CAN_SEND_MAIL = ChatColor.RED + "Only a player can send mail.";
    public static final String ERROR_INVALID_MAILBOX_SELECTION = ChatColor.RED + "You must select a chest as your mailbox.";
    public static final String ERROR_MUST_HOLD_ITEM_TO_SEND = ChatColor.RED + "You must be holding the items you with to send.";
    public static final String ERROR_SENDMAIL_SYNTAX = ChatColor.RED + "Usage: /sendmail <MailBox Name>";
    public static final String ERROR_REMOVEMAILBOX_SYNTAX = ChatColor.RED + "Usage: /removemailbox <MailBox Name>";
    public static final String ERROR_CAN_ONLY_REMOVE_OWN_MAILBOX = ChatColor.RED + "You may only remove your own mailbox.";
    public static final String ERROR_SETMAILBOX_NOT_ENOUGH_MONEY = ChatColor.RED + "You do not have enough money to set a mailbox.";
    public static final String ERROR_SENDMAIL_NOT_ENOUGH_MONEY = ChatColor.RED + "You do not have enough money to send mail.";
    
    ///////////
    // PROMPTS
    ///////////
    public static final String PROMPT_SELECT_A_CHEST = ChatColor.YELLOW + "Left click a chest to use as a mailbox.";
    
    ////////////
    // MESSAGES
    ////////////
    public static final String MESSAGE_WITHDRAW_SETMAILBOX = ChatColor.YELLOW + "For setting your mailbox, you have been charged " + ChatColor.GREEN;
    public static final String MESSAGE_WITHDRAW_SENDMAIL = ChatColor.YELLOW + "For sending mail, you have been charged " + ChatColor.GREEN;
    public static final String MESSAGE_DEPOSIT_SETMAILBOX = ChatColor.YELLOW + "You have been reimbursed " + ChatColor.GREEN;
    
    ///////////
    // QUERIES
    ///////////
    public static final String QUERY_CREATE_TABLE = "CREATE TABLE IF NOT EXISTS ChestMail_MailBox(MailBoxName VARCHAR(256), PlayerUUID VARCHAR(256), World VARCHER(48), X INTEGER, Y INTEGER, Z INTEGER, PRIMARY KEY(MailBoxName));";
    public static final String QUERY_GET_MAILBOX = "SELECT World, X, Y, Z FROM ChestMail_MailBox WHERE MailBoxName = ?";
    public static final String QUERY_GET_SIMILAR_MAILBOX_NAMES = "SELECT MailBoxName FROM ChestMail_MailBox WHERE MailBoxName LIKE ?;";
    public static final String QUERY_SET_MAILBOX = "INSERT INTO ChestMail_MailBox(MailBoxName, PlayerUUID, World, X, Y, Z) VALUES(?, ?, ?, ?, ?, ?);";
    public static final String QUERY_GET_MAILBOX_OWNER = "SELECT PlayerUUID FROM ChestMail_MailBox WHERE MailBoxName = ?;";
    public static final String QUERY_DELETE_MAILBOX = "DELETE FROM ChestMail_MailBox WHERE MailBoxName = ?;";
}
