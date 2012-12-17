package dk.diku.pcsd.assignment3.impl;

import java.io.Serializable;

public class SpaceIdent implements Comparable<SpaceIdent>, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1330331121961034752L;

	private long pos;

	private int length;

	private long next;

	public long getPos() {
		return pos;
	}

	public int getLength() {
		return length;
	}

	public long getNext() {
		return next;
	}

	@Deprecated
	public void setLength(int length) {
		this.length = length;
		this.next = pos + length;
	}

	public SpaceIdent(long pos, int length) {
		this.pos = pos;
		this.length = length;
		this.next = pos + length;
	}

	@Override
	public int compareTo(SpaceIdent other) {
		long otherP = other.getPos();
		if (pos < otherP)
			return -1;
		else if (pos == otherP)
			return 0;
		else
			return 1;

	}
}
