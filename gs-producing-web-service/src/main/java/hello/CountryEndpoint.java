package hello;

import io.spring.guides.gs_producing_web_service.ListFlightsSoapHeaders;
import io.spring.guides.gs_producing_web_service.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import io.spring.guides.gs_producing_web_service.GetCountryRequest;
import io.spring.guides.gs_producing_web_service.GetCountryResponse;
import org.springframework.ws.soap.SoapHeaderElement;
import org.springframework.ws.soap.server.endpoint.annotation.SoapHeader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Unmarshaller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Endpoint
public class CountryEndpoint {


    private static final Logger LOGGER = LoggerFactory.getLogger(CountryEndpoint.class);

    private static final String NAMESPACE_URI = "http://spring.io/guides/gs-producing-web-service";

    private CountryRepository countryRepository;

    @Autowired
    public CountryEndpoint(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getCountryRequest")
    @ResponsePayload
    public GetCountryResponse getCountry(@RequestPayload GetCountryRequest request, @SoapHeader(
            value = "{http://spring.io/guides/gs-producing-web-service}listFlightsSoapHeaders") SoapHeaderElement soapHeaderElement) {
        GetCountryResponse response = new GetCountryResponse();
        response.setCountry(countryRepository.findCountry(request.getName()));


        try {
            // create an unmarshaller
            JAXBContext context = JAXBContext.newInstance(ObjectFactory.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();

            // unmarshal the header from the specified source
            JAXBElement<ListFlightsSoapHeaders> headers =
                    (JAXBElement<ListFlightsSoapHeaders>) unmarshaller
                            .unmarshal(soapHeaderElement.getSource());

            // get the header values
            ListFlightsSoapHeaders requestSoapHeaders = headers.getValue();
            String clientId = requestSoapHeaders.getClientId();
            LOGGER.info(clientId);
        } catch (Exception e) {
            LOGGER.error("error during unmarshalling of the SOAP headers", e);
        }

        return response;
    }
}