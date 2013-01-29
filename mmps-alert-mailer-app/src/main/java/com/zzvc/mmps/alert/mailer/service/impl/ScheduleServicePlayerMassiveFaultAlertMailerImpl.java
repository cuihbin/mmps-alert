package com.zzvc.mmps.alert.mailer.service.impl;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import com.zzvc.mmps.alert.dao.AlertConfigDao;
import com.zzvc.mmps.alert.dao.PlayerMassiveFaultAlertDao;
import com.zzvc.mmps.alert.model.AlertBaseEntity;
import com.zzvc.mmps.alert.model.PlayerMassiveFaultAlert;
import com.zzvc.mmps.alert.util.AlertConstants;
import com.zzvc.mmps.dao.PlayerDao;

public class ScheduleServicePlayerMassiveFaultAlertMailerImpl extends ScheduleServiceAlertMailerSupport {
	
	@Resource
	private AlertConfigDao alertConfigDao;
	
	@Resource
	private PlayerDao playerDao;
	
	@Resource
	private PlayerMassiveFaultAlertDao playerMassiveFaultAlertDao;
	
	private int playerMassiveFaultThreshold;

	@Override
	public void init() {
		super.init();
		
		try {
			playerMassiveFaultThreshold = Integer.parseInt(alertConfigDao.getConfig(AlertConstants.CFG_PLAYER_MASSIVE_FAULT_THRESHOLD));
		} catch (Exception e) {
			playerMassiveFaultThreshold = getDefaultPlayerMassiveFaultThreshold();
		}
		infoMessage("mailer.config.parameter", "CFG_PLAYER_MASSIVE_FAULT_THRESHOLD", playerMassiveFaultThreshold);
	}

	@Override
	protected Collection findActiveAlert() {
		return Arrays.asList(new Object[] {playerMassiveFaultAlertDao.findActiveAlert()});
	}

	@Override
	protected Object findRelatedEntity(AlertBaseEntity alert) {
		return null;
	}

	@Override
	protected String getAlertKeyWord(AlertBaseEntity alert) {
		return String.valueOf(((PlayerMassiveFaultAlert) alert).getFaultPlayers());
	}

	@Override
	protected Map<String, Object> getTempleteRootMap(AlertBaseEntity alert) {
		Map<String, Object> rootMap = new HashMap<String, Object>();
		rootMap.put("count", getAlertKeyWord(alert));
		return rootMap;
	}
	
	private int getDefaultPlayerMassiveFaultThreshold() {
		int numberOfPlayers = playerDao.findAll().size();
		return numberOfPlayers / 2 + numberOfPlayers % 2;
	}
}
