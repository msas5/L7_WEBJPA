package ru.msas.writer;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.msas.Model;
import ru.msas.entity.Agreement;
import ru.msas.entity.TppProduct;
import ru.msas.entity.TppProductRegister;
import ru.msas.entity.TppRefProductRegisterType;
import ru.msas.repo.AgreementRepo;
import ru.msas.repo.TppProductRegisterRepo;
import ru.msas.repo.TppProductRepo;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;


@Component
public class WriterInstance  implements UnaryOperator<Model> {
    // @Autowired
    TppProductRepo tppProduct;
    //@Autowired
    TppProductRegisterRepo tppProductRegister;

    // @Autowired
    AgreementRepo agreement;
    @Transactional
    @Override
    public Model apply(Model model) {
        Map<String, Object> rMessage = new HashMap<String, Object>();
        String tStr;
        if (model.getInstanceExists() == false) {
            //todo Необходимо сделать транзакцию с записываемыми далее двумя таблицами
            //Шаг 1.4
            //Добавить строку в tppProduct, согласно Request.Body,Сформировать новый ИД ЭП tppProduct.id
            TppProduct tppProductForSave;
            try {
                tppProductForSave = new TppProduct(model.getRqMap());
            } catch (Exception e) {
                rMessage.clear();
                tStr = "400/Bad make completed object for class TppProduct " + e.getMessage();
                rMessage.put("Error", (Object) tStr);
                rMessage.put("ErrorCode",(Object) "400");
                model.setError(true);
                model.setrMessage(rMessage);
                model.setError(true);
                model.setrMessage(rMessage);
                return model;

            }
            TppProduct tppProductWasSaved;
            try {
                tppProductWasSaved = tppProduct.save(tppProductForSave);
            } catch (Exception e) {
                rMessage.clear();
                tStr = "400/Bad save completed object for class TppProduct " + e.getMessage();
                rMessage.put("Error", (Object) tStr);
                rMessage.put("ErrorCode",(Object) "400");
                model.setError(true);
                model.setrMessage(rMessage);
                return model;

            }


            //Шаг 1.5
            String sInstanceId = tppProductWasSaved.getId();
            Map<String,Object> rqMap = model.getRqMap();
            rqMap.put("instanceId", (Object) sInstanceId);

            TppRefProductRegisterType tRefProductRegisterType = model.getlProductRegistertType().get(0);
            rqMap.put("type", tRefProductRegisterType.getValue());
            TppProductRegister tppProductRegisterForSave;
            try {
                tppProductRegisterForSave = new TppProductRegister(rqMap);
            } catch (Exception e) {
                rMessage.clear();
                tStr = "400/Bad make completed object for class tppProductRegister " + e.getMessage();
                rMessage.put("Error", (Object) tStr);
                rMessage.put("ErrorCode",(Object) "400");
                model.setError(true);
                model.setrMessage(rMessage);
                return model;

            }
            //Добавить в таблицу ПР tppProductRegistry строки id, productId,type,account_id,currencyCode,state
            try {
                TppProductRegister tppProductRegisterWasSaved = tppProductRegister.save(tppProductRegisterForSave);
            } catch (Exception e) {
                rMessage.clear();
                tStr = "400/Bad save completed object for class tppProductRegister " + e.getMessage();
                rMessage.put("Error", (Object) tStr);
                rMessage.put("ErrorCode",(Object) "400");
                model.setError(true);
                model.setrMessage(rMessage);
                return model;

            }

        } else
        {
            try {
                Agreement agreementWasSaved = agreement.save(model.getAgreementForSave());
            } catch (Exception e) {
                rMessage.clear();
                tStr = "400/Bad save completed object for class agreement " + e.getMessage();
                rMessage.put("Error", (Object) tStr);
                rMessage.put("ErrorCode",(Object) "400");
                model.setError(true);
                model.setrMessage(rMessage);
                return model;

            }




        }

        return null;
    }
}

