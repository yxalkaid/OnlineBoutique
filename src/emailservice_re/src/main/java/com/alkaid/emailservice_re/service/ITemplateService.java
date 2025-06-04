package com.alkaid.emailservice_re.service;

public interface ITemplateService {

    /**
     * 渲染邮件
     * @param templateName
     * @param email
     * @param order
     * @return
     */
    public String renderEmail(String templateName, String email,Object order);
}
