package com.zamenwolk.spigot.datas;

import com.zamenwolk.spigot.Animagi;
import com.zamenwolk.spigot.helper.ConfigExtractible;
import com.zamenwolk.spigot.helper.DataManager;

import java.io.File;
import java.io.IOException;

/**
 * Created by Martin on 11/07/2017.
 */
public class House extends DataManager<HouseData> implements ConfigExtractible
{
    private static final String houseFolder = "houses/";
    
    public House(String houseFile) throws IOException, ClassNotFoundException
    {
        super(new File(Animagi.dataFolder(), houseFolder + houseFile));
    }
    
    public House(HouseData data, String houseFile) throws IOException
    {
        super(data, new File(Animagi.dataFolder(), houseFolder + houseFile));
    }
    
    public String getName()
    {
        return data.getName();
    }
    
    public School getSchool()
    {
        return data.getSchool();
    }
    
    public int getPoints()
    {
        return data.getPoints();
    }
    
    public void setPoints(int points)
    {
        int prev = getPoints();
        if (prev != points)
        {
            data.setPoints(points);
            saveChanges();
        }
    }
    
    @Override
    public String toString()
    {
        return "House{" +
               "data=" + data +
               '}';
    }
    
    @Override
    public void getFromConfig(Object config)
    {
        data.getFromConfig(config);
    }
}
