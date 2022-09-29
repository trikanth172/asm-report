package com.untd.database.asmreport;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
// imports for parsing xml 
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.Date;
import java.util.Properties;
import java.sql.PreparedStatement;


@SpringBootApplication
public class AsmReportApplication
{
	
	public final static String configFile_name="dbconfig.xml";
	public final static String db_creds_file="db.properties";
	public final static String output_file = "asm_report.html";

	public static void main(String[] args) throws Exception
	{
		 SpringApplication.run(AsmReportApplication.class, args);
		// all the db configs are in xml file 
		try
		{
			String[] res = new String[3];
			String[][] html_data = new String[28][3] ; 
			//File configFile = new File(configFile_name);
			ClassLoader classLoader = AsmReportApplication.class.getClassLoader();
            InputStream configfileStream = classLoader.getResourceAsStream(configFile_name);
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			//Document doc = builder.parse(configFile);
			Document doc = builder.parse(configfileStream);
			doc.getDocumentElement().normalize();
			NodeList nodes = doc.getElementsByTagName("database");
			int row = 0 ; 
			for (int i = 0; i < nodes.getLength(); i++)
			{
				Node mynode = nodes.item(i);
				//System.out.println("Property = " + mynode.getNodeName()); // name of node is database
				if (mynode.getNodeType() == Node.ELEMENT_NODE) 
				{
					Element myelement = (Element) mynode;
/*
					System.out.println("dbname = " + myelement.getElementsByTagName("dbname").item(0).getTextContent());
					System.out.println("dbtype = " + myelement.getElementsByTagName("dbtype").item(0).getTextContent());
					System.out.println("url = " + myelement.getElementsByTagName("url").item(0).getTextContent());
				*/
				
				//sString xml_sid = myelement.getElementsByTagName("dbname").item(0).getTextContent() ; 
									
				  res[0] = myelement.getElementsByTagName("hostname").item(0).getTextContent(); 
				  res[1] = myelement.getElementsByTagName("driver").item(0).getTextContent(); 
				  res[2] = myelement.getElementsByTagName("url").item(0).getTextContent(); 
				
				String sql = "SELECT name , free_mb FROM v$asm_diskgroup " ; 
				Class.forName(res[1]);
				// connection info
				Properties info = new Properties();
				try {
				InputStream dbcreds_filestream = classLoader.getResourceAsStream(db_creds_file);
				

				info.load(dbcreds_filestream);
				// connect as sysdba
				Connection con = DriverManager.getConnection(res[2], info);
				PreparedStatement ps = con.prepareStatement(sql);
				ResultSet rs = ps.executeQuery();
				
				while(rs.next())
				{
					
					//System.out.println("the host is "+res[0]+", DISK "+rs.getString(1)+" has "+rs.getInt(2)+" mb free " ) ;
					html_data[row][0] = res[0] ; 
					html_data[row][1] = rs.getString(1);
					html_data[row][2] = rs.getObject(2).toString();
					//html_data[row][2] = Integer.toString(rs.getInt(2)) ; 
					
					row++; 
				}
				rs.close();
				ps.close();
				con.close();	
				}catch(Exception e) {e.printStackTrace();}
			}
						
		}
			if(args[0].equals("--logdir") && args.length == 2) 
			{
				generate_html(html_data,args[1]);
			}
			else
			{
				System.out.println("the jarfile must be invoked with --logdir option");
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

		

	}
	public static void generate_html(String[][] html_data,String Path)
	{
	      Date date = new Date();
	      // display time and date
	      String str = String.format("%tc", date );
	     // System.out.printf(str);
		StringBuilder sb = new StringBuilder("<br>"
				+ "<p>"
				+ "<table border='1' width='90%' align='center' summary='Script output'>"
				+ " <tr>"
				+ "    <th colspan=\"3\">ASM report FOR MJ DATABASES ON "+str+"  "
				+ "	</th>    "
				+ " </tr>" 
				+ "<tr>"
				+ "	<th scope=\"col\">host</th>"
				+ "	<th scope=\"col\">NAME</th>"
				+ "	<th scope=\"col\">FREE_MB</th>"
				+ "</tr>" ) ;  


		sb.append("<tr>"
				+ "	<td> "+html_data[0][0]+"</td>"
				+ "	<td>"+html_data[0][1]+"</td>"
				+ "	<td align=\"center\"> "+html_data[0][2]+"</td>"
				+ "</tr>"
				+ "<tr>"
				+ "	<td>"+html_data[1][0]+"</td>"
				+ "	<td>"+html_data[1][1]+"</td>"
				+ "	<td align=\"center\">"+html_data[1][2]+"</td>"
				+ "</tr>"
				+ "<tr>"
				+ "	<td>"+html_data[2][0]+"</td>"
				+ "	<td>"+html_data[2][1]+"</td>"
				+ "	<td align=\"center\">"+html_data[2][2]+"</td>"
				+ "</tr>"
				+ "<tr>"
				+ "	<td>"+ html_data[3][0] +"</td>"
				+ "	<td>"+html_data[3][1]+"</td>"
				+ "	<td align=\"center\">"+html_data[3][2]+"</td>"
				+ "</tr>"
				+ "<tr>"
				+ "	<td>"+html_data[4][0]+"</td>"
				+ "	<td>"+html_data[4][1]+"</td>"
				+ "	<td align=\"center\">"+html_data[4][2]+"</td>"
				+ "</tr>"
				+ "<tr>"
				+ "	<td>"+html_data[5][0]+"</td>"
				+ "	<td>"+html_data[5][1]+"</td>"
				+ "	<td align=\"center\">"+html_data[5][2]+"</td>"
				+ "</tr>"
				+ "<tr>"
				+ "	<td>"+html_data[6][0]+"</td>"
				+ "	<td>"+html_data[6][1]+"</td>"
				+ "	<td align=\"center\">"+html_data[6][2]+"</td>"
				+ "</tr>"
				+ "<tr>"
				+ "	<td>"+html_data[7][0]+"</td>"
				+ "	<td>"+html_data[7][1]+"</td>"
				+ "	<td align=\"center\">"+html_data[7][2]+"</td>"
				+ "</tr>"
				+ "<tr>"
				+ "	<td>"+html_data[8][0]+"</td>"
				+ "	<td>"+html_data[8][1]+"</td>"
				+ "	<td align=\"center\">"+html_data[8][2]+"</td>"
				+ "</tr>"
				+ "<tr>"
				+ "	<td>"+html_data[9][0]+"</td>"
				+ "	<td>"+html_data[9][1]+"</td>"
				+ "	<td align=\"center\">"+html_data[9][2]+"</td>"
				+ "</tr>"
				+ "<tr>"
				+ "	<td>"+html_data[10][0]+"</td>"
				+ "	<td>"+html_data[10][1]+"</td>"
				+ "	<td align=\"center\">"+html_data[10][2]+"</td>"
				+ "</tr>"
				+ "<tr>"
				+ "	<td>"+html_data[11][0]+"</td>"
				+ "	<td>"+html_data[11][1]+"</td>"
				+ "	<td align=\"center\">"+html_data[11][2]+"</td>"
				+ "</tr>"
				+ "<tr>"
				+ "	<td>"+html_data[12][0]+"</td>"
				+ "	<td>"+html_data[12][1]+"</td>"
				+ "	<td align=\"center\">"+html_data[12][2]+"</td>"
				+ "</tr>"
				+ "<tr>"
				+ "	<td>"+html_data[13][0]+"</td>"
				+ "	<td>"+html_data[13][1]+"</td>"
				+ "	<td align=\"center\">"+html_data[13][2]+"</td>"
				+ "</tr>"
				+ "<tr>"
				+ "	<td>"+html_data[14][0]+"</td>"
				+ "	<td>"+html_data[14][1]+"</td>"
				+ "	<td align=\"center\">"+html_data[14][2]+"</td>"
				+ "</tr>"
				+ "<tr>"
				+ "	<td>"+html_data[15][0]+"</td>"
				+ "	<td>"+html_data[15][1]+"</td>"
				+ "	<td align=\"center\">"+html_data[15][2]+"</td>"
				+ "</tr>"
				+ "<tr>"
				+ "	<td>"+html_data[16][0]+"</td>"
				+ "	<td>"+html_data[16][1]+"</td>"
				+ "	<td align=\"center\">"+html_data[16][2]+"</td>"
				+ "</tr>"
				+ "<tr>"
				+ "	<td>"+html_data[17][0]+"</td>"
				+ "	<td>"+html_data[17][1]+"</td>"
				+ "	<td align=\"center\">"+html_data[17][2]+"</td>"
				+ "</tr>"
				+ "<tr>"
				+ "	<td>"+html_data[18][0]+"</td>"
				+ "	<td>"+html_data[18][1]+"</td>"
				+ "	<td align=\"center\">"+html_data[18][2]+"</td>"
				+ "</tr>"
				+ "<tr>"
				+ "	<td>"+html_data[19][0]+"</td>"
				+ "	<td>"+html_data[19][1]+"</td>"
				+ "	<td align=\"center\">"+html_data[19][2]+"</td>"
				+ "</tr>"
				+ "<tr>"
				+ "	<td>"+html_data[20][0]+"</td>"
				+ "	<td>"+html_data[20][1]+"</td>"
				+ "	<td align=\"center\">"+html_data[20][2]+"</td>"
				+ "</tr>"
				+ "<tr>"
				+ "	<td>"+html_data[21][0]+"</td>"
				+ "	<td>"+html_data[21][1]+"</td>"
				+ "	<td align=\"center\">"+html_data[21][2]+"</td>"
				+ "</tr>"
				+ "<tr>"
				+ "	<td>"+html_data[22][0]+"</td>"
				+ "	<td>"+html_data[22][1]+"</td>"
				+ "	<td align=\"center\">"+html_data[22][2]+"</td>"
				+ "</tr>"
				+ "<tr>"
				+ "	<td>"+html_data[23][0]+"</td>"
				+ "	<td>"+html_data[23][1]+"</td>"
				+ "	<td align=\"center\">"+html_data[23][2]+"</td>"
				+ "</tr>"
				+ "<tr>"
				+ "	<td>"+html_data[24][0]+"</td>"
				+ "	<td>"+html_data[24][1]+"</td>"
				+ "	<td align=\"center\">"+html_data[24][2]+"</td>"
				+ "</tr>"
				+ "<tr>"
				+ "	<td>"+html_data[25][0]+"</td>"
				+ "	<td>"+html_data[25][1]+"</td>"
				+ "	<td align=\"center\">"+html_data[25][2]+"</td>"
				+ "</tr>"
				+ "<tr>"
				+ "	<td>"+html_data[26][0]+"</td>"
				+ "	<td>"+html_data[26][1]+"</td>"
				+ "	<td align=\"center\">"+html_data[26][2]+"</td>"
				+ "</tr>"
				+ "<tr>"
				+ "	<td>"+html_data[27][0]+"</td>"
				+ "	<td>"+html_data[27][1]+"</td>"
				+ "	<td align=\"center\">"+html_data[27][2]+"</td>"
				+ "</tr>"
				+ "</table>")  ;
		
		try {
			File myObj; 
			if(!Path.equals(null))
			{ 
		      myObj = new File(Path+"/"+output_file);
			}
			else
			{
				 myObj  = new File(output_file);
			}
		      if (myObj.createNewFile()) {
		        System.out.println("File created: " + myObj.getName());
		      	//myObj = new File("asm_report.html");
	        	FileWriter fw = new FileWriter(myObj.getAbsoluteFile());
	            BufferedWriter bw = new BufferedWriter(fw);
	            bw.write(sb.toString());
	            bw.close();
	            System.out.println("done writing to file ");
    		
		      } else {
		        System.out.println("File already exists.");
		      
		        	//myObj = new File("asm_report.html");
		        	FileWriter fw = new FileWriter(myObj.getAbsoluteFile());
		            BufferedWriter bw = new BufferedWriter(fw);
		            bw.write(sb.toString());
		            bw.close();
		            System.out.println("done writing to file ");
        			        		        
		      }
			
		    } catch (IOException e) {
		      System.out.println("An error occurred.");
		      e.printStackTrace();
		    }
	}
	
		

}
