package MIDOCS_Upgrade;

import Test_Extract.DFCHelper;

import com.documentum.fc.client.IDfCollection;
import com.documentum.fc.client.IDfSession;
import com.documentum.fc.client.IDfSysObject;
import com.documentum.fc.common.DfException;
import com.documentum.fc.common.DfId;
import com.documentum.fc.common.DfTime;
import com.documentum.fc.common.IDfId;


public class RefAclUpdate {
	public static void main(String args[]) throws DfException
	{
		DFCHelper dfcHelper = new DFCHelper();
		String docbaseLoginId = "midocsp";
		String docbasePassword = "m1d0csp74";
		String docbaseName = "midocs_prod";
		System.out.println("ReferenceUtil: main: enter");
		IDfSession session = dfcHelper.createSession(docbaseLoginId, docbasePassword, docbaseName);

		try {	
			updateRefDocs(session);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("error: " + e.getMessage());
			throw e;
		} 
	}

	private static  void updateRefDocs(IDfSession pSession) throws DfException {
		
		StringBuffer strBuf=new StringBuffer();
		strBuf.append("select r_object_id,object_name,a_status,r_version_label from mi_response(all) where r_object_id='0902e9e98022fad8'");

		System.out.println("Query "+strBuf);
		IDfCollection col = DFCHelper.executeQuery(pSession, strBuf.toString());
		int i=0;
		while(col.next()){			
			String objectId=col.getString("r_object_id");
			System.out.println("object id "+objectId+" count "+i);
			
			IDfSysObject doc=(IDfSysObject)pSession.getObject((IDfId) new DfId(objectId));
			if(!doc.isImmutable() && !doc.isCheckedOut()){
			//doc.setBoolean("i_latest_flag", true);
			doc.setString("acl_name","acl_mi_response_retired");
			doc.setString("acl_domain", "midocs_prod");
			doc.setString("a_status", "Retired");
			//doc.setString("group_name", "mi_authors");
			//doc.setString("group_permit","6");
			doc.save();
			}
			i++;
		}
		col.close();
	}

	private static void makeImmutable(IDfSession pSession, String objectId) throws DfException {
		IDfSysObject obj=(IDfSysObject)pSession.getObject(new DfId(objectId));
		obj.setInt("r_immutable_flag", 1);
		obj.save();
	}

	private static void updateResposeDoc(IDfSession pSession, String objectId) throws DfException 
	{
		IDfSysObject sysObj=(IDfSysObject)pSession.getObject((IDfId) new DfId(objectId));
		String today="04/10/2018";
		//sysObj.setTime("retired_date", new DfTime(today,"MM-dd-yyyy HH:mm:ss"));	
		sysObj.setStatus("Approved");
		sysObj.setACLName("acl_mi_response_approved");
		//sysObj.setString("retired_by", "dmadmin-p");
		sysObj.setInt("active",1);
		//sysObj.mark("3.0");
		//sysObj.setACLName("ACL_RESPONSE_DOCUMENT_RETIRED");
		sysObj.save();
	}

	private static void makeMutable(IDfSession pSession, String objectId) throws DfException 
	{
		boolean ret=false;
		IDfSysObject obj=(IDfSysObject)pSession.getObject(new DfId(objectId));
		obj.setInt("r_immutable_flag", 0);
		obj.save();
	}
}
