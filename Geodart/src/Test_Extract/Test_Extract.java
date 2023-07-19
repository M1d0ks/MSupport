package Test_Extract;

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
import com.documentum.fc.common.IDfLoginInfo;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;

public class Test_Extract
{
	public static void main(String[] args)
			throws DfException, IOException, InvalidFormatException
	{
		String commonAttributes = "r_object_id,i_chronicle_id,object_name,r_version_label,title,"
				+ "r_object_type,response_type,comment_text,r_creation_date,"
				+ "r_creator_name,created_byName,creator_fullName,"
				+ "approved_date,approved_by,approved_byName,approved_by_fullName"
				+ ",active,category_id,category_name,product_id,"
				+ "product_name,ta_id,ta_name,topic_id,topic_name,a_status,author,author_Name,author_fullName,"
				+ "expiration_date,retired_date,retired_by,retired_byName,retired_by_fullName";

		//filename date
		DateFormat df = new SimpleDateFormat("yyyyMMdd");
		Calendar cal1 = Calendar.getInstance();
		cal1.add(Calendar.DATE, 1);
		String fDate=df.format(cal1.getTime());
		System.out.println("Filename time  "+fDate);


		//String resAttributes = "expiration_date";
		String refAttributes = "reference_citation,reference_number";
		String productAttributes = "config_product_id,config_product_name,ta_id,ta_name,config_product_owner,powner_Name,powner_fullName";
		String relationAttributes = "parent_id,child_id";
		String audittrailAttributes = "r_object_id,time_stamp,event_name,user_name,user_FName,user_fullName,audited_obj_id,product_id,product_name";

		ArrayList commonAtts = getAtts(commonAttributes);
		//ArrayList resAtts = getAtts(resAttributes);
		ArrayList refAtts = getAtts(refAttributes);
		ArrayList productAtts = getAtts(productAttributes);
		ArrayList relationAtts = getAtts(relationAttributes);
		ArrayList audittrailAtts = getAtts(audittrailAttributes); 

		Workbook workbook = new SXSSFWorkbook(1000);

		String sDirectoryPath = "/u01/home/dmadmin-np/Geoda3rt/";
		//String sDirectoryPath = "/u01/home/dmadmin-np/Geodart/";
		//String sDirectoryPath ="C:/Geodart/";

		FileOutputStream fileOut = new FileOutputStream(sDirectoryPath + "Geodart_test.xlsx");

		DFCHelper dfcHelper = new DFCHelper();
		String docbaseLoginId = "dmadmin-p";
		String docbasePassword = "merlin";
		String docbaseName = "midocsp";
		System.out.println("ReferenceUtil: main: enter");
		IDfSession dfSession = dfcHelper.createSession(docbaseLoginId, docbasePassword, docbaseName);

		SXSSFSheet sheet1 = (SXSSFSheet)workbook.createSheet("Document Information");
		SXSSFSheet sheet2 = (SXSSFSheet)workbook.createSheet("Product Information");
		SXSSFSheet sheet3 = (SXSSFSheet)workbook.createSheet("Relationship Information");
		SXSSFSheet sheet4 = (SXSSFSheet)workbook.createSheet("Audittrail Information");
		SXSSFSheet sheet5 = (SXSSFSheet)workbook.createSheet("Audittrail backup");


		HashMap userNames = getFullName(dfSession);

		HashMap proTainfo = getProductta(dfSession);

		ArrayList results = gettaInfo(dfSession);

		HashMap taInfo = (HashMap)results.get(0);

		HashMap proId = (HashMap)results.get(1);


		System.out.println("Writing Document information");
		//getDocumentInfo(workbook, sheet1, dfSession, dfcHelper, fileOut, commonAtts, refAtts, userNames, proTainfo, proId);
		System.out.println("Writing Document Completed");

		System.out.println("Writing Product information");
		//getProductInfo(workbook, sheet2, dfSession, dfcHelper, fileOut, productAtts, userNames, taInfo);
		System.out.println("Writing Product Info completed");

		System.out.println("Writing Relationship information");
		//getRelationInfo(workbook, sheet3, dfSession, dfcHelper, fileOut, relationAtts, userNames);
		System.out.println("Writing Relaptionship Info completed");

		System.out.println("Writing Audittrail information");
		getAuditInfo(workbook, sheet4,sheet5, dfSession, dfcHelper, fileOut, audittrailAtts, userNames, proId,fDate);
		System.out.println("Writing Audittrail completed");

		workbook.write(fileOut);
		fileOut.close();


		InputStream inp = new FileInputStream(sDirectoryPath + "Geodart_test.xlsx");
		Workbook wb = WorkbookFactory.create(inp);

		String fileDocument = "/u01/home/dmadmin-np/Geoda3rt/MIDOC_DOCUMENT_"+fDate+".txt";
		//String fileDocument = "/u01/home/dmadmin-np/Geodart/MIDOC_DOCUMENT_20160722.txt";
		//String fileDocument="C:/Geodart/MIDOC_DOCUMENT_20180202.txt";
		FileWriter fileWriter = new FileWriter(fileDocument);
		BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
		Sheet docInfo = wb.getSheetAt(0);

		System.out.println("Writing text files");
		Iterator<Row> rowIterator = docInfo.iterator();
		int rowCount=0;
		while (rowIterator.hasNext())
		{
			int i=0;
			int columncount=0;

			Row row = rowIterator.next();
			//For each row, iterate through all the columns
			Iterator<Cell> cellDocIterator = row.cellIterator();

			while (cellDocIterator.hasNext()) 
			{
				Cell cellDoc = cellDocIterator.next();
				try
				{
					if (i >= 1)
					{
						//System.out.println("i>1");
						String a = "";
						String res=cellDoc.getStringCellValue();
						res=res.replaceAll("[\\t\\n\\r]+"," ");
						a = a + "|" + res;

						if (i == 35) {
							a = a + "\r\n";
							System.out.println("Breaking the line here");
						}
						bufferedWriter.write(a);

						System.out.println("Row "+rowCount+" Cell iteration "+i+" Value " + cellDoc.getStringCellValue());
					}
					else
					{
						String a = cellDoc.getStringCellValue();
						bufferedWriter.write(a);
					}
					i++;
					columncount++;
				}
				catch (Exception E)
				{
					System.out.println(E+" While Writing Document info");
					String a = " | ";
					bufferedWriter.write(a);
					columncount++;
					i++;
				}
				rowCount++;
			}
		}
		inp.close();
		bufferedWriter.close();

		//String fileProduct = "/u01/home/dmadmin-np/Geoda3rt/MIDOC_PRODUCT_"+fDate+".txt";
		//String fileProduct = "/u01/home/dmadmin-np/Geodart/MIDOC_PRODUCT_20160722.txt";
		/*String fileProduct="C:/Geodart/MIDOC_PRODUCT_20160915.txt";


		FileWriter fileWriter1 = new FileWriter(fileProduct);
		BufferedWriter bufferedWriter1 = new BufferedWriter(fileWriter1);
		InputStream inp1 = new FileInputStream(sDirectoryPath + "Geodart_test.xlsx");
		Workbook wb1 = WorkbookFactory.create(inp1);
		Sheet prodSheet = wb.getSheetAt(1);

		System.out.println("Writing product Info file");
		Iterator<Row> rowProIterator = prodSheet.iterator();
		//for (Iterator localIterator1 = docInfo.iterator(); localIterator1.hasNext(); columncount <= 32)
		//System.out.println("Pro row iterator");
		while (rowProIterator.hasNext())
		{
			int i=0;
			int columncount=0;
			Row row = rowProIterator.next();
			Iterator<Cell> cellProIterator = row.cellIterator();
			//System.out.println("Pro Cell iterator");
			while (cellProIterator.hasNext()) 
			{
				Cell cellPro = cellProIterator.next();
				try{
					if (i >= 1)
					{
						String a = "";
						//		System.out.println("Pro before");
						a = a + "|" + cellPro.getStringCellValue();
						//		System.out.println("Pro After");
						if (i == row.getLastCellNum() - 1) {
							a = a + "\r\n";
						}
						bufferedWriter1.write(a);
					}
					else
					{
						String a = cellPro.getStringCellValue();
						bufferedWriter1.write(a);
					}
					i++;

				}catch(Exception e)
				{
					//	System.out.println("here");
					System.out.println(e);
					String a = " | ";
					bufferedWriter1.write(a);
					i++;
				}
			}
		}
		inp1.close();
		bufferedWriter1.close();

		String fileRelation = "/u01/home/dmadmin-np/Geoda3rt/MIDOC_RELATIONSHIP_"+fDate+".txt";
		//String fileRelation = "/u01/home/dmadmin-np/Geodart/MIDOC_RELATIONSHIP_20160722.txt";
		//String fileRelation="C:/Geodart/MIDOC_RELATIONSHIP_20160915.txt";

		FileInputStream inp2 = new FileInputStream(sDirectoryPath + "Geodart_test.xlsx");
		FileWriter fileWriter2 = new FileWriter(fileRelation);
		BufferedWriter bufferedWriter2 = new BufferedWriter(fileWriter2);
		Workbook wb2 = WorkbookFactory.create(inp2);
		Sheet relSheet = wb.getSheetAt(2);

		System.out.println("Writing Relation Info file");
		Iterator<Row> rowRelIterator = relSheet.iterator();
		//for (Iterator localIterator1 = docInfo.iterator(); localIterator1.hasNext(); columncount <= 32)
		//System.out.println("Pro row iterator");
		while (rowRelIterator.hasNext())
		{
			int i=0;
			int columncount=0;
			Row row = rowRelIterator.next();
			Iterator<Cell> cellRelIterator = row.cellIterator();
			//	System.out.println("Pro Cell iterator");
			while (cellRelIterator.hasNext()) 
			{
				Cell cellRel = cellRelIterator.next();
				try{
					if (i >= 1)
					{
						String a = "";
						a = a + "|" + cellRel.getStringCellValue();
						if (i == row.getLastCellNum() - 1) {
							a = a + "\r\n";
						}
						bufferedWriter2.write(a);
					}
					else
					{
						String a = cellRel.getStringCellValue();
						bufferedWriter2.write(a);
					}
					row.getLastCellNum();

					i++;
				}catch(Exception e)
				{
					System.out.println(e+" at Relation data");
				}
			}
		}
		inp2.close();
		bufferedWriter2.close(); */

		/*String fileAudit = "/u01/home/dmadmin-np/Geoda3rt/MIDOC_DOCHISTORY_20161118.txt";
		//String fileAudit = "/u01/home/dmadmin-np/Geodart/MIDOC_DOCHISTORY_20160722.txt";
		//String fileAudit="C:/Geodart/MIDOC_DOCHISTORY_20160915.txt";

		FileInputStream inp3 = new FileInputStream(sDirectoryPath + "Geodart_test.xlsx");
		FileWriter fileWriter3 = new FileWriter(fileAudit);
		BufferedWriter bufferedWriter3 = new BufferedWriter(fileWriter3);
		Workbook wb3 = WorkbookFactory.create(inp3);  
		Sheet audSheet = wb.getSheetAt(3);
        Sheet auxSheet=wb.getSheetAt(4);
		System.out.println("Writing Audit Info file");
		Iterator<Row> rowAudIterator = audSheet.iterator();
		Iterator<Row> rowAuxIterator = auxSheet.iterator();

	//for (Iterator localIterator1 = docInfo.iterator(); localIterator1.hasNext(); columncount <= 32)
		//System.out.println("Pro row iterator");
		while (rowAudIterator.hasNext())
		{
			int i=0;
			int columncount=0;
			Row row = rowAudIterator.next();
			if(rowAudIterator.hasNext()==false)
			{
			row=rowAuxIterator.next();
			}
			Iterator<Cell> cellAudIterator = row.cellIterator();
			//	System.out.println("Pro Aud iterator");
			while (cellAudIterator.hasNext()) 
			{
				try{
					Cell cellAud = cellAudIterator.next();
					if (i >= 1)
					{
						String a = "";
						a = a + "|" + cellAud.getStringCellValue();
						if (i == row.getLastCellNum() - 1)
						{
							a = a + "\r\n";
						}
						bufferedWriter3.write(a);
					}
					else
					{
						String a = cellAud.getStringCellValue();
						bufferedWriter3.write(a);
					}
					i++;
				}catch(Exception e)
				{
					System.out.println(e+" at Audit Data ");
				}
			}
			//SftpFiles();
		}
		inp3.close();
		bufferedWriter3.close();*/
	}



