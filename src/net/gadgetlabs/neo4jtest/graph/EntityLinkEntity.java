package net.gadgetlabs.neo4jtest.graph;

public class EntityLinkEntity {

	private Entity a = new Entity();
	private Entity b = new Entity();
	
	public void setA(String type, String attribute) {
		a.setType(type);
		a.setAttribute(attribute);
	}

	public void setB(String type, String attribute) {
		b.setType(type);
		b.setAttribute(attribute);
		
	}

	public Entity getA()
	{
		return a;
	}
	
	public Entity getB()
	{
		return b;
	}
	
	public void prettyPrint(){
		System.out.println(b.getAttribute() + "\t" + a.getAttribute());
	}
	
	
}
