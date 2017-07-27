package com.zamenwolk.spigot.helper;

import org.bukkit.configuration.ConfigurationSection;

import javax.xml.crypto.Data;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * Author: Martin
 * created on 20/07/2017.
 */
public class ConfigExtractor
{
    private ConfigExtractor() {}
    
    public static <Data extends ConfigExtractible> Data createObject(Class<Data> dataClass, ConfigurationSection configObject)
    throws InvocationTargetException
    {
        try
        {
            Data object = dataClass.getConstructor().newInstance();
            object.getFromConfig(configObject);
            return object;
        }
        catch (NoSuchMethodException e)
        {
            throw new ClassCastException("This class doesn't have a ConfigurationSection constructor");
        }
        catch (IllegalAccessException e)
        {
            throw new ClassCastException("The class's default constructor is not public");
        }
        catch (InstantiationException e)
        {
            throw new ClassCastException("This class can't be instanciated");
        }
    }
    
    public static <Data extends ConfigExtractible> List<Data> createList(Class<Data> dataClass, List<ConfigurationSection> configObject)
    throws InvocationTargetException
    {
        List<Data> ret = new LinkedList<>();
        
        for (ConfigurationSection currObj : configObject)
        {
            ret.add(createObject(dataClass, currObj));
        }
        
        return ret;
    }
    
    public static <Key, Data extends ConfigExtractible> Map<Key, Data> createMap(Class<Data> dataClass, Map<Key, ConfigurationSection> configObject)
    throws InvocationTargetException
    {
        Map<Key, Data> ret = new HashMap<>();
        
        for (Map.Entry<Key, ConfigurationSection> entry : configObject.entrySet())
        {
            ret.put(entry.getKey(), createObject(dataClass, entry.getValue()));
        }
        
        return ret;
    }
    
    public static <Data extends ConfigExtractible> void setObject(Data object, ConfigurationSection configObject)
    {
        object.getFromConfig(configObject);
    }
    
    public static <Data extends ConfigExtractible> void setList(List<Data> dataList, List<ConfigurationSection> configObject)
    {
        Iterator<Data> dataIterator = dataList.iterator();
        Iterator<ConfigurationSection> configIterator = configObject.iterator();
        
        while (dataIterator.hasNext() && configIterator.hasNext())
        {
            Data currData = dataIterator.next();
            ConfigurationSection currConfig = configIterator.next();
            
            setObject(currData, currConfig);
        }
    }
    
    public static <Key, Data extends ConfigExtractible> void setMap(Map<Key, Data> dataMap, Map<Key, ConfigurationSection> configObject)
    {
        for (Map.Entry<Key, Data> d : dataMap.entrySet())
        {
            if (configObject.containsKey(d.getKey()))
                setObject(d.getValue(), configObject.get(d.getKey()));
        }
    }
}
