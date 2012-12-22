package com.zzvc.mmps.alert.mailer.service.impl;

import java.io.IOException;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.mail.internet.MimeUtility;

import org.apache.log4j.Logger;
import org.springframework.mail.MailException;
import org.springframework.mail.MailPreparationException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import com.zzvc.mmps.alert.dao.AlertConfigDao;
import com.zzvc.mmps.alert.model.AlertBaseEntity;
import com.zzvc.mmps.alert.util.AlertConstants;
import com.zzvc.mmps.console.localize.LocalizeUtil;
import com.zzvc.mmps.dao.UniversalDao;
import com.zzvc.mmps.scheduler.task.SchedulerTask;
import com.zzvc.mmps.task.TaskException;
import com.zzvc.mmps.task.TaskSupport;

import freemarker.template.Configuration;
import freemarker.template.TemplateException;

public abstract class ScheduleServiceAlertMailerSupport extends TaskSupport implements SchedulerTask {
	private static Logger logger = Logger.getLogger(ScheduleServiceAlertMailerSupport.class);
	
	@Resource
	private AlertConfigDao alertConfigDao;
	
	@Resource
	protected UniversalDao universalDao;
	
	@Resource
	protected JavaMailSender mailSender;
	
	private String systemName;
	
	private String appName;
	
	private String mailFrom;
	
	private String[] to;
	
	private Configuration configuration;
	
	abstract protected Collection<AlertBaseEntity> findActiveAlert();
	
	abstract protected Object findRelatedEntity(AlertBaseEntity alert);
	
	abstract protected String getAlertKeyWord(AlertBaseEntity alert);
	
	abstract protected Map<String, Object> getTempleteRootMap(AlertBaseEntity alert);
	
	public ScheduleServiceAlertMailerSupport() {
		super();
		pushBundle("AlertMailerResources");
	}

	@Override
	public void init() {
		ResourceBundle mailBundle = ResourceBundle.getBundle("mail");
		
		systemName = alertConfigDao.getConfig(AlertConstants.CFG_SYSTEM_NAME);
		appName = findText("app.name");
		
		infoMessage("alert.mailer.mail.list.loading");
		to = parseMailList(mailBundle.getString("mail.to"));

		configuration = new Configuration();
		configuration.setClassForTemplateLoading(this.getClass(), "/");
		configuration.setDefaultEncoding("UTF-8");
		
		mailFrom = getLabeledMailFrom(mailBundle.getString("mail.default.from"));
		
	}

	@Override
	public void onSchedule() {
		for (AlertBaseEntity alert : findActiveAlert()) {
			if (alert != null && (alert.getMailSend() == null || !alert.getMailSend())) {
				try {
					sendAlertMail(alert);

					alert.setMailSend(true);
					universalDao.save(alert);
					
					infoMessage("alert.mailer.sendmail." + getId(), getAlertKeyWord(alert));
				} catch (MailException e) {
					warnMessage("alert.mailer.error.sendmail");
					break;
				}
			}
		}
	}
	
	private String[] parseMailList(String mailAddresses) {
		List<String> validMailAddresses = new ArrayList<String>();
		Pattern emailAddressPattern = Pattern.compile("^\\w+@(\\w+.)+[a-z]{2,3}$");
		String mailAddressSeparators = "[,;:|\\s]";
		
		for (String email : mailAddresses.split(mailAddressSeparators)) {
			Matcher matcher = emailAddressPattern.matcher(email);
			if (matcher.find()) {
				validMailAddresses.add(email);
			}
		}
		
		if (validMailAddresses.size() == 0) {
			errorMessage("alert.mailer.warn.invaliduser");
			throw new TaskException("Invalid mail address");
		}

		return validMailAddresses.toArray(new String[validMailAddresses.size()]);
	}
	
	private String getLabeledMailFrom(String mailFrom) {
		try {
			return findText("alert.mailer.mail.from", MimeUtility.encodeText(systemName), mailFrom);
		} catch (UnsupportedEncodingException e) {
			logger.warn("Error encoding mail sender's name", e);
			return mailFrom;
		}
	}
	
	private void sendAlertMail(AlertBaseEntity alert) {
		String subject = findText("alert.mailer.mail.subject." + getId(), systemName, getAlertKeyWord(alert));
		
		Map<String, Object> rawTempleteRootMap = getTempleteRootMap(alert);
		rawTempleteRootMap.put("systemName", systemName);
		rawTempleteRootMap.put("appName", appName);
		rawTempleteRootMap.put("datetime", new Date());
		Map<String, String> rootMap = formatRootMap(rawTempleteRootMap);
		String body = processTemplete(rootMap);
		
		sendMail(subject, body);
	}
	
	private Map<String, String> formatRootMap(Map<String, Object> rawTempleteRootMap) {
		Map<String, String> rootMap = new HashMap<String, String>();
		
		for (String key : rawTempleteRootMap.keySet()) {
			Object value = rawTempleteRootMap.get(key);
			if (value == null) {
				rootMap.put(key, "N/A");
			} else if (value instanceof Date) {
				rootMap.put(key, LocalizeUtil.formatDateTimeMedium((Date) value));
			} else if (value instanceof String) {
				rootMap.put(key, (String) value);
			} else {
				rootMap.put(key, String.valueOf(value));
			}
		}
		
		return rootMap;
	}
	
	private String processTemplete(Map<String, String> rootMap) {
		String templeteName = getId() + ".ftl";
		try {
			StringWriter result = new StringWriter();
			configuration.getTemplate(templeteName).process(rootMap, result);
			return result.toString();
		} catch (IOException e) {
			logger.error("Error reading freemarker templete.", e);
			throw new MailPreparationException("Cannot creating mail body", e);
		} catch (TemplateException e) {
			logger.error("Error processing freemarker templete.", e);
			throw new MailPreparationException("Cannot creating mail body", e);
		} catch (Exception e) {
			logger.error("Unknown freemarker templete error.", e);
			throw new MailPreparationException("Cannot creating mail body", e);
		}
	}
	
	private void sendMail(String subject, String body) {
		SimpleMailMessage mail = new SimpleMailMessage();
		mail.setFrom(mailFrom);
		mail.setTo(to);
		mail.setSubject(subject);
		mail.setText(body);
		
		try {
			mailSender.send(mail);
		} catch (MailException e) {
			logger.error("Cannot sending mail", e);
			throw e;
		}
	}

}
