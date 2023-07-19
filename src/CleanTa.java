import java.util.HashMap;

import Test_Extract.DFCHelper;

import com.documentum.fc.client.IDfCollection;
import com.documentum.fc.client.IDfSession;
import com.documentum.fc.client.IDfSysObject;
import com.documentum.fc.client.IDfWorkflow;
import com.documentum.fc.common.DfException;
import com.documentum.fc.common.DfId;
import com.documentum.fc.common.IDfId;
import com.emc.d2.api.D2Session;
import com.emc.d2.api.methods.D2CoreMethod;
import com.emc.d2.api.methods.D2Method;



public class CleanTa {
	public static void main(String args[]) throws DfException
	{
		DFCHelper dfcHelper = new DFCHelper();
		String docbaseLoginId = "dmadmin-p";
		String docbasePassword = "merlin";
		String docbaseName = "midocsp";
		System.out.println("ReferenceUtil: main: enter");
		IDfSession session = dfcHelper.createSession(docbaseLoginId, docbasePassword, docbaseName);

		changeTa(session);
		//deleteProduct(session,oldName);

	}

	private static void changeTa(IDfSession session) throws DfException {
		//String query="Select r_object_id from mi_response where any product_name='"+oldName+"'";
		String query="Select r_object_id from mi_response where any product_name='Arranon'";

		System.out.println("Query "+query);

		IDfCollection col = DFCHelper.executeQuery(session, query);
		int count=0;
		while(col.next())
		{
			//System.out.println("Number of document "+count);
			String object_id=col.getString("r_object_id");
			count++;

			IDfSysObject document = (IDfSysObject)session.getObject(new DfId(object_id));

			if(!document.isCheckedOut())
			{
				String objName=document.getString("object_name");
				//System.out.println("Changing TA to the document "+objName);

				String tas=document.getAllRepeatingStrings("ta_name",",");
				//System.out.println("Ta Names "+tas);
				String[] taNames=tas.split(",");

				if(taNames.length>1)
				{

					for(int i=0;i<taNames.length;i++)
					{
						for(int j=i+1;j<taNames.length;j++)
						{
							if (taNames[i].equals(taNames[j]))
							{
								System.out.println("Ta Names length "+taNames.length);
								System.out.println("Document name "+objName+" Repeating TA "+taNames[i]);
							}
						}
					}
				}
			}		

		}
	}
}
