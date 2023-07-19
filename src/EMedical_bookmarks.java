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


public class EMedical_bookmarks {
	public static void main(String args[]) throws DfException, IOException
	{		
		DFCHelper dfcHelper = new DFCHelper();
		String docbaseLoginId = "dmadmin-p";
		String docbasePassword = "merlin";
		String docbaseName = "midocsp";
		System.out.println("ReferenceUtil: main: enter");
		IDfSession dfSession = dfcHelper.createSession(docbaseLoginId, docbasePassword, docbaseName);

		String sDirectoryPathIn ="C:/Geodart/MI_Website.xlsx";
		FileInputStream fileIn = new FileInputStream(new File(sDirectoryPathIn));
		Workbook workbook = new XSSFWorkbook(fileIn); 

		Sheet datatypeSheet = workbook.getSheetAt(0);
		Iterator<Row> iterator = datatypeSheet.iterator();

		String sDirectoryPathOut ="C:/Geodart/";
		FileOutputStream fileOut = new FileOutputStream(sDirectoryPathOut + "MI_Website_status.xlsx");

	//Workbook workbook1 = new SXSSFWorkbook(1000);
		Sheet sheet = workbook.getSheetAt(0);

		int i=0;
			while (iterator.hasNext()) {
				//for(int j=0;j<10;j++){
			try{
				
				Row currentRow = iterator.next();
				Iterator<Cell> cellIterator = currentRow.iterator();
			
				Cell objN=currentRow.getCell(5);
				String objName=objN.getStringCellValue();

				//Cell proId=currentRow.getCell(17);
				//double product_id=proId.getNumericCellValue();

				System.out.println("****** Object_name *********"+objN);
				//Cell catId=currentRow.getCell(16);
				//double category_id=catId.getNumericCellValue();

				//System.out.println("Got category id "+category_id);	
				//Cell category=currentRow.getCell(3);
				//String categRef=category.getStringCellValue();
				//System.out.println("Sent for refining "+categRef);
				//String catName=refineCatName(categRef);

			//   if(!objName.equals("#N/A"))
			  // {
				//System.out.println("Obj Name is not #N//A "+currentRow.getCell(4));
				//ArrayList data=getObjId(currentRow.getCell(1),dfSession,currentRow,sheet);	
				String data=getstatusId(objName,dfSession,currentRow,sheet);	
				
			
			/*	String objId=(String) data.get(0);
				String chronicleId=(String) data.get(1);
				String status=(String) data.get(2);
				String access=(String) data.get(3);
				String active=(String) data.get(4);
				String app_date=(String) data.get(5);
				String expiration=(String) data.get(6); */
				
				Row row = sheet.getRow(i);
				
			/*	Cell cell=row.createCell(5);
				cell.setCellValue(chronicleId);

				Cell cell1=row.createCell(6);
				cell1.setCellValue(objId);

				Cell cell2=row.createCell(7);
				cell2.setCellValue(status);
				
				Cell cell3=row.createCell(8);
				cell3.setCellValue(access);
				
				Cell cell4=row.createCell(7);
				cell4.setCellValue(active);*/
				

				Cell cell5=row.createCell(7);
				cell5.setCellValue(data);
				
				//System.out.println("iposit to excel "+iposit+" Row number "+row.getRowNum());
				
			 //  }
			   i++;
			}catch(Exception e)
			{
				System.out.println(e); 
			}
		}
		workbook.write(fileOut);
		fileOut.close();

	}

	private static String getstatusId(String s, IDfSession dfSession, Row currentRow, Sheet sheet) throws DfException {
		System.out.println("Get Status");
		String status = null;
		String resQuery="Select a_status from mi_response where object_name='"+s+"' ";
		
		IDfCollection resColl = DFCHelper.executeQuery(dfSession, resQuery);
		while(resColl.next())
		{
			status=resColl.getString("a_status");
			System.out.println("Status retrieved "+status);
		}
		resColl.close();
		return status;
	}
	private static String refineCatName(String categRef) {
		String[] r = categRef.split("(?<=.)(?=\\p{Lu})");

		if(r[0].equals("Dosageand"))
		{
			r[0]="Dosage and Administration";
		}else if(r[0].equals("Pharmacokineticsdynamics"))
		{
			r[0]="Pharmacokinetics/dynamics";
		}
		System.out.println("Category name "+r[0]);
		return r[0];
	}

