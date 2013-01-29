package com.zzvc.mmps.alert.task.impl;

import java.io.IOException;
import java.util.Date;

import javax.annotation.Resource;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.spring.ActiveMQConnectionFactory;
import org.apache.activemq.transport.TransportListener;
import org.apache.log4j.Logger;

import com.zzvc.mmps.alert.dao.AlertConfigDao;
import com.zzvc.mmps.alert.dao.ServerAlertDao;
import com.zzvc.mmps.alert.model.ServerAlert;
import com.zzvc.mmps.alert.util.AlertConstants;
import com.zzvc.mmps.task.TaskException;

public class ScheduleServieJmsBrokerFaultAlertImpl extends ScheduleServiceAlertSupport implements TransportListener {
	private static Logger logger = Logger.getLogger(ScheduleServieJmsBrokerFaultAlertImpl.class);
	
	private static final String JMS_BROKER_SERVER_CODE = "AMQ-SERVER";
	
	private static final String JMS_BROKER_SERVER_NAME = "JMS Broker";
	
	@Resource
	private AlertConfigDao alertConfigDao;
	
	@Resource
	private ServerAlertDao serverAlertDao;
	
	@Resource
	private ActiveMQConnectionFactory amqConnectionFactory;
	
	private int minutesBeforeJmsBrokerFault;
	
	private Date lastFailTime;
	
	private ServerAlert activeJmsBrokerFaultAlert;

	@Override
	public void init() {
		try {
			minutesBeforeJmsBrokerFault = Integer.parseInt(alertConfigDao.getConfig(AlertConstants.CFG_MINUTES_BEFORE_JMS_BROKER_FAULT));
		} catch (Exception e) {
			minutesBeforeJmsBrokerFault = AlertConstants.DEFAULT_MINUTES_BEFORE_JMS_BROKER_FAULT;
		}
		infoMessage("alert.config.parameter", "CFG_MINUTES_BEFORE_JMS_BROKER_FAULT", minutesBeforeJmsBrokerFault);
		
		activeJmsBrokerFaultAlert = getActiveJmsBrokerAlert();
		if (activeJmsBrokerFaultAlert != null) {
			lastFailTime = activeJmsBrokerFaultAlert.getLastHeartbeat();
		} else {
			lastFailTime = new Date();
		}
		
		try {
			ActiveMQConnection amqConnection = (ActiveMQConnection) amqConnectionFactory.createConnection();
			amqConnection.addTransportListener(this);
		} catch (Exception e) {
			logger.error("Error initializing JMS Broker Fault Alert module", e);
			throw new TaskException("Error initialize JMS Broker Fault alert module");
		}
	}

	@Override
	public void onSchedule() {
		if (activeJmsBrokerFaultAlert != null) {
			return;
		}
		
		if (lastFailTime == null || System.currentTimeMillis() - lastFailTime.getTime() <= minutesBeforeJmsBrokerFault * MILLISECONDS_OF_MINUTE) {
			return;
		}
		
		activeJmsBrokerFaultAlert = createJmsBrokerAlert();
		activeAlert(activeJmsBrokerFaultAlert);
		serverAlertDao.save(activeJmsBrokerFaultAlert);
		
		warn(findTextFilterNullArgs("alert.fault.arising.server", JMS_BROKER_SERVER_NAME, lastFailTime));
	}
	
	@Override
	public void onCommand(Object command) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onException(IOException error) {
		// TODO Auto-generated method stub

	}

	@Override
	public void transportInterupted() {
		if (lastFailTime == null) {
			lastFailTime = new Date();
		}
		
		warn(findTextFilterNullArgs("alert.arising.server.fault", JMS_BROKER_SERVER_NAME, lastFailTime));
	}

	@Override
	public void transportResumed() {
		if (activeJmsBrokerFaultAlert == null) {
			info(findText("alert.jms.broker.started"));
		} else {
			recoverAlert(activeJmsBrokerFaultAlert);
			serverAlertDao.save(activeJmsBrokerFaultAlert);
			activeJmsBrokerFaultAlert = null;
			
			info(findTextFilterNullArgs("alert.recover.server.fault", JMS_BROKER_SERVER_NAME, lastFailTime));
		}
		lastFailTime = null;
	}
	
	private ServerAlert getActiveJmsBrokerAlert() {
		for (ServerAlert alert : serverAlertDao.findActiveAlert()) {
			if (JMS_BROKER_SERVER_CODE.equals(alert.getCode())) {
				return alert;
			}
		}
		return null;
	}
	
	private ServerAlert createJmsBrokerAlert() {
		ServerAlert ariseAlert = new ServerAlert();
		ariseAlert.setCode(JMS_BROKER_SERVER_CODE);
		ariseAlert.setLastHeartbeat(lastFailTime);
		return ariseAlert;
	}
}
