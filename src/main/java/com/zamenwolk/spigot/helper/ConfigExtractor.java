package com.zamenwolk.spigot.helper;

import org.bukkit.configuration.ConfigurationSection;

import java.io.ObjectInputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Martin on 20/07/2017.
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
}
