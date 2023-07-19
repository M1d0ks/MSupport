package QA_Refresh;
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


public class ProducTA_export {
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
		//HashMap tArea=getTA(dfSession);

		printExcel(pro,0);
		//printExcel(tArea,1);



	}

	private static void printExcel(HashMap pro, int i) throws IOException 
	{

		Workbook workbook = new SXSSFWorkbook(1000);
		String sDirectoryPath ="C:/Geodart/";
		FileOutputStream fileOut = new FileOutputStream(sDirectoryPath + "pro_info.xlsx");

		SXSSFSheet sheet1 = (SXSSFSheet)workbook.createSheet("Product");

		for(int j=0;j<pro.size();j++)
		{
			ArrayList info=(ArrayList) pro.get(j);

			Row headerRow = sheet1.createRow(j);
			for (int p = 0; p < info.size(); p++)
			{
				Cell headerCell = headerRow.createCell(p);
				headerCell.setCellValue((String)info.get(p));
			}
		}

		workbook.write(fileOut);
		fileOut.close();
	}


	private static HashMap getTA(IDfSession dfSession) throws DfException {
		HashMap proInfo=new HashMap<Integer,ArrayList>();
		String resQuery = "select ta_name,ta_id from mi_config_ta";
		IDfCollection resColl = DFCHelper.executeQuery(dfSession, resQuery);
		int x=0;
		while(resColl.next())
		{
			ArrayList oncs=new ArrayList();
			

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
		String resQuery = "select r_object_id,business_unit,config_product_code,config_product_name,config_ta_name from mi_config_product";
		IDfCollection resColl = DFCHelper.executeQuery(dfSession, resQuery);
		int x=0;
		while(resColl.next())
		{
			ArrayList oncs=new ArrayList();
			IDfSysObject document = (IDfSysObject)dfSession.getObject(new DfId(resColl.getString("r_object_id")));
			String product_name=document.getString("config_product_name");
			oncs.add(product_name);

			String product_code=document.getString("config_product_code");
			oncs.add(product_code);
			
			String product_ta=document.getString("config_ta_name");
			oncs.add(product_ta);

			String product_owner=document.getString("config_product_owner");
			oncs.add(product_owner);
			System.out.println("Product "+product_name+" Product owner " +product_owner);

			String business_unit=document.getString("business_unit");
			oncs.add(business_unit);
			
			String product_id=document.getString("config_product_id");
			oncs.add(product_id);
			
			String ta_id=document.getString("config_ta_id");
			oncs.add(ta_id);
			
			proInfo.put(x, oncs);
			x++;
		}
		return proInfo;
	}
}
