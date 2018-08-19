package com.poc;

import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.client.WebServiceClientException;
import org.springframework.ws.client.support.interceptor.ClientInterceptor;
import org.springframework.ws.context.MessageContext;
import org.springframework.ws.soap.SoapHeader;
import org.springframework.ws.soap.SoapMessage;
import org.springframework.ws.soap.saaj.SaajSoapMessage;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPException;

public class HeaderInterceptor implements ClientInterceptor {

    public boolean handleRequest(MessageContext messageContext) throws WebServiceClientException {
        System.out.println("handleRequest OK");

        WebServiceMessage requestMsg = messageContext.getRequest();
        SoapMessage soapMsg = (SoapMessage) requestMsg;
        SoapHeader soapHeader = soapMsg.getSoapHeader();

        if(soapHeader == null) {
            // throw exception
        }
        // KO !
        /*soapHeader.addHeaderElement(new QName("http://spring.io/guides/gs-producing-web-service","listFlightsSoapHeaders"))
            .setText("<clientId>def456</clientId>");*/

        // ou avec javax.xml
        javax.xml.soap.SOAPMessage sOAPMessage = ((SaajSoapMessage)messageContext.getRequest()).getSaajMessage();
        try {
            javax.xml.soap.SOAPHeader sOAPHeader = sOAPMessage.getSOAPHeader();
            QName listFlightsSoapHeaders = new QName("http://spring.io/guides/gs-producing-web-service","listFlightsSoapHeaders");
            javax.xml.soap.SOAPHeaderElement listFlightsSoapHeadersElement = sOAPHeader.addHeaderElement(listFlightsSoapHeaders);
            QName clientId = new QName("http://spring.io/guides/gs-producing-web-service","clientId");
            javax.xml.soap.SOAPElement clientIdElement = listFlightsSoapHeadersElement.addChildElement((clientId));
            clientIdElement.setTextContent("def456");
        } catch (SOAPException e) {
            e.printStackTrace();
        }

        return true;
    }

    public boolean handleResponse(MessageContext messageContext) throws WebServiceClientException {
        System.out.println("handleResponse OK");
        return true;
    }

    public boolean handleFault(MessageContext messageContext) throws WebServiceClientException {
        System.out.println("handleFault");
        return true;
    }
}
