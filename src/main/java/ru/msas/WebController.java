package ru.msas;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.msas.checker.CheckerAccount;
import ru.msas.checker.CheckerInstance;
import ru.msas.services.AccountService;
import ru.msas.services.InstanceService;
import ru.msas.writer.WriterAccount;
import ru.msas.writer.WriterInstance;
import java.util.*;

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
    CheckerAccount checkerAccount;
    @Autowired
    AccountService accountService;
    @Autowired
    InstanceService instanceService;

    @PostMapping("/corporate-settlement-instance/create")
    public ResponseEntity<Map <String, Object>> corporateSettlementInstanseCreate(@RequestBody Map<String, Object> dtoRequest){

        Model model = dataReader.get();
        model.setRqMap(dtoRequest);

        model = checkerInstance.apply(model);
        model = instanceService.Create(model);
        model = writerInstance.apply(model);

        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(model.getrMessage());
    }

    @PostMapping("/corporate-settlement-account/create")
    public ResponseEntity<Map <String, Object>>  corporateSettlementAccountCreate(@RequestBody Map<String, Object> dtoRequest){

        Model model = dataReader.get();

        model.setRqMap(dtoRequest);

        model = checkerAccount.apply(model);

        model = accountService.Create(model);

        model = writerAccount.apply(model);

        //Отправить данные в систему источник запроса на создание продуктового регистра
        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(model.getrMessage());
    }
}