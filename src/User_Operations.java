import Test_Extract.DFCHelper;

import com.documentum.fc.client.IDfSession;
import com.documentum.fc.common.DfException;


public class User_Operations {
public static void main(String args[]) throws DfException
{
	DFCHelper dfcHelper = new DFCHelper();
	String docbaseLoginId = "dmadmin-p";
	String docbasePassword = "merlin";
	String docbaseName = "midocsp";
	System.out.println("ReferenceUtil: main: enter");
	IDfSession dfSession = dfcHelper.createSession(docbaseLoginId, docbasePassword, docbaseName);
} 
}
