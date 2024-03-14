package ru.msas;

import org.springframework.stereotype.Component;
import ru.msas.enums.ArrangementBodyFields;
import ru.msas.enums.InstanceBodyFields;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.UnaryOperator;

@Component
public class CheckerInstance implements UnaryOperator<Model> {
    @Override
    public Model apply(Model model) {
        Map<String, Object> rqMap = model.getRqMap();
        Map<String, Object> rMessage = new HashMap<String, Object>();
        Object tObj;
        String tStr;

        for (InstanceBodyFields value : InstanceBodyFields.values()) {
            if (value.isRequired == true) {
                tObj = rqMap.get(value.name());
                try {
                    tStr = (String) (tObj);
                } catch (Exception e) {
                    rMessage.clear();
                    rMessage.put("Error", (Object) "String = (String)tobj for value '" + value.name() + "'" + e.getMessage());
                    rMessage.put("ErrorCode",(Object) "400");
                    model.setrMessage(rMessage);
                    return model;

                }
                if (tObj == null || tStr.isEmpty()) {
                    rMessage.put("Error", (Object) "400/Bad Request. Required Parameter <" + value.name() + "> is Empty");
                    rMessage.put("ErrorCode",(Object) "400");
                    model.setrMessage(rMessage);
                    return model;

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
            String st = e.getMessage();
            rMessage.clear();
            rMessage.put("Error", (Object) "400/ " + st);
            rMessage.put("ErrorCode",(Object) "400");
            model.setrMessage(rMessage);
            return model;

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
                        rMessage.clear();
                        rMessage.put("Error", (Object) "400/Bad Request. Required Parameter <" + value.name() + "> is Empty in arrangement array N" + i);
                        rMessage.put("ErrorCode", (Object) "400");
                        model.setrMessage(rMessage);
                        return model;

                    }
                }
            }

        }
        model.setAlArrangement(alArrangement);
        return model;
    }
}
