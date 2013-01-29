package com.zzvc.mmps.alert.task.impl;

import java.util.Date;

import com.zzvc.mmps.alert.model.AlertBaseEntity;
import com.zzvc.mmps.scheduler.task.SchedulerTask;
import com.zzvc.mmps.task.TaskSupport;

abstract public class ScheduleServiceAlertSupport extends TaskSupport implements SchedulerTask {
	public ScheduleServiceAlertSupport() {
		super();
		pushBundle("AlertResources");
	}

	protected void activeAlert(AlertBaseEntity alert) {
		alert.setActive(true);
		alert.setAriseTime(new Date());
	}
	
	protected void recoverAlert(AlertBaseEntity alert) {
		alert.setActive(false);
		alert.setRecoverTime(new Date());
	}
	
	protected String findTextFilterNullArgs(String key, Object... args) {
		for (int i = 0; i < args.length; i++) {
			if (args[i] == null) {
				args[i] = "N/A";
			}
		}
		return findText(key, args);
	}
}
