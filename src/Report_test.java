import Test_Extract.DFCHelper;

import com.documentum.com.DfClientX;
import com.documentum.com.IDfClientX;
import com.documentum.fc.client.DfQuery;
import com.documentum.fc.client.IDfClient;
import com.documentum.fc.client.IDfCollection;
import com.documentum.fc.client.IDfQuery;
import com.documentum.fc.client.IDfSession;
import com.documentum.fc.client.IDfSessionManager;
import com.documentum.fc.client.IDfSysObject;
import com.documentum.fc.common.DfException;
import com.documentum.fc.common.DfId;
import com.documentum.fc.common.DfLogger;
import com.documentum.fc.common.IDfLoginInfo;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Report_test
{
	public static void main(String args[])
	{
		HashMap<String, ArrayList> mapretiring = new HashMap();
		HashMap<String, ArrayList> mapretired = new HashMap();
		try
		{
			IDfSession session=null;
			DFCHelper dfcHelper = new DFCHelper();
			String docbaseLoginId = "midocsp";
			String docbasePassword = "m1d0csp74";
			String docbaseName = "midocs_prod";
			System.out.println("ReferenceUtil: main: enter");
			session = dfcHelper.createSession(docbaseLoginId, docbasePassword, docbaseName);

			System.out.println("ReferenceUtil: main: enter");

			//String sDirectoryPath = "/u01/home/dmadmin-p/";
			String sDirectoryPath ="C:/Midocs-Upgrade-Git/";
			FileOutputStream fileOut = new FileOutputStream(sDirectoryPath + "Retired_Documents.xlsx");
			FileOutputStream fileOut1 = new FileOutputStream(sDirectoryPath + "Expiring_Documents.xlsx");

			DateFormat dateFormat = new SimpleDateFormat("MM/dd/YYYY HH:mm:ss");
			Date date = new Date();
			String currentDate = dateFormat.format(date);
			System.out.println("Current date" + currentDate);

			Calendar c = Calendar.getInstance();
			c.setTime(new Date());
			c.add(Calendar.DATE,14);
			String retiring = dateFormat.format(c.getTime());
			System.out.println("retiring " + retiring);

			Calendar c1 = Calendar.getInstance();
			c1.setTime(new Date());
			c1.add( Calendar.DATE,-14);
			String retired = dateFormat.format(c1.getTime());
			//String retired;

		/*	if (c1.get(2) == 0)
			{
				retired = 

						"01/" + c1.get(5) + "/" + c1.get(1) + " " + c1.get(11) + ":" + c1.get(12) + ":" + c1.get(13);
			}
			else
			{
				retired = 

						c1.get(2) + "/" + c1.get(5) + "/" + c1.get(1) + " " + c1.get(11) + ":" + c1.get(12) + ":" + c1.get(13);
				System.out.println("retired " + retired);
			}*/
			System.out.println("retired " + retired);
			
			XSSFWorkbook wb = runExpired(session, retired,mapretired);
			XSSFWorkbook wb1 = runExpiring(session, retiring,mapretiring);

			wb.write(fileOut);
			fileOut.close();
			wb1.write(fileOut1);
			fileOut1.close();

			ArrayList productOwners = getProductOwners(session);
			System.out.println("Email");
			String pMessageBody = "Report for expiring documents in the next two weeks and expired documents in the last two weeks"
					              + "Note: Documents currently checked out were not retired and will be retired in the subsequent weeks";
		//	String pSmtpHost = MINotificationHelper.getSMTPName(session);
			String pSubject = "Expiring Reports";
			String pFilePath = "/u01/home/dmadmin-p/Retired_Documents.xlsx";
			
			String pFrom = "MIDOCS_Support";

			String pFilePath1 = "/u01/home/dmadmin-p/Expiring_Documents.xlsx";
			/*for (int i = 0; i < productOwners.size(); i++)
			{
				String pTo = (String)productOwners.get(i);
				Boolean localBoolean = Boolean.valueOf(sendEmailWithAttachment(pMessageBody, pSmtpHost, pSubject, pFilePath, pFilePath1, pFrom, pTo));
			}
			if (session != null) {
				((FileOutputStream)session).close();
			}*/
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public static boolean sendEmailWithAttachment(String pMessageBody, String pSmtpHost, String pSubject, String pFilePath, String pFilePath1, String pFrom, String pTo)
	{
		boolean flag = false;
		try
		{
			Properties properties = System.getProperties();

			properties.setProperty("mail.smtp.host", pSmtpHost);

			Session session = Session.getDefaultInstance(properties);

			MimeMessage message = new MimeMessage(session);

			message.setFrom(new InternetAddress(pFrom));

		//	message.setRecipients(message.RecipientType.TO, InternetAddress.parse(pTo));

			message.setSubject(pSubject);

			MimeBodyPart messagePart = new MimeBodyPart();

			messagePart.setContent(pMessageBody, "text/html");

			MimeBodyPart attachmentPart = new MimeBodyPart();
			FileDataSource fileDataSource = new FileDataSource(pFilePath)
			{
				public String getContentType()
				{
					return "application/octet-stream";
				}
			};
			MimeBodyPart attachmentPart1 = new MimeBodyPart();
			FileDataSource fileDataSource1 = new FileDataSource(pFilePath1)
			{
				public String getContentType()
				{
					return "application/octet-stream";
				}
			};
			attachmentPart.setDataHandler(new DataHandler(fileDataSource));
			attachmentPart.setFileName(fileDataSource.getName());
			attachmentPart1.setDataHandler(new DataHandler(fileDataSource1));
			attachmentPart1.setFileName(fileDataSource1.getName());

			Multipart multipart = new MimeMultipart();
			multipart.addBodyPart(messagePart);
			multipart.addBodyPart(attachmentPart);
			multipart.addBodyPart(attachmentPart1);
			message.setContent(multipart);

			Transport.send(message);
			System.out.println("Sent message successfully....");
			flag = true;
		}
		catch (MessagingException e)
		{
			System.out.println(e.getMessage());

			return flag;
		}
		return flag;
	}

	private static ArrayList getProductOwners(IDfSession session)
			throws DfException
	{
		IDfQuery query = new DfQuery();
		query.setDQL("select user_address from dm_user where user_name in (select DISTINCT(config_product_owner) from mi_config_product_r) or user_name='kucheja1' or user_name='chattju1'");
		//query.setDQL("select user_address from dm_user where (user_name='kucheja1' or user_name='chattju1')");

		IDfCollection coll = query.execute(session, 1);
		ArrayList emailAddress = new ArrayList();
		while (coll.next())
		{
			String email = coll.getString("user_address");
			System.out.println("Sending email to "+email);
			emailAddress.add(email);
		}
		return emailAddress;
	}

	private static XSSFWorkbook runExpired(IDfSession session, String time, HashMap<String, ArrayList> mapretired)
			throws DfException
	{
		IDfQuery query = new DfQuery();
		System.out.println("Run expired time" + time);
		System.out.println("select object_name,title,expiration_date,owner_name,product_name from mi_response where retired_date > Date('" + 
				time + "','MM/DD/YYYY hh:mm:ss') and retired_date < DATE('TODAY')");

		query.setDQL("select r_object_id,object_name,title,expiration_date,retired_date,owner_name,product_name,a_status from mi_response where retired_date > Date('" + 
				time + "','MM/DD/YYYY hh:mm:ss') and retired_date < DATE('TODAY')");
		IDfCollection coll = query.execute(session, 1);

		ArrayList atts = new ArrayList();
		while (coll.next())
		{
			ArrayList columns = new ArrayList();
			String object_id=coll.getString("r_object_id");
			IDfSysObject document = (IDfSysObject)session.getObject(new DfId(object_id));	

			String objName = document.getString("object_name");
			columns.add(objName); 
			String title = document.getString("title");
			columns.add(title);
			String status = document.getString("a_status");
			columns.add(status);
			String prduct_name = document.getRepeatingString("product_name",0);
			columns.add(prduct_name);
			//String expDate = coll.getString("expiration_date");
			String expDate = document.getString("retired_date");	
			columns.add(expDate);
			String owner_name = document.getString("owner_name");
			columns.add(owner_name);
			mapretired.put(objName, columns);
			System.out.println("Put in columns "+objName+" "+title+" "+status);
		}
		String notification = "Report for documents retired in the last two weeks";
		XSSFWorkbook wb = createExcelFile(session, mapretired, notification, notification,"Retired Date");
		return wb;
	}

	private static XSSFWorkbook runExpiring(IDfSession session, String time, HashMap<String, ArrayList> mapretiring)
			throws DfException
	{
		//IDfQuery query = new DfQuery();
		System.out.println("sent time" + time);
		System.out.println("select object_name,title,a_status,product_name,expiration_date,owner_name from mi_response where expiration_date > DATE('TODAY') and expiration_date < Date('" + 
 
      time + "','MM/DD/YYYY hh:mm:ss') ");
		String query="select r_object_id,object_name,title,a_status,product_name,expiration_date,owner_name from mi_response where expiration_date > DATE('TODAY') and expiration_date < Date('" + 
				time + "','MM/DD/YYYY hh:mm:ss') ";
		
		IDfCollection coll = DFCHelper.executeQuery(session, query);
		while (coll.next())
		{
			ArrayList columns = new ArrayList();

			String object_id=coll.getString("r_object_id");
			IDfSysObject document = (IDfSysObject)session.getObject(new DfId(object_id));	
			
			String objName = document.getString("object_name");
			columns.add(objName);
			String title = document.getString("title");
			columns.add(title);
			String status = document.getString("a_status");
			columns.add(status);
			String prduct_name = document.getRepeatingString("product_name",0);
			System.out.println("Document name "+objName+" PROOOO name: "+prduct_name);
			columns.add(prduct_name);
			String expDate = document.getString("expiration_date");
			columns.add(expDate);
			String owner_name = document.getString("owner_name");
			columns.add(owner_name);
			mapretiring.put(objName, columns);
		}
		String notification = "Report for documents expiring in the next two weeks";
		XSSFWorkbook wb1 = createExcelFile(session, mapretiring, notification, notification,"Expiry Date");
		return wb1;
	}

	private static XSSFWorkbook createExcelFile(IDfSession session, HashMap<String, ArrayList> map2, String notification, String notification1, String dates)
	{
		XSSFWorkbook workbook = new XSSFWorkbook();

		XSSFSheet worksheet = workbook.createSheet("User Report");
		System.out.println("Writing to Excel");
		XSSFFont font = workbook.createFont();
		font.setBoldweight((short)700);
		font.setColor((short)9);
		XSSFCellStyle style = workbook.createCellStyle();
		style.setFont(font);
		style.setFillForegroundColor(IndexedColors.BLACK.getIndex());
		style.setFillPattern((short)1);

		Row row0 = worksheet.createRow(0);
		Cell cellTitle = row0.createCell(0);

		cellTitle.setCellValue(notification1);
		cellTitle.setCellStyle(style);
		Cell cellTitle1 = row0.createCell(1);
		cellTitle1.setCellStyle(style);
		Cell cellTitle2 = row0.createCell(2);
		cellTitle2.setCellStyle(style);
		Cell cellTitle3 = row0.createCell(3);
		cellTitle3.setCellStyle(style);
		Cell cellTitle4 = row0.createCell(4);
		cellTitle4.setCellStyle(style);
		worksheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 4));
		Cell cellTitle5 = row0.createCell(5);
		cellTitle5.setCellStyle(style);
		SimpleDateFormat dateFormatHeader = new SimpleDateFormat(
				"MMM d, yyyy HH:mm");
		cellTitle5.setCellValue(dateFormatHeader.format(new Date()));
		Cell cellTitle6 = row0.createCell(6);
		cellTitle6.setCellStyle(style);
		worksheet.addMergedRegion(new CellRangeAddress(0, 0, 5, 6));

		Row row1 = worksheet.createRow(1);
		Cell cellProduct = row1.createCell(0);
		cellProduct.setCellValue("Document name");
		cellProduct.setCellStyle(style);

		Cell cellCreated = row1.createCell(1);
		cellCreated.setCellValue("Title");
		cellCreated.setCellStyle(style);

		Cell cellUpdated = row1.createCell(2);
		cellUpdated.setCellValue("Status");
		cellUpdated.setCellStyle(style);

		Cell cellApproved = row1.createCell(3);
		cellApproved.setCellValue("Product Name");
		cellApproved.setCellStyle(style);

		Cell cellRetired = row1.createCell(4);
		cellRetired.setCellValue(""+dates+"");
		cellRetired.setCellStyle(style);

		Cell cellReaffirmed = row1.createCell(5);
		cellReaffirmed.setCellValue("Author");
		cellReaffirmed.setCellStyle(style);

		int rownum = 2;
		ArrayList<String> data;
		int i;
		Iterator entries = map2.entrySet().iterator();
		while(entries.hasNext())
		{
			Map.Entry<String, ArrayList> ee = (Map.Entry)entries.next();
			Row row = worksheet.createRow(rownum++);
			int cellnum = 0;
			data = new ArrayList();
			data = (ArrayList)ee.getValue();
			i=0;
			//continue;
			for(int x=0;x<data.size();x++)
			{
			String aux = (String)data.get(x);
			System.out.println(aux);
			Cell cell1 = row.createCell(cellnum++);
			cell1.setCellValue(aux);
			}
			i++;
			}
		return workbook;
	}
}
