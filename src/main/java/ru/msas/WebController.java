package ru.msas;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.msas.checker.CheckerInstance;
import ru.msas.services.AccountService;
import ru.msas.services.InstanceService;
import ru.msas.writer.WriterAccount;
import ru.msas.writer.WriterInstance;

import java.util.*;
import static java.lang.Integer.parseInt;

@RestController
public class WebController {


    @Autowired
    WriterInstance writerInstance;

    @Autowired
    WriterAccount writerAccount;

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
        model.setError(false);
        model.setRqMap(dtoRequest);

        model = checkerInstance.apply(model);
        model = instanceService.Create(model);
        Map<String,Object> rqMap = model.getRqMap();
        Map<String,Object> rMessage = new HashMap<String,Object>();
        String sErrorCode = (String) rqMap.get("ErrorCode");
        if (!sErrorCode.isEmpty()) {
            model.setError(true);
            String sError = (String )rqMap.get("Error");
            int iErrorCode = parseInt(sErrorCode);
            rMessage.put("Error", (Object) sError);
            return ResponseEntity.status(iErrorCode).contentType(MediaType.APPLICATION_JSON).body(rMessage);
        }

        model = writerInstance.apply(model);


        //Отправить данные в систему источник запроса на создание экземпляра продукта
        rMessage.clear();
        String tStr = "200/ok ";
        rMessage.put("Ok", (Object) tStr ) ;
        return ResponseEntity.status(200).contentType(MediaType.APPLICATION_JSON).body(rMessage);
    }

    @PostMapping("/corporate-settlement-account/create")
    public ResponseEntity<Map <String, Object>>  corporateSettlementAccountCreate(@RequestBody Map<String, Object> dtoRequest){

        Model model = dataReader.get();
        model.setError(false);
        model.setRqMap(dtoRequest);
        model = checkerAccount.apply(model);

       model = accountService.Create(model);
       if (model.getError() == true){
       Map<String,Object> rMessage = new HashMap<String,Object>();
       Map<String,Object> rqMap  = model.getRqMap();
       String sErrorCode = (String)rqMap.get("ErrorCode");
       String sError = (String)rqMap.get("Error");
       int iErrorCode = parseInt(sErrorCode);
       rMessage.put("Error", (Object) sError);
       return ResponseEntity.status(iErrorCode).contentType(MediaType.APPLICATION_JSON).body(rMessage);
       }

        model = writerAccount.apply(model);
        if (model.getError() == true){
            Map<String,Object> rMessage = new HashMap<String,Object>();
            Map<String,Object> rqMap  = model.getRqMap();
            String sErrorCode = (String)rqMap.get("ErrorCode");
            String sError = (String)rqMap.get("Error");
            int iErrorCode = parseInt(sErrorCode);
            rMessage.put("Error", (Object) sError);
            return ResponseEntity.status(iErrorCode).contentType(MediaType.APPLICATION_JSON).body(rMessage);
        }

        //Отправить данные в систему источник запроса на создание продуктового регистра
        Map<String,Object> rMessage = new HashMap<String,Object>();
        rMessage.clear();
        String tStr = "200/Ok ";
        rMessage.put("Ok", (Object) tStr);
        return ResponseEntity.status(200).contentType(MediaType.APPLICATION_JSON).body(rMessage);
    }
}