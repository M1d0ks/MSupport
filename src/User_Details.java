import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import Test_Extract.DFCHelper;

import com.documentum.fc.client.DfQuery;
import com.documentum.fc.client.IDfCollection;
import com.documentum.fc.client.IDfQuery;
import com.documentum.fc.client.IDfSession;
import com.documentum.fc.client.IDfSysObject;
import com.documentum.fc.common.DfException;
import com.documentum.fc.common.DfId;


public class User_Details {
	public static void main(String args[]) throws DfException, IOException
	{
		Workbook workbook = new SXSSFWorkbook(1000);
		String sDirectoryPath ="C:/Geodart/";
		FileOutputStream fileOut = new FileOutputStream(sDirectoryPath + "UserList_07-11-2023_Prod.xlsx");

		DFCHelper dfcHelper = new DFCHelper();
		String docbaseLoginId = "midocsp";
		String docbasePassword = "m1d0csp74";
		String docbaseName = "midocs_prod";
		System.out.println("ReferenceUtil: main: enter");
		IDfSession dfSession = dfcHelper.createSession(docbaseLoginId, docbasePassword, docbaseName);

		SXSSFSheet sheet1 = (SXSSFSheet)workbook.createSheet("Document Information");
		ArrayList<String> authors=getAuthorList(dfSession); 
		ArrayList<String> reviewers=getReviewerList(dfSession); 
		ArrayList<String> approvers=getApproverList(dfSession); 
		ArrayList<String> appAdmins=getAppAdmins(dfSession);
		ArrayList<String> guests=getGuests(dfSession); 

		//String resQuery = "select r_object_id,user_name,user_address,description,r_modify_date,last_login_utc_time,client_capability from dm_user where user_state='0' and user_name not like '%grp_wf%'";
		String resQuery = "SELECT r_object_id, user_name, user_os_name, user_address, client_capability FROM dm_user WHERE user_state = 0 AND r_is_group = 0";
		
		//String resQuery = "select r_object_id,user_name,user_address,description,r_modify_date,last_login_utc_time from dm_user where user_name in (select approved_by from mi_response(all) where approved_date > DATE('12/31/2018','MM/DD/YYYY') and approved_date < DATE('01/01/2020','MM/DD/YYYY') and response_type='1' )";
		IDfCollection resColl = DFCHelper.executeQuery(dfSession, resQuery);

		int row_count=0;
		while(resColl.next())
		{
			try{
				String object_id = resColl.getString("r_object_id");
				String userid=resColl.getString("user_name");
				String eMail=resColl.getString("user_os_name");
				String userName=resColl.getString("user_address");
				int clientC= resColl.getInt("client_capability");
				String userType="";
				//String creationDate=resColl.getString("r_modify_date");
				//String lastLogingDate=resColl.getString("last_login_utc_time");
				String appRole="End User";
				String clientCapability = "";
				if(clientC==1){
					clientCapability = "Consumer";
				}else{
					clientCapability = "Contributor";
				}
				if(guests.contains(userid))
				{
					userType="Guest";
					clientCapability = "Consumer";
					//userType="End User";
				}
				if(authors.contains(userid))
				{
					userType="Author";
					//userType="End User";
				}
				if(reviewers.contains(userid))
				{
					//userType="Reviewer";
					userType="End User";
				}
				if(approvers.contains(userid))
				{
					userType="Approver";
					//userType="Privileged";
				}
				if(appAdmins.contains(userid))
				{
					//userType="App Admin";
					userType="Privileged";
					appRole="Business Admin";
				}
				//if(onc.contains(docprods[i]))
				//{
				
				System.out.println("User id "+userid+" UserName "+userName+" email "+eMail);
				Row row = sheet1.createRow(row_count);
				row_count++;

				Cell dataCell = row.createCell(0);
				dataCell.setCellValue(userid);

				Cell dataCell1 = row.createCell(1);
				dataCell1.setCellValue(userName);

				Cell dataCell2 = row.createCell(2);
				dataCell2.setCellValue(eMail);

				Cell dataCell3 = row.createCell(3);
				dataCell3.setCellValue(userType);

				Cell dataCell4 = row.createCell(4);
				dataCell4.setCellValue(appRole);

				//Cell dataCell5 = row.createCell(5);
				//dataCell5.setCellValue(creationDate);
				
				//Cell dataCell6 = row.createCell(6);
				///dataCell6.setCellValue(lastLogingDate);
				
				Cell dataCell7 = row.createCell(7);
				dataCell7.setCellValue(clientCapability);
				
				
			}catch(Exception e){
				System.out.println(e);
			}

		}
		dfcHelper.closeCollection(resColl);
		workbook.write(fileOut);
		fileOut.close();
	}
	private static ArrayList<String> getGuests(IDfSession dfSession) throws DfException {
		String resQuery = "select users_names,group_name from dm_group where any users_names in "
				+ "(Select user_name from dm_user where user_state='0') and group_name like '%guest%' ";
		IDfCollection resColl = DFCHelper.executeQuery(dfSession, resQuery);
		ArrayList<String> authors=new ArrayList<String>();
		while(resColl.next())
		{
			String userid=resColl.getString("users_names");
			authors.add(userid);
		}
		resColl.close();
		return authors;
	}
	
	private static ArrayList<String> getAppAdmins(IDfSession dfSession) throws DfException {
		String resQuery = "select users_names,group_name from dm_group where any users_names in "
				+ "(Select user_name from dm_user where user_state='0') and group_name like '%app_admin%' ";
		IDfCollection resColl = DFCHelper.executeQuery(dfSession, resQuery);
		ArrayList<String> authors=new ArrayList<String>();
		while(resColl.next())
		{
			String userid=resColl.getString("users_names");
			authors.add(userid);
		}
		resColl.close();
		return authors;
	}
	private static ArrayList<String> getApproverList(IDfSession dfSession) throws DfException {
		String resQuery = "select users_names,group_name from dm_group where any users_names in "
				+ "(Select user_name from dm_user where user_state='0') "
				+ "and (group_name like '%onc_approvers%' or  group_name like '%gen_approvers%') ";
		IDfCollection resColl = DFCHelper.executeQuery(dfSession, resQuery);
		ArrayList<String> authors=new ArrayList<String>();
		while(resColl.next())
		{
			String userid=resColl.getString("users_names");
			authors.add(userid);
		}
		resColl.close();
		return authors;
	}
	private static ArrayList<String> getReviewerList(IDfSession dfSession) throws DfException {
		String resQuery = "select users_names,group_name from dm_group where any users_names in "
				+ "(Select user_name from dm_user where user_state='0') and group_name like '%reviewers%' ";
		IDfCollection resColl = DFCHelper.executeQuery(dfSession, resQuery);
		ArrayList<String> authors=new ArrayList<String>();
		while(resColl.next())
		{
			String userid=resColl.getString("users_names");
			authors.add(userid);
		}
		resColl.close();
		return authors;
	}

	private static ArrayList<String> getAuthorList(IDfSession dfSession) throws DfException {

		String resQuery = "select users_names,group_name from dm_group where any users_names in "
				+ "(Select user_name from dm_user where user_state='0') and group_name like '%authors%' ";
		IDfCollection resColl = DFCHelper.executeQuery(dfSession, resQuery);
		ArrayList<String> authors=new ArrayList<String>();
		while(resColl.next())
		{
			String userid=resColl.getString("users_names");
			authors.add(userid);
		}
		resColl.close();
		return authors;
	}
}
