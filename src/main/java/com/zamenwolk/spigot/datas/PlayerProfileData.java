package com.zamenwolk.spigot.datas;

import com.zamenwolk.spigot.Animagi;
import com.zamenwolk.spigot.helper.DataModel;
import org.apache.commons.lang3.tuple.Pair;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Created by Martin on 17/07/2017.
 */
public class PlayerProfileData extends DataModel implements Serializable
{
    private UUID   playerID;
    private String realName;
    private int    year;
    private House  house;
    private String role;
    
    public PlayerProfileData(UUID playerID, String realName, int year, House house, String role)
    {
        super();
        this.playerID = playerID;
        this.realName = realName;
        this.year = year;
        this.house = house;
        this.role = role;
    }
    
    public PlayerProfileData(UUID playerID, String realName)
    {
        this(playerID, realName, 0, null, "Nobody");
    }
    
    int getYear()
    {
        return year;
    }
    
    void setYear(int year)
    {
        if (year < 0 || year > 8)
            throw new IllegalArgumentException("Year should be between 0 and 8");
        
        this.year = year;
    }
    
    String getRole()
    {
        return role;
    }
    
    void setRole(String role)
    {
        if (role == null)
            throw new IllegalArgumentException("role is null");
        this.role = role;
    }
    
    UUID getPlayerID()
    {
        return playerID;
    }
    
    void setPlayerID(UUID playerID)
    {
        if (playerID == null)
            throw new IllegalArgumentException("playerID is null");
        this.playerID = playerID;
    }
    
    String getRealName()
    {
        return realName;
    }
    
    void setRealName(String realName)
    {
        this.realName = realName;
    }
    
    House getHouse()
    {
        return house;
    }
    
    void setHouse(House house)
    {
        this.house = house;
    }
    
    private void writeObject(ObjectOutputStream out) throws IOException
    {
        objectWriter(out);
    }
    
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException
    {
        objectReader(in);
    }
    
    @Override
    protected Map<String, Pair<Supplier<Object>, Consumer<Object>>> createDataModelIO()
    {
        Map<String, Pair<Supplier<Object>, Consumer<Object>>> modelIO = new HashMap<>();
        
        modelIO.put("playerID", Pair.of(this::getPlayerID,
                                        (Object o) ->
                                        {
                                            if (o != null && o instanceof UUID)
                                                setPlayerID((UUID) o);
                                        }));
        
        modelIO.put("year", Pair.of(this::getYear,
                                    (Object o) -> {
                                        if (o != null)
                                        {
                                            setYear((Integer) o);
                                        }
                                    }));
        
        modelIO.put("house", Pair.of(() -> house != null ? house.getName() : null,
                                     (Object o) -> {
                                         if (o != null && o instanceof String)
                                         {
                                             House currHouse = Animagi.getHouse((String) o);
                                             if (currHouse == null)
                                                 throw new IllegalArgumentException("House \"" + o + "\" non-existant");
                                         }
                                     }));
        
        modelIO.put("role", Pair.of(this::getRole,
                                    (Object o) -> {
                                        if (o != null && o instanceof String)
                                            setRole((String) o);
                                    }));
        
        modelIO.put("realName", Pair.of(this::getRealName,
                                        (Object o) -> {
                                            if (o != null && o instanceof String)
                                                setRealName((String) o);
                                        }));
        
        return modelIO;
    }
}
