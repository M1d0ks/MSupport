package MIDOCS_Upgrade;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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

import com.documentum.fc.client.IDfCollection;
import com.documentum.fc.client.IDfDocument;
import com.documentum.fc.client.IDfGroup;
import com.documentum.fc.client.IDfSession;
import com.documentum.fc.client.IDfSysObject;
import com.documentum.fc.client.IDfUser;
import com.documentum.fc.client.IDfWorkflow;
import com.documentum.fc.common.DfException;
import com.documentum.fc.common.DfId;
import com.documentum.fc.common.IDfId;
import com.emc.d2.api.D2Session;
import com.emc.d2.api.methods.D2CoreMethod;
import com.emc.d2.api.methods.D2Method;



public class MidocsEmedicalDataExport {
public static void main(String args[]) throws DfException, IOException, ParseException
{
	DFCHelper dfcHelper = new DFCHelper();
	String docbaseLoginId = "midocsp";
	String docbasePassword = "m1d0csp74";
	String docbaseName = "midocs_prod";
	System.out.println("ReferenceUtil: main: enter");
	IDfSession session = dfcHelper.createSession(docbaseLoginId, docbasePassword, docbaseName);
	Workbook workbookOut = new SXSSFWorkbook(1000);
	String sDirectoryPathIn ="C:/eMed/ResponseLetter/metadata.xlsx";
	
	String sDirectoryPathOut ="C:/eMed/ResponseLetter/";
	FileOutputStream fileOut = new FileOutputStream(sDirectoryPathOut + "FullExport.xlsx");
	
	SXSSFSheet sheet1 = (SXSSFSheet)workbookOut.createSheet("Document Information");
	FileInputStream fileIn = new FileInputStream(new File(sDirectoryPathIn));

	
	Workbook workbookIn = new XSSFWorkbook(fileIn);
	Sheet datatypeSheet = workbookIn.getSheetAt(0);
	System.out.println("****Starting to Read*******"+datatypeSheet);
	Iterator<Row> iterator = datatypeSheet.iterator();
	
	System.out.println("****Starting to Read*******"+iterator);
	int i=0;
	while (iterator.hasNext()) {
	try{
		//Reading Data
		Row currentRow = iterator.next();
		Iterator<Cell> cellIterator = currentRow.iterator();
	 
		//Cell objN=currentRow.getCell(0);
		//String sfdcId=objN.getStringCellValue();
		
		Cell keyword=currentRow.getCell(14);
		String ObjId=keyword.getStringCellValue();
		
		//Cell startDate=currentRow.getCell(3);
		//Date sdate=startDate.getDateCellValue();
		
		System.out.println("****** Object_name ********* ***Keyword*** "+ObjId+" Count "+i);
		
		IDfDocument dfSysObject = (IDfDocument)session.getObjectByQualification("mi_response(all) where prev_obj_id = '" + ObjId + "'");
		if(dfSysObject!=null){
		//Writing data
		Row row = sheet1.createRow(i);

		//Cell dataCell = row.createCell(0);
		//dataCell.setCellValue(sfdcId);

		Cell dataCell1 = row.createCell(0);
		dataCell1.setCellValue(dfSysObject.getString("r_object_id"));
		
		Cell dataCell2 = row.createCell(1);
		dataCell2.setCellValue(dfSysObject.getString("i_chronicle_id"));
		
//		
//		System.out.println("This is a date field "+ sdate);
//		SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
//		//String dateValue=sd.format(sdate);
//		System.out.println("fieldName " + sdate + " -- Date -- "+dateValue);
//		Cell dataCell3 = row.createCell(3);
//		dataCell3.setCellValue(dateValue);
		i++;
		
		}else{
			Row row = sheet1.createRow(i);

			//Cell dataCell = row.createCell(0);
			//dataCell.setCellValue(sfdcId);

			Cell dataCell1 = row.createCell(0);
			dataCell1.setCellValue("");
			
			Cell dataCell2 = row.createCell(1);
			dataCell2.setCellValue("");
		}
	}catch(Exception ex){
		System.out.println(ex);
	}
} 
	
	workbookOut.write(fileOut);
	fileOut.close();
}

}
//select r_object_id,users_names,group_name from dm_group where group_name in ('mi_onc_authors','mi_gen_authors','mi_gen_approvers','mi_onc_approvers')