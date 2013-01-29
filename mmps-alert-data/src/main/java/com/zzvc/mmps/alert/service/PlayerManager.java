package com.zzvc.mmps.alert.service;

import java.util.List;

import org.appfuse.service.GenericManager;

import com.zzvc.mmps.model.Player;

public interface PlayerManager extends GenericManager<Player, Long> {
	Player findByAddress(String address);
	List<Player> findByFaultLasting(int minutes);
}
