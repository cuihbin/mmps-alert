package com.zzvc.mmps.alert.mailer.service.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import com.zzvc.mmps.alert.dao.AlertConfigDao;
import com.zzvc.mmps.alert.dao.PlayerLastingFaultAlertDao;
import com.zzvc.mmps.alert.model.AlertBaseEntity;
import com.zzvc.mmps.alert.model.PlayerLastingFaultAlert;
import com.zzvc.mmps.alert.util.AlertConstants;
import com.zzvc.mmps.dao.PlayerDao;
import com.zzvc.mmps.model.Player;

public class ScheduleServicePlayerLastingFaultAlertMailerImpl extends ScheduleServiceAlertMailerSupport {
	
	@Resource
	private AlertConfigDao alertConfigDao;
	
	@Resource
	private PlayerDao playerDao;
	
	@Resource
	private PlayerLastingFaultAlertDao playerLastingFaultAlertDao;
	
	private int minutesBeforeAlert;

	@Override
	public void init() {
		super.init();
		
		try {
			minutesBeforeAlert = Integer.parseInt(alertConfigDao.getConfig(AlertConstants.CFG_MINUTES_BEFORE_PLAYER_LASTING_FAULT));
		} catch (Exception e) {
			minutesBeforeAlert = AlertConstants.DEFAULT_MINUTES_BEFORE_PLAYER_LASTING_FAULT;
		}
		infoMessage("mailer.config.parameter", "CFG_MINUTES_BEFORE_PLAYER_LASTING_FAULT", minutesBeforeAlert);
	}

	@Override
	protected Collection findActiveAlert() {
		return playerLastingFaultAlertDao.findActiveAlert();
	}

	@Override
	protected Object findRelatedEntity(AlertBaseEntity alert) {
		return playerDao.findByAddress(((PlayerLastingFaultAlert) alert).getAddress());
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