	private static HashMap getProductta(IDfSession dfSession2)
			throws DfException
	{
		String resQuery = "select r_object_id,config_product_name,config_ta_name,config_ta_id from mi_config_product";
		IDfQuery query = new DfQuery();

		HashMap<String, ArrayList> proTAInfo = new HashMap();

		System.out.println("Query");
		query.setDQL(resQuery);
		IDfCollection coll = query.execute(dfSession2, 1);
		while (coll.next())
		{
			String product_name = coll.getString("config_product_name");
			String taName = coll.getString("config_ta_name");
			String taId = coll.getString("config_ta_id");

			ArrayList taInfo = new ArrayList();

			taInfo.add(taName);
			taInfo.add(taId);

			proTAInfo.put(product_name, taInfo);
		}
		return proTAInfo;
	}

	private static void SftpFiles() {}

	private static ArrayList gettaInfo(IDfSession dfSession2)
			throws DfException
	{
		String resQuery = "select DISTINCT r_object_id,config_product_id,config_product_name,config_ta_id,config_ta_name from mi_config_product";
		IDfQuery query = new DfQuery();

		HashMap<String, HashMap> taInfo = new HashMap();
		HashMap<String, String> prIdInfo = new HashMap();

		ArrayList results = new ArrayList();

		System.out.println("Query");
		query.setDQL(resQuery);
		IDfCollection coll = query.execute(dfSession2, 1);
		try		{
			while (coll.next())
			{
				String product_name = coll.getString("config_product_name");
				if (taInfo.containsKey(product_name))
				{
					HashMap a = (HashMap)taInfo.get(product_name);
					String taName = coll.getString("config_ta_name");
					String taid = coll.getString("config_ta_id");
					if ((!taid.isEmpty()) && (!taName.isEmpty()))
					{
						a.put(taid, taName);
						taInfo.put(product_name, a);
					}
				}
				else
				{
					HashMap taList = new HashMap();
					String taName = coll.getString("config_ta_name");
					String taid = coll.getString("config_ta_id");
					if ((!taid.isEmpty()) && (!taName.isEmpty()))
					{
						taList.put(taid, taName);
						taInfo.put(product_name, taList);
					}
				}
				String product_id = coll.getString("config_product_id");
				prIdInfo.put(product_name, product_id);
			}
		}
		catch (Exception E)
		{
			System.out.println(E);
		}
		coll.close();
		System.out.println(taInfo);
		System.out.println(prIdInfo);
		results.add(taInfo);
		results.add(prIdInfo);
		return results;
	}

