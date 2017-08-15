/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.zamenwolk.spigot.datas;

import com.zamenwolk.spigot.helper.ConfigExtractible;
import com.zamenwolk.spigot.helper.ConfigExtractor;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

/**
 * Author: Martin
 * created on 26/07/2017.
 */
public class Question implements ConfigExtractible
{
    private String              questionText;
    private Map<String, Answer> answerMap;
    
    public Question()
    {
        questionText = "";
        answerMap = new HashMap<>();
    }
    
    public boolean isAnswerValid(String answer)
    {
        return answerMap
                .entrySet()
                .parallelStream()
                .anyMatch(e -> e.getKey().equalsIgnoreCase(answer));
    }
    
    public Answer getAnswer(String answer)
    {
        for (Map.Entry<String, Answer> e : answerMap.entrySet())
        {
            if (e.getKey().equalsIgnoreCase(answer))
                return e.getValue();
        }
        
        return null;
    }
    
    public String getQuestionText()
    {
        return questionText;
    }
    
    public Map<String, Answer> getAnswerMap()
    {
        return answerMap;
    }
    
    @Override
    public void getFromConfig(Object config)
    {
        Map<String, Object> actConf = (Map<String, Object>) config;
        questionText = (String) actConf.getOrDefault("question", null);
        if (questionText == null)
            throw new  IllegalArgumentException("No question text given for question");
        
        Map<String, Object> confMap = new HashMap<>();
        for (Map.Entry<String, Object> e : ((Map<String, Object>)actConf.get("answers")).entrySet())
        {
            Map<String, Object> currSec = (Map<String, Object>) e.getValue();
            confMap.put(e.getKey(), currSec);
        }
    
        try
        {
            answerMap = ConfigExtractor.createMap(Answer.class, confMap);
        }
        catch (InvocationTargetException e)
        {
            throw new IllegalArgumentException("Invocation target exception", e);
        }
        if (answerMap.size() == 0)
            throw new IllegalArgumentException("No answers given for question");
    }
    
    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        
        Question question = (Question) o;
        
        if (!questionText.equals(question.questionText)) return false;
        return answerMap.equals(question.answerMap);
    }
    
    @Override
    public int hashCode()
    {
        int result = questionText.hashCode();
        result = 31 * result + answerMap.hashCode();
        return result;
    }
}
