import Test_Extract.DFCHelper;

import com.documentum.fc.client.IDfSession;
import com.documentum.fc.client.IDfSysObject;
import com.documentum.fc.common.DfException;
import com.documentum.fc.common.DfId;


public class DocumentApprover {

	public static void main(String args[]) throws DfException
	{
		IDfSession session=null;
		DFCHelper dfcHelper = new DFCHelper();
		String docbaseLoginId = "dmadmin-p";
		String docbasePassword = "merlin";
		String docbaseName = "midocsp";
		System.out.println("ReferenceUtil: main: enter");
		session = dfcHelper.createSession(docbaseLoginId, docbasePassword, docbaseName);

		System.out.println("ReferenceUtil: main: enter");
		
		String objectId="0902b7208023e523";
		IDfSysObject sObj = (IDfSysObject)session.getObject(new DfId(objectId));
		sObj.mark("10.7");
		
		sObj.save();

		
	}
}
