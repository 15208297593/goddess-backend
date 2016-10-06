package com.goddess.ec.manage.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.beetl.core.BeetlKit;

import com.alibaba.druid.support.json.JSONUtils;
import com.goddess.ec.manage.common.DictKeys;
import com.goddess.ec.manage.constants.AppConstants;
import com.goddess.ec.manage.model.Manufacturer;
import com.goddess.ec.manage.model.Order;
import com.goddess.ec.manage.plugin.PropertiesPlugin;
import com.goddess.ec.manage.tools.ToolSqlXml;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

public class MailService extends BaseService {

    @SuppressWarnings("unused")
    private static Logger log = Logger.getLogger(MailService.class);

    public static final MailService service = new MailService();
    
    public void sendMail(HttpServletRequest request, String orderId, int manufacturerId) throws Exception {
    	Properties props = new Properties();
    	props.load(MailService.class.getClassLoader().getResourceAsStream("mail.properties"));

        Session mailSession = Session.getDefaultInstance(props, new MyAuthenticator(props.getProperty("mail.user"), props.getProperty("mail.password")));
        mailSession.setDebug(true);
        Transport transport = mailSession.getTransport();

        MimeMessage message = new MimeMessage(mailSession);
        message.setSubject("所欲订单："+orderId);
        message.setFrom(new InternetAddress(props.getProperty("mail.from")));
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(getMailAddress(manufacturerId)));
        
        Multipart mp = new MimeMultipart();
        MimeBodyPart mbp = new MimeBodyPart();  
        List<File> attachments = new ArrayList<File>();
        mbp.setContent(renderHtml(request, orderId, attachments), "text/html; charset=utf-8");  
        mp.addBodyPart(mbp);    
        for(File f : attachments){   
            mbp=new MimeBodyPart();  
            FileDataSource fds=new FileDataSource(f); //得到数据源  
            mbp.setDataHandler(new DataHandler(fds)); //得到附件本身并至入BodyPart  
            mbp.setFileName(fds.getName());  //得到文件名同样至入BodyPart  
            mp.addBodyPart(mbp);  
        }    
        message.setContent(mp); //Multipart加入到信件  
        message.setSentDate(new Date());     //设置信件头的发送日期  
        //发送信件  
        message.saveChanges();
        transport.connect();
        transport.sendMessage(message, message.getRecipients(Message.RecipientType.TO));
        transport.close();
    }
    
    @SuppressWarnings("unchecked")
	public String renderHtml(HttpServletRequest request, String orderId, List<File> attachments) throws IOException {
    	
    	Map<String, Object> paras = new HashMap<String, Object>();
    	List<Record> commodities = OrderService.service.getOrderCommodities(orderId);
    	for (Record r : commodities) {
        	Map<String, Object> baseProperties = (Map<String, Object>)JSONUtils.parse(String.format("{%s}", 
        			r.getStr("base_properties") == null ? "" : r.getStr("base_properties")));
        	r.set("base_properties", baseProperties);
        	// 定制属性
        	List<Record> customizePros = CommodityService.service.getCommodityCustomizationProps(r.getInt("user_customization_id"), r.getInt("commodity_id"));
    		// 非定制上架商品的材料属性描述
    		if ((customizePros == null || customizePros.size() == 0) && StringUtils.isNotEmpty(r.getStr("detail_properties"))) {
    	    	Map<String, Object> detailProperties = (Map<String, Object>)JSONUtils.parse(String.format("{%s}", r.getStr("detail_properties")));
    	    	for (String tp : detailProperties.keySet())
    	    		customizePros.add(new Record().set("type_name", tp).set("material_name", detailProperties.get(tp)));
    		}

        	// 商品配饰（内部开格，logo等）
        	List<Record> accessories =  Db.find(ToolSqlXml.getSql("manage.order.getOrderAccs"), r.getInt("order_commodity_id"));
    		// 添加内部开格和定制logo选项；高端定制时，在后台生成订单
    		List<Record> insideSlots = new ArrayList<Record>();
    		List<Record> logoStyles = new ArrayList<Record>();
    		for (Record acc : accessories) {
    			if (AppConstants.ACCESSORY_SLOT.equals(acc.getStr("accessory_type"))) {
    				insideSlots.add(acc);
    			} else if (AppConstants.ACCESSORY_LOGO.equals(acc.getStr("accessory_type"))) {
    				logoStyles.add(acc);
    			}
    		}
    		r.set("inside_slot", insideSlots);
    		r.set("logo_style", logoStyles);
    		r.set("customize_properties", customizePros);
    	}
    	paras.put("order_commodities", commodities);
    	paras.put("imageUrl", request.getAttribute("imageUrl"));
    	paras.put("order", Order.dao.findById(orderId));
    	String template = FileUtils.readFileToString(new File(DictKeys.path_root, PropertiesPlugin.getParamMapValue(DictKeys.resource_root)+"/mail/orderMail.html"), "UTF-8");
    	return BeetlKit.render(template, paras);
    }
    
    private String getMailAddress(int manufacturerId) {
    	return Manufacturer.dao.findById(manufacturerId).getStr("manufacturer_email");
    }
    
    class MyAuthenticator extends Authenticator {
	  private String strUser;
	  private String strPwd;
	  public MyAuthenticator(String user, String password) {
	    this.strUser = user;
	    this.strPwd = password;
	  }
	
	  protected PasswordAuthentication getPasswordAuthentication() {
	    return new PasswordAuthentication(strUser, strPwd);
	  }
}
}
