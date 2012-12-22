package com.zzvc.mmps.alert.dao.hibernate;

import org.appfuse.dao.hibernate.GenericDaoHibernate;
import org.hibernate.criterion.Restrictions;

import com.zzvc.mmps.alert.dao.AlertConfigDao;
import com.zzvc.mmps.alert.model.AlertConfig;

public class AlertConfigDaoHibernate extends GenericDaoHibernate<AlertConfig, Long> implements AlertConfigDao {
	
	public AlertConfigDaoHibernate() {
		super(AlertConfig.class);
	}

	@Override
	public String getConfig(String configKey) {
		AlertConfig config = findAlertConfig(configKey);
		if (config == null) {
			return null;
		} else {
			return config.getConfigValue();
		}
	}
	
	@Override
	public void setConfig(String configKey, String configValue) {
		AlertConfig config = findAlertConfig(configKey);
		if (config == null) {
			config = new AlertConfig();
			config.setConfigKey(configKey);
		}
		config.setConfigValue(configValue);
		save(config);
	}
	
	private AlertConfig findAlertConfig(String configKey) {
		return (AlertConfig) getSession().createCriteria(AlertConfig.class).add(Restrictions.eq("configKey", configKey)).uniqueResult();
	}
	

}
