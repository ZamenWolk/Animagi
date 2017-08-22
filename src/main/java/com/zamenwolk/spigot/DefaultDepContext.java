/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.zamenwolk.spigot;

import com.zamenwolk.spigot.datas.House;
import com.zamenwolk.spigot.datas.School;
import com.zamenwolk.spigot.dependencyInjection.DepContext;
import com.zamenwolk.spigot.helper.IndexFinder;
import org.bukkit.Bukkit;

import java.io.File;

/**
 * Author: Martin
 * created on 17/08/2017.
 */
public class DefaultDepContext extends DepContext
{
    static void setContext(Animagi plugin)
    {
        DepContext.context = new DefaultDepContext(plugin);
    }
    
    private Animagi animagiPlugin;
    
    private DefaultDepContext(Animagi plugin)
    {
        animagiPlugin = plugin;
    }
    
    @Override
    protected IndexFinder<String, House> houseFinder()
    {
        return animagiPlugin.getHouseFinder();
    }
    
    @Override
    protected IndexFinder<String, School> schoolFinder()
    {
        return animagiPlugin.getSchoolFinder();
    }
    
    @Override
    protected File dataFolder()
    {
        return animagiPlugin.getPluginDataFolder();
    }
}
