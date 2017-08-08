/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.zamenwolk.spigot.datas;

import com.zamenwolk.spigot.helper.ConfigExtractible;

import java.util.HashMap;
import java.util.Map;

/**
 * Author: Martin
 * created on 26/07/2017.
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
    public void getFromConfig(Object config)
    {
        Map<String, Object> actConf = (Map<String, Object>) config;
        responseText = (String) actConf.getOrDefault("text", null);
        if (responseText == null)
            throw new IllegalArgumentException("No answer given");
        
        for (Map.Entry<String, Object> e : ((Map<String, Object>)actConf.get("traitGain")).entrySet())
        {
            try
            {
                Double notIndexedFactor = Double.valueOf(e.getValue().toString());
        
                if (notIndexedFactor != 0)
                    traitsChange.put(e.getKey(), notIndexedFactor);
            }
            catch (NumberFormatException err)
            {
                throw new IllegalArgumentException("Value of " + e.getKey() + " is not a number");
            }
        }
    
        if (traitsChange.size() == 0)
            throw new IllegalArgumentException("No valid traits found");
    }
    
    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        
        Response response = (Response) o;
        
        if (!responseText.equals(response.responseText)) return false;
        return traitsChange.equals(response.traitsChange);
    }
    
    @Override
    public int hashCode()
    {
        int result = responseText.hashCode();
        result = 31 * result + traitsChange.hashCode();
        return result;
    }
    
    public Map<String, Double> getTraitsChange()
    {
        return traitsChange;
    }
}
