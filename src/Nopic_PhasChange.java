import Test_Extract.DFCHelper;

import com.documentum.fc.client.IDfCollection;
import com.documentum.fc.client.IDfSession;
import com.documentum.fc.client.IDfSysObject;
import com.documentum.fc.common.DfException;
import com.documentum.fc.common.DfId;
import com.documentum.fc.common.DfTime;

public class Nopic_PhasChange {
	public static void main(String args[]) throws DfException
	{
		IDfSession session=null;
		DFCHelper dfcHelper = new DFCHelper();
		String docbaseLoginId = "dmadmin-p";
		String docbasePassword = "merlin";
		String docbaseName = "midocsp";
		System.out.println("ReferenceUtil: main: enter");
		session = dfcHelper.createSession(docbaseLoginId, docbasePassword, docbaseName);
		String query="SELECT r_object_id FROM mi_reference WHERE reference_type = 14 and pl_document_phase='Registration'";
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
					System.out.println("plDocument phase "+document.getString("pl_document_phase"));
					String phase = document.getString("pl_document_phase");
					makeMutable(document);
					if(phase.equals("Phase I")){
						System.out.println("plDocument phase: Mutable : "+document.getString("pl_document_phase"));
						document.setString("pl_document_phase","In Registration");
						document.save();
					}

					makeImmutable(document);
					/*makeMutable(document);
					System.out.println("document name "+objName);
					System.out.println("Inactivating the document "+objName+" Count "+c);
					document.setInt("active", 1);
					document.save();
					makeImmutable(document);
					System.out.println("document saved");*/

				}else{
					//REF-BCZ-PL057195
					// Phase I --> Early Clinical Trial
					// Change Phase II to Early Clinical Trial
					// Change Phase III to  Registration Trial (Phase III/Pivotal)
					// Change Registration to In Registration
					String phase = document.getString("pl_document_phase");
					System.out.println("document name "+objName);
					if(phase.equals("Phase I")){
						System.out.println("plDocument phase: Mutable : "+document.getString("pl_document_phase"));
						document.setString("pl_document_phase","In Registration");
						document.save();
					}
					/*document.save();
					System.out.println("document saved");*/
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

