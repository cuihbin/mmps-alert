package com.zzvc.mmps.alert.service;

import java.util.List;

import org.appfuse.service.GenericManager;

import com.zzvc.mmps.alert.model.PlayerLastingFaultAlert;

public interface PlayerLastingFaultAlertManager extends GenericManager<PlayerLastingFaultAlert, Long> {
	List<PlayerLastingFaultAlert> findActiveAlert();
}
