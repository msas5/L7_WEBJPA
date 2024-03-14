package ru.msas;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.msas.entity.*;
import ru.msas.repo.*;


import java.util.*;


@RestController
public class WebService {


    // @Autowired
    TppProductRepo tppProduct;
    // @Autowired
    AgreementRepo agreement;
    //@Autowired
    TppRefProductClassRepo tppProductClass;
    //@Autowired
    TppRefProductRegisterTypeRepo tppRefProductRegisterType;
    //@Autowired
    TppProductRegisterRepo tppProductRegister;
    //@Autowired
    AccountPoolRepo accountPool;
    //@Autowired
    AccountRepo account;
    sealed interface CreateInstanceResponse{};

    record CreateInstanseResponseError() implements CreateInstanceResponse{}
    record ResponseOk() implements CreateInstanceResponse {}
    record RequestFields(){}
    @PostMapping("/corporate-settlement-instance/create")
    public ResponseEntity<Map <String, Object>> corporateSettlementInstanseCreate(@RequestBody Map<String, Object> model){

        Map<String,Object> rqMap = model;
        Map<String,Object> rMessage = new HashMap<String,Object>();
        Object tObj;
        String tStr;



        //Шаг 1.
        for(InstanceBodyFields value: InstanceBodyFields.values()){
            if(value.isRequired == true) {
                tObj = rqMap.get(value.name());
                try{
                    tStr = (String) (tObj);
                } catch( Exception e){
                    rMessage.clear();
                    rMessage.put("Error", (Object) "String = (String)tobj for value '" + value.name()+ "'" + e.getMessage());
                    return ResponseEntity.status(400).contentType(MediaType.APPLICATION_JSON).body(rMessage);
                }
                if ( tObj == null || tStr.isEmpty() ){
                    rMessage.put("Error",(Object)"400/Bad Request. Required Parameter <" + value.name() + "> is Empty");
                    return ResponseEntity.status(400).contentType(MediaType.APPLICATION_JSON).body(rMessage);
                }
            }
        }



        // Валидация arrangement, если присутствует  todo Постараться сосредоточить всю валидация под одним интерфейсом
        Object tObjarrangement = rqMap.get("arrangement");
        ArrayList<Map<String,Object>> alArrangement;


        // Валидация arrangement, если присутствует
        try {
            alArrangement = (ArrayList<Map<String,Object>>)tObjarrangement;
        } catch (Exception e){
            String st = e.getMessage();
            rMessage.clear();
            rMessage.put("Error",(Object)"400/ " + st);
            return ResponseEntity.status(400).contentType(MediaType.APPLICATION_JSON).body(rMessage);
        }
        int iLength = alArrangement.size();
        for (int i = 0; i < iLength;i++){
            Map<String,Object> mArrangementMap = alArrangement.get(i);
            //*******************************************
            // Обработка по текущему mArrangementMap из aArrangementMap
            for(ArrangementBodyFields value: ArrangementBodyFields.values()){

                if(value.isRequired == true){

                    tObj = mArrangementMap.get(value.name());
                    tStr = (String)(tObj);
                    if ( tObj == null || tStr.isEmpty() ){
                        rMessage.clear();
                        rMessage.put("Error",(Object)"400/Bad Request. Required Parameter <" + value.name() + "> is Empty in arrangement array N" + i);
                        return ResponseEntity.status(400).contentType(MediaType.APPLICATION_JSON).body(rMessage);
                    }
                }
            }

            //*******************************************


        }


        tObj = rqMap.get("instanceId");
        String sInstanceId = (String)tObj;



        //Шаг 2.
        if (tObj == null || sInstanceId.isEmpty()) {


            //Шаг 1.1
            //Ищем повторы в таблице tppProduct
            //Ищем строки tppPproduct.number == Request.Body.ContractNumber
            String contractNumber = (String) rqMap.get("contractNumber");
            int iCnt = tppProduct.findByNumber(contractNumber);
            //Если строки есть, значит имеются повторы, то return 400
            if (iCnt > 0) {

                rMessage.clear();
                rMessage.put("Error", (Object) "400/Bad Request. Repeated record in tppProduct for contractNumber = " + contractNumber + " Count repeated records is " + iCnt);
                return ResponseEntity.status(400).contentType(MediaType.APPLICATION_JSON).body(rMessage);
            }


            //Шаг 1.2
            //Проверка таблицы agreement на дубли
            //Ищем строки agreement.number == Request.Body.Arrangement[N].Number
            List<String> lnumberDs = new ArrayList<>();
            for (Map arrangementParams : alArrangement) {
                String arrangeNumber = (String) arrangementParams.get("Number");
                lnumberDs.add(arrangeNumber);
            }
            Integer agreementNumberCount = agreement.CountByNumberIn(lnumberDs);
            //Если строки есть, значит имеются повторы, то return 400
            if (agreementNumberCount > 0) {
                rMessage.clear();
                rMessage.put("Error", (Object) "400/Bad Request. Repeated record in agreement table for numbers = " + lnumberDs.toString() + " Count repeated records is " + agreementNumberCount);
                return ResponseEntity.status(400).contentType(MediaType.APPLICATION_JSON).body(rMessage);
            }


            //Шаг 1.3
            //Поиск связанных записей в Каталоге Типа регистра
            String productCode = (String) rqMap.get("productCode");
            //Ищем строки Request.Body.ProductCode == tppRefProductClass.value

            List<TppRefProductClass> lProductCode = tppProductClass.findByValue(productCode);
            if (lProductCode.size() == 0) {
                return ResponseEntity.status(404).contentType(MediaType.APPLICATION_JSON).body(rqMap);
            }
            //Среди найденных строк отобрать те,у которых tppRefProductRegisterType.accountType = "Клиентский"
            List<TppRefProductClass> lProductCodeClient = new ArrayList<>();
            List<TppRefProductRegisterType> lProductRegistertType = new ArrayList<>();
            for (TppRefProductClass rowProductClass : lProductCode) {
                List<TppRefProductRegisterType> lTempProductRegistertType = tppRefProductRegisterType.findByProductClassCodeAndAccountType(rowProductClass.getValue(), "Клиентский");
                if (lTempProductRegistertType.size() > 0) {
                    // Среди найденных строк отобрали те,у которых tppRefProductRegisterType.accountType = "Клиентский"
                    lProductCodeClient.add(rowProductClass);
                    //Если записи найдены, то запомнить registerType для добавления в tppProductRegistry
                    for (TppRefProductRegisterType tempProductRegistertType : lTempProductRegistertType) {
                        lProductRegistertType.add(tempProductRegistertType);
                    }
                }
            }

            //Если записей не найдено, то return 404
            if (lProductCodeClient.size() == 0) {
                return ResponseEntity.status(404).contentType(MediaType.APPLICATION_JSON).body(rqMap);
            }


            //todo Необходимо сделать транзакцию с записываемыми далее двумя таблицами
            //Шаг 1.4
            //Добавить строку в tppProduct, согласно Request.Body,Сформировать новый ИД ЭП tppProduct.id
            TppProduct tppProductForSave;
            try {
                tppProductForSave = new TppProduct(rqMap);
            } catch (Exception e) {
                rMessage.clear();
                tStr = "400/Bad make completed object for class TppProduct " + e.getMessage();
                rMessage.put("Error", (Object) tStr);
                return ResponseEntity.status(400).contentType(MediaType.APPLICATION_JSON).body(rMessage);
            }
            TppProduct tppProductWasSaved;
            try {
                tppProductWasSaved = tppProduct.save(tppProductForSave);
            } catch (Exception e) {
                rMessage.clear();
                tStr = "400/Bad save completed object for class TppProduct " + e.getMessage();
                rMessage.put("Error", (Object) tStr);
                return ResponseEntity.status(400).contentType(MediaType.APPLICATION_JSON).body(rMessage);
            }


            //Шаг 1.5
            sInstanceId = tppProductWasSaved.getId();
            rqMap.put("instanceId", (Object) sInstanceId);

            TppRefProductRegisterType tRefProductRegisterType  = lProductRegistertType.get(0);
            rqMap.put("type",tRefProductRegisterType.getValue());
            TppProductRegister tppProductRegisterForSave;
            try {
                tppProductRegisterForSave = new TppProductRegister(rqMap);
            } catch (Exception e) {
                rMessage.clear();
                tStr = "400/Bad make completed object for class tppProductRegister " + e.getMessage();
                rMessage.put("Error", (Object) tStr);
                return ResponseEntity.status(400).contentType(MediaType.APPLICATION_JSON).body(rMessage);
            }
            //Добавить в таблицу ПР tppProductRegistry строки id, productId,type,account_id,currencyCode,state
            try{
                TppProductRegister tppProductRegisterWasSaved = tppProductRegister.save(tppProductRegisterForSave);
            } catch (Exception e){
                rMessage.clear();
                tStr = "400/Bad save completed object for class tppProductRegister " + e.getMessage();
                rMessage.put("Error", (Object) tStr);
                return ResponseEntity.status(400).contentType(MediaType.APPLICATION_JSON).body(rMessage);
            }

            rMessage.clear();
            tStr = "200 All Good!!! ";
            rMessage.put("Ok", (Object) tStr ) ;
            return ResponseEntity.status(200).contentType(MediaType.APPLICATION_JSON).body(rMessage);


        } else {
            //************************************* По Instance not null **********************************************
            //Шаг 2.1
            //Проверка tppProduct на ЭП по tppProduct.id == Request.Body.instanceid
            Long instanceId = Long.parseLong(sInstanceId);
            Optional<TppProduct> oProduct = tppProduct.findById(instanceId);
            if (oProduct.isEmpty()){
                //Если запись не найдена, то return 404 Экземплар продуктс с параметром instance не найден
                rMessage.clear();
                tStr = "404/Product object for instance <" + instanceId.toString() + "> not found ";
                rMessage.put("Error", (Object) tStr);
                return ResponseEntity.status(404).contentType(MediaType.APPLICATION_JSON).body(rMessage);
            }

            //Шаг 2.2
            //Проверка agreement на дубли по agreement.number == Request.Body.Arrangement[N].Number
            //************************************************************************
            List<String> lnumberDs = new ArrayList<>();
            for (Map arrangementParams : alArrangement) {
                String arrangeNumber = (String) arrangementParams.get("Number");
                lnumberDs.add(arrangeNumber);
            }

            Integer agreementNumberCount = agreement.CountByNumberIn(lnumberDs);
            //Если записи найдены, то return 404  Параметр N Доп.соглашения уже существует..
            if (agreementNumberCount > 0){
                rMessage.clear();
                tStr = "404/Agrrement for parameter <" + lnumberDs.toString() + "> alredy exsits. ";
                rMessage.put("Error", (Object) tStr);
                return ResponseEntity.status(404).contentType(MediaType.APPLICATION_JSON).body(rMessage);
            }
            //***********************************************************************
            //!!!Шаг 8. Добавить строку в таблицу ДС(agreement),сформировать agreement.id, связать с таблицей ЭП
            Agreement agreementForSave;
            try {
                agreementForSave = new Agreement(instanceId);
            } catch (Exception e){
                rMessage.clear();
                tStr = "400/Bad make completed object for class agreement " + e.getMessage();
                rMessage.put("Error", (Object) tStr);
                return ResponseEntity.status(400).contentType(MediaType.APPLICATION_JSON).body(rMessage);
            }
            try {
                Agreement agreementWasSaved = agreement.save(agreementForSave);
            } catch (Exception e){
                rMessage.clear();
                tStr = "400/Bad save completed object for class agreement " + e.getMessage();
                rMessage.put("Error", (Object) tStr);
                return ResponseEntity.status(400).contentType(MediaType.APPLICATION_JSON).body(rMessage);
            }
        }
        //Отправить данные в систему источник запроса на создание экземпляра продукта
        rMessage.clear();
        tStr = "200/ok ";
        rMessage.put("Ok", (Object) tStr ) ;
        return ResponseEntity.status(200).contentType(MediaType.APPLICATION_JSON).body(rMessage);
    }


