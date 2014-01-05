package net.gadgetlabs.neo4jtest.deduplication;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class DeduplicationRuleReader {

	public Document doc = null;
	public ArrayList<DeduplicationRule> rules = new ArrayList<DeduplicationRule>();
	
	public ArrayList<DeduplicationRule> readRules(File filename) throws SAXException, IOException, ParserConfigurationException {
		// TODO Auto-generated method stub
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		doc = dBuilder.parse(filename);
		
		//read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
		doc.getDocumentElement().normalize();
		
		// Check that its the right type of file 
		String nodeName = doc.getDocumentElement().getNodeName();
		if (nodeName != "dedupe")
		{
			throw new IOException("Probably not a deduplicaton file");
		}
				
		// read the entities
		NodeList eList = doc.getElementsByTagName("entity");
		ArrayList<EntityDeduplicationRule> entities = readEntities(eList);
		rules.addAll(entities);
		
		// read the links
		NodeList lList = doc.getElementsByTagName("link");
		ArrayList<LinkDeduplicationRule> links = readLinks(lList);
		rules.addAll(links);
		
		return rules;
		
	}
	
	//TODO code reuse 

	private ArrayList<EntityDeduplicationRule> readEntities(NodeList eList) {
		
		ArrayList<EntityDeduplicationRule> ruleList = new ArrayList<EntityDeduplicationRule>();
		EntityDeduplicationRule rule = new EntityDeduplicationRule();
		
		for (int i = 0; i < eList.getLength(); i++) {
			
			Element element = (Element) eList.item(i);
			rule.setType(element.getAttribute("type"));
			
			NodeList elementsByTagName = element.getElementsByTagName("deduplicate");
			Element item = (Element) elementsByTagName.item(0);
			rule.setAttributes(item.getAttribute("attribute")); 
			ruleList.add(rule);
		     
			//rule.prettyPrint();
			
		}	
		return ruleList;
	}
	
	private ArrayList<LinkDeduplicationRule> readLinks(NodeList lList) {
		ArrayList<LinkDeduplicationRule> ruleList = new ArrayList<LinkDeduplicationRule>();
		LinkDeduplicationRule rule = new LinkDeduplicationRule();
		
		for (int i = 0; i < lList.getLength(); i++) {
			
			Element element = (Element) lList.item(i);
			rule.setType(element.getAttribute("type"));
			
			NodeList elementsByTagName = element.getElementsByTagName("deduplicate");
			Element item = (Element) elementsByTagName.item(0);
			rule.setAttributes(item.getAttribute("attribute")); 
			ruleList.add(rule);
		     
		}	
		return ruleList;
	}


}
