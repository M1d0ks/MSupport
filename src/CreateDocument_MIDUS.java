import Test_Extract.DFCHelper;

import com.documentum.com.DfClientX;
import com.documentum.com.IDfClientX;
import com.documentum.fc.client.IDfClient;
import com.documentum.fc.client.IDfCollection;
import com.documentum.fc.client.IDfDocument;
import com.documentum.fc.client.IDfSession;
import com.documentum.fc.client.IDfSessionManager;
import com.documentum.fc.client.IDfSysObject;
import com.documentum.fc.common.DfException;
import com.documentum.fc.common.DfId;
import com.documentum.fc.common.IDfId;
import com.documentum.fc.common.IDfLoginInfo;
import com.documentum.operations.IDfExportNode;
import com.documentum.operations.IDfExportOperation;


public class CreateDocument_MIDUS {

	public static void main(String args[]) throws DfException
	{
		String sDirectoryPath ="C:\\Geodart\\Ref_extracts\\";
		//String sDirectoryPath ="/home/kucheja1/refextract/";
		IDfSession session=null;
		DFCHelper dfcHelper = new DFCHelper();
		String docbaseLoginId = "dmadmin-p";
		String docbasePassword = "merlin";
		String docbaseName = "midocsp";
		System.out.println("ReferenceUtil: main: enter");
		session = dfcHelper.createSession(docbaseLoginId, docbasePassword, docbaseName);
		
		IDfSession dfSession = null;
	    IDfSessionManager dfSessionManager = null;
	    System.out.println("instantiating client x libraries");
	    IDfClientX clientx = new DfClientX();
	    System.out.println("client x initialized");
	    IDfClient client = clientx.getLocalClient();
	    System.out.println("df client initialized");
	    dfSessionManager = client.newSessionManager();
	    System.out.println("retrieved session manager");
	    IDfLoginInfo loginInfo = clientx.getLoginInfo();
	    loginInfo.setUser(docbaseLoginId);
	    loginInfo.setPassword(docbasePassword);
	    System.out.println("setting user identity for docbase session");
	    dfSessionManager.setIdentity(docbaseName, loginInfo);
	    dfSession = dfSessionManager.getSession(docbaseName);
	    System.out.println("docbase session retrieved");
}
}
