package MIDOCS_Upgrade;

import java.nio.file.Paths;

import Test_Extract.DFCHelper;

import com.documentum.fc.client.IDfCollection;
import com.documentum.fc.client.IDfGroup;
import com.documentum.fc.client.IDfRelation;
import com.documentum.fc.client.IDfSession;
import com.documentum.fc.client.IDfSysObject;
import com.documentum.fc.client.IDfUser;
import com.documentum.fc.common.DfException;
import com.documentum.fc.common.DfId;
import com.documentum.fc.common.DfTime;
import com.documentum.fc.common.IDfId;

public class CreateGuestUsers {
	public static void main(String args[]) throws DfException
	{
		IDfSession session=null;
		DFCHelper dfcHelper = new DFCHelper();
		String docbaseLoginId = "midocsp";
		String docbasePassword = "m1d0csp74";
		String docbaseName = "midocs_prod";
		System.out.println("ReferenceUtil: main: enter");
		session = dfcHelper.createSession(docbaseLoginId, docbasePassword, docbaseName);
		
		//Enter user name
		String username="Justin Paolino";
		String u521="paoliju1";
		String email="JUSTIN.PAOLINO@NOVARTIS.COM";
		
		//User's account has been provisioned successfully. User should be able to access and view documents now.
		
		IDfUser user = (IDfUser)session.newObject("dm_user");
    	
		user.setUserName(username);
		user.setUserLoginName(u521);
		user.setUserAddress(email);
		user.setString("user_source","LDAP");
		user.setString("user_login_domain","Internal_LDAP_Server");
		user.setString("user_os_name",u521);
		
		
		user.setClientCapability(IDfUser.DF_CAPABILITY_CONSUMER);
		user.setUserPrivileges(IDfUser.DF_PRIVILEGE_CREATE_GROUP);
		user.setUserXPrivileges(IDfUser.DF_XPRIVILEGE_CONFIG_AUDIT 
				+ IDfUser.DF_XPRIVILEGE_VIEW_AUDIT);
		user.save();
		String[] groups= {"mi_confirmed_users","mi_workgroup","mi_guest","midocs_users"};
		System.out.println(" Adding user Name "+username);
		for(int i=0;i<groups.length;i++){
			IDfGroup group= (IDfGroup)session.getGroup(groups[i]);
			group.addUser(user.getString("user_name"));
			System.out.println(" Added");
			group.save();
			System.out.println("Saved");
		}
		
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

