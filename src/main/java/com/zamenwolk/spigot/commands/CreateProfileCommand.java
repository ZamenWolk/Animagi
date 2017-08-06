/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.zamenwolk.spigot.commands;

import com.zamenwolk.spigot.datas.PlayerProfileData;
import com.zamenwolk.spigot.datas.ProfileCache;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Author: Martin
 * created on 31/07/2017.
 */
public class CreateProfileCommand implements CommandExecutor
{
    private ProfileCache cache;
    
    public CreateProfileCommand()
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
    
        Player target = Bukkit.getPlayer(args[0]);
    
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