	private static void getRelationInfo(Workbook workbook, SXSSFSheet sheet3, IDfSession dfSession2, DFCHelper dfcHelper, FileOutputStream fileOut, ArrayList relationAtts, HashMap userNames)
			throws DfException
	{
		DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		Calendar cal = Calendar.getInstance();

		cal.add(Calendar.DATE, -730);
		String date=dateFormat.format(cal.getTime());

		String resQuery = "select parent_id,child_id,relation_name from dm_relation "
				+ "where relation_name='mi_response_reference' "
				+ "and parent_id in "
				+ "(select audited_obj_id from dm_audittrail "
				+ "where time_stamp>Date('"+date+"','MM/DD/YYYY') and time_stamp<Date('TODAY'))";
		//+ " time_stamp>Date('12/31/2013','MM/DD/YYYY') "
		//+ "and time_stamp<Date('03/01/2014','MM/DD/YYYY'))";

		IDfCollection relationColl = DFCHelper.executeQuery(dfSession2, resQuery);
		System.out.println("ReferenceUtil: main: query executed " + resQuery);
		ArrayList relationInfo = dfcHelper.getArrayListFromDMColl(relationColl);
		dfcHelper.closeCollection(relationColl);

		Row headerRow = sheet3.createRow(0);
		//relationAtts.size()
		for (int p = 0; p < relationAtts.size(); p++)
		{
			Cell headerCell = headerRow.createCell(p);
			String h = (String)relationAtts.get(p);
			headerCell.setCellValue(h);
		}
		//relationInfo.size()
		for (int i = 0; i < relationInfo.size(); i++)
		{
			HashMap productValues = (HashMap)relationInfo.get(i);

			System.out.println("Getting Product data");

			ArrayList<String> attributeTemp = new ArrayList();
			for (int x = 0; x < relationAtts.size(); x++)
			{
				String attribute = (String)productValues.get(relationAtts.get(x));
				if (userNames.containsKey(attribute))
				{
					String userName = (String)userNames.get(attribute);
					System.out.println("Id:" + attribute + " Changed to " + userName);
					attributeTemp.add(userName);
				}
				else
				{
					attributeTemp.add(attribute);
				}
			}
			Row row1 = sheet3.createRow(i + 1);

			System.out.println("Writing Relationship data");
			for (int y = 0; y < attributeTemp.size(); y++)
			{
				Cell dataCell = row1.createCell(y);
				CellStyle style = workbook.createCellStyle();
				style.setWrapText(true);
				dataCell.setCellValue((String)attributeTemp.get(y));
			}
		}
	}

	private static void getAuditInfo(Workbook workbook, SXSSFSheet sheet4, SXSSFSheet sheet5, IDfSession dfSession2, DFCHelper dfcHelper, FileOutputStream fileOut, ArrayList audittrailAtts, HashMap userNames, HashMap proId, String fDate)
			throws DfException, IOException
	{
		DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		Calendar cal = Calendar.getInstance();

		cal.add(Calendar.DATE, -730);
		//cal.add(Calendar.DATE, -100);
		String date=dateFormat.format(cal.getTime());

		String resQuery="select r_object_id,time_stamp,event_name,user_name,audited_obj_id from dm_audittrail where time_stamp>Date('"+date+"','MM/DD/YYYY')" 
				+"and time_stamp<Date('TODAY') AND EVENT_NAME NOT IN ('affirm document','d2_autolink','d2_change_state','d2_export'"
				+",'d2_security','d2_workflow_change_state_task','dm_checkin','dm_checkout','dm_destroy',"
				+"'dm_link','dm_lock','dm_mark','dm_prune','dm_save','dm_setfile','dm_unlink','dm_unlock',"
				+"'mi_selected_approvers','mi_selected_reviewers','new version created','no data','review approved',"
				+"'review returned','sent for review','set active','set inactive','set inactive document review','set retire')";
		       //  + "time_stamp>Date('08/01/2016','MM/DD/YYYY') and time_stamp<Date('TODAY')";

		IDfCollection relationColl = DFCHelper.executeQuery(dfSession2, resQuery);
		System.out.println("ReferenceUtil: main: query executed " + resQuery);
		ArrayList audittrailInfo = dfcHelper.getArrayListFromDMColl(relationColl);
		dfcHelper.closeCollection(relationColl);


		String fileAudit = "/u01/home/dmadmin-np/Geoda3rt/MIDOC_DOCHISTORY_"+fDate+".txt";
		//String fileAudit="C:/Geodart/MIDOC_DOCHISTORY_20160915.txt";
		FileWriter fileWriter3 = new FileWriter(fileAudit);
		BufferedWriter bufferedWriter3 = new BufferedWriter(fileWriter3);

		Row headerRow = sheet4.createRow(0);
		for (int p = 0; p < audittrailAtts.size(); p++)
		{
			Cell headerCell = headerRow.createCell(p);
			String h = (String)audittrailAtts.get(p);
			headerCell.setCellValue(h);
		}
		try
		{
			System.out.println("Audit Size " + audittrailInfo.size());

			int row_count = 1;
			int row_count1 = 1;
			int count=0;
			//audittrailInfo.size()
			for (int i = 0; i <audittrailInfo.size(); i++)
			{
				try{

					HashMap auditValues = (HashMap)audittrailInfo.get(i);

					System.out.println("Getting Audit data line number "+count+ " out of "+audittrailInfo.size());
					count++;
					int pN = 0;int pIdLen = 0;

					String object_id = (String)auditValues.get("audited_obj_id");
					IDfSysObject Document;
					String objName;

					Document = (IDfSysObject)dfSession2.getObject(new DfId(object_id));
					objName = (String)auditValues.get("object_name");

					String pName = Document.getAllRepeatingStrings("product_name", ",");
					System.out.println("Product_names:" + pName);
					String[] pNames = pName.split(",");

					String product_id = Document.getAllRepeatingStrings("product_id", ",");
					String[] pId = product_id.split(",");
					pIdLen = pId.length;

					System.out.println("Audit product length " + pIdLen);
					for (int p = 0; p < pNames.length; p++)
					{
						ArrayList<String> attributeTemp = new ArrayList();
						System.out.println("ObjectName " + objName + " Row Count :" + row_count);
						for (int x = 0; x < audittrailAtts.size() - 2; x++)
						{
							String att = (String)auditValues.get(audittrailAtts.get(x));
							String user_name = (String)auditValues.get("user_name");
							if (audittrailAtts.get(x).equals("audited_obj_id"))
							{
								try
								{
									String attribute = (String)auditValues.get(audittrailAtts.get(x));
									attributeTemp.add(attribute);
									String product_name = pNames[p];
									String pro_id = (String)proId.get(pNames[p]);
									attributeTemp.add(pro_id);
									attributeTemp.add(product_name);
								}
								catch (Exception e)
								{
									System.out.println(e);
								}
							}
							else if (audittrailAtts.get(x).equals("user_fullName"))
							{
								if (userNames.containsKey(user_name))
								{
									String aux = (String)userNames.get(user_name);
									String userName = "";
									if (aux.length() > 0)
									{
										userName = getcommadelimitedName(aux);
										if (userName.length() > 0) {
											userName = userName + " - " + user_name;
										} else {
											userName = user_name;
										}
									}
									attributeTemp.add(userName);
								}
								else
								{
									attributeTemp.add(att);
								}
							}
							else if (audittrailAtts.get(x).equals("user_FName"))
							{
								if (userNames.containsKey(user_name))
								{
									String aux = (String)userNames.get(user_name);
									String userName = "";
									if (aux.length() > 0) {
										userName = getcommadelimitedName(aux);
									}
									attributeTemp.add(userName);
								}
								else
								{
									attributeTemp.add(user_name);
								}
							}
							else if (audittrailAtts.get(x).equals("time_stamp"))
							{
								String time_stamp = cleanDate(att);
								attributeTemp.add(time_stamp);
							}
							else if (audittrailAtts.get(x).equals("event_name"))	
							{
								String attribute = (String)auditValues.get(audittrailAtts.get(x));
								if(attribute.equals("d2_cancel_checkout"))
								{
									attributeTemp.add("Document Cancel Checkout");
								}else if(attribute.equals("d2_checkin"))
								{
									attributeTemp.add("Document Checkin");
								}
								else if(attribute.equals("d2_checkout"))
								{
									attributeTemp.add("Document Checkout");
								}
								else if(attribute.equals("d2_create"))
								{
									attributeTemp.add("Document Created");
								}
								else if(attribute.equals("d2_destroy"))
								{
									attributeTemp.add("Document Destroyed");
								}
								else if(attribute.equals("d2_edit"))
								{
									attributeTemp.add("Document Edited");
								}
								else if(attribute.equals("d2_import"))
								{
									attributeTemp.add("Document imported");
								}
								else if(attribute.equals("d2_properties_save"))
								{
									attributeTemp.add("Document properties save");
								}
								//Workflows			
								else if(attribute.equals("d2_workflow_aborted"))
								{
									attributeTemp.add("Workflow Aborted");
								}
								else if(attribute.equals("d2_workflow_acquired_task"))
								{
									attributeTemp.add("Workflow Acquired");
								}
								else if(attribute.equals("d2_workflow_added_note"))
								{
									attributeTemp.add("Workflow Note Added");
								}
								else if(attribute.equals("d2_workflow_delegated_task"))
								{
									attributeTemp.add("Workflow Delegated");
								}
								else if(attribute.equals("d2_workflow_forwarded_task"))
								{
									attributeTemp.add("Workflow Forwarded");
								}
								else if(attribute.equals("d2_workflow_rejected_task"))
								{
									attributeTemp.add("Workflow Rejected");
								}
								else if(attribute.equals("d2_workflow_started"))
								{
									attributeTemp.add("Workflow Started");
								}
								//References			
								else if(attribute.equals("mi_attach_reference"))
								{
									attributeTemp.add("Attach Reference");
								}
								else if(attribute.equals("mi_guest_search"))
								{
									attributeTemp.add("Guest Search");
								}
								else if(attribute.equals("mi_remove_reference"))
								{
									attributeTemp.add("Remove Reference");
								}
								else if(attribute.equals("mi_response_reaffirm"))
								{
									attributeTemp.add("Response Reaffirm");
								}
								else if(attribute.equals("mi_update_reference"))
								{
									attributeTemp.add("Update Reference");
								}else{
									attributeTemp.add("Null");
								}


							}
							else
							{
								attributeTemp.add(att);
							}
						}

						int x=0;
						System.out.println("Row length "+attributeTemp.size());
						for (int y = 0; y <=attributeTemp.size()-1; y++)
						{
							//Cell dataCell = row1.createCell(y);
							//dataCell.setCellValue((String)attributeTemp.get(y));						

							try{
								if (x >= 1)
								{
									String a = "";
									a = a + "|" + attributeTemp.get(y);
									if (x == attributeTemp.size()-1)
									{
										a = a + "\r\n";
									}
									bufferedWriter3.write(a);
								}
								else                                                 
								{
									String a =attributeTemp.get(y);
									bufferedWriter3.write(a);
								}
								x++;

							}catch(Exception e)
							{
								System.out.println(e+" at Audit Data ");
							}
						}
						row_count++;
					}
				}catch(Exception e){
					System.out.println("Inner loop audit trail exceptions "+e);
				}
			}
			bufferedWriter3.close();
		}
		catch (Exception e)
		{
			System.out.println("Audit trail exception "+e);
		}
	}



