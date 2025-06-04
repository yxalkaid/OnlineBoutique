package com.alkaid.emailservice_re.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.alkaid.emailservice_re.service.ITemplateService;

@Service
public class TemplateServiceImpl implements ITemplateService {

    @Autowired
    private TemplateEngine templateEngine;

    @Override
    public String renderEmail(String templateName, String email, String order) {
        Context context = new Context();
        context.setVariable("email", email);
        context.setVariable("order", order);
        return templateEngine.process(templateName, context);
    }
}
