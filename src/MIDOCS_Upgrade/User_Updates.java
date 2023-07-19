package MIDOCS_Upgrade;

import Test_Extract.DFCHelper;

import com.documentum.fc.client.IDfCollection;
import com.documentum.fc.client.IDfGroup;
import com.documentum.fc.client.IDfSession;
import com.documentum.fc.client.IDfSysObject;
import com.documentum.fc.client.IDfUser;
import com.documentum.fc.common.DfException;
import com.documentum.fc.common.DfId;
import com.documentum.fc.common.IDfId;


public class User_Updates {
public static void main(String args[]) throws DfException
{
	IDfSession session=null;
	DFCHelper dfcHelper = new DFCHelper();
	String docbaseLoginId = "midocsp";
	String docbasePassword = "m1d0csp74";
	String docbaseName = "midocs_prod";
	System.out.println("ReferenceUtil: main: enter");
	session = dfcHelper.createSession(docbaseLoginId, docbasePassword, docbaseName);
	
	String resQuery = "select r_object_id,user_name from dm_user where"
			+ " user_source='LDAP' and user_name not in ('dmadmin','midocs_prod','midocsp')";
	IDfCollection resColl = DFCHelper.executeQuery(session, resQuery);

   while(resColl.next())
   {
	   			String userName=resColl.getString("user_name");
	   			String objId=resColl.getString("r_object_id");
	   		 final IDfUser user = (IDfUser)session.getObjectByQualification("dm_user where user_name='" + userName + "'");
	   			user.setClientCapability(IDfUser.DF_CAPABILITY_COORDINATOR);
	   			user.setUserPrivileges(IDfUser.DF_PRIVILEGE_CREATE_GROUP);
	   			user.setUserXPrivileges(IDfUser.DF_XPRIVILEGE_CONFIG_AUDIT 
						+ IDfUser.DF_XPRIVILEGE_VIEW_AUDIT);
	   			String[] groups= {"mi_confirmed_users","mi_workgroup"};
	   			System.out.println(" Adding user Name "+userName);
	   			for(int i=0;i<groups.length;i++){
	   			IDfGroup group= (IDfGroup)session.getGroup(groups[i]);
				group.addUser(user.getString("user_name"));
				System.out.println(" Added");
				group.save();
				System.out.println("Saved");
	   			}
			}
	   			System.out.println("Saved");
   }
}

