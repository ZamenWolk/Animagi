/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 *  License, v. 2.0. If a copy of the MPL was not distributed with this
 *  file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.zamenwolk.spigot;

import com.zamenwolk.spigot.commands.*;
import com.zamenwolk.spigot.datas.*;
import com.zamenwolk.spigot.helper.ConfigExtractor;
import com.zamenwolk.spigot.helper.IndexFinder;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Created by Martin on 10/07/2017.
 */
public class Animagi extends JavaPlugin
{
    private static final Logger logger               = Logger.getLogger(Animagi.class.getName());
    private static final String dataOrganizationFile = "dataConf.yml";
    private static final String quizConfigFile       = "quizConf.yml";
    
    private static List<Question>      quiz;
    private static File                dataFolder;
    private static Map<String, School> schools;
    private static Map<String, House>  houses;
    private static IndexFinder<String, School> schoolFinder;
    private static IndexFinder<String, House> houseFinder;
    
    public static File dataFolder()
    {
        return dataFolder;
    }
    
    public static List<House> getHousesOfSchool(School school)
    {
        return houses.entrySet()
                     .stream() //Make stream
                     .filter((Map.Entry<String, House> e) -> e.getValue().getSchool().equals(school)) //Filter with house name
                     .map(Map.Entry::getValue) //Map to stream of House objects
                     .collect(Collectors.toList()); //Make to list
    }
    
    @Override
    public void onDisable()
    {
        logger.info("[ANM] Disabling plugin");
        unloadData();
    }
    
    @Override
    public void onEnable()
    {
        logger.info("[ANM] Loading plugin");
        loadData();
        loadCommands();
        logger.info("[ANM] Plugin loaded successfully");
    }
    
    public static School findSchool(String schoolName)
    {
        return findSchool(schoolName, true);
    }
    
    public static School findSchool(String schoolName, boolean allowAliases)
    {
        return schoolFinder.find(schoolName, allowAliases);
    }
    
    public static House findHouse(String houseName)
    {
        return houseFinder.find(houseName, true);
    }
    
    private void unloadData()
    {
        dataFolder = null;
        schools = null;
        schoolFinder = null;
        houses = null;
        houseFinder = null;
    }
    
    private void loadCommands()
    {
        String pluginName = getDescription().getName().toLowerCase();
        
        getCommand("profile").setExecutor(new ProfileCommand());
        getCommand("points").setExecutor(new PointsCommand());
        getCommand("enableQuiz").setExecutor(new EnableQuizCommand());
        getCommand("createProfile").setExecutor(new CreateProfileCommand());
        getCommand("setHouse").setExecutor(new SetHouseCommand());
        getCommand(pluginName).setExecutor(new VersionCommand(getDescription().getName(), getDescription().getVersion()));
    }
    
    private void loadData()
    {
        dataFolder = new File(this.getDataFolder(), "data/");
        schools = new HashMap<>();
        houses = new HashMap<>();
        
        try
        {
            YamlConfiguration dataConf = YamlConfiguration.loadConfiguration(new File(getDataFolder(), dataOrganizationFile));
            YamlConfiguration quizConf = YamlConfiguration.loadConfiguration(new File(getDataFolder(), quizConfigFile));
        
            loadSchools(dataConf);
            loadHouses(dataConf);
            loadQuiz(quizConf);
        }
        catch (Exception e)
        {
            logger.log(Level.SEVERE, "[ANM] Exception thrown", e);
            getPluginLoader().disablePlugin(this);
        }
    }
    
    private void loadQuiz(Configuration quizConf) throws InvocationTargetException
    {
        logger.info("[ANM] Loading questions");
        
        List<?> questionList = quizConf.getList("quiz");
        
        quiz = ConfigExtractor.createList(Question.class, (List<Object>) questionList);
        
        logger.info("[ANM] Questions loaded");
    }
    
    private void loadSchools(Configuration dataConf)
    {
        logger.info("[ANM] Loading schools");
        
        for (String school : dataConf.getKeys(false))
        {
            try
            {
                schools.put(school, new School(school));
            }
            catch (FileNotFoundException e)
            {
                logger.warning("[ANM] School " + school + " not found and will be created ! Make sure it's normal ! Is it the first time using the plugin here ? Did you change the config ?");
                schools.put(school, new School(new SchoolData(school)));
            }
        }
        
        schoolFinder = new IndexFinder<>(schools, null);
    
        logger.info("[ANM] Schools loaded");
    }
    
    private void loadHouses(Configuration dataConf)
    {
        logger.info("[ANM] Loading houses");
    
        for (String school : dataConf.getKeys(false))
        {
            logger.info("[ANM] Loading houses of " + school);
            Map<String, Object> currSchoolSec = dataConf.getConfigurationSection(school).getValues(false);
            
            for (Map.Entry<String, Object> houseEntry : currSchoolSec.entrySet())
            {
                String houseName = houseEntry.getKey();
                ConfigurationSection houseConf = (ConfigurationSection) houseEntry.getValue();
                try
                {
                    houses.put(houseName, new House(houseName));
                }
                catch (FileNotFoundException e)
                {
                    logger.warning("[ANM] House " + houseName + " not found and will be created ! Make sure it's normal ! Is it the first time using the plugin here ? Did you change the config ?");
                    School currSchool = findSchool(school);
                    houses.put(houseName, new House(new HouseData(houseName, currSchool, 0)));
                }
    
                ConfigExtractor.setObject(houses.get(houseName), houseConf);
            }
        }
        
        houseFinder = new IndexFinder<>(houses, null);
    
        logger.info("[ANM] Houses loaded");
    }
}
