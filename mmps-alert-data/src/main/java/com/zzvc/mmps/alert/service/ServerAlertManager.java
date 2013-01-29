package com.zzvc.mmps.alert.service;

import java.util.List;

import org.appfuse.service.GenericManager;

import com.zzvc.mmps.alert.model.ServerAlert;

public interface ServerAlertManager extends GenericManager<ServerAlert, Long> {
	List<ServerAlert> findActiveAlert();
}
