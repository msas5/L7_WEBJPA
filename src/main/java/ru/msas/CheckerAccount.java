package ru.msas;

import org.springframework.stereotype.Component;
import ru.msas.enums.AccountBodyFields;

import java.util.HashMap;
import java.util.Map;
import java.util.function.UnaryOperator;

@Component
public class CheckerAccount  implements UnaryOperator<Model> {

    @Override
    public Model apply(Model model) {
        Map<String, Object> rqMap = model.getRqMap();
        Map<String, Object> rMessage = new HashMap<String, Object>();
        Object tObj;
        String tStr;



        //Шаг 1.
        for(AccountBodyFields value: AccountBodyFields.values()){
            if(value.isRequired == true) {
                tObj = rqMap.get(value.name());
                try{
                    tStr = (String) (tObj);
                } catch( Exception e){
                    rMessage.clear();
                    rMessage.put("Error", (Object) "String = (String)tobj for value '" + value.name()+ "'" + e.getMessage());
                    rMessage.put("ErrorCode",(Object) "400");
                    model.setrMessage(rMessage);
                    return model;

                }
                if ( tObj == null || tStr.isEmpty() ){
                    rMessage.clear();
                    rMessage.put("Error",(Object)"400/Bad Request. Required Parameter <" + value.name() + "> is Empty");
                    rMessage.put("ErrorCode",(Object) "400");
                    model.setrMessage(rMessage);
                    return model;



                }
            }
        }


        return null;
    }
}
