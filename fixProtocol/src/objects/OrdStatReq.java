package objects;

public class OrdStatReq extends StdHdrTrlr {
	
	private String clordID;
	private int length;
	
	public OrdStatReq(String name, String message){
		super(name);
		super.setMsgType("H");
		String[] str = message.split(" ");
		clordID = str[1];
		length = 3 + clordID.length();
		super.newLength(length);
	}
	
	public OrdStatReq(String fixMsg){
		super(fixMsg.substring(0,55),1);
		char c = 1;
		String[] str = fixMsg.substring(55).split(c+"|\\=");
		clordID = str[1];
		length = 3 + clordID.length();
		super.newLength(length);
	}
	
	public String getClordID() {
		return clordID;
	}

	public void setClordID(String clordID) {
		this.clordID = clordID;
	}

	public String toFixString(){
		char c = 1;
		return super.toFixString()+"11="+clordID+c+"10="+super.getChkSum()+c;
	}
	
	public String toString(){
		return ("status " + clordID).toUpperCase();
	}

}
