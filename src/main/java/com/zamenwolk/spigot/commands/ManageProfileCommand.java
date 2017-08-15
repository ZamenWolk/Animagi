/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.zamenwolk.spigot.commands;

import com.google.common.collect.Lists;
import com.zamenwolk.spigot.Animagi;
import com.zamenwolk.spigot.datas.*;
import com.zamenwolk.spigot.helper.CmdParamUtils;
import com.zamenwolk.spigot.helper.SubbedCommand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author: Martin
 * created on 15/08/2017.
 */
public class ManageProfileCommand extends SubbedCommand
{
    private Map<String, CommandExecutor> subCommands;
    
    @Override
    protected Map<String, CommandExecutor> subCommands()
    {
        if (subCommands == null)
        {
            subCommands = new HashMap<>();
            subCommands.put("enablequiz", new EnableQuiz());
            subCommands.put("create", new Create());
            subCommands.put("sethouse", new SetHouse());
            subCommands.put("resetsorting", new ResetSorting());
        }
    
        return subCommands;
    }
    
    private class EnableQuiz implements CommandExecutor
    {
        private ProfileCache cache;
        
        public EnableQuiz()
        {
            cache = ProfileCache.getInstance();
        }
        
        @Override
        public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
        {
            List<String>    arguments = Lists.newArrayList(args);
            Player          target;
            PlayerProfile   profile;
            QuizTakingState targetQuizState;
            
            if (arguments.size() == 0)
            {
                sender.sendMessage(ChatColor.RED + "No parameter given !" + ChatColor.RESET);
                return false;
            }
            
            target = Bukkit.getPlayer(CmdParamUtils.fromArg(arguments.remove(0)));
            
            if (target == null)
            {
                sender.sendMessage(ChatColor.RED + "This player doesn't exist or is not online." + ChatColor.RESET);
                return true;
            }
            
            profile = cache.getProfile(target.getUniqueId());
            if (profile == null)
                profile = cache.createProfile(new PlayerProfileData(target.getUniqueId(), target.getDisplayName()));
            
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
                profile.advanceQuiz(QuizCommand.getPrelimQuiz(), Animagi.getQuiz());
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
    
    private class Create implements CommandExecutor
    {
        private ProfileCache cache;
        
        public Create()
        {
            cache = ProfileCache.getInstance();
        }
        
        @Override
        public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
        {
            if (args.length != 1)
            {
                sender.sendMessage("Invalid number of arguments !");
                return false;
            }
            
            Player target = Bukkit.getPlayer(CmdParamUtils.fromArg(args[0]));
            
            if (target == null)
            {
                sender.sendMessage("Player isn't connected right now !");
                return true;
            }
            
            if (cache.getProfile(target.getUniqueId()) != null)
                sender.sendMessage("This player already has a profile !");
            else
            {
                sender.sendMessage("Creating the player profile !");
                cache.createProfile(new PlayerProfileData(target.getUniqueId(), target.getDisplayName()));
            }
            
            return true;
        }
    }
    
    private class SetHouse implements CommandExecutor
    {
        private ProfileCache cache;
        
        public SetHouse()
        {
            cache = ProfileCache.getInstance();
        }
        
        @Override
        public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
        {
            PlayerProfile profile;
            House         house;
            
            if (args.length != 2)
            {
                sender.sendMessage("Invalid number of arguments !");
                return false;
            }
            
            Player target = Bukkit.getPlayer(args[0]);
            
            if (target == null)
            {
                sender.sendMessage("Player isn't connected right now !");
                return true;
            }
            
            profile = cache.getProfile(target.getUniqueId());
            if (profile == null)
            {
                sender.sendMessage("This player has no profile yet !");
                return true;
            }
            
            house = Animagi.findHouse(CmdParamUtils.fromArg(args[1]));
            
            if (house == null)
            {
                sender.sendMessage("Unknown house");
                return true;
            }
            
            profile.setHouse(house);
            target.sendMessage("You have been sorted to house " + house.getName() + " by " + sender.getName());
            sender.sendMessage("Player sorted !");
            return true;
        }
    }
    
    private class ResetSorting implements CommandExecutor
    {
        private ProfileCache cache;
        
        public ResetSorting()
        {
            cache = ProfileCache.getInstance();
        }
        
        @Override
        public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
        {
            List<String>    arguments = Lists.newArrayList(args);
            Player          target;
            PlayerProfile   profile;
            
            if (arguments.size() == 0)
            {
                sender.sendMessage(ChatColor.RED + "No parameter given !" + ChatColor.RESET);
                return false;
            }
            
            target = Bukkit.getPlayer(CmdParamUtils.fromArg(arguments.remove(0)));
            
            if (target == null)
            {
                sender.sendMessage(ChatColor.RED + "This player doesn't exist or is not online." + ChatColor.RESET);
                return true;
            }
            
            profile = cache.getProfile(target.getUniqueId());
            
            if (profile == null)
            {
                sender.sendMessage("THis person doesn't have a profile yet");
                return true;
            }
            
            profile.unsetQuiz();
            sender.sendMessage("Quiz deactivated !");
            target.sendMessage("Your quiz and sorting have been reset by " + sender.getName());
            
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
}
