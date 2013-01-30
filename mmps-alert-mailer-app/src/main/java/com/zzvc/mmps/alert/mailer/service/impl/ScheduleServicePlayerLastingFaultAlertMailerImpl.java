package com.zzvc.mmps.alert.mailer.service.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import com.zzvc.mmps.alert.model.AlertBaseEntity;
import com.zzvc.mmps.alert.model.PlayerLastingFaultAlert;
import com.zzvc.mmps.alert.service.AlertConfigManager;
import com.zzvc.mmps.alert.service.PlayerLastingFaultAlertManager;
import com.zzvc.mmps.alert.service.PlayerManager;
import com.zzvc.mmps.alert.util.AlertConstants;
import com.zzvc.mmps.model.Player;

public class ScheduleServicePlayerLastingFaultAlertMailerImpl extends ScheduleServiceAlertMailerSupport {
	
	@Resource
	private AlertConfigManager alertConfigManager;
	
	@Resource
	private PlayerManager playerManager;
	
	@Resource
	private PlayerLastingFaultAlertManager playerLastingFaultAlertManager;
	
	private int minutesBeforeAlert;

	@Override
	public void init() {
		super.init();
		
		try {
			minutesBeforeAlert = Integer.parseInt(alertConfigManager.getConfig(AlertConstants.CFG_MINUTES_BEFORE_PLAYER_LASTING_FAULT));
		} catch (Exception e) {
			minutesBeforeAlert = AlertConstants.DEFAULT_MINUTES_BEFORE_PLAYER_LASTING_FAULT;
		}
		infoMessage("alert.mailer.config.parameter", "CFG_MINUTES_BEFORE_PLAYER_LASTING_FAULT", minutesBeforeAlert);
	}

	@Override
	protected Collection findActiveAlert() {
		return playerLastingFaultAlertManager.findActiveAlert();
	}

	@Override
	protected Object findRelatedEntity(AlertBaseEntity alert) {
		return playerManager.findByAddress(((PlayerLastingFaultAlert) alert).getAddress());
	}

	@Override
	protected String getAlertKeyWord(AlertBaseEntity alert) {
		Player player = (Player) findRelatedEntity(alert);
		return player.getName() + "(" + player.getAddress() + ")";
	}

	@Override
	protected Map<String, Object> getTempleteRootMap(AlertBaseEntity alert) {
		Map<String, Object> rootMap = new HashMap<String, Object>();
		Player player = (Player) findRelatedEntity(alert);
		rootMap.put("name", getAlertKeyWord(alert));
		rootMap.put("period", minutesBeforeAlert / 60);
		rootMap.put("lastHeartbeat", player.getLastHeartbeat());
		return rootMap;
	}
}
