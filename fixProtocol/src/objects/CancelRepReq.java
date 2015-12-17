package objects;

public class CancelRepReq extends StdHdrTrlr {
	
	private static int ordID = 311;
	private String ciOrdID;
	private String origID;
	private String side;
	private String ordType;
	private double p;
	private String price;
	private String timeInForce;
	private String ordQty;
	private String symbol;
	private int length;
	private String side2;
	
	public CancelRepReq(String name, String msg){
		super(name);
		String[] str = msg.split(" ");
		super.setMsgType("G");
		ciOrdID = name.toUpperCase() + ordID;
		ordID++;
		origID = str[3];
		if (str[1].equalsIgnoreCase("BUY"))
			side = "1";
		else
			side = "2";
		if (str[10].equalsIgnoreCase("MKT"))
			ordType = "1";
		else
			ordType = "2";
		if (ordType.equals("2")){
			p = Double.parseDouble(str[10]);
			price = super.con2Str(p);
		}
		else
			p = 0;
		if(str.length > 11 && str[11].equalsIgnoreCase("GTC"))
			timeInForce = "1";
		else 
			timeInForce = "0";
		ordQty = str[7];
		symbol = str[8];
		side2 = str[6];
		length = 3 + ciOrdID.length() + 3 + origID.length() + 3 + side.length() + 3 + ordType.length() + 3 + ordQty.length() + 3 + symbol.length();
		if (p>0)
			length += 3 + price.length();
		super.newLength(length);
	}
	
	public CancelRepReq(String fixMsg){
		super(fixMsg.substring(0,55),1);
		super.setMsgType("G");
		char c = 1;
		String[] str = fixMsg.substring(55).split(c+"|\\=");
		ciOrdID = str[1];
		origID = str[3];
		side = str[5];
		ordType = str[7];
		if (ordType.equals("1")){
			p = 0;
			timeInForce = str[9];
			ordQty = str[11];
			symbol = str[13];
			if (str[15].equalsIgnoreCase("1"))
				side2 = "buy";
			else
				side2 = "sell";
		}
		else{
			price = str[9];
			p = Double.parseDouble(price);
			timeInForce = str[11];
			ordQty = str[13];
			symbol = str[15];
			if (str[17].equalsIgnoreCase("1"))
				side2 = "buy";
			else
				side2 = "sell";
		}
		length = 3 + ciOrdID.length() + 3 + origID.length() + 3 + side.length() + 3 + ordType.length() + 3 + ordQty.length() + 3 + symbol.length();
		if (p>0)
			length += 3 + price.length();
		super.newLength(length);
	}
	
	public CancelRepReq(String fixMsg, char i){
		super(fixMsg.substring(0,55),1);
		char c = 1;
		String[] str = fixMsg.substring(55).split(c+"|\\=");
		ciOrdID = str[1];
		side = str[5];
		origID = str[3];
		length = 9 + ciOrdID.length() + side.length() + origID.length();
		super.newLength(length);
	}
	
	public CancelRepReq(ExecOrd oe, CancelExec ce){
		super(oe.toFixString().substring(0, 55),1);
		ciOrdID = oe.getCiOrdID();
		origID = ce.getOrigID();
		side = ce.getSide();
		side2 = oe.getSide();
		ordType = oe.getOrdType();
		p = oe.getPrice();
		if (p>0)
			price = super.con2Str(p);
		timeInForce = oe.getTimeInForce();
		ordQty = ""+oe.getOrdQty();
		symbol = oe.getSymbol();
		length = 3 + ciOrdID.length() + 3 + origID.length() + 3 + side.length() + 3 + ordType.length() + 3 + ordQty.length() + 3 + symbol.length();
		if (p>0)
			length += 3 + price.length();
		super.newLength(length);
	}
	
