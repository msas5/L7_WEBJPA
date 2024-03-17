package ru.msas.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.msas.entity.Account;
import ru.msas.entity.AccountPool;
import ru.msas.entity.TppProductRegister;
import ru.msas.entity.TppRefProductRegisterType;
import ru.msas.enums.StatePRegisterEnum;
import ru.msas.exceptions.*;
import ru.msas.repo.*;
import java.util.List;
import java.util.Map;

@Service
public class AccountService {

    @Autowired
    TppRefProductRegisterTypeRepo tppRefProductRegisterType;

    @Autowired
    TppProductRegisterRepo tppProductRegister;
    @Autowired
    AccountPoolRepo accountPool;

    @Autowired
    AccountRepo account;

    public ru.msas.Model Create(ru.msas.Model model) {

        Map<String, Object> rqMap = model.getRqMap();
        String sErrorMessage;

        //Шаг 2.
        //Проверка таблицы ПР tppProductRegister на дубли по tppProductRegister.productId == Request.Body.instanceId
        //У результата отобрать tppProductRegister.type=Request.Body.registryTypeCode.
        String sInstanceId = (String) rqMap.get("instanceId");
        model.setInstanceExists(true);
        Long instanceId = Long.parseLong(sInstanceId);
        String registryTypeCode = (String) rqMap.get("registryTypeCode");

        List<TppProductRegister> lTppProductRegisterInstance;
        try {
            lTppProductRegisterInstance = tppProductRegister.findByProductIdAndType(instanceId, registryTypeCode);
        } catch (Exception e) {
            sErrorMessage = "Error for findByProductIdAndType InstanceId = " + sInstanceId + " registryTypeCode = " + registryTypeCode + " " + e.getMessage();
            throw new ProcessTppProductRegisterException(sErrorMessage);
        }
        if (lTppProductRegisterInstance.size() > 0) {
            // Если повторы найдены, то return 400 Параметр registryTypeCode существует для ЭП с ИД
            sErrorMessage = "Parameter registryTypeCode exists already for ap and id" + " sInstanceId = " + sInstanceId + " registryTypeCode = " + registryTypeCode;
            throw new BadRequestException(sErrorMessage);
        }

        //Шаг 3.
        //Найти запись в tppRefProductRegisterType.value по значению Request.Body.registryTypeCode
        List<TppRefProductRegisterType> lTppRefProductRegisterType = tppRefProductRegisterType.findByValue(registryTypeCode);
        if (lTppRefProductRegisterType.size() == 0) {
            //Если совпадений не найдено, то return 404 Код продукта не найден для данного типа регистра
            sErrorMessage = "Product code <" + registryTypeCode + "> not found for this registry type in table tpp_ref_product_register_type";
            throw new NotFoundException(sErrorMessage);
        }

        //Шаг 4.
        //Сформировать новый продуктовый регистр и записать его в БД
        //Найти значение номера счета по параметрам branchCode,currencyCode,mdbCode,priorityCode,registryTypeCode в таблице accoutPool
        String branchCode = (String) rqMap.get("branchCode");
        String currencyCode = (String) rqMap.get("currencyCode");
        String mdmCode = (String) rqMap.get("mdmCode");
        String priorityCode = (String) rqMap.get("priorityCode");
        List<AccountPool> tAccountPool;
        //Номер счета берется первый из пула.
        try {
            tAccountPool = accountPool.findTopByBranchCodeAndCurrencyCodeAndMdmCodeAndPriorityCodeAndRegistryTypeCodeOrderByIdAsc(branchCode, currencyCode, mdmCode, priorityCode, registryTypeCode);
        } catch (Exception e) {
            sErrorMessage = "Error for findByinAccountPool " + e.getMessage();
            throw new ProcessAccountPoolException(sErrorMessage);
        }
        if (tAccountPool.size() == 0) {
            sErrorMessage = "Error for findByinAccountPoolSize AccountPool is empty for branch,currency,mdmcode,priority from your request and busy";
            throw new NotFoundException(sErrorMessage);
        }

        List<Account> tAccount;
        try {
            tAccount = account.findByAccountPoolIdAndBussyOrderByIdAsc(tAccountPool.get(0).getId(), false);
        } catch (Exception e) {
            sErrorMessage = "Error for findByinAccount " + e.getMessage();
            throw new ProcessAccountPoolException(sErrorMessage);
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
        } catch (Exception e) {
            sErrorMessage = "Error for make tppProductRegister " + e.getMessage();
            throw new SaveTppProductRegisterException(sErrorMessage);
        }

        model.setTppProductRegisterForSave(tppProductRegisterForSave);
        return model;
    }
}