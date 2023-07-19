import Test_Extract.DFCHelper;

import com.documentum.fc.client.IDfCollection;
import com.documentum.fc.client.IDfSession;
import com.documentum.fc.client.IDfSysObject;
import com.documentum.fc.common.DfException;
import com.documentum.fc.common.DfId;

public class Freeze_Documents {
	public static void main(String args[]) throws DfException
	{
	IDfSession session=null;
	DFCHelper dfcHelper = new DFCHelper();
	String docbaseLoginId = "dmadmin-p";
	String docbasePassword = "merlin";
	String docbaseName = "midocsp";
	System.out.println("ReferenceUtil: main: enter");
	session = dfcHelper.createSession(docbaseLoginId, docbasePassword, docbaseName);
	//String path="C:/Users/kucheja1/Downloads/US-SOM-S025.docx";
	String docs="US-GLI-S390";
	String query="select r_object_id,object_name from mi_response(all) where object_name in ('"+docs+"') and any r_version_label='6.0.2.4'";
	System.out.println("Query "+query);
	IDfCollection col = DFCHelper.executeQuery(session, query);
	
	while(col.next())
	{
		String object_id=col.getString("r_object_id");
		IDfSysObject document = (IDfSysObject)session.getObject(new DfId(object_id));	
		String objName= document.getString("object_name");
		 
		
		//boolean transactionActive = session.isTransactionActive();
		//session.beginTrans(); 
		
		System.out.println("Freezing the document "+objName);
		document.setInt("r_frozen_flag", 1);
		//document.setString("r_lock_owner", "dmadmin-p");
		document.setString("a_status", "Retired");
		//document.setACLName("acl_mi_response_retired");
		//document.checkout();
		//document.lock();
		//document.setFile(path);
		//document.setString("owner_name", "geevali1");
		document.save();
	}
	col.close();
	}
}
