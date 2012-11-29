package dk.diku.pcsd.keyvaluebase.interfaces;

public abstract class Predicate<T> {
	public abstract boolean evaluate(T input);
}
