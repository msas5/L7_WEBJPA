package ru.msas.writer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.msas.Model;
import ru.msas.entity.TppProductRegister;
import ru.msas.repo.TppProductRegisterRepo;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;


@Component
public class WriterAccount  implements UnaryOperator<Model> {

    @Autowired
    TppProductRegisterRepo tppProductRegister;

    @Transactional
    @Override
    public Model apply(Model model) {
        Map<String, Object> rMessage = new HashMap<String, Object>();

        try {
            TppProductRegister tppProductRegisterWasSaved = tppProductRegister.save(model.getTppProductRegisterForSave());
        } catch (Exception e) {
            rMessage.clear();
            String tStr = "400/Error for save tppProductRegister";
            rMessage.put("Error", (Object) tStr + " " + e.getMessage());
            rMessage.put("ErrorCode", (Object) "400");
            model.setrMessage(rMessage);
            model.setError(true);
            return model;
        }

        return model;
    }
}
