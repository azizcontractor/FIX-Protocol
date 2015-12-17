package objects;

public class OrdStatExec extends OrdStatReq {
	
	private static int exID = 452;
	private String execID;
	private String execType;
	private String ordStat;
	private int length;
	
	public OrdStatExec(OrdStatReq osr){
		super(osr.toFixString());
		execID = (osr.getClordID().substring(0,2) + exID).toUpperCase();
		super.setMsgType("8");
		execType = "I";
		ordStat = null;
		length = 10 + execID.length() + execType.length();
		super.newLength(length);
		super.setSenderID("CONTRASV1");
	}
	
	public OrdStatExec(String fixMsg){
		super(fixMsg.substring(0,55) + fixMsg.substring(76));
		char c = 1;
		String[] str = fixMsg.substring(56, 76).split(c+"|\\=");
		execID = str[1];
		execType = str[3];
		ordStat = str[5];
	}
	
	public String toFixString(){
		char c = 1;
		StringBuilder str = new StringBuilder(super.toFixString());
		str.insert(55, "17="+execID+c+"150="+execType+c+"39="+ordStat+c);
		return str.toString().toUpperCase();
	}
	
	public String toString(){
		String oStat = "0";
		if(ordStat.equalsIgnoreCase("2"))
			oStat = "Filled";
		else if (ordStat.equalsIgnoreCase("4"))
			oStat = "Canceled";
		else if (ordStat.equalsIgnoreCase("C"))
			oStat = "Expired";
		else if (ordStat.equalsIgnoreCase("5"))
			oStat = "Replaced";
		else if (ordStat.equalsIgnoreCase("A"))
			oStat = "Pending";
		return ("order " + super.getClordID() + " status " + oStat).toUpperCase();
	}

	public String getOrdStat() {
		return ordStat;
	}

	public void setOrdStat(String ordStat) {
		this.ordStat = ordStat;
		length += ordStat.length();
		super.newLength(length);
	}
	
	public static void main(String[] args){
		OrdStatReq osr = new OrdStatReq("cl1", "status cl1235");
		StringBuilder str = new StringBuilder(osr.toFixString());
		for (int i = 0; i < str.length(); i++)
			if (str.charAt(i) == 1)
				str.replace(i, i+1, "|");
		System.out.println(str);
		System.out.println(osr);
		OrdStatExec ose = new OrdStatExec(osr);
		ose.setOrdStat("C");
		str = new StringBuilder(ose.toFixString());
		for (int i = 0; i < str.length(); i++)
			if (str.charAt(i) == 1)
				str.replace(i, i+1, "|");
		System.out.println("\n" + str);
		System.out.println(ose);
		
		ose = new OrdStatExec(ose.toFixString());
		str = new StringBuilder(ose.toFixString());
		for (int i = 0; i < str.length(); i++)
			if (str.charAt(i) == 1)
				str.replace(i, i+1, "|");
		System.out.println("\n" + str);
		System.out.println(ose);
	}

}
