package com.zamenwolk.spigot.datas;

import com.zamenwolk.spigot.Animagi;
import com.zamenwolk.spigot.helper.ConfigExtractible;
import com.zamenwolk.spigot.helper.DataManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Map;

/**
 * Created by Martin on 11/07/2017.
 */
public class House extends DataManager<HouseData> implements ConfigExtractible
{
    private static final String houseFolder = "houses/";
    
    public House(File dataFolder, String houseFile) throws FileNotFoundException
    {
        super(new File(dataFolder, houseFolder + houseFile));
    }
    
    public House(File dataFolder, HouseData data)
    {
        super(data, new File(dataFolder, houseFolder + data.getName()));
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
    
    public Map<String, Double> getTraitsFactor()
    {
        return data.getTraitsFactors();
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
