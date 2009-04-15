/**
 * <p>Title: ParticipantLookupAction Class>
 * <p>Description:	This Action Class invokes the Participant Lookup Algorithm and gets matching participants</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author vaishali_khandelwal
 * @Created on May 19, 2006
 */

package edu.wustl.catissuecore.action;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import edu.wustl.catissuecore.bizlogic.ParticipantBizLogic;
import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.domain.ParticipantMedicalIdentifier;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.Variables;
import edu.wustl.common.action.BaseAction;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.factory.AbstractFactoryConfig;
import edu.wustl.common.factory.IDomainObjectFactory;
import edu.wustl.common.factory.IFactory;
import edu.wustl.common.lookup.DefaultLookupResult;
import edu.wustl.common.lookup.LookupLogic;
import edu.wustl.common.util.Utility;
import edu.wustl.common.util.XMLPropertyHandler;
import edu.wustl.common.util.global.CommonServiceLocator;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.dao.DAO;
import edu.wustl.dao.exception.DAOException;

public class ParticipantLookupAction extends BaseAction
{
	private transient Logger logger = Logger.getCommonLogger(ParticipantLookupAction.class);
	/**
	 * @param mapping object of ActionMapping
	 * @param form object of ActionForm
	 * @param request object of HttpServletRequest
	 * @param response object of HttpServletResponse
	 * @throws Exception generic exception
	 * @return value for ActionForward object
	 */
	@Override
	public ActionForward executeAction(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception 
	{
		ActionMessages messages=null;
		String target=null;
		final StringBuffer partMRNColName= new StringBuffer("");
		
		AbstractActionForm abstractForm = (AbstractActionForm) form;
		
		IDomainObjectFactory factoryObj = AbstractFactoryConfig.getInstance().getDomainObjectFactory();
		
		
		AbstractDomainObject abstractDomain = factoryObj.getDomainObject(abstractForm.getFormId(),
				abstractForm);
		Participant participant = (Participant) abstractDomain;
		// 11968 S		
		if(!isAuthorized(mapping,request,participant))
		{
	        ActionErrors errors = new ActionErrors();
	        ActionError error = new ActionError("access.execute.action.denied");
	        errors.add(ActionErrors.GLOBAL_ERROR, error);
	        saveErrors(request, errors);
	        return mapping.findForward("failure");	      
		}
		// 11968 E
		logger.debug("Participant Id :"+request.getParameter("participantId"));
		//checks weather participant is selected from the list and so forwarding to next action instead of participant lookup.
		//Abhishek Mehta
		if(request.getAttribute("continueLookup") == null)
		{
			if(request.getParameter("participantId")!=null &&!request.getParameter("participantId").equals("null")&&!request.getParameter("participantId").equals("")&&!request.getParameter("participantId").equals("0"))
			{
				logger.info("inside the participant mapping");
				return mapping.findForward("participantSelect");
			}
		}
		
		boolean isCallToLookupLogicNeeded = isCallToLookupLogicNeeded(participant);
		
		if(isCallToLookupLogicNeeded)
		{
			IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
			ParticipantBizLogic bizlogic = (ParticipantBizLogic)factory.getBizLogic(Constants.PARTICIPANT_FORM_ID);
			LookupLogic participantLookupLogic = (LookupLogic)Utility.getObject(XMLPropertyHandler.getValue(Constants.PARTICIPANT_LOOKUP_ALGO));
			List matchingParticipantList = bizlogic.getListOfMatchingParticipants(participant,participantLookupLogic);
			if (matchingParticipantList!=null && matchingParticipantList.size() > 0)
			{
				messages=new ActionMessages();
				messages.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage("participant.lookup.success","Submit was not successful because some matching participants found."));
	   			//Creating the column headings for Data Grid
				List columnList = getColumnHeadingList(bizlogic,partMRNColName);
				request.setAttribute(Constants.SPREADSHEET_COLUMN_LIST, columnList);
				request.setAttribute(Constants.PARTICIPANT_MRN_COL_NAME, partMRNColName);
				
				//Getitng the Participant List in Data Grid Format
				List participantDisplayList=getParticipantDisplayList(matchingParticipantList,bizlogic);
				request.setAttribute(edu.wustl.simplequery.global.Constants.SPREADSHEET_DATA_LIST, participantDisplayList);
				
				target=Constants.PARTICIPANT_LOOKUP_SUCCESS;
			}
			//	if no participant match found then add the participant in system
			else
			{
				target=Constants.PARTICIPANT_ADD_FORWARD;
			}
		}
		else
		{
			target=Constants.PARTICIPANT_ADD_FORWARD;
		}
		
		//if any matching participants are there then show the participants otherwise add the participant
		
		
		//setting the Submitted_for and Forward_to variable in request
		if(request.getParameter(Constants.SUBMITTED_FOR)!=null && !request.getParameter(Constants.SUBMITTED_FOR).equals(""))
		{
			request.setAttribute(Constants.SUBMITTED_FOR,request.getParameter(Constants.SUBMITTED_FOR));
		}
		if(request.getParameter(Constants.FORWARD_TO)!=null && !request.getParameter(Constants.FORWARD_TO).equals(""))
		{
			request.setAttribute(Constants.FORWARD_TO,request.getParameter(Constants.FORWARD_TO));
			
		}
		
		request.setAttribute("participantId","");
		if(request.getAttribute("continueLookup") == null)
		{
			if (messages != null)
	        {
	            saveMessages(request,messages);
	        }
		}
		logger.debug("target:"+target);
		return (mapping.findForward(target));
	}

