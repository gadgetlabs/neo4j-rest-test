package net.gadgetlabs.neo4jtest.deduplication;

public abstract class DeduplicationRule {

	public String type;
	public String attribute;
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	//TODO these need changing in the future to allow multiple attributes
	public String getAttributes() {
		return attribute;
	}
	public void setAttributes(String attribute) {
		this.attribute = attribute;
	}
	
	public void prettyPrint()
	{
		System.out.println(this.getType() + " " + this.getAttributes() );
	}
	
}
