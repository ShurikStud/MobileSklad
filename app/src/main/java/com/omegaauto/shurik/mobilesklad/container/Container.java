package com.omegaauto.shurik.mobilesklad.container;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.util.Date;

public class Container {

    String driver_name; // ФИО водителя
    String vehicle_name; //№ транспортного средства)

    String zayavkaTEP_highway_date; // (Дата Заявки ТЭП магистральной)
    String zayavkaTEP_highway_number; // (№ Заявки ТЭП магистральной)
    String zayavkaTEP_number; // (№ Заявки ТЭП подчиненной)
    String trip_number; // (№ рейса)
    String nn; // (№ по порядку в карте погрузки)
    String nnMax; // (№ по порядку в карте погрузки)
    String partner_address; // (Адрес доставки (Строка))
    String partner_name; // (Наименование контрагента)
    String partner_phone; // (Тел. Номера контрагента)
    String invoice_numbers; // (Номера РН )
    String type_pack; // (Тип упаковки)
    String weight; // (Вес, кг.)

    String amount_goods; // (Количество грузов)

    String number; // (Номер тарного места)
    String volume; // (Объем ТарногоМеста)

    public Container() {
    }

    public String getPropertyValueString(String propertyName){
        // возвращает значение свойства, переданного в параметре, в текстовом формате

        Class thisClass = getClass();


        try {
            Method method = thisClass.getMethod("get" + propertyName, null);

            if (method == null) {
                return "";
            } else {
                String result = String.valueOf(method.invoke(this, null));
                return result;
            }
        }catch (NoSuchMethodException e1){
            return "";
        }catch (InvocationTargetException e2){
            return "";
        }catch (IllegalAccessException e3){
            return "";
        }


    }

    public String getDriver_name() {
        return driver_name;
    }

//    public String getStringDriver_name() {
//        return driver_name;
//    }

    public void setDriver_name(String driver_name) {
        this.driver_name = driver_name;
    }

    public String getVehicle_name() {
        return vehicle_name;
    }

    public void setVehicle_name(String vehicle_name) {
        this.vehicle_name = vehicle_name;
    }

    public String getZayavkaTEP_highway_date() {
        return zayavkaTEP_highway_date;
    }

    public void setZayavkaTEP_highway_date(String zayavkaTEP_highway_date) {
        this.zayavkaTEP_highway_date = zayavkaTEP_highway_date;
    }

    public String getZayavkaTEP_highway_number() {
        return zayavkaTEP_highway_number;
    }

    public void setZayavkaTEP_highway_number(String zayavkaTEP_highway_number) {
        this.zayavkaTEP_highway_number = zayavkaTEP_highway_number;
    }

    public String getZayavkaTEP_number() {
        return zayavkaTEP_number;
    }

    public void setZayavkaTEP_number(String zayavkaTEP_number) {
        this.zayavkaTEP_number = zayavkaTEP_number;
    }

    public String getTrip_number() {
        return trip_number;
    }

    public void setTrip_number(String trip_number) {
        this.trip_number = trip_number;
    }

    public String getNn() {
        return nn + " / " + nnMax ;
    }

    public void setNn(String nn) {
        this.nn = nn;
    }

    public String getPartner_address() {
        return convertToNL(partner_address, ';');
    }

    public void setPartner_address(String partner_address) {
        this.partner_address = partner_address;
    }

    public String getPartner_name() {
        return partner_name;
    }

    public void setPartner_name(String partner_name) {
        this.partner_name = partner_name;
    }

    public String getPartner_phone() {
        return partner_phone;
    }

    public void setPartner_phone(String partner_phone) {
        this.partner_phone = partner_phone;
    }

    public String getInvoice_numbers() {
        return invoice_numbers;
    }

    public void setInvoice_numbers(String invoice_numbers) {
        this.invoice_numbers = convertToNL(invoice_numbers, ',');
    }

    public String getType_pack() {
        return type_pack;
    }

    public void setType_pack(String type_pack) {
        this.type_pack = type_pack;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getAmount_goods() {
        return amount_goods;
    }

    public void setAmount_goods(String amount_goods) {
        this.amount_goods = amount_goods;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public String getNnMax() {
        return nnMax;
    }

    public void setNnMax(String nnMax) {
        this.nnMax = nnMax;
    }

    private String convertToNL(String inputString, char oldSymbol){
        String outputString;

        outputString = inputString.replace(oldSymbol, '\n');

        return outputString;
    }

    public void setNoData(){
        driver_name = "нет данных";
        vehicle_name = "нет данных";

        zayavkaTEP_highway_date = "нет данных";
        zayavkaTEP_highway_number = "нет данных";
        zayavkaTEP_number = "нет данных";
        trip_number = "нет данных";
        nn = "нет данных";
        partner_address = "нет данных";
        partner_name = "нет данных";
        partner_phone = "нет данных";
        invoice_numbers = "нет данных";
        type_pack = "нет данных";
        weight = "нет данных";

        amount_goods = "нет данных";

        number = "нет данных";
        volume = "нет данных";
    }

    @Override
    public String toString() {
        return "Container{" +
                "driver_name='" + driver_name + '\'' +
                ", vehicle_name='" + vehicle_name + '\'' +
                ", zayavkaTEP_highway_date=" + zayavkaTEP_highway_date +
                ", zayavkaTEP_highway_number=" + zayavkaTEP_highway_number +
                ", zayavkaTEP_number='" + zayavkaTEP_number + '\'' +
                ", trip_number='" + trip_number + '\'' +
                ", nn=" + nn +
                ", partner_address='" + partner_address + '\'' +
                ", partner_name='" + partner_name + '\'' +
                ", partner_phone='" + partner_phone + '\'' +
                ", invoice_numbers='" + invoice_numbers + '\'' +
                ", type_pack='" + type_pack + '\'' +
                ", weight=" + weight +
                ", amount_goods=" + amount_goods +
                ", number=" + number +
                ", volume=" + volume +
                '}';
    }
}
