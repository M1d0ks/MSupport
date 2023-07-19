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


public class Workflow_aborter{
public static void main(String args[]) throws DfException
{
	DFCHelper dfcHelper = new DFCHelper();
	String docbaseLoginId = "dmadmin-p";
	String docbasePassword = "merlin";
	String docbaseName = "midocsp";
	System.out.println("ReferenceUtil: main: enter");
	IDfSession session = dfcHelper.createSession(docbaseLoginId, docbasePassword, docbaseName);
	try {	
		String[] object_name={"US-AIN-S065"};
		abortwf(session,object_name);
		
		} catch (Exception e) {
		e.printStackTrace();
		System.out.println("error: " + e.getMessage());
		throw e;
	} 
	
}

private static void abortwf(IDfSession session, String[] object_name) throws DfException {
	
	
	for(int i=0;i<object_name.length;i++)
	{
	//strBuf.append("select r_object_id from mi_response(all) where expiration_date<=Date('");
	String query="select r_object_id from dm_workflow where"
			+ " r_object_id in (select r_workflow_id from dmi_package where any r_component_id in "
			+ "(Select r_object_id from mi_response where object_name='"+object_name[i]+"'))";
	System.out.println("Query "+query);
	IDfCollection col = DFCHelper.executeQuery(session, query);
	//updateProps(session, object_name[i]);
	while(col.next())
	{
		try{
		String objId=col.getString("r_object_id");
		//String objId="4d02b72080005989";
		IDfWorkflow workflowObj = (IDfWorkflow)session.getObject((IDfId) new DfId(objId));
		System.out.println("Got workflow");
		workflowObj.haltAll();
		workflowObj.abort();
		workflowObj.destroy();
		//workflowObj.restart(arg0);
		session.apiExec("abort, session, objId", objId);
		System.out.println("Aborting workflow for "+object_name);
	   //updateProps(session, object_name[i]);	
	}catch(Exception e)
		{
		System.out.println("Abort failed for "+object_name);
		}
	}
	col.close();
	}
}

private static void updateProps(IDfSession session, String object_name) throws DfException {
	IDfSysObject sysObj=(IDfSysObject)session.getObjectByQualification("mi_response where object_name='"+object_name+"'");
	//HashMap arguments = new HashMap();
	
	try{
	/*arguments.put(D2Method.ARG_ID, sysObj);
	arguments.put(D2CoreMethod.ARG_AUTOLINK, Boolean.TRUE);
	arguments.put(D2CoreMethod.ARG_SECURITY, Boolean.TRUE);
	D2Session.initTBO(session);
	D2Method.start(session, D2CoreMethod.class, arguments);*/
	//sysObj.setString("owner_name", "saidsa3");
	System.out.println("Here");
	sysObj.setString("a_status", "Draft");
	sysObj.setACLName("acl_mi_response_draft");
	//sysObj.mark("11.0, Draft");
	sysObj.save();
	}
	catch (Exception ex) {
		System.out.println(ex.getMessage());
	}
	}

}