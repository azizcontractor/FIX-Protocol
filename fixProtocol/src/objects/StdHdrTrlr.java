package objects;

import java.sql.Timestamp;

public class StdHdrTrlr {
	
	private String beginString;
	private int length;
	private String bodyLength;
	private String msgType;
	private String senderID;
	private Timestamp time;
	private String sendTime;
	private int checksum;
	private String chkSum;
	
	public StdHdrTrlr(String name){
		beginString = "FIX.5.0";
		senderID = "CONTRA" + name.toUpperCase();
		time = new Timestamp(System.currentTimeMillis());
		sendTime = time.toString();
		sendTime = sendTime.substring(0,4) + sendTime.substring(5, 7) + sendTime.substring(8, 10) + "-" +sendTime.substring(11,19);
		length = sendTime.length()+3+senderID.length()+3;
		setBodyLength();
		setChecksum();
	}
	
	public StdHdrTrlr(String fixMsg, int i){
		Character c = 1;
		String[] str = fixMsg.split(c.toString()+"|\\=");
		beginString = str[1];
		msgType = str[5];
		senderID = str[7].toUpperCase();
		sendTime = str[9];
		length = sendTime.length()+3+senderID.length()+3;
		setBodyLength();
		setChecksum();
	}
	
	public String con2Str(Double d){
		d = d * 100;
		StringBuilder s = new StringBuilder(d.toString());
		for (int i = 0; i < s.length(); i++){
			if (s.charAt(i) == '.'){
				s.delete(i, s.length());
				s.insert(i-2, '.');
				break;
			}
		}
		return s.toString();
	}
	
	public String toFixString(){
		char c = 1;
		return "8="+beginString+c+"9="+bodyLength+c+"35="+msgType+c+"49="+senderID+c+"52="+sendTime+c;
	}
	
	public int getLength(){
		return this.length;
	}
	
	public void setLength(int l){
		length = l;
		setBodyLength();
		setChecksum();
	}
	
	public void newLength(int len){
		this.length += len;
		setBodyLength();
		setChecksum();
	}

	private void setBodyLength() {
		if(length < 100)
			bodyLength = "0" + length;
		else
			bodyLength = "" + length;
	}

	public String getMsgType() {
		return msgType;
	}

	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}

	public int getChecksum() {
		return checksum;
	}

	public void setChecksum() {
		this.checksum = 256 - this.length%256;
		this.setChkSum();
	}
	
	private void setChkSum(){
		if(checksum < 100)
			chkSum = "0" + checksum;
		else
			chkSum = "" + checksum;
	}
	
	public String getChkSum(){
		return this.chkSum;
	}
	
	public String getSenderID() {
		return senderID;
	}

	public void setSenderID(String senderID) {
		this.senderID = senderID;
	}

	public static void main(String[] args){
		StdHdrTrlr a = new StdHdrTrlr("Aziz");
		StringBuilder str = new StringBuilder(a.toFixString());
		for (int i = 0; i < str.length(); i++)
			if (str.charAt(i) == 1)
				str.replace(i, i+1, "|");
		System.out.println(str);
		
	}
	
}
