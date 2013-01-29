package com.zzvc.mmps.alert.service;

import org.appfuse.service.GenericManager;

import com.zzvc.mmps.alert.model.AlertConfig;

public interface AlertConfigManager extends GenericManager<AlertConfig, Long> {
	String getConfig(String configKey);
	void setConfig(String configKey, String configValue);
}
