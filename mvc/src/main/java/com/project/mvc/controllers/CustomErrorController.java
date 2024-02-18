package com.project.mvc.controllers;

import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.WebRequest;

import java.util.Map;

@Controller
public class CustomErrorController implements ErrorController {

    private final ErrorAttributes errorAttributes;

    public CustomErrorController(ErrorAttributes errorAttributes) {
        this.errorAttributes = errorAttributes;
    }

    @RequestMapping("/error")
    public String handleError(WebRequest request, Model model) {
        // Retrieve error attributes
        java.util.Map<String, Object> errorAttributesMap = errorAttributes.getErrorAttributes(request, ErrorAttributeOptions.defaults());

        // Add error attributes to the model
        model.addAttribute("errorCode", errorAttributesMap.get("status"));
        model.addAttribute("error", errorAttributesMap.get("error"));
        model.addAttribute("errorMessage", errorAttributesMap.get("message"));
        model.addAttribute("requestId", request.getSessionId());

        // Add your custom logic to determine whether to show requestId
        model.addAttribute("showRequestId", false);

        return "error";
    }
}
