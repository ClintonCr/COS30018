package CSP;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;

public class CSP {
	
	private List<Variable> _variables;
	private List<Domain> _domains;
	private List<Constraint> _constraints;
	
	private Hashtable<Variable,Integer> _varIndexHash;
	private Hashtable<Variable,List<Constraint>> _cnet;
	
	public CSP() {
		_variables = new ArrayList<Variable>();
		_domains = new ArrayList<Domain>();
		_constraints = new ArrayList<Constraint>();
		_varIndexHash = new Hashtable<Variable, Integer>();
		_cnet = new Hashtable<Variable, List<Constraint>>();
	}
	
	public CSP(List<Variable> vars) {
		this();
		for (Variable v : vars) {
			addVariable(v);
		}
	}
	
	protected void addVariable(Variable var) {
		if (!_varIndexHash.containsKey(var)) {
			Domain emptyDomain = new Domain(Collections.emptyList());
			_variables.add(var);
			_domains.add(emptyDomain);
			_varIndexHash.put(var,  _variables.size() - 1);
			_cnet.put(var, new ArrayList<Constraint>());
		} else {
			throw new IllegalArgumentException("Variable already exists.");
		}
	}
	
	public List<Variable> getVariables(){
		return Collections.unmodifiableList(_variables);
	}
	
	public int indexOf(Variable var) {
		return _varIndexHash.get(var);
	}
	
	public Domain getDomain(Variable var) {
		return _domains.get(_varIndexHash.get(var));
	}
	
	public void setDomain(Variable var, Domain domain) {
		_domains.set(indexOf(var),domain);
	}
	
    /*public void removeValueFromDomain(Variable var, Object value) {
        Domain currDomain = getDomain(var);
        List<Object> values = new ArrayList<Object>(currDomain.size());
        for (Object v : currDomain)
            if (!v.equals(value))
                values.add(v);
        setDomain(var, new Domain(values));
    }*/

    public void addConstraint(Constraint constraint) {
        _constraints.add(constraint);
        for (Variable var : constraint.getScope())
            _cnet.get(var).add(constraint);
    }

    public List<Constraint> getConstraints() {
        return _constraints;
    }

    public List<Constraint> getConstraints(Variable var) {
        return _cnet.get(var);
    }

    public Variable getNeighbor(Variable var, Constraint constraint) {
        List<Variable> scope = constraint.getScope();
        if (scope.size() == 2) {
            if (var.equals(scope.get(0)))
                return scope.get(1);
            else if (var.equals(scope.get(1)))
                return scope.get(0);
        }
        return null;
    }
    
    public CSP copyDomains() {
    	CSP result = new CSP();
    	result._variables = _variables;
        result._domains = new ArrayList<Domain>(_domains.size());
        result._domains.addAll(_domains);
        result._constraints = _constraints;
        result._varIndexHash = _varIndexHash;
        result._cnet = _cnet;
        return result;
    }
}
