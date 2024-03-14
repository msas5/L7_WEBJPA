package ru.msas;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.msas.services.AccountService;
import ru.msas.services.InstanceService;
import java.util.*;
import static java.lang.Integer.parseInt;

@RestController
public class WebController {

    @Autowired
    DataReader dataReader;
    @Autowired
    CheckerInstance checkerInstance;
    @Autowired
    CheckerInstance checkerAccount;
    AccountService accountService;
    InstanceService instanceService;

    @PostMapping("/corporate-settlement-instance/create")
    public ResponseEntity<Map <String, Object>> corporateSettlementInstanseCreate(@RequestBody Map<String, Object> dtoRequest){
        Model model = dataReader.get();
        model.setRqMap(dtoRequest);
        model = checkerInstance.apply(model);

        Map<String,Object> rMap = instanceService.Create(model);
        Map<String,Object> rMessage = new HashMap<String,Object>();
        String sErrorCode = (String) rMap.get("ErrorCode");
        if (!sErrorCode.isEmpty()) {
            String sError = (String )rMap.get("Error");
            int iErrorCode = parseInt(sErrorCode);
            rMessage.put("Error", (Object) sError);
            return ResponseEntity.status(iErrorCode).contentType(MediaType.APPLICATION_JSON).body(rMessage);
        }

            //Отправить данные в систему источник запроса на создание экземпляра продукта
        rMessage.clear();
        String tStr = "200/ok ";
        rMessage.put("Ok", (Object) tStr ) ;
        return ResponseEntity.status(200).contentType(MediaType.APPLICATION_JSON).body(rMessage);
    }

    @PostMapping("/corporate-settlement-account/create")
    public ResponseEntity<Map <String, Object>>  corporateSettlementAccountCreate(@RequestBody Map<String, Object> dtoRequest){
        Model model = dataReader.get();
        model.setRqMap(dtoRequest);
        model = checkerAccount.apply(model);


       Map<String,Object> rMap = accountService.Create(model);

       Map<String,Object> rMessage = new HashMap<String,Object>();
       String sErrorCode = (String)rMap.get("ErrorCode");
       if (!sErrorCode.isEmpty()){
           String sError = (String)rMap.get("Error");
           int iErrorCode = parseInt(sErrorCode);
           rMessage.put("Error", (Object) sError);
           return ResponseEntity.status(iErrorCode).contentType(MediaType.APPLICATION_JSON).body(rMessage);
       }

        //Отправить данные в систему источник запроса на создание продуктового регистра
        rMessage.clear();
        String tStr = "200/Ok ";
        rMessage.put("Ok", (Object) tStr);
        return ResponseEntity.status(200).contentType(MediaType.APPLICATION_JSON).body(rMessage);
    }
}