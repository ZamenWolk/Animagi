package com.zamenwolk.spigot.datas;

import com.zamenwolk.spigot.Animagi;
import com.zamenwolk.spigot.datas.SchoolData;
import com.zamenwolk.spigot.helper.DataManager;

import java.io.File;
import java.io.IOException;

/**
 * Created by Martin on 15/07/2017.
 */
public class School extends DataManager<SchoolData>
{
    private static final String schoolFolder = "schools/";
    
    public School(String schoolFile) throws IOException, ClassNotFoundException
    {
        super(new File(Animagi.dataFolder(), schoolFolder + schoolFile));
    }
    
    public School(SchoolData data, String schoolFile) throws IOException
    {
        super(data, new File(Animagi.dataFolder(), schoolFolder + schoolFile));
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
}
