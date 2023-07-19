import Test_Extract.DFCHelper;

import com.documentum.fc.client.IDfCollection;
import com.documentum.fc.client.IDfSession;
import com.documentum.fc.client.IDfSysObject;
import com.documentum.fc.common.DfException;
import com.documentum.fc.common.DfId;


public class Remove_ProductOwner {
	public static void main(String args[]) throws DfException
	{
	IDfSession session=null;
	DFCHelper dfcHelper = new DFCHelper();
	String docbaseLoginId = "midocsp";
	String docbasePassword = "m1d0csp74";
	String docbaseName = "midocs_prod";
	System.out.println("ReferenceUtil: main: enter");
	session = dfcHelper.createSession(docbaseLoginId, docbasePassword, docbaseName);
	
	//String documents="('US-ARR-S024','US-ARR-S005','US-ARR-S006','US-ARR-S025','US-ARR-S002','US-ARR-S033','US-ARR-S013','US-ARR-S014'',''US-ARR-S015'',''US-ARR-S004','US-ARR-S016','US-ARR-S027','US-ARR-S021','US-ARR-S007','US-ARR-S018','US-ARR-S001','US-ARR-S029','US-ARR-S019','US-ARR-S035','US-ARR-S030')";
	
	//String query="select r_object_id,object_name from mi_response where object_name in ('US-ARR-S024','US-ARR-S005','US-ARR-S006','US-ARR-S025','US-ARR-S002','US-ARR-S033','US-ARR-S013','US-ARR-S014'',''US-ARR-S015'',''US-ARR-S004','US-ARR-S016','US-ARR-S027','US-ARR-S021','US-ARR-S007','US-ARR-S018','US-ARR-S001','US-ARR-S029','US-ARR-S019','US-ARR-S035','US-ARR-S030')";
	//String query="select * from mi_config_product where any config_product_owner= 'fikhmal1'";
	String query="select r_object_id from mi_config_product where business_unit!='Oncology' and active='1'";
	
	System.out.println("Query "+query);
	IDfCollection col = DFCHelper.executeQuery(session, query);
	
	while(col.next())
	{
		String object_id=col.getString("r_object_id");
		IDfSysObject document = (IDfSysObject)session.getObject(new DfId(object_id));
		String object_name=document.getString("config_product_name");
		
		String pOwners=document.getAllRepeatingStrings("config_product_owner", ",");
		String[] userName = {"shahtr"};
		System.out.println(pOwners);
		String[] owners=pOwners.split(",");
		
		System.out.println("Removing user to product "+object_name);
	for(int u=0;u<userName.length;u++)
	{
		// for(int i=0;i<owners.length;i++)
		//{
			//if(owners[i].equals(userName[u]))
			//{
				System.out.println("product "+object_name+" removing "+userName[u]);
				document.removeAll("config_product_owner");
				document.save();
			//}
		//} 
	}
	}
	}
}
