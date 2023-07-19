import java.nio.file.Paths;

import Test_Extract.DFCHelper;

import com.documentum.fc.client.IDfCollection;
import com.documentum.fc.client.IDfSession;
import com.documentum.fc.client.IDfSysObject;
import com.documentum.fc.common.DfException;
import com.documentum.fc.common.DfId;
import com.documentum.fc.common.DfTime;
import com.documentum.fc.common.IDfId;

public class Approve_Document {
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
		String query=" select r_object_id from mi_response(all) where object_name='US-MAF-S006936' and any r_version_label='2.3'";
		//String query="select r_object_id from mi_response where object_name in ('US-AIN-S047','US-AIN-S104','US-AIN-S057','US-AIN-S086','US-AIN-S030')";
		//String query="select r_object_id from dm_document where object_name='US-EOQ-S001272'";
		//String query="select * from mi_reference where any product_name in (Select config_product_name from mi_config_product where business_unit='Oncology' and active='1') and reference_type='2'";
		//String query="select r_object_id from mi_reference where object_name='REF-TAF-PI041544'";
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
					//document.setString("owner_name", "dmadmin-np");
					//document.mark("3.0");
					//document.setString("product_name", "Cosentyx");
					//document.appendString("product_name", "Durezol");
					//document.setString("a_status", "Approved");
					//document.setString("acl_domain","midocs_prod");
					//document.setString("acl_name","acl_mi_response_draft");
					//document.unfreeze(arg0);
					document.cancelCheckout();
					
					//System.out.println("Versions "+document.getVersions(","));
					//document.appendString("product_name", "Kymriah");
					//document.setACLName("acl_mi_response_approved");
					//document.setInt("active", 1);
					//document.setString("emedical_noise_words","anaphylaxis, infusion related reaction");
					//document.setInt("active", 1);
					//document.setString("product_name", "Cosentyx");
					//document.setString("object_name", "Pluvicto");
					//document.setString("product_name", "Pluvicto");
					//document.setString("product_code", "PLU");
					
					//java.util.Date myDate = convertDateStringToBinaryUsingJava("May 28,2009 02:15:55 PM");
					//document.setTime("approved_date", new DfTime("TODAY"));
					//document.setTime("last_reviewed_date", new DfTime("TODAY"));
				    //document.setTime("expiration_date", new DfTime("TODAY"));
				    //document.setTime("r_modify_date", new DfTime("TODAY"));
				    //document.setTime("r_creation_date", new DfTime("TODAY"));
				    //document.mark("2.0");
				    //System.out.println("Expiration time "+document.getTime("r_creation_time"));
					//document.setTime("last_reviewed_date", "10/30/2019");
					//document.mark("1.0");
					//document.mark("6.0");
					//makeImmutable(document);
					//final IDfId renditionQueueId = document.queue("dm_autorender_win31", "rendition", 0, false,null, "rendition_req_ps_pdf");
					document.save();
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

