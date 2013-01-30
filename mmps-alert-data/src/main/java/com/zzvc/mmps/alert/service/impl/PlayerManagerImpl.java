package com.zzvc.mmps.alert.service.impl;

import java.util.Date;
import java.util.List;

import org.appfuse.service.impl.GenericManagerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ObjectRetrievalFailureException;
import org.springframework.stereotype.Service;

import com.zzvc.mmps.alert.service.PlayerManager;
import com.zzvc.mmps.dao.PlayerDao;
import com.zzvc.mmps.model.Player;

@Service("playerManager")
public class PlayerManagerImpl extends GenericManagerImpl<Player, Long> implements PlayerManager {
	private static int MILLISECONDS_OF_MINUTE = 60 * 1000;

	private PlayerDao playerDao;
	
	@Autowired
	public PlayerManagerImpl(PlayerDao playerDao) {
		super(playerDao);
		this.playerDao = playerDao;
	}

	@Override
	public Player findByAddress(String address) {
		try {
			return playerDao.findByAddress(address);
		} catch (ObjectRetrievalFailureException e) {
			return null;
		}
	}

	@Override
	public List<Player> findByFaultLasting(int minutes) {
		return playerDao.findByHeartbeatBefore(new Date(System.currentTimeMillis() - minutes * MILLISECONDS_OF_MINUTE));
	}
}
