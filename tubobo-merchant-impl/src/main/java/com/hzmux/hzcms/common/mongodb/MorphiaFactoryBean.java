package com.hzmux.hzcms.common.mongodb;

import org.mongodb.morphia.Morphia;
import org.springframework.beans.factory.config.AbstractFactoryBean;

public class MorphiaFactoryBean extends AbstractFactoryBean<Morphia> {

    private String[] packageNames;
    private String[] mapClasses;
    private boolean ignoreInvalidClasses = false;
    @Override
    public Class<?> getObjectType() {
        return Morphia.class;
    }

    @Override
    protected Morphia createInstance() throws Exception {
        Morphia morphia = new Morphia();
        if(null != packageNames) {
            for (String packageName : packageNames) {
                morphia.mapPackage(packageName, ignoreInvalidClasses);
            }
        }
        if(null != mapClasses) {
            for (String mapClass : mapClasses) {
                morphia.map(Class.forName(mapClass));
            }
        }
        return morphia;
    }

    public String[] getPackageNames() {
        return packageNames;
    }

    public String[] getMapClasses() {
        return mapClasses;
    }

    public boolean isIgnoreInvalidClasses() {
        return ignoreInvalidClasses;
    }

    public void setPackageNames(String[] packageNames) {
        this.packageNames = packageNames;
    }

    public void setMapClasses(String[] mapClasses) {
        this.mapClasses = mapClasses;
    }

    public void setIgnoreInvalidClasses(boolean ignoreInvalidClasses) {
        this.ignoreInvalidClasses = ignoreInvalidClasses;
    }

}
