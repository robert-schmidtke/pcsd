package dk.diku.pcsd.assignment1.impl;

import java.io.IOException;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import dk.diku.pcsd.keyvaluebase.exceptions.BeginGreaterThanEndException;
import dk.diku.pcsd.keyvaluebase.exceptions.KeyAlreadyPresentException;
import dk.diku.pcsd.keyvaluebase.exceptions.KeyNotFoundException;
import dk.diku.pcsd.keyvaluebase.interfaces.Index;
import dk.diku.pcsd.keyvaluebase.interfaces.Pair;

public class IndexImpl implements Index<KeyImpl, ValueListImpl> {

	private StoreImpl store;

	private ValueSerializerImpl vs = new ValueSerializerImpl();

	private int fileLength = 0;

	private List<SpaceIdent> emptyList = Collections.synchronizedList(new LinkedList<SpaceIdent>());

	private Map<KeyImpl, SpaceIdent> mappings = new Hashtable<KeyImpl, SpaceIdent>();
	
	private IndexImpl(){
		this.store=StoreImpl.getInstance();
	}

	private SpaceIdent findFreeSpace(int length) {
		SpaceIdent result = null;
		for (Iterator<SpaceIdent> i = emptyList.iterator(); i.hasNext();) {
			SpaceIdent current = i.next();
			if (current.getLength() >= length) {
				result = current;
				i.remove();
				emptyList.add(new SpaceIdent(current.getPos() + length, current
						.getLength() - length));
				break;
			}
		}

		result = new SpaceIdent(fileLength, length);
		fileLength += length;

		return result;
	}

	private void freeSpace(SpaceIdent s) {
		boolean done = false;

		for (Iterator<SpaceIdent> i = emptyList.iterator(); i.hasNext()
				&& !done;) {
			SpaceIdent current = i.next();

			if (s.getPos() == (current.getPos() + current.getLength())) {
				current.setLength(current.getLength() + s.getLength());
				s = current;
				done = true;
			}

			if (current.getPos() == s.getPos() + s.getLength()) {
				current.setPos(s.getPos());
				current.setLength(current.getLength() + s.getLength());
				s = current;
				done = true;
			}
		}

		if (!done)
			emptyList.add(s);
	}

	public void insert(KeyImpl k, ValueListImpl v)
			throws KeyAlreadyPresentException, IOException {
		if (mappings.containsKey(k)) {
			throw new KeyAlreadyPresentException(k);
		}

		byte[] toWrite = vs.toByteArray(v);

		SpaceIdent space = findFreeSpace(toWrite.length);

		store.write(space.getPos(), toWrite);

		int ldiff = space.getLength() - toWrite.length;

		if (ldiff > 0)
			emptyList
					.add(new SpaceIdent(space.getPos() + toWrite.length, ldiff));

		mappings.put(k, space);
	}

	public void remove(KeyImpl k) throws KeyNotFoundException {
		SpaceIdent s = mappings.get(k);

		if (s == null) {
			throw new KeyNotFoundException(k);
		} else {
			freeSpace(s);
			mappings.remove(k);
		}
	}

	public ValueListImpl get(KeyImpl k)
			throws KeyNotFoundException, IOException {
		SpaceIdent s = mappings.get(k);

		if (s == null) {
			throw new KeyNotFoundException(k);
		} else {
			byte[] read = store.read(s.getPos(), s.getLength());
			return vs.fromByteArray(read);
		}
	}

	public void update(KeyImpl k, ValueListImpl v)
			throws KeyNotFoundException, IOException {
		SpaceIdent s = mappings.get(k);

		if (s == null) {
			throw new KeyNotFoundException(k);
		} else {
			byte[] toWrite = vs.toByteArray(v);

			int ldiff = s.getLength() - toWrite.length;

			if (ldiff < 0) {
				freeSpace(s);
				s = findFreeSpace(toWrite.length);
				store.write(s.getPos(), toWrite);
				mappings.put(k, s);
			} else {
				store.write(s.getPos(), toWrite);
				if (ldiff > 0) {
					emptyList.add(new SpaceIdent(s.getPos() + toWrite.length,
							ldiff));
				}
				s.setLength(toWrite.length);
			}
		}

	}

	@Override
	public List<ValueListImpl> scan(KeyImpl begin, KeyImpl end)
			throws BeginGreaterThanEndException, IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ValueListImpl> atomicScan(KeyImpl begin, KeyImpl end)
			throws BeginGreaterThanEndException, IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void bulkPut(List<Pair<KeyImpl, ValueListImpl>> keys)
			throws IOException {
		// TODO Auto-generated method stub

	}

}