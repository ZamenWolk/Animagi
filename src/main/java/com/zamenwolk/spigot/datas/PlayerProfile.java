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
    
    public void setQuizToPreliminary(int preliminaryHash)
    {
        data.setQuizState(QuizTakingState.PRELIMINARY_QUESTIONS);
        data.setPrelimQuestionsHash(preliminaryHash);
        saveChanges();
    }
    
    public void resetQuiz()
    {
        data.setQuizState(QuizTakingState.NOT_TAKING_QUIZ);
        data.setPrelimQuestionsHash(0);
        data.setQuizHash(0);
        data.setHouse(null);
        saveChanges();
    }
    
    public boolean isSorted()
    {
        if (data.getHouse() != null)
        {
            if (data.getQuizState() != QuizTakingState.QUIZ_TAKEN)
                data.setQuizState(QuizTakingState.QUIZ_TAKEN);
            return true;
        }
        
        return false;
    }
    
    public boolean isTakingQuiz()
    {
        return (!isSorted() && data.getQuizState() != QuizTakingState.NOT_TAKING_QUIZ);
    }
    
    public int getQuizHash()
    {
        return data.getQuizHash();
    }
    
    public void setQuizHash(int hash)
    {
        data.setQuizHash(hash);
        saveChanges();
    }
    
    public int getPrelimQuestionsHash()
    {
        return data.getPrelimQuestionsHash();
    }
    
    public void setPrelimQuestionsHash(int hash)
    {
        data.setPrelimQuestionsHash(hash);
        saveChanges();
    }
    
    @Override
    public String toString()
    {
        return "PlayerProfile{" +
               "data=" + data +
               '}';
    }
}
