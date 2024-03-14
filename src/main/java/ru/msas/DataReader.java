package ru.msas;

import org.springframework.stereotype.Component;
import java.util.function.Supplier;

@Component
public class DataReader implements Supplier<Model> {
    @Override
    public Model get() {
        Model model = new Model();
        return model;
    }
}
