package ru.msas.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.msas.entity.*;
import ru.msas.repo.*;
import ru.msas.exceptions.*;
import java.util.*;

@Service
public class InstanceService {

    @Autowired
    TppProductRepo tppProduct;

    @Autowired
    AgreementRepo agreement;

    @Autowired
    TppRefProductClassRepo tppProductClass;

    @Autowired
    TppRefProductRegisterTypeRepo tppRefProductRegisterType;

    public ru.msas.Model Create(ru.msas.Model model) {

        Map<String, Object> rqMap = model.getRqMap();
        String sErrorMessage;
        Object tObj;

        tObj = rqMap.get("instanceId");
        String sInstanceId = (String) tObj;

        //Шаг 2.
        if (tObj == null || sInstanceId.isEmpty()) {
            model.setInstanceExists(false);

            //Шаг 1.1
            //Ищем повторы в таблице tppProduct
            //Ищем строки tppPproduct.number == Request.Body.ContractNumber
            String contractNumber = (String) rqMap.get("contractNumber");
            int iCnt = tppProduct.findByNumber(contractNumber);
            //Если строки есть, значит имеются повторы, то return 400
            if (iCnt > 0) {
                sErrorMessage = "Repeated record in tppProduct for contractNumber = " + contractNumber + " Count repeated records is " + iCnt;
                throw new BadRequestException(sErrorMessage);
            }

            //Шаг 1.2
            //Проверка таблицы agreement на дубли
            //Ищем строки agreement.number == Request.Body.Arrangement[N].Number
            List<String> lnumberDs = new ArrayList<>();
            for (Map arrangementParams : model.getAlArrangement()) {
                String arrangeNumber = (String) arrangementParams.get("Number");
                lnumberDs.add(arrangeNumber);
            }
            Integer agreementNumberCount = agreement.CountByNumberIn(lnumberDs);
            //Если строки есть, значит имеются повторы, то return 400
            if (agreementNumberCount > 0) {
                sErrorMessage = "Repeated record in agreement table for numbers = " + lnumberDs.toString() + " Count repeated records is " + agreementNumberCount;
                throw new BadRequestException(sErrorMessage);
            }

            //Шаг 1.3
            //Поиск связанных записей в Каталоге Типа регистра
            String productCode = (String) rqMap.get("productCode");
            //Ищем строки Request.Body.ProductCode == tppRefProductClass.value

            List<TppRefProductClass> lProductCode = tppProductClass.findByValue(productCode);
            if (lProductCode.size() == 0) {
                sErrorMessage = "Not fond records in tppRefProductClass for productcode = " + productCode;
                throw new NotFoundException(sErrorMessage);
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
                sErrorMessage = "Not fond records where tppRefProductRegisterType.accountType = Clients";
                throw new NotFoundException(sErrorMessage);
            }

            model.setlProductRegistertType(lProductRegistertType);

            return model;
        } else {
            //************************************* По Instance not null **********************************************
            model.setInstanceExists(true);
            //Шаг 2.1
            //Проверка tppProduct на ЭП по tppProduct.id == Request.Body.instanceid
            Long instanceId = Long.parseLong(sInstanceId);
            Optional<TppProduct> oProduct = tppProduct.findById(instanceId);
            if (oProduct.isEmpty()) {
                //Если запись не найдена, то return 404 Экземплар продуктс с параметром instance не найден
                sErrorMessage = "404/Product object for instance <" + instanceId.toString() + "> not found ";
                throw new NotFoundException(sErrorMessage);
            }

            //Шаг 2.2
            //Проверка agreement на дубли по agreement.number == Request.Body.Arrangement[N].Number
            //************************************************************************
            List<String> lnumberDs = new ArrayList<>();
            for (Map arrangementParams : model.getAlArrangement()) {
                String arrangeNumber = (String) arrangementParams.get("Number");
                lnumberDs.add(arrangeNumber);
            }

            Integer agreementNumberCount = agreement.CountByNumberIn(lnumberDs);
            //Если записи найдены, то return 404  Параметр N Доп.соглашения уже существует..
            if (agreementNumberCount > 0) {
                sErrorMessage = "Agreement for parameter <" + lnumberDs.toString() + "> alredy exsits";
                throw new NotFoundException(sErrorMessage);
            }
            //***********************************************************************
            //!!!Шаг 8. Добавить строку в таблицу ДС(agreement),сформировать agreement.id, связать с таблицей ЭП
            Agreement agreementForSave;
            try {
                agreementForSave = new Agreement(instanceId);
            } catch (Exception e) {
                sErrorMessage = "Bad make completed object for class agreement " + e.getMessage();
                throw new SaveAgreementException(sErrorMessage);
            }
            model.setAgreementForSave(agreementForSave);
            return model;
        }
    }
}