package dk.diku.pcsd.keyvaluebase.interfaces;

public interface KeyValueBaseLog<K extends Key<K>, V extends Value> extends KeyValueBase<K, V>
{
	public void quiesce();
	public void resume();
}
