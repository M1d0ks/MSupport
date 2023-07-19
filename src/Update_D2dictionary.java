

	import Test_Extract.DFCHelper;

	import com.documentum.fc.client.IDfCollection;
	import com.documentum.fc.client.IDfSession;
	import com.documentum.fc.client.IDfSysObject;
	import com.documentum.fc.common.DfException;
	import com.documentum.fc.common.DfId;

	public class Update_D2dictionary {

		public static void main(String args[]) throws DfException
		{
			IDfSession session=null;
			DFCHelper dfcHelper = new DFCHelper();
			String docbaseLoginId = "dmadmin-np";
			String docbasePassword = "merlin";
			String docbaseName = "midocsq";
			System.out.println("ReferenceUtil: main: enter");
			session = dfcHelper.createSession(docbaseLoginId, docbasePassword, docbaseName);
			
			String query ="select r_object_id,alias_value from d2_dictionary_value where r_object_id='0002b71f800a77e8'";
			System.out.println("Query "+query);
			IDfCollection col = DFCHelper.executeQuery(session, query);
			int c=0;
			while(col.next())
			{
			try{	
				String alias = col.getAllRepeatingStrings("alias_value", ",");
				String[] atts = alias.split(",");
				IDfSysObject document = (IDfSysObject)session.getObject(new DfId(col.getString("r_object_id")));
				for(int i=0;i<atts.length;i++)
				{
					if(atts[i]=="Other")
					{
						atts[i]="Pipeline";
					}
				}
				System.out.println("Alias "+alias);
					}
				catch(Exception e)
				{
					System.out.println("Error "+e);
				}
		}

			col.close();
}
}