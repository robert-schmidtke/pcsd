package dk.diku.pcsd.assignment1.impl;

public class SpaceIdent {
	private long pos;
	
	private int length;

	public long getPos() {
		return pos;
	}

	public int getLength() {
		return length;
	}

	public SpaceIdent(long pos, int length){
		this.pos=pos;
		this.length=length;
	}
}
