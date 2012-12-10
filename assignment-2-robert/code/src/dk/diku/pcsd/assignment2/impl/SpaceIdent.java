package dk.diku.pcsd.assignment2.impl;

public class SpaceIdent implements Comparable<SpaceIdent> {
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
