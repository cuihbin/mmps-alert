package com.zzvc.mmps.alert.dao;

import org.appfuse.dao.GenericDao;

import com.zzvc.mmps.alert.model.AlertConfig;

public interface AlertConfigDao extends GenericDao<AlertConfig, Long> {
	String getConfig(String configKey);
	void setConfig(String configKey, String configValue);
}
