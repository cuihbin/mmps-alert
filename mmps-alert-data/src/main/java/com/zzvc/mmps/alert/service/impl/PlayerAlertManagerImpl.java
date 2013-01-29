package com.zzvc.mmps.alert.service.impl;

import java.util.List;

import org.appfuse.service.impl.GenericManagerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zzvc.mmps.alert.dao.PlayerAlertDao;
import com.zzvc.mmps.alert.model.PlayerAlert;
import com.zzvc.mmps.alert.service.PlayerAlertManager;

@Service("PlayerAlertManager")
public class PlayerAlertManagerImpl extends GenericManagerImpl<PlayerAlert, Long> implements PlayerAlertManager {
	
	private PlayerAlertDao playerAlertDao;

	@Autowired
	public PlayerAlertManagerImpl(PlayerAlertDao playerAlertDao) {
		super(playerAlertDao);
		this.playerAlertDao = playerAlertDao;
	}

	@Override
	public List<PlayerAlert> findActiveAlert() {
		return playerAlertDao.findActiveAlert();
	}

}
