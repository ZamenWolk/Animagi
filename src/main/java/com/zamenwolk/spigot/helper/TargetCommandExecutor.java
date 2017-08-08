package com.zamenwolk.spigot.helper;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * Created by Martin on 18/07/2017.
 */
public abstract class TargetCommandExecutor implements CommandExecutor
{
    protected Player getTarget(CommandSender commandSender, List<String> arguments)
    {
        if (arguments.size() >= 1)
        {
            Player player = Bukkit.getPlayer(CmdParamUtils.fromArg(arguments.get(0)));
            if (player != null)
            {
                arguments.remove(0);
                return player;
            }
        }
        
        if (commandSender instanceof Player)
            return (Player) commandSender;
        
        return null;
    }
}
