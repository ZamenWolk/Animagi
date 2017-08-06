/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.zamenwolk.spigot.datas;

import com.zamenwolk.spigot.Animagi;
import com.zamenwolk.spigot.helper.DataModel;
import org.apache.commons.lang3.tuple.Pair;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Created by Martin on 17/07/2017.
 */
public class PlayerProfileData extends DataModel implements Serializable
{
    private static final long serialVersionUID = 7137425549750149760L;
    
    private UUID            playerID;
    private String          realName;
    private int             year;
    private House           house;
    private String          role;
    
    private QuizTakingState quizState;
    private List<String>    answersList;
    private int             quizHash;
    private int             prelimQuestionsHash;
    
    public PlayerProfileData(UUID playerID,
                             String realName,
                             int year,
                             House house,
                             String role,
                             QuizTakingState quizState,
                             List<String> answersList,
                             int quizHash,
                             int prelimQuestionsHash)
    {
        super();
        this.playerID = playerID;
        this.realName = realName;
        this.year = year;
        this.house = house;
        this.role = role;
        this.quizState = quizState;
        this.answersList = answersList;
        this.quizHash = quizHash;
        this.prelimQuestionsHash = prelimQuestionsHash;
    }
    
    public PlayerProfileData(UUID playerID, String realName)
    {
        this(playerID, realName, 0, null, "Nobody", QuizTakingState.NOT_TAKING_QUIZ, new LinkedList<>(), 0, 0);
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
    
    QuizTakingState getQuizState()
    {
        return quizState;
    }
    
    void setQuizState(QuizTakingState takingQuiz)
    {
        quizState = takingQuiz;
    }
    
    List<String> getAnswersList()
    {
        return new LinkedList<>(answersList);
    }
    
    private void setAnswersList(List<String> answersList)
    {
        this.answersList = answersList;
    }
    
    void addAnswer(String answer)
    {
        answersList.add(answer);
    }
    
    void resetAnswersList()
    {
        answersList = new LinkedList<>();
    }
    
    int getQuizHash()
    {
        return quizHash;
    }
    
    void setQuizHash(int quizHash)
    {
        this.quizHash = quizHash;
    }
    
    int getPrelimQuestionsHash()
    {
        return prelimQuestionsHash;
    }
    
    void setPrelimQuestionsHash(int prelimQuestionsHash)
    {
        this.prelimQuestionsHash = prelimQuestionsHash;
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
                                             House currHouse = Animagi.findHouse((String) o);
                                             if (currHouse == null)
                                                 throw new IllegalArgumentException("House \"" + o + "\" non-existant");
                                             
                                             setHouse(currHouse);
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
                                            else
                                                setRealName("");
                                        }));
        
        modelIO.put("quizTakingState", Pair.of(this::getQuizState,
                                               (Object o) -> {
                                                   if (o != null && o instanceof QuizTakingState)
                                                       setQuizState((QuizTakingState) o);
                                                   else
                                                       setQuizState(QuizTakingState.NOT_TAKING_QUIZ);
                                               }));
        
        modelIO.put("answersList", Pair.of(this::getAnswersList,
                                           (Object o) -> {
                                               if (o != null && o instanceof List)
                                                   setAnswersList((List<String>) o);
                                               else
                                                   setAnswersList(new LinkedList<>());
                                           }));
        
        modelIO.put("quizHash", Pair.of(this::getQuizHash,
                                        (Object o) -> {
                                            if (o != null)
                                                setQuizHash((Integer) o);
                                            else
                                                setQuizHash(0);
                                        }));
        
        modelIO.put("prelimQuestions", Pair.of(this::getPrelimQuestionsHash,
                                               (Object o) -> {
                                                   if (o != null)
                                                       setPrelimQuestionsHash((Integer) o);
                                                   else
                                                       setPrelimQuestionsHash(0);
                                               }));
        
        return modelIO;
    }
    
    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        
        PlayerProfileData that = (PlayerProfileData) o;
        
        if (year != that.year) return false;
        if (quizHash != that.quizHash) return false;
        if (prelimQuestionsHash != that.prelimQuestionsHash) return false;
        if (playerID != null ? !playerID.equals(that.playerID) : that.playerID != null) return false;
        if (realName != null ? !realName.equals(that.realName) : that.realName != null) return false;
        if (house != null ? !house.equals(that.house) : that.house != null) return false;
        if (role != null ? !role.equals(that.role) : that.role != null) return false;
        if (quizState != that.quizState) return false;
        return answersList != null ? answersList.equals(that.answersList) : that.answersList == null;
    }
    
    @Override
    public int hashCode()
    {
        return playerID != null ? playerID.hashCode() : 0;
    }
    
    @Override
    public String toString()
    {
        return "PlayerProfileData{" +
               "playerID=" + playerID +
               ", realName='" + realName + '\'' +
               '}';
    }
}
