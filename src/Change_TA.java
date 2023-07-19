import java.util.HashMap;

import Test_Extract.DFCHelper;

import com.documentum.fc.client.IDfCollection;
import com.documentum.fc.client.IDfSession;
import com.documentum.fc.client.IDfSysObject;
import com.documentum.fc.client.IDfWorkflow;
import com.documentum.fc.common.DfException;
import com.documentum.fc.common.DfId;
import com.documentum.fc.common.IDfId;
import com.emc.d2.api.D2Session;
import com.emc.d2.api.methods.D2CoreMethod;
import com.emc.d2.api.methods.D2Method;



public class Change_TA {
public static void main(String args[]) throws DfException
{
	DFCHelper dfcHelper = new DFCHelper();
	String docbaseLoginId = "dmadmin-p";
	String docbasePassword = "merlin";
	String docbaseName = "midocsp";
	System.out.println("ReferenceUtil: main: enter");
	IDfSession session = dfcHelper.createSession(docbaseLoginId, docbasePassword, docbaseName);
	
	String proName="Kesimpta";
	String taName="NeuroScience";
	System.out.println("Adding products");
	addTA(session,proName, taName);
	//deleteProduct(session,oldName);
	
}

private static void addTA(IDfSession session, String proName, String taName) throws DfException {
	String query="Select r_object_id from mi_response where any product_name='"+proName+"' and object_name like '%OMB%' ";
	//String query="Select r_object_id from mi_response where any product_name='"+oldName+"'";
	
	System.out.println("Query "+query);

	IDfCollection col = DFCHelper.executeQuery(session, query);
	int count=0;
	while(col.next())
	{
		System.out.println("Number of document "+count);
		String object_id=col.getString("r_object_id");
		count++;
	
		IDfSysObject document = (IDfSysObject)session.getObject(new DfId(object_id));
    
		if(!document.isCheckedOut() && !document.isImmutable())
		{
		String objName=document.getString("object_name");
		System.out.println("Adding TA to the document "+objName);
		
		document.setString("ta_name", taName);
		document.save();
		}
	}
}

}