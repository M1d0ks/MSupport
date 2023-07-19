package MIDOCS_Upgrade;

import java.nio.file.Paths;

import Test_Extract.DFCHelper;

import com.documentum.fc.client.IDfCollection;
import com.documentum.fc.client.IDfRelation;
import com.documentum.fc.client.IDfSession;
import com.documentum.fc.client.IDfSysObject;
import com.documentum.fc.common.DfException;
import com.documentum.fc.common.DfId;
import com.documentum.fc.common.DfTime;
import com.documentum.fc.common.IDfId;

public class RequestRenditions {
	public static void main(String args[]) throws DfException
	{
		IDfSession session=null;
		DFCHelper dfcHelper = new DFCHelper();
		String docbaseLoginId = "midocsp";
		String docbasePassword = "m1d0csp74";
		String docbaseName = "midocs_prod";
		System.out.println("ReferenceUtil: main: enter");
		session = dfcHelper.createSession(docbaseLoginId, docbasePassword, docbaseName);
		//String documents="'US-TAS-S028'";
		String query=" select r_object_id from mi_response where object_name='US-XOL-S013'";
		IDfCollection col = DFCHelper.executeQuery(session, query);
		int c=0;
		while(col.next())		
		{
			try
			{
				String object_id=col.getString("r_object_id");
				IDfSysObject document = (IDfSysObject)session.getObject(new DfId(object_id));
				

				String objName= document.getString("object_name");
				//String version = session.apiGet("get", object_id + ",r_version_label");
				//String version=document.getString("r_version_label");
				if(document.isImmutable()){
					System.out.println("Immutable document "+objName);
					makeMutable(document);
					System.out.println("document name "+objName);
					System.out.println("Inactivating the document "+objName+" Count "+c);
					//document.setString("owner_name", "dmadmin-p");
					//document.mark("2.0");
					//document.appendString("product_name", "Mayzent");
					//document.setString("a_status", "Approved");
					//document.setACLName("acl_mi_response_approved");
					//document.setInt("active", 1);
					//document.setInt("active", 1);
					//document.mark("1.0");
					//document.mark("6.0");
					//makeImmutable(document);
					document.save();
					makeImmutable(document);
					System.out.println("document saved");

				}else{
					System.out.println("document name "+objName);
					System.out.println("Approving the document "+objName+" Count "+c+" Product "+document.getAllRepeatingStrings("product_name", ","));
					IDfRelation relationObject = (IDfRelation)session.newObject("dm_relation");
					relationObject.setRelationName("mi_response_overview");
					relationObject.setParentId(new DfId(object_id));
					relationObject.setChildId(new DfId("0902b7208004db69"));
					relationObject.setChildLabel("1.0");
					relationObject.save();
					System.out.println("document saved");
				}
				c++;
			}
			catch(Exception e)
			{
				System.out.println("Error "+e);
			}

		}
		col.close();
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

