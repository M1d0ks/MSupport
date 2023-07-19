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

public class SearchUpdate {
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
		String query="select * from d2_queryform_config where r_object_id='0802e9e9800361f9'";
		IDfCollection col = DFCHelper.executeQuery(session, query);
		int c=0;
		while(col.next())		
		{
			try
			{
				String object_id=col.getString("r_object_id");
				IDfSysObject document = (IDfSysObject)session.getObject(new DfId(object_id));
				
				String dql_value="select object_name, a_status, owner_name, r_modify_date, r_modifier, ta_name, product_name, category_name,r_version_label, expiration_date from mi_response search document contains '%$value(document_text)%' where  ('$value(title) '=' ' or lower(title) like lower('%$value(title)%')) and ('$value(response_type) '=' ' or response_type='$value(response_type)') and  ('$value(object_name) '=' ' or lower(object_name) like lower('%$value(object_name)%')) "
						+ "and  ('$value(ta_name) '=' ' or any ta_name in ($repeatingvalue(ta_name))) and  (('$value(active_products) '=' ' or any product_name in ($repeatingvalue(active_products))) or apply_to_all_products=1) "
						+ "and  ('$value(inactive_products) '=' ' or any product_name in ($repeatingvalue(inactive_products))) "
						+ "and ('$value(business_unit) '=' ' or any product_name in (select config_product_name from mi_config_product where business_unit='$value(business_unit)')) "
						+ "and ('$value(category_name) '=' ' or any category_name in ($repeatingvalue(category_name))) "
						+ "and ('$value(topic_name) '=' ' or any topic_name in ($repeatingvalue(topic_name))) "
						+ "and ('$value(owner_name) '=' ' or owner_name in ($repeatingvalue(owner_name))) "
						+ "and ('$value(a_status) '=' ' or a_status = '$value(a_status)') "
						+ "and (r_creation_date >=date('$value(search_start_date)','MM/dd/yyyy')) "
						+ "and (r_creation_date <=dateadd(day,1,date('$value(search_end_date)','MM/dd/yyyy'))) "
						+ "and ('$value(active) '=' ' or active = '$value(active)') "
						+ "and ('$value(legacy_migrated) '=' ' or '$value(legacy_migrated)' = '0' or legacy_migrated = '$value(legacy_migrated)') order by r_object_id desc  ENABLE (RETURN_TOP 350)";
				String objName= document.getString("object_name");
				//String version = session.apiGet("get", object_id + ",r_version_label");
				//String version=document.getString("r_version_label");
					System.out.println("document name "+objName);
					document.setString("dql_value",dql_value);
					System.out.println("Approving the document "+objName+" Count "+c+" Product "+document.getString("dql_value"));
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

