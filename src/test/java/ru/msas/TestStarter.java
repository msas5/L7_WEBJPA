package ru.msas;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


@WebMvcTest(controllers=WebService.class)
public class TestStarter {

    @Autowired
    MockMvc mockMvc;


    @Test
    void contextLoadsInstanceBad() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/corporate-settlement-instance/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"instanceId\":\"10\",\"registryTypeCode\":\"03.012.002_47533_ComSoLd\"}"
                        ))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }


    @Test
    void contextLoadsAccountBad() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/corporate-settlement-account/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"instanceId\":\"10\",\"registryTypeCode\":\"03.012.002_47533_ComSoLd\"}"
                        ))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void contextLoadsInstanceOkWithoutInstance() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/corporate-settlement-instance/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                                 	"productType":"CMO",
                                                 	"productCode":"03.012.002",
                                                 	"registerType":"123",
                                                 	 "mdmCode":"2020",
                                                 	 "contractNumber":"2828",
                                                 	 "contractDate":"01.01.2025",
                                                 	 "priority":"2",
                                                 	 "interestRatePenalty":"2.2",
                                                 	 "minimalBalance":"3.1",
                                                 	 "thresholdAmount":"3.2",
                                                 	 "accountingDetails":"payment from 01.01.2025" ,
                                                 	  "rateType":"0",
                                                 	  "taxPercentageRate":"3.2",
                                                 	  "technicalOverdraftLimitAmount":"4.2",
                                                 	  "contractId":"128",
                                                 	  "branchCode":"5",
                                                 	  "isoCurrencyCode":"EUR",
                                                 	  "urgencyCode":"00",
                                                 	 "arrangement": [
                                                 	 {
                                                 	  "generalAgreementId":"0",
                                                 	  "number":"1234567",
                                                 	  "openingDate":"01.01.2025"
                                                 	  },
                                                 	  {
                                                 	  "generalAgreementId":"1",
                                                 	  "number":"98765",
                                                 	   "openingDate":"02.01.2025"
                                                 	  }
                                                 	  ]
                                                 }
                                                        
                                                        
                                                        
                                                        
                                """
                        ))
                .andExpect(MockMvcResultMatchers.status().isAccepted());
    }

    @Test
    void contextLoadsInstanceBadRepeatedWithoutInstance() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/corporate-settlement-instance/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                                 	"productType":"CMO",
                                                 	"productCode":"03.012.002",
                                                 	"registerType":"123",
                                                 	 "mdmCode":"2020",
                                                 	 "contractNumber":"2828",
                                                 	 "contractDate":"01.01.2025",
                                                 	 "priority":"2",
                                                 	 "interestRatePenalty":"2.2",
                                                 	 "minimalBalance":"3.1",
                                                 	 "thresholdAmount":"3.2",
                                                 	 "accountingDetails":"payment from 01.01.2025" ,
                                                 	  "rateType":"0",
                                                 	  "taxPercentageRate":"3.2",
                                                 	  "technicalOverdraftLimitAmount":"4.2",
                                                 	  "contractId":"128",
                                                 	  "branchCode":"5",
                                                 	  "isoCurrencyCode":"EUR",
                                                 	  "urgencyCode":"00",
                                                 	 "arrangement": [
                                                 	 {
                                                 	  "generalAgreementId":"0",
                                                 	  "number":"1234567",
                                                 	  "openingDate":"01.01.2025"
                                                 	  },
                                                 	  {
                                                 	  "generalAgreementId":"1",
                                                 	  "number":"98765",
                                                 	   "openingDate":"02.01.2025"
                                                 	  }
                                                 	  ]
                                                 }
                                                        
                                                        
                                                        
                                                        
                                """
                        ))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void contextLoadsInstanceOkWithInstance() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/corporate-settlement-instance/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                                 	"instanceId":"1",
                                                 	"productType":"CMO",
                                                 	"productCode":"03.012.002",
                                                 	"registerType":"123",
                                                 	 "mdmCode":"2020",
                                                 	 "contractNumber":"2828",
                                                 	 "contractDate":"01.01.2025",
                                                 	 "priority":"2",
                                                 	 "interestRatePenalty":"2.2",
                                                 	 "minimalBalance":"3.1",
                                                 	 "thresholdAmount":"3.2",
                                                 	 "accountingDetails":"payment from 01.01.2025" ,
                                                 	  "rateType":"0",
                                                 	  "taxPercentageRate":"3.2",
                                                 	  "technicalOverdraftLimitAmount":"4.2",
                                                 	  "contractId":"128",
                                                 	  "branchCode":"5",
                                                 	  "isoCurrencyCode":"EUR",
                                                 	  "urgencyCode":"00",
                                                 	 "arrangement": [
                                                 	 {
                                                 	  "generalAgreementId":"0",
                                                 	  "number":"1234567",
                                                 	  "openingDate":"01.01.2025"
                                                 	  },
                                                 	  {
                                                 	  "generalAgreementId":"1",
                                                 	  "number":"98765",
                                                 	   "openingDate":"02.01.2025"
                                                 	  }
                                                 	  ]
                                                 }
                                                        
                                                        
                                                        
                                                        
                                """
                        ))
                .andExpect(MockMvcResultMatchers.status().isAccepted());
    }

    @Test
    void contextLoadsInstanceBadRepeatedWithInstance() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/corporate-settlement-instance/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                                    "instanceId":"1",
                                                 	"productType":"CMO",
                                                 	"productCode":"03.012.002",
                                                 	"registerType":"123",
                                                 	 "mdmCode":"2020",
                                                 	 "contractNumber":"2828",
                                                 	 "contractDate":"01.01.2025",
                                                 	 "priority":"2",
                                                 	 "interestRatePenalty":"2.2",
                                                 	 "minimalBalance":"3.1",
                                                 	 "thresholdAmount":"3.2",
                                                 	 "accountingDetails":"payment from 01.01.2025" ,
                                                 	  "rateType":"0",
                                                 	  "taxPercentageRate":"3.2",
                                                 	  "technicalOverdraftLimitAmount":"4.2",
                                                 	  "contractId":"128",
                                                 	  "branchCode":"5",
                                                 	  "isoCurrencyCode":"EUR",
                                                 	  "urgencyCode":"00",
                                                 	 "arrangement": [
                                                 	 {
                                                 	  "generalAgreementId":"0",
                                                 	  "number":"1234567",
                                                 	  "openingDate":"01.01.2025"
                                                 	  },
                                                 	  {
                                                 	  "generalAgreementId":"1",
                                                 	  "number":"98765",
                                                 	   "openingDate":"02.01.2025"
                                                 	  }
                                                 	  ]
                                                 }
                                                        
                                                        
                                                        
                                                        
                                """
                        ))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void contextLoadsAccountOk() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/corporate-settlement-account/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                """
                                        {
                                        "instanceId":"28",
                                         "registryTypeCode":"02.001.005_45343_CoDowFF",
                                          "currencyCode":"800",
                                          "branchCode":"0022",
                                          "priorityCode":"00",
                                           "mdmCode":"15"
                                          }
                                          """
                        ))
                .andExpect(MockMvcResultMatchers.status().isAccepted());
    }

    @Test
    void contextLoadsAccountBadRepeat() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/corporate-settlement-account/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                """
                                        {
                                        "instanceId":"28",
                                         "registryTypeCode":"02.001.005_45343_CoDowFF",
                                          "currencyCode":"800",
                                          "branchCode":"0022",
                                          "priorityCode":"00",
                                           "mdmCode":"15"
                                          }
                                          """
                        ))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
}

