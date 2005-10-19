/**
 * <p>Title: ShoppingCartAction Class>
 * <p>Description:	This class initializes the fields of ShoppingCart.jsp Page</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Aniruddha Phadnis
 * @version 1.00
 * Created on Jul 18, 2005
 */

package edu.wustl.catissuecore.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.ShoppingCartForm;
import edu.wustl.catissuecore.bizlogic.BizLogicFactory;
import edu.wustl.catissuecore.bizlogic.ShoppingCartBizLogic;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.query.ShoppingCart;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.Variables;
import edu.wustl.common.util.ExportReport;
import edu.wustl.common.util.SendFile;

public class ShoppingCartAction  extends BaseAction
{
    /**
     * Overrides the execute method of Action class.
     * Initializes the various fields in ShoppingCart.jsp Page.
     * */
    public ActionForward executeAction(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException
    {
        //Gets the value of the operation parameter.
        String operation = (String)request.getParameter(Constants.OPERATION);
        
        HttpSession session = request.getSession(true);
        ShoppingCart cart = (ShoppingCart)session.getAttribute(Constants.SHOPPING_CART);
        ShoppingCartBizLogic bizLogic = (ShoppingCartBizLogic)BizLogicFactory.getBizLogic(Constants.SHOPPING_CART_FORM_ID);
        ShoppingCartForm shopForm = (ShoppingCartForm)form;
     
        if(cart == null)
        {
        	cart = new ShoppingCart();
        }
        
        if(operation == null)
        {
        	try
			{
        		List specimenList = bizLogic.retrieve(Specimen.class.getName());
        		Iterator it = specimenList.iterator();
        		
        		while(it.hasNext())
        		{
        			Specimen specimen = (Specimen)it.next();
        			cart.add(specimen);
        		}
        		
        		session.setAttribute(Constants.SHOPPING_CART,cart);
        		
        		request.setAttribute(Constants.SPREADSHEET_DATA_LIST,makeGridData(cart));        		
			}
        	catch(Exception e)
			{
        		e.printStackTrace();
			}
        }
        else
        {
        	if(operation.equals(Constants.ADD))
	        {
	        	//bizLogic.add(cart);
	        }
	        else if(operation.equals(Constants.DELETE))
	        {
	        	Map map = shopForm.getValues();
	        	Object obj[] = map.keySet().toArray();
	        	
	        	session.setAttribute(Constants.SHOPPING_CART,bizLogic.delete(cart,obj));
	        }
	        else if(operation.equals(Constants.EXPORT))
	        {
	        	String fileName = Variables.catissueHome + System.getProperty("file.separator") + session.getId() + ".csv";
	        	
	        	List cartList = bizLogic.export(cart,fileName);
	        	
	        	ExportReport report = new ExportReport(fileName);
	    		report.writeData(cartList);
	    		report.closeFile();
	        	 
	        	SendFile.sendFileToClient(response,fileName,"ShoppingCart.csv","application/csv");
	        	
	        	String path = "/" + fileName;
	        	return new ActionForward(path);
	        }
	        
        	request.setAttribute(Constants.SPREADSHEET_DATA_LIST,makeGridData(cart));
        }
        
        //Sets the operation attribute to be used in the Add/Edit Shopping Cart Page. 
        request.setAttribute(Constants.OPERATION, operation);
        
        return mapping.findForward(Constants.SUCCESS);
    }
    
    //This function prepares the data in Grid Format
    private List makeGridData(ShoppingCart cart)
    {	
		List gridData = new ArrayList(); 
			
    	if(cart != null)
		{
			Hashtable cartTable = cart.getCart();
			
			if(cartTable != null && cartTable.size() != 0)
			{				
				Enumeration enum = cartTable.keys();
				
				while(enum.hasMoreElements())
				{
					String key = (String)enum.nextElement();
					Specimen specimen = (Specimen)cartTable.get(key);
					
					List rowData = new ArrayList();
					
					rowData.add("<input type='checkbox' name='value(CB_" + specimen.getSystemIdentifier() + ")'>");
					rowData.add(String.valueOf(specimen.getSystemIdentifier()));
					rowData.add(specimen.getClassName());
					
					if(specimen.getType() != null)
						rowData.add(specimen.getType());
					else
						rowData.add("");
					
					rowData.add(specimen.getSpecimenCharacteristics().getTissueSite());
					rowData.add(specimen.getSpecimenCharacteristics().getTissueSide());
					rowData.add(specimen.getSpecimenCharacteristics().getPathologicalStatus());
					
					gridData.add(rowData);
				}
			}
		}
		
		return gridData;
    }
}