
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



public class SetBRules {
public static void main(String args[]) throws DfException
{
	DFCHelper dfcHelper = new DFCHelper();
	String docbaseLoginId = "midocsp";
	String docbasePassword = "m1d0csp74";
	String docbaseName = "midocs_prod";
	System.out.println("ReferenceUtil: main: enter");
	IDfSession session = dfcHelper.createSession(docbaseLoginId, docbasePassword, docbaseName);
	 
	//String proName="'Aimovig'";
	changeTa(session);
}

private static void changeTa(IDfSession session) throws DfException {

	String query="select r_object_id from mi_reference where r_object_id='0902e9e9801c87c3'";
	System.out.println("Query "+query);

	IDfCollection col = DFCHelper.executeQuery(session, query);
	int count=0; 
	while(col.next())
	{
		String object_id=col.getString("r_object_id");
		IDfSysObject document = (IDfSysObject)session.getObject(new DfId(object_id));
		document.setString("r_object_type","mi_reference");
		if(!document.isCheckedOut())
		{
	    System.out.println("Applying rules for "+document.getString("object_name")+" "+count);
	    document.link("/MIDOCS/References/Legacy");
		document.save();
		String result = invokeD2Method(session, document.getString("r_object_id"));
		}
		count++;
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