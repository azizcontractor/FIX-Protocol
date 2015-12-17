package objects;

import java.util.Random;

public class Quote extends StdHdrTrlr {
	
	private String quoteReqID;
	private static int qid = 56;
	private String quoteID;
	private Integer bidSize;
	private Integer offerSize;
	private double bidPx;
	private double offerPx;
	private String symbol;
	private int length;
	
	public Quote(QuoteReq qr){
		super("SV1");
		super.setMsgType("S");
		Random r = new Random(System.currentTimeMillis());
		quoteID = "SV1"+qid;
		qid++;
		bidSize = (r.nextInt(10) + 1)*100;
		offerSize = (r.nextInt(10)+1)*100;
		bidPx = r.nextDouble()*150;
		offerPx = r.nextDouble()*150;
		while(offerPx > (bidPx+5.0))
			offerPx -=  (float) 5;
		while(offerPx < (bidPx-5.0))
			offerPx += (float) 5;
		symbol = qr.getSymbol();
		quoteReqID = qr.getQuoteReqID();
		length = quoteReqID.length() + 4 + quoteID.length() + 4 + bidSize.toString().length() + 4 + offerSize.toString().length() + 4
				+ super.con2Str(bidPx).length() + 4 + super.con2Str(offerPx).length() + 4 + symbol.length() + 3;
		super.newLength(length);
	}
	
	public Quote(String fixString) throws Exception{
		super(fixString.substring(0,55),1);
		Character c = 1;
		String[] str = fixString.substring(55).split(c.toString()+"|\\=");
		quoteReqID = str[1];
		quoteID = str[3];
		symbol = str[5];
		bidSize = Integer.parseInt(str[7]);
		offerSize = Integer.parseInt(str[9]);
		bidPx = Double.parseDouble(str[11]);
		offerPx = Double.parseDouble(str[13]);
		length = quoteReqID.length() + 4 + quoteID.length() + 4 + bidSize.toString().length() + 4 + offerSize.toString().length() + 4
				+ super.con2Str(bidPx).length() + 4 + super.con2Str(offerPx).length() + 4 + symbol.length() + 3;
		super.newLength(length);
		if (!super.getChkSum().equals(str[15]))
			throw new Exception("Invalid FIX String");
	}
	
	public String toFixString(){
		char c = 1;
		return super.toFixString()+"131="+quoteReqID+c+"117="+quoteID+c+"55="+symbol+c+"134="+bidSize+c+"135="+offerSize+c+
				"132="+super.con2Str(bidPx)+c+"133="+super.con2Str(offerPx)+c+"10="+super.getChkSum()+c;
	}
	
	public String toString(){
		return  "QUOTE " + super.getSenderID() + " " +symbol + " " +  + bidSize + " X " + offerSize + " @ " + super.con2Str(bidPx) + " X " + super.con2Str(offerPx);
	}
	
	public static void main(String[] args){
		QuoteReq qr = new QuoteReq("CL1", "Quote Request for ibm");
		StringBuilder str = new StringBuilder(qr.toFixString());
		for (int i = 0; i < str.length(); i++)
			if (str.charAt(i) == 1)
				str.replace(i, i+1, "|");
		System.out.println(str);
		System.out.println(qr); 
		Quote q = new Quote(qr);
		str = new StringBuilder(q.toFixString());
		for (int i = 0; i < str.length(); i++)
			if (str.charAt(i) == 1)
				str.replace(i, i+1, "|");
		System.out.println("\n\n"+str);
		System.out.println(q); 	
		try {
			q = new Quote(q.toFixString());
		} catch (Exception e) {
			e.getMessage();
		}
		str = new StringBuilder(q.toFixString());
		for (int i = 0; i < str.length(); i++)
			if (str.charAt(i) == 1)
				str.replace(i, i+1, "|");
		System.out.println("\n\n"+str);
		System.out.println(q);
		
	}
	
	

}