	// 11968 S
	private boolean isAuthorized(ActionMapping mapping,HttpServletRequest request,Participant participant)
	{
		DAO dao = null;
		SessionDataBean sessionDataBean = getSessionData(request);
		boolean authorizedFlag=false;
		try
		{
			 dao = AppUtility.openDAOSession(null);
			ParticipantBizLogic biz= new ParticipantBizLogic();			
			authorizedFlag = biz.isAuthorized(dao,participant,sessionDataBean);
			
		}
		catch (Exception e) 
		{
			logger.error("Exception occured : " + e.getMessage() , e);
			authorizedFlag=false;
		}
		finally
		{
			try 
			{
				AppUtility.closeDAOSession(dao);
			}
			catch (ApplicationException e)
			{
				logger.error("Exception occured : " + e.getMessage() , e);
			}
		}
		return authorizedFlag;		
	}
	// 11968 E
	
	
	private boolean isCallToLookupLogicNeeded(Participant participant)
	{
		if((participant.getFirstName() == null || participant.getFirstName().length()==0) && (participant.getMiddleName() == null || participant.getMiddleName().length() == 0) && (participant.getLastName() == null || participant.getLastName().length() == 0) && (participant.getSocialSecurityNumber()== null || participant.getSocialSecurityNumber().length() == 0) && participant.getBirthDate() == null && (participant.getParticipantMedicalIdentifierCollection() == null || participant.getParticipantMedicalIdentifierCollection().size()==0))
		{
			return false;
		}
		return true;
	}
	/**
	 * This Function creates the Column Headings for Data Grid
	 * @param bizlogic instance of ParticipantBizLogic
	 * @throws Exception generic exception
	 * @return List Column List
	 */
	private List getColumnHeadingList(ParticipantBizLogic bizlogic,StringBuffer partMRNColName) throws Exception
	{
		//Creating the column list which is used in Data grid to display column headings
		String[] columnHeaderList = new String[]{Constants.PARTICIPANT_MEDICAL_RECORD_NO,Constants.PARTICIPANT_GENDER,Constants.PARTICIPANT_BIRTH_DATE,Constants.PARTICIPANT_SOCIAL_SECURITY_NUMBER,Constants.PARTICIPANT_DEATH_DATE,Constants.PARTICIPANT_VITAL_STATUS};
		List columnList = new ArrayList();
		logger.info("column List header size ;"+columnHeaderList.length);	
		for (int i = 0; i < columnHeaderList.length; i++)
		{
			columnList.add(columnHeaderList[i]);
		}
		logger.info("column List size ;"+columnList.size());
		List displayList=bizlogic.getColumnList(columnList,partMRNColName);
	
		displayList.add(0, Constants.PARTICIPANT_NAME_HEADERLABEL);
	//	displayList.add(0,Constants.PARTICIPANT_PROBABLITY_MATCH);
		return displayList;
	}
    /**
	 * This functions creates Particpant List with each participant informaton  with the match probablity
	 * @param participantList list of participant
	 * @param bizLogic : ParticipantBizLogic
	 * @return List of Participant Information  List
	 * @throws DAOException : db exception
	 */
	private List getParticipantDisplayList(List participantList,
			ParticipantBizLogic bizLogic)throws BizLogicException
	{
		List participantDisplayList=new ArrayList();
		Iterator<DefaultLookupResult> itr=participantList.iterator();
		while(itr.hasNext())
		{
			DefaultLookupResult result=(DefaultLookupResult)itr.next();
			Participant participant=(Participant)result.getObject();
			List participantInfo = getParticipantInfo(bizLogic, participant);
			participantDisplayList.add(participantInfo);
		}
		return participantDisplayList;
	}


