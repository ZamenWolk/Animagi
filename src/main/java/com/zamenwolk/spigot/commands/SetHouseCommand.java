/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.zamenwolk.spigot.commands;

import com.zamenwolk.spigot.Animagi;
import com.zamenwolk.spigot.datas.House;
import com.zamenwolk.spigot.datas.PlayerProfile;
import com.zamenwolk.spigot.helper.CmdParamUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.FileNotFoundException;

/**
 * Author: Martin
 * created on 31/07/2017.
 */
public class SetHouseCommand implements CommandExecutor
{
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
    
        try
        {
            profile = new PlayerProfile(target.getUniqueId());
        }
        catch (FileNotFoundException e)
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
