package CSP;

public class Variable {
	
	private String _name;
	
	public Variable(String name) {
		_name = name;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj.getClass() == getClass()) {
			return _name.equals(((Variable) obj)._name);
		} else {
			return false;
		}
	}
	
	@Override
	public int hashCode() {
		return _name.hashCode();
	}
	
	
	//properties
	public String getName() {return _name;}
}