package com.omegaauto.shurik.mobilesklad.settings;

public final class MobileSkladSettings {

    private static MobileSkladSettings instance;

    private int counter; // время в секундах отображения информации об контейнере при сканировании через камеру телефона
    private Boolean isCounterEnable = false;

    private int timeout = 0;

    private MobileSkladSettings(){}

    static public MobileSkladSettings getInstance(){

        if (instance == null){
            instance = new MobileSkladSettings();

        }

        return instance;
    }

    public int getCounter() {

        return counter;
    }

    public void setCounter(int counter) {
        if (counter < 0) {
            this.counter = 0;
        }else {
            this.counter = counter;
        }
    }

    public Boolean getCounterEnable() {
        if (isCounterEnable == null){
            return false;
        } else {
            return isCounterEnable;
        }
    }

    public void setCounterEnable(Boolean counterEnable) {
        isCounterEnable = counterEnable;
    }

    public void initDefault(){
        isCounterEnable = false;
        counter = 0;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public void setProperties(MobileSkladSettings input){

        setCounter(input.getCounter());
        setCounterEnable(input.getCounterEnable());
        setTimeout(input.getTimeout());
    }
}
