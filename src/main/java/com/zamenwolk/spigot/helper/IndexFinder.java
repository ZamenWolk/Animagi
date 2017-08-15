/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.zamenwolk.spigot.helper;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * Author: Martin
 * created on 03/08/2017.
 */
public class IndexFinder <Key, Data>
{
    private Map<Key, Data> index;
    private Map<Key, Key> aliases;
    
    public IndexFinder(Map<Key, Data> index, Map<Key, Key> aliases)
    {
        this.index = index != null ? index : new HashMap<>();
        this.aliases = aliases != null ? aliases : new HashMap<>();
    }
    
    public Set<Key> getKeys()
    {
        return index.keySet();
    }
    
    public Data find(Key key, boolean allowAliases)
    {
        Data res = findInternal(key);
        
        if (res != null || !allowAliases)
            return res;
    
        if (aliases.get(key) != null)
            return findInternal(aliases.get(key));
        
        return null;
    }
    
    
    private Data findInternal(Key key)
    {
        if (key instanceof String)
            return findInternalString((String) key);
        
        return index.get(key);
    }
    
    private Data findInternalString(String key)
    {
        Optional<Map.Entry<Key, Data>> res = index.entrySet()
                                                  .stream()
                                                  .filter(e -> ((String)e.getKey()).equalsIgnoreCase(key))
                                                  .findFirst();
        
        return res.isPresent() ? res.get().getValue() : null;
    }
}
