package com.zamenwolk.spigot.commands;

import com.google.common.collect.Lists;
import com.zamenwolk.spigot.Animagi;
import com.zamenwolk.spigot.datas.House;
import com.zamenwolk.spigot.datas.PlayerProfile;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.List;

/**
 * Created by Martin on 18/07/2017.
 */
public class PointsCommand implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        List<String> arguments = Lists.newArrayList(args);
        
        if (arguments.size() == 0)
        {
            sender.sendMessage(ChatColor.RED + "No parameter given !" + ChatColor.RESET);
            return false;
        }
        
        String subCommand = arguments.remove(0);
        
        if (subCommand.equalsIgnoreCase("list"))
            return listSubCommand(sender, command, label, arguments);
        if (subCommand.equalsIgnoreCase("give") || subCommand.equalsIgnoreCase("take"))
            return giveTakeSubCommand(sender, command, label, arguments, subCommand.equalsIgnoreCase("give"));
    
        sender.sendMessage(ChatColor.RED + "Only primary parameters accepted : list, give, take" + ChatColor.RESET);
        return false;
    }
    
    public boolean listSubCommand(CommandSender sender, Command command, String label, List<String> args)
    {
        if (args.size() == 0)
        {
            sender.sendMessage(ChatColor.RED + "No school to list !" + ChatColor.RESET);
            return false;
        }
        
        String schoolName = Animagi.checkSchool(args.get(0));
        
        if (schoolName == null)
        {
            sender.sendMessage(ChatColor.RED + "This school doesn't exist !" + ChatColor.RESET);
            return false;
        }
        
        List<House> houses = Animagi.getHousesOfSchool(schoolName);
        
        if (houses.size() == 0)
            sender.sendMessage(ChatColor.YELLOW + "No houses in this school !" + ChatColor.RESET);
        else
        {
            sender.sendMessage("Points of the houses of school " + ChatColor.AQUA + schoolName + ChatColor.RESET + " : ");
    
            houses.forEach((House h) -> {
                sender.sendMessage(ChatColor.GREEN + h.getName() + ChatColor.RESET + " : " + h.getPoints());
            });
        }
        
        return true;
    }
    
    public boolean giveTakeSubCommand(CommandSender sender, Command command, String label, List<String> args, boolean give)
    {
        PlayerProfile targetProfile;
        Player target;
        House targetHouse;
        int pointsChange;
        String reason;
        
        if (args.size() < 3)
        {
            sender.sendMessage("Not enough arguments !");
            return false;
        }
    
        try
        {
            pointsChange = Integer.parseInt(args.remove(0));
        }
        catch (NumberFormatException e)
        {
            sender.sendMessage("The point change is not a valid number.");
            return false;
        }
        
        target = Bukkit.getPlayer(args.remove(0));
        
        if (target == null)
        {
            sender.sendMessage("This player doesn't exist !");
            return false;
        }
        
        reason = args.stream().reduce("", (String a, String b) -> a + b);
        
        try
        {
            targetProfile = new PlayerProfile(target.getUniqueId());
        }
        catch (IOException | ClassNotFoundException e)
        {
            sender.sendMessage("This player has no profile.");
            return true;
        }
        
        targetHouse = targetProfile.getHouse();
        
        if (targetHouse == null)
        {
            sender.sendMessage("This player is not sorted yet.");
            return true;
        }
        
        int housePoints = targetHouse.getPoints();
        
        housePoints = give ? housePoints + pointsChange : housePoints - pointsChange;
        
        targetHouse.setPoints(housePoints);
    
        Bukkit.getServer().broadcastMessage(generateBroadcast(targetHouse, target, pointsChange, give, reason));
        return true;
    }
    
    private String generateBroadcast(House targetHouse, Player cause, int pointChange, boolean positive, String reason)
    {
        String res = "";
        
        res += targetHouse.getName();
        res += positive ? " gained " : " lost ";
        res += pointChange;
        res += " points ";
        res += positive ? " thanks to " : " because of ";
        res += cause.getName();
        res += " " + reason;
        
        return res;
    }
}
