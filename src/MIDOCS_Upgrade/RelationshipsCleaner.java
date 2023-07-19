package MIDOCS_Upgrade;

import Test_Extract.DFCHelper;
import com.documentum.fc.client.IDfCollection;
import com.documentum.fc.client.IDfRelation;
import com.documentum.fc.client.IDfSession;
import com.documentum.fc.client.IDfSysObject;
import com.documentum.fc.client.IDfUser;
import com.documentum.fc.common.DfException;
import com.documentum.fc.common.DfId;
import com.documentum.fc.common.DfTime;
import com.documentum.fc.common.IDfId;

public class RelationshipsCleaner {
	public static void main(String args[]) throws DfException
	{
		IDfSession session=null;
		DFCHelper dfcHelper = new DFCHelper();
		String docbaseLoginId = "midocsp";
		String docbasePassword = "m1d0csp74";
		String docbaseName = "midocs_prod";
		System.out.println("ReferenceUtil: main: enter");
		session = dfcHelper.createSession(docbaseLoginId, docbasePassword, docbaseName);
		//String documents="'US-TAS-S028'";
		String query="select r_object_id from mi_reference where a_status='Approved' and acl_name='acl_mi_reference_approved' ";
		System.out.println("Query "+query); 
		IDfCollection col = DFCHelper.executeQuery(session, query);
		while(col.next())		
		{
			
			while(col.next())		
			{
			
			}
		}
		col.close();
	}

	private static void makeImmutable(IDfSysObject document) throws DfException {
		document.setInt("r_immutable_flag", 1);
		document.save();
	}

	private static void makeMutable( IDfSysObject document) throws DfException 
	{
		document.setInt("r_immutable_flag", 0);
		document.save();
	}

}

