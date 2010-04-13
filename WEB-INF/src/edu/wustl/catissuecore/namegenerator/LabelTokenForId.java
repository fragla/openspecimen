package edu.wustl.catissuecore.namegenerator;

import edu.wustl.catissuecore.bizlogic.NewSpecimenBizLogic;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.exception.BizLogicException;


// TODO: Auto-generated Javadoc
/**
 * The Class LabelTokenForId.
 */
/**
 * @author nitesh_marwaha
 *
 */
public class LabelTokenForId implements LabelTokens
{

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.namegenerator.LabelTokens#getTokenValue(java.lang.Object, java.lang.String)
	 */
	public String getTokenValue(Object object, String token)
	{
		Specimen objSpecimen = (Specimen) object;
		String valToReplace = "";
		Long specimenCount = 0l;
		if (objSpecimen.getLineage().equals(Constants.NEW_SPECIMEN) ||objSpecimen.getLineage().equals(Constants.DERIVED_SPECIMEN))
		{
			NewSpecimenBizLogic newSpecimenBizLogic = new NewSpecimenBizLogic();
			try
			{
				specimenCount=newSpecimenBizLogic.getSpecimenCountforPPI(objSpecimen);
			}
			catch (BizLogicException e)
			{
				e.printStackTrace();
			}
			specimenCount = specimenCount+1;
			valToReplace = specimenCount+"";
		}

		return valToReplace;
	}
}
