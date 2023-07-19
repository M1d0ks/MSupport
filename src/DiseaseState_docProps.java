import Test_Extract.DFCHelper;

import com.documentum.fc.client.IDfCollection;
import com.documentum.fc.client.IDfSession;
import com.documentum.fc.client.IDfSysObject;
import com.documentum.fc.common.DfException;
import com.documentum.fc.common.DfId;

public class DiseaseState_docProps {
	public static void main(String args[]) throws DfException
	{
		IDfSession session=null;
		DFCHelper dfcHelper = new DFCHelper();
		String docbaseLoginId = "dmadmin-p";
		String docbasePassword = "merlin";
		String docbaseName = "midocsp";
		System.out.println("ReferenceUtil: main: enter");
		session = dfcHelper.createSession(docbaseLoginId, docbasePassword, docbaseName);
		String documents="'US-LCZ-S006194'";
		//String documents="'US-SDL-S002402','US-GLI-U352','US-GLI-S003811','US-ZOM-S058','US-ZOM-S358','US-ZOM-S323'";
		//String query="select r_object_id,object_name,a_status,product_name,acl_name,approved_date from mi_response where a_status='Approved' and acl_name='acl_mi_response_pending_approval'";
		String query="select r_object_id from mi_response where object_name in ("+documents+")";
		//String query="select r_object_id from mi_reference where object_name in ("+documents+")";   
		//String query="select r_object_id from mi_config_journal where active='1'";
		//String query="select * from mi_config_product where business_unit='Oncology' and active='1'";
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
					//String version = session.apiGet("get", object_id + ",r_version_label");
					//String version=document.getString("r_version_label");
							//document.setString("owner_name", "dmadmin-p");
							//document.mark("2.0");
							document.setString("a_status", "Approved");
						    document.setACLName("acl_mi_response_approved");
						    document.setInt("active", 1);
						    // document.setInt("active", 1);
							//document.mark("1.0");
							//document.mark("6.0");
							//makeImmutable(document);
							/*document.setString("di_content_title","Disease State test Prod");
							document.setString("di_nprc","12134");
							document.setString("di_content_type","Cardiology");
							document.setString("di_content_overview","EMed oververview content from Midocs production");
							document.setString("di_nprc_expdate","4/12/2019");
							document.setString("di_disease_condition","Biosimilars");*/

					
							document.save();
							System.out.println("document saved");
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

