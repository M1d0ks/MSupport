import Test_Extract.DFCHelper;

import com.documentum.fc.client.IDfCollection;
import com.documentum.fc.client.IDfSession;
import com.documentum.fc.client.IDfSysObject;
import com.documentum.fc.common.DfException;
import com.documentum.fc.common.DfId;

public class Doc_Checkin {
	public static void main(String args[]) throws DfException
	{
	String filePath="C:/Documentum/Checkin/";
	IDfSession session=null;
	DFCHelper dfcHelper = new DFCHelper();
	String docbaseLoginId = "dmadmin-p";
	String docbasePassword = "merlin";
	String docbaseName = "midocsp";
	System.out.println("ReferenceUtil: main: enter");
	session = dfcHelper.createSession(docbaseLoginId, docbasePassword, docbaseName);
		
	String query="select r_object_id from mi_response where object_name='US-EXJ-S035'";
	
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
		String contentType= document.getContentType();
		String path=filePath+objName+"."+document.getFormat().getDOSExtension();
		
		if(!document.isCheckedOut())
		{
		//document.getFile(path);
			document.setFile(path);
			document.setContentType(contentType);
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
