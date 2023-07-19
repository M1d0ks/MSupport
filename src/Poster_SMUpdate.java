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


public class Poster_SMUpdate {
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
	
	
	String resQuery="select r_object_id, congress_title, congress_startdate, congress_enddate, reference_publish_city, reference_publish_state from mi_reference where congress_title in ('Academy of Managed Care Pharmacy','American Academy of Allergy, Asthma and Immunology (AAAAI)','American Academy of Dermatology (AAD)','American Academy of Neurology (AAN)','American Academy of Ophthalmology','American Association for Cancer Research (AACR)','American Association for the Study of Liver Diseases (AASLD) Liver Meeting','American Association of Clinical Endocrinologists (AACE)','American Association of Neuroscience Nurses (AANN) Annual Educational Meeting','American College of Allergy, Asthma and Immunology (ACAAI)','American College of Cardiology','American College of Clinical Pharmacy (ACCP)','American College of Rheumatology/Association of Rheumatology Health Professionals (ACR/ARHP)','American Epilepsy Society (AES)','American Headache Society Annual Scientific Meeting','American Heart Association (AHA) Scientific Sessions','American Nephrology Nurses Association (ANNA)','American Neurological Association (ANA)','American Psychiatric Association  ','American Society of Clinical Oncology (ASCO)','American Society of Hematology (ASH)','American Society of Nephrology (ASN)','American Society of Pediatric Hematology/Oncology (ASPHO) Annual Meeting','American Thoracic Society International Conference','American Transplant Congress (ATC)','Americas Committee for Treatment & Research in Multiple Sclerosis (ACTRIMS)','Annual European Association of Urology Congress','Annual Meeting of the American Headache Society','ASCO Breast Cancer Symposium','ASCO Gastrointestinal Cancers Symposium','ASCO-SITC Clinical Immuno-Oncology Symposium','Asia-Pacific Academy of Ophthalmology Congress','CHEST- Annual Meeting of the American College of Chest Physicians','Chicago Multidisciplinary Symposium in Thoracic Oncology','Child Neurology Society Annual Meeting','Congress of the International Headache Society','Connective Tissue Oncology Society (CTOS)','Consortium of Multiple Sclerosis Centers (CMSC) Annual Meeting','Cooperative Meeting of the CMSC and ACTRIMS','Diamond Headache Clinic Research and Educational Foundation','EAACI-WAO World Allergy and Asthma Congress','Eastern Asia Dermatology Congress (EADC)','Endocrine Nurses Society','Endocrine Society Annual Meeting (ENDO)','EORTC-NCI-AACR Molecular Targets and Cancer Therapeutics Symposium','ESMO Symposium on Immuno-Oncology','EULAR Annual European Congress of Rheumatology','European Academy of Dermatology and Venereology Congress (EADV)','European Academy of Neurology','European Atherosclerotic Society','European Breast Cancer Conference (EBCC)','European Cancer Congress','European Committee for the Treatment and Research in Multiple Sclerosis (ECTRIMS)','European Congress of Endocrinology (ECE)','European Congress on Endometriosis (EEL)','European Group for Blood and Marrow Transplantation (EBMT)','European Headache and Migraine Trust International Congress','European Headache Federation','European Hematology Association (EHA)','European Lung Cancer Conference (ELCC)','European Multidisciplinary Cancer Congress (ECCO ESMO ESTRO)','European Neuroendocrine Association (ENEA)','European Neuroendocrine Tumor Society (ENETS)','European Neurological Society (ENS)','European Pediatric Neurology Society (EPNS) Congress','European Respiratory Society (ERS)','European Society for Dermatological Research (ESDR)','European Society for Medical Oncology - Asia (ESMO - Asia)','European Society for Medical Oncology (ESMO)','European Society for Organ Transplantation (ESOT)','European Society for Pediatric Dermatology (ESPD)','European Society of Cardiology','European Society of Cardiology- Heart Failure','Fall Clinical Dermatology Conference','Federation of Clinical Immunology Societies (FOCiS) Annual Meeting','Foundation for Sickle Cell Disease Research','German Society of Hematooncology','Heart Failure Society of America (HFSA)','Hematology Oncology Pharmacy Association Annual Conference (HOPA)','International BioIron Society','International Conference on Alzheimers and Parkinsons Diseases (AD-PD)','International Conference on Malignant Lymphoma (ICML)','International Epilepsy Congress (IEC)','International Federation of Psoriasis Associations (IFPA) World Conference','International Kidney Cancer Symposium','International Liver Transplantation Society','International Pituitary Congress','International Society For Pharmacoeconomics and Outcomes Research (ISPOR)','International TSC Research Conference','Joint ACTRIMS-ECTRIMS Meeting','LAM Foundation International Research Conference','Maui Derm for Dermatologists','Miami Breast Cancer Conference (MBCC)','North American Cystic Fibrosis Conference (NACFC)','North American Neuroendocrine Tumor Society (NANETS)','Oncology Nursing Society (ONS)','Pan-American League of Associations for Rheumatology','Pediatric Rheumatology Symposium','Perspectives in Rheumatic Diseases (PRD)','Population Approach Group in Europe (PAGE)','Psoriasis','Psoriasis: from Gene to Clinic International Congress','Pulmonary Hypertension Association’s PH Professional Network Symposium (PHA PHPN Symposium)','Rheumatology Winter Clinical Symposium (RWCS)','San Antonio Breast Cancer Symposium (SABCS)','Society for Immunotherapy of Cancer (SITC)','Society for Investigative Dermatology (SID)','Society for Melanoma Research (SMR)','Society for Neuro-Oncology (SNO)','Society of Toxicology','St. Gallen International Breast Cancer Conference','Symposium on Hidradenitis Suppurativa Advances (SHSA)','The Association for Research in Vision and Ophthalmology (ARVO)','Transplantation and Cellular Therapy Meetings','United European Gastroenterology Week','Western Society of Allergy, Asthma and Immunology (WSAAI)','Winter Clinical Dermatology Conference','World Conference on Lung Cancer (WCLC)','World Congress of Dermatology','World Congress on Gastrointestinal Cancer (WCGI)','World Preclinical Congress - WPC','World Psoriasis and Psoriatic Arthritis Conference','World Transplant Congress (WTC)','World TSC Conference (TSC)')"
			+ " and congress_startdate > DATE('01/01/2018','MM/DD/YYYY') and reference_type='10'"; 
	//NewVersion String resQuery="select r_object_id,object_name,r_version_label,r_creation_date from mi_response(all) where response_type='1' and r_creation_date<DATE('08/01/2020','MM/DD/YYYY') and r_creation_date >DATE('06/30/2020','MM/DD/YYYY') and any ta_name like '%Oncology%'";
	//Approved String resQuery="select r_object_id,object_name,r_version_label from mi_response(all) where response_type='1' and approved_date<DATE('08/01/2020','MM/DD/YYYY') and approved_date>DATE('06/30/2020','MM/DD/YYYY') and any ta_name like '%Oncology%'";
	//Reviewed String resQuery="select r_object_id,object_name,r_version_label from mi_response(all) where response_type='1' and last_reviewed_date<DATE('08/01/2020','MM/DD/YYYY') and last_reviewed_date>DATE('06/30/2020','MM/DD/YYYY') and any ta_name like '%Oncology%' and a_status='Peer Reviewed'";
	
	
	IDfCollection resColl = DFCHelper.executeQuery(dfSession, resQuery);

	//ArrayList responseInfo = dfcHelper.getArrayListFromDMColl(resColl);
	//System.out.println("Query executed "+responseInfo.size());
	//DfcHelper.closeCollection(resColl);
	int row_count=0;
	//HashMap userNames = getFullName(dfSession);
	while(resColl.next())
	{
		
		//HashMap hmResponse = (HashMap)responseInfo.get(i);
		String object_id = resColl.getString("r_object_id");
		IDfSysObject document = (IDfSysObject)dfSession.getObject(new DfId(object_id));
		String congressName = document.getString("congress_title");
		String startDate = document.getString("congress_startdate");
		 
		String[] tempyear = startDate.split(" ");
		String[] year = tempyear[0].split("/");
		try{
		if(!document.isImmutable()){
		String scientificMeeting = congressName + " - " + year[2];
		document.setString("scientific_meeting", scientificMeeting);
		System.out.println("Year -- "+scientificMeeting);
	    document.save();
		}else{
			String scientificMeeting = congressName + " - " + year[2];
			document.setString("scientific_meeting", scientificMeeting);
			System.out.println("Year -- "+scientificMeeting);
		    document.save();
		}
		}catch(Exception ex){
			System.out.println(ex);
		}
}
}
}