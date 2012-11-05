/**
 * <p>Title: InstitutionBizLogic Class>
 * <p>Description:	InstitutionBizLogic </p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Ashish Gupta
 * @version 1.00
 * Created on Sep 19, 2006
 */

package edu.wustl.catissuecore.bizlogic;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import edu.wustl.catissuecore.domain.ClinicalDiagnosis;
import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.CollectionProtocolEvent;
import edu.wustl.catissuecore.domain.ConsentTier;
import edu.wustl.catissuecore.domain.SpecimenRequirement;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.util.global.CommonServiceLocator;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.dao.DAO;
import edu.wustl.dao.daofactory.DAOConfigFactory;
import edu.wustl.dao.daofactory.IDAOFactory;
import edu.wustl.dao.exception.DAOException;
import edu.wustl.dao.query.generator.ColumnValueBean;
/**
 * @author
 *
 */
public class ExportCollectionProtocolBizLogic extends CatissueDefaultBizLogic
{
	DAO dao = null;
	private transient final Logger logger = Logger
			.getCommonLogger(ExportCollectionProtocolBizLogic.class);

	public StringBuffer getCPXMLFile(String title) throws BizLogicException
	{
		StringBuffer csvFile = null;
		final IDAOFactory daofactory = DAOConfigFactory.getInstance()
				.getDAOFactory(CommonServiceLocator.getInstance().getAppName());
		try
		{
			dao = daofactory.getDAO();
			dao.openSession(null);
			csvFile = getCSV(getCollectionProtocol(title));
			
		} catch (final DAOException e) {
			logger.error(e.getMessage(), e);
			throw getBizLogicException(e, e.getErrorKeyName(), e.getMsgValues());
		}
		finally
		{
			try {
				if (dao != null)
					dao.closeSession();
			} catch (final DAOException e) {
				logger.error(e.getMessage(), e);
				throw getBizLogicException(e, e.getErrorKeyName(), e
						.getMsgValues());
			}
		}
		return csvFile;
	}
	/**
     * @param collectionProtocol
     * @return
     * @throws BizLogicException
	 * @throws DAOException 
     */
    private CollectionProtocol getCollectionProtocol(String title)
            throws DAOException
    {
		CollectionProtocol collectionProtocol;
		ColumnValueBean columnValueBean = new ColumnValueBean("title", title);
		List<CollectionProtocol> collectionProtocols = (List<CollectionProtocol>) dao
				.retrieve(CollectionProtocol.class.getName(), columnValueBean);
		collectionProtocol = collectionProtocols.get(0);

		return collectionProtocol;
    }
    private StringBuffer getCSV(CollectionProtocol collectionProtocol) throws BizLogicException
    {
    	StringBuffer headerString=new StringBuffer();
    	StringBuffer valueString=new StringBuffer();
    	
    	headerString.append("Principal Investigator").append(",");
    	valueString.append("\"").append( collectionProtocol.getPrincipalInvestigator().getLoginName()).append("\"").append(",");
    	//headerDataMap.put("Principal Investigator", collectionProtocol.getPrincipalInvestigator().getLoginName());
    	Collection<User> coordinatorUsers=collectionProtocol.getCoordinatorCollection();
    	int coordinatorCount=1;
    	for (User user : coordinatorUsers) {
    		headerString.append("Principal cordinator#").append(coordinatorCount++).append(",");
    		valueString.append("\"").append(user.getLoginName()).append("\"").append(",");
		}
    	
    	//headerDataMap.put("Title",collectionProtocol.getTitle());
    	headerString.append("Title").append(",");
    	valueString.append("\"").append(collectionProtocol.getTitle()).append("\"").append(",");
    	
    	//headerDataMap.put("STitle",collectionProtocol.getShortTitle());
    	headerString.append("STitle").append(",");
    	valueString.append("\"").append(collectionProtocol.getShortTitle()).append("\"").append(",");
    	
    	if(collectionProtocol.getIrbIdentifier()!=null)
    	{
    		//headerDataMap.put("IRB",collectionProtocol.getIrbIdentifier());
    		headerString.append("IRB").append(",");
        	valueString.append("\"").append(collectionProtocol.getIrbIdentifier()).append("\"").append(",");
    	}
    	//headerDataMap.put("Date",getDateAsString(collectionProtocol.getStartDate()));
    	headerString.append("Date").append(",");
    	valueString.append("\"").append(getDateAsString(collectionProtocol.getStartDate())).append("\"").append(",");
    	
    	//headerDataMap.put("URL",collectionProtocol.getDescriptionURL());
    	headerString.append("URL").append(",");
    	valueString.append("\"").append(collectionProtocol.getDescriptionURL()).append("\"").append(",");
    	
    	//headerDataMap.put("Activity Status",collectionProtocol.getActivityStatus());
    	headerString.append("Activity Status").append(",");
    	valueString.append("\"").append(collectionProtocol.getActivityStatus()).append("\"").append(",");
    	if(collectionProtocol.getConsentsWaived()!=null)
    	{
    		//headerDataMap.put("Waived",collectionProtocol.getConsentsWaived().toString());
    		headerString.append("Waived").append(",");
        	valueString.append("\"").append(collectionProtocol.getConsentsWaived().toString()).append("\"").append(",");
    	}
    	//headerDataMap.put("Aliquot In Same Container",collectionProtocol.getAliquotInSameContainer().toString());
    	headerString.append("Aliquot In Same Container").append(",");
    	valueString.append("\"").append(collectionProtocol.getAliquotInSameContainer().toString()).append("\"").append(",");
    	
    	if(collectionProtocol.getEnrollment()!=null)
    	{
    		//headerDataMap.put("Enrollment",collectionProtocol.getEnrollment().toString());
    		headerString.append("Enrollment").append(",");
        	valueString.append("\"").append(collectionProtocol.getEnrollment().toString()).append("\"").append(",");
    	}
    	Collection<ConsentTier> consentTiers=collectionProtocol.getConsentTierCollection();
    	int consentTiersCount=1;
    	for (ConsentTier consentTier : consentTiers) {
    		//headerDataMap.put("Statements#"+consentTiersCount++,consentTier.getStatement());
    		headerString.append("Statements#").append(consentTiersCount++).append(",");
        	valueString.append("\"").append(consentTier.getStatement()).append("\"").append(",");
        	
		}
    	Collection<ClinicalDiagnosis> clinicalDiagnosis=collectionProtocol.getClinicalDiagnosisCollection();
    	int clinicalDiagnosisCount=1;
    	for (ClinicalDiagnosis cd : clinicalDiagnosis) {
    		//headerDataMap.put("Clinical Diagnosis#"+clinicalDiagnosisCount++,cd.getName());
    		headerString.append("Clinical Diagnosis#").append(clinicalDiagnosisCount++).append(",");
        	valueString.append("\"").append(cd.getName()).append("\"").append(",");
		}
    	Collection<CollectionProtocolEvent> events=collectionProtocol.getCollectionProtocolEventCollection();
    	int eventsCount=1;
    	for (CollectionProtocolEvent collectionProtocolEvent : events) {
    		//headerDataMap.put("Study Calender Event Point#"+eventsCount,collectionProtocolEvent.getStudyCalendarEventPoint().toString());
    		headerString.append("Study Calender Event Point#").append(eventsCount).append(",");
        	valueString.append("\"").append(collectionProtocolEvent.getStudyCalendarEventPoint().toString()).append("\"").append(",");
        	
    		//headerDataMap.put("CPL#"+eventsCount,collectionProtocolEvent.getCollectionPointLabel());
        	headerString.append("CPL#").append(eventsCount).append(",");
        	valueString.append("\"").append(collectionProtocolEvent.getCollectionPointLabel()).append("\"").append(",");
        	
    		//headerDataMap.put("CS#"+eventsCount,collectionProtocolEvent.getClinicalStatus());
        	headerString.append("CS#").append(eventsCount).append(",");
        	valueString.append("\"").append(collectionProtocolEvent.getClinicalStatus()).append("\"").append(",");
        	
    		//headerDataMap.put("ActivityStatus#"+eventsCount,collectionProtocolEvent.getActivityStatus());
        	headerString.append("ActivityStatus#").append(eventsCount).append(",");
        	valueString.append("\"").append(collectionProtocolEvent.getActivityStatus()).append("\"").append(",");
        	Collection<SpecimenRequirement> requirements=collectionProtocolEvent.getSpecimenRequirementCollection();
        	int specimenRequirementCount=1;
        	for (SpecimenRequirement specimenRequirement : requirements) {
        		if(specimenRequirement.getParentSpecimen()==null)
        		{
        			//headerDataMap.put("Specimen Class"+postfix+"#"+childSpecimenCount,abstractSpecimen.getSpecimenClass());
            		headerString.append("Specimen Class#").append(eventsCount).append("#").append(specimenRequirementCount).append(",");
            		valueString.append("\"").append(specimenRequirement.getSpecimenClass()).append("\"").append(",");
            				
        			//headerDataMap.put("Specimen Type"+postfix+"#"+childSpecimenCount,abstractSpecimen.getSpecimenType());
        			headerString.append("Specimen Type#").append(eventsCount).append("#").append(specimenRequirementCount).append(",");
        			valueString.append("\"").append(specimenRequirement.getSpecimenType()).append("\"").append(",");
        			
        			//headerDataMap.put("Storage Location"+postfix+"#"+childSpecimenCount,((SpecimenRequirement)abstractSpecimen).getStorageType());
        			headerString.append("Storage Location#").append(eventsCount).append("#").append(specimenRequirementCount).append(",");
        			valueString.append("\"").append(specimenRequirement.getStorageType()).append("\"").append(",");
        			
        			//headerDataMap.put("Pathological Status"+postfix+"#"+childSpecimenCount,abstractSpecimen.getPathologicalStatus());
        			headerString.append("Pathological Status#").append(eventsCount).append("#").append(specimenRequirementCount).append(",");
        			valueString.append("\"").append(specimenRequirement.getPathologicalStatus()).append("\"").append(",");
        			
        			//headerDataMap.put("Initial Quantity"+postfix+"#"+childSpecimenCount,abstractSpecimen.getInitialQuantity().toString());
        			headerString.append("Initial Quantity#").append(eventsCount).append("#").append(specimenRequirementCount).append(",");
        			valueString.append("\"").append(specimenRequirement.getInitialQuantity().toString()).append("\"").append(",");
        			
        			//headerDataMap.put("lineage"+postfix+"#"+childSpecimenCount,abstractSpecimen.getLineage());
        			headerString.append("Lineage#").append(eventsCount).append("#").append(specimenRequirementCount).append(",");
        			valueString.append("\"").append(specimenRequirement.getLineage()).append("\"").append(",");
        			
        			//headerDataMap.put("Tissue Site"+postfix+"#"+childSpecimenCount,abstractSpecimen.getSpecimenCharacteristics().getTissueSite());
        			headerString.append("Tissue Site#").append(eventsCount).append("#").append(specimenRequirementCount).append(",");
        			valueString.append("\"").append(specimenRequirement.getSpecimenCharacteristics().getTissueSite()).append("\"").append(",");
        			
        			//headerDataMap.put("Tissue Side"+postfix+"#"+childSpecimenCount,abstractSpecimen.getSpecimenCharacteristics().getTissueSide());
        			headerString.append("Tissue Side#").append(eventsCount).append("#").append(specimenRequirementCount).append(",");
        			valueString.append("\"").append(specimenRequirement.getSpecimenCharacteristics().getTissueSide()).append("\"").append(",");
        			updateMapForChildSpecimen(headerString,valueString,specimenRequirement.getChildSpecimenCollection(),"#"+eventsCount+"#"+specimenRequirementCount);
        			specimenRequirementCount++;
        		}
			}
    		
    		eventsCount++;
		}
    	headerString=headerString.replace(headerString.lastIndexOf(","), headerString.lastIndexOf(",")+1, "");
    	valueString=valueString.replace(valueString.lastIndexOf(","), valueString.lastIndexOf(",")+1, "");
    	return headerString.append("\n").append(valueString);//writeCSVFile(collectionProtocol.getShortTitle(),headerString,valueString);
    }
    private String getDateAsString(Date date)
    {
    	DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
    	String dateString = formatter.format(date);
    	return dateString;
    }
    private void updateMapForChildSpecimen(StringBuffer headerString,StringBuffer valueString,Collection specimenCollection,String postfix)
    {
    	int childSpecimenCount=1;
    	for (Object abstractSpecimen : specimenCollection) {
    		//headerDataMap.put("Specimen Class"+postfix+"#"+childSpecimenCount,abstractSpecimen.getSpecimenClass());
    		headerString.append("Specimen Class").append(postfix).append("#").append(childSpecimenCount).append(",");
    		valueString.append("\"").append(((SpecimenRequirement)abstractSpecimen).getSpecimenClass()).append("\"").append(",");
    				
			//headerDataMap.put("Specimen Type"+postfix+"#"+childSpecimenCount,abstractSpecimen.getSpecimenType());
			headerString.append("Specimen Type").append(postfix).append("#").append(childSpecimenCount).append(",");
			valueString.append("\"").append(((SpecimenRequirement)abstractSpecimen).getSpecimenType()).append("\"").append(",");
			
			//headerDataMap.put("Storage Location"+postfix+"#"+childSpecimenCount,((SpecimenRequirement)abstractSpecimen).getStorageType());
			headerString.append("Storage Location").append(postfix).append("#").append(childSpecimenCount).append(",");
			valueString.append("\"").append(((SpecimenRequirement)abstractSpecimen).getStorageType()).append("\"").append(",");
			
			//headerDataMap.put("Pathological Status"+postfix+"#"+childSpecimenCount,abstractSpecimen.getPathologicalStatus());
			headerString.append("Pathological Status").append(postfix).append("#").append(childSpecimenCount).append(",");
			valueString.append("\"").append(((SpecimenRequirement)abstractSpecimen).getPathologicalStatus()).append("\"").append(",");
			
			//headerDataMap.put("Initial Quantity"+postfix+"#"+childSpecimenCount,abstractSpecimen.getInitialQuantity().toString());
			headerString.append("Initial Quantity").append(postfix).append("#").append(childSpecimenCount).append(",");
			valueString.append("\"").append(((SpecimenRequirement)abstractSpecimen).getInitialQuantity().toString()).append("\"").append(",");
			
			//headerDataMap.put("lineage"+postfix+"#"+childSpecimenCount,abstractSpecimen.getLineage());
			headerString.append("Lineage").append(postfix).append("#").append(childSpecimenCount).append(",");
			valueString.append("\"").append(((SpecimenRequirement)abstractSpecimen).getLineage()).append("\"").append(",");
			
			//headerDataMap.put("Tissue Site"+postfix+"#"+childSpecimenCount,abstractSpecimen.getSpecimenCharacteristics().getTissueSite());
			headerString.append("Tissue Site").append(postfix).append("#").append(childSpecimenCount).append(",");
			valueString.append("\"").append(((SpecimenRequirement)abstractSpecimen).getSpecimenCharacteristics().getTissueSite()).append("\"").append(",");
			
			//headerDataMap.put("Tissue Side"+postfix+"#"+childSpecimenCount,abstractSpecimen.getSpecimenCharacteristics().getTissueSide());
			headerString.append("Tissue Side").append(postfix).append("#").append(childSpecimenCount).append(",");
			valueString.append("\"").append(((SpecimenRequirement)abstractSpecimen).getSpecimenCharacteristics().getTissueSide()).append("\"").append(",");
			
			updateMapForChildSpecimen(headerString,valueString,((SpecimenRequirement)abstractSpecimen).getChildSpecimenCollection(),postfix+"#"+childSpecimenCount);
			childSpecimenCount++;
		}
    }
}