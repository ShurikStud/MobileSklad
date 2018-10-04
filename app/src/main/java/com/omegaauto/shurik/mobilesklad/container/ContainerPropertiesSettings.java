package com.omegaauto.shurik.mobilesklad.container;

import java.util.ArrayList;
import java.util.List;

public final class ContainerPropertiesSettings {
    // класс настроек порядка и видимости реквизитов КОНТЕЙНЕРа (Container)

    private List<Property> properties; // все возможные свойства контейнера
    private int separator; // номер в списке, меньше которго - видимые свойства, больше или равно - невидимые
    static ContainerPropertiesSettings instance;


    private ContainerPropertiesSettings(){}

    static public ContainerPropertiesSettings getInstance() {

        if (instance == null){
            instance = new ContainerPropertiesSettings();

        }

        return instance;
    }

    public void setProperties(ContainerPropertiesSettings input){

        if (properties == null) {
            properties = new ArrayList<Property>();
        } else {
            properties.clear();
        }

        for (Property property: input.getProperties()) {
            properties.add(property);
        }

        separator = input.getSeparator();

    }

    public void initDefault(){

        if (properties == null) {
            properties = new ArrayList<Property>();
        } else {
            properties.clear();
        }

        properties.add(new Property("Driver_name", "ФИО водителя"));
        properties.add(new Property("Vehicle_name", "№ транспортного средства"));
        properties.add(new Property("ZayavkaTEP_highway_date", "дата Заявки ТЭП (магистральной)"));
        properties.add(new Property("ZayavkaTEP_highway_number", "№ Заявки ТЭП (магистральной)"));
        properties.add(new Property("ZayavkaTEP_number", "№ Заявки ТЭП"));
        properties.add(new Property("Trip_number", "№ рейса"));
        properties.add(new Property("Nn", "№ п/п"));
        properties.add(new Property("SEPARATOR", "НЕ ОТОБРАЖАЮТСЯ"));
        properties.add(new Property("Partner_address", "адрес Клиента"));
        properties.add(new Property("Partner_name", "Клиент"));
        properties.add(new Property("Partner_phone", "тел. Клиента"));
        properties.add(new Property("Invoice_numbers", "№№ накладных"));
        properties.add(new Property("Type_pack", "Тип упаковки"));
        properties.add(new Property("Weight", "Вес, кг."));
        properties.add(new Property("Amount_goods", "количество грузов"));
        properties.add(new Property("Number", "№ тарного места"));
        properties.add(new Property("Volume", "объем тарного места"));
        //separator = properties.size();
        separator = 7;
    }

    public List<Property> getProperties() {
        return properties;
    }

    public List<Property> getVisibleProperties(){
        List<Property> visibleProperties = new ArrayList<Property>();

        for (int i = 0; i < separator; i++){
            visibleProperties.add(properties.get(i));
        }

        return visibleProperties;

    }

    public int getSeparator(){
        return separator;
    }

    public int getSize(){
        return properties.size();
    }

    public Property getProperty(int index){
        return properties.get(index);
    }

    public int getIndex(Property property){
        return properties.indexOf(property);
    }

    public void movePropertyTo(int index, Property element){
        int currentIndex = properties.indexOf(element);
        if (currentIndex < separator && index >= separator){
            --separator;
        } else if (currentIndex > separator && index <= separator){
            ++separator;
        }
        properties.remove(element);
        properties.add(index, element);

    }

    public class Property{
        String name;
        String description;

        public Property(String name, String description) {
            this.name = name;
            this.description = description;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }

}