	private static void getProductInfo(Workbook workbook, SXSSFSheet sheet2, IDfSession dfSession2, DFCHelper dfcHelper, FileOutputStream fileOut, ArrayList productAtts, HashMap userNames, HashMap taInfo)
			throws DfException, IOException
	{
		String resQuery = "select config_product_id,config_product_name,config_product_owner from mi_config_product ";

		IDfCollection productColl = DFCHelper.executeQuery(dfSession2, resQuery);
		System.out.println("ReferenceUtil: main: query executed " + resQuery);
		ArrayList productInfo = dfcHelper.getArrayListFromDMColl(productColl);
		dfcHelper.closeCollection(productColl);

		Row headerRow = sheet2.createRow(0);
		for (int p = 0; p < productAtts.size(); p++)
		{
			Cell headerCell = headerRow.createCell(p);
			String h = (String)productAtts.get(p);
			headerCell.setCellValue(h);
		}
		for (int i = 0; i < productInfo.size(); i++)
		{
			HashMap productValues = (HashMap)productInfo.get(i);

			System.out.println("Getting Product data");
			String objectId = (String)productValues.get("r_object_id");

			String ownername = (String)productValues.get("config_product_owner");

			System.out.println("owner Name " + ownername);

			ArrayList<String> attributeTemp = new ArrayList();
			for (int x = 0; x < productAtts.size(); x++)
			{
				String attribute = (String)productValues.get(productAtts.get(x));
				if (productAtts.get(x).equals("powner_fullName"))
				{
					if (userNames.containsKey(ownername))
					{
						String aux = (String)userNames.get(ownername);
						String powner_fullName = "";
						if (aux.length() > 0) {
							powner_fullName = getcommadelimitedName(aux);
						}
						if (powner_fullName.length() > 0) {
							powner_fullName = powner_fullName + " - " + ownername;
						} else {
							powner_fullName = powner_fullName;
						}
						attributeTemp.add(powner_fullName);
					}
					else
					{
						attributeTemp.add(ownername);
					}
				}
				else if (productAtts.get(x).equals("powner_Name"))
				{
					if (userNames.containsKey(ownername))
					{
						String aux = (String)userNames.get(ownername);
						String config_product_owner_firstName = "";
						if (aux.length() > 0) {
							config_product_owner_firstName = getcommadelimitedName(aux);
						}
						attributeTemp.add(config_product_owner_firstName);
					}
					else
					{
						attributeTemp.add(ownername);
					}
				}
				else if (!productAtts.get(x).equals("ta_id")) {
					if (!productAtts.get(x).equals("ta_name")) {
						if (productAtts.get(x).equals("config_product_name"))
						{
							attributeTemp.add(attribute);
							if (taInfo.containsKey(attribute))
							{
								HashMap ta = (HashMap)taInfo.get(attribute);

								String ta_name = " ";
								String ta_id = " ";
								Iterator<String> iterusers = ta.keySet().iterator();
								while (iterusers.hasNext())
								{
									int a1 = 0;
									String key = (String)iterusers.next();

									System.out.println("Key " + key + " Value " + ta.get(key));
									ta_id = key;
									ta_name = (String)ta.get(key);
									a1++;
								}
								attributeTemp.add(ta_id);
								attributeTemp.add(ta_name);
							}
						}
						else
						{
							attributeTemp.add(attribute);
						}
					}
				}
			}
			Row row1 = sheet2.createRow(i + 1);

			System.out.println("Writing Product data");
			for (int y = 0; y < attributeTemp.size(); y++)
			{
				Cell dataCell = row1.createCell(y);
				dataCell.setCellValue((String)attributeTemp.get(y));
			}
		}
	}

	private static ArrayList getAtts(String commonAttributes)
	{
		ArrayList commonAtts = new ArrayList();
		String[] splitAts = commonAttributes.split(",");
		for (int i = 0; i < splitAts.length; i++)
		{
			commonAtts.add(splitAts[i]);
			System.out.println("Adding the attribute " + splitAts[i]);
		}
		return commonAtts;
	}

