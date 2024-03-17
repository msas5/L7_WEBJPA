package ru.msas.checker;

import org.springframework.stereotype.Component;
import ru.msas.Model;
import ru.msas.enums.AccountBodyFields;
import ru.msas.exceptions.BadRequestException;
import java.util.Map;
import java.util.function.UnaryOperator;

@Component
public class CheckerAccount  implements UnaryOperator<Model> {

    @Override
    public Model apply(Model model) {
        Map<String, Object> rqMap = model.getRqMap();
        Object tObj;
        String sErrorMessage;
        String tStr;
        //Шаг 1.
        for(AccountBodyFields value: AccountBodyFields.values()){
            if(value.isRequired == true) {
                tObj = rqMap.get(value.name());
                try{
                    tStr = (String) (tObj);
                } catch( Exception e){
                    sErrorMessage = "String = (String)tobj for value '" + value.name()+ "'" + e.getMessage();
                    throw new BadRequestException(sErrorMessage);
                }
                if ( tObj == null || tStr.isEmpty() ){
                    sErrorMessage = "Bad Request. Required Parameter <" + value.name() + "> is Empty";
                    throw new BadRequestException(sErrorMessage);
                }
            }
        }
        return model;
    }
}