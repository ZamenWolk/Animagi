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
    
    public PlayerProfile(File dataFolder, UUID playerUuid) throws FileNotFoundException
    {
        super(new File(dataFolder, profileFolder + playerUuid.toString()));
    }
    
    public PlayerProfile(File dataFolder, PlayerProfileData data)
    {
        super(data, new File(dataFolder, profileFolder + data.getPlayerID().toString()));
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
    
    public void advanceQuiz(List<String> prelimQuiz, List<Question> quiz)
    {
        QuizTakingState currState = getQuizState();
        
        switch (currState)
        {
        case NOT_TAKING_QUIZ:
            data.setPrelimQuestionsHash(prelimQuiz.hashCode());
            if (prelimQuiz.size() > 0)
            {
                data.setQuizState(QuizTakingState.PRELIMINARY_QUESTIONS);
                break;
            }
            
        case PRELIMINARY_QUESTIONS:
            data.setQuizHash(quiz.hashCode());
            data.setQuizState(QuizTakingState.TAKING_QUIZ);
            data.resetAnswersList();
            break;
            
        case TAKING_QUIZ:
            data.setQuizState(QuizTakingState.SCHOOL_PICKING);
            break;
            
        case SCHOOL_PICKING:
            data.setQuizState(QuizTakingState.QUIZ_TAKEN);
        }
        
        saveChanges();
    }
    
    public void unsetQuiz()
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
