package objects;

public class CancelRepExec extends CancelRepReq {
	
	private String execID;
	private String ordStat;
	private int length;
	private String cFix;
	
	public CancelRepExec(String fixMsg){
		super(fixMsg.substring(0,55)+fixMsg.substring(76), 'c');
		cFix = fixMsg;
		char c = 1;
		String[] str = fixMsg.substring(55).split(c+"|\\=");
		execID = str[1];
		ordStat = str[3];
		length = 6 + execID.length() + ordStat.length();
		super.newLength(length);
	}
	
	public CancelRepExec(ExecOrd oe, CancelExec ce){
		super(oe, ce);
		execID = oe.getExecID();
		ordStat = "5";
		length = 6 + execID.length() + ordStat.length();
		super.newLength(length);
	}
	
	public CancelRepExec(String fixMsg, int i){
		super(fixMsg, 1);
		char c = 1;
		String[] str = fixMsg.substring(55, 77).split(c+"|\\=");
		execID = str[1];
		ordStat = str[3];
		length = 6 + execID.length() + ordStat.length();
		super.newLength(length);
	}
	
	public String toFixString(){
		String str;
		StringBuilder s;
		char c = 1;
		if (ordStat.equals("8")){
			str = cFix;
			return str.toUpperCase();
		}
		else{
			s = new StringBuilder(super.toFixString());
			s.insert(55, "17="+execID+c+"39="+ordStat+c);
			return s.toString().toUpperCase();
		}
		
	}
	
	public String toString(){
		String str, sd,sd1;
		if (ordStat.equals("8"))
			str = "cancel/replace rejected for order " + super.getOrigID();
		else{
			if(super.getSide().equals("2"))
				sd = "SELL";
			else 
				sd = "BUY";
			if(super.getSide2().equals("2"))
				sd1 = "SELL";
			else 
				sd1 = "BUY";
			str = sd + " Order " + super.getOrigID() + " canceled replaced with new pending " + sd1 + " order " + super.getCiOrdID();
		}
		return str.toUpperCase();
	}
	
	public static void main(String[] args){
		try {
			CancelExec c= new CancelExec(new CancelReq("cl1", "cancel buy order cl1435"));
			c.setOrdStat("8");
			CancelRepExec cre = new CancelRepExec(c.toFixString());
			StringBuilder str = new StringBuilder(cre.toFixString());
			for (int i = 0; i < str.length(); i++){
				if (str.charAt(i) == 1)
					str.replace(i, i+1, "|");
			}
			System.out.println(str);
			System.out.println(cre);
			
			ExecOrd oe = new ExecOrd(new Order("cl1", "sell 25 ibm @ mkt"));
			
			cre = new CancelRepExec(oe, c);
			str = new StringBuilder(cre.toFixString());
			for (int i = 0; i < str.length(); i++){
				if (str.charAt(i) == 1)
					str.replace(i, i+1, "|");
			}
			System.out.println(str);
			System.out.println(cre);
			
			cre = new CancelRepExec(cre.toFixString(),1);
			str = new StringBuilder(cre.toFixString());
			for (int i = 0; i < str.length(); i++){
				if (str.charAt(i) == 1)
					str.replace(i, i+1, "|");
			}
			System.out.println(str);
			System.out.println(cre);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
