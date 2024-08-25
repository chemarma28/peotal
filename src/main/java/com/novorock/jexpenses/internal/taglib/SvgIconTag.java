package com.novorock.jexpenses.internal.taglib;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

public class SvgIconTag extends TagSupport {
    private int size;
    private String name;

    public void setSize(int size) {
        this.size = size;
    }

    public int getSize() {
        return size;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public int doStartTag() throws JspException {
        JspWriter out = pageContext.getOut();

        String contextPath = pageContext.getServletContext().getContextPath();
        String url = String.format("%s/resources/%s-%s.svg", contextPath, size, name);
        String style = String.format("mask-image:url('%s');", url);

        try {
            out.write(String.format("<span class=\"icon-%s\" style=\"%s\"></span>", size, style));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return SKIP_BODY;
    }
}
