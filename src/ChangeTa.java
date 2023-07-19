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



public class ChangeTa {
public static void main(String args[]) throws DfException
{
	DFCHelper dfcHelper = new DFCHelper();
	String docbaseLoginId = "dmadmin-p";
	String docbasePassword = "merlin";
	String docbaseName = "midocsp";
	System.out.println("ReferenceUtil: main: enter");
	IDfSession session = dfcHelper.createSession(docbaseLoginId, docbasePassword, docbaseName);
	
//	String proName="'Argatroban','Arranon','Hycamtin Capsule','Hycamtin Injection','Zofran Injection','Zofran Oral Formulations','Zometa'";
	String proName="'Mayzent'";
	String taName="Oncology Legacy";
	System.out.println("Adding products");
	changeTa(session,proName,taName);
	//deleteProduct(session,oldName);
	
}

private static void changeTa(IDfSession session, String newName, String taName) throws DfException {
	//String query="Select r_object_id from mi_response where any product_name='"+oldName+"'";
	String query="Select r_object_id from mi_response where any product_name in ("+newName+") and any ta_name!='Oncology Legacy' and response_type='1'";
	
	System.out.println("Query "+query);

	IDfCollection col = DFCHelper.executeQuery(session, query);
	int count=0;
	while(col.next())
	{
		System.out.println("Number of document "+count);
		String object_id=col.getString("r_object_id");
		count++;
	
		IDfSysObject document = (IDfSysObject)session.getObject(new DfId(object_id));
    
		if(!document.isCheckedOut())
		{
		String objName=document.getString("object_name");
		System.out.println("Changing TA to the document "+objName);
		
		String pros=document.getAllRepeatingStrings("product_name",",");
		String[] products=pros.split(",");
		
		if(products.length==1)
		{
			document.setString("ta_name",taName);
		}else{
		Boolean add=checkifAlreadyExists(session,document,taName);
		if(add==false){
			
		}else{
			document.appendString("ta_name","Oncology Legacy");
		}
		}
		document.save();
		
		String result = invokeD2Method(session, document.getString("r_object_id"));
		}
	}
}

private static Boolean checkifAlreadyExists(IDfSession session, IDfSysObject document, String taName) throws DfException {
	String tas=document.getAllRepeatingStrings("ta_name",",");
	String[] taNames=tas.split(",");
	
	Boolean add=true;
	for(int i=0;i<taNames.length;i++)
	{
		if(taNames[i].equals(taName))
		{
			add=false;
		     break;
		}
	}
return add;
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