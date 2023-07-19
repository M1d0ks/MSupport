import Test_Extract.DFCHelper;

import com.documentum.fc.client.IDfCollection;
import com.documentum.fc.client.IDfSession;
import com.documentum.fc.client.IDfSysObject;
import com.documentum.fc.client.IDfUser;
import com.documentum.fc.common.DfException;
import com.documentum.fc.common.DfId;


public class User_BulkUpdate {
	public static void main(String args[]) throws DfException
	{
	IDfSession dfSession=null;
	DFCHelper dfcHelper = new DFCHelper();
	String docbaseLoginId = "midocsp";
	String docbasePassword = "m1d0csp74";
	String docbaseName = "midocs_prod";
	System.out.println("ReferenceUtil: main: enter");
	dfSession = dfcHelper.createSession(docbaseLoginId, docbasePassword, docbaseName);
	
	String query="select r_object_id from dm_user where user_ldap_dn is not null and user_source='LDAP' and user_name not in ('dmadmin','midocs_prod','midocsp')"
			+ " and user_name in (select i_all_users_names from dm_group where )";
			//"select * from dm_user where user_ldap_dn is not null and user_source='LDAP' and last_login_utc_time > DATE('04/20/2023 14:35:00','MM/DD/YYYY HH:MM:SS') and user_name not in ('dmadmin','midocs_prod')";
	
	System.out.println("Query "+query);
	IDfCollection resColl = DFCHelper.executeQuery(dfSession, query);
	
	while(resColl.next())
	{
		String objId=resColl.getString("r_object_id");
		System.out.println(objId);

		IDfUser user=(IDfUser) dfSession.getObjectByQualification
				("dm_user where r_object_id = '"+objId+"'");

		String userLoginName=user.getString("user_login_name");
		System.out.println("User "+userLoginName+" Updated");
		user.setString("user_login_name", userLoginName.toLowerCase());
		user.save();
		System.out.println(userLoginName);
	}
	}
}
