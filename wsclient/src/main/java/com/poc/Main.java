package com.poc;

import io.spring.guides.gs_producing_web_service.GetCountryRequest;
import io.spring.guides.gs_producing_web_service.GetCountryResponse;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.client.core.WebServiceMessageCallback;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.soap.SoapMessage;

import javax.xml.transform.TransformerException;
import java.io.IOException;

public class Main {
    public static void main(String... args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("spring-beans.xml");
        WebServiceTemplate webServiceTemplateCountries = (WebServiceTemplate)context.getBean("webServiceTemplateCountries");

        GetCountryRequest getCountryRequest = new GetCountryRequest();
        getCountryRequest.setName("Spain");
        GetCountryResponse getCountryResponse = (GetCountryResponse) webServiceTemplateCountries.marshalSendAndReceive(getCountryRequest,
                new WebServiceMessageCallback() {
                    @Override
                    public void doWithMessage(WebServiceMessage webServiceMessage) throws IOException, TransformerException {
                        ((SoapMessage) webServiceMessage).setSoapAction("getCountry");
                    }
                });
        System.out.println(getCountryResponse);
    }
}
