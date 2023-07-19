
import java.util.List;

import Test_Extract.DFCHelper;

import com.documentum.fc.client.IDfCollection;
import com.documentum.fc.client.IDfSession;
import com.documentum.fc.client.IDfSysObject;
import com.documentum.fc.common.DfException;
import com.documentum.fc.common.DfId;
import com.documentum.fc.common.IDfId;


public class AddProducts {
	public static void main(String args[]) throws DfException
	{
		IDfSession session=null;
		DFCHelper dfcHelper = new DFCHelper();
		String docbaseLoginId = "dmadmin-p";
		String docbasePassword = "merlin";
		String docbaseName = "midocsp";
		System.out.println("ReferenceUtil: main: enter");
		session = dfcHelper.createSession(docbaseLoginId, docbasePassword, docbaseName);
		String docName="'US-SEG-S004578'";

		String resQuery = "select r_object_id from mi_response  where object_name in ("+docName+")";
		System.out.println("Query--"+resQuery);
		IDfCollection resColl = DFCHelper.executeQuery(session, resQuery);

		while(resColl.next())
		{
			try{
				String objectId=resColl.getString("r_object_id");
				System.out.println("object id "+objectId);
				IDfSysObject sysObj=(IDfSysObject)session.getObject((IDfId) new DfId(objectId));
				//sysObj.appendString("product_name", "Other Oncology Product");
				List<String> products= getOncProds(session);
				System.out.println("Adding product_name to the document "+products);
				//sysObj.save();
			}catch(Exception e)
			{
				System.out.println("Exception for document "+e);
			}

		}
	}
	
	private static List<String> getOncProds(IDfSession session) throws DfException {
		List<String> oncprods = null;
		String query="select config_product_name from mi_config_product where business_unit='Oncology' and active=1";
		System.out.println("Query "+query);
		IDfCollection col = DFCHelper.executeQuery(session, query);
		while(col.next())
		{
			String prod=col.getString("config_product_name");
			oncprods.add(prod);
		}
		return oncprods;
	}
}

