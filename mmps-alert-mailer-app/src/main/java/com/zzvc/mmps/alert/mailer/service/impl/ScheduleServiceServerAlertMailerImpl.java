package com.zzvc.mmps.alert.mailer.service.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import com.zzvc.mmps.alert.model.AlertBaseEntity;
import com.zzvc.mmps.alert.model.ServerAlert;
import com.zzvc.mmps.alert.service.AlertConfigManager;
import com.zzvc.mmps.alert.service.ServerAlertManager;
import com.zzvc.mmps.alert.service.ServerManager;
import com.zzvc.mmps.alert.util.AlertConstants;
import com.zzvc.mmps.model.Server;

public class ScheduleServiceServerAlertMailerImpl extends ScheduleServiceAlertMailerSupport {
	
	@Resource
	private AlertConfigManager alertConfigManager;
	
	@Resource
	private ServerManager serverManager;
	
	@Resource
	private ServerAlertManager serverAlertManager;
	
	private int minutesBeforeServerFault;

	@Override
	public void init() {
		super.init();
		
		try {
			minutesBeforeServerFault = Integer.parseInt(alertConfigManager.getConfig(AlertConstants.CFG_MINUTES_BEFORE_SERVER_FAULT));
		} catch (Exception e) {
			minutesBeforeServerFault = AlertConstants.DEFAULT_MINUTES_BEFORE_SERVER_FAULT;
		}
		infoMessage("alert.mailer.config.parameter", "CFG_MINUTES_BEFORE_SERVER_FAULT", minutesBeforeServerFault);
	}

	@Override
	protected Collection findActiveAlert() {
		return serverAlertManager.findActiveAlert();
	}

	@Override
	protected Object findRelatedEntity(AlertBaseEntity alert) {
		for (Server server : serverManager.findByCode(((ServerAlert) alert).getCode())) {
			if (server.getEnabled()) {
				return server;
			}
		}
		return null;
	}

	@Override
	protected String getAlertKeyWord(AlertBaseEntity alert) {
		return ((Server) findRelatedEntity(alert)).getName();
	}

	@Override
	protected Map<String, Object> getTempleteRootMap(AlertBaseEntity alert) {
		Map<String, Object> rootMap = new HashMap<String, Object>();
		Server server = (Server) findRelatedEntity(alert); 
		rootMap.put("name", server.getName());
		rootMap.put("lastHeartbeat", server.getLastHeartbeat());
		return rootMap;
	}

}
