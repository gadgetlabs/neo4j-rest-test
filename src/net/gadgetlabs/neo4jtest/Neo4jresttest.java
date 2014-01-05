package net.gadgetlabs.neo4jtest;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import net.gadgetlabs.neo4jtest.dataload.EnronDataloader;
import net.gadgetlabs.neo4jtest.deduplication.DeduplicationRule;
import net.gadgetlabs.neo4jtest.deduplication.DeduplicationRuleReader;
import net.gadgetlabs.neo4jtest.deduplication.DeduplicationService;
import net.gadgetlabs.neo4jtest.graph.EntityLinkEntity;

import org.neo4j.rest.graphdb.RestAPI;
import org.neo4j.rest.graphdb.RestAPIFacade;
import org.neo4j.rest.graphdb.entity.RestNode;
import org.neo4j.rest.graphdb.query.QueryEngine;
import org.neo4j.rest.graphdb.query.RestCypherQueryEngine;
import org.neo4j.rest.graphdb.util.QueryResult;
import org.xml.sax.SAXException;

public class Neo4jresttest {

	private static final String Integer = null;
	private static String DEDUPLICATOR_FILENAME = "/home/badger/workspace/neo4j-rest-test/src/deduplicator.xml";
	private static String NEO4J_URI = "http://localhost:7474/db/data";
	private static String ENRON_FILENAME = "/home/badger/projects/20_sept_enron.data";
	@SuppressWarnings("rawtypes")
	private static QueryEngine engine;
	private static DeduplicationService dedupeService = null; 
	
	public static void main(String[] args) throws FileNotFoundException {
		
		/*
		 * Read the deduplication rules in - this could all be moved to
		 * some configuration class eventually.
		 */
		DeduplicationRuleReader deduplication = new DeduplicationRuleReader();
		ArrayList<DeduplicationRule> rules = null;
		File deduplication_rules_file = new File(DEDUPLICATOR_FILENAME);
		try {
			rules = deduplication.readRules(deduplication_rules_file);
		} catch (SAXException | IOException | ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		/*
		 *  Need some deduplication service
		 */
		dedupeService = new DeduplicationService();
		dedupeService.setup(rules);
		
		/* 
		 * Create a connection to Neo4j
		 */
		RestAPI graphDb = new RestAPIFacade(NEO4J_URI);
		engine = new RestCypherQueryEngine(graphDb); 
		
		/*
		 * Read in the dataset, run it past the deduplicator service 
		 * http://stackoverflow.com/questions/5849154/can-we-write-our-own-iterator-in-java
		 */
		EnronDataloader enronDataloader = new EnronDataloader(ENRON_FILENAME);

	    for(EntityLinkEntity e : enronDataloader) {
	        
	    	e.prettyPrint();
	    
	    	String aAttributes = dedupeService.lookup(e.getA().getType());
	    	String bAttributes = dedupeService.lookup(e.getB().getType());
	    	
	    	Integer aID = createEntity(e.getA().getType(), e.getA().getAttribute(), aAttributes);
	    	Integer bID = createEntity(e.getB().getType(), e.getB().getAttribute(), bAttributes);    	
	    	
	    	System.out.println(aID + " " + bID);
	    	
	    	// Check the type and see what the deduplicator set things.
	    	//TODO need O(1) lookup on the deduplication service
	    	createLink(aID, bID);
	    	
	    	// Get A 
	    	
	    	// What type is it?
	    	
	    	// Lookup the type in the deduplication service (O(1))

	    	// Does a node of that type exist with that attribute?
	    	
	    	// Do we have anything to add to the node? Does it just need creating?
	    	
	    	// Do the same for B
	    	
	    }
	    
	    
	}
	
	
	public static Integer createEntity(String type, String attribute, String dedupeKey)
	{
		Integer nodeId = null;
		Map<String, String> properties = new HashMap<String, String>();
		properties.put("address", attribute);
		properties.put("type", type);
		
		System.out.println(properties.get("address"));
		System.out.println(properties.get("type"));
		
		final String queryString = "MERGE (n: " + properties.get("type") + " {address:\"" + properties.get("address") + "\"}) RETURN ID(n) as id";
		
		System.out.println(queryString);
		
		QueryResult<Map<String, Object>> result = engine.query(queryString, properties);
		
		for(Map<String, Object> row: result)
		{
			Object id = row.get("id");
			if (id instanceof Integer)
			{
				nodeId = (Integer)id;
			}
				
		}
		return nodeId;
		
	}
	
	public static void createLink(Integer idA, Integer idB)
	{
		final String queryString = "START n=node(" + idA.toString() + "), m=node(" + idB.toString() + ") CREATE (n)-[r:FRIENDSHIP]->(m)";
		
		//TODO Need to look into a better way of composing querystrings
		QueryResult<Map<String, Object>> result = engine.query(queryString, null);
	}
	

	/*
	 * List the total number of nodes in the graph database
	 */
	public static void listTotalNodes()
	{
		QueryResult<Map<String,Object>> result= engine.query("start n=node(*) return count(n) as total", Collections.EMPTY_MAP); 
		Iterator<Map<String, Object>> iterator=result.iterator(); 
		
		if(iterator.hasNext()) { 
				Map<String,Object> row= iterator.next(); 
				System.out.println("Total nodes: " + row.get("total")); 
		}
	}
	
	/*
	 * START n=NODE(*) MATCH n-[r]-() DELETE n,r;
	 */
	
	/*
	 * MATCH (n)-[r:FRIENDSHIP]->(m) RETURN n,m limit 100
	 */
	
}
