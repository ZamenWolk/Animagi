package com.zamenwolk.spigot.datas;

import com.zamenwolk.spigot.Animagi;
import com.zamenwolk.spigot.helper.ConfigExtractible;
import com.zamenwolk.spigot.helper.DataModel;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.configuration.ConfigurationSection;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Created by Martin on 10/07/2017.
 */
public class HouseData extends DataModel implements Serializable, ConfigExtractible
{
    private String name;
    private School school;
    private int    points;
    private Map<String, Double> cf_traitsFactors;
    
    public HouseData(String house, School school, int points)
    {
        super();
        if (school == null)
            throw new IllegalArgumentException("School is null");
        
        name = house;
        this.school = school;
        this.points = points;
        
        cf_traitsFactors = new HashMap<>();
    }
    
    String getName()
    {
        return name;
    }
    
    void setName(String name)
    {
        if (name != null && !"".equals(name))
            this.name = name;
    }
    
    School getSchool()
    {
        return school;
    }
    
    int getPoints()
    {
        return points;
    }
    
    void setPoints(int points)
    {
        this.points = points;
    }
    
    private void writeObject(ObjectOutputStream out) throws IOException
    {
        objectWriter(out);
    }
    
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException
    {
        objectReader(in);
        cf_traitsFactors = new HashMap<>();
    }
    
    @Override
    public void getFromConfig(Object config)
    {
        if (config == null)
            throw new IllegalArgumentException("config object is null");
        ConfigurationSection actConf = (ConfigurationSection) config;
        
        ConfigurationSection section = actConf.getConfigurationSection("traits");
        if (section == null)
            throw new IllegalArgumentException("config object doesn't have traits");
        
        Map<String, Object> traits = section.getValues(false);
    
        for (Map.Entry<String, Object> currTrait : traits.entrySet())
        {
            try
            {
                Double notIndexedFactor = Double.valueOf(currTrait.getValue().toString());
                
                if (notIndexedFactor != 0)
                    cf_traitsFactors.put(currTrait.getKey().toLowerCase(), notIndexedFactor);
            }
            catch (NumberFormatException e)
            {
                throw new IllegalArgumentException("Value of " + currTrait.getKey() + " is not a number");
            }
        }
        
        if (cf_traitsFactors.size() == 0)
            throw new IllegalArgumentException("No valid traits found");
    }
    
    @Override
    protected Map<String, Pair<Supplier<Object>, Consumer<Object>>> createDataModelIO()
    {
        Map<String, Pair<Supplier<Object>, Consumer<Object>>> dataModelIO = new HashMap<>();
        
        dataModelIO.put("name", Pair.of(this::getName,
                                        (Object o) ->
                                        {
                                            if (o != null && o instanceof String)
                                                setName((String) o);
                                        }));
        
        dataModelIO.put("school", Pair.of(() -> this.getSchool().getName(),
                                          (Object o) ->
                                          {
                                              if (o != null && o instanceof String)
                                                  school = Animagi.getSchool((String) o);
                                              if (school == null)
                                                  throw new IllegalArgumentException("School \"" + o + "\" non-existant");
                                          }));
        
        dataModelIO.put("points", Pair.of(this::getPoints,
                                          (Object o) -> {
                                              if (o != null)
                                                  setPoints((Integer) o);
                                          }));
        
        return dataModelIO;
    }
    
    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        
        HouseData houseData = (HouseData) o;
        
        if (points != houseData.points) return false;
        if (name != null ? !name.equals(houseData.name) : houseData.name != null) return false;
        return school != null ? school.equals(houseData.school) : houseData.school == null;
    }
    
    @Override
    public int hashCode()
    {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (school != null ? school.hashCode() : 0);
        result = 31 * result + points;
        return result;
    }
    
    @Override
    public String toString()
    {
        return "HouseData{" +
               "name='" + name + '\'' +
               ", school=" + school +
               ", points=" + points +
               '}';
    }
}
