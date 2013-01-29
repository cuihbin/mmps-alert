package com.zzvc.mmps.alert.mailer.service.impl;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import com.zzvc.mmps.alert.model.AlertBaseEntity;
import com.zzvc.mmps.alert.model.PlayerMassiveFaultAlert;
import com.zzvc.mmps.alert.service.AlertConfigManager;
import com.zzvc.mmps.alert.service.PlayerManager;
import com.zzvc.mmps.alert.service.PlayerMassiveFaultAlertManager;
import com.zzvc.mmps.alert.util.AlertConstants;

public class ScheduleServicePlayerMassiveFaultAlertMailerImpl extends ScheduleServiceAlertMailerSupport {
	
	@Resource
	private AlertConfigManager alertConfigManager;
	
	@Resource
	private PlayerManager playerManager;
	
	@Resource
	private PlayerMassiveFaultAlertManager playerMassiveFaultAlertManager;
	
	private int playerMassiveFaultThreshold;

	@Override
	public void init() {
		super.init();
		
		try {
			playerMassiveFaultThreshold = Integer.parseInt(alertConfigManager.getConfig(AlertConstants.CFG_PLAYER_MASSIVE_FAULT_THRESHOLD));
		} catch (Exception e) {
			playerMassiveFaultThreshold = getDefaultPlayerMassiveFaultThreshold();
		}
		infoMessage("mailer.config.parameter", "CFG_PLAYER_MASSIVE_FAULT_THRESHOLD", playerMassiveFaultThreshold);
	}

	@Override
	protected Collection findActiveAlert() {
		return Arrays.asList(new Object[] {playerMassiveFaultAlertManager.findActiveAlert()});
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
		int numberOfPlayers = playerManager.getAll().size();
		return numberOfPlayers / 2 + numberOfPlayers % 2;
	}
}