	/**
	 *  To get participant info
	 * @param bizLogic :ParticipantBizLogic
	 * @param participant :Participant
	 * @return List of particcipant info
	 * @throws DAOException :db exception
	 */
	private List getParticipantInfo(ParticipantBizLogic bizLogic, Participant participant)
			throws BizLogicException
	{
		StringBuffer participantName = new StringBuffer();
		List participantInfo = new ArrayList();
		String partLastName = Utility.toString(participant.getLastName());
		String partFirstName = Utility.toString(participant.getFirstName());
		participantName.append(partLastName);
		if(partLastName!=null &&!(("").equals(partLastName))&&
				partFirstName!=null &&!("").equals(partFirstName))
		{
			String stringCharAppend = "~";
			participantName.append(stringCharAppend);
		}
		participantName.append(partFirstName);
		participantInfo.add(participantName.toString());
		String mrn= getParticipantMrnDisplay(bizLogic, participant);
		participantInfo.add(Utility.toString(mrn));
		participantInfo.add(Utility.toString(participant.getGender()));
		
		//participantInfo.add(Utility.toString(participant.getBirthDate()));
		// Added by Geeta  for date format change.
		participantInfo.add(Utility.parseDateToString(participant.getBirthDate(),CommonServiceLocator.getInstance().getDatePattern()));
		// End by geeta  
		if(!Variables.isSSNRemove)
		{
			participantInfo.add(Utility.toString(participant.getSocialSecurityNumber()));
		}
		//participantInfo.add(Utility.toString(participant.getDeathDate()));
		// Added by Geeta  for date format change.
		participantInfo.add(Utility.parseDateToString(participant.getDeathDate(),CommonServiceLocator.getInstance().getDatePattern()));
		//End by Geeta
		participantInfo.add(Utility.toString(participant.getVitalStatus()));
		participantInfo.add(participant.getId());
		return participantInfo;
	}

/**
 * Method to get MRNs related to a participant.
 * @param bizLogic  ParticipantBizLogic
 * @param participant :participant
 * @return String
 * @throws DAOException : db exception
 */
	private String getParticipantMrnDisplay(ParticipantBizLogic bizLogic,
			Participant participant)throws BizLogicException
	{
		StringBuffer mrn = new StringBuffer();
		Long siteId;
		String siteName;
		if(participant.getParticipantMedicalIdentifierCollection()!=null)
		{
			Iterator<ParticipantMedicalIdentifier> pmiItr =
				participant.getParticipantMedicalIdentifierCollection().iterator();
			while(pmiItr.hasNext())
			{
				ParticipantMedicalIdentifier participantMedicalIdentifier =pmiItr.next();
				if(participantMedicalIdentifier.getSite()!=null&&
						participantMedicalIdentifier.getSite().getId() != null)
				{
					siteId = participantMedicalIdentifier.getSite().getId();
					Site site = (Site)bizLogic.retrieve(Site.class.getName(), siteId);
					siteName= site.getName();
					mrn.append(siteName);
					String stringCharAppend=":";
					mrn.append(stringCharAppend);
					mrn.append(participantMedicalIdentifier.getMedicalRecordNumber());
					mrn.append("\n"+"<br>");
				}
			}
		}
		return mrn.toString();
	}
}