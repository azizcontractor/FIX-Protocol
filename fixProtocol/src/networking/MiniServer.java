package networking;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Console;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.*;
import java.util.ArrayList;
import java.util.Scanner;
import objects.*;

public class MiniServer extends Thread {

	private Socket socket = null;
	private static ArrayList<Order> orders = new ArrayList<Order>();
	private ArrayList<Order> trans = new ArrayList<Order>();

    public MiniServer(Socket socket) {

        super("MiniServer");
        this.socket = socket;

    }

    public void run(){
    	try{
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
			Scanner kb = new Scanner(System.in);
			String clName = null;
			String message, input;
			if((message = in.readLine()) != null){
				clName = message;
				System.out.println("CONTRASV1>Connected to " + clName);
			}
			out.println("Connected to " + Server.getIp());
			out.flush();
			StringBuilder fixMsg;
			StdHdrTrlr z, a;
			String[] str;
			String emg = null;
			char c = 1;
			Order m;
			while(true){
				if ((message = in.readLine()) != null){
					if (orders.size() != 0){
						Order o;
						for (int i = 0; i < orders.size(); i++){
							o = orders.get(i);
							o.age();
							if(o.getLife() <= 0){
								a = new ExecOrd(o,1);
								orders.remove(i);
								o.setStat("C");
								emg = a.toFixString();
							}
						}
						if (emg != null){
							String s = "911\n"+emg;
							out.println(s);
							out.flush();
						}
						emg = null;
					}
					str = message.split(c+"|\\=|\\ ");
					if (str.length > 5 && str[5].equals("R")){
						z = new QuoteReq(message);
						System.out.println("\n" + clName + ">" + z);
						a = new Quote((QuoteReq) z);
						fixMsg = new StringBuilder(a.toFixString());
						for (int i = 0; i < fixMsg.length(); i++)
							if (fixMsg.charAt(i) == 1)
								fixMsg.replace(i, i+1, "|");
						System.out.println("\nCONTRASV1>" + fixMsg + "\n");
						out.println(a.toFixString());
						out.flush();
						message = null;
					}
					else if (str.length > 5 && str[5].equals("D")){
						z = new Order(message);
						m = (Order) z;
						System.out.println("\n" + clName + ">" + z);
						if (((Order) z).getOrdType().equals("1")){
							a = new ExecOrd((Order) z);
							m.setStat("2");
						}
						else{
							a = new ExecOrd(z.toFixString());
							orders.add((Order) z);
							m.setStat("A");
						}
						fixMsg = new StringBuilder(a.toFixString());
						for (int i = 0; i < fixMsg.length(); i++)
							if (fixMsg.charAt(i) == 1)
								fixMsg.replace(i, i+1, "|");
						System.out.println("\nCONTRASV1>" + fixMsg + "\n");
						out.println(a.toFixString());
						out.flush();
						message = null;
						trans.add(m);
					}
					else if(message.equalsIgnoreCase("Show all")){
						String p = "All Transactions: ,";
						for (Order q: trans){
							p += q.toString(1) + " ,";
						}
						message = null;
						out.println(p);
						out.flush();
					}
					else if (str[0].equalsIgnoreCase("Fill") && !orders.isEmpty()){
						System.out.println("\n" + clName + ">" + message.toUpperCase());
						String id = str[1];
						Order o;
						for(int i = 0; i < orders.size(); i++){
							o = orders.get(i);
							if (o.getCiOrdID().equalsIgnoreCase(id)){
								a = new ExecOrd(o);
								fixMsg = new StringBuilder(a.toFixString());
								for (int k = 0; k < fixMsg.length(); k++)
									if (fixMsg.charAt(k) == 1)
										fixMsg.replace(k, k+1, "|");
								System.out.println("\nCONTRASV1>" + fixMsg + "\n");
								out.println(a.toFixString());
								out.flush();
								o.setStat("2");
								orders.remove(i);
								break;
							}
							else if (i == orders.size()-1){
								out.println("Invalid Order ID!!");
								out.flush();
							}
						}
						message = null;
					}
					else if (str[0].equalsIgnoreCase("fill") && orders.isEmpty()){
						out.println("NO PENDING ORDERS");
						out.flush();
						message = null;
					}
					else if (str.length > 5 && str[5].equalsIgnoreCase("H")){
						OrdStatReq osr = new OrdStatReq(message);
						System.out.println(clName + ">" + osr);
						OrdStatExec ose;
						String id = osr.getClordID();
						int i = 0;
						if(trans.isEmpty()){
							out.println("Invalid Order ID!!");
							out.flush();
						}
						else{
							for(Order o: trans){
								if(o.getCiOrdID().equalsIgnoreCase(id)){
									ose = new OrdStatExec(osr);
									ose.setOrdStat(o.getStat());
									out.println(ose.toFixString());
									out.flush();
									fixMsg = new StringBuilder(ose.toFixString());
									for (int k = 0; k < fixMsg.length(); k++)
										if (fixMsg.charAt(k) == 1)
											fixMsg.replace(k, k+1, "|");
									System.out.println("\nCONTRASV1>" + fixMsg + "\n");
									break;
								}
								else if (i == trans.size()-1){
									out.println("Invalid Order ID!!");
									out.flush();
								}
								i++;
							}
						}
						message = null;
					}
					else if (str.length > 5 && str[5].equalsIgnoreCase("F")){
						CancelReq cq = new CancelReq(message);
						System.out.println(clName + ">" + cq);
						CancelExec ce = new CancelExec(cq);
						if(!orders.isEmpty()){
							Order o;
							for(int i = 0; i < orders.size(); i++){
								o = orders.get(i);
								if (o.getSide().equalsIgnoreCase(cq.getSide()) && o.getCiOrdID().equalsIgnoreCase(cq.getOrigID())){
									ce.setOrdStat("4");
									o.setStat("4");
									orders.remove(i);
									break;
								}
								else if(i == orders.size()-1){
									ce.setOrdStat("8");
								}
							}
						}
						else{
							ce.setOrdStat("8");
						}
						fixMsg = new StringBuilder(ce.toFixString());
						for (int i = 0; i < fixMsg.length(); i++)
							if (fixMsg.charAt(i) == 1)
								fixMsg.replace(i, i+1, "|");
						System.out.println("\nCONTRASV1>" + fixMsg + "\n");
						out.println(ce.toFixString());
						out.flush();
						message = null;	
					}
					else if (str.length > 5 && str[5].equalsIgnoreCase("G")){
						CancelRepReq crr = new CancelRepReq(message);
						System.out.println( clName + ">" +crr);
						CancelReq cr = crr.genCancel();
						CancelExec ce = new CancelExec(cr);
						Order ord = crr.genOrd();
						ExecOrd oe;
						CancelRepExec cre;
						if(!orders.isEmpty()){
							Order o;
							for(int i = 0; i < orders.size(); i++){
								o = orders.get(i);
								if (o.getSide().equalsIgnoreCase(cr.getSide()) && o.getCiOrdID().equalsIgnoreCase(cr.getOrigID())){
									ce.setOrdStat("5");
									o.setStat("5");
									orders.remove(i);
									break;
								}
								else if(i == orders.size()-1){
									ce.setOrdStat("8");
								}
							}
						}
						else{
							ce.setOrdStat("8");
						}
						if (ce.getOrdStat().equals("5")){
							oe = new ExecOrd(ord);
							orders.add(ord);
							ord.setStat("A");
							trans.add(ord);
							cre = new CancelRepExec(oe,ce);
						}
						else{
							cre = new CancelRepExec(ce.toFixString());
						}
						fixMsg = new StringBuilder(cre.toFixString());
						for (int i = 0; i < fixMsg.length(); i++)
							if (fixMsg.charAt(i) == 1)
								fixMsg.replace(i, i+1, "|");
						System.out.println("\nCONTRASV1>" + fixMsg + "\n");
						out.println(cre.toFixString());
						out.flush();
						message = null;
					}
					if (message != null){
						System.out.println(clName + ">" + message);
						System.out.print("CONTRASV1>");
						input = kb.nextLine();
						if(input.equals("exit")){
							out.println("Server Left");
							out.flush();
							socket.close();
							in.close();
							kb.close();
							System.exit(0);
						}							
						out.println(input);
						out.flush();
					}
				}
				if(!orders.isEmpty()){
					System.out.println("\nPending Orders:");
					for(Order o: orders)
						System.out.println(o.getCiOrdID() + ": " + o + ", Life = " + o.getLife());
				}
				else{
					System.out.println("\nPending Orders:\nNONE");
				}
			}
    	}catch(Exception e){
    		e.getStackTrace();
    	}
		
    }
            //implement your methods here
}
