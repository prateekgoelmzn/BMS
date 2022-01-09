package com.pg.bms;

import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

import org.h2.tools.RunScript;

public class BankDemo {
	static Scanner s1 = new Scanner(System.in);
	static Connection con = null;

	static {
		BankDemo.initConnection();
		try {
			RunScript.execute(con, new FileReader("src/main/resources/sqlscript.sql"));
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public static Connection initConnection() {
		try {
			Class.forName("org.h2.Driver");
			con = DriverManager.getConnection("jdbc:h2:~/bankdata?user=sa&password=");
			return con;
		} catch (Exception e) {
			System.out.println(e);
			return null;
		}
	}

	public static void openAccount() {
		try {
			System.out.println("\tEnter Account number");
			int acno = s1.nextInt();
			System.out.println("\tEnter your Name");
			String cname = s1.next();
			System.out.println("\tEnter account type");
			String actype = s1.next();
			System.out.println("\tEnter amount");
			int amt = s1.nextInt();
			PreparedStatement ps = con.prepareStatement("insert into bank values(?,?,?,?)");
			ps.setInt(1, acno);
			ps.setString(2, cname);
			ps.setString(3, actype);
			ps.setInt(4, amt);
			int a = ps.executeUpdate();
			if (a == 1) {
				System.out.println("\t\tRecord added");
			} else {
				System.out.println("\t\tRecord not added");
			}
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public static void depositMoney() {
		try {
			System.out.println("\tenter account number to be deposited");
			int acno = s1.nextInt();
			System.out.println("\tenter amount to be deposited");
			int dm = s1.nextInt();
			PreparedStatement ps = con.prepareStatement("update bank set amt=amt+? where acno=?");
			ps.setInt(1, dm);
			ps.setInt(2, acno);
			int a = ps.executeUpdate();
			if (a == 1) {
				System.out.println("\t\tMoney deposited");
			} else {
				System.out.println("\t\tMoney Not deposited");
			}
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public static void withDraw() {
		try {
			System.out.println("\tenter account number to be withdraw");
			int acno = s1.nextInt();
			System.out.println("\tEnter amount to be witdraw");
			int wamt = s1.nextInt();
			PreparedStatement ps = con.prepareStatement("update bank set amt=amt-? where acno=?");
			ps.setInt(1, wamt);
			ps.setInt(2, acno);
			int a = ps.executeUpdate();
			if (a == 1) {
				System.out.println("\t\tmoney is withdraw");
			} else {
				System.out.println("\t\tmoney is not withdraw");
			}
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public static void displayAccountInfo() {
		try {
			System.out.println("\tenter account number to be displayed");
			int acno1 = s1.nextInt();
			PreparedStatement ps = con.prepareStatement("select * from bank where acno=?");
			ps.setInt(1, acno1);
			ResultSet rs = ps.executeQuery();
			boolean infoFound =  false;
			while (rs.next()) {
				int acno2 = rs.getInt(1);
				String cname = rs.getString(2);
				String actype = rs.getString(3);
				int amt = rs.getInt(4);
				System.out.println("\t\tAccount number :\t" + acno2);
				System.out.println("\t\tCustomer name :\t" + cname);
				System.out.println("\t\tAccount type :\t" + actype);
				System.out.println("\t\tBalance :\t" + amt);
				if(infoFound!=true) infoFound = true;
			}
			if(!infoFound) {
				System.out.println("\tNo entry found for account number : "+acno1);
			}
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public static void closeAccount() {
		try {
			System.out.println("\tEnter account number to be close");
			int acno4 = s1.nextInt();
			PreparedStatement ps = con.prepareStatement("delete from bank where acno=?");
			ps.setInt(1, acno4);
			int a = ps.executeUpdate();
			if (a == 1) {
				System.out.println(acno4 + "\t is deleted");
			} else {
				System.out.println(acno4 + "\tis not deleted");
			}
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public static void transferMoney() {
		try {
			System.out.println("\tenter your account number");
			int acno1 = s1.nextInt();
			System.out.println("\tenter the account number to which you transfer money");
			int acno2 = s1.nextInt();
			System.out.println("\tenter amount to transfer");
			int amt = s1.nextInt();
			PreparedStatement ps1 = con.prepareStatement("update bank set amt=amt-? where acno=?");
			ps1.setInt(1, amt);
			ps1.setInt(2, acno1);
			int a1 = ps1.executeUpdate();
			PreparedStatement ps2 = con.prepareStatement("update bank set amt=amt+? where acno=?");
			ps2.setInt(1, amt);
			ps2.setInt(2, acno2);
			int a2 = ps2.executeUpdate();
			if (a1 == 1 && a2 == 1) {
				System.out.println("\tmoney is transefered");
			} else {
				System.out.println("\tmoney is not transefered");
			}
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public static void enquiry() {
		try {
			PreparedStatement ps = con.prepareStatement("select * from bank");
			ResultSet rs = ps.executeQuery();
			boolean dataFound = false; 
			while (rs.next()) {
				int acno1 = rs.getInt(1);
				String cuname = rs.getString(2);
				String actype = rs.getString(3);
				int amt = rs.getInt(4);
				System.out.println("account number is " + acno1);
				System.out.println("customer name is " + cuname);
				System.out.println("account type is " + actype);
				System.out.println("balance is " + amt);
				System.out.println("\n===================================\n");
				if(!dataFound) dataFound = true;
			}
			if(!dataFound) {
				System.out.println("\tNo entry found in DB");
			}
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public static void main(String ar[]) {

		String k = "0";
		do {
			System.out.println("\t\t\t\tBANK MENU\t\t\t\t");
			System.out.println("\t1.Open Account");
			System.out.println("\t2.Deposit Money");
			System.out.println("\t3.WithDraw");
			System.out.println("\t4.Transfer");
			System.out.println("\t5.Display Account");
			System.out.println("\t6.Enquery Account");
			System.out.println("\t7.Close Account");
			System.out.println("\t8.Exit");
			System.out.println("\tEnter Your Choice");
			int ch = s1.nextInt();
			switch (ch) {
			case 1:
				openAccount();
				break;
			case 2:
				depositMoney();
				break;
			case 3:
				withDraw();
				break;
			case 4:
				transferMoney();
				break;
			case 5:
				displayAccountInfo();
				break;
			case 6:
				enquiry();
				break;
			case 7:
				closeAccount();
				break;
			default:
				System.out.println("Invalid choice");
			}
			System.out.println("\t\t\t\tDo u want to continue....... press 1 for No");
			k = s1.nextLine();
		} while (k != "1");

		try {
			if (con != null && !con.isClosed()) {
				con.close();
			}
		} catch (Exception e) {
			System.out.println(e);
		}
	}
}