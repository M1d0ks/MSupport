package MIDOCS_Upgrade;

import Test_Extract.DFCHelper;

import com.documentum.fc.client.IDfCollection;
import com.documentum.fc.client.IDfDocument;
import com.documentum.fc.client.IDfSession;
import com.documentum.fc.client.IDfSysObject;
import com.documentum.fc.common.DfException;
import com.documentum.fc.common.DfId;
import com.documentum.fc.common.DfTime;
import com.documentum.fc.common.IDfId;


public class VersionCleanup {
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
		strBuf.append("select r_object_id,object_name,a_status,r_version_label,i_latest_flag,i_branch_cnt from mi_reference(all) where r_object_id='0902e9e9802438c4'");
		
		System.out.println("Query "+strBuf);
		IDfCollection col = DFCHelper.executeQuery(pSession, strBuf.toString());
		int i=0;
		while(col.next()){			
			String objectId=col.getString("r_object_id");
			System.out.println("object id "+objectId+" count "+i);
			
			IDfDocument doc=(IDfDocument)pSession.getObject((IDfId) new DfId(objectId));
			if(!doc.isImmutable() && !doc.isCheckedOut()){
		    //doc.mark("1.1");
			//doc.setRepeatingString("r_version_label", 1, "");
			//doc.setString("a_status","Draft");
			doc.save();
			makeImmutable(pSession,objectId);
			}else{
				makeMutable(pSession,objectId);
				doc.setBoolean("i_latest_flag", true);
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


	private static void makeMutable(IDfSession pSession, String objectId) throws DfException 
	{
		System.out.println("Doc is immutable");
		boolean ret=false;
		IDfSysObject obj=(IDfSysObject)pSession.getObject(new DfId(objectId));
		obj.setInt("r_immutable_flag", 0);
		obj.save();
	}
}
