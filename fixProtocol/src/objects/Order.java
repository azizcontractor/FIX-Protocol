package objects;


public class Order extends StdHdrTrlr {
	
	private static int ordid = 235;
	private String ciOrdID;
	private String side;
	private double price;
	private String timeInForce;
	private Integer ordQty;
	private String symbol;
	private String ordType;
	private int length;
	private int life = 5;
	private String stat;
	
	public Order(String name, String msg){
		super(name);
		super.setMsgType("D");
		String[] str = msg.split(" ");
		ciOrdID = name.toUpperCase() + ordid;
		ordid++;
		if ((str.length >=5 && str[4].equalsIgnoreCase("GTC")) || (str.length >=6 && str[5].equalsIgnoreCase("GTC"))){
			timeInForce = "1";
		}
		else
			timeInForce = "0";
		ordQty = Integer.parseInt(str[1]);
		symbol = str[2].toUpperCase();
		if (str[0].equalsIgnoreCase("BUY"))
			side = "1";
		else if (str[0].equalsIgnoreCase("SELL")){
			side = "2";
		}
		else
			side = "1";
		if (str.length > 4 && !str[4].equalsIgnoreCase("MKT"))
			ordType = "2";
		else
			ordType = "1";
		if (str.length > 4 && ordType.equalsIgnoreCase("2"))
			price = Double.parseDouble(str[4]);
		else
			price = 0;
		length = 3 + ciOrdID.length() + 3 + ordType.length() + 3 + side.length();
		if (price > 0)
			length += 3 + super.con2Str(price).length() + 3 + timeInForce.length() +
				3 + ordQty.toString().length() + 3 + symbol.length();
		else
			length +=3 + timeInForce.length() +	3 + ordQty.toString().length() + 3 + symbol.length();
		super.newLength(length);
	}
	
	public Order(String fixMsg) throws Exception{
		super(fixMsg.substring(0,55),1);
		char c = 1;
		String s[] = fixMsg.substring(55).split(c+"|\\=");
		ciOrdID = s[1];
		ordType = s[3];
		side = s[5];
		if (ordType.equals("2")){
			price = Double.parseDouble(s[7]);
			timeInForce = s[9];
			ordQty = Integer.parseInt(s[11]);
			symbol = s[13];
		}
		else{
			price = 0;
			timeInForce = s[7];
			ordQty = Integer.parseInt(s[9]);
			symbol = s[11];
		}
		length = 3 + ciOrdID.length() + 3 + ordType.length() + 3 + side.length();
		if (price > 0)
			length += 3 + super.con2Str(price).length() + 3 + timeInForce.length() +
				3 + ordQty.toString().length() + 3 + symbol.length();
		else
			length +=3 + timeInForce.length() +	3 + ordQty.toString().length() + 3 + symbol.length();
		super.newLength(length);
		if ((price > 0 && !super.getChkSum().equalsIgnoreCase(s[15])) || (price == 0 && !super.getChkSum().equalsIgnoreCase(s[13])))
			if (super.getMsgType().equals("D"))
				throw new Exception("Invalid FIX String");
	}
	
	protected Order(String fixMsg, int w) throws Exception{
		super(fixMsg.substring(0,55),1);
		char c = 1;
		String s[] = fixMsg.substring(55).split(c+"|\\=");
		ciOrdID = s[1];
		ordType = s[3];
		side = s[5];
		price = Double.parseDouble(s[7]);
		timeInForce = s[9];
		ordQty = Integer.parseInt(s[11]);
		symbol = s[13];
		length = 3 + ciOrdID.length() + 3 + ordType.length() + 3 + side.length();
		if (price > 0)
			length += 3 + super.con2Str(price).length() + 3 + timeInForce.length() +
				3 + ordQty.toString().length() + 3 + symbol.length();
		else
			length +=3 + timeInForce.length() +	3 + ordQty.toString().length() + 3 + symbol.length();
		super.newLength(length);
		if ((price > 0 && !super.getChkSum().equalsIgnoreCase(s[15])) || (price == 0 && !super.getChkSum().equalsIgnoreCase(s[13])))
			if (super.getMsgType().equals("D"))
				throw new Exception("Invalid FIX String");
	}
	
