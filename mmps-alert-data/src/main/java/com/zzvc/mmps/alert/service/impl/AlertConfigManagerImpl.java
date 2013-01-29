package com.zzvc.mmps.alert.service.impl;

import org.appfuse.service.impl.GenericManagerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zzvc.mmps.alert.dao.AlertConfigDao;
import com.zzvc.mmps.alert.model.AlertConfig;
import com.zzvc.mmps.alert.service.AlertConfigManager;

@Service("AlertConfigManager")
public class AlertConfigManagerImpl extends GenericManagerImpl<AlertConfig, Long> implements AlertConfigManager {
	
	private AlertConfigDao alertConfigDao;

	@Autowired
	public AlertConfigManagerImpl(AlertConfigDao playerAlertDao) {
		super(playerAlertDao);
		this.alertConfigDao = playerAlertDao;
	}

	@Override
	public String getConfig(String configKey) {
		AlertConfig config = alertConfigDao.findAlertConfig(configKey);
		if (config == null) {
			return null;
		} else {
			return config.getConfigValue();
		}
	}

	@Override
	public void setConfig(String configKey, String configValue) {
		AlertConfig config = alertConfigDao.findAlertConfig(configKey);
		if (config == null) {
			config = new AlertConfig();
			config.setConfigKey(configKey);
		}
		config.setConfigValue(configValue);
		save(config);
	}

}
