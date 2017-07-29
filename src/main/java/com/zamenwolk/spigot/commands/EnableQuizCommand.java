/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.zamenwolk.spigot.commands;

import com.google.common.collect.Lists;
import com.zamenwolk.spigot.datas.PlayerProfile;
import com.zamenwolk.spigot.datas.PlayerProfileData;
import com.zamenwolk.spigot.datas.QuizTakingState;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.*;

/**
 * Author: Martin
 * created on 29/07/2017.
 */
public class EnableQuizCommand implements CommandExecutor
{
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        List<String> arguments = Lists.newArrayList(args);
        Player target;
        PlayerProfile profile;
        QuizTakingState targetQuizState;
        
    
        if (arguments.size() == 0)
        {
            sender.sendMessage(ChatColor.RED + "No parameter given !" + ChatColor.RESET);
            return false;
        }
        
        target = Bukkit.getPlayer(arguments.remove(0));
        
        if (target == null)
        {
            sender.sendMessage(ChatColor.RED + "This player doesn't exist or is not online." + ChatColor.RESET);
            return true;
        }
    
        try
        {
            profile = new PlayerProfile(target.getUniqueId());
        }
        catch (IOException e)
        {
            try
            {
                profile = new PlayerProfile(new PlayerProfileData(target.getUniqueId(), target.getName()), target.getUniqueId());
            }
            catch (IOException e1)
            {
                sender.sendMessage("Unknown error");
                e.printStackTrace();
                return true;
            }
        }
        catch (ClassNotFoundException e)
        {
            sender.sendMessage("Unknown error");
            e.printStackTrace();
            return true;
        }
        
        if (profile.getHouse() != null)
        {
            sender.sendMessage("This player is already in a house !");
            return true;
        }
        
        targetQuizState = profile.getQuizState();
        
        if (targetQuizState != QuizTakingState.NOT_TAKING_QUIZ)
        {
            sender.sendMessage("This player is already taking the test !");
            notifyPlayer(target, false);
        }
        else
        {
            sender.sendMessage("Enabling quiz for this player !");
            profile.setQuizState(QuizTakingState.PRELIMINARY_QUESTIONS);
            notifyPlayer(target, true);
        }
        
        return true;
    }
    
    private void notifyPlayer(Player target, boolean justActivated)
    {
        if (justActivated)
        {
            target.sendMessage("You can now take the quiz to integrate one of our wizarding schools, and be sorted in one of its houses ! To continue, use the command \"/quiz\"");
        }
        else
        {
            target.sendMessage("Take the quiz now to integrate one of our wizarding schools, and be sorted in one of its houses ! To continue, use the command \"");
        }
    }
}
