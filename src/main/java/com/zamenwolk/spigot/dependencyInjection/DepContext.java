/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.zamenwolk.spigot.dependencyInjection;

import com.zamenwolk.spigot.datas.House;
import com.zamenwolk.spigot.datas.School;
import com.zamenwolk.spigot.helper.IndexFinder;

import java.io.File;

/**
 * Author: Martin
 * created on 17/08/2017.
 */
public abstract class DepContext
{
    protected static DepContext context;
    
    public static IndexFinder<String, House> getHouseFinder()
    {
        return context.houseFinder();
    }
    
    public static IndexFinder<String, School> getSchoolFinder()
    {
        return context.schoolFinder();
    }
    
    public static File getDataFolder()
    {
        return context.dataFolder();
    }
    
    protected DepContext()
    {
    
    }
    
    protected abstract IndexFinder<String, House> houseFinder();
    
    protected abstract IndexFinder<String, School> schoolFinder();
    
    protected abstract File dataFolder();
}
