package networking;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
import objects.*;

public class Client {

	public static void main(String[] args) throws Exception {
		
		String serverIP = Server.getIp();
		int port = 6667;
		Socket socket = new Socket(serverIP, port);
		Scanner kb = new Scanner(System.in);
		System.out.print(">Enter Firm Name\n>");
		String prompt = kb.nextLine().toUpperCase();
		String prefix = "";
		while(prefix.length() != 3){
			System.out.print(prompt+">Enter 3 letter Prefix\n"+ prompt+">");
			prefix = kb.nextLine().toUpperCase();
		}
		BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
		out.println(prompt);
		out.flush();
		String message, input = null;
		String[] str, str1; 
		StdHdrTrlr z, a;
		StringBuilder fixMsg;
		char c = 1;
		while(true){
			if ((message = in.readLine()) != null){
				str = message.split(c+"|\\=");
				if (str.length > 5 && str[5].equals("S")){
					a = new Quote(message);
					System.out.println("CONTRASV1>" + a + "\n");
					message = null;
				}
				else if (str.length > 13 && str[5].equals("8") && str[13].equals("I")){
					a = new OrdStatExec(message);
					System.out.println("CONTRASV1>" + a + "\n");
					message = null;
				}
				else if (str.length > 13 && str[5].equals("8") && (str[13].equals("8") || str[13].equals("4"))){
					a = new CancelExec(message);
					System.out.println("CONTRASV1>" + a + "\n");
					message = null;
				}
				else if (str.length > 13 && str[5].equals("8") && str[13].equals("5")){
					a = new CancelRepExec(message,1);
					System.out.println("CONTRASV1>" + a + "\n");
					message = null;
				}
				else if (str.length > 5 && str[5].equals("8")){
					a = new ExecOrd(message, 1);
					System.out.println("CONTRASV1>" + a + "\n");
					message = null;
				}
				else if (str[0].equals("911")){
					if((message = in.readLine()) != null){
						a = new ExecOrd(message,1);
						System.out.println("CONTRASV1>" + a + "\n");
					}
					continue;
				}
				else if (message.length() > 4 && message.substring(0, 3).equalsIgnoreCase("all")){
					System.out.print("CONTRASV1>");
					String[] disp = message.split(",");
					for(String g: disp)
						System.out.println(g);
					message = null;
					System.out.println();
				}
				if (message != null){
					System.out.println("CONTRASV1>"+ message + "\n");
				}
				boolean retry;
				do{
					System.out.print(prompt+">");
					input = kb.nextLine();
					input.toUpperCase();
					if(input.equals("exit")){
						out.println(prefix + " Left");
						out.flush();
						socket.close();
						in.close();
						kb.close();
						System.exit(0);
					}
					if (input.substring(0, 4).equalsIgnoreCase("fill"))
						System.out.println();
					str = input.split(" ");
					if ((str[0].equalsIgnoreCase("buy") || str[0].equalsIgnoreCase("sell")) && (str.length < 5 || !chkInt(str[1]) || !str[3].equalsIgnoreCase("@") ||
							!(str[4].equalsIgnoreCase("mkt") || chkDouble(str[4])))){
						System.out.println(prompt+">Invalid Command!!!");
						retry = true;
					}
					else if (str[0].equalsIgnoreCase("quote") && (str.length < 3 || !str[1].equalsIgnoreCase("req"))){
						System.out.println(prompt+">Invalid Command!!!");
						retry = true;
					}
					else if (str.length > 4 && str[0].equalsIgnoreCase("cancel") && (!(str[1].equalsIgnoreCase("buy") || str[1].equalsIgnoreCase("sell")) || 
							str.length < 11 || !str[2].equalsIgnoreCase("order") || !str[4].equalsIgnoreCase("replace") || !str[5].equalsIgnoreCase("with")
							|| !(str[6].equalsIgnoreCase("buy") || str[6].equalsIgnoreCase("sell")) ||  !chkInt(str[7]) || !str[9].equalsIgnoreCase("@") ||
							!(str[10].equalsIgnoreCase("mkt") || chkDouble(str[10])))){
						System.out.println(prompt+">Invalid Command!!!");
						retry = true;
					}
					else if (str[0].equalsIgnoreCase("cancel") && (!(str[1].equalsIgnoreCase("buy") || str[1].equalsIgnoreCase("sell")) || str.length < 4 ||
							!str[2].equalsIgnoreCase("order"))){
						System.out.println(prompt+">Invalid Command!!!");
						retry = true;
					}
					else if (str[0].equals("show") && !str[1].equalsIgnoreCase("All")){
						System.out.println(prompt+">Invalid Command!!!");
						retry = true;
					}
					else if (str.length != 2 && str[0].equalsIgnoreCase("status")){
						System.out.println(prompt+">Invalid Command!!!");
						retry = true;
					}
					else{
						retry = false;
					}
				}while(retry);
				if (str[0].equalsIgnoreCase("QUOTE")){
					z = new QuoteReq(prefix, input);
					fixMsg = new StringBuilder(z.toFixString());
					for (int i = 0; i < fixMsg.length(); i++)
						if (fixMsg.charAt(i) == 1)
							fixMsg.replace(i, i+1, "|");
					System.out.println("\n" + prompt+"> FIX Message - " + fixMsg + "\n");
					out.println(z.toFixString());
					out.flush();
					input = null;
				}
				else if (str[0].equalsIgnoreCase("BUY") || str[0].equalsIgnoreCase("SELL")){
					z = new Order (prefix, input);
					fixMsg = new StringBuilder(z.toFixString());
					for (int i = 0; i < fixMsg.length(); i++)
						if (fixMsg.charAt(i) == 1)
							fixMsg.replace(i, i+1, "|");
					System.out.println("\n" + prompt+"> FIX Message - " + fixMsg + "\n");
					out.println(z.toFixString());
					out.flush();
					input = null;
				}
				else if (str.length > 5 && str[0].equalsIgnoreCase("cancel") && str[4].equalsIgnoreCase("replace")){
					z = new CancelRepReq(prefix, input);
					fixMsg = new StringBuilder(z.toFixString());
					for (int i = 0; i < fixMsg.length(); i++)
						if (fixMsg.charAt(i) == 1)
							fixMsg.replace(i, i+1, "|");
					System.out.println("\n" + prompt+"> FIX Message - " + fixMsg + "\n");
					out.println(z.toFixString());
					out.flush();
					input = null;
				}
				else if (str[0].equalsIgnoreCase("cancel")){
					z = new CancelReq (prefix, input);
					fixMsg = new StringBuilder(z.toFixString());
					for (int i = 0; i < fixMsg.length(); i++)
						if (fixMsg.charAt(i) == 1)
							fixMsg.replace(i, i+1, "|");
					System.out.println("\n" + prompt+"> FIX Message - " + fixMsg + "\n");
					out.println(z.toFixString());
					out.flush();
					input = null;
				}
				else if (str[0].equalsIgnoreCase("status")){
					z = new OrdStatReq(prefix, input);
					fixMsg = new StringBuilder(z.toFixString());
					for (int i = 0; i < fixMsg.length(); i++)
						if (fixMsg.charAt(i) == 1)
							fixMsg.replace(i, i+1, "|");
					System.out.println("\n" + prompt+"> FIX Message - " + fixMsg + "\n");
					out.println(z.toFixString());
					out.flush();
					input = null;
				}
				if (input != null){
					out.println(input);
					out.flush();
				}
			}
		}
	}
	
	public static boolean chkInt(String s){
		try{
			Integer.parseInt(s);
			return true;
		}catch(Exception e){
			return false;
		}
	}
	
	public static boolean chkDouble(String s){
		try{
			Double.parseDouble(s);
			return true;
		}catch(Exception e){
			return false;
		}
	}

}
