package QA_Refresh;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import Test_Extract.DFCHelper;

import com.documentum.fc.client.DfQuery;
import com.documentum.fc.client.IDfCollection;
import com.documentum.fc.client.IDfQuery;
import com.documentum.fc.client.IDfSession;
import com.documentum.fc.client.IDfSysObject;
import com.documentum.fc.common.DfException;
import com.documentum.fc.common.DfId;
import com.documentum.fc.common.IDfId;

public class MidocMidusInt {
	public static void main(String args[]){
	try {
		DFCHelper dfcHelper = new DFCHelper();
		String docbaseLoginId = "midocsq";
		String docbasePassword = "m1d0csp25"; 
		String docbaseName = "midocs_qa";
		System.out.println("ReferenceUtil: main: enter");
		IDfSession dfSession = dfcHelper.createSession(docbaseLoginId, docbasePassword, docbaseName);

//		System.out.println("createDocument: objectType " + objectType);
//		System.out.println("createDocument: objectName " + objectName);
//		System.out.println("createDocument: title " + title);
//		System.out.println("createDocument: contentType " + contentType);
//		System.out.println("createDocument: fulfillmentApplicationName " + fulfillmentApplicationName);
//		System.out.println("createDocument: aclName " + aclName);
//		System.out.println("createDocument: aclDomain " + aclDomain);
//		
//		System.out.println("createDocument: contentPath " + contentPath);
		
////	11:49:01,444  INFO [DFS] com.novartis.documentum.services.create.DocumentService - createDocument: objectType mcfs_cover_letter
//		11:49:01,445  INFO [DFS] com.novartis.documentum.services.create.DocumentService - createDocument: objectName Cover Letter for Inquiry - I-04919750 (WHITE MAIL)
//		11:49:01,447  INFO [DFS] com.novartis.documentum.services.create.DocumentService - createDocument: title Cover Letter for Inquiry - I-04919750 (WHITE MAIL)
//		11:49:01,447  INFO [DFS] com.novartis.documentum.services.create.DocumentService - createDocument: contentType msw8
//		11:49:01,448  INFO [DFS] com.novartis.documentum.services.create.DocumentService - createDocument: fulfillmentApplicationName MIDUS
//		11:49:01,449  INFO [DFS] com.novartis.documentum.services.create.DocumentService - createDocument: aclName mcfs_document_acl
//		11:49:01,450  INFO [DFS] com.novartis.documentum.services.create.DocumentService - createDocument: aclDomain Merlin
//		11:49:01,451  INFO [DFS] com.novartis.documentum.services.create.DocumentService - createDocument: contentPath C:\Program Files (x86)\Apache Software Foundation\tomcat 9.0.22\webapps\DmJsonServices\data\95637e8ecafd4dfba815be11c7a42c901682351340879.txt
//		11:49:01,503  INFO [DFS] com.novartis.documentum.services.create.DocumentService - createDocument: content type set 
//		11:49:01,526  INFO [DFS] com.novartis.documentum.services.create.DocumentService - createDocument: content path set 
//		11:49:01,528  INFO [DFS] com.novartis.documentum.services.create.DocumentService - createDocument: acl name set set 
//		11:49:01,551  INFO [DFS] com.novartis.documentum.services.create.DocumentService - createDocument: linked to  /Response Letters/MIDUS

		
		String objectType="mcfs_cover_letter";
		String objectName="Cover Letter for Inquiry - I-04919750 (WHITE MAIL)";
		String title="Cover Letter for Inquiry - I-04919750 (WHITE MAIL)";
		String contentType="msw8";
		String fulfillmentApplicationName="MIDUS";
		String aclName="mcfs_document_acl";
		String aclDomain="Merlin";
		
		System.out.println("Starting document creation ");
		IDfSysObject dfSysObject = (IDfSysObject)dfSession.newObject("mcfs_cover_letter");
		dfSysObject.setObjectName(objectName);
		dfSysObject.setString("title", title);
		dfSysObject.setContentType(contentType);
		System.out.println("createDocument: content type set ");
		//dfSysObject.setFile(contentPath);
		System.out.println("createDocument: content path set ");
		dfSysObject.setACLName(aclName);
		dfSysObject.setACLDomain(aclDomain);
		System.out.println("createDocument: acl name set set ");
		dfSysObject.link("/Response Letters/" + fulfillmentApplicationName);
		System.out.println("createDocument: linked to  /Response Letters/" + fulfillmentApplicationName );

		
		if ((objectType.equals("mcfs_response_letter")) || (objectType.equals("mcfs_cover_letter")) || 
				(objectType.equals("mcfs_attachment"))) {
			dfSysObject.setString("case_id", "Test");
			dfSysObject.setString("case_number", "Test");
			dfSysObject.setString("inquiry_ids", "Test");
			dfSysObject.setString("inquiry_numbers", "Test");
			dfSysObject.setString("fulfillment_id", "Test");
			dfSysObject.setString("application_name", "Test");
			
			if ((objectType.equals("mcfs_attachment"))) {
				dfSysObject.setString("uploaded_by", "Jaswanth");				
				
			}
		}

		dfSysObject.save();
		System.out.println("createDocument: saved sys object ");
		
	}
	catch (Exception ex) {
		System.out.println(ex);
	}
	}
}
