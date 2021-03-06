package com.zamenwolk.spigot.helper;

import java.io.*;

/**
 * Created by Martin on 11/07/2017.
 */
public abstract class DataManager<Data extends DataModel & Serializable>
{
	protected Data             data;
	private File               dataFile;
	
	public DataManager(File dataFile) throws FileNotFoundException
    {
		try
		{
			this.dataFile = dataFile;
			data = (Data) new ObjectInputStream(new FileInputStream(dataFile)).readObject();
			
			saveChanges();
		}
        catch (ClassNotFoundException e)
        {
            throw new RuntimeException("Class error while extracting object", e);
        }
        catch (IOException e)
        {
            if (e instanceof FileNotFoundException)
                throw ((FileNotFoundException)e);
            throw new RuntimeException("IOException while creating members", e);
        }
    }
	
	public DataManager(Data data, File dataFile)
	{
	    try
        {
            this.dataFile = dataFile;
    
            if (dataFile.exists())
                this.data = (Data) new ObjectInputStream(new FileInputStream(dataFile)).readObject();
            else
            {
                this.data = data;
                dataFile.getParentFile().mkdirs();
                dataFile.createNewFile();
            }
    
            saveChanges();
        }
        catch (ClassNotFoundException e)
        {
            throw new RuntimeException("Class error while extracting object", e);
        }
        catch (IOException e)
        {
            throw new RuntimeException("IOException while creating members", e);
        }
	}
	
	protected final void saveChanges()
	{
		try
		{
            //Delete existing file
            dataFile.delete();
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(dataFile));
			out.writeUnshared(data);
			out.close();
        }
		catch (IOException e)
		{
			e.printStackTrace();
			System.exit(-1);
		}
	}
    
    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        
        DataManager<?> that = (DataManager<?>) o;
        
        if (!data.equals(that.data)) return false;
        return dataFile.equals(that.dataFile);
    }
    
    @Override
    public int hashCode()
    {
        int result = data.hashCode();
        result = 31 * result + dataFile.hashCode();
        return result;
    }
}