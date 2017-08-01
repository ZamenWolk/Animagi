/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.zamenwolk.spigot.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.io.IOException;
import java.net.URL;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

/**
 * Author: Martin
 * created on 01/08/2017.
 */
public class VersionCommand implements CommandExecutor
{
    private String pluginName;
    private String version;
    
    public VersionCommand(String pluginName, String version)
    {
        this.pluginName = pluginName;
        this.version = version;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        try
        {
            Class clazz = getClass();
            String className = clazz.getSimpleName() + ".class";
            String classPath = clazz.getResource(className).toString();
            String manifestPath = classPath.substring(0, classPath.lastIndexOf("!") + 1) +
                              "/META-INF/MANIFEST.MF";
            Manifest   manifest = new Manifest(new URL(manifestPath).openStream());
            Attributes attr  = manifest.getMainAttributes();
    
            String buildTime = attr.getValue("Build-Time-String");
            sender.sendMessage(pluginName + " plugin version \"" + version + "\", built on " + buildTime);
        }
        catch (IOException e)
        {
            sender.sendMessage("Error while fetching version");
        }
        
        return true;
    }
}
