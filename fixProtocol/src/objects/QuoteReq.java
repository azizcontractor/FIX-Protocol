package objects;

public class QuoteReq extends StdHdrTrlr {
	
	private String quoteReqID;
	private static int qrID = 71;
	private String symbol;
	private int length;
	
	public QuoteReq(String name, String message){
		super(name);
		String[] s = message.split(" ");
		super.setMsgType("R");
		symbol = s[2].trim().toUpperCase();
		quoteReqID = name.toUpperCase() + qrID;
		qrID++;
		length = symbol.length() + 3 + 4 + quoteReqID.length();
		super.newLength(length);
	}
	
	public QuoteReq(String fixMsg) throws Exception{
		super(fixMsg.substring(0, 55), 1);
		Character c = 1;
		String[] str = fixMsg.substring(55).split(c+"|\\=");
		quoteReqID = str[1];
		symbol = str[3];
		length = symbol.length() + 3 + 4 + quoteReqID.length();
		super.newLength(length);
		if (!super.getChkSum().equals(str[5]))
			throw new Exception("Invalid Fix String");
	}
	
	public String getQuoteReqID() {
		return quoteReqID;
	}
	
	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public String toFixString(){
		char c = 1;
		return super.toFixString()+"131="+quoteReqID+c+"55="+symbol+c+"10="+super.getChkSum()+c;
	}
	
	public String toString(){
		return "QUOTE REQ " + symbol;
	}
	
	public static void main(String[] args){
		QuoteReq q = new QuoteReq("CL1", "Quote Request for APPL");
		StringBuilder str = new StringBuilder(q.toFixString());
		for (int i = 0; i < str.length(); i++)
			if (str.charAt(i) == 1)
				str.replace(i, i+1, "|");
		System.out.println(str);
		System.out.println(q);
		try{
			q = new QuoteReq(q.toFixString());
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
		str = new StringBuilder(q.toFixString());
		for (int i = 0; i < str.length(); i++)
			if (str.charAt(i) == 1)
				str.replace(i, i+1, "|");
		System.out.println("\n\n"+str);
		System.out.println(q);
	}

}
