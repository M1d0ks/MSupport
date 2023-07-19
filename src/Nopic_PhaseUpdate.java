import Test_Extract.DFCHelper;

import com.documentum.fc.client.IDfCollection;
import com.documentum.fc.client.IDfSession;
import com.documentum.fc.client.IDfSysObject;
import com.documentum.fc.common.DfException;
import com.documentum.fc.common.DfId;
import com.documentum.fc.common.DfTime;

public class Nopic_PhaseUpdate {
	public static void main(String args[]) throws DfException{
	IDfSession session=null;
	DFCHelper dfcHelper = new DFCHelper();
	String docbaseLoginId = "dmadmin-np";
	String docbasePassword = "merlin";
	String docbaseName = "midocsq";
	System.out.println("ReferenceUtil: main: enter");
	session = dfcHelper.createSession(docbaseLoginId, docbasePassword, docbaseName);
	//String documents="'US-TAS-S028'";
	//String documents="'US-SDL-S002402','US-GLI-U352','US-GLI-S003811','US-ZOM-S058','US-ZOM-S358','US-ZOM-S323'";
	//String query="select r_object_id,object_name,a_status,product_name,acl_name,approved_date from mi_response where a_status='Approved' and acl_name='acl_mi_response_pending_approval'";
	//String query="select r_object_id from mi_response where object_name in ("+documents+") and any r_version_label='15.0'";
	//String query="select r_object_id from mi_reference where object_name in ("+documents+")";   
	//String query="select * from mi_reference where any product_name in (Select config_product_name from mi_config_product where business_unit='Oncology' and active='1') and reference_type='2'";
	String query="SELECT r_object_id FROM mi_reference WHERE reference_type = 13 and a_status='Approved'";
	System.out.println("Query "+query); 
	IDfCollection col = DFCHelper.executeQuery(session, query);
	
	// Phase I --> Early Clinical Trial
	// Change Phase II to Early Clinical Trial
	// Change Phase III to  Registration Trial (Phase III/Pivotal)
	// Change Registration to In Registration
	
	while(col.next())		
	{
	
		String object_id=col.getString("r_object_id");
		IDfSysObject document = (IDfSysObject)session.getObject(new DfId(object_id));
		
		System.out.println("Document Name "+document.getString("object_name")+" Phase ---------> "+document.getString("pl_document_phase"));
	}
}
}