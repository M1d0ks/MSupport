import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import Test_Extract.DFCHelper;

import com.documentum.fc.client.DfQuery;
import com.documentum.fc.client.IDfCollection;
import com.documentum.fc.client.IDfQuery;
import com.documentum.fc.client.IDfSession;
import com.documentum.fc.client.IDfSysObject;
import com.documentum.fc.common.DfException;
import com.documentum.fc.common.DfId;


public class User_Activity {
public static void main(String args[]) throws DfException, IOException
{
	
	DFCHelper dfcHelper = new DFCHelper();
	String docbaseLoginId = "dmadmin-p";
	String docbasePassword = "merlin";
	String docbaseName = "midocsp";
	System.out.println("ReferenceUtil: main: enter");
	IDfSession dfSession = dfcHelper.createSession(docbaseLoginId, docbasePassword, docbaseName);
	
	
	
	//getting product info
	HashMap pro=getProducts(dfSession);
	
	//Getting TA info
	HashMap tArea=getTA(dfSession);
	
	printExcel(pro,1);
	printExcel(tArea,2);
	
}

private static void printExcel(HashMap pro, int i) throws FileNotFoundException {
	
	Workbook workbook = new SXSSFWorkbook(1000);
	String sDirectoryPath ="C:/Geodart/";
	FileInputStream filein = new FileInputStream(sDirectoryPath + "Prod_info.xlsx");

	SXSSFSheet sheet1 = (SXSSFSheet)workbook.getSheetAt(i);
	
	for(int j=0;j<pro.size();j++)
	{
		ArrayList info=(ArrayList) pro.get(j);
		
		for(int k=0;k<info.size();k++)
		{
			
			
		}
	}
}

private static HashMap getTA(IDfSession dfSession) throws DfException {
	HashMap proInfo=new HashMap<Integer,ArrayList>();
	String resQuery = "select ta_name,ta_id from mi_config_ta";
	IDfCollection resColl = DFCHelper.executeQuery(dfSession, resQuery);
	
	while(resColl.next())
	{
		ArrayList oncs=new ArrayList();
		int x=0;
		
		String ta_Id=resColl.getString("ta_id");
		oncs.add(ta_Id);
		
		String ta_name=resColl.getString("ta_name");
		oncs.add(ta_name);		
		
		proInfo.put(x, oncs);
		x++;
	}
	return proInfo;
}

private static HashMap getProducts(IDfSession dfSession) throws DfException {
	HashMap proInfo=new HashMap<Integer,ArrayList>();
	String resQuery = "select business_unit,config_product_code,config_product_name,config_ta_name,config_product_owner from mi_config_product where business_unit='Oncology'";
	IDfCollection resColl = DFCHelper.executeQuery(dfSession, resQuery);
	
	while(resColl.next())
	{
		ArrayList oncs=new ArrayList();
		int x=0;
		String product_name=resColl.getString("config_product_name");
		oncs.add(product_name);
		
		String product_code=resColl.getString("config_product_code");
		oncs.add(product_code);
		
		String product_ta=resColl.getString("config_ta_name");
		oncs.add(product_ta);
		
		String product_owner=resColl.getString("config_product_owner");
		oncs.add(product_owner);
		
		String business_unit=resColl.getString("business_unit");
		oncs.add(business_unit);		
		
		proInfo.put(x, oncs);
		x++;
	}
	return proInfo;
}
}
