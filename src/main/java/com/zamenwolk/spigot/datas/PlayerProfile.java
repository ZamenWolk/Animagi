package com.zamenwolk.spigot.datas;

import com.zamenwolk.spigot.Animagi;
import com.zamenwolk.spigot.helper.DataManager;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * Created by Martin on 17/07/2017.
 */
public class PlayerProfile extends DataManager<PlayerProfileData>
{
    private static final String profileFolder = "profiles/";
    
    public PlayerProfile(UUID playerUuid) throws IOException, ClassNotFoundException
    {
        super(new File(Animagi.dataFolder(), profileFolder + playerUuid.toString()));
    }
    
    public PlayerProfile(PlayerProfileData data, UUID playerUuid) throws IOException
    {
        super(data, new File(Animagi.dataFolder(), profileFolder + playerUuid.toString()));
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
