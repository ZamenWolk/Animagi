package com.zamenwolk.spigot.datas;

import com.zamenwolk.spigot.Animagi;
import com.zamenwolk.spigot.helper.DataManager;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * Created by Martin on 15/07/2017.
 */
public class School extends DataManager<SchoolData>
{
    private static final String schoolFolder = "schools/";
    
    public School(String schoolFile) throws FileNotFoundException
    {
        super(new File(Animagi.dataFolder(), schoolFolder + schoolFile));
    }
    
    public School(SchoolData data)
    {
        super(data, new File(Animagi.dataFolder(), schoolFolder + data.getName()));
    }
    
    public String getName()
    {
        return data.getName();
    }
    
    @Override
    public String toString()
    {
        return "School{" +
               "data=" + data +
               '}';
    }
    
    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        
        School school = (School) o;
        
        return data.equals(school.data);
    }
    
    @Override
    public int hashCode()
    {
        return data.hashCode();
    }
}
