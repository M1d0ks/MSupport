
package QA_Refresh;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.Iterator;

import javax.mail.Session;

import Test_Extract.DFCHelper;

import com.documentum.fc.client.DfQuery;
import com.documentum.fc.client.IDfCollection;
import com.documentum.fc.client.IDfDocument;
import com.documentum.fc.client.IDfGroup;
import com.documentum.fc.client.IDfQuery;
import com.documentum.fc.client.IDfSession;
import com.documentum.fc.client.IDfSysObject;
import com.documentum.fc.client.IDfUser;
import com.documentum.fc.common.DfException;
import com.documentum.fc.common.DfId;
import com.emc.d2.api.D2Session;
import com.emc.d2.api.methods.D2CoreMethod;
import com.emc.d2.api.methods.D2Method;


public class CreateProducts {
	public static void main(String args[]) throws DfException, IOException
	{
		DFCHelper dfcHelper = new DFCHelper();
		String docbaseLoginId = "dmadmin-np";
		String docbasePassword = "merlin";
		String docbaseName = "midocsq";
		System.out.println("ReferenceUtil: main: enter");
		IDfSession dfSession = dfcHelper.createSession(docbaseLoginId, docbasePassword, docbaseName);
			
	
		String sDirectoryPath ="C:/Geodart/pro_info.xlsx";
		//String sDirectoryPath ="C:/Geodart/ta_info.xlsx";
		FileInputStream fileIn = new FileInputStream(new File(sDirectoryPath));
		Workbook workbook = new XSSFWorkbook(fileIn);
		
		Sheet datatypeSheet = workbook.getSheetAt(0);
        Iterator<Row> iterator = datatypeSheet.iterator();
		int i=0;
        while (iterator.hasNext()) {
      //  while(i<2){
        	Row currentRow = iterator.next();
            Iterator<Cell> cellIterator = currentRow.iterator();

            	System.out.println(" Product_name "+currentRow.getCell(0));
            	System.out.println(" Product_code "+currentRow.getCell(1));
            	System.out.println(" ta_name "+currentRow.getCell(2));
            	System.out.println(" Product_owner "+currentRow.getCell(3));
            	System.out.println(" business_unit  "+currentRow.getCell(4));
            	System.out.println(" product id  "+currentRow.getCell(5));
            	
        		IDfSysObject document = (IDfSysObject) dfSession.newObject("mi_config_product");
        		//document.setString("name",""+currentRow.getCell(0)+"");
        		document.setString("config_product_name",""+currentRow.getCell(0)+"");
        		document.setString("config_product_code",""+currentRow.getCell(1)+"");
        		document.setRepeatingString("config_ta_name",0,""+currentRow.getCell(2)+"");
        		document.setString("config_product_owner",""+currentRow.getCell(3)+"");
        		document.setString("business_unit",""+currentRow.getCell(4)+"");
        		document.setString("config_product_id",""+currentRow.getCell(5)+"");
        		document.setString("config_ta_id",""+currentRow.getCell(6)+"");
        		
            	
            	//IDfSysObject document = (IDfSysObject) dfSession.newObject("mi_config_ta");
            	
            	//document.setString("ta_id",""+currentRow.getCell(0)+"");
        		//document.setString("ta_name",""+currentRow.getCell(1)+"");
        		
        		//System.out.println("Creating document "+j);
        		document.save();
            	i++;
        }
	}
	
	private static String String(Cell cell) {
		// TODO Auto-generated method stub
		return null;
	}

	private static String invokeD2Method(IDfSession session, String objId) {
		String result = "";
		try {
			HashMap arguments = new HashMap();
			arguments.put(D2Method.ARG_ID, objId);
			arguments.put(D2CoreMethod.ARG_AUTOLINK, Boolean.TRUE);
			arguments.put(D2CoreMethod.ARG_SECURITY, Boolean.TRUE);
			D2Session.initTBO(session);
			D2Method.start(session, D2CoreMethod.class, arguments);

		}catch (Exception ex) {
			System.out.println(ex.getMessage());
			result = "ERROR - " + ex.getMessage();
		}
		return result;
	}

	}
