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
import com.emc.d2.api.D2Session;
import com.emc.d2.api.methods.D2CoreMethod;
import com.emc.d2.api.methods.D2Method;


public class DelAll_pros {
	public static void main(String args[]) throws DfException, IOException
	{

		DFCHelper dfcHelper = new DFCHelper();
		String docbaseLoginId = "dmadmin-np";
		String docbasePassword = "merlin";
		String docbaseName = "midocsq";
		System.out.println("ReferenceUtil: main: enter");
		IDfSession dfSession = dfcHelper.createSession(docbaseLoginId, docbasePassword, docbaseName);
		
		delProducts(dfSession);
		
	}

	private static void delProducts(IDfSession dfSession) throws DfException {
		HashMap proInfo=new HashMap<Integer,ArrayList>();
		String resQuery = "select r_object_id from mi_config_product";
		IDfCollection resColl = DFCHelper.executeQuery(dfSession, resQuery);
		 
		while(resColl.next())
		{
		IDfSysObject document = (IDfSysObject)dfSession.getObject(new DfId(resColl.getString("r_object_id")));
		String objName=document.getString("config_product_name");
		System.out.println("Deleting document "+objName);
		//document.destroyAllVersions();
		
		String obj_id=document.getString("r_object_id");
		document.setBoolean("active",true);
		document.save();
		invokeD2Method(dfSession, obj_id);
		}
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