package com.suifeng.javaparsertool.support.config;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Config {
    /**
     * type : 0
     * check_dir : false
     * src_path : source_in
     * out_path : source_out
     * package_name : com.dmy
     * pre_class_name : ClassName
     * entry_method : startCheck
     * config_xml_name : SdkToolConfig.xml
     * class_count : 3
     * method_low_limit : 6
     * static_ratio : 0.8
     * modify_Modifier : true
     * static_method : ["abc"]
     * white_list : {"keep_method":["abc"],"keep_class":["abc"]}
     */

    @SerializedName("type")
    private int type;
    @SerializedName("check_dir")
    private boolean checkDir;
    @SerializedName("src_path")
    private String srcPath;
    @SerializedName("out_path")
    private String outPath;
    @SerializedName("package_name")
    private String packageName;
    @SerializedName("pre_class_name")
    private String preClassName;
    @SerializedName("entry_method")
    private String entryMethod;
    @SerializedName("config_xml_name")
    private String configXmlName;
    @SerializedName("class_count")
    private int classCount;
    @SerializedName("method_low_limit")
    private int methodLowLimit;
    @SerializedName("static_ratio")
    private double staticRatio;
    @SerializedName("modify_Modifier")
    private boolean modifyModifier;
    @SerializedName("white_list")
    private WhiteListBean whiteList;
    @SerializedName("static_method")
    private List<String> staticMethod;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isCheckDir() {
        return checkDir;
    }

    public void setCheckDir(boolean checkDir) {
        this.checkDir = checkDir;
    }

    public String getSrcPath() {
        return srcPath;
    }

    public void setSrcPath(String srcPath) {
        this.srcPath = srcPath;
    }

    public String getOutPath() {
        return outPath;
    }

    public void setOutPath(String outPath) {
        this.outPath = outPath;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getPreClassName() {
        return preClassName;
    }

    public void setPreClassName(String preClassName) {
        this.preClassName = preClassName;
    }

    public String getEntryMethod() {
        return entryMethod;
    }

    public void setEntryMethod(String entryMethod) {
        this.entryMethod = entryMethod;
    }

    public String getConfigXmlName() {
        return configXmlName;
    }

    public void setConfigXmlName(String configXmlName) {
        this.configXmlName = configXmlName;
    }

    public int getClassCount() {
        return classCount;
    }

    public void setClassCount(int classCount) {
        this.classCount = classCount;
    }

    public int getMethodLowLimit() {
        return methodLowLimit;
    }

    public void setMethodLowLimit(int methodLowLimit) {
        this.methodLowLimit = methodLowLimit;
    }

    public double getStaticRatio() {
        return staticRatio;
    }

    public void setStaticRatio(double staticRatio) {
        this.staticRatio = staticRatio;
    }

    public boolean isModifyModifier() {
        return modifyModifier;
    }

    public void setModifyModifier(boolean modifyModifier) {
        this.modifyModifier = modifyModifier;
    }

    public WhiteListBean getWhiteList() {
        return whiteList;
    }

    public void setWhiteList(WhiteListBean whiteList) {
        this.whiteList = whiteList;
    }

    public List<String> getStaticMethod() {
        return staticMethod;
    }

    public void setStaticMethod(List<String> staticMethod) {
        this.staticMethod = staticMethod;
    }

    public static class WhiteListBean {
        @SerializedName("keep_method")
        private List<String> keepMethod;
        @SerializedName("keep_class")
        private List<String> keepClass;

        public List<String> getKeepMethod() {
            return keepMethod;
        }

        public void setKeepMethod(List<String> keepMethod) {
            this.keepMethod = keepMethod;
        }

        public List<String> getKeepClass() {
            return keepClass;
        }

        public void setKeepClass(List<String> keepClass) {
            this.keepClass = keepClass;
        }
    }
}
