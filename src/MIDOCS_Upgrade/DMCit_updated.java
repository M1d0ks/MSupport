package MIDOCS_Upgrade;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
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


public class DMCit_updated {
	public static void main(String args[]) throws DfException, IOException
	{

		String sDirectoryPath ="C:/Midocs_Migration/OVW/";
		File folder=new File(sDirectoryPath);
		ArrayList<String> users=new ArrayList<String>();

		DFCHelper dfcHelper = new DFCHelper();
		String docbaseLoginId = "midocsp";
		String docbasePassword = "m1d0csp74";
		String docbaseName = "midocs_prod";
		System.out.println("ReferenceUtil: main: enter");
		IDfSession dfSession = dfcHelper.createSession(docbaseLoginId, docbasePassword, docbaseName);
		
		File[] files=folder.listFiles();
		
		for(File file:files){
			if(file.isFile()){
				String[] objId = file.getName().split("_");
				if(objId.length>0){
				System.out.println("File name: "+objId[1]+" For object "+objId[0]);
				String[] rObjId= objId[1].split("\\.");
				System.out.println("File name: "+rObjId[0]+" For object "+rObjId[1]);
				IDfDocument dfSysObject = (IDfDocument)dfSession.getObjectByQualification("mi_response_overview where r_object_id='0902e9e9801aecba'");
				//if(dfSysObject.isCheckedOut()){
				//	dfSysObject.cancelCheckout();
				//}
				//System.out.println("Sysobject "+dfSysObject+" status "+dfSysObject.getString("a_status"));
				//if(dfSysObject!=null && dfSysObject.getString("a_status").equals("Retired") && !dfSysObject.isCheckedOut()){
				System.out.println("Object Name :"+dfSysObject.getString("object_name") +"File name: "+file.getName()+" For object "+rObjId[1]);
				dfSysObject.setFile(sDirectoryPath+"/"+file.getName());
				dfSysObject.save();
				//}
				}
			}
		}
	}
}
