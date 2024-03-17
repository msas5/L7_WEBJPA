package ru.msas.checker;

import org.springframework.stereotype.Component;
import ru.msas.Model;
import ru.msas.enums.ArrangementBodyFields;
import ru.msas.enums.InstanceBodyFields;
import ru.msas.exceptions.BadRequestException;
import java.util.ArrayList;
import java.util.Map;
import java.util.function.UnaryOperator;

@Component
public class CheckerInstance implements UnaryOperator<Model> {
    @Override
    public Model apply(Model model) {
        Map<String, Object> rqMap = model.getRqMap();
        Object tObj;
        String tStr;
        String sErrorMessage;

        for (InstanceBodyFields value : InstanceBodyFields.values()) {
            if (value.isRequired == true) {
                tObj = rqMap.get(value.name());
                try {
                    tStr = (String) (tObj);
                } catch (Exception e) {
                    sErrorMessage = "String = (String)tobj for value '" + value.name() + "'" + e.getMessage();
                    throw new BadRequestException(sErrorMessage);
                }
                if (tObj == null || tStr.isEmpty()) {
                    sErrorMessage = "Bad Request. Required Parameter <" + value.name() + "> is Empty";
                    throw new BadRequestException(sErrorMessage);
                }
            }
        }

        // Валидация arrangement, если присутствует
        Object tObjarrangement = rqMap.get("arrangement");
        ArrayList<Map<String, Object>> alArrangement;

        // Валидация arrangement, если присутствует
        try {
            alArrangement = (ArrayList<Map<String, Object>>) tObjarrangement;
        } catch (Exception e) {
            sErrorMessage = "Error for getting Arrangement section of your request " + e.getMessage();
            throw new BadRequestException(sErrorMessage);
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
                        sErrorMessage = "Bad Request. Required Parameter <" + value.name() + "> is Empty in arrangement array N" + i;
                        throw new BadRequestException(sErrorMessage);
                    }
                }
            }
        }
        model.setAlArrangement(alArrangement);
        return model;
    }
}