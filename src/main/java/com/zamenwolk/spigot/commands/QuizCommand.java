/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.zamenwolk.spigot.commands;

import com.zamenwolk.spigot.Animagi;
import com.zamenwolk.spigot.datas.*;
import com.zamenwolk.spigot.helper.CmdParamUtils;
import com.zamenwolk.spigot.helper.IndexFinder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Author: Martin
 * created on 31/07/2017.
 */
public class QuizCommand implements CommandExecutor
{
    private List<Question> quiz;
    private ProfileCache cache;
    private IndexFinder<String, School> schoolFinder;
    private Animagi plugin;
    private List<String> preliminary;
    
    private Map<UUID, List<String>> prelimAns;
    
    public QuizCommand(List<Question> quiz,
                       ProfileCache cache,
                       IndexFinder<String, School> schoolFinder,
                       Animagi plugin,
                       List<String> preliminary)
    {
        this.quiz = quiz != null ? quiz : new ArrayList<>();
        this.cache = cache;
        this.schoolFinder = schoolFinder;
        this.plugin = plugin;
        this.preliminary = preliminary;
        
        prelimAns = new HashMap<>();
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
        
        if (!handleChange(target, profile) && args.length != 0)
        {
            switch (quizState)
            {
            case PRELIMINARY_QUESTIONS:
                prelimAns.putIfAbsent(profile.getPlayerID(), new LinkedList<>());
                List<String> playerPrelimAns = prelimAns.get(profile.getPlayerID());
                String       answer          = String.join(" ", args);
        
                playerPrelimAns.add(answer);
                if (playerPrelimAns.size() == preliminary.size())
                {
                    onEndOfPreliminary(target, profile);
                    profile.advanceQuiz(preliminary, quiz);
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
                    profile.advanceQuiz(preliminary, quiz);
                }
                break;
                
            case SCHOOL_PICKING:
                School schoolToSort = schoolFinder.find(CmdParamUtils.fromArg(args[0]), true);
                
                if (schoolToSort == null)
                {
                    sender.sendMessage("This school doesn't exist !");
                    break;
                }
                
                House sortedHouse = sort(profile, schoolToSort);
                
                profile.setHouse(sortedHouse);
                Bukkit.getServer().broadcastMessage(ChatColor.AQUA + target.getDisplayName() +
                                                              " has been sorted to " +
                                                              sortedHouse.getName() +
                                                              " ! Welcome !" + ChatColor.RESET);
                profile.advanceQuiz(preliminary, quiz);
                break;
            }
        }
    
        //Display current question
        quizState = profile.getQuizState();
        
        switch (quizState)
        {
        case PRELIMINARY_QUESTIONS:
            printQuestion(target, new String[]{preliminary.get(prelimAns.getOrDefault(target.getUniqueId(), new LinkedList<>()).size())});
            break;
            
        case TAKING_QUIZ:
            printQuestion(target, questionToString(quiz.get(profile.getAnswersList().size())));
            break;
            
        case SCHOOL_PICKING:
            List<String> schoolPick = new ArrayList<>();
            schoolPick.add("Now is the time to pick your school ? Where do you want to go ?");
            schoolPick.addAll(schoolFinder.getKeys());
            printQuestion(target, schoolPick.toArray(new String[]{}));
            break;
        }
        
        return true;
    }
    
    private String[] questionToString(Question question)
    {
        List<String> str = new ArrayList<>();
        
        str.add(question.getQuestionText());
        
        for (Map.Entry<String, Answer> a : question.getAnswerMap().entrySet())
        {
            str.add(a.getKey() + ": " + a.getValue().getResponseText());
        }
        
        return str.toArray(new String[]{});
    }
    
    private void printQuestion(Player target, String[] s)
    {
        target.sendMessage(s);
    }
    
    private boolean handleChange(Player target, PlayerProfile profile)
    {
        QuizTakingState quizState = profile.getQuizState();
        
        if (profile.isTakingQuiz())
        {
            if ((preliminary.hashCode() != profile.getPrelimQuestionsHash()) ||
                (quizState != QuizTakingState.PRELIMINARY_QUESTIONS && quiz.hashCode() != profile.getQuizHash()))
            {
                resetQuiz(target, profile);
                return true;
            }
        }
        
        return false;
    }
    
    private void resetQuiz(Player target, PlayerProfile profile)
    {
        target.sendMessage("The quiz has changed after you started answering it ! Your answers have been deleted. Please start over. Sorry for the inconvenience.");
        
        profile.unsetQuiz();
        profile.advanceQuiz(preliminary, quiz);
        prelimAns.remove(profile.getPlayerID());
    }
    
    private void onEndOfPreliminary(Player target, PlayerProfile profile)
    {
        List<String> currPrelim = prelimAns.get(profile.getPlayerID());
        String realName = currPrelim.get(0) + " " + currPrelim.get(1);
        
        profile.setRealName(realName);
        
        target.setDisplayName(realName);
        prelimAns.remove(target.getUniqueId());
    }
    
    private House sort(PlayerProfile profile, School schoolSorted)
    {
        List<House> potentials = plugin.getHousesOfSchool(schoolSorted);
        List<String> quizAnswers = profile.getAnswersList();
        Map<String, Double> playerTraits = new HashMap<>();
        Map<House, Double> housePoints = new HashMap<>();
        
        Iterator<String> is = quizAnswers.iterator();
        Iterator<Question> iq = quiz.iterator();
        
        while (is.hasNext() && iq.hasNext())
        {
            String currAns = is.next();
            Question currQuest = iq.next();
            
            Map<String, Double> currResp = currQuest.getAnswer(currAns).getTraitsChange();
            
            currResp.forEach((s, d) ->
            {
                Double curr = playerTraits.getOrDefault(s, 0d);
                curr += d;
                playerTraits.put(s, curr);
            });
        }
        
        potentials.forEach(house ->
        {
            Map<String, Double> houseTraits = house.getTraitsFactor();
            Double points = 0d;
            
            for (Map.Entry<String, Double> e : playerTraits.entrySet())
            {
                if (houseTraits.containsKey(e.getKey()))
                    points += e.getValue() * houseTraits.get(e.getKey());
            }
            
            housePoints.put(house, points);
        });
        
        Double maxPoints = Collections.max(housePoints.entrySet(), Map.Entry.comparingByValue()).getValue();
        
        List<House> eligibleHouses = housePoints
                                     .entrySet()
                                     .stream()
                                     .filter(e -> e.getValue().equals(maxPoints))
                                     .map(Map.Entry::getKey)
                                     .collect(Collectors.toList());
        
        if (eligibleHouses.size() == 1)
            return eligibleHouses.get(0);
        
        return eligibleHouses.get(new Random().nextInt(eligibleHouses.size()));
    }
}
