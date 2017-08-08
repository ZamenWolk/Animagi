package com.zamenwolk.spigot.datas;

import com.zamenwolk.spigot.Animagi;
import com.zamenwolk.spigot.helper.DataManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
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
    
    public String getRealName()
    {
        return data.getRealName();
    }
    
    public void setRealName(String realName)
    {
        data.setRealName(realName);
        
        saveChanges();
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
    
    public void setQuizToMain(int quizHash)
    {
        data.setQuizState(QuizTakingState.TAKING_QUIZ);
        data.setQuizHash(quizHash);
    
        if (data.getAnswersList().size() != 0)
            data.resetAnswersList();
    
        saveChanges();
    }
    
    public void setQuizToHousePicking()
    {
        data.setQuizState(QuizTakingState.SCHOOL_PICKING);
        
        saveChanges();
    }
    
    public void setQuizToTaken()
    {
        data.setQuizState(QuizTakingState.QUIZ_TAKEN);
        
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
    
    public int getPrelimQuestionsHash()
    {
        return data.getPrelimQuestionsHash();
    }
    
    public List<String> getAnswersList()
    {
        return data.getAnswersList();
    }
    
    public void addAnswer(String answer)
    {
        data.addAnswer(answer);
    }
    
    @Override
    public String toString()
    {
        return "PlayerProfile{" +
               "data=" + data +
               '}';
    }
}
