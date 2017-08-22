package com.zamenwolk.spigot.commands;

import com.google.common.collect.Lists;
import com.zamenwolk.spigot.Animagi;
import com.zamenwolk.spigot.datas.House;
import com.zamenwolk.spigot.datas.PlayerProfile;
import com.zamenwolk.spigot.datas.ProfileCache;
import com.zamenwolk.spigot.datas.School;
import com.zamenwolk.spigot.helper.CmdParamUtils;
import com.zamenwolk.spigot.helper.IndexFinder;
import com.zamenwolk.spigot.helper.SubbedCommand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Martin on 18/07/2017.
 */
public class PointsCommand extends SubbedCommand
{
    private ProfileCache cache;
    private IndexFinder<String, School> schoolFinder;
    private Map<String, CommandExecutor> subCommands;
    private Animagi plugin;
    
    public PointsCommand(ProfileCache cache,
                         IndexFinder<String, School> schoolFinder,
                         Animagi plugin)
    {
        this.cache = cache;
        this.plugin = plugin;
        this.schoolFinder = schoolFinder;
    }
    
    @Override
    protected Map<String, CommandExecutor> subCommands()
    {
        if (subCommands == null)
        {
            subCommands = new HashMap<>();
            subCommands.put("list", new ListSub());
            subCommands.put("give", new GiveTakeSub(true));
            subCommands.put("take", new GiveTakeSub(false));
        }
        
        return subCommands;
    }
    
    private class ListSub implements CommandExecutor
    {
        @Override
        public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
        {
            if (args.length == 0)
            {
                sender.sendMessage(ChatColor.RED + "No school to list !" + ChatColor.RESET);
                return false;
            }
    
            School school = schoolFinder.find(CmdParamUtils.fromArg(args[0]), true);
    
            if (school == null)
            {
                sender.sendMessage(ChatColor.RED + "This school doesn't exist !" + ChatColor.RESET);
                return false;
            }
    
            List<House> houses = plugin.getHousesOfSchool(school);
    
            if (houses.size() == 0)
                sender.sendMessage(ChatColor.YELLOW + "No houses in this school !" + ChatColor.RESET);
            else
            {
                sender.sendMessage("Points of the houses of school " + ChatColor.AQUA + school.getName() + ChatColor.RESET + " : ");
        
                houses.forEach(h -> sender.sendMessage(ChatColor.GREEN + h.getName() + ChatColor.RESET + " : " + h.getPoints()));
            }
    
            return true;
        }
    }
    
    private class GiveTakeSub implements CommandExecutor
    {
        private boolean give;
    
        public GiveTakeSub(boolean give)
        {
            this.give = give;
        }
    
        @Override
        public boolean onCommand(CommandSender sender, Command command, String label, String[] argsArray)
        {
            PlayerProfile targetProfile;
            OfflinePlayer target;
            House         targetHouse;
            int           pointsChange;
            String        reason;
            List<String>  args = Lists.newArrayList(argsArray);
    
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
    
            String playerName = CmdParamUtils.fromArg(args.remove(0));
            target = Bukkit.getPlayer(playerName);
            if (target == null)
            {
                target = Bukkit.getOfflinePlayer(playerName);
                if (target == null)
                {
                    sender.sendMessage("This player doesn't exist !");
                    return false;
                }
                else
                {
                    sender.sendMessage("Player found but not currently connected ! That can cause unexpected behavior");
                }
            }
    
            reason = args.stream().reduce("", (a, b) -> a + " " + b);
    
            targetProfile = cache.getProfile(target.getUniqueId());
            if (targetProfile == null)
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
    
        private String generateBroadcast(House targetHouse, OfflinePlayer cause, int pointChange, boolean positive, String reason)
        {
            String res = "";
        
            res += targetHouse.getName();
            res += positive ? " gained " : " lost ";
            res += pointChange;
            res += " points ";
            res += positive ? "thanks to " : "because of ";
            res += cause.getName() + reason;
        
            return res;
        }
    }
}
