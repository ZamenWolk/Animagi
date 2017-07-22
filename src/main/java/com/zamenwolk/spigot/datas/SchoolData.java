/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 *  License, v. 2.0. If a copy of the MPL was not distributed with this
 *  file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.zamenwolk.spigot.datas;

import com.zamenwolk.spigot.helper.DataModel;
import org.apache.commons.lang3.tuple.Pair;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Created by Martin on 15/07/2017.
 */
public class SchoolData extends DataModel implements Serializable
{
    private String name;
    
    public SchoolData(String name)
    {
        super();
        setName(name);
    }
    
    String getName()
    {
        return name;
    }
    
    void setName(String name)
    {
        if (name == null)
            throw new IllegalArgumentException("name is null");
        this.name = name;
    }
    
    private void writeObject(java.io.ObjectOutputStream out)
    throws IOException
    {
        objectWriter(out);
    }
    private void readObject(java.io.ObjectInputStream in)
    throws IOException, ClassNotFoundException
    {
        objectReader(in);
    }
    
    @Override
    protected Map<String, Pair<Supplier<Object>, Consumer<Object>>> createDataModelIO()
    {
        Map<String, Pair<Supplier<Object>, Consumer<Object>>> modelIO = new HashMap<>();
    
        modelIO.put("name", Pair.of(this::getName,
                                     (Object o) ->
                                     {
                                         if (o != null && o instanceof String)
                                             setName((String) o);
                                     }));
    
        return modelIO;
    }
    
    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        
        SchoolData that = (SchoolData) o;
        
        return name != null ? name.equals(that.name) : that.name == null;
    }
    
    @Override
    public int hashCode()
    {
        return name != null ? name.hashCode() : 0;
    }
    
    @Override
    public String toString()
    {
        return "SchoolData{" +
               "name='" + name + '\'' +
               '}';
    }
}
