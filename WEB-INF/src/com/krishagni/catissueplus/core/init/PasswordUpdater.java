package com.krishagni.catissueplus.core.init;

import java.sql.ResultSet;
import java.sql.Statement;

import org.apache.commons.lang.StringUtils;

import com.krishagni.catissueplus.core.common.util.Utility;

import liquibase.change.custom.CustomTaskChange;
import liquibase.database.Database;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.CustomChangeException;
import liquibase.exception.SetupException;
import liquibase.exception.ValidationErrors;
import liquibase.resource.ResourceAccessor;

public class PasswordUpdater implements CustomTaskChange {

	@Override
	public String getConfirmationMessage() {
		return "Update email account password successfully";
	}

	@Override
	public void setUp() throws SetupException {
	}

	@Override
	public void setFileOpener(ResourceAccessor resourceAccessor) {
	}

	@Override
	public ValidationErrors validate(Database database) {
		return new ValidationErrors();
	}

	@Override
	public void execute(Database database) throws CustomChangeException {
		JdbcConnection dbConnection = (JdbcConnection) database.getConnection();
		Statement statement;
		ResultSet rs;
		try {
			String sql = null;
			String password = null;
			Long id = null;
			
			statement = dbConnection.createStatement();
			sql = "select identifier, value from os_cfg_settings where property_id = "
					+ "(select identifier from os_cfg_props where name = 'account_password') and activity_status = 'Active'";
			
			rs = statement.executeQuery(sql);
			
			while(rs.next()) {
				id = rs.getLong("identifier");
				password = rs.getString("value");
			}
			
			if (StringUtils.isNotBlank(password)) {
				password = Utility.encrypt(password);
				sql = "update os_cfg_settings set value = '" + password + "' where identifier = " + id;
				statement.executeUpdate(sql);
				
				sql = "select identifier, value from os_cfg_settings where property_id = "
						+ "(select identifier from os_cfg_props where name = 'account_password') and activity_status = 'Active'";
				rs = statement.executeQuery(sql);
			}
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}