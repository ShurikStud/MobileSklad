package com.omegaauto.shurik.mobilesklad.settings;

public final class MobileSkladSettings {

    private static MobileSkladSettings instance;

    private int counter; // время в секундах отображения информации об контейнере при сканировании через камеру телефона
    private Boolean isCounterEnable = false;
    private MobileSkladFontSize fontSize;
    private int timeout = 0;

    private static final int PROPERTIES_VERSION = 1;
    int current_version;

    //================ МЕТОДЫ ==================

    private MobileSkladSettings(){}

    static public MobileSkladSettings getInstance(){

        if (instance == null){
            instance = new MobileSkladSettings();
            instance.setCurrentVersion();
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
        timeout = 10;
        fontSize = ListFontSizes.getInstance().get("NORMAL");
        setCurrentVersion();
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public MobileSkladFontSize getFontSize() {
        return fontSize;
    }

    public void setFontSize(MobileSkladFontSize fontSize) {
        this.fontSize = fontSize;
    }

    public void setProperties(MobileSkladSettings input){
        if (input.current_version != current_version) {
            initDefault();
            return;
        }
        setCounter(input.getCounter());
        setCounterEnable(input.getCounterEnable());
        setTimeout(input.getTimeout());
        if (input.getFontSize() == null) {
            setFontSize(ListFontSizes.getInstance().getDefault());
        } else {
            setFontSize(ListFontSizes.getInstance().get(input.getFontSize().getId()));
        }
    }

    private void setCurrentVersion(){
        current_version = PROPERTIES_VERSION;
    }
}
