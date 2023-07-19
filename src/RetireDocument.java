import Test_Extract.DFCHelper;

import com.documentum.fc.client.IDfCollection;
import com.documentum.fc.client.IDfSession;
import com.documentum.fc.client.IDfSysObject;
import com.documentum.fc.common.DfException;
import com.documentum.fc.common.DfId;
import com.documentum.fc.common.DfTime;
import com.documentum.fc.common.IDfId;


public class RetireDocument {
	public static void main(String args[]) throws DfException
	{
		DFCHelper dfcHelper = new DFCHelper();
		String docbaseLoginId = "midocsp";
		String docbasePassword = "m1d0csp74";
		String docbaseName = "midocs_prod";
		System.out.println("ReferenceUtil: main: enter");
		IDfSession session = dfcHelper.createSession(docbaseLoginId, docbasePassword, docbaseName);

		try {	

			updateExpiredDocs(session);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("error: " + e.getMessage());
			throw e;
		} 
	}

	private static  void updateExpiredDocs(IDfSession pSession) throws DfException {
		//String today="10/13/2017";
		StringBuffer strBuf=new StringBuffer();
		//String docName="'US-SDL-S002923'";
		//strBuf.append("select r_object_id from mi_response(all) where expiration_date<=Date('");
		//strBuf.append("select r_object_id from mi_response where object_name in ("+docName+")");
		strBuf.append("select r_object_id from mi_response where object_name='US-RIB-S010461'");
		
		//+ "and expiration_date<=Date('");
		//strBuf.append(today);
		//strBuf.append("') AND expiration_date is not nulldate AND a_status='Approved'");
		System.out.println("Query "+strBuf);
		IDfCollection col = DFCHelper.executeQuery(pSession, strBuf.toString());
		//IDfCollection col=MINotificationHelper.executeQuery(pSession, strBuf.toString());


		while(col.next()){			
			String objectId=col.getString("r_object_id");
			System.out.println("object id "+objectId);
			IDfSysObject sysObj=(IDfSysObject)pSession.getObject((IDfId) new DfId(objectId));
			if(sysObj.isImmutable())
			{
				System.out.println("It is immutable"+objectId);
				//makeMutable(pSession,objectId);
				updateResposeDoc(pSession,objectId);
				//makeImmutable(pSession,objectId);
			}else{
				updateResposeDoc(pSession,objectId);
			}
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
		//sysObj.setStatus("Approved");
		//sysObj.setACLName("acl_mi_response_approved");
		//sysObj.setString("retired_by", "dmadmin-p");
		//sysObj.setInt("active",1);
		sysObj.setBoolean("i_latest_flag", true);
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
