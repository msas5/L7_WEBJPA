package ru.msas.writer;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.msas.Model;

import java.util.function.Consumer;
import java.util.function.UnaryOperator;

@Transactional
@Component
public class WriterInstance  implements UnaryOperator<Model> {


    @Override
    public Model apply(Model model) {
        return null;
    }
}