	public void age(){
		if (this.timeInForce.equals("0"))
			life--;
	}
	
	public int getLife(){
		return life;
	}
	
	public String getOrdType() {
		return ordType;
	}

	public double getPrice() {
		return price;
	}

	public Integer getOrdQty() {
		return ordQty;
	}

	public void setOrdQty(Integer ordQty) {
		this.ordQty = ordQty;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public void setOrdType(String ordType) {
		this.ordType = ordType;
	}

	public String getStat() {
		return stat;
	}

	public void setStat(String stat) {
		this.stat = stat;
	}

	public String toFixString(){
		char c = 1;
		String s = super.toFixString()+"11="+ciOrdID+c+"40="+ordType+c+"54="+side+c;
		if (price > 0)
			s += "44="+super.con2Str(price)+c+"59="+timeInForce+c+"38="+ordQty+c+"55="+symbol+c+"10="+super.getChkSum()+c;
		else
			s += "59="+timeInForce+c+"38="+ordQty+c+"55="+symbol+c+"10="+super.getChkSum()+c;
		return s;
	}
	
	public String toString(){
		String s="";
		if (side.equalsIgnoreCase("1"))
			s += "BUY ";
		else
			s += "SELL ";
		s += ordQty + " " + symbol + " @ ";
		if (price == 0)
			s += "MKT ";
		else{
			s += super.con2Str(price) + " ";
		}
		if (timeInForce.equalsIgnoreCase("1"))
			s += "GTC";
		else
			s += "DAY";
		return s;
	}
	
	public String toString(int i){
		String oStat = "0", sd;
		if (side.equalsIgnoreCase("1"))
			sd = "BUY ";
		else
			sd = "SELL";
		if(stat.equalsIgnoreCase("2"))
			oStat = "Filled";
		else if (stat.equalsIgnoreCase("4"))
			oStat = "Canceled";
		else if (stat.equalsIgnoreCase("C"))
			oStat = "Expired";
		else if (stat.equalsIgnoreCase("5"))
			oStat = "Replaced";
		else if (stat.equalsIgnoreCase("A"))
			oStat = "Pending";
		String str = sd + " order " + ciOrdID + " Status " + oStat;
		return str.toUpperCase();
	}
	
	public String getCiOrdID() {
		return ciOrdID;
	}

	public void setCiOrdID(String ciOrdID) {
		this.ciOrdID = ciOrdID;
	}

	public String getSide() {
		return side;
	}

	public void setSide(String side) {
		this.side = side;
	}

	public String getTimeInForce() {
		return timeInForce;
	}

	public void setTimeInForce(String timeInForce) {
		this.timeInForce = timeInForce;
	}

	public static void main(String[] args){
		Order o = new Order("CL1", "sell 100 ibm @ mkt ");
		StringBuilder str = new StringBuilder(o.toFixString());
		for (int i = 0; i < str.length(); i++)
			if (str.charAt(i) == 1)
				str.replace(i, i+1, "|");
		System.out.println(str);
		System.out.println(o);
		try {
			o = new Order(o.toFixString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.getMessage();
		}
		str = new StringBuilder(o.toFixString());
		for (int i = 0; i < str.length(); i++)
			if (str.charAt(i) == 1)
				str.replace(i, i+1, "|");
		System.out.println("\n"+str);
		System.out.println(o);
		o = new Order("cl1", "buy 11 aapl @ 123.23 GTC");
		str = new StringBuilder(o.toFixString());
		for (int i = 0; i < str.length(); i++)
			if (str.charAt(i) == 1)
				str.replace(i, i+1, "|");
		System.out.println("\n\n"+str);
		System.out.println(o);
		try {
			o = new Order(o.toFixString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.getMessage();
		}
		str = new StringBuilder(o.toFixString());
		for (int i = 0; i < str.length(); i++)
			if (str.charAt(i) == 1)
				str.replace(i, i+1, "|");
		System.out.println("\n"+str);
		System.out.println(o);
	}

}