	private static void getDocumentInfo(Workbook workbook, SXSSFSheet sheet1, IDfSession dfSession2, DFCHelper dfcHelper, FileOutputStream fileOut, ArrayList commonAtts, ArrayList refAtts, HashMap userNames, HashMap proTainfo, HashMap proId2)
			throws DfException, IOException
	{
		DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		Calendar cal = Calendar.getInstance();

		cal.add(Calendar.DATE, -730);
		String date=dateFormat.format(cal.getTime());
		String resQuery = "select r_object_id,i_chronicle_id,object_name,r_version_label,"
				+ "title,r_object_type,response_type,comment_text,owner_name,r_creation_date,"
				+ "r_creator_name,approved_date,approved_by,active,category_id,category_name,"
				+ "product_id,product_name,ta_id,ta_name,topic_id,topic_name,a_status,owner_name,"
				+ "expiration_date,retired_date,retired_by from mi_response(all) "
				+ "where r_object_id in (select audited_obj_id from dm_audittrail where time_stamp>Date('"+date+"','MM/DD/YYYY') and time_stamp<=Date('TODAY'))";

		System.out.println("ReferenceUtil: main: query executed " + resQuery);
		IDfCollection resColl = DFCHelper.executeQuery(dfSession2, resQuery);

		ArrayList responseInfo = dfcHelper.getArrayListFromDMColl(resColl);
		System.out.println("Number of responses "+responseInfo.size());
		dfcHelper.closeCollection(resColl);

		/*String refQuery = "select r_object_id,i_chronicle_id,object_name,r_version_label,title,r_object_type,reference_type,comment_text,owner_name,r_creation_date,r_creator_name,approved_date,"
				+ "	approved_by,a_status,active,category_id,category_name,"
				+ "	product_id,product_name,ta_id,ta_name,topic_id,"
				+ "	topic_name,reference_citation,reference_number,"
				+ "	reference_publish_city,reference_publish_date,"
				+ "	reference_publish_state,reference_publish_zip,reference_publisher_name"
				+ "	from mi_reference(all) where r_object_id in"
				+ "	(select audited_obj_id from dm_audittrail where "
				+ "	time_stamp>Date('12/31/2013','MM/DD/YYYY') and time_stamp<Date('02/01/2014','MM/DD/YYYY') and object_type='mi_reference')";*/


		String refQuery="Select r_object_id,i_chronicle_id,object_name,r_version_label,title,r_object_type,reference_type,comment_text,owner_name,r_creation_date,r_creator_name,approved_date,"
				+ "	approved_by,a_status,active,category_id,category_name,"
				+ "	product_id,product_name,ta_id,ta_name,topic_id,"
				+ "	topic_name,reference_citation,reference_number,"
				+ "	reference_publish_city,reference_publish_date,retired_date,retired_by,"
				+ "	reference_publish_state,reference_publish_zip,reference_publisher_name from mi_reference(all) where r_modify_date>Date('"+date+"','MM/DD/YYYY')";
 
		/*String refQuery="Select r_object_id,i_chronicle_id,object_name,r_version_label,title,r_object_type,reference_type,comment_text,owner_name,r_creation_date,r_creator_name,approved_date,"
				+ "	approved_by,a_status,active,category_id,category_name,"
				+ "	product_id,product_name,ta_id,ta_name,topic_id,"
				+ "	topic_name,reference_citation,reference_number,"
				+ "	reference_publish_city,reference_publish_date,retired_date,retired_by,"
				+ "	reference_publish_state,reference_publish_zip,reference_publisher_name from mi_reference where"
				//+ "	reference_publish_state,reference_publish_zip,reference_publisher_name from mi_reference(all) where i_chronicle_id in "
				//+ "(select child_id from dm_relation where relation_name='mi_response_reference') "
				+ " approved_date>=Date('11/20/2017','MM/DD/YYYY') and approved_date<Date('TODAY')";*/


		System.out.println("ReferenceUtil: main: query executed " + refQuery);
		IDfCollection refColl = DFCHelper.executeQuery(dfSession2, refQuery);

		ArrayList referenceInfo = dfcHelper.getArrayListFromDMColl(refColl);
		System.out.println("Number of references "+referenceInfo.size());
		dfcHelper.closeCollection(refColl);

		int columnCount = 0;

		int row_count = 1;
		Row headerRow = sheet1.createRow(0);
		ArrayList<String> masterList = new ArrayList();
		masterList.addAll(commonAtts);
		//masterList.addAll(resAtts);
		masterList.addAll(refAtts);

		HashMap<String, ArrayList> productConsistency = new HashMap();
		ArrayList prodcts = new ArrayList();
		for (int p = 0; p < masterList.size(); p++)
		{
			Cell headerCell = headerRow.createCell(p);
			headerCell.setCellValue((String)masterList.get(p));
		}
		//responseInfo.size() 
		for (int i = 0; i < responseInfo.size(); i++)
		{
			try {


				HashMap hmResponse = (HashMap)responseInfo.get(i);

				System.out.println("Getting Response data");
				int pN = 0;int tN = 0;

				String object_id = (String)hmResponse.get("r_object_id");
				IDfSysObject document = (IDfSysObject)dfSession2.getObject(new DfId(object_id));

				String objName = (String)hmResponse.get("object_name");

				String pName = document.getAllRepeatingStrings("product_name", ",");
				System.out.println("Product_names:" + pName);
				String[] pNames = pName.split(",");
				pN = pNames.length;

				String tName = document.getAllRepeatingStrings("ta_name", ",");
				System.out.println("TA_names:" + tName);
				String[] tNames = tName.split(",");
				tN = tNames.length;

				System.out.println("Ta length: " + tN + " Product Length: " + pN);
				for (int q = 0; q < pN; q++)
				{
					System.out.println("ObjectName " + objName + " Row Count :" + row_count + " Product_name " + pNames[q] + " ta_name   ta_name count  pName count" + q + "  Ta_name total count " + tN + " PName total count " + tN);
					ArrayList<String> attributeTemp = new ArrayList();
					for (int x = 0; x < commonAtts.size(); x++)
					{
						String attribute = (String)hmResponse.get(commonAtts.get(x));
						String r_object_id = (String)hmResponse.get("r_object_id");
						String r_object_type = (String)hmResponse.get("r_object_type");
						String r_creator_name = (String)hmResponse.get("r_creator_name");
						String author_name=(String)hmResponse.get("owner_name");
						String approved_by = (String)hmResponse.get("approved_by");
						String retired_by = (String)hmResponse.get("retired_by");
						if (retired_by.length() > 8) {
							retired_by = " ";
						}
						IDfSysObject dfDocument = (IDfSysObject)dfSession2.getObject(new DfId(r_object_id));
						if (commonAtts.get(x).equals("category_id"))
						{
							String cat_id = dfDocument.getAllRepeatingStrings("category_id", ",");
							attributeTemp.add(cat_id);
						}
						else if (commonAtts.get(x).equals("category_name"))
						{
							System.out.println("Entering Category name de duplication");
							String category_name = dfDocument.getAllRepeatingStrings("category_name", ",");
							System.out.println("Cat Name " + category_name);
							String[] a = category_name.split(",");
							String aux = "";
							HashMap<String, String> deDup = new HashMap();
							for (int p = 0; p < a.length; p++) {
								deDup.put(a[p], "0");
							}
							System.out.println("added keys to the hashMap");
							for (String key : deDup.keySet()) {
								if (deDup.size() > 1)
								{
									System.out.println("aux + key " + aux + " " + key);
									aux = aux + "," + key;
								}
								else
								{
									aux = key;
								}
							}
							System.out.println("Unique Category " + aux);
							if (aux.substring(0, 0).equals(",")) {
								aux = aux.substring(1);
							}
							attributeTemp.add(aux);
						}
						else if (commonAtts.get(x).equals("product_id"))
						{
							try
							{
								String product_id = (String)proId2.get(pNames[q]);
								attributeTemp.add(product_id);
							}
							catch (Exception e)
							{
								System.out.println(e);
							}
						}
						else if (commonAtts.get(x).equals("product_name"))
						{
							attributeTemp.add(pNames[q]);
						}
						else if (commonAtts.get(x).equals("ta_id"))
						{
							try
							{
								if (proTainfo.containsKey(pNames[q]))
								{
									ArrayList ab = new ArrayList();
									ab = (ArrayList)proTainfo.get(pNames[q]);
									String ta_id = (String)ab.get(1);
									System.out.println("TA ID " + ta_id);
									attributeTemp.add(ta_id);
								}
								else
								{
									attributeTemp.add(" ");
								}
							}
							catch (Exception E)
							{
								System.out.println(E);
							}
						}
						else if (commonAtts.get(x).equals("ta_name"))
						{
							System.out.println("TA NAME");
							if (proTainfo.containsKey(pNames[q]))
							{
								ArrayList ab = new ArrayList();
								ab = (ArrayList)proTainfo.get(pNames[q]);
								String ta_name = (String)ab.get(0);
								System.out.println("TA Name " + ta_name);
								attributeTemp.add(ta_name);
							}
							else
							{
								attributeTemp.add(" ");
							}
						}
						else if (commonAtts.get(x).equals("topic_id"))
						{
							String topic_id = dfDocument.getAllRepeatingStrings("topic_id", ",");
							attributeTemp.add(topic_id);
						}
						else if (commonAtts.get(x).equals("topic_name"))
						{
							String topic_name = dfDocument.getAllRepeatingStrings("topic_name", ",");
							attributeTemp.add(topic_name);
						}
						else if (commonAtts.get(x).equals("creator_fullName"))
						{
							if (userNames.containsKey(r_creator_name))
							{
								String aux = (String)userNames.get(r_creator_name);
								String creator_fullName = "";
								if (aux.length() > 1)
								{
									creator_fullName = getcommadelimitedName(aux);
									if (creator_fullName.length() > 0) {
										creator_fullName = creator_fullName + " - " + r_creator_name;
									} else {
										creator_fullName = r_creator_name;
									}
								}
								attributeTemp.add(creator_fullName);
							}
							else
							{
								attributeTemp.add(r_creator_name);
							}
						}
						else if (commonAtts.get(x).equals("approved_by_fullName"))
						{
							if (userNames.containsKey(approved_by))
							{
								String aux = (String)userNames.get(approved_by);
								String approved_by_fullName = "";
								if (aux.length() > 1)
								{
									approved_by_fullName = getcommadelimitedName(aux);
									if (approved_by_fullName.length() > 0) {
										approved_by_fullName = approved_by_fullName + " - " + approved_by;
									} else {
										approved_by_fullName = approved_by;
									}
								}
								if (approved_by_fullName.contains("null")) {
									attributeTemp.add(" ");
								} else {
									attributeTemp.add(approved_by_fullName);
								}
							}
							else
							{
								attributeTemp.add(approved_by);
							}
						}
						else if (commonAtts.get(x).equals("created_byName"))
						{
							if (userNames.containsKey(r_creator_name))
							{
								String aux = (String)userNames.get(r_creator_name);
								String created_byName = "";
								if (aux.length() > 0) {
									created_byName = getcommadelimitedName(aux);
								}
								attributeTemp.add(created_byName);
							}
							else
							{
								attributeTemp.add(r_creator_name);
							}
						}
						else if (commonAtts.get(x).equals("approved_byName"))
						{
							if (userNames.containsKey(approved_by))
							{
								String aux = (String)userNames.get(approved_by);
								String approved_byName = "";
								if (aux.length() > 1) {
									approved_byName = getcommadelimitedName(aux);
								}
								attributeTemp.add(approved_byName);
							}
							else
							{
								attributeTemp.add(approved_by);
							}
						}
						else if (commonAtts.get(x).equals("approved_date"))
						{
							String approved_date = cleanDate(attribute);
							attributeTemp.add(approved_date);
						}
						else if (commonAtts.get(x).equals("r_creation_date"))
						{
							String r_creation_date = cleanDate(attribute);
							attributeTemp.add(r_creation_date);
						}
						else if (commonAtts.get(x).equals("active"))
						{
							String active = (String)hmResponse.get("active");
							if (active.equals("0")) {
								active = "N";
							} else if (active.equals("1")) {
								active = "Y";
							}
							attributeTemp.add(active);
						}
						else if (commonAtts.get(x).equals("response_type"))
						{
							String type = (String)hmResponse.get("response_type");
							String objtype = "Response";
							String a = getType(objtype, type);
							attributeTemp.add(a);
						}
						else if (commonAtts.get(x).equals("title"))
						{
							String a = "?,=,[,],[,+,&,|,!,(,),{,},^,~,*,?,�,:,-,�,�,�,@,�,�,?";
							String[] escape = a.split(",");
							for (int y = 0; y < escape.length; y++) {
								if (attribute.contains(escape[y])) {
									attribute = attribute.replace(escape[y], " ");
								}
							}
							attribute = attribute.replace("\n", " ").replace("\r", " ");
							attributeTemp.add(attribute);
						}
						else if (commonAtts.get(x).equals("r_version_label"))
						{
							String version = dfSession2.apiGet("get", r_object_id + ",r_version_label");
							String a = null;
							try{
								System.out.println("Version attribute "+attribute);
								/*if (attribute.contains("CURRENT")) {
								version = version + " , " + "Current";
							}*/
								a=document.getVersionLabel(1);
								if (a.contains("CURRENT")) {
									version = version + " , " + "Current";
								}
							}catch(Exception e)
							{
								System.out.println("version_error");
							}
							System.out.println("Version Label " + version+ " Get V Label "+a);
							attributeTemp.add(version);
						}else if (commonAtts.get(x).equals("author"))
						{
							System.out.println("Just Author");
							String author=(String)hmResponse.get("owner_name");
							attributeTemp.add(author);
						}else if (commonAtts.get(x).equals("author_Name"))
						{
							System.out.println("Entering author name");
							String author=(String)hmResponse.get("owner_name");
							if (userNames.containsKey(author))
							{
								String aux = (String)userNames.get(author);
								String created_byName = "";
								if (aux.length() > 0) {
									created_byName = getcommadelimitedName(aux);
								}
								attributeTemp.add(created_byName);
								System.out.println("Exiting author name  "+created_byName);

							}
							else
							{
								System.out.println("Exiting author name "+author);
								attributeTemp.add(author);
							}

						}else if (commonAtts.get(x).equals("author_fullName"))
						{
							if (userNames.containsKey(author_name))
							{
								System.out.println("Exiting author Full name "+author_name);
								String author=(String)hmResponse.get("owner_name");
								String aux = (String)userNames.get(author);
								String author_fullName = "";
								if (aux.length() > 1)
								{
									author_fullName = getcommadelimitedName(aux);
									if (author_fullName.length() > 0) {
										author_fullName = author_fullName + " - " + author;
									} else {
										author_fullName = author;
									}
								}
								if (author_fullName.contains("null")) {
									attributeTemp.add(" ");
								} else {
									attributeTemp.add(author_fullName);
								}
							}
							else
							{
								attributeTemp.add(author_name);
							}
						}else if (commonAtts.get(x).equals("expiration_date"))
						{
							String expiration_date = cleanDate(attribute);
							attributeTemp.add(expiration_date);
						} 
						else if (commonAtts.get(x).equals("retired_date"))
						{
							String retired_date = cleanDate(attribute);
							attributeTemp.add(retired_date);
						}else if (commonAtts.get(x).equals("retired_by_fullName"))
						{
							if (userNames.containsKey(retired_by))
							{
								String aux = (String)userNames.get(retired_by);
								String retired_by_fullName = "";
								retired_by_fullName = getcommadelimitedName(aux);
								if (retired_by_fullName.length() > 0) {
									retired_by_fullName = retired_by_fullName + " - " + retired_by;
								} else {
									retired_by_fullName = retired_by;
								}
								attributeTemp.add(retired_by_fullName);
							}
							else
							{
								attributeTemp.add(retired_by);
							}
						}
						else if (commonAtts.get(x).equals("retired_byName"))
						{

							if (retired_by.length() > 8) {
								retired_by = " ";
							}
							if (userNames.containsKey(retired_by))
							{
								String aux = (String)userNames.get(retired_by);
								String retired_byName = "";
								if (aux.length() > 1) {
									retired_byName = getcommadelimitedName(aux);
								}
								attributeTemp.add(retired_byName);
							}
							else
							{
								attributeTemp.add(retired_by);
							}
						}
						else if (commonAtts.get(x).equals("retired_by"))
						{

							if (retired_by.length() > 8) {
								retired_by = " ";
							}
							if (retired_by.length() > 8) {
								retired_by = " ";
							}
							attributeTemp.add(retired_by);
						}
						else
						{
							attributeTemp.add(attribute);
						}
					}
					Row row = sheet1.createRow(row_count);
					row_count++;
					for (int y = 0; y < attributeTemp.size(); y++)
					{
						Cell dataCell = row.createCell(y);
						dataCell.setCellValue((String)attributeTemp.get(y));
					}
					int p = attributeTemp.size();
					Cell dataCell = row.createCell(p);
					dataCell.setCellValue("");
					Cell dataCell1 = row.createCell(p + 1);
					dataCell.setCellValue("");

					columnCount = commonAtts.size();
				}
				//i = responseInfo.size() - 1;
				if (i == responseInfo.size() - 1)
				{
					System.out.println("Entering Reference Data retrieval");
					int ref_rowC = 0;
					//referenceInfo.size()

					for (int j = 0; j <=referenceInfo.size(); j++)
					{
						try{
						//System.out.println("Ref info size "+referenceInfo.size());
						System.out.println("***************Reference attributes loop*************** "+j);
						HashMap refData = (HashMap) referenceInfo.get(j);

						String object_id1 = (String)refData.get("r_object_id");
						String objctName = (String)refData.get("object_name");
						IDfSysObject Document1 = (IDfSysObject)dfSession2.getObject(new DfId(object_id1));

						String product_name = Document1.getAllRepeatingStrings("product_name", ",");
						String[] proName = product_name.split(",");
						int proLen = proName.length;
						System.out.println("Getting Ref data to excel XX Pro len "+proLen);

						System.out.println("ObjectName " + objctName + " Row Count :" + ref_rowC+ " out of "+referenceInfo.size());

						for (int q = 0; q < proLen; q++)
						{
							ArrayList<String> attributeRefTemp = new ArrayList();
							ArrayList<String> refAttforEx = new ArrayList();
							int refCol = columnCount;

							for (int x = 0; x < commonAtts.size(); x++)
							{
								String r_creator_name = (String)refData.get("r_creator_name");
								String approved_by = (String)refData.get("approved_by");
								String r_object_id = (String)refData.get("r_object_id");
								String attribute = (String)refData.get(commonAtts.get(x));
								IDfSysObject dfDocument = (IDfSysObject)dfSession2.getObject(new DfId(object_id1));
								if (commonAtts.get(x).equals("ta_id"))
								{
									try
									{
										if (proTainfo.containsKey(proName[q]))
										{
											ArrayList ab = new ArrayList();
											ab = (ArrayList)proTainfo.get(proName[q]);
											attributeRefTemp.add((String)ab.get(1));
										}
										else
										{
											attributeRefTemp.add(" ");
										}
									}
									catch (Exception localException1) {}
								}
								else if (commonAtts.get(x).equals("ta_name"))
								{
									if (proTainfo.containsKey(proName[q]))
									{
										ArrayList ab = new ArrayList();
										ab = (ArrayList)proTainfo.get(proName[q]);
										attributeRefTemp.add((String)ab.get(0));
									}
									else
									{
										attributeRefTemp.add(" ");
									}
								}
								else if (commonAtts.get(x).equals("topic_id"))
								{
									String product_id = dfDocument.getAllRepeatingStrings("topic_id", ",");
									attributeRefTemp.add(product_id);
								}
								else if (commonAtts.get(x).equals("topic_name"))
								{
									String product_id = dfDocument.getAllRepeatingStrings("topic_name", ",");
									attributeRefTemp.add(product_id);
								}
								else if (commonAtts.get(x).equals("category_id"))
								{
									String product_id = dfDocument.getAllRepeatingStrings("category_id", ",");

									attributeRefTemp.add(product_id);
								}
								else if (commonAtts.get(x).equals("category_name"))
								{
									try
									{
										String category_name = dfDocument.getAllRepeatingStrings("category_name", ",");
										String[] a = category_name.split(",");
										String aux = "";
										System.out.println("Category Name");
										HashMap<String, String> deDup = new HashMap();
										for (int p1 = 0; p1 < a.length; p1++) {
											deDup.put(a[p1], "0");
										}
										for (String key : deDup.keySet()) {
											if (deDup.size() > 1)
											{
												System.out.println("aux + key " + aux + " " + key);
												aux = aux + "," + key;
											}
											else
											{
												aux = key;
											}
										}
										attributeRefTemp.add(aux);
									}
									catch (Exception e)
									{
										System.out.println(e);
									}
								}
								else if (commonAtts.get(x).equals("product_id"))
								{
									String proId = dfDocument.getAllRepeatingStrings("product_id", ",");
									String[] product_Id = proId.split(",");
									attributeRefTemp.add(product_Id[q]);
								}
								else if (commonAtts.get(x).equals("product_name"))
								{
									attributeRefTemp.add(proName[q]);
								}
								else if (commonAtts.get(x).equals("creator_fullName"))
								{
									if (userNames.containsKey(r_creator_name))
									{
										String aux = (String)userNames.get(r_creator_name);
										String creator_fullName = getcommadelimitedName(aux);
										if (creator_fullName.length() > 0) {
											creator_fullName = creator_fullName + " - " + r_creator_name;
										} else {
											creator_fullName = r_creator_name;
										}
										attributeRefTemp.add(creator_fullName);
									}
									else
									{
										attributeRefTemp.add(r_creator_name);
									}
								}
								else if (commonAtts.get(x).equals("approved_by_fullName"))
								{
									if (userNames.containsKey(approved_by))
									{
										String aux = (String)userNames.get(approved_by);
										String approved_by_fullName = "";
										if (aux.length() > 0) {
											approved_by_fullName = getcommadelimitedName(aux);
										}
										if (approved_by_fullName.length() > 0) {
											approved_by_fullName = approved_by_fullName + " - " + approved_by;
										} else {
											approved_by_fullName = approved_by;
										}
										attributeRefTemp.add(approved_by_fullName);
									}
									else
									{
										attributeRefTemp.add(approved_by);
									}
								}
								else if (commonAtts.get(x).equals("created_byName"))
								{
									if (userNames.containsKey(r_creator_name))
									{
										String aux = (String)userNames.get(r_creator_name);
										String created_byName = "";
										if (aux.length() > 0) {
											created_byName = getcommadelimitedName(aux);
										}
										attributeRefTemp.add(created_byName);
									}
									else
									{
										attributeRefTemp.add(r_creator_name);
									}
								}
								else if (commonAtts.get(x).equals("approved_byName"))
								{
									if (userNames.containsKey(approved_by))
									{
										String aux = (String)userNames.get(approved_by);
										String approved_byName = "";
										if (aux.length() > 0) {
											approved_byName = getcommadelimitedName(aux);
										}
										attributeRefTemp.add(approved_byName);
									}
									else
									{
										attributeRefTemp.add(approved_by);
									}
								}
								else if (commonAtts.get(x).equals("response_type"))
								{
									String ref_type = (String)refData.get("reference_type");
									String type = "Reference";

									String reference_type = getType(type, ref_type);
									attributeRefTemp.add(reference_type);
								}
								else if (commonAtts.get(x).equals("approved_date"))
								{
									String approved_date = cleanDate(attribute);
									System.out.println("Approved date "+approved_date);
									attributeRefTemp.add(approved_date);
								}
								else if (commonAtts.get(x).equals("r_creation_date"))
								{
									String r_creation_date = cleanDate(attribute);
									attributeRefTemp.add(r_creation_date);
								}
								else if (commonAtts.get(x).equals("title"))
								{
									String a = "?,=,[,],[,+,&,|,!,(,),{,},^,~,*,?,�,:,-,�,�,�,@,�,�,?";
									String[] escape = a.split(",");
									for (int y = 0; y < escape.length; y++) {
										if (attribute.contains(escape[y])) {
											attribute = attribute.replace(escape[y], " ");
										}
									}
									attribute = attribute.replace("\n", " ").replace("\r", " ");
									attributeRefTemp.add(attribute);
								}
								else if (commonAtts.get(x).equals("active"))
								{
									String active = (String)refData.get("active");
									System.out.println("active " + active);
									if (active.equals("0")) {
										active = "N";
									} else if (active.equals("1")) {
										active = "Y";
									}
									attributeRefTemp.add(active);
								}
								else if (commonAtts.get(x).equals("r_version_label"))
								{
									String a=null;
									String version = dfSession2.apiGet("get", object_id1 + ",r_version_label");
									try{
										/*if (attribute.contains("CURRENT")) {
										version = version + " , " + "Current";
									}*/
										a=document.getVersionLabel(1);
										if (a.contains("CURRENT")) {
											version = version + " , " + "Current";
										}

									}catch(Exception e)
									{
										System.out.println("Reference version error");
									}
									System.out.println("Version Label " + version+ " Get V Label "+a);
									attributeRefTemp.add(version);
								}
								else if (commonAtts.get(x).equals("author"))
								{
									System.out.println("Just Author");
									String author=(String)refData.get("owner_name");
									attributeRefTemp.add(author);
								}else if (commonAtts.get(x).equals("author_Name"))
								{
									System.out.println("Entering author name");
									String author=(String)refData.get("owner_name");
									if (userNames.containsKey(author))
									{
										String aux = (String)userNames.get(author);
										String created_byName = "";
										if (aux.length() > 0) {
											created_byName = getcommadelimitedName(aux);
										}
										attributeRefTemp.add(created_byName);
										System.out.println("Exiting author name  "+created_byName);
									}
									else
									{
										System.out.println("Exiting author name "+author);
										attributeRefTemp.add(author);
									}
								}
								else if (commonAtts.get(x).equals("author_fullName"))
								{
									String author=(String)refData.get("owner_name");
									if (userNames.containsKey(author))
									{
										System.out.println("Exiting author Full name "+author);
										String authorName=(String)refData.get("owner_name");
										String aux = (String)userNames.get(authorName);
										String author_fullName = "";
										if (aux.length() > 1)
										{
											author_fullName = getcommadelimitedName(aux);
											if (author_fullName.length() > 0) {
												author_fullName = author_fullName + " - " + author;
											} else {
												author_fullName = author;
											}
										}
										if (author_fullName.contains("null")) {
											attributeRefTemp.add(" ");
										} else {
											attributeRefTemp.add(author_fullName);
										}
									}else
									{
										attributeRefTemp.add(author);
									}
								}else if (commonAtts.get(x).equals("expiration_date"))
								{
									attributeRefTemp.add(" ");
								}else if (commonAtts.get(x).equals("retired_date"))
								{
									String r_creation_date = cleanDate(attribute);
									attributeRefTemp.add(r_creation_date);
								}else if(commonAtts.get(x).equals("retired_by"))
								{
									attributeRefTemp.add(attribute);
								}else if(commonAtts.get(x).equals("retired_byName"))
								{
									//refAttforEx.add(attribute);
									String retBy=(String)refData.get("retired_by");
									if (userNames.containsKey(retBy))
									{
										String aux = (String)userNames.get(retBy);
										String retired_byName = "";
										if (aux.length() > 0) {
											retired_byName = getcommadelimitedName(aux);
										}
										attributeRefTemp.add(retired_byName);
										System.out.println("Exiting Retired By Ref name  "+retired_byName);
									}
									else
									{
										System.out.println("Retired By Ref name   "+retBy);
										attributeRefTemp.add(retBy);
									}
								}else if(commonAtts.get(x).equals("retired_by_fullName"))
								{
									String retBy=(String)refData.get("retired_by");
									if (userNames.containsKey(retBy))
									{
										try{
											System.out.println("Retired By Ref Full name  "+retBy);
											String retiredName=(String)refData.get("retired_by");
											String aux = (String)userNames.get(retiredName);
											String ret_fullName = "";

											if(!aux.equals(null))
											{
												if (aux.length() > 1)
												{
													ret_fullName = getcommadelimitedName(aux);
													if (ret_fullName.length() > 0) {
														ret_fullName = ret_fullName + " - " + retBy;
													} else {
														ret_fullName = retBy;
													}
												}
											}
											if (ret_fullName.contains("null")) {
												attributeRefTemp.add(" ");
											} else {
												attributeRefTemp.add(ret_fullName);
											}
										}catch(Exception e){
											System.out.println(e);
										}
									}
									else
									{
										attributeRefTemp.add(attribute);
									}
								}
								else
								{
									attributeRefTemp.add(attribute);
								}
							}

							//Writing ref Attributes
							for (int x1 = 0; x1 < refAtts.size(); x1++)
							{
								String attribute1 = (String)refData.get(refAtts.get(x1));
								//System.out.println("***************Reference attributes***************"+refAtts.get(x1));
								if (refAtts.get(x1).equals("reference_citation"))
								{
									String a = "?,=,[,],[,+,&,|,!,(,),{,},^,~,*,?,�,:,-,�,�,�,@,�,�,?,/";
									String[] escape = a.split(",");
									for (int y = 0; y < escape.length; y++) {
										if (attribute1.contains(escape[y])) {
											attribute1 = attribute1.replace(escape[y], " ");
										}
									}
									refAttforEx.add(attribute1);
								}else if (refAtts.get(x1).equals("reference_number"))
								{
									System.out.println("Reference number "+(String)refData.get("reference_number"));
									String refNo=(String)refData.get("reference_number");
									if(refNo.equals(" "))
									{
										refAttforEx.add(" ");
									}else{
										refAttforEx.add(refNo);
									}
								}
							}
							int rownum=sheet1.getLastRowNum();
							Row row1 = sheet1.createRow(rownum + 1);
							ref_rowC++;


							System.out.println("**********Writing Ref data to excel***********");
							for (int x = 0; x < attributeRefTemp.size(); x++)
							{
								Cell dataCellref = row1.createCell(x);
								dataCellref.setCellValue((String)attributeRefTemp.get(x));
								if (x == attributeRefTemp.size() - 1) {
									for (int y = 0; y < refAttforEx.size(); y++)
									{
										dataCellref = row1.createCell(refCol);
										dataCellref.setCellValue((String)refAttforEx.get(y));
										refCol++;
									}
								}
							}
						}
						//Error to handled DFid errors in reference data retrieval
					}catch(Exception e)
					{
						System.out.println("Reference Issue");
					}
				}
				}
			}catch(Exception e)
			{
				System.out.println("DfID error");
			}
		}
	}

