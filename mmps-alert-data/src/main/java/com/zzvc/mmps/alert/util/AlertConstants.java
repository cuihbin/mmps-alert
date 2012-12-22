package com.zzvc.mmps.alert.util;

public interface AlertConstants {
	String CFG_SYSTEM_NAME = "systemName";
	
	String CFG_MINUTES_BEFORE_PLAYER_FAULT = "minutesBeforePlayerFault";
	String CFG_MINUTES_BEFORE_PLAYER_LASTING_FAULT = "minutesBeforePlayerLastingFault";
	String CFG_PLAYER_MASSIVE_FAULT_THRESHOLD = "playerMassiveFaultThreshold";
	String CFG_MINUTES_BEFORE_SERVER_FAULT = "minutesBeforeServerFault";
	String CFG_MINUTES_BEFORE_JMS_BROKER_FAULT = "minutesBeforeJmsBrokerFault";
	
	int DEFAULT_MINUTES_BEFORE_PLAYER_FAULT = 30;
	int DEFAULT_MINUTES_BEFORE_PLAYER_LASTING_FAULT = 2880;
	int DEFAULT_MINUTES_BEFORE_SERVER_FAULT = 10;
	int DEFAULT_MINUTES_BEFORE_JMS_BROKER_FAULT = 5;
}
