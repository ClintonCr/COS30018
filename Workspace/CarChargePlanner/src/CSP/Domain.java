package CSP;

import java.util.List;

public class Domain {

    private Object[] _values;

    public Domain(List<?> values) {
        this._values = new Object[values.size()];
        for (int i = 0; i < values.size(); i++)
            this._values[i] = values.get(i);
    }

    public Domain(Object[] values) {
        this._values = new Object[values.length];
        for (int i = 0; i < values.length; i++)
            this._values[i] = values[i];
    }

    public int size() {
        return _values.length;
    }

    public Object get(int index) {
        return _values[index];
    }

    public boolean isEmpty() {
        return _values.length == 0;
    }

    public boolean contains(Object value) {
        for (Object v : _values)
            if (v.equals(value))
                return true;
        return false;
    }

  //  @Override
  //  public Iterator<Object> iterator() {
  //     return new ArrayIterator<Object>(values);
  //  }

   /*  public List<Object> asList() {
        List<Object> result = new ArrayList<Object>();
        for (Object value : _values)
            result.add(value);
        return result;
    }*/

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Domain) {
            Domain d = (Domain) obj;
            if (d.size() != _values.length)
                return false;
            else
                for (int i = 0; i < _values.length; i++)
                    if (!_values[i].equals(d._values[i]))
                        return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 9; // arbitrary seed value
        int multiplier = 13; // arbitrary multiplier value
        for (int i = 0; i < _values.length; i++)
            hash = hash * multiplier + _values[i].hashCode();
        return hash;
    }

    @Override
    public String toString() {
        StringBuffer result = new StringBuffer("{");
        boolean comma = false;
        for (Object value : _values) {
            if (comma)
                result.append(", ");
            result.append(value.toString());
            comma = true;
        }
        result.append("}");
        return result.toString();
    }
}