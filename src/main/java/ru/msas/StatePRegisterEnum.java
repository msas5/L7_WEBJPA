package ru.msas;

public enum StatePRegisterEnum {
   CLOSED (0,"Закрыт"),
   OPENNED (1,"Открыт"),
   RESERVED (2,"Зарезервирован"),
   DELETED (3,"Удалён");

    int value;
    String statusName;


    StatePRegisterEnum(int value,String statusname){
        this.value = value;
        this.statusName = statusname;

    }
}
