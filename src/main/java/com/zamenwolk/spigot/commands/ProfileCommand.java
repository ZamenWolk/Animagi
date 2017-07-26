package com.zamenwolk.spigot.commands;

import com.google.common.collect.Lists;
import com.zamenwolk.spigot.datas.House;
import com.zamenwolk.spigot.datas.PlayerProfile;
import com.zamenwolk.spigot.helper.TargetCommandExecutor;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Martin on 18/07/2017.
 */
public class ProfileCommand extends TargetCommandExecutor
{
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        List<String> argList = Lists.newArrayList(args);
        Player target = getTarget(sender, argList);
        PlayerProfile profile;
        
        if (target == null)
            return false;
    
        try
        {
            profile = new PlayerProfile(target.getUniqueId());
        }
        catch (IOException | ClassNotFoundException e)
        {
            sender.sendMessage(ChatColor.RED.toString() + ChatColor.BOLD.toString() + "This player has no profile." + ChatColor.RESET.toString());
            return true;
        }
        catch (Exception e2)
        {
            sender.sendMessage(ChatColor.RED.toString() + ChatColor.BOLD.toString() + "Unknown error" + ChatColor.RESET.toString());
            e2.printStackTrace();
            return true;
        }
        
        List<String> mess = new LinkedList<>();
        mess.add(ChatColor.AQUA + "Player name : " + ChatColor.RESET + target.getName());
        
        House house = profile.getHouse();
        if (house != null)
            mess.add(ChatColor.AQUA +
                     "House " +
                     ChatColor.RESET +
                     house.getName() +
                     ChatColor.AQUA +
                     " of school " +
                     ChatColor.RESET  +
                     house.getSchool().getName());
        else
            mess.add(ChatColor.LIGHT_PURPLE + "Not sorted yet !" + ChatColor.RESET);
        
        int year = profile.getYear();
        if (year > 0 && year < 8)
            mess.add(ChatColor.AQUA + "Year : " + ChatColor.RESET + year);
        else if (year == 0)
            mess.add(ChatColor.LIGHT_PURPLE + "Not studying yet !" + ChatColor.RESET);
        else
            mess.add(ChatColor.LIGHT_PURPLE + "Graduated !" + ChatColor.RESET);
        
        String occupation = profile.getRole();
        
        if (occupation != null && !"".equals(occupation))
            mess.add(ChatColor.AQUA + "Occupation : " + ChatColor.RESET + occupation);
        else
            mess.add(ChatColor.LIGHT_PURPLE +"No occupation" + ChatColor.RESET);
        
        sender.sendMessage(mess.toArray(new String[0]));
        return true;
    }
}
