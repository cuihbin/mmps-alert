package com.zzvc.mmps.alert.service;

import org.appfuse.service.GenericManager;

import com.zzvc.mmps.alert.model.PlayerMassiveFaultAlert;

public interface PlayerMassiveFaultAlertManager extends GenericManager<PlayerMassiveFaultAlert, Long> {
	PlayerMassiveFaultAlert findActiveAlert();
}
