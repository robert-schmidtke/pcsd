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
	
	
	
	public void setPos(long pos) {
		this.pos = pos;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public SpaceIdent(long pos, int length){
		this.pos=pos;
		this.length=length;
	}
}
