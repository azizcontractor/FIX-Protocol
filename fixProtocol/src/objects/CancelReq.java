package objects;

public class CancelReq extends StdHdrTrlr {
	
	private static int canID = 543;
	private String cID;
	private String origID;
	private String side;
	private int length;
	
	public CancelReq(String name, String msg){
		super(name);
		String[] str = msg.split(" ");
		cID = name.toUpperCase() + canID;
		canID++;
		super.setMsgType("F");
		if (str[1].equalsIgnoreCase("BUY"))
			side = "1";
		else if (str[1].equalsIgnoreCase("SELL")){
			side = "2";
		}
		origID = str[3].toUpperCase();
		length = 3 + cID.length() + 3 + side.length() + 3 + origID.length();
		super.newLength(length);
	}
	
	public CancelReq(String fixMsg) throws Exception{
		super(fixMsg.substring(0,55),1);
		char c = 1;
		String[] str = fixMsg.substring(55).split(c+"|\\=");
		cID = str[1];
		origID = str[3];
		side = str[5];
		length = 3 + cID.length() + 3 + side.length() + 3 + origID.length();
		super.newLength(length);
		if (!super.getChkSum().equals(str[7]) && !super.getSenderID().equalsIgnoreCase("CONTRASV1"))
			if (super.getMsgType().equals("F"))
				throw new Exception("Invalid Fix String");
	}
	
	public String toFixString(){
		char c = 1;
		return super.toFixString()+"11="+cID+c+"41="+origID+c+"54="+side+c+"10="+super.getChkSum()+c;
	}
	
	public String toString(){
		String str = "Cancel ";
		if(side.equals("1"))
			str += "Buy order ";
		else
			str += "Sell order ";
		str += origID;
		return str.toUpperCase();
	}
	
	public String getOrigID() {
		return origID;
	}

	public void setOrigID(String origID) {
		this.origID = origID;
	}

	public String getSide() {
		return side;
	}

	public void setSide(String side) {
		this.side = side;
	}

	public static void main(String[] args){
		CancelReq c = new CancelReq("Cl1", "cancel sell order cl1299");
		StringBuilder str = new StringBuilder(c.toFixString());
		for (int i = 0; i < str.length(); i++)
			if (str.charAt(i) == 1)
				str.replace(i, i+1, "|");
		System.out.println(str);
		System.out.println(c);
		try {
			c = new CancelReq(c.toFixString());
			str = new StringBuilder(c.toFixString());
			for (int i = 0; i < str.length(); i++)
				if (str.charAt(i) == 1)
					str.replace(i, i+1, "|");
			System.out.println("\n"+str);
			System.out.println(c);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}

}