	public CancelRepReq(String fixMsg, int b){
		super(fixMsg.substring(0,55),1);
		char c = 1;
		String[] str = fixMsg.substring(55).split(c+"|\\=");
		if (str[3].equalsIgnoreCase("5")){
			str = fixMsg.substring(77).split(c+"|\\=");
			ciOrdID = str[1];
			origID = str[3];
			side = str[5];
			ordType = str[7];
			if (ordType.equals("1")){
				p = 0;
				timeInForce = str[9];
				ordQty = str[11];
				symbol = str[13];
				if (str[15].equalsIgnoreCase("1"))
					side2 = "buy";
				else
					side2 = "sell";
			}
			else{
				price = str[9];
				p = Double.parseDouble(price);
				timeInForce = str[11];
				ordQty = str[13];
				symbol = str[15];
				if (str[17].equalsIgnoreCase("1"))
					side2 = "buy";
				else
					side2 = "sell";
			}
			length = 3 + ciOrdID.length() + 3 + origID.length() + 3 + side.length() + 3 + ordType.length() + 3 + ordQty.length() + 3 + symbol.length();
			if (p>0)
				length += 3 + price.length();
			super.newLength(length);
		}
		else {
			str = fixMsg.substring(55).split(c+"|\\=");
			ciOrdID = str[1];
			side = str[5];
			origID = str[3];
			length = 9 + ciOrdID.length() + side.length() + origID.length();
			super.newLength(length);
		}
	}
	
	public String toFixString(){
		char c = 1;
		String str = super.toFixString() +"11="+ciOrdID+c+"41="+origID+c+"54="+side+c+"40="+ordType+c;
		if(p > 0)
			str += "44="+price+c + "59="+timeInForce+c+"38="+ordQty+c+"55="+symbol+c;
		else
			str += "59="+timeInForce+c+"38="+ordQty+c+"55="+symbol+c;
		if(side2.equalsIgnoreCase("BUY"))
			str += "18=1"+c;
		else
			str +="18=2"+c;
		str += "10="+super.getChkSum()+c;
		return str.toUpperCase();
	}
	
	public String toString(){
		String str = "cancel ";
		if (side.equals("1"))
			str += "buy order " + origID + " replace with " + side2 + " " + ordQty + " " + symbol + " @ ";
		else
			str += "sell order " + origID + " replace with " + side2 + " " + ordQty + " " + symbol + " @ ";
		if (ordType.equals("1"))
			str += "mkt ";
		else 
			str += price + " ";
		if (timeInForce.equals("1"))
			str += "GTc";
		else 
			str += "day";
		return str.toUpperCase();
	}
	
	public Order genOrd() throws Exception{
		String sk;
		if(side2.equalsIgnoreCase("buy"))
			sk = "1";
		else
			sk = "2";
		char c = 1;
		String s = super.toFixString()+"11="+ciOrdID+c+"40="+ordType+c+"54="+sk+c;
		if (p > 0)
			s += "44="+price+c+"59="+timeInForce+c+"38="+ordQty+c+"55="+symbol+c+"10="+super.getChkSum()+c;
		else
			s += "59="+timeInForce+c+"38="+ordQty+c+"55="+symbol+c+"10="+super.getChkSum()+c;
		Order o = new Order(s);
		return o;
	}
	
	public CancelReq genCancel() throws Exception{
		char c = 1;
		String str = super.toFixString()+"11="+ciOrdID+c+"41="+origID+c+"54="+side+c+"10="+super.getChkSum()+c;
		return new CancelReq(str);
	}
	
	public String getCiOrdID() {
		return ciOrdID;
	}

	public void setCiOrdID(String ciOrdID) {
		this.ciOrdID = ciOrdID;
	}

	public String getSide2() {
		return side2;
	}

	public void setSide2(String side2) {
		this.side2 = side2;
	}

	public String getSide() {
		return side;
	}

	public void setSide(String side) {
		this.side = side;
	}

	public String getOrigID() {
		return origID;
	}

	public void setOrigID(String origID) {
		this.origID = origID;
	}

	public static void main(String[] args){
		CancelRepReq c = new CancelRepReq("cl1", "cancel sell order cl1323 replace with buy 44 ibm @ 99.45");
		StringBuilder str = new StringBuilder(c.toFixString());
		for (int i = 0; i < str.length(); i++)
			if (str.charAt(i) == 1)
				str.replace(i, i+1, "|");
		System.out.println("\n"+str);
		System.out.println(c);
		
		CancelRepReq n = new CancelRepReq(c.toFixString());
		str = new StringBuilder(n.toFixString());
		for (int i = 0; i < str.length(); i++)
			if (str.charAt(i) == 1)
				str.replace(i, i+1, "|");
		System.out.println("\n"+str);
		System.out.println(n);
	}
}
