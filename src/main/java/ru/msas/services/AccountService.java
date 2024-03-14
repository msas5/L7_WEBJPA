package ru.msas.services;

import org.springframework.stereotype.Service;
import ru.msas.entity.Account;
import ru.msas.entity.AccountPool;
import ru.msas.entity.TppProductRegister;
import ru.msas.entity.TppRefProductRegisterType;
import ru.msas.enums.AccountBodyFields;
import ru.msas.enums.StatePRegisterEnum;
import ru.msas.repo.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AccountService {

    //@Autowired
    TppRefProductRegisterTypeRepo tppRefProductRegisterType;
    //@Autowired
    TppProductRegisterRepo tppProductRegister;
    //@Autowired
    AccountPoolRepo accountPool;
    //@Autowired
    AccountRepo account;

    public Map<String,Object> Create(ru.msas.Model model){
        Map<String,Object> rqMap = model.getRqMap();
        Map<String,Object> rMessage = new HashMap<String,Object>();
        Object tObj;
        String tStr;


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
            rMessage.put("ErrorCode",(Object) "400");
            return rMessage;
        }
        if (lTppProductRegisterInstance.size() > 0){
            // Если повторы найдены, то return 400 Параметр registryTypeCode существует для ЭП с ИД
            rMessage.clear();
            tStr = "400/Bad request. parameter registryTypeCode exists already for ap and id"+ " sInstanceId = " + sInstanceId + " registryTypeCode = " + registryTypeCode;
            rMessage.put("Error", (Object) tStr ) ;
            rMessage.put("ErrorCode",(Object) "400");
            return rMessage;
        }


        //Шаг 3.
        //Найти запись в tppRefProductRegisterType.value по значению Request.Body.registryTypeCode
        List<TppRefProductRegisterType> lTppRefProductRegisterType = tppRefProductRegisterType.findByValue(registryTypeCode);
        if(lTppRefProductRegisterType.size() == 0){
            //Если совпадений не найдено, то return 404 Код продукта не найден для данного типа регистра
            rMessage.clear();
            tStr = "404/Bad request. Product code <" + registryTypeCode + "> not found for this registry type in table tpp_ref_product_register_type";
            rMessage.put("Error", (Object) tStr ) ;
            rMessage.put("ErrorCode",(Object) "404");
            return rMessage;
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
            rMessage.put("ErrorCode",(Object) "400");
            return rMessage;
        }
        if (tAccountPool.size() ==0){
            rMessage.clear();
            tStr = "404/Error for findByinAccountPoolSize";
            rMessage.put("Error", (Object) tStr + " AccountPool is empty for branch,currency,mdmcode,priority from your request and busy");
            rMessage.put("ErrorCode",(Object) "404");
            return rMessage;
        }

        List<Account> tAccount;
        try{
            tAccount = account.findByAccountPoolIdAndBussyOrderByIdAsc(tAccountPool.get(0).getId(),false);

        } catch (Exception e){
            rMessage.clear();
            tStr = "400/Error for findByinAccount";
            rMessage.put("Error", (Object) tStr + " " + e.getMessage() );
            rMessage.put("ErrorCode",(Object) "400");
            return rMessage;
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
            rMessage.put("ErrorCode",(Object) "400");
            return rMessage;
        }

        try{
            TppProductRegister tppProductRegisterWasSaved = tppProductRegister.save(tppProductRegisterForSave);
        } catch (Exception e){
            rMessage.clear();
            tStr = "400/Error for save tppProductRegister";
            rMessage.put("Error", (Object) tStr + " " + e.getMessage() );
            rMessage.put("ErrorCode",(Object) "400");
            return rMessage;
        }

        return rqMap;
    }
}
