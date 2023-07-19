import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import Test_Extract.DFCHelper;

import com.documentum.fc.client.DfQuery;
import com.documentum.fc.client.IDfCollection;
import com.documentum.fc.client.IDfQuery;
import com.documentum.fc.client.IDfSession;
import com.documentum.fc.client.IDfSysObject;
import com.documentum.fc.common.DfException;
import com.documentum.fc.common.DfId;
import com.documentum.fc.common.IDfAttr;


public class Migration_TestExtract {
public static void main(String args[]) throws DfException, IOException
{
	Workbook workbook = new SXSSFWorkbook(1000);
	
	
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
	FileOutputStream fileOut = new FileOutputStream(sDirectoryPathOut + "Sandoz_extract.xlsx");
	
	Sheet datatypeSheet = workbook.createSheet();
    Iterator<Row> iterator = datatypeSheet.iterator();
	int i=0;
    while (iterator.hasNext()) {
    	Row currentRow = iterator.next();
        Iterator<Cell> cellIterator = currentRow.iterator();

        	//System.out.println(" Object_name "+currentRow.getCell(0));
        	i++;
    }
	
	SXSSFSheet sheet1 = (SXSSFSheet)workbook.createSheet("Document Information");
	//All onc products 
	String resQuery="select * from mi_response where any ta_name='Sandoz'";
	
	
	IDfCollection resColl = DFCHelper.executeQuery(dfSession, resQuery);
	System.out.println("Query executed");
	//ArrayList responseInfo = dfcHelper.getArrayListFromDMColl(resColl);
	//DfcHelper.closeCollection(resColl);
	int row_count=0;
	//HashMap userNames = getFullName(dfSession);
	while(resColl.next())
	{
		try{
		//HashMap hmResponse = (HashMap)responseInfo.get(i);
		String object_id = resColl.getString("r_object_id");
		IDfSysObject document = (IDfSysObject)dfSession.getObject(new DfId(object_id));
		int atts = document.getAttrCount();
		
		
		Row row = sheet1.createRow(row_count);
		
		if(row_count==0){
		for(int j=0;j<atts;j++)
		{
			IDfAttr attribute = document.getAttr(j);
			System.out.println("Writing headers");
			Cell dataCell = row.createCell(j);
			dataCell.setCellValue(attribute.getName());
		}
		}
	/*	row_count++;
		for(int j=0;j<atts;j++)
		{
			IDfAttr attribute = document.getAttr(j);
			System.out.println("Document name "+document.getString("object_name")+" === "+attribute);
			
				Cell dataCell1 = row.createCell(j);
				dataCell1.setCellValue(document.getString(attribute.getName()));	
		}*/
		/*String object_name=document.getString("object_name");
		//String count=resColl.getString("time_stamp");
		//String r_modify_date=document.getString("r_modify_date");
		String r_modify_date=document.getString("approved_date");
		
		String title=document.getString("title");
		String a_status=document.getString("a_status");
		String r_modifier=document.getString("r_modifier");
		String owner_name=document.getString("owner_name");
		String ta_name=document.getString("object_name");
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
		//String expiration_date=resColl.getString("expiration_date");
		
		//String rV=document.getVersionLabel(1);
		//String created=Document.getString("r_creation_date");
		
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
		// System.out.println("Getting Document "+object_name+ " Version "+version+ " Row count "+row_count+" Created by "+userName);
		
		 //if(onc.contains(docprods[i]))
		//{
		
	/*	System.out.println("Writing data of doc "+object_name);
		Row row = sheet1.createRow(row_count);
		row_count++;
	
			Cell dataCell = row.createCell(0);
			dataCell.setCellValue(object_name);
			
			Cell dataCell1 = row.createCell(1);
			dataCell1.setCellValue(title);
			
			Cell dataCell2 = row.createCell(2);
			dataCell2.setCellValue(r_modify_date);
			
			Cell dataCell3 = row.createCell(3);
			dataCell3.setCellValue(a_status);
			
			Cell dataCell4 = row.createCell(4);
			dataCell4.setCellValue(r_modifier);
			
			Cell dataCell5 = row.createCell(5);
			dataCell5.setCellValue(owner_name);
			
			Cell dataCell6 = row.createCell(6);
			dataCell6.setCellValue(ta_name);
			
			Cell dataCell7 = row.createCell(7);
			dataCell7.setCellValue(product_name);
			
			Cell dataCell8 = row.createCell(8);
			dataCell8.setCellValue(category_Name);
			
			Cell dataCell9 = row.createCell(9);
			dataCell9.setCellValue(version); 
			
			//Cell dataCell10 = row.createCell(10);
			//dataCell10.setCellValue(expiration_date);
			*/
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

}
