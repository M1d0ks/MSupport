import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.poi.common.usermodel.Hyperlink;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CreationHelper;
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


public class Reports_RefApproval {
public static void main(String args[]) throws DfException, IOException
{
	Workbook workbook = new XSSFWorkbook();
	CreationHelper createHelper = workbook.getCreationHelper();
	
	DFCHelper dfcHelper = new DFCHelper();
	String docbaseLoginId = "dmadmin-p";
	String docbasePassword = "merlin";
	String docbaseName = "midocsp";
	System.out.println("ReferenceUtil: main: enter");
	IDfSession dfSession = dfcHelper.createSession(docbaseLoginId, docbasePassword, docbaseName);
	
	//String sDirectoryPathIn ="C:/Geodart/ApprovedRefs_11-20_01-06.xlsx";
	//String sDirectoryPath ="C:/Geodart/ta_info.xlsx";
	//FileInputStream fileIn = new FileInputStream(new File(sDirectoryPathIn));
	//Workbook workbook1 = new XSSFWorkbook(fileIn);
	
	String sDirectoryPathOut ="C:/Geodart/";
	FileOutputStream fileOut = new FileOutputStream(sDirectoryPathOut + "ReconReport2.xlsx");
	
	Sheet datatypeSheet = workbook.createSheet();
    Iterator<Row> iterator = datatypeSheet.iterator();
	int i=0;
    while (iterator.hasNext()) {
    	Row currentRow = iterator.next();
        Iterator<Cell> cellIterator = currentRow.iterator();
        	//System.out.println(" Object_name "+currentRow.getCell(0));
        	i++;
    }
	
	XSSFSheet sheet1 = (XSSFSheet)workbook.createSheet("Document Information");
	//Change Dates
	//NEW 
	//String resQuery="select r_object_id,object_name,r_version_label,r_creation_date from mi_reference where r_creation_date<DATE('10/01/2020','MM/DD/YYYY') and r_creation_date>DATE('07/30/2020','MM/DD/YYYY') and any ta_name like '%Oncology%'"; 
	String resQuery="select r_object_id,object_name,title,r_object_type,r_creation_date from dm_document where r_object_type in ('mi_response','mi_reference') and r_creation_date>DATE('12/01/2022','MM/DD/YYYY') and r_creation_date<DATE('01/10/2023','MM/DD/YYYY')";
			//+ "and r_creation_date>DATE('03/21/2023','MM/DD/YYYY')"; 
	
	IDfCollection resColl = DFCHelper.executeQuery(dfSession, resQuery);

	//ArrayList responseInfo = dfcHelper.getArrayListFromDMColl(resColl);
	//System.out.println("Query executed "+responseInfo.size());
	//DfcHelper.closeCollection(resColl);
	int row_count=0;
	//HashMap userNames = getFullName(dfSession);
	while(resColl.next())
	{
		try{
		//HashMap hmResponse = (HashMap)responseInfo.get(i);
		String object_id = resColl.getString("r_object_id");
		IDfSysObject document = (IDfSysObject)dfSession.getObject(new DfId(object_id));
		
		String object_name=document.getString("object_name");
		//String count=resColl.getString("time_stamp");
		//String r_modify_date=document.getString("r_modify_date");
		//String r_modify_date=document.getString("r_modify_date");
		
		String title=document.getString("title");
		System.out.println("Title --- "+title);
		String a_status=document.getString("a_status");
		String r_modifier=document.getString("r_object_type");
		String owner_name=document.getString("owner_name");
		//String ta_name=document.getString("ta_name");
		String approved_date=document.getString("approved_date");
		//String expired_date = document.getString("expiration_date");
		String category_Name="";
		try{
		 //category_Name=document.getString("category_name");
		}catch(Exception e)
		{
			System.out.println(e);
		}
		String product_name=document.getAllRepeatingStrings("product_name", ",");
		String docprods[]=product_name.split(",");
		String version=document.getString("r_version_label");
		//String congress_title=document.getString("congress_title");
		//String reference_citation=document.getString("reference_citation");
		//String rauthors=document.getAllRepeatingStrings("rauthors", ",");
		//String expiration_date=resColl.getString("expiration_date");
		
		//String rV=document.getVersionLabel(1);
		String created=document.getString("r_creation_date");
		
		//for(int i=0;i<docprods.length;i++)
		//{
	    //String pName=docprods[i];
		//String user_name=resColl.getString("user_name");
		//String userName=document.getString("r_creator_name");
	//	if(userNames.containsKey(userName))
		//{
			//userName=(String) userNames.get(userName);
		//}
		//String version = dfSession.apiGet("get", object_id + ",r_version_label");
		 //System.out.println("Getting Document "+object_name+ " Version "+version+ " Row count "+row_count+" Created by "+userName);
		
		 //if(onc.contains(docprods[i]))
		//{
		XSSFCellStyle hlinkstyle = (XSSFCellStyle) workbook.createCellStyle();
	      XSSFFont hlinkfont = (XSSFFont) workbook.createFont();
	      hlinkfont.setUnderline(XSSFFont.U_SINGLE);
	      hlinkfont.setColor(HSSFColor.BLUE.index);
	      hlinkstyle.setFont(hlinkfont);
	      
		System.out.println("Writing data of doc "+object_name);
		Row row = sheet1.createRow(row_count);
		row_count++;
	
			Cell dataCell = row.createCell(0);
			dataCell.setCellValue(object_name);          
			
			Cell dataCell1 = row.createCell(1);
			dataCell1.setCellValue(title);
			
			Cell dataCell2 = row.createCell(2);
			dataCell2.setCellValue(version); 
			
			Cell dataCell3 = row.createCell(3);
			dataCell3.setCellValue(a_status);
			
			Cell dataCell4 = row.createCell(4);
			dataCell4.setCellValue(created);
			
			Cell dataCell5 = row.createCell(5);
			dataCell5.setCellValue(r_modifier);
			
			Cell dataCell6 = row.createCell(6);
			dataCell6.setCellValue(r_modifier);
			
			Cell dataCell7 = row.createCell(7);
			dataCell7.setCellValue(approved_date);
			
			//Cell dataCell8 = row.createCell(11);
			//dataCell8.setCellValue(expired_date);
			
			Cell dataCell9 = row.createCell(8);
			dataCell9.setCellValue(object_id);
			
			Cell dataCell10 = row.createCell(9);
			dataCell10.setCellValue(product_name);
			
			Cell dataCell11 = row.createCell(10);
			dataCell11.setCellValue(category_Name);
			
			//Cell dataCell12 = row.createCell(11);
			//dataCell12.setCellValue(congress_title);
			
			//Cell dataCell13 = row.createCell(12);
			//dataCell13.setCellValue(reference_citation);
			
			//Cell dataCell14 = row.createCell(13);
			//dataCell14.setCellValue(rauthors);
			
			//Cell dataCell15 = row.createCell(14);
			//String type = "Reference";
			//String reference_type = getType(type, "Reference");
			//dataCell5.setCellValue(reference_type);
			
			Cell dataCell16 = row.createCell(5);
			dataCell16.setCellValue(owner_name);	
			
			
			/*Cell dataCell10 = row.createCell(10);
			dataCell10.setCellValue(object_id); 
			
			Cell dataCell5 = row.createCell(5);
			dataCell5.setCellValue("File Link");
			
			org.apache.poi.ss.usermodel.Hyperlink link = createHelper.createHyperlink(Hyperlink.LINK_FILE);
            link.setAddress(path);
            dataCell5.setHyperlink(link);
            dataCell5.setCellStyle(hlinkstyle); */
			
		//}
	//	}
	}catch(Exception e){
	System.out.println(e);
	}
		
}
	dfcHelper.closeCollection(resColl);
	workbook.write(fileOut);
	fileOut.close();
}
private static ArrayList getOncProduct(IDfSession dfSession) throws DfException {
	String resQuery = "select config_product_name from mi_config_product where business_unit='Oncology'";
	IDfCollection resColl = DFCHelper.executeQuery(dfSession, resQuery);
	ArrayList oncs=new ArrayList();
	while(resColl.next())
	{
		String product_name=resColl.getString("config_product_name");
		oncs.add(product_name);
	}
	return oncs;
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
}
