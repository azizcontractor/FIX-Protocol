package objects;

public class CancelExec extends CancelReq {
	
	private static int exID = 101;
	private String execID;
	private String ordStat;
	private int length;
	
	public CancelExec(CancelReq cr) throws Exception{
		super(cr.toFixString());
		super.setSenderID("ContraSV1".toUpperCase());
		super.setMsgType("8");
		execID = "ContraSV1".toUpperCase()+exID;
		exID++;
		length = 3 + execID.length();
		super.newLength(length);
	}
	
	public CancelExec(String fixMsg) throws Exception{
		super(fixMsg.substring(0, 55) + fixMsg.substring(76));
		char c = 1;
		String[] str = fixMsg.substring(55,76).split(c+"|\\=");
		execID = str[1];
		ordStat = str[3];
		length = 3 + execID.length() + 3 + ordStat.length();
		super.newLength(length);
	}
	
	public String toFixString(){
		char c = 1;
		StringBuilder str = new StringBuilder(super.toFixString());
		str.insert(55, "17="+execID+c+"39="+ordStat+c);
		return str.toString();
	}
	
	public String toString(){
		String str, sd;
		if(super.getSide().equals("2"))
			sd = "SELL";
		else 
			sd = "BUY";
		if (ordStat.equals("4"))
			str = "Cancelled " + sd + " order " + super.getOrigID();
		else
			str = "Cancel Request Reject for " + sd + " order " + super.getOrigID();
		return str.toUpperCase();
	}
	
	public String getOrdStat(){
		return ordStat;
	}
	
	public void setOrdStat(String s){
		ordStat = s;
		length = 3 + ordStat.length();
		super.newLength(length);
	}
	
	
	
	public String getExecID() {
		return execID;
	}

	public void setExecID(String execID) {
		this.execID = execID;
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
			CancelExec e = new CancelExec(c);
			e.setOrdStat("8");
			str = new StringBuilder(e.toFixString());
			for (int i = 0; i < str.length(); i++)
				if (str.charAt(i) == 1)
					str.replace(i, i+1, "|");
			System.out.println("\n"+ str);
			System.out.println(e);
			e = new CancelExec(e.toFixString());
			str = new StringBuilder(e.toFixString());
			for (int i = 0; i < str.length(); i++)
				if (str.charAt(i) == 1)
					str.replace(i, i+1, "|");
			System.out.println(str);
			System.out.println(e);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
}