	private static ArrayList getObjId(Cell cell, IDfSession dfSession, Row currentRow, Sheet sheet) throws DfException, IOException {

		String resQuery="Select r_object_id,i_chronicle_id,a_status,external_access,active,approved_date,expiration_date from mi_response where object_name='"+cell+"'";
		//String resQuery="Select r_object_id,i_chronicle_id,i_position from mi_response where r_object_id='0902b72080196404'";
		IDfCollection resColl = DFCHelper.executeQuery(dfSession, resQuery);
		//String object_name=refineObjectName(objName);
		ArrayList data=new ArrayList();
		while(resColl.next())
		{
			String objId=resColl.getString("r_object_id");
			IDfSysObject doc = (IDfSysObject)dfSession.getObject(new DfId(objId));

			String i_chronicle_id=resColl.getString("i_chronicle_id");
			String a_status=resColl.getString("a_status");
			String ex_access=resColl.getString("external_access");
			String active=resColl.getString("active");
			String approved_date=resColl.getString("approved_date");
			
			data.add(objId);
			data.add(i_chronicle_id);
			data.add(a_status);
			data.add(ex_access);
			data.add(active);
			data.add(approved_date);
			data.add(resColl.getString("expiration_date"));
		}
		resColl.close();
		return data;
	}
			//String iposit=generateUni(objId,product_id,category_id);

			//String cat=doc.getAllRepeatingStrings("category_name", ",");
			//String[] catName=cat.split(",");

			//int rowC=currentRow.getRowNum();
			//for(int i=0;i<catName.length;i++){
			
			//ArrayList proId=getObjName(dfSession,objId);
			//System.out.println("pro ID size "+proId.size());
			//int i=sheet.getLastRowNum();
			//for(int x=0;x<proId.size();x++)
			//{
				//try{ 
					//ArrayList ret=new ArrayList<String>();
					//Row row = sheet.createRow(i);
					//i++;
					//System.out.println("iposit array "+proId);
					//String iposit=(String) proId.get(x);
					//String product_id=(String) proId.get(0);
					//System.out.println("proId "+proId);
					//String object_name=refineObjectName(objName);

					//	if(product_id.equals("0"))
					//{
					//iposit=getiPosition(dfSession,objId,catName,product_id);
					//	iposit=getiPosition(dfSession,objId,catName,object_name);
					//}else{
					//iposit=getiPositionwithPro(dfSession,product_id,objId,catName);
					//}

					//System.out.println("Adding obj_id "+objId+" i_chronicle_id "+i_chronicle_id+" i Position "+iposit+"");
				//	String uniq=objId+i_chronicle_id+iposit;
					//ret.add(objId);
					//ret.add(i_chronicle_id);
					//ret.add(iposit);
					//ret.add(uniq);
					//System.out.println("iposit before writing "+iposit);
					//writeExcel(ret,row);
				//}
				//catch(Exception e)
				//{
				//	System.out.println("Over flow");
				//}
			//}		
		//}
	//}

	//}

	private static String generateUni(String objId, double product_id, double category_id) {
		int y=(int) (product_id+category_id);
		String result=objId+"X"+y;
		return result;
	}

	private static ArrayList gettopName(IDfSession dfSession, String objId, String catName, Cell taName) throws DfException {
		String resQuery="Select i_position,ta_id from mi_response_r where r_object_id='"+objId+"' and category_name='"+catName+"' ";
		System.out.println("Product Id query "+resQuery);
		ArrayList protopId=new ArrayList();
		IDfCollection resColl = DFCHelper.executeQuery(dfSession, resQuery);
		String proId="null";
		String topicId="null";
		while(resColl.next())
		{
			topicId=resColl.getString("ta_id");
			protopId.add(topicId);
		}
		resColl.close();
		return protopId;
	}

	private static String getiPositionwithPro(IDfSession dfSession, String proId, String objId, String catName) throws DfException 
	{
		String resQuery="Select i_position,product_id from mi_response_r where r_object_id='"+objId+"' and category_name like '%"+catName+"%' and product_id='"+proId+"'";
		System.out.println("iPosition query with Pro "+resQuery);
		IDfCollection resColl = DFCHelper.executeQuery(dfSession, resQuery);
		String iposit ="";
		while(resColl.next())
		{
			iposit=resColl.getString("i_position");
		}
		resColl.close();
		return iposit;
	}

	private static ArrayList getObjName(IDfSession dfSession, String objId) throws DfException {
		ArrayList protopId=new ArrayList();
		try{
			String resQuery="Select i_position,product_id,topic_id from mi_response_r where r_object_id='"+objId+"' ";
			System.out.println("Product Id query "+resQuery);

			IDfCollection resColl = DFCHelper.executeQuery(dfSession, resQuery);
			String proId="null";
			//	String topicId="null";
			while(resColl.next())
			{
				proId=resColl.getString("i_position");
				protopId.add(proId);
			}
			resColl.close();

		}catch(Exception e)
		{
			System.out.println("Exceptio "+e);
		}
		return protopId;
	}

	private static void writeExcel(ArrayList ret, Row row) throws IOException {
		
		String objId=(String) ret.get(0);
		String chronId=(String) ret.get(1);
		String iposit= (String) ret.get(2);
		String uniq=(String) ret.get(3);
		
		Cell cell=row.createCell(0);
		cell.setCellValue(chronId);

		Cell cell1=row.createCell(1);
		cell1.setCellValue(objId);

		System.out.println("iposit to excel "+iposit+" Row number "+row.getRowNum());

		Cell cell2=row.createCell(2);
		cell2.setCellValue(iposit);
		
		Cell cell3=row.createCell(3);
		cell3.setCellValue(uniq);
		System.out.println("iposit after writing "+iposit);
	}

	private static String getiPosition(IDfSession dfSession, String objId, String catName, String product_name) throws DfException {
		System.out.println("Get iPosition w-o prod");
		String resQuery="Select i_position,product_id from mi_response_r where r_object_id='"+objId+"' and (category_name like '%"+catName+"%' or product_name='"+product_name+"') ";
		System.out.println("iPosition query "+resQuery);
		IDfCollection resColl = DFCHelper.executeQuery(dfSession, resQuery);
		String iposit ="";
		while(resColl.next())
		{
			iposit=resColl.getString("i_position");
		}
		resColl.close();
		return iposit;
	}

	private static String refineObjectName(String objName) 
	{
		String[] splstring=objName.split(" ");
		String Name ="";
		try{
			if(splstring.length>0)
			{
				for(int i=0;i<splstring.length;i++)
				{
					char[] t=splstring[i].toCharArray();
					char firstCap=t[0];

					String restName="";
					for (int y=1;y<t.length;y++)
					{
						restName= restName +t[y];
					}
					if(i==0)
					{
						Name=firstCap+restName.toLowerCase();
					}else{
						Name=Name+" "+firstCap+restName.toLowerCase();
					}
				}
			}else{
				char[] x=objName.toCharArray();
				char firstCap=x[0];
				String restName="";
				for (int i=1;i<x.length;i++)
				{
					restName= restName +x[i];
				}
				Name= firstCap+restName.toLowerCase();
			}
			if(splstring.length>=1)
			{
				if(splstring[1].equals("LAR"))
				{
					Name="Sandostatin LAR Depot";
				}
			}

		}catch(Exception e)
		{
			System.out.println("Exception while refining name"+e);
		}
		return Name;
	}
}