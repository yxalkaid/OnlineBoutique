package com.alkaid.emailservice_re.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
public class TemplateService {

    @Autowired
    private TemplateEngine templateEngine;

    /**
     * 渲染邮件模板
     * @param templateName
     * @param email
     * @param order
     * @return
     */
    public String renderEmail(String templateName, String email, Object order) {

        Context context = new Context();
        context.setVariable("email", email);
        context.setVariable("order", order);
        return templateEngine.process(templateName, context);
    }
}
