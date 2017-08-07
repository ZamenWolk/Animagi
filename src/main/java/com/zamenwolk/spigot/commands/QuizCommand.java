/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.zamenwolk.spigot.commands;

import com.zamenwolk.spigot.datas.PlayerProfile;
import com.zamenwolk.spigot.datas.ProfileCache;
import com.zamenwolk.spigot.datas.Question;
import com.zamenwolk.spigot.datas.QuizTakingState;
import org.bukkit.Bukkit;
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
    private static final List<String> preliminary = new ArrayList<>();
    
    static {
        preliminary.add("What is your first name ?");
        preliminary.add("What is your last name ?");
    }
    
    public static int getPrelimQuizHash()
    {
        return preliminary.hashCode();
    }
    
    private List<Question> quiz;
    private ProfileCache cache;
    
    private Map<UUID, List<String>> prelimAns;
    
    public QuizCommand(List<Question> quiz)
    {
        this.quiz = quiz != null ? quiz : new ArrayList<>();
        cache = ProfileCache.getInstance();
        prelimAns = new HashMap<>();
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) //TODO implement jump if
    {
        if (!(sender instanceof Player))
        {
            sender.sendMessage("Only a player can execute this command !");
            return true;
        }
        
        Player target = (Player) sender;
        PlayerProfile profile = cache.getProfile(target.getUniqueId());
        QuizTakingState quizState;
        
        if (profile == null)
        {
            target.sendMessage("You can't take the quiz right now ! Sorry");
            return true;
        }
        if (!profile.isTakingQuiz())
        {
            target.sendMessage("You can't take the quiz right now ! Sorry");
            return true;
        }
        
        quizState = profile.getQuizState();
        
        if (!handleChange(profile) && args.length != 0)
        {
            switch (quizState)
            {
            case PRELIMINARY_QUESTIONS:
                List<String> playerPrelimAns = prelimAns.putIfAbsent(profile.getPlayerID(), new LinkedList<>());
                String       answer          = String.join(" ", args);
        
                playerPrelimAns.add(answer);
                if (playerPrelimAns.size() == preliminary.size())
                {
                    onEndOfPreliminary(profile);
                    profile.setQuizToMain(quiz.hashCode());
                }
                break;
                
            case TAKING_QUIZ:
                String quizAnswer = args[0];
                
                if (!quiz.get(profile.getAnswersList().size()).isAnswerValid(quizAnswer))
                {
                    sender.sendMessage("Invalid answer !");
                    break;
                }
                
                profile.addAnswer(quizAnswer);
                if (profile.getAnswersList().size() == quiz.size())
                {
                    profile.setQuizToHousePicking();
                }
                break;
                
                //TODO add case for housePicking
            }
        }
        
        displayCurrentQuestion(target);
        return true;
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
        Player target = Bukkit.getPlayer(profile.getPlayerID());
        
        target.sendMessage("The quiz has changed after you started answering it ! Your answers have been deleted. Please start over. Sorry for the inconvenience.");
        
        profile.setQuizToPreliminary(preliminary.hashCode());
        prelimAns.remove(profile.getPlayerID());
    }
    
    private void displayCurrentQuestion(Player target)
    {
        //TODO this
    }
    
    private void onEndOfPreliminary(PlayerProfile profile)
    {
        //TODO this
    }
}
