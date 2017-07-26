/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.zamenwolk.spigot.datas;

import com.zamenwolk.spigot.helper.ConfigExtractible;
import org.bukkit.configuration.ConfigurationSection;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Martin on 26/07/2017.
 */
public class Response implements ConfigExtractible
{
    private String responseText;
    private Map<String, Double> traitsChange;
    
    public Response()
    {
        responseText = "";
        traitsChange = new HashMap<>();
    }
    
    @Override
    public void getFromConfig(ConfigurationSection config)
    {
    
    }
}