    @PostMapping("/corporate-settlement-account/create")
    public ResponseEntity<Map <String, Object>>  corporateSettlementAccountCreate(@RequestBody Map<String, Object> model){

        Map<String,Object> rqMap = model;
        Map<String,Object> rMessage = new HashMap<String,Object>();
        Object tObj;
        String tStr;


        //Шаг 1.
        for(AccountBodyFields value: AccountBodyFields.values()){
            if(value.isRequired == true) {
                tObj = rqMap.get(value.name());
                try{
                    tStr = (String) (tObj);
                } catch( Exception e){
                    rMessage.clear();
                    rMessage.put("Error", (Object) "String = (String)tobj for value '" + value.name()+ "'" + e.getMessage());
                    return ResponseEntity.status(400).contentType(MediaType.APPLICATION_JSON).body(rMessage);
                }
                if ( tObj == null || tStr.isEmpty() ){
                    rMessage.put("Error",(Object)"400/Bad Request. Required Parameter <" + value.name() + "> is Empty");
                    return ResponseEntity.status(400).contentType(MediaType.APPLICATION_JSON).body(rMessage);
                }
            }
        }




        //Шаг 2.
        //Проверка таблицы ПР tppProductRegister на дубли по tppProductRegister.productId == Request.Body.instanceId
        //У результата отобрать tppProductRegister.type=Request.Body.registryTypeCode.
        String sInstanceId = (String)rqMap.get("instanceId");
        Long instanceId = Long.parseLong(sInstanceId);
        String registryTypeCode = (String) rqMap.get("registryTypeCode");



        List<TppProductRegister> lTppProductRegisterInstance;
        try {
            lTppProductRegisterInstance = tppProductRegister.findByProductIdAndType(instanceId, registryTypeCode);
        } catch (Exception e) {
            rMessage.clear();
            tStr = "400/Error for findByProductIdAndType";
            rMessage.put("Error", (Object) tStr + " sInstanceId = " + sInstanceId + " registryTypeCode = " + registryTypeCode + " " + e.getMessage() );
            return ResponseEntity.status(400).contentType(MediaType.APPLICATION_JSON).body(rMessage);
        }
        if (lTppProductRegisterInstance.size() > 0){
            // Если повторы найдены, то return 400 Параметр registryTypeCode существует для ЭП с ИД
            rMessage.clear();
            tStr = "400/Bad request. parameter registryTypeCode exists already for ap and id"+ " sInstanceId = " + sInstanceId + " registryTypeCode = " + registryTypeCode;
            rMessage.put("Error", (Object) tStr ) ;
            return ResponseEntity.status(400).contentType(MediaType.APPLICATION_JSON).body(rMessage);
        }


        //Шаг 3.
        //Найти запись в tppRefProductRegisterType.value по значению Request.Body.registryTypeCode
        List<TppRefProductRegisterType> lTppRefProductRegisterType = tppRefProductRegisterType.findByValue(registryTypeCode);
        if(lTppRefProductRegisterType.size() == 0){
            //Если совпадений не найдено, то return 404 Код продукта не найден для данного типа регистра
            rMessage.clear();
            tStr = "404/Bad request. Product code <" + registryTypeCode + "> not found for this registry type in table tpp_ref_product_register_type";
            rMessage.put("Error", (Object) tStr ) ;
            return ResponseEntity.status(404).contentType(MediaType.APPLICATION_JSON).body(rMessage);

        }

        //Шаг 4.
        //Сформировать новый продуктовый регистр и записать его в БД
        //Найти значение номера счета по параметрам branchCode,currencyCode,mdbCode,priorityCode,registryTypeCode в таблице accoutPool

        String branchCode = (String)rqMap.get("branchCode");
        String currencyCode = (String)rqMap.get("currencyCode");
        String mdmCode = (String)rqMap.get("mdmCode");
        String priorityCode = (String)rqMap.get("priorityCode");
        List<AccountPool> tAccountPool;
        //Номер счета берется первый из пула.
        try {
            tAccountPool = accountPool.findTopByBranchCodeAndCurrencyCodeAndMdmCodeAndPriorityCodeAndRegistryTypeCodeOrderByIdAsc(branchCode, currencyCode, mdmCode, priorityCode, registryTypeCode);
        } catch (Exception e){
            rMessage.clear();
            tStr = "400/Error for findByinAccountPool";
            rMessage.put("Error", (Object) tStr + " " + e.getMessage() );
            return ResponseEntity.status(400).contentType(MediaType.APPLICATION_JSON).body(rMessage);
        }
        if (tAccountPool.size() ==0){
            rMessage.clear();
            tStr = "404/Error for findByinAccountPoolSize";
            rMessage.put("Error", (Object) tStr + " AccountPool is empty for branch,currency,mdmcode,priority from your request and busy");
            return ResponseEntity.status(404).contentType(MediaType.APPLICATION_JSON).body(rMessage);
        }

        List<Account> tAccount;
        try{
            tAccount = account.findByAccountPoolIdAndBussyOrderByIdAsc(tAccountPool.get(0).getId(),false);

        } catch (Exception e){
            rMessage.clear();
            tStr = "400/Error for findByinAccount";
            rMessage.put("Error", (Object) tStr + " " + e.getMessage() );
            return ResponseEntity.status(400).contentType(MediaType.APPLICATION_JSON).body(rMessage);
        }

        //Сформировать новый продуктовый регистр и записать его в БД
        TppProductRegister tppProductRegisterForSave;
        try {
            tppProductRegisterForSave = new TppProductRegister(rqMap);
            tppProductRegisterForSave.setType(registryTypeCode);
            tppProductRegisterForSave.setAccountNumber(tAccount.get(0).getAccountNumber());
            tppProductRegisterForSave.setAccount(tAccount.get(0).getId());
            tppProductRegisterForSave.setCurrencyCode(currencyCode);
            tppProductRegisterForSave.setState(StatePRegisterEnum.OPENNED.name());
        } catch (Exception e){
            rMessage.clear();
            tStr = "400/Error for make tppProductRegister";
            rMessage.put("Error", (Object) tStr + " " + e.getMessage() );
            return ResponseEntity.status(400).contentType(MediaType.APPLICATION_JSON).body(rMessage);
        }

        try{
            TppProductRegister tppProductRegisterWasSaved = tppProductRegister.save(tppProductRegisterForSave);
        } catch (Exception e){
            rMessage.clear();
            tStr = "400/Error for save tppProductRegister";
            rMessage.put("Error", (Object) tStr + " " + e.getMessage() );
            return ResponseEntity.status(400).contentType(MediaType.APPLICATION_JSON).body(rMessage);
        }

        //Отправить данные в систему источник запроса на создание продуктового регистра
        rMessage.clear();
        tStr = "200/Ok ";
        rMessage.put("Ok", (Object) tStr);
        return ResponseEntity.status(200).contentType(MediaType.APPLICATION_JSON).body(rMessage);

    }
}
