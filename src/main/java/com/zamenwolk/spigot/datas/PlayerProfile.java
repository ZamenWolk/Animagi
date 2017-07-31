package com.zamenwolk.spigot.datas;

import com.zamenwolk.spigot.Animagi;
import com.zamenwolk.spigot.helper.DataManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.UUID;

/**
 * Created by Martin on 17/07/2017.
 */
public class PlayerProfile extends DataManager<PlayerProfileData>
{
    private static final String profileFolder = "profiles/";
    
    public PlayerProfile(UUID playerUuid) throws FileNotFoundException
    {
        super(new File(Animagi.dataFolder(), profileFolder + playerUuid.toString()));
    }
    
    public PlayerProfile(PlayerProfileData data)
    {
        super(data, new File(Animagi.dataFolder(), profileFolder + data.getPlayerID().toString()));
    }
    
    public int getYear()
    {
        return data.getYear();
    }
    
    public String getRole()
    {
        return data.getRole();
    }
    
    public UUID getPlayerID()
    {
        return data.getPlayerID();
    }
    
    public House getHouse()
    {
        return data.getHouse();
    }
    
    public void setHouse(House house)
    {
        data.setHouse(house);
        saveChanges();
    }
    
    public QuizTakingState getQuizState()
    {
        return data.getQuizState();
    }
    
    public void setQuizState(QuizTakingState state)
    {
        data.setQuizState(state);
        saveChanges();
    }
}
