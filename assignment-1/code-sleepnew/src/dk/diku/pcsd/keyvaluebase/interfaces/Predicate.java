package dk.diku.pcsd.keyvaluebase.interfaces;

import javax.xml.bind.annotation.XmlSeeAlso;

import dk.diku.pcsd.assignment1.impl.StringLengthPredicate;

@XmlSeeAlso({StringLengthPredicate.class})
public abstract class Predicate<T> {
	public abstract boolean evaluate(T input);
}
