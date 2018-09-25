/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.motoratrib.rest.jsrules;

import cl.motoratrib.rest.jsrules.exception.InvalidParameterException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @author Marcelo
 */
@SuppressWarnings("rawtypes")
public enum Operator {
    GT {
        @Override
        public Boolean compare(Object left, Object right) throws InvalidParameterException {
            Number leftNumber = getNumber(left);
            Number rightNumber = getNumber(right);
            return leftNumber.doubleValue() > rightNumber.doubleValue();
        }
    },
    LT {
        @Override
        public Boolean compare(Object left, Object right) throws InvalidParameterException {
            return GT.compare(right, left);
        }
    },
    GTE {
        @Override
        public Boolean compare(Object left, Object right) throws InvalidParameterException {
            Number leftNumber = getNumber(left);
            Number rightNumber = getNumber(right);
            return leftNumber.doubleValue() >= rightNumber.doubleValue();
        }
    },
    LTE {
        @Override
        public Boolean compare(Object left, Object right) throws InvalidParameterException {
            return GTE.compare(right, left);
        }
    },
    EQ {
        @Override
        public Boolean compare(Object left, Object right) throws InvalidParameterException {
            Object oLeft = getDoubleValue(left);
            Object oRight = getDoubleValue(right);
            return oLeft.equals(oRight);
        }
    },
    EX {
        @Override
        public Boolean compare(Object left, Object right) throws InvalidParameterException {
            return left.toString().matches(right.toString());
        }
    },
    NE {
        @Override
        public Boolean compare(Object left, Object right) throws InvalidParameterException {
            return !EQ.compare(left, right);
        }
    },
    IN {
        @Override
        @SuppressWarnings("unchecked")
        public Boolean compare(Object left, Object right) throws InvalidParameterException {
            Set set = getSet(right);
            Number leftNumber = getNumber(left);
            boolean valueMatched = false;
            for (Object setValueObject : set) {
                Number setValue = getNumber(setValueObject);
                if( Double.doubleToRawLongBits(setValue.doubleValue()) == Double.doubleToRawLongBits(leftNumber.doubleValue()) ) {
                    valueMatched = true;
                    return valueMatched;
                }
            }
            return valueMatched;
        }
    },
    EX_IN {
        @Override
        @SuppressWarnings("unchecked")
        public Boolean compare(Object left, Object right) throws InvalidParameterException {
            Set<String> set = (Set<String>)right;
            boolean valueMatched = false;
            for (String setValueObject : set) {
                if (left.toString().matches(setValueObject)) {
                    valueMatched = true;
                    return valueMatched;
                }
            }

            return valueMatched;
        }
    },
    NOT_IN {
        @Override
        public Boolean compare(Object left, Object right) throws InvalidParameterException {
            return !IN.compare(left, right);
        }
    },
    BETWEEN {
        @Override
        public Boolean compare(Object left, Object right) throws InvalidParameterException {
            Set set = getSet(right);

            if (set.size() != 2) {
                throw new InvalidParameterException("Right parameter must be a set of 2");
            }

            List<Double> doubleList = new ArrayList<>();

            for (Object obj : set) {
                doubleList.add(getNumber(obj).doubleValue());
            }

            Double[] doubleArray = new Double[2];
            doubleArray = doubleList.toArray(doubleArray);

            Double leftDouble = getNumber(left).doubleValue();

            boolean between;

            if (doubleArray[0] < doubleArray[1]) {
                between = leftDouble >= doubleArray[0] && leftDouble <= doubleArray[1];
            } else {
                between = leftDouble >= doubleArray[1] && leftDouble <= doubleArray[0];
            }

            return between;
        }
    },
    NOT_BETWEEN {
        @Override
        public Boolean compare(Object left, Object right) throws InvalidParameterException {
            return !BETWEEN.compare(left, right);
        }
    };

    public abstract Boolean compare(Object left, Object right) throws InvalidParameterException;

    protected Number getNumber(Object obj) throws InvalidParameterException {
        Number number;
        System.out.println("entro getNumber " + obj);
        if (obj instanceof Number) {
            System.out.println("instanceof Number " + obj);
            number = (Number) obj;
        } else {
            // lets see if it's a DateTime instead
            System.out.println("not instanceof Number " + obj);
            try {
                SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
                Date dateTime = formatDate.parse( obj.toString() );
                number = dateTime.getTime();
            } catch (IllegalArgumentException ex) {
                throw new InvalidParameterException(obj.toString() + " is not a number or date. Unable to compare", ex);
            } catch (ParseException ex){
                throw new InvalidParameterException(obj.toString() + " is not a number or date. Unable to compare", ex);
            }
        }
        return number;
    }

    protected Object getDoubleValue(Object obj) {
        Object tObj = obj;
        System.out.println("entro getDoubleValue 1 " + obj);
        if (obj instanceof Number) {
            System.out.println("entro getDoubleValue 2 " + obj);
            Number number = (Number) obj;
            tObj = number.doubleValue();
        }
        System.out.println("salio getDoubleValue " + obj);
        return tObj;
    }

    protected Set getSet(Object param) throws InvalidParameterException {
        Set set;
        if (param instanceof Set) {
            set = (Set) param;
        } else {
            throw new InvalidParameterException("Parameter must be a Set");
        }
        return set;
    }
}
