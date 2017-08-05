package com.zamenwolk.spigot.helper;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * Author: Martin
 * created on 20/07/2017.
 */
public class ConfigExtractor
{
    private ConfigExtractor() {}
    
    public static <Data extends ConfigExtractible> Data createObject(Class<Data> dataClass, Object configObject)
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
    
    public static <Data extends ConfigExtractible> List<Data> createList(Class<Data> dataClass, List<Object> configObject)
    throws InvocationTargetException
    {
        List<Data> ret = new ArrayList<>();
        
        for (Object currObj : configObject)
        {
            ret.add(createObject(dataClass, currObj));
        }
        
        return ret;
    }
    
    public static <Key, Data extends ConfigExtractible> Map<Key, Data> createMap(Class<Data> dataClass, Map<Key, Object> configObject)
    throws InvocationTargetException
    {
        Map<Key, Data> ret = new HashMap<>();
        
        for (Map.Entry<Key, Object> entry : configObject.entrySet())
        {
            ret.put(entry.getKey(), createObject(dataClass, entry.getValue()));
        }
        
        return ret;
    }
    
    public static <Data extends ConfigExtractible> void setObject(Data object, Object configObject)
    {
        object.getFromConfig(configObject);
    }
    
    public static <Data extends ConfigExtractible> void setList(List<Data> dataList, List<Object> configObject)
    {
        Iterator<Data> dataIterator = dataList.iterator();
        Iterator<Object> configIterator = configObject.iterator();
        
        while (dataIterator.hasNext() && configIterator.hasNext())
        {
            Data currData = dataIterator.next();
            Object currConfig = configIterator.next();
            
            setObject(currData, currConfig);
        }
    }
    
    public static <Key, Data extends ConfigExtractible> void setMap(Map<Key, Data> dataMap, Map<Key, Object> configObject)
    {
        for (Map.Entry<Key, Data> d : dataMap.entrySet())
        {
            if (configObject.containsKey(d.getKey()))
                setObject(d.getValue(), configObject.get(d.getKey()));
        }
    }
}
