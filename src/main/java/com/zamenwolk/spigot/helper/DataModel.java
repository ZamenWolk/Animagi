/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 *  License, v. 2.0. If a copy of the MPL was not distributed with this
 *  file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.zamenwolk.spigot.helper;

import org.apache.commons.lang3.tuple.Pair;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Created by Martin on 17/07/2017.
 */
public abstract class DataModel
{
    private Map<String, Pair<Supplier<Object>, Consumer<Object>>> dataModelIO;
    
    protected abstract Map<String, Pair<Supplier<Object>, Consumer<Object>>> createDataModelIO();
    
    protected void objectWriter(ObjectOutputStream out) throws IOException
    {
        if (dataModelIO == null)
        {
            dataModelIO = createDataModelIO();
        }
        
        Map<String, Object> data = new HashMap<>();
        
        for (Map.Entry<String, Pair<Supplier<Object>, Consumer<Object>>> entry : dataModelIO.entrySet())
        {
            data.put(entry.getKey(), entry.getValue().getLeft().get());
        }
        
        out.writeObject(data);
    }
    
    protected void objectReader(ObjectInputStream in) throws IOException, ClassNotFoundException
    {
        if (dataModelIO == null)
        {
            dataModelIO = createDataModelIO();
        }
        
        Map<String, Object> data = (Map<String, Object>) in.readObject();
    
        for (Map.Entry<String, Pair<Supplier<Object>, Consumer<Object>>> piece : dataModelIO.entrySet())
        {
            piece.getValue().getRight().accept(data.get(piece.getKey()));
        }
    }
}
