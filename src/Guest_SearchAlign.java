import Test_Extract.DFCHelper;

import com.documentum.fc.client.IDfCollection;
import com.documentum.fc.client.IDfSession;
import com.documentum.fc.client.IDfSysObject;
import com.documentum.fc.common.DfException;
import com.documentum.fc.common.DfId;

public class Guest_SearchAlign {
	public static void main(String args[]) throws DfException
	{
	IDfSession session=null;
	DFCHelper dfcHelper = new DFCHelper();
	String docbaseLoginId = "dmadmin-p";
	String docbasePassword = "merlin";
	String docbaseName = "midocsp";
	System.out.println("ReferenceUtil: main: enter");
	session = dfcHelper.createSession(docbaseLoginId, docbasePassword, docbaseName);
	
	//String documents="('US-ARR-S024','US-ARR-S005','US-ARR-S006','US-ARR-S025','US-ARR-S002','US-ARR-S033','US-ARR-S013','US-ARR-S014'',''US-ARR-S015'',''US-ARR-S004','US-ARR-S016','US-ARR-S027','US-ARR-S021','US-ARR-S007','US-ARR-S018','US-ARR-S001','US-ARR-S029','US-ARR-S019','US-ARR-S035','US-ARR-S030')";
	
	String query="select user_name from dm_user where user_name in  "
			+ "(select i_all_users_names from dm_group where group_name='mi_authors')'";
	
	System.out.println("Query "+query);
	IDfCollection col = DFCHelper.executeQuery(session, query);
	int c=0;
	while(col.next())
	{
		try
		{
		String object_id=col.getString("r_object_id");
		IDfSysObject document = (IDfSysObject)session.getObject(new DfId(object_id));	
		String objName= document.getString("object_name");
		if(!document.isCheckedOut())
		{
		System.out.println("Changing the ACl of the document "+objName+" Count "+c);
		document.setACLName("acl_mi_response_approved");
		document.save();
		}
		c++;
		}catch(Exception e)
		{
			System.out.println("Error "+e);
		}
		}
	col.close();
	}
}
