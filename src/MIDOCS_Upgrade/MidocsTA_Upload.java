package MIDOCS_Upgrade;
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
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.Iterator;

import javax.mail.Session;

import Test_Extract.DFCHelper;

import com.documentum.fc.client.DfQuery;
import com.documentum.fc.client.IDfCollection;
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


public class MidocsTA_Upload {
	public static void main(String args[]) throws DfException, IOException
	{

		String sDirectoryPath ="C:/Geodart/Midocs_TA.xlsx";
		FileInputStream filein = new FileInputStream(new File(sDirectoryPath));
		Workbook workbook = new XSSFWorkbook(filein);
		ArrayList<String> users=new ArrayList<String>();

		DFCHelper dfcHelper = new DFCHelper();
		String docbaseLoginId = "midocsp";
		String docbasePassword = "m1d0csp74";
		String docbaseName = "midocs_prod";
		System.out.println("ReferenceUtil: main: enter");
		IDfSession dfSession = dfcHelper.createSession(docbaseLoginId, docbasePassword, docbaseName);

		Sheet datatypeSheet = workbook.getSheetAt(0);
        Iterator<Row> iterator = datatypeSheet.iterator();
		int i=0;
        while (iterator.hasNext()) {
       //while(i<2){
		
		IDfSysObject document = (IDfSysObject) dfSession.newObject("mi_config_ta");
		//document.setString("name",""+currentRow.getCell(0)+"");
		Row currentRow = iterator.next();
        Iterator<Cell> cellIterator = currentRow.iterator();
        
        System.out.println(" object_name "+currentRow.getCell(0));
    	System.out.println(" title "+currentRow.getCell(1));
    	System.out.println(" acl_name "+currentRow.getCell(2));
    	System.out.println(" ta_name "+currentRow.getCell(3));
    	System.out.println(" ta_id  "+currentRow.getCell(4));
    	System.out.println(" r_object_type "+currentRow.getCell(5));
    	
        //document.setObjectName("US-REF-SM"+counter);
		document.setString("object_name",""+currentRow.getCell(0)+"");
		document.setString("title",""+currentRow.getCell(1)+"");
		document.setString("acl_name",""+currentRow.getCell(2)+"");
		document.setString("owner_name","midocsp");
		document.setString("ta_name",""+currentRow.getCell(3)+"");
		document.setString("ta_id",""+currentRow.getCell(4)+"");
		document.setString("active",""+currentRow.getCell(5)+"");
		document.setString("r_object_type",""+currentRow.getCell(6)+"");
		//document.setObjectName(arg0);
		document.save();
		String result = invokeD2Method(dfSession, document.getString("r_object_id"));
       }
		
	}

	private static String getdelimited(ArrayList<String> users) {
		String x="("+"'"+null+"'";
		for(int i=0;i<users.size();i++)
		{
			x=x+","+"'"+users.get(i)+"'";
		}
		x=x+")";
		return x;
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

		}
		catch (Exception ex) {
			System.out.println(ex.getMessage());
			result = "ERROR - " + ex.getMessage();
		}
		return result;
	}
}