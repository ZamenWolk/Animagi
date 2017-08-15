/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.zamenwolk.spigot.helper;

import com.google.common.collect.Lists;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.List;
import java.util.Map;

/**
 * Author: Martin
 * created on 15/08/2017.
 */
public abstract class SubbedCommand implements CommandExecutor
{
    @Override
    public final boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        List<String> arguments = Lists.newArrayList(args);
        
        if (arguments.size() == 0)
        {
            noArgsError(sender, command, label);
            return false;
        }
        
        String subCommand = arguments.remove(0);
        
        for (Map.Entry<String, CommandExecutor> e : subCommands().entrySet())
        {
            if (e.getKey().equalsIgnoreCase(subCommand))
                return e.getValue().onCommand(sender, command, label, arguments.toArray(new String[]{}));
        }
        
        return defaultBehavior() != null ? defaultBehavior().onCommand(sender, command, label, arguments.toArray(new String[]{})) : false;
    }
    
    protected abstract Map<String, CommandExecutor> subCommands();
    
    protected CommandExecutor defaultBehavior()
    {
        return null;
    }
    
    protected void noArgsError(CommandSender sender, Command command, String label)
    {}
}