	private static String getcommadelimitedName(String aux)
	{
		String name = "";
		String[] a = aux.split(" ");
		int i = a.length - 1;
		if (a.length > 1) {
			while (i >= 0)
			{
				if (i == a.length - 1) {
					name = a[i] + " ";
				} else {
					name = name + " " + a[i];
				}
				i--;
			}
		}
		return name;
	}

	private static String getType(String objtype, String type)
	{
		String result = null;
		HashMap<String, String> responseType = new HashMap();
		HashMap<String, String> refType = new HashMap();

		responseType.put("0", "Unique document");
		responseType.put("1", "Standard Document");
		responseType.put("2", "Consort Document");
		responseType.put("3", "State Medicaid");
		responseType.put("4", "Guidance");

		refType.put("0", "Legacy");
		refType.put("2", "Package Insert");
		refType.put("3", "Publication");
		refType.put("4", "Poster");
		refType.put("5", "Abstract");
		refType.put("6", "Book");
		refType.put("7", "Website");
		refType.put("8", "Press Release");
		refType.put("9", "Data on File");
		refType.put("10", "Oral Presentation");
		refType.put("12", "Clinical Trail");
		refType.put("13", "Other");
		if (objtype.equals("Response")) {
			result = (String)responseType.get(type);
		}
		if (objtype.equals("Reference")) {
			result = (String)refType.get(type);
		}
		return result;
	}

