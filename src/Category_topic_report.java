
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javax.mail.Session;

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


public class Category_topic_report {
public static void main(String args[]) throws DfException, IOException
{
	Workbook workbook = new SXSSFWorkbook(1000);
	
	
	DFCHelper dfcHelper = new DFCHelper();
	String docbaseLoginId = "dmadmin-p";
	String docbasePassword = "merlin";
	String docbaseName = "midocsp";
	System.out.println("ReferenceUtil: main: enter");
	IDfSession dfSession = dfcHelper.createSession(docbaseLoginId, docbasePassword, docbaseName);
	
	String sDirectoryPathOut ="C:/Geodart/";
	FileOutputStream fileOut = new FileOutputStream(sDirectoryPathOut + "Category_topics.xlsx");
	
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
	
	String resQuery="Select r_object_id,object_name from mi_config_topic";
	
	
	IDfCollection resColl = DFCHelper.executeQuery(dfSession, resQuery);
	System.out.println("Query executed");
	//ArrayList responseInfo = dfcHelper.getArrayListFromDMColl(resColl);
	//DfcHelper.closeCollection(resColl);
	int row_count=0;
	while(resColl.next())
	{
		try{
		String object_id = resColl.getString("r_object_id");
		String object_name=resColl.getString("object_name");
		
		String[] catop=object_name.split("-");
		String category="";
		String topic="";
		if(catop.length==2)
		{
		category=catop[0];
		topic=catop[1];
		}else{
			category=catop[0];
			for(int i1=1;i1<catop.length;i1++)
			{
				topic=topic+" "+catop[i1];
			}
		}
		int active=getCategoryActive(category,dfSession);
		
		System.out.println("Writing data of doc "+object_name);
		Row row = sheet1.createRow(row_count);
		row_count++;
	
			Cell dataCell = row.createCell(0);
			dataCell.setCellValue(category);
			
			Cell dataCell1 = row.createCell(1);
			dataCell1.setCellValue(topic);		
			
			Cell dataCell2 = row.createCell(2);
			dataCell2.setCellValue(active);	
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
private static int getCategoryActive(String category, IDfSession dfSession) throws DfException {
	System.out.println("Getting active");
	String resQuery = "select active from mi_config_category where object_name='"+category+"'";
	System.out.println("Query : "+resQuery);
	IDfCollection resColl = DFCHelper.executeQuery(dfSession, resQuery);
	int active=0;
	while(resColl.next())
	{
	active=resColl.getInt("active");
	}
	return active;
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
