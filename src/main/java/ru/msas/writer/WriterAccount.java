package ru.msas.writer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.msas.Model;
import ru.msas.entity.TppProductRegister;
import ru.msas.exceptions.SaveTppProductRegisterException;
import ru.msas.repo.TppProductRegisterRepo;
import java.util.function.UnaryOperator;

@Component
public class WriterAccount  implements UnaryOperator<Model> {

    @Autowired
    TppProductRegisterRepo tppProductRegister;

    @Transactional
    @Override
    public Model apply(Model model) {

        try {
            TppProductRegister tppProductRegisterWasSaved = tppProductRegister
                    .save(model.getTppProductRegisterForSave());
        } catch (Exception e) {
            String tStr = "Error for save tppProductRegister " + e.getMessage();
            throw new SaveTppProductRegisterException(tStr);
        }
        return model;
    }
}