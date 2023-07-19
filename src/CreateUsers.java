
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import Test_Extract.DFCHelper;

import com.documentum.com.DfClientX;
import com.documentum.com.IDfClientX;
import com.documentum.fc.client.IDfClient;
import com.documentum.fc.client.IDfCollection;
import com.documentum.fc.client.IDfDocument;
import com.documentum.fc.client.IDfGroup;
import com.documentum.fc.client.IDfSession;
import com.documentum.fc.client.IDfSessionManager;
import com.documentum.fc.client.IDfSysObject;
import com.documentum.fc.client.IDfUser;
import com.documentum.fc.common.DfException;
import com.documentum.fc.common.DfId;
import com.documentum.fc.common.IDfId;
import com.documentum.fc.common.IDfLoginInfo;
import com.documentum.operations.IDfExportNode;
import com.documentum.operations.IDfExportOperation;


public class CreateUsers {
	
	public static void main(String args[]) throws DfException, IOException
	{
	 
	    IDfSession session=null;
		DFCHelper dfcHelper = new DFCHelper();
		String docbaseLoginId = "dmadmin-p";
		String docbasePassword = "merlin";
		String docbaseName = "midocsp";
		System.out.println("ReferenceUtil: main: enter");
		session = dfcHelper.createSession(docbaseLoginId, docbasePassword, docbaseName); 

		String sDirectoryPathOut ="C:/Geodart/";
		FileInputStream fileOut = new FileInputStream(sDirectoryPathOut + "MSL_login.xlsx");
		 XSSFWorkbook workbook = new XSSFWorkbook(fileOut);
		 XSSFSheet sheet = workbook.getSheetAt(0);
		 
		 Iterator<Row> rowIterator = sheet.iterator();
		 int i = 0;
	     while (rowIterator.hasNext())
	     //for(int i=0;i<1;i++)   
		 {
	    	 System.out.println(" in Loop ");
	    	 if(i==0){
	    		 i++;
	    		 continue;
	    	 }
	    	 
	        	Row row = rowIterator.next();
	        	
	        	 Cell l = row.getCell(3);
	        	 String uniqId =l.toString();
	        	 Cell d= row.getCell(2);
	        	 String email = d.toString();
	        	 
	        	 //Iterator<Cell> cellIterator = row.cellIterator();
	        	 //Cell cell = cellIterator.next();
	             //while (cellIterator.hasNext()) 
	            // {
	        	uniqId = uniqId.toLowerCase();
	            System.out.println("Login  "+uniqId+" Description "+email);
	            IDfUser guser = (IDfUser)session.getObjectByQualification("dm_user where user_name='"+uniqId+"'");
	            System.out.println("Creating guser -"+guser+" for user --> "+uniqId);
	            if(guser == null){
	            	System.out.println("Creating guser -"+guser+" for user --> "+uniqId);
	            	IDfUser user = (IDfUser)session.newObject("dm_user");
	            	
	        		user.setUserName(uniqId);
	        		user.setUserLoginName(uniqId);
	        		user.setUserAddress(email);
	        		user.setString("user_source","inline password");
	        		user.setString("user_login_domain","MIDOCS_LDAP");
	        		String password = "welcome"+uniqId;
	        		user.setString("user_password",password);
	        		user.save();
	        		
	        		IDfGroup group= (IDfGroup)session.getGroup ("mi_confirmed_users");
		   			System.out.println(" Adding user Name "+uniqId);
		   			group.addUser(uniqId);
		   			System.out.println(" Added");
		   			group.save();
		   			System.out.println("Saved");
		   			
		   			IDfGroup group2= (IDfGroup)session.getGroup ("mi_guest");
		   			System.out.println(" Adding user Name "+uniqId);
		   			group2.addUser(uniqId);
		   			System.out.println(" Added");
		   			group2.save();
		   			System.out.println("Saved");
	            }else{
	            	
	            	System.out.println("Modifying guser -"+guser+" for user --> "+uniqId);
	            	IDfUser user = (IDfUser)session.getObject(guser.getObjectId());
	        		user.setString("user_login_domain","MIDOCS_LDAP");
	        		user.setString("user_source","inline password");
	        		String password = "welcome"+uniqId;
	        		user.setString("user_password",password);
	        		user.setString("user_address", email);
	        		user.save();
	            }
	             //}
	        }
	}
}
