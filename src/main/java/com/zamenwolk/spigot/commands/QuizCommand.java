/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.zamenwolk.spigot.commands;

import com.zamenwolk.spigot.datas.PlayerProfile;
import com.zamenwolk.spigot.datas.Question;
import com.zamenwolk.spigot.datas.QuizTakingState;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;

/**
 * Author: Martin
 * created on 31/07/2017.
 */
public class QuizCommand implements CommandExecutor
{
    private List<Question> quiz;
    private List<String> preliminary;
    
    private Map<UUID, List<String>> prelimQuestionsAnswers;
    
    public QuizCommand(List<Question> quiz, List<String> preliminary)
    {
        this.quiz = quiz != null ? quiz : new ArrayList<>();
        this.preliminary = preliminary != null ? preliminary : new ArrayList<>();
        
        prelimQuestionsAnswers = new HashMap<>();
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (!(sender instanceof Player))
        {
            sender.sendMessage("Only a player can execute this command !");
            return true;
        }
        
        Player target = (Player) sender;
        
        //TODO finish this
        
        return false;
    }
    
    private boolean handleChange(PlayerProfile profile)
    {
        QuizTakingState quizState = profile.getQuizState();
        
        if (profile.isTakingQuiz())
        {
            if ((preliminary.hashCode() != profile.getPrelimQuestionsHash()) ||
                (quizState != QuizTakingState.PRELIMINARY_QUESTIONS && quiz.hashCode() != profile.getQuizHash()))
            {
                resetQuiz(profile);
                return true;
            }
        }
        
        return false;
    }
    
    private void resetQuiz(PlayerProfile profile)
    {
        //TODO this
    }
}
