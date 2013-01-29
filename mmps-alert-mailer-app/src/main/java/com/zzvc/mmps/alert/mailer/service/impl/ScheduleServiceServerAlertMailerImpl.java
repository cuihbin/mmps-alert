package com.zzvc.mmps.alert.mailer.service.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import com.zzvc.mmps.alert.dao.AlertConfigDao;
import com.zzvc.mmps.alert.dao.ServerAlertDao;
import com.zzvc.mmps.alert.model.AlertBaseEntity;
import com.zzvc.mmps.alert.model.ServerAlert;
import com.zzvc.mmps.alert.util.AlertConstants;
import com.zzvc.mmps.dao.ServerDao;
import com.zzvc.mmps.model.Server;

public class ScheduleServiceServerAlertMailerImpl extends ScheduleServiceAlertMailerSupport {
	
	@Resource
	private AlertConfigDao alertConfigDao;
	
	@Resource
	private ServerDao serverDao;
	
	@Resource
	private ServerAlertDao serverAlertDao;
	
	private int minutesBeforeServerFault;

	@Override
	public void init() {
		super.init();
		
		try {
			minutesBeforeServerFault = Integer.parseInt(alertConfigDao.getConfig(AlertConstants.CFG_MINUTES_BEFORE_SERVER_FAULT));
		} catch (Exception e) {
			minutesBeforeServerFault = AlertConstants.DEFAULT_MINUTES_BEFORE_SERVER_FAULT;
		}
		infoMessage("mailer.config.parameter", "CFG_MINUTES_BEFORE_SERVER_FAULT", minutesBeforeServerFault);
	}

	@Override
	protected Collection findActiveAlert() {
		return serverAlertDao.findActiveAlert();
	}

	@Override
	protected Object findRelatedEntity(AlertBaseEntity alert) {
		Server server = null;
		try {
			server = serverDao.findByCode(((ServerAlert) alert).getCode()).get(0);
		} catch (IndexOutOfBoundsException e) {
		}
		return server;
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
