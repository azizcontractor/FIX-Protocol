package objects;

import java.util.Random;

public class ExecOrd extends Order {
	
	private static int exID = 987;
	private String execID;
	private String ordStatus;
	private int length;
	private String price;
	private boolean bool = false;
	private boolean fix = true;
	
	public ExecOrd(String fixMsg) throws Exception{
		super(fixMsg);
		execID = "CONTRASV1";
		super.setSenderID("ContraSV1".toUpperCase());
		if (exID <= 999)
			execID += "0"+exID;
		else
			execID +=exID;
		exID++;
		ordStatus = "A";
		super.setMsgType("8");
		length = execID.length() + 3 + 3 + ordStatus.length();
		if (super.getOrdType().equals("1")){
			Random rand = new Random(System.currentTimeMillis());
			double p = rand.nextDouble()*150;
			price = super.con2Str(p);
			length += 3+price.length();
		}
		super.newLength(length);
	}
	
	public ExecOrd(Order o) throws Exception{
		super(o.toFixString());
		super.setSenderID("ContraSV1".toUpperCase());
		bool = true;
		execID = "CONTRASV1";
		if (exID <= 999)
			execID += "0"+exID;
		else
			execID += exID;
		exID++;
		ordStatus = "2";
		super.setMsgType("8");
		length = execID.length() + 3 + 3 + ordStatus.length();
		if (super.getOrdType().equals("1")){
			Random rand = new Random(System.currentTimeMillis());
			double p = rand.nextDouble()*150;
			price = super.con2Str(p);
			length += 3+price.length();
		}
		super.newLength(length);
	}
	
	public ExecOrd(Order o, int i) throws Exception{
		super(o.toFixString());
		super.setSenderID("ContraSV1".toUpperCase());
		execID = "CONTRASV1";
		if (exID <= 999)
			execID += "0"+exID;
		else
			execID += exID;
		exID++;
		ordStatus = "C";
		super.setMsgType("8");
		length = execID.length() + 3 + 3 + ordStatus.length();
		if (super.getOrdType().equals("1")){
			Random rand = new Random(System.currentTimeMillis());
			double p = rand.nextDouble()*150;
			price = super.con2Str(p);
			length += 3+price.length();
		}
		super.newLength(length);
	}
	
	public ExecOrd(String fixMsg, int k) throws Exception{
		super(fixMsg.substring(0,55)+fixMsg.substring(77),1);
		fix = false;
		char c =1;
		String[] str = fixMsg.substring(55, 76).split(c+"|\\=");
		execID = str[1];
		ordStatus = str[3];
		length = execID.length() + 3 + 3 + ordStatus.length();
		str = fixMsg.split(c+"|\\=");
		price = super.con2Str(super.getPrice());
		super.newLength(length);
	}
	
	public String toFixString(){
		char c = 1;
		StringBuilder str = new StringBuilder(super.toFixString());
		str.insert(55, "17="+execID+c+"39="+ordStatus+c);
		if (super.getOrdType().equals("1") && fix)
			str.insert(97, "44="+price+c);
		return str.toString();
	}
	
	public String superStr(){
		return super.toString();
	}
	
	public String toString(){
		StringBuilder str;
		String sd;
		if(super.getSide().equals("1"))
			sd = "BUY";
		else
			sd = "Sell";
		if (ordStatus.equals("A"))
			str = new StringBuilder("Pending new " + sd +  " Order " + super.getCiOrdID());
		else if (ordStatus.equals("C"))
			str = new StringBuilder("Expired " + sd +  " Order " + super.getCiOrdID());
		else{
			str = new StringBuilder(super.toString().substring(0, super.toString().length()-4));
			if(super.getSide().equals("1"))
				str.replace(0, 3, "BOT");
			else
				str.replace(0, 4, "SLD");
			if (super.getOrdType().equals("1") && bool)
				str.replace(str.length()-3, str.length(), price);
		}
		return str.toString().toUpperCase();
		
	}
	
	public String getExecID() {
		return execID;
	}

	public void setExecID(String execID) {
		this.execID = execID;
	}

	public String getOrdStatus() {
		return ordStatus;
	}

	public void setOrdStatus(String ordStatus) {
		this.ordStatus = ordStatus;
	}

	public static void main(String[] args){
		try {
			Order o = new Order("cl1", "sell 100 ibm @ 99.9 gtc");
			StringBuilder str = new StringBuilder(o.toFixString());
			for (int i = 0; i < str.length(); i++)
				if (str.charAt(i) == 1)
					str.replace(i, i+1, "|");
			System.out.println(str);
			ExecOrd e = new ExecOrd(o.toFixString());
			str = new StringBuilder(e.toFixString());
			for (int i = 0; i < str.length(); i++)
				if (str.charAt(i) == 1)
					str.replace(i, i+1, "|");
			System.out.println("\n" + str);
			System.out.println(e);
			e = new ExecOrd(e.toFixString(),1);
			str = new StringBuilder(e.toFixString());
			for (int i = 0; i < str.length(); i++)
				if (str.charAt(i) == 1)
					str.replace(i, i+1, "|");
			System.out.println("\n" + str);
			System.out.println(e);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
