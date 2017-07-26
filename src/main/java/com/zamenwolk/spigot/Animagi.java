/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 *  License, v. 2.0. If a copy of the MPL was not distributed with this
 *  file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.zamenwolk.spigot;

import com.zamenwolk.spigot.commands.PointsCommand;
import com.zamenwolk.spigot.commands.ProfileCommand;
import com.zamenwolk.spigot.datas.House;
import com.zamenwolk.spigot.datas.HouseData;
import com.zamenwolk.spigot.datas.School;
import com.zamenwolk.spigot.datas.SchoolData;
import com.zamenwolk.spigot.helper.ConfigExtractor;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Created by Martin on 10/07/2017.
 */
public class Animagi extends JavaPlugin
{
    public static final Logger logger = Logger.getLogger(Animagi.class.getName());
    public static final String dataOrganizationFile = "dataConf.yml";
    
    private static File                dataFolder;
    private static Map<String, School> schools;
    private static Map<String, House>  houses;
    
    public static File dataFolder()
    {
        return dataFolder;
    }
    
    public static School getSchool(String schoolName)
    {
        return schools.get(schoolName);
    }
    
    public static List<House> getHousesOfSchool(String schoolName)
    {
        return houses.entrySet()
                     .stream() //Make stream
                     .filter((Map.Entry<String, House> e) -> e.getValue().getSchool().getName().equalsIgnoreCase(schoolName)) //Filter with house name
                     .map(Map.Entry::getValue) //Map to stream of House objects
                     .collect(Collectors.toList()); //Make to list
    }
    
    public static House getHouse(String houseName)
    {
        return houses.get(houseName);
    }
    
    public static String checkSchool(String schoolName)
    {
        for (Map.Entry<String, School> schoolEntry : schools.entrySet())
        {
            if (schoolEntry.getKey().equalsIgnoreCase(schoolName))
                return schoolEntry.getKey();
        }
        
        return null;
    }
    
    @Override
    public void onDisable()
    {
        logger.info("Disabling plugin");
        unloadData();
    }
    
    @Override
    public void onEnable()
    {
        logger.info("Loading plugin");
        loadData();
        loadCommands();
        logger.info("Plugin loaded successfully");
    }
    
    private void unloadData()
    {
        dataFolder = null;
        schools = null;
        houses = null;
    }
    
    private void loadCommands()
    {
        getCommand("profile").setExecutor(new ProfileCommand());
        getCommand("points").setExecutor(new PointsCommand());
    }
    
    private void loadData()
    {
        dataFolder = new File(this.getDataFolder(), "data/");
        schools = new HashMap<>();
        houses = new HashMap<>();
    
        YamlConfiguration dataConf = YamlConfiguration.loadConfiguration(new File(getDataFolder(), dataOrganizationFile));
    
        loadSchools(dataConf);
        loadHouses(dataConf);
    }
    
    private void loadSchools(Configuration dataConf)
    {
        logger.info("Loading schools");
        
        for (String school : dataConf.getKeys(false))
        {
            try
            {
                schools.put(school, new School(school));
            }
            catch (IOException e)
            {
                logger.warning("School " + school + " not found and will be created ! Make sure it's normal ! Is it the first time using the plugin here ? Did you change the config ?");
                try
                {
                    schools.put(school, new School(new SchoolData(school), school));
                }
                catch (IOException e1)
                {
                    logger.severe("Severe error occured. Disabling plugin");
                    e.printStackTrace();
                    getPluginLoader().disablePlugin(this);
                }
            }
            catch (ClassNotFoundException e)
            {
                logger.severe("Severe error occured. Disabling plugin");
                e.printStackTrace();
                getPluginLoader().disablePlugin(this);
            }
        }
    
        logger.info("Schools loaded");
    }
    
    private void loadHouses(Configuration dataConf) //TODO finish that
    {
        logger.info("Loading houses");
    
        for (String school : dataConf.getKeys(false))
        {
            logger.info("Loading houses of " + school);
            Map<String, Object> currSchoolSec = dataConf.getConfigurationSection(school).getValues(false);
            
            for (Map.Entry<String, Object> houseEntry : currSchoolSec.entrySet())
            {
                String houseName = houseEntry.getKey();
                ConfigurationSection houseConf = (ConfigurationSection) houseEntry.getValue();
                try
                {
                    houses.put(houseName, new House(houseName));
                }
                catch (IOException e)
                {
                    logger.warning("House " + houseName + " not found and will be created ! Make sure it's normal ! Is it the first time using the plugin here ? Did you change the config ?");
                    try
                    {
                        School currSchool = getSchool(school);
                        houses.put(houseName, new House(new HouseData(houseName, currSchool, 0), houseName));
                    }
                    catch (IOException e1)
                    {
                        logger.severe("Severe error occured. Disabling plugin");
                        e.printStackTrace();
                        getPluginLoader().disablePlugin(this);
                    }
                }
                catch (ClassNotFoundException e)
                {
                    logger.severe("Severe error occured. Disabling plugin");
                    e.printStackTrace();
                    getPluginLoader().disablePlugin(this);
                }
    
                ConfigExtractor.setObject(houses.get(houseName), houseConf);
            }
        }
    
        logger.info("Houses loaded");
    }
}
