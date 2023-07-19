import Test_Extract.DFCHelper;

import com.documentum.fc.client.IDfCollection;
import com.documentum.fc.client.IDfGroup;
import com.documentum.fc.client.IDfSession;
import com.documentum.fc.client.IDfSysObject;
import com.documentum.fc.client.IDfUser;
import com.documentum.fc.common.DfException;
import com.documentum.fc.common.DfId;
import com.documentum.fc.common.IDfId;


public class Guest {
public static void main(String args[]) throws DfException
{
	IDfSession session=null;
	DFCHelper dfcHelper = new DFCHelper();
	String docbaseLoginId = "midocsp";
	String docbasePassword = "m1d0csp74";
	String docbaseName = "midocs_prod";
	System.out.println("ReferenceUtil: main: enter");
	session = dfcHelper.createSession(docbaseLoginId, docbasePassword, docbaseName);
	
	String resQuery = "select user_name from dm_user where user_name not in (select i_all_users_names from dm_group where group_name='mi_onc_authors') and user_name not in (select i_all_users_names from dm_group where group_name='mi_gen_authors') and user_source='LDAP' ";
	IDfCollection resColl = DFCHelper.executeQuery(session, resQuery);

   while(resColl.next())
   {
	   			String userName=resColl.getString("user_name");
	   			IDfGroup group= (IDfGroup)session.getGroup ("mi_guest");
	   			System.out.println(" Adding user Name "+userName);
	   			group.addUser(userName);
	   			System.out.println(" Added");
	   			group.save();
	   			System.out.println("Saved");
   }
   resColl.close();
}
}
