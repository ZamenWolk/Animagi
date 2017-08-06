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

package com.zamenwolk.spigot.datas;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Author: Martin
 * created on 30/07/2017.
 */
public class ProfileCache
{
    private Map<UUID, PlayerProfile> profileMap;
    
    private ProfileCache()
    {
        profileMap = new HashMap<>();
    }
    
    private static final ProfileCache INSTANCE = new ProfileCache();
    
    public static ProfileCache getInstance()
    {
        return INSTANCE;
    }
    
    public PlayerProfile getProfile(UUID playerID)
    {
        PlayerProfile profile = profileMap.getOrDefault(playerID, null);
        
        if (profile != null)
            return profile;
    
        try
        {
            profile = new PlayerProfile(playerID);
            profileMap.put(playerID, profile);
            return profile;
        }
        catch (FileNotFoundException e)
        {
            return null;
        }
    }
    
    public PlayerProfile createProfile(PlayerProfileData data)
    {
        if (profileMap.get(data.getPlayerID()) != null)
            return profileMap.get(data.getPlayerID());
    
        PlayerProfile profile = new PlayerProfile(data);
        profileMap.put(profile.getPlayerID(), profile);
        return profile;
    }
}
