package com.zzvc.mmps.alert.service;

import java.util.List;

import org.appfuse.service.GenericManager;

import com.zzvc.mmps.alert.model.PlayerAlert;

public interface PlayerAlertManager extends GenericManager<PlayerAlert, Long> {
	List<PlayerAlert> findActiveAlert();
}
