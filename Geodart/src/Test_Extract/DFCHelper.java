package Test_Extract;

import com.documentum.com.DfClientX;

import com.documentum.com.IDfClientX;
import com.documentum.fc.client.IDfACL;
import com.documentum.fc.client.IDfClient;
import com.documentum.fc.client.IDfCollection;
import com.documentum.fc.client.IDfGroup;
import com.documentum.fc.client.IDfPersistentObject;
import com.documentum.fc.client.IDfQuery;
import com.documentum.fc.client.IDfSession;
import com.documentum.fc.client.IDfSessionManager;
import com.documentum.fc.client.IDfSysObject;
import com.documentum.fc.client.IDfTypedObject;
import com.documentum.fc.client.IDfUser;
import com.documentum.fc.common.DfException;
import com.documentum.fc.common.DfId;
import com.documentum.fc.common.IDfAttr;
import com.documentum.fc.common.IDfId;
import com.documentum.fc.common.IDfLoginInfo;
import com.documentum.fc.common.IDfTime;

import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class DFCHelper
{
  public static final int DELETE_PERMIT = 7;
  public static final int WRITE_PERMIT = 6;
  public static final int VERSION_PERMIT = 5;
  public static final int RELATE_PERMIT = 4;
  public static final int READ_PERMIT = 3;
  public static final int BROWSE_PERMIT = 2;
  public static final int NONE_PERMIT = 1;
  
  public static IDfCollection executeQuery(IDfSession dfSession, String sDql)
    throws DfException
  {
    IDfCollection dfColl = null;
    try
    {
      IDfClientX dfClientX = new DfClientX();
      IDfQuery dfQuery = dfClientX.getQuery();
      dfQuery.setDQL(sDql);
      dfColl =dfQuery.execute(dfSession,IDfQuery.DF_EXEC_QUERY);   
  
    }
    catch (DfException ex)
    {
      String sErrMsg = "DFCHelper: executeQuery: Error encountered while executing query. \n" + sDql + "\n" + ex.getMessage() + "\n";
      throw new DfException(sErrMsg);
    }
    return dfColl;
  }
  
  public void closeCollection(IDfCollection dfColl)
  {
    try
    {
      if (dfColl != null) {
        dfColl.close();
      }
    }
    catch (DfException localDfException) {}
  }
  
  public ArrayList getArrayListFromDMColl(IDfCollection dfColl)
    throws DfException
  {
    ArrayList aResultList = new ArrayList();
    //HashMap hmResultSet;
    int count=0;
    try
    {
      if (dfColl != null)
      {
        while(dfColl.next())
        {
          HashMap hmResultSet = new HashMap();
          IDfTypedObject dfTypedObject = dfColl.getTypedObject();
          for (int i = 0; i < dfTypedObject.getAttrCount(); i++)
          {
            IDfAttr dfAttribute = dfTypedObject.getAttr(i);
            String sAttrName = dfAttribute.getName();
           
           //aResultList. System.out.println("Atribute "+hmResultSet);
            hmResultSet.put(sAttrName, dfTypedObject.getString(sAttrName));
            }
          aResultList.add(hmResultSet);
         // System.out.println("Count "+count);
          count++;
        }
      }
    }
    catch (DfException ex)
    {
      String sErrMsg = "DFCHelper: getArrayListFromDMColl: Error encountered while populating HashMap from query resultset. \n" + ex.getMessage() + "\n";
      throw new DfException(sErrMsg);
    }
    return aResultList;
  }
  
  public String checkNull(String sVal)
  {
    if (sVal == null) {
      return "";
    }
    return sVal;
  }
  
  public String getGroupNameById(IDfSession dfSession, String groupObjectId)
    throws DfException
  {
    String groupName = "";
    try
    {
      IDfGroup dfGroup = (IDfGroup)dfSession.getObject(new DfId(groupObjectId));
      groupName = dfGroup.getGroupName();
    }
    catch (DfException ex)
    {
      String sErrMsg = "DFCHelper: getGroupNameById: Error encountered while retrieving group name for object " + groupObjectId + ".\n" + ex.getMessage() + "\n";
      throw new DfException(sErrMsg);
    }
    return groupName;
  }
  
  public String getGroupIdByName(IDfSession dfSession, String groupName)
    throws DfException
  {
    String groupId = "";
    try
    {
      IDfGroup dfGroup = (IDfGroup)dfSession.getObjectByQualification("dm_group where group_name = '" + groupName + "'");
      groupId = dfGroup.getString("r_object_id");
    }
    catch (DfException ex)
    {
      String sErrMsg = "DFCHelper: getGroupNameById: Error encountered while retrieving group name for object " + groupName + ".\n" + ex.getMessage() + "\n";
      throw new DfException(sErrMsg);
    }
    return groupId;
  }
  
  public String getRepeatingAttributeValueUsingDelimiter(IDfSession dfSession, IDfPersistentObject dfPersistentObject, String attrName, String delimiter)
    throws DfException
  {
    String sRepeatingAttrValues = "";
    try
    {
      for (int i = 0; i < dfPersistentObject.getValueCount(attrName); i++)
      {
        String sAttrValue = dfPersistentObject.getRepeatingString(attrName, i);
        sRepeatingAttrValues = sRepeatingAttrValues + sAttrValue + delimiter;
      }
    }
    catch (DfException ex)
    {
      String sErrMsg = "DFCHelper: getRepeatingAttributeValueUsingDelimiter: Error encountered while fetching repeating attribute values for " + attrName + ". \n" + ex.getMessage() + "\n";
      throw new DfException(sErrMsg);
    }
    return sRepeatingAttrValues;
  }
  
  public boolean isCurrentUserDocbaseOwner(IDfSession dfSession)
    throws DfException
  {
    boolean bRetVal = false;
    try
    {
      String sDocbaseOwner = dfSession.getDocbaseOwnerName();
      String sCurrentUser = dfSession.getLoginUserName();
      if (sCurrentUser.equals(sDocbaseOwner)) {
        bRetVal = true;
      }
    }
    catch (DfException ex)
    {
      String sErrMsg = "DFCHelper: isCurrentUserDocbaseOwner: Error encountered while checking if the current user is the docbase owner.\n" + ex.getMessage() + "\n";
      throw new DfException(sErrMsg);
    }
    return bRetVal;
  }
  
  public String getCurrentDateAsString(String dateFormat)
  {
    SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
    String sDateString = sdf.format(new Date());
    return sDateString;
  }
  
  public String getObjectNameById(IDfSession dfSession, String objectId)
    throws DfException
  {
    String sObjectName = "";
    try
    {
      IDfPersistentObject dfPersistentObject = dfSession.getObject(new DfId(objectId));
      sObjectName = dfPersistentObject.getString("object_name");
    }
    catch (DfException ex)
    {
      String sErrMsg = "DFCHelper: getObjectNameById: Error encountered while retrieving object name for object with ID " + objectId + ".\n" + ex.getMessage() + "\n";
      throw new DfException(sErrMsg);
    }
    return sObjectName;
  }
  
  public IDfSysObject getObjectById(IDfSession dfSession, String objectId)
    throws DfException
  {
    IDfSysObject dfSysObject = null;
    try
    {
      dfSysObject = (IDfSysObject)dfSession.getObject(new DfId(objectId));
    }
    catch (DfException ex)
    {
      String sErrMsg = "DFCHelper: getObjectById: Error retrieving object by id  " + objectId + ". \n" + ex.getMessage() + "\n";
      throw new DfException(sErrMsg);
    }
    return dfSysObject;
  }
  
  public IDfPersistentObject getPersistentObjectById(IDfSession dfSession, String objectId)
    throws DfException
  {
    IDfPersistentObject dfSysObject = null;
    try
    {
      dfSysObject = (IDfSysObject)dfSession.getObject(new DfId(objectId));
    }
    catch (DfException ex)
    {
      String sErrMsg = "DFCHelper: getObjectById: Error retrieving object by id  " + objectId + ". \n" + ex.getMessage() + "\n";
      throw new DfException(sErrMsg);
    }
    return dfSysObject;
  }
  
  public String getObjectAttributeValue(IDfPersistentObject dfSysObject, String attributeName, String attributeType)
    throws DfException
  {
    String sAttributeValue = "";
    if (attributeType.equals("Repeating"))
    {
      sAttributeValue = dfSysObject.getAllRepeatingStrings(attributeName, ",");
    }
    else if (attributeType.equals("Time"))
    {
      sAttributeValue = dfSysObject.getTime(attributeName).asString("mm/dd/yy");
    }
    else if (attributeType.equals("Boolean"))
    {
      boolean bAttributeValue = dfSysObject.getBoolean(attributeName);
      if (bAttributeValue) {
        sAttributeValue = "True";
      } else {
        sAttributeValue = "False";
      }
    }
    else
    {
      sAttributeValue = dfSysObject.getString(attributeName);
    }
    return sAttributeValue;
  }
  
  public int getRepeatingAttributeValueCount(IDfPersistentObject dfSysObject, String attributeName)
    throws DfException
  {
    return dfSysObject.getValueCount(attributeName);
  }
  
  public String getObjectIDByName(IDfSession dfSession, String objectName)
    throws DfException
  {
    String sObjectId = "";
    try
    {
      IDfSysObject dfSysObject = (IDfSysObject)dfSession.getObjectByQualification("dm_sysobject where object_name ='" + objectName + "'");
      sObjectId = dfSysObject.getObjectId().getId();
    }
    catch (DfException ex)
    {
      String sErrMsg = "DFCHelper: getObjectIDByName: Error retrieving object id  for " + objectName + ". \n" + ex.getMessage() + "\n";
      throw new DfException(sErrMsg);
    }
    return sObjectId;
  }
  
  public ArrayList getRepeatingAttributeValues(IDfPersistentObject dfSysObject, String attributeName)
    throws DfException
  {
    int iValueCount = getRepeatingAttributeValueCount(dfSysObject, attributeName);
    ArrayList aValues = new ArrayList();
    for (int i = 0; i < iValueCount; i++) {
      aValues.add(dfSysObject.getRepeatingString(attributeName, i));
    }
    return aValues;
  }
  
  public void setRepAttrValueForSysObjects(IDfSession dfSession, IDfPersistentObject dfPersistentObject, String attrName, String delimiter, String attrValue)
    throws DfException
  {
    String[] aAttrValue = attrValue.split(delimiter);
    try
    {
      dfPersistentObject.removeAll(attrName);
      for (int i = 0; i < aAttrValue.length; i++)
      {
        String sAttributeValue = aAttrValue[i];
        dfPersistentObject.appendString(attrName, sAttributeValue);
      }
    }
    catch (DfException ex)
    {
      String sErrMsg = "DFCHelper: setRepAttrValueForSysObjects: Error encountered while setting repeating valued attribute. \n" + ex.getMessage() + "\n";
      throw new DfException(sErrMsg);
    }
  }
  
  public IDfACL getACL(IDfSession dfSession, String aclName)
    throws DfException
  {
    IDfACL dfACL = null;
    try
    {
      dfACL = (IDfACL)dfSession.getObjectByQualification("dm_acl where object_name ='" + aclName.replaceAll("'", "''") + "'");
    }
    catch (DfException ex)
    {
      String sErrMsg = "DFCHelper: getACL: Error encountered retrieving ACL " + aclName;
      throw new DfException(sErrMsg);
    }
    return dfACL;
  }
  
  public IDfUser getUser(IDfSession dfSession)
    throws DfException
  {
    IDfUser dfUser = null;
    try
    {
      dfUser = dfSession.getUser("");
    }
    catch (DfException ex)
    {
      String sErrorMsg = "DFCHelper: getUser: Exception\n" + ex.getMessage();
      throw new DfException(ex);
    }
    return dfUser;
  }
  
  public ArrayList getGroupMembership(IDfSession dfSession, String memberName)
    throws DfException
  {
    ArrayList aGroupMembershipList = new ArrayList();
    IDfCollection dfColl = null;
    try
    {
      String sDql = "SELECT group_name FROM dm_group WHERE group_class = 'group' and any i_all_users_names = '" + memberName + "'";
      dfColl = executeQuery(dfSession, sDql);
      aGroupMembershipList = getArrayListFromDMColl(dfColl);
      closeCollection(dfColl);
    }
    catch (DfException ex)
    {
      closeCollection(dfColl);
      String sErrMsg = "DFCHelper: getGroupMembership: Error encountered while retrieving Group Membership list. \n" + ex.getMessage() + "\n";
      throw new DfException(sErrMsg);
    }
    return aGroupMembershipList;
  }
  
  public boolean isMemberAGroup(IDfSession dfSession, String memberName)
    throws DfException
  {
    boolean isGroup = false;
    ArrayList aResultList = new ArrayList();
    IDfCollection dfColl = null;
    try
    {
      String sDql = "SELECT r_object_id FROM dm_group where group_name = '" + memberName + "'";
      dfColl = executeQuery(dfSession, sDql);
      aResultList = getArrayListFromDMColl(dfColl);
      closeCollection(dfColl);
      if (aResultList.size() > 0) {
        isGroup = true;
      }
    }
    catch (DfException ex)
    {
      closeCollection(dfColl);
      String sErrMsg = "DFCHelper: isMemberAGroup: Error encountered while checking for Group. \n" + ex.getMessage() + "\n";
      throw new DfException(sErrMsg);
    }
    return isGroup;
  }
  
  public boolean isMemberAUser(IDfSession dfSession, String memberName)
    throws DfException
  {
    boolean isUser = false;
    ArrayList aResultList = new ArrayList();
    IDfCollection dfColl = null;
    try
    {
      String sDql = "SELECT r_object_id FROM dm_user where user_name = '" + memberName.replaceAll("'", "''") + "'";
      dfColl = executeQuery(dfSession, sDql);
      aResultList = getArrayListFromDMColl(dfColl);
      closeCollection(dfColl);
      if (aResultList.size() > 0) {
        isUser = true;
      }
    }
    catch (DfException ex)
    {
      closeCollection(dfColl);
      String sErrMsg = "DFCHelper: isMemberAUser: Error encountered while checking for User. \n" + ex.getMessage() + "\n";
      throw new DfException(sErrMsg);
    }
    return isUser;
  }
  
  public String getMemberObjectId(IDfSession dfSession, String memberName)
    throws DfException
  {
    String sObjectId = "";
    try
    {
      if (isMemberAGroup(dfSession, memberName))
      {
        IDfGroup dfGroup = (IDfGroup)dfSession.getObjectByQualification("dm_group where group_name = '" + memberName.replaceAll("'", "''") + "'");
        sObjectId = dfGroup.getString("r_object_id");
      }
      if (isMemberAUser(dfSession, memberName))
      {
        IDfUser dfUser = (IDfUser)dfSession.getObjectByQualification("dm_user where user_name = '" + memberName.replaceAll("'", "''") + "'");
        sObjectId = dfUser.getString("r_object_id");
      }
    }
    catch (DfException ex)
    {
      String sErrMsg = "DFCHelper: getMemberObjectId: Error encountered while checking for Member. \n" + ex.getMessage() + "\n";
      throw new DfException(sErrMsg);
    }
    return sObjectId;
  }
  
  public void getDocbaseOwnerLoginId(IDfSession dfSession)
    throws DfException
  {
    try
    {
      System.out.println("DFCHelper: getDocbaseOwnerLoginId: enter .....");
      String str = dfSession.getDocbaseOwnerName();
    }
    catch (DfException ex)
    {
      throw new DfException(ex);
    }
  }
  
  public boolean isGroupCreated(IDfSession dfSession, String groupName)
    throws DfException
  {
    boolean bRetVal = false;
    try
    {
      IDfPersistentObject dfGroup = dfSession.getObjectByQualification("dm_group where group_name = '" + groupName.replaceAll("'", "''") + "'");
      if (dfGroup != null) {
        bRetVal = true;
      }
    }
    catch (DfException ex)
    {
      throw new DfException(ex);
    }
    return bRetVal;
  }
  
  public String getMemberObjectIdByName(IDfSession dfSession, String memberName)
    throws DfException
  {
    String sMemberId = "";
    try
    {
      if (isMemberAGroup(dfSession, memberName))
      {
        IDfGroup dfGroup = (IDfGroup)dfSession.getObjectByQualification("dm_group where group_name = '" + memberName + "'");
        sMemberId = dfGroup.getObjectId().getId();
      }
      else if (isMemberAUser(dfSession, memberName))
      {
        IDfUser dfUser = (IDfUser)dfSession.getObjectByQualification("dm_user where user_name = '" + memberName.replaceAll("'", "''") + "'");
        sMemberId = dfUser.getObjectId().getId();
      }
      else
      {
        String sErrMsg = "DFCHelper: getMemberObjectIdByName: Unable to locate member info for " + memberName + ". \n";
        throw new DfException(sErrMsg);
      }
    }
    catch (DfException ex)
    {
      String sErrMsg = "DFCHelper: getMemberObjectIdByName: Error encountered while retrieving member info for " + memberName + ". \n" + ex.getMessage() + "\n";
      throw new DfException(sErrMsg);
    }
    return sMemberId;
  }
  
  public boolean isUserInGroup(IDfSession dfSession, String memberName, String groupName)
    throws DfException
  {
    boolean bIsUserInGroup = false;
    ArrayList aGroupMembershipList = new ArrayList();
    IDfCollection dfColl = null;
    try
    {
      String sDql = "SELECT group_name FROM dm_group WHERE group_name = '" + groupName + "' and any i_all_users_names = '" + memberName.replaceAll("'", "''") + "'";
      dfColl = executeQuery(dfSession, sDql);
      aGroupMembershipList = getArrayListFromDMColl(dfColl);
      if (aGroupMembershipList.size() > 0) {
        bIsUserInGroup = true;
      }
      closeCollection(dfColl);
    }
    catch (DfException ex)
    {
      closeCollection(dfColl);
      String sErrMsg = "DFCHelper: isUserInGroup: Error encountered while retrieving Group Membership list. \n" + ex.getMessage() + "\n";
      throw new DfException(sErrMsg);
    }
    return bIsUserInGroup;
  }
  
  public String replaceString(String sourceString, String findChar, String replaceChar)
    throws Exception
  {
    String sUpdatedString = "";
    
    char[] ch = sourceString.toCharArray();
    for (int i = 0; i < ch.length; i++)
    {
      if (ch[i] == findChar.charAt(0)) {
        ch[i] = replaceChar.charAt(0);
      }
      sUpdatedString = sUpdatedString + ch[i];
    }
    return sUpdatedString;
  }
  
  private String getElapsedTime(Date date1, Date date2)
  {
    long time1InMS = date1.getTime();
    long time2InMS = date2.getTime();
    long timeInSeconds = (time2InMS - time1InMS) / 1000L;
    int hours = (int)(timeInSeconds / 3600L);
    timeInSeconds -= hours * 3600;
    int minutes = (int)(timeInSeconds / 60L);
    timeInSeconds -= minutes * 60;
    int seconds = (int)timeInSeconds;
    
    String elapsedTime = hours + " hour(s) " + minutes + " minute(s) " + seconds + " second(s)";
    return elapsedTime;
  }
  
  public String getSMTPServerEx(IDfSession dfSession)
    throws DfException
  {
    IDfTypedObject dfServerConfig = dfSession.getServerConfig();
    String sSMTPServer = dfServerConfig.getString("smtp_server");
    if (sSMTPServer == null) {
      sSMTPServer = "";
    }
    return sSMTPServer;
  }
  
  public String getInstallOwner(IDfSession dfSession)
    throws DfException
  {
    IDfTypedObject dfServerConfig = dfSession.getServerConfig();
    
    return dfServerConfig.getString("r_install_owner");
  }
  
  public String getUserAddress(IDfSession dfSession, String sMemberName)
    throws DfException
  {
    String sUserAddress = null;
    if (isMemberAGroup(dfSession, sMemberName))
    {
      IDfGroup dfGroup = dfSession.getGroup(sMemberName);
      if (dfGroup != null) {
        sUserAddress = dfGroup.getGroupAddress();
      }
    }
    else if (isMemberAUser(dfSession, sMemberName))
    {
      IDfUser dfUser = dfSession.getUser(sMemberName);
      if (dfUser != null) {
        sUserAddress = dfUser.getUserAddress();
      }
    }
    return sUserAddress;
  }
  
  public String getUserNameByEMail(IDfSession dfSession, String sMemberMail)
    throws DfException
  {
    String sUserName = null;
    IDfUser dfUser = (IDfUser)dfSession.getObjectByQualification("dm_user where user_address = '" + sMemberMail + "'");
    if (dfUser != null) {
      sUserName = dfUser.getUserName();
    }
    return sUserName;
  }
  
  public String getUserProperty(IDfSession dfSession, String sMemberName, String propertyName)
    throws DfException
  {
    String sPropertyValue = null;
    IDfUser dfUser = dfSession.getUser(sMemberName);
    if (dfUser != null) {
      sPropertyValue = dfUser.getString(propertyName);
    }
    return sPropertyValue;
  }
  
  public IDfSession createSession(String docbaseUserName, String docbasePassword, String docbaseName)
    throws DfException
  {
    IDfSession dfSession = null;
    IDfSessionManager dfSessionManager = null;
    System.out.println("instantiating client x libraries");
    IDfClientX clientx = new DfClientX();
    System.out.println("client x initialized");
    IDfClient client = clientx.getLocalClient();
    System.out.println("df client initialized");
    dfSessionManager = client.newSessionManager();
    System.out.println("retrieved session manager");
    IDfLoginInfo loginInfo = clientx.getLoginInfo();
    loginInfo.setUser(docbaseUserName);
    loginInfo.setPassword(docbasePassword);
    System.out.println("setting user identity for docbase session");
    dfSessionManager.setIdentity(docbaseName, loginInfo);
    dfSession = dfSessionManager.getSession(docbaseName);
    System.out.println("docbase session retrieved");
    return dfSession;
  }
  
  public final void releaseSessionEx(IDfSession dfSession)
  {
    if (dfSession != null) {
      try
      {
        dfSession.disconnect();
      }
      catch (DfException localDfException) {}
    }
  }
  
  public final void releaseSession(IDfSession dfSession)
  {
    if (dfSession != null)
    {
      IDfSessionManager dfSessionManager = dfSession.getSessionManager();
      dfSessionManager.release(dfSession);
    }
  }
  
  public String isNameValid(String name)
  {
    String sMsg = "";
    if (name.indexOf("/") >= 0) {
      sMsg = sMsg + "/";
    }
    if (name.indexOf("\\") >= 0) {
      if (sMsg.equals("")) {
        sMsg = sMsg + "\\.";
      } else {
        sMsg = sMsg + ",\\.";
      }
    }
    return sMsg;
  }
}
