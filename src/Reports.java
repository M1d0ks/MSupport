import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.poi.common.usermodel.Hyperlink;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import Test_Extract.DFCHelper;

import com.documentum.fc.client.DfQuery;
import com.documentum.fc.client.IDfCollection;
import com.documentum.fc.client.IDfQuery;
import com.documentum.fc.client.IDfSession;
import com.documentum.fc.client.IDfSysObject;
import com.documentum.fc.common.DfException;
import com.documentum.fc.common.DfId;


public class Reports {
public static void main(String args[]) throws DfException, IOException
{
	Workbook workbook = new XSSFWorkbook();
	CreationHelper createHelper = workbook.getCreationHelper();
	
	DFCHelper dfcHelper = new DFCHelper();
	String docbaseLoginId = "dmadmin-p";
	String docbasePassword = "merlin";
	String docbaseName = "midocsp";
	System.out.println("ReferenceUtil: main: enter");
	IDfSession dfSession = dfcHelper.createSession(docbaseLoginId, docbasePassword, docbaseName);
	
	//String sDirectoryPathIn ="C:/Geodart/ApprovedRefs_11-20_01-06.xlsx";
	//String sDirectoryPath ="C:/Geodart/ta_info.xlsx";
	//FileInputStream fileIn = new FileInputStream(new File(sDirectoryPathIn));
	//Workbook workbook1 = new XSSFWorkbook(fileIn);
	
	String sDirectoryPathOut ="C:/Geodart/";
	FileOutputStream fileOut = new FileOutputStream(sDirectoryPathOut + "MIDOCS_Website.xlsx");
	
	Sheet datatypeSheet = workbook.createSheet();
    Iterator<Row> iterator = datatypeSheet.iterator();
	int i=0;
    while (iterator.hasNext()) {
    	Row currentRow = iterator.next();
        Iterator<Cell> cellIterator = currentRow.iterator();
        	//System.out.println(" Object_name "+currentRow.getCell(0));
        	i++;
    }
	
	XSSFSheet sheet1 = (XSSFSheet)workbook.createSheet("Document Information");
	//Change Dates
	//NEW 
	//String resQuery="select r_object_id,object_name,business_unit,is_featured,medication_guide,product_description,pr_pilink,is_rwe,pr_genericname,pr_ppilink from mi_config_product where active='1' and externally_visible='1'"; 
	String resQuery ="select r_object_id,object_name,title from mi_response where response_type=1 and a_status='Approved' and response_type=1 and external_access='1'";
	//String resQuery="select r_object_id, congress_title, congress_startdate, congress_enddate, reference_publish_city, reference_publish_state from mi_reference where congress_title in ('Academy of Managed Care Pharmacy','American Academy of Allergy, Asthma and Immunology (AAAAI)','American Academy of Dermatology (AAD)','American Academy of Neurology (AAN)','American Academy of Ophthalmology','American Association for Cancer Research (AACR)','American Association for the Study of Liver Diseases (AASLD) Liver Meeting','American Association of Clinical Endocrinologists (AACE)','American Association of Neuroscience Nurses (AANN) Annual Educational Meeting','American College of Allergy, Asthma and Immunology (ACAAI)','American College of Cardiology','American College of Clinical Pharmacy (ACCP)','American College of Rheumatology/Association of Rheumatology Health Professionals (ACR/ARHP)','American Epilepsy Society (AES)','American Headache Society Annual Scientific Meeting','American Heart Association (AHA) Scientific Sessions','American Nephrology Nurses Association (ANNA)','American Neurological Association (ANA)','American Psychiatric Association  ','American Society of Clinical Oncology (ASCO)','American Society of Hematology (ASH)','American Society of Nephrology (ASN)','American Society of Pediatric Hematology/Oncology (ASPHO) Annual Meeting','American Thoracic Society International Conference','American Transplant Congress (ATC)','Americas Committee for Treatment & Research in Multiple Sclerosis (ACTRIMS)','Annual European Association of Urology Congress','Annual Meeting of the American Headache Society','ASCO Breast Cancer Symposium','ASCO Gastrointestinal Cancers Symposium','ASCO-SITC Clinical Immuno-Oncology Symposium','Asia-Pacific Academy of Ophthalmology Congress','CHEST- Annual Meeting of the American College of Chest Physicians','Chicago Multidisciplinary Symposium in Thoracic Oncology','Child Neurology Society Annual Meeting','Congress of the International Headache Society','Connective Tissue Oncology Society (CTOS)','Consortium of Multiple Sclerosis Centers (CMSC) Annual Meeting','Cooperative Meeting of the CMSC and ACTRIMS','Diamond Headache Clinic Research and Educational Foundation','EAACI-WAO World Allergy and Asthma Congress','Eastern Asia Dermatology Congress (EADC)','Endocrine Nurses Society','Endocrine Society Annual Meeting (ENDO)','EORTC-NCI-AACR Molecular Targets and Cancer Therapeutics Symposium','ESMO Symposium on Immuno-Oncology','EULAR Annual European Congress of Rheumatology','European Academy of Dermatology and Venereology Congress (EADV)','European Academy of Neurology','European Atherosclerotic Society','European Breast Cancer Conference (EBCC)','European Cancer Congress','European Committee for the Treatment and Research in Multiple Sclerosis (ECTRIMS)','European Congress of Endocrinology (ECE)','European Congress on Endometriosis (EEL)','European Group for Blood and Marrow Transplantation (EBMT)','European Headache and Migraine Trust International Congress','European Headache Federation','European Hematology Association (EHA)','European Lung Cancer Conference (ELCC)','European Multidisciplinary Cancer Congress (ECCO ESMO ESTRO)','European Neuroendocrine Association (ENEA)','European Neuroendocrine Tumor Society (ENETS)','European Neurological Society (ENS)','European Pediatric Neurology Society (EPNS) Congress','European Respiratory Society (ERS)','European Society for Dermatological Research (ESDR)','European Society for Medical Oncology - Asia (ESMO - Asia)','European Society for Medical Oncology (ESMO)','European Society for Organ Transplantation (ESOT)','European Society for Pediatric Dermatology (ESPD)','European Society of Cardiology','European Society of Cardiology- Heart Failure','Fall Clinical Dermatology Conference','Federation of Clinical Immunology Societies (FOCiS) Annual Meeting','Foundation for Sickle Cell Disease Research','German Society of Hematooncology','Heart Failure Society of America (HFSA)','Hematology Oncology Pharmacy Association Annual Conference (HOPA)','International BioIron Society','International Conference on Alzheimers and Parkinsons Diseases (AD-PD)','International Conference on Malignant Lymphoma (ICML)','International Epilepsy Congress (IEC)','International Federation of Psoriasis Associations (IFPA) World Conference','International Kidney Cancer Symposium','International Liver Transplantation Society','International Pituitary Congress','International Society For Pharmacoeconomics and Outcomes Research (ISPOR)','International TSC Research Conference','Joint ACTRIMS-ECTRIMS Meeting','LAM Foundation International Research Conference','Maui Derm for Dermatologists','Miami Breast Cancer Conference (MBCC)','North American Cystic Fibrosis Conference (NACFC)','North American Neuroendocrine Tumor Society (NANETS)','Oncology Nursing Society (ONS)','Pan-American League of Associations for Rheumatology','Pediatric Rheumatology Symposium','Perspectives in Rheumatic Diseases (PRD)','Population Approach Group in Europe (PAGE)','Psoriasis','Psoriasis: from Gene to Clinic International Congress','Pulmonary Hypertension Association’s PH Professional Network Symposium (PHA PHPN Symposium)','Rheumatology Winter Clinical Symposium (RWCS)','San Antonio Breast Cancer Symposium (SABCS)','Society for Immunotherapy of Cancer (SITC)','Society for Investigative Dermatology (SID)','Society for Melanoma Research (SMR)','Society for Neuro-Oncology (SNO)','Society of Toxicology','St. Gallen International Breast Cancer Conference','Symposium on Hidradenitis Suppurativa Advances (SHSA)','The Association for Research in Vision and Ophthalmology (ARVO)','Transplantation and Cellular Therapy Meetings','United European Gastroenterology Week','Western Society of Allergy, Asthma and Immunology (WSAAI)','Winter Clinical Dermatology Conference','World Conference on Lung Cancer (WCLC)','World Congress of Dermatology','World Congress on Gastrointestinal Cancer (WCGI)','World Preclinical Congress - WPC','World Psoriasis and Psoriatic Arthritis Conference','World Transplant Congress (WTC)','World TSC Conference (TSC)')"
		//	+ " and congress_startdate > DATE('01/01/2018','MM/DD/YYYY') and reference_type='4' "; 
	//NewVersion String resQuery="select r_object_id,object_name,r_version_label,r_creation_date from mi_response(all) where response_type='1' and r_creation_date<DATE('08/01/2020','MM/DD/YYYY') and r_creation_date >DATE('06/30/2020','MM/DD/YYYY') and any ta_name like '%Oncology%'";
	//Approved String resQuery="select r_object_id,object_name,r_version_label from mi_response(all) where response_type='1' and approved_date<DATE('08/01/2020','MM/DD/YYYY') and approved_date>DATE('06/30/2020','MM/DD/YYYY') and any ta_name like '%Oncology%'";
	//Reviewed String resQuery="select r_object_id,object_name,r_version_label from mi_response(all) where response_type='1' and last_reviewed_date<DATE('08/01/2020','MM/DD/YYYY') and last_reviewed_date>DATE('06/30/2020','MM/DD/YYYY') and any ta_name like '%Oncology%' and a_status='Peer Reviewed'"; 
	//String resQuery= "Select r_object_id, object_name, product_name, external_access,title, scientific_meeting from mi_reference where scientific_meeting like '%2022%'";
	//String resQuery = "select user_name, user_os_name, user_address, client_capability, user_state from dm_user where user_state = 0 and r_is_group = 0";
	IDfCollection resColl = DFCHelper.executeQuery(dfSession, resQuery);

	//ArrayList responseInfo = dfcHelper.getArrayListFromDMColl(resColl);
	//System.out.println("Query executed "+responseInfo.size());
	//DfcHelper.closeCollection(resColl);
	int row_count=0;
	//HashMap userNames = getFullName(dfSession);
	while(resColl.next()) 
	{
		try{
		
		//HashMap hmResponse = (HashMap)responseInfo.get(i);
		String object_id = resColl.getString("r_object_id");
		IDfSysObject document = (IDfSysObject)dfSession.getObject(new DfId(object_id));
		System.out.println("Line "+row_count);
		i++;
		/*String congressName = document.getString("congress_title");
		String startDate = document.getString("congress_startdate");
		String endDate = document.getString("congress_enddate");
		String city = document.getString("reference_publish_city"); 
		String state = document.getString("reference_publish_state"); 
		String refName = document.getString("object_name"); 
		String[] tempyear = startDate.split(" ");
		String[] year = tempyear[0].split("/");*/
	
		String object_name=resColl.getString("object_name");
		//String a_status=resColl.getString("a_status");
		//String products = document.getAllRepeatingStrings("product_name", ",");
		String title = resColl.getString("title");
		//String extAccess = resColl.getString("client_capability");
		//String scientific_meeting = resColl.getString("user_state");
		
		//String count=resColl.getString("time_stamp");
		//String r_modify_date=document.getString("r_modify_date");
		//Products
		/*String business_unit=document.getString("business_unit");
		Boolean isFeatured=document.getBoolean("is_featured");
		String medGuide=document.getString("medication_guide");
		String prodDescription=document.getString("product_description");
		String piLink=document.getString("pr_pilink");
		String ppiLink=document.getString("pr_ppilink");
		Boolean isRwe=document.getBoolean("is_rwe");
		String  genericName = document.getString("pr_genericname");
		String r_modify_date=document.getString("r_modify_date");
		String title=document.getString("title");
		String a_status=document.getString("a_status");
		String r_modifier=document.getString("r_modifier");
		String owner_name=document.getString("owner_name");
		String ta_name=document.getString("ta_name");
		String approved_date=document.getString("approved_date");
		String expired_date = document.getString("expiration_date");
		String category_Name=""; */
		try{
		 //category_Name=document.getString("category_name");
		}catch(Exception e)
		{
			System.out.println(e);
		}
		//String product_name=document.getAllRepeatingStrings("product_name", ",");
		//String ta_name=document.getString("ta_name");
		//String docprods[]=product_name.split(",");
		//String version=document.getString("r_version_label");
		//String expiration_date=resColl.getString("expiration_date");
		//String approvedDate= resColl.getString("approved_date");
		//String rV=document.getVersionLabel(1);
		String created=document.getString("r_creation_date");
		
		//for(int i=0;i<docprods.length;i++)
		//{
	    //String pName=docprods[i];
		//String user_name=resColl.getString("user_name");
		//String userName=document.getString("r_creator_name");
	//	if(userNames.containsKey(userName))
		//{
			//userName=(String) userNames.get(userName);
		//}
		//String version = dfSession.apiGet("get", object_id + ",r_version_label");
		 //System.out.println("Getting Document "+object_name+ " Version "+version+ " Row count "+row_count+" Created by "+userName);
		
		 //if(onc.contains(docprods[i]))
		//{
		XSSFCellStyle hlinkstyle = (XSSFCellStyle) workbook.createCellStyle();
	      XSSFFont hlinkfont = (XSSFFont) workbook.createFont();
	      hlinkfont.setUnderline(XSSFFont.U_SINGLE);
	      hlinkfont.setColor(HSSFColor.BLUE.index);
	      hlinkstyle.setFont(hlinkfont);
	      
		//System.out.println("Writing data of doc "+object_name);
		Row row = sheet1.createRow(row_count);
		row_count++;
	
			Cell dataCell = row.createCell(0);
			dataCell.setCellValue(object_id);
			
			Cell dataCell1 = row.createCell(1);
			dataCell1.setCellValue(object_id);
			
			Cell dataCell2 = row.createCell(2);
			dataCell2.setCellValue(title);
			
			Cell dataCell3 = row.createCell(3);
			dataCell3.setCellValue(created);
			
			//Cell dataCell4 = row.createCell(4);
			//dataCell4.setCellValue(ta_name);
			
			//Cell dataCell5 = row.createCell(5);
			//dataCell5.setCellValue(approvedDate);
			
			//Cell dataCell6 = row.createCell(6);
			//dataCell6.setCellValue(expiration_date);
			
			/*
			Cell dataCell5 = row.createCell(5);
			dataCell5.setCellValue(scientific_meeting);*/
			
			/*Cell dataCell6 = row.createCell(6);
			dataCell6.setCellValue(refName);*/
			
		    
			
		/*	Cell dataCell1 = row.createCell(1);
			dataCell1.setCellValue(business_unit);
			
			Cell dataCell2 = row.createCell(2);
			dataCell2.setCellValue(isFeatured);
			
			Cell dataCell3 = row.createCell(3);
			dataCell3.setCellValue(medGuide);
			
			Cell dataCell4 = row.createCell(4);
			dataCell4.setCellValue(prodDescription);
			
			Cell dataCell5 = row.createCell(5);
			dataCell5.setCellValue(piLink);
			
			Cell dataCell6 = row.createCell(6);
			dataCell6.setCellValue(ppiLink);
			
			Cell dataCell8 = row.createCell(8);
			dataCell8.setCellValue(isRwe);
			
			Cell dataCell9 = row.createCell(9);
			dataCell9.setCellValue(genericName); 
			
			Cell dataCell10 = row.createCell(10);
			dataCell10.setCellValue(r_modifier);
			
			Cell dataCell11 = row.createCell(11);
			dataCell11.setCellValue(expired_date); 
			
			Cell dataCell12 = row.createCell(12);
			dataCell12.setCellValue(approved_date);
			
			Cell dataCell13 = row.createCell(13);
			dataCell13.setCellValue(object_id); 
			/*
			Cell dataCell5 = row.createCell(5);
			dataCell5.setCellValue("File Link");
			
			org.apache.poi.ss.usermodel.Hyperlink link = createHelper.createHyperlink(Hyperlink.LINK_FILE);
            link.setAddress(path);
            dataCell5.setHyperlink(link);
            dataCell5.setCellStyle(hlinkstyle); */
			
		//}
	//	}
	}catch(Exception e){
	System.out.println(e);
	}
		
}
	dfcHelper.closeCollection(resColl);
	workbook.write(fileOut);
	fileOut.close();
}
private static ArrayList getOncProduct(IDfSession dfSession) throws DfException {
	String resQuery = "select config_product_name from mi_config_product where business_unit='Oncology'";
	IDfCollection resColl = DFCHelper.executeQuery(dfSession, resQuery);
	ArrayList oncs=new ArrayList();
	while(resColl.next())
	{
		String product_name=resColl.getString("config_product_name");
		oncs.add(product_name);
	}
	return oncs;
}
private static HashMap getFullName(IDfSession dfSession2)
		throws DfException
{
	HashMap usernames = new HashMap();
	String resQuery = "select user_name,description from dm_user";
	IDfQuery query = new DfQuery();
	System.out.println("Query");
	query.setDQL(resQuery);
	IDfCollection coll = query.execute(dfSession2, 1);
	while (coll.next())
	{
		String userid = coll.getString("user_name");
		String userName = coll.getString("description");
		usernames.put(userid, userName);
	}
	coll.close();
	System.out.println(usernames);
	return usernames;
}
}
