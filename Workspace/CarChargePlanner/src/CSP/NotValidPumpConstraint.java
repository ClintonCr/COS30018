package CSP;

import java.util.List;

public class NotValidPumpConstraint implements Constraint{

	public NotValidPumpConstraint() {
		
	}

	@Override
	public List<Variable> getScope() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isSatisfiedWith(Assignment assignment) {
		// TODO Auto-generated method stub
		return false;
	}

}
