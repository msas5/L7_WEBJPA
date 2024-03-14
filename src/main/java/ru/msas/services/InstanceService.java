package ru.msas.services;

import org.springframework.stereotype.Service;
import ru.msas.entity.*;
import ru.msas.enums.ArrangementBodyFields;
import ru.msas.enums.InstanceBodyFields;
import ru.msas.repo.*;

import java.util.*;

@Service
public class InstanceService {
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


    public Map<String, Object> Create(Map<String, Object> model) {
        Map<String, Object> rqMap = model;
        Map<String, Object> rMessage = new HashMap<String, Object>();
        Object tObj;
        String tStr;
//Шаг 1.
        for (InstanceBodyFields value : InstanceBodyFields.values()) {
            if (value.isRequired == true) {
                tObj = rqMap.get(value.name());
                try {
                    tStr = (String) (tObj);
                } catch (Exception e) {
                    rMessage.clear();
                    rMessage.put("Error", (Object) "String = (String)tobj for value '" + value.name() + "'" + e.getMessage());
                    rMessage.put("ErrorCode",(Object) "400");
                    return rMessage;

                }
                if (tObj == null || tStr.isEmpty()) {
                    rMessage.put("Error", (Object) "400/Bad Request. Required Parameter <" + value.name() + "> is Empty");
                    rMessage.put("ErrorCode",(Object) "400");
                    return rMessage;

                }
            }
        }


        // Валидация arrangement, если присутствует  todo Постараться сосредоточить всю валидация под одним интерфейсом
        Object tObjarrangement = rqMap.get("arrangement");
        ArrayList<Map<String, Object>> alArrangement;


        // Валидация arrangement, если присутствует
        try {
            alArrangement = (ArrayList<Map<String, Object>>) tObjarrangement;
        } catch (Exception e) {
            String st = e.getMessage();
            rMessage.clear();
            rMessage.put("Error", (Object) "400/ " + st);
            rMessage.put("ErrorCode",(Object) "400");
            return rMessage;

        }
        int iLength = alArrangement.size();
        for (int i = 0; i < iLength; i++) {
            Map<String, Object> mArrangementMap = alArrangement.get(i);
            //*******************************************
            // Обработка по текущему mArrangementMap из aArrangementMap
            for (ArrangementBodyFields value : ArrangementBodyFields.values()) {

                if (value.isRequired == true) {

                    tObj = mArrangementMap.get(value.name());
                    tStr = (String) (tObj);
                    if (tObj == null || tStr.isEmpty()) {
                        rMessage.clear();
                        rMessage.put("Error", (Object) "400/Bad Request. Required Parameter <" + value.name() + "> is Empty in arrangement array N" + i);
                        rMessage.put("ErrorCode",(Object) "400");
                        return rMessage;

                    }
                }
            }

            //*******************************************


        }


        tObj = rqMap.get("instanceId");
        String sInstanceId = (String) tObj;


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
                rMessage.put("ErrorCode",(Object) "400");
                return rMessage;

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
                rMessage.put("ErrorCode",(Object) "400");
                return rMessage;

            }


            //Шаг 1.3
            //Поиск связанных записей в Каталоге Типа регистра
            String productCode = (String) rqMap.get("productCode");
            //Ищем строки Request.Body.ProductCode == tppRefProductClass.value

            List<TppRefProductClass> lProductCode = tppProductClass.findByValue(productCode);
            if (lProductCode.size() == 0) {
                rMessage.clear();
                rMessage.put("Error", (Object) "404/Bad Request. Not fond records in tppRefProductClass for productcode = " + productCode);
                rMessage.put("ErrorCode",(Object) "404");
                return rMessage;
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
                rMessage.clear();
                rMessage.put("Error", (Object) "404/Bad Request. Not fond records where tppRefProductRegisterType.accountType = Clients");
                rMessage.put("ErrorCode",(Object) "404");
                return rMessage;


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
                rMessage.put("ErrorCode",(Object) "400");
                return rMessage;

            }
            TppProduct tppProductWasSaved;
            try {
                tppProductWasSaved = tppProduct.save(tppProductForSave);
            } catch (Exception e) {
                rMessage.clear();
                tStr = "400/Bad save completed object for class TppProduct " + e.getMessage();
                rMessage.put("Error", (Object) tStr);
                rMessage.put("ErrorCode",(Object) "400");
                return rMessage;

            }


            //Шаг 1.5
            sInstanceId = tppProductWasSaved.getId();
            rqMap.put("instanceId", (Object) sInstanceId);

            TppRefProductRegisterType tRefProductRegisterType = lProductRegistertType.get(0);
            rqMap.put("type", tRefProductRegisterType.getValue());
            TppProductRegister tppProductRegisterForSave;
            try {
                tppProductRegisterForSave = new TppProductRegister(rqMap);
            } catch (Exception e) {
                rMessage.clear();
                tStr = "400/Bad make completed object for class tppProductRegister " + e.getMessage();
                rMessage.put("Error", (Object) tStr);
                rMessage.put("ErrorCode",(Object) "400");
                return rMessage;

            }
            //Добавить в таблицу ПР tppProductRegistry строки id, productId,type,account_id,currencyCode,state
            try {
                TppProductRegister tppProductRegisterWasSaved = tppProductRegister.save(tppProductRegisterForSave);
            } catch (Exception e) {
                rMessage.clear();
                tStr = "400/Bad save completed object for class tppProductRegister " + e.getMessage();
                rMessage.put("Error", (Object) tStr);
                rMessage.put("ErrorCode",(Object) "400");
                return rMessage;

            }

            rMessage.clear();
            tStr = "200 All Good!!! ";
            rMessage.put("Ok", (Object) tStr);
            rMessage.put("ErrorCode",(Object) "200");
            return rMessage;



        } else {
            //************************************* По Instance not null **********************************************
            //Шаг 2.1
            //Проверка tppProduct на ЭП по tppProduct.id == Request.Body.instanceid
            Long instanceId = Long.parseLong(sInstanceId);
            Optional<TppProduct> oProduct = tppProduct.findById(instanceId);
            if (oProduct.isEmpty()) {
                //Если запись не найдена, то return 404 Экземплар продуктс с параметром instance не найден
                rMessage.clear();
                tStr = "404/Product object for instance <" + instanceId.toString() + "> not found ";
                rMessage.put("Error", (Object) tStr);
                rMessage.put("ErrorCode",(Object) "404");
                return rMessage;

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
            if (agreementNumberCount > 0) {
                rMessage.clear();
                tStr = "404/Agrrement for parameter <" + lnumberDs.toString() + "> alredy exsits. ";
                rMessage.put("Error", (Object) tStr);
                rMessage.put("ErrorCode",(Object) "400");
                return rMessage;

            }
            //***********************************************************************
            //!!!Шаг 8. Добавить строку в таблицу ДС(agreement),сформировать agreement.id, связать с таблицей ЭП
            Agreement agreementForSave;
            try {
                agreementForSave = new Agreement(instanceId);
            } catch (Exception e) {
                rMessage.clear();
                tStr = "400/Bad make completed object for class agreement " + e.getMessage();
                rMessage.put("Error", (Object) tStr);
                rMessage.put("ErrorCode",(Object) "400");
                return rMessage;

            }
            try {
                Agreement agreementWasSaved = agreement.save(agreementForSave);
            } catch (Exception e) {
                rMessage.clear();
                tStr = "400/Bad save completed object for class agreement " + e.getMessage();
                rMessage.put("Error", (Object) tStr);
                rMessage.put("ErrorCode",(Object) "400");
                return rMessage;

            }
        }

        return rqMap;
    }
}


