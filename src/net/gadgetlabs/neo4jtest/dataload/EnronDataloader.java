package net.gadgetlabs.neo4jtest.dataload;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;

import net.gadgetlabs.neo4jtest.graph.EntityLinkEntity;

public class EnronDataloader implements Iterable<EntityLinkEntity>{

	private BufferedReader r;
	private String DEFAULT_TYPE = "email";
	
	public EnronDataloader(String filename) throws FileNotFoundException
	{
		/*
		 * This dataset is very crude, its basically just a CSV of 
		 * entity -> entity (String, String)
		 * The dataset has been datetime trimmed before load, to limit the size of the graph
		 */
		r = new BufferedReader(new FileReader(filename));
		
	}
	
	@Override
	public Iterator<EntityLinkEntity> iterator() {
		return new Iterator<EntityLinkEntity>() {
			
			@Override
			public boolean hasNext()
			{
				try
				{
					r.mark(1);
                	if (r.read() < 0) 
                	{
                    	return false;
                	}
                	r.reset();
                	return true;
            	} 
				catch (IOException e) 
				{
            		return false;
            	}
				
			}
			
			@Override
			public EntityLinkEntity next()
			{
				try
				{
					EntityLinkEntity ele = new EntityLinkEntity();
					
					String line = r.readLine();
					String[] data = line.split(",");
					
					
					//TODO ignore the date at the moment
					ele.setA(DEFAULT_TYPE, data[2].replace("\"", ""));
					ele.setB(DEFAULT_TYPE, data[3].replace("\"", ""));
					
					return ele;
				}
				catch (IOException e) 
				{
            		return null;
            	}
			}

			@Override
			public void remove() {
				// TODO Auto-generated method stub
				
			}
		};
		// TODO Auto-generated method stub
	}
	
	
}
