import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import org.apache.poi.common.usermodel.Hyperlink;
import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import Test_Extract.DFCHelper;

import com.documentum.fc.client.DfQuery;
import com.documentum.fc.client.IDfCollection;
import com.documentum.fc.client.IDfQuery;
import com.documentum.fc.client.IDfSession;
import com.documentum.fc.client.IDfSysObject;
import com.documentum.fc.common.DfException;
import com.documentum.fc.common.DfId;
import com.documentum.fc.common.IDfId;

public class OncologyMonthly_Test {
	static HashMap<String, ArrayList> mapretiring= new HashMap<String, ArrayList>();
	public static void main(String args[]){
		try{
			DFCHelper dfcHelper = new DFCHelper();
			String docbaseLoginId = "dmadmin-p";
			String docbasePassword = "merlin"; 
			String docbaseName = "midocsp";
			System.out.println("ReferenceUtil: main: enter");
			IDfSession session = dfcHelper.createSession(docbaseLoginId, docbasePassword, docbaseName);

			final String sDirectoryPath = "C:/Geodart/";
			final FileOutputStream fileOut = new FileOutputStream(String.valueOf(sDirectoryPath) + "Sandoz_YTD_Activity_07-06-22.xlsx");
			HashMap<String, ArrayList> mapNew = new HashMap<String, ArrayList>();
			HashMap<String, ArrayList> mapNewVersion = new HashMap<String, ArrayList>();
			HashMap<String, ArrayList> mapApproved = new HashMap<String, ArrayList>();
			HashMap<String, ArrayList> mapReviewed = new HashMap<String, ArrayList>();

			final DateFormat dateFormat = new SimpleDateFormat("MM/dd/YYYY HH:mm:ss");
			final Date date = new Date();
			final String currentDate = dateFormat.format(date);
			System.out.println("Current date" + currentDate);
			
			final Calendar c = Calendar.getInstance();
			c.setTime(new Date());
			c.add(5, -35);
			final String retiring = dateFormat.format(c.getTime());
			System.out.println("retiring " + retiring);
			final Calendar c2 = Calendar.getInstance();
			c2.setTime(new Date());
			c2.add(5, -35);
			//final String retired = dateFormat.format(c2.getTime());
			final String retired = "01/01/2021";
			System.out.println("retired " + retired);
			final XSSFWorkbook wb = new XSSFWorkbook();

			XSSFSheet worksheet = wb.createSheet("New Document");
			worksheet = newDocument(session, retired, mapNew, "NewDocument",wb,worksheet);
			System.out.println("Size of worksheet "+worksheet.getLastRowNum());

			XSSFSheet worksheet1 = wb.createSheet("New Version");
			worksheet1 = newDocument(session, retired,mapNewVersion,"NewVersion",wb,worksheet1);

			XSSFSheet worksheet2 = wb.createSheet("Approved");
			worksheet2 = newDocument(session, retired, mapApproved, "Approved",wb,worksheet2);

			XSSFSheet worksheet3 = wb.createSheet("Reviewed");
			worksheet3 = newDocument(session, retired, mapReviewed, "Reviewed",wb,worksheet3);

			wb.write((OutputStream)fileOut);
			fileOut.close();

			/*		final ArrayList productOwners = getProductOwners(session);
			System.out.println("Email");
			final String pMessageBody = "Report for oncology monthly activty";
			final String pSmtpHost = MINotificationHelper.getSMTPName(session);
			final String pSubject = "Expiring Reports";
			final String pFilePath = "/u01/home/dmadmin-p/Retired_Documents.xlsx";
			final String pFrom = "MIDOCS_Support";
			for (int i = 0; i < productOwners.size(); ++i) {
				final String pTo = (String) productOwners.get(i);
				sendEmailWithAttachment(pMessageBody, pSmtpHost, pSubject, pFilePath, pFrom, pTo);
			}*/
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*public static boolean sendEmailWithAttachment(final String pMessageBody, final String pSmtpHost, final String pSubject, final String pFilePath, final String pFrom, final String pTo) {
		boolean flag = false;
		try {
			final Properties properties = System.getProperties();
			properties.setProperty("mail.smtp.host", pSmtpHost);
			final Session session = Session.getDefaultInstance(properties);
			final MimeMessage message = new MimeMessage(session);
			message.setFrom((Address)new InternetAddress(pFrom));
			message.setRecipients(Message.RecipientType.TO, (Address[])InternetAddress.parse(pTo));
			message.setSubject(pSubject);
			final MimeBodyPart messagePart = new MimeBodyPart();
			messagePart.setContent((Object)pMessageBody, "text/html");
			final MimeBodyPart attachmentPart = new MimeBodyPart();
			final FileDataSource fileDataSource = (FileDataSource)new FileDataSource(pFilePath);
			;
			attachmentPart.setDataHandler(new DataHandler((DataSource)fileDataSource));
			attachmentPart.setFileName(fileDataSource.getName());

			final Multipart multipart = (Multipart)new MimeMultipart();
			multipart.addBodyPart((BodyPart)messagePart);
			multipart.addBodyPart((BodyPart)attachmentPart);
			message.setContent(multipart);
			Transport.send((Message)message);
			System.out.println("Sent message successfully....");
			flag = true;
		}
		catch (MessagingException e) {
			System.out.println(e.getMessage());
			return flag;
		}
		return flag;
	} 

	private static ArrayList getProductOwners(final IDfSession session) throws DfException {
		final IDfQuery query = (IDfQuery)new DfQuery();
		query.setDQL("select user_address from dm_user where user_name in (select DISTINCT(config_product_owner) from mi_config_product_r) or user_name='kucheja1' or user_name='chattju1'");
		final IDfCollection coll = query.execute(session, 1);
		final ArrayList emailAddress = new ArrayList();
		while (coll.next()) {
			final String email = coll.getString("user_address");
			System.out.println("Sending email to " + email);
			emailAddress.add(email);
		}
		return emailAddress;
	} */

	private static XSSFSheet newDocument(final IDfSession session, final String time, final HashMap<String, ArrayList> mapretired, String type, XSSFWorkbook wb, XSSFSheet worksheet) throws DfException {
		final IDfQuery query = (IDfQuery)new DfQuery();
		if(type=="NewDocument"){
			System.out.println("New DOCUMENT ------- select r_object_id,object_name,r_version_label,r_creation_date from mi_response(all) where response_type='1' and r_creation_date<DATE('TODAY') and r_creation_date>DATE('"+time+"','MM/DD/YYYY') and any r_version_label='0.1' and any ta_name like '%Oncology%'");
			//query.setDQL("select r_object_id,object_name,r_version_label,r_creation_date from mi_response(all) where response_type='1' and r_creation_date<DATE('TODAY') and r_creation_date>DATE('"+time+"','MM/DD/YYYY HH:MM:SS') and any r_version_label='0.1' and any ta_name like '%Oncology%'");
			query.setDQL("select r_object_id,object_name,r_version_label,r_creation_date from mi_response(all) where response_type='1' and r_creation_date<DATE('TODAY') and r_creation_date>DATE('"+time+"','MM/DD/YYYY') and any r_version_label='0.1' and any product_name in ('Zarxio','Omnitrope','Glatopa','Erelzi','Ziextenzo','Biosimilars','Bivalirudin','Kerydin','Veregen','Oxistat','Pandel','Apexicone','Argatroban','Arranon','Hycamtin Capsule','Hycamtin Injection','Zofran Injection','Zofran Oral Formulations','Symjepi','Treprostinil Injection','Hydroxychloroquine','Fulvestrant Injection','Other Sandoz','Ferumoxytol')");
            		
		}else if(type=="NewVersion"){ 
			System.out.println("New Version ---- select r_object_id,object_name,r_version_label,r_creation_date from mi_response(all) where response_type='1' and r_creation_date<DATE('TODAY') and r_creation_date>DATE('"+time+"','MM/DD/YYYY') and any ta_name like '%Oncology%'");
			//query.setDQL("select r_object_id,object_name,r_version_label,r_creation_date from mi_response(all) where response_type='1' and r_creation_date<DATE('TODAY') and r_creation_date>DATE('"+time+"','MM/DD/YYYY HH:MM:SS') and any ta_name like '%Oncology%'");
			query.setDQL("select r_object_id,object_name,r_version_label,r_creation_date from mi_response(all) where response_type='1' and r_creation_date<DATE('TODAY') and r_creation_date>DATE('"+time+"','MM/DD/YYYY') and any product_name in ('Zarxio','Omnitrope','Glatopa','Erelzi','Ziextenzo','Biosimilars','Bivalirudin','Kerydin','Veregen','Oxistat','Pandel','Apexicone','Argatroban','Arranon','Hycamtin Capsule','Hycamtin Injection','Zofran Injection','Zofran Oral Formulations','Symjepi','Treprostinil Injection','Hydroxychloroquine','Fulvestrant Injection','Other Sandoz','Ferumoxytol')");
			
		}else if(type=="Approved"){
			System.out.println(" Approved ---- select r_object_id,object_name,r_version_label from mi_response(all) where response_type='1' and approved_date<DATE('TODAY') and approved_date>DATE('"+time+"','MM/DD/YYYY') and any ta_name like '%Oncology%'");
			//query.setDQL("select r_object_id,object_name,r_version_label from mi_response(all) where response_type='1' and approved_date<DATE('TODAY') and approved_date>DATE('"+time+"','MM/DD/YYYY HH:MM:SS') and any ta_name like '%Oncology%'");
			query.setDQL("select r_object_id,object_name,r_version_label from mi_response(all) where response_type='1' and approved_date<DATE('TODAY') and approved_date>DATE('"+time+"','MM/DD/YYYY') and any product_name in ('Zarxio','Omnitrope','Glatopa','Erelzi','Ziextenzo','Biosimilars','Bivalirudin','Kerydin','Veregen','Oxistat','Pandel','Apexicone','Argatroban','Arranon','Hycamtin Capsule','Hycamtin Injection','Zofran Injection','Zofran Oral Formulations','Symjepi','Treprostinil Injection','Hydroxychloroquine','Fulvestrant Injection','Other Sandoz','Ferumoxytol')");
			
		}else if(type=="Reviewed"){
			System.out.println("Reviewed ---- select r_object_id,object_name,r_version_label from mi_response(all) where response_type='1' and last_reviewed_date<DATE('TODAY') and last_reviewed_date>DATE('"+time+"','MM/DD/YYYY') and any ta_name like '%Oncology%' and a_status='Peer Reviewed'");
			//query.setDQL("select r_object_id,object_name,r_version_label from mi_response(all) where response_type='1' and last_reviewed_date<DATE('TODAY') and last_reviewed_date>DATE('"+time+"','MM/DD/YYYY HH:MM:SS') and any ta_name like '%Oncology%' and a_status='Peer Reviewed'");
			query.setDQL("select r_object_id,object_name,r_version_label from mi_response(all) where response_type='1' and last_reviewed_date<DATE('TODAY') and last_reviewed_date>DATE('"+time+"','MM/DD/YYYY') and a_status='Peer Reviewed' and any product_name in ('Zarxio','Omnitrope','Glatopa','Erelzi','Ziextenzo','Biosimilars','Bivalirudin','Kerydin','Veregen','Oxistat','Pandel','Apexicone','Argatroban','Arranon','Hycamtin Capsule','Hycamtin Injection','Zofran Injection','Zofran Oral Formulations','Symjepi','Treprostinil Injection','Hydroxychloroquine','Fulvestrant Injection','Other Sandoz','Ferumoxytol')");
			
		}
		
		
		/*if(type=="NewDocument"){
			System.out.println("New DOCUMENT ------- select r_object_id,object_name,r_version_label,r_creation_date from mi_response(all) where response_type='1' and r_creation_date<DATE('TODAY') and r_creation_date>DATE('"+time+"','MM/DD/YYYY') and any r_version_label='0.1' and any ta_name like '%Oncology%'");
			query.setDQL("select r_object_id,object_name,r_version_label,r_creation_date from mi_response(all) where response_type='1' and r_creation_date<DATE('04/01/2021','MM/DD/YYYY') and r_creation_date>DATE('02/28/2021','MM/DD/YYYY') and any r_version_label='0.1' and any ta_name like '%Oncology%'");
		}else if(type=="NewVersion"){
			System.out.println("New Version ---- select r_object_id,object_name,r_version_label,r_creation_date from mi_response(all) where response_type='1' and r_creation_date<DATE('TODAY') and r_creation_date>DATE('"+time+"','MM/DD/YYYY') and any ta_name like '%Oncology%'");
			query.setDQL("select r_object_id,object_name,r_version_label,r_creation_date from mi_response(all) where response_type='1' and r_creation_date<DATE('04/01/2021','MM/DD/YYYY') and r_creation_date>DATE('02/28/2021','MM/DD/YYYY') and any ta_name like '%Oncology%'");
		}else if(type=="Approved"){
			System.out.println(" Approved ---- select r_object_id,object_name,r_version_label from mi_response(all) where response_type='1' and approved_date<DATE('TODAY') and approved_date>DATE('"+time+"','MM/DD/YYYY') and any ta_name like '%Oncology%'");
			query.setDQL("select r_object_id,object_name,r_version_label from mi_response(all) where response_type='1' and approved_date<DATE('04/01/2021','MM/DD/YYYY') and approved_date>DATE('02/28/2021','MM/DD/YYYY') and any ta_name like '%Oncology%'");
		}else if(type=="Reviewed"){
			System.out.println("Reviewed ---- select r_object_id,object_name,r_version_label from mi_response(all) where response_type='1' and last_reviewed_date<DATE('TODAY') and last_reviewed_date>DATE('"+time+"','MM/DD/YYYY') and any ta_name like '%Oncology%' and a_status='Peer Reviewed'");
			query.setDQL("select r_object_id,object_name,r_version_label from mi_response(all) where response_type='1' and last_reviewed_date<DATE('04/01/2021','MM/DD/YYYY') and last_reviewed_date>DATE('02/28/2021','MM/DD/YYYY') and any ta_name like '%Oncology%' and a_status='Peer Reviewed'");
		}*/
		final IDfCollection coll = query.execute(session, 1);
		while (coll.next()) {
			final ArrayList columns = new ArrayList();
			final String object_id = coll.getString("r_object_id");
			final IDfSysObject document = (IDfSysObject)session.getObject((IDfId)new DfId(object_id));
			final String objName = document.getString("object_name");
			columns.add(objName);
			final String title = document.getString("title");
			columns.add(title);
			final String modifyDate = document.getString("r_modify_date");
			columns.add(modifyDate);
			final String status = document.getString("a_status");
			columns.add(status);
			final String prduct_name = document.getRepeatingString("product_name", 0);
			columns.add(prduct_name);
			final String owner_name = document.getString("owner_name");
			columns.add(owner_name);
			final String version = document.getString("r_version_label");
			columns.add(version); 
			final String modified_by = document.getString("r_modifier");
			columns.add(modified_by);
			final String expDate = document.getString("retired_date");
			columns.add(expDate);
			final String approvedDate = document.getString("approved_date");
			columns.add(approvedDate);
			mapretired.put(object_id, columns);
			//System.out.println("Put in columns " + objName + " " + title + " " + status);
		}
		final String notification = "Report for oncology monthly activity";
		System.out.println("-------Query Size------- "+mapretired.size());
		worksheet  = createExcelFile(session, mapretired, notification, notification,wb,worksheet);
		return worksheet;
	}

	private static XSSFSheet createExcelFile(final IDfSession session, final HashMap<String, ArrayList> map2, final String notification, final String notification1, XSSFWorkbook workbook, XSSFSheet worksheet) {

		System.out.println("Writing to Excel");
		final XSSFFont font = workbook.createFont();
		font.setBoldweight((short)700);
		font.setColor((short)9);
		final XSSFCellStyle style = workbook.createCellStyle();
		style.setFont((Font)font);
		style.setFillForegroundColor(IndexedColors.BLACK.getIndex());
		style.setFillPattern((short)1);
		final Row row0 = (Row)worksheet.createRow(0);
		final Cell cellTitle = row0.createCell(0);
		cellTitle.setCellValue(notification1);
		cellTitle.setCellStyle((CellStyle)style);
		final Cell cellTitle2 = row0.createCell(1);
		cellTitle2.setCellStyle((CellStyle)style);
		final Cell cellTitle3 = row0.createCell(2);
		cellTitle3.setCellStyle((CellStyle)style);
		final Cell cellTitle4 = row0.createCell(3);
		cellTitle4.setCellStyle((CellStyle)style);
		final Cell cellTitle5 = row0.createCell(4);
		cellTitle5.setCellStyle((CellStyle)style);
		worksheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 4));
		final Cell cellTitle6 = row0.createCell(5);
		cellTitle6.setCellStyle((CellStyle)style);
		final SimpleDateFormat dateFormatHeader = new SimpleDateFormat("MMM d, yyyy HH:mm");
		cellTitle6.setCellValue(dateFormatHeader.format(new Date()));
		final Cell cellTitle7 = row0.createCell(6);
		cellTitle7.setCellStyle((CellStyle)style);
		worksheet.addMergedRegion(new CellRangeAddress(0, 0, 5, 6));
		final Row row2 = (Row)worksheet.createRow(1);
		final Cell cellProduct = row2.createCell(0);
		cellProduct.setCellValue("Document name");
		cellProduct.setCellStyle((CellStyle)style);
		final Cell cellCreated = row2.createCell(1);
		cellCreated.setCellValue("Title");
		cellCreated.setCellStyle((CellStyle)style);
		final Cell cellUpdated = row2.createCell(2);
		cellUpdated.setCellValue(" Last Modified Date");
		cellUpdated.setCellStyle((CellStyle)style);
		final Cell cellApproved = row2.createCell(3);
		cellApproved.setCellValue("Status");
		cellApproved.setCellStyle((CellStyle)style);
		/*	final Cell cellRetired = row2.createCell(4);
		cellRetired.setCellValue(new StringBuilder().append(dates).toString());
		cellRetired.setCellStyle((CellStyle)style); */
		final Cell cellReaffirmed = row2.createCell(4);
		cellReaffirmed.setCellValue("Product Name");
		cellReaffirmed.setCellStyle((CellStyle)style);
		final Cell cellAuthors = row2.createCell(5);
		cellAuthors.setCellValue("Authors");
		cellAuthors.setCellStyle((CellStyle)style);
		final Cell cellVersion = row2.createCell(6);
		cellVersion.setCellValue("Version");
		cellVersion.setCellStyle((CellStyle)style);
		final Cell cellModifier = row2.createCell(7);
		cellModifier.setCellValue("Last Modified by");
		cellModifier.setCellStyle((CellStyle)style);
		final Cell cellExpiredDate = row2.createCell(8);
		cellExpiredDate.setCellValue("Expired Date");
		cellExpiredDate.setCellStyle((CellStyle)style);
		final Cell cellApprovedDate = row2.createCell(9);
		cellApprovedDate.setCellValue("Approved Date");
		cellApprovedDate.setCellStyle((CellStyle)style);
		int rownum = 2;
		System.out.println("--------Map Size---- "+map2.size());
		for (final Entry<String, ArrayList> ee : map2.entrySet()) {
			final Row row3 = (Row)worksheet.createRow(rownum++);
			int cellnum = 0;
			ArrayList<String> data = new ArrayList<String>();
			data = ee.getValue();
			for (int x = 0; x < data.size(); x++) {
				final String aux = data.get(x);
				//System.out.println(aux);
				final Cell cell1 = row3.createCell(cellnum++);
				cell1.setCellValue(aux);
			}
		}
		return worksheet;
	}

}
