package net.gadgetlabs.neo4jtest.deduplication;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DeduplicationService {

	public Map<String, String> dedupeRules = new HashMap<String, String>();
		
	public String lookup(String type)
	{
		String ret = this.dedupeRules.get(type);
		if(ret != null)
		{
			return ret;
		}
		else
		{
			return null;
		}	
	}

	public void setup(ArrayList<DeduplicationRule> rules) {
		
		for(DeduplicationRule r: rules)
		{
			//TODO need to consider using set on for attributes 
			this.dedupeRules.put(r.getType(), r.getAttributes());
		}
		
	}
	
}