	private static String cleanDate(String attribute)
	{
		String finalDate = "";
		String padDate;
		if (attribute.equals("nulldate"))
		{
			padDate = " ";
		}
		else
		{
			String[] aux = attribute.split(" ");
			String date = aux[0];

			padDate = date;
			String[] aux1 = padDate.split("/");
			if ((aux1[0].length() < 2) && (aux1[1].length() > 1)) {
				padDate = "0" + padDate;
			} else if ((aux1[1].length() < 2) && (aux1[0].length() > 1)) {
				padDate = aux1[0] + "/" + "0" + aux1[1] + "/" + aux1[2];
			} else if ((aux1[0].length() < 2) && (aux1[1].length() < 2)) {
				padDate = "0" + aux1[0] + "/" + "0" + aux1[1] + "/" + aux1[2];
			} else {
				padDate = padDate;
			}
		}
		return padDate;
	}

	private static HashMap getFullName(IDfSession dfSession2)
			throws DfException
	{
		HashMap usernames = new HashMap();
		String resQuery = "select user_name,description from dm_user";
		IDfQuery query = new DfQuery();
		System.out.println("Query");
		query.setDQL(resQuery);
		IDfCollection coll = query.execute(dfSession2, 1);
		while (coll.next())
		{
			String userid = coll.getString("user_name");
			String userName = coll.getString("description");
			usernames.put(userid, userName);
		}
		coll.close();
		System.out.println(usernames);
		return usernames;
	}
}
