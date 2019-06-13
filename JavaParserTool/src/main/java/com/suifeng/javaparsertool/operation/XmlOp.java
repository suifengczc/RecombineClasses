package com.suifeng.javaparsertool.operation;

import com.suifeng.javaparsertool.support.MethodData;
import com.suifeng.javaparsertool.support.RandomUtil;
import com.suifeng.javaparsertool.support.Utils;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.dom4j.tree.DefaultText;

import java.beans.PropertyEditorManager;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;


/**
 * 生成SdkToolConfig.xml 相关类
 */
public class XmlOp {

    private static final String[] PLUGIN_ARRAY = {"web", "dsp", "live", "appleid", "AZeroPlug", "IFirstPlug", "LSecondPlug", "NThreePlug", "CFivePlug", "FSixPlug", "VEightPlug", "TNinePlug", "BTenPlug", "MElevenPlug", "PTwelvePlug", "AThirteenPlug", "BasicLibrary", "AssetName", "PluginConfig", "VirName"};
    private static final String[] OTHERS_ARRAY = {"ForeignCount", "PluginsDir", "PluginFile", "DownloadDB", "NowData", "ApiInfo", "PathChange", "DownloadDBNew", "XNJ", "VirtualEnc", "DataEnc", "OptEnc", "UserEnc", "Mob"};
    private static final String[] ENCRYPT_ARRAY = {"IFirstPlug", "LSecondPlug", "NThreePlug", "CFivePlug", "FSixPlug", "VEightPlug", "TNinePlug", "BTenPlug", "MElevenPlug"};
    private static int addFileCount = 3;//添加文件的个数
    private static int pluginDirCount = 3;//插件路径个数

    /**
     * 生成SdkToolConfig.xml文件
     *
     * @param xmlOutPath          SdkToolConfig.xml输出路径
     * @param classCount          生成的类个数
     * @param preClassName        类名前缀
     * @param mAllMethodsNameList 方法名list
     * @param mAllStringMap       字符串map
     * @param mAllMethodDataMap   MethodData map
     * @param mPackageName        包名
     * @param mEntryMethodName    入口方法名
     */
    public static void buildXml(String xmlOutPath, int classCount, String preClassName, ArrayList<String> mAllMethodsNameList, Map<String, String> mAllStringMap, Map<String, MethodData> mAllMethodDataMap, String mPackageName, String mEntryMethodName) {
        try {
            // 创建document对象
            Document document = DocumentHelper.createDocument();
            // 创建根节点Config
            Element configElement = document.addElement("Config");

            addFileCount = RandomUtil.randInt(3, 6);
            pluginDirCount = RandomUtil.randInt(3, 6);

            // 生成子节点Setup
            Element setupElement = configElement.addElement("Setup");

            setSetupElement(setupElement, classCount, preClassName, mAllMethodsNameList);
            //子节点setup结束

            //生成子节点ExtraConfig
            Element extraConfigElement = configElement.addElement("ExtraConfig");
            extraConfigElement.addAttribute("Name", "Extra");
            //子节点ExtraConfig结束

            Element configRunInfoElement = configElement.addElement("ConfigRunInfo");
            setConfigRunInfoElement(configRunInfoElement, mAllStringMap);

            //设置EditConfig节点
            Element editConfigElement = configElement.addElement("EditConfig");
            setEditConfigElement(editConfigElement);

            Element editCompMappingElement = configElement.addElement("EditCompMapping");
            setEditCompMappingElement(editCompMappingElement);

            Element editManifestElement = configElement.addElement("EditManifest");
            setEditManifestElement(editManifestElement);

            //FastSdkApplication的配置
            Element editSdkCodeElement = configElement.addElement("EditSdkCode");
            setEditSdkCodeElement(editSdkCodeElement, mAllMethodDataMap, mPackageName, mEntryMethodName, mAllStringMap);

            //各个class配置
            for (int i = 0; i < classCount; i++) {
                Element classElement = configElement.addElement("EditSdkCode");
                setClassElement(classElement, i, preClassName, mPackageName, classCount, mAllMethodsNameList, mAllStringMap);
            }

            //设置WriteConfig节点
            Element writeConfigElement = configElement.addElement("WriteConfig");
            setWriteConfigElement(writeConfigElement);

            for (String encrypt : ENCRYPT_ARRAY) {
                Element encryptElement = configElement.addElement("Encrypt");
                setencryptElement(encryptElement, encrypt);
            }

            Element fileCopy = configElement.addElement("FileCopy");
            fileCopy.addAttribute("Name", "assets/ugsdk");
            fileCopy.addAttribute("IfName", "umKey");
            fileCopy.addAttribute("MoveName", "UmengSdk");

            fileCopy = configElement.addElement("FileCopy");
            fileCopy.addAttribute("Name", "apk:///assets/virName");
            fileCopy.addAttribute("MoveName", "VirName");
            fileCopy.addAttribute("DirName", "assets/");

            setLastEncryptElement(configElement);

            setWriteRunInfoElement(configElement);

            setAddFileElemenet(configElement);

            // 5、设置生成xml的格式
            OutputFormat format = OutputFormat.createCompactFormat();
            // 设置编码格式
            format.setEncoding("UTF-8");
            format.setNewlines(true);
            format.setIndent(true);
            format.setIndentSize(4);
            format.setNewLineAfterDeclaration(true);

            // 6、生成xml文件
            File file = new File(xmlOutPath);
            XMLWriter writer = new XMLWriter(new FileOutputStream(file), format);
            // 设置是否转义，默认使用转义字符
            writer.setEscapeText(false);
            writer.write(document);
            writer.close();
            System.out.println("general xml success");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("general xml fail");
        }
    }


    private static void setSetupElement(Element setupElement, int classCount, String preClassName, ArrayList<String> mAllMethodsNameList) {
        setupElement.addAttribute("Name", "Setup");

        Element sourceElement = setupElement.addElement("Source");

        sourceElement.addAttribute("Name", "packagename");
        sourceElement.addAttribute("Value", "com.quick.photo");

        sourceElement = setupElement.addElement("Source");
        sourceElement.addAttribute("Name", "SdkApplication");
        sourceElement.addAttribute("Value", "${%packagename}" + RandomUtil.getMultRandConfig(".", null, 1, 3, true));

        for (int i = 0; i < classCount; i++) {
            sourceElement = setupElement.addElement("Source");
            sourceElement.addAttribute("Name", preClassName + i);
            sourceElement.addAttribute("Value", "${%packagename}" + RandomUtil.getMultRandConfig(".", null, 1, 3, true));
        }

        for (String method : mAllMethodsNameList) {
            int i = RandomUtil.randInt(1, 5);
            sourceElement = setupElement.addElement("Source");
            sourceElement.addAttribute("Name", Utils.upperCaseChar(method, 0));
            sourceElement.addAttribute("Value", RandomUtil.getRandConfig("Method"));
        }

        for (int i = 0; i < pluginDirCount; i++) {
            sourceElement = setupElement.addElement("Source");
            sourceElement.addAttribute("Name", "Plugin_" + i);
            sourceElement.addAttribute("Value", RandomUtil.getMultRandConfig("/", null, 1, 3, false));
        }


        sourceElement = setupElement.addElement("Source");
        sourceElement.addAttribute("Name", "PluginMask");
        sourceElement.addAttribute("Value", "${#rand|#min=3|#max=6}");

        for (String plugin : PLUGIN_ARRAY) {
            int p = RandomUtil.randInt(1, 3);
            sourceElement = setupElement.addElement("Source");
            sourceElement.addAttribute("Name", plugin);
            sourceElement.addAttribute("Value", "${%Plugin_" + p + "}/" + RandomUtil.getRandConfig("plugin"));
        }
        for (String other : OTHERS_ARRAY) {
            sourceElement = setupElement.addElement("Source");
            sourceElement.addAttribute("Name", other);
            sourceElement.addAttribute("Value", RandomUtil.getRandConfig("other"));
        }

        sourceElement = setupElement.addElement("Source");
        sourceElement.addAttribute("Name", "VirtualShortcutAction");
        sourceElement.addAttribute("Value", "com." + RandomUtil.getRandConfig(null) + "." + RandomUtil.getRandConfig(null) + ".action");

        sourceElement = setupElement.addElement("Source");
        sourceElement.addAttribute("Name", "HostTaskAffinity");
        sourceElement.addAttribute("Value", "com." + RandomUtil.getRandConfig(null) + "." + RandomUtil.getRandConfig(null));

        sourceElement = setupElement.addElement("Source");
        sourceElement.addAttribute("Name", "StyleCStuThemeConfig");
        sourceElement.addAttribute("Value", "@style/" + RandomUtil.getRandConfig(null));

        sourceElement = setupElement.addElement("Source");
        sourceElement.addAttribute("Name", "StyleCDiaThemeConfig");
        sourceElement.addAttribute("Value", "@style/" + RandomUtil.getRandConfig(null));

        sourceElement = setupElement.addElement("Source");
        sourceElement.addAttribute("Name", "StyleCStuTheme");
        sourceElement.addAttribute("Value", "${%StyleCStuThemeConfig}");

        sourceElement = setupElement.addElement("Source");
        sourceElement.addAttribute("Name", "StyleCDiaTheme");
        sourceElement.addAttribute("Value", "${%StyleCDiaThemeConfig}");

        sourceElement = setupElement.addElement("Source");
        sourceElement.addAttribute("Name", "StyleCDiaTheme");
        sourceElement.addAttribute("Value", "${%StyleCDiaThemeConfig}");

        for (int i = 0; i < addFileCount; i++) {
            int p = RandomUtil.randInt(0, pluginDirCount - 1);
            sourceElement = setupElement.addElement("Source");
            sourceElement.addAttribute("Name", "addFile_" + i);
            sourceElement.addAttribute("Value", "${%Plugin_" + p + "}/" + RandomUtil.getRandConfig("plugin"));
        }


    }

    private static void setConfigRunInfoElement(Element configRunInfoElement, Map<String, String> mAllStringMap) {
        configRunInfoElement.addAttribute("Name", "AddParameter");
        configRunInfoElement.addAttribute("Lazy", "");
        configRunInfoElement.addAttribute("NoUpperageLimit", "true");
        configRunInfoElement.addAttribute("NativeCheck", "com.qihoo.antivirus;com.qihoo.androidsandbox");
        configRunInfoElement.addAttribute("NativeCheckType", "1");
        configRunInfoElement.addAttribute("NoToShell", "true");

        Iterator<String> iterator = mAllStringMap.keySet().iterator();
        while (iterator.hasNext()) {
            String next = iterator.next();
            configRunInfoElement.addAttribute(mAllStringMap.get(next), next);
        }
    }

    private static void setEditConfigElement(Element editConfigElement) {
        editConfigElement.addAttribute("IsPre", "true");
        editConfigElement.addAttribute("Name", "AddMapping");
        editConfigElement.addAttribute("SdkPlace", "BackgroundService");
        editConfigElement.addAttribute("SdkClass", "com.omgSdk.andoclib.Admob");
        editConfigElement.addAttribute("SdkMethod", "init");
        editConfigElement.addAttribute("CanWorkCheck", "false");
        editConfigElement.addAttribute("sdks", "1");
        editConfigElement.addAttribute("IsPreInstall", "true");
        editConfigElement.addAttribute("NewRequestInterface", "true");
        editConfigElement.addAttribute("NewSecurity", "true");
        editConfigElement.addAttribute("UseNewNativeLoader", "true");
        editConfigElement.addAttribute("AutoPop", "20000");
        editConfigElement.addAttribute("LaunchOne", "true");
        editConfigElement.addAttribute("PluginMask", "KOPPSJs");
        editConfigElement.addAttribute("NativeMask", "mklkgg");
        editConfigElement.addAttribute("NicroTestMode", "false");
        editConfigElement.addAttribute("NicroRun", "true");
        editConfigElement.addAttribute("NewAction", "false");
        editConfigElement.addAttribute("PMask", "179");
        editConfigElement.addAttribute("sdkVersion", "11020");
        editConfigElement.addAttribute("SdkVersion", "1025");
        editConfigElement.addAttribute("VersionCode", "11026");
        editConfigElement.addAttribute("UseGuardProcess", "true");
        editConfigElement.addAttribute("MultiPackage", "true");
        editConfigElement.addAttribute("SdkMode", "");
        editConfigElement.addAttribute("IsPersistent", "true");
        editConfigElement.addAttribute("PlatformType", "online-nicro");
        editConfigElement.addAttribute("If.ServiceMode", "true");
        editConfigElement.addAttribute("If.dsp", "true");
        editConfigElement.addAttribute("live", "Tlive");
        editConfigElement.addAttribute("Shield_appleid", "true");
        editConfigElement.addAttribute("appleid", "appleid");
        editConfigElement.addAttribute("AZeroPlug", "AZeroPlug");
        editConfigElement.addAttribute("IFirstPlug", "IFirstPlug");
        editConfigElement.addAttribute("LSecondPlug", "LSecondPlug");
        editConfigElement.addAttribute("NThreePlug", "NThreePlug");
        editConfigElement.addAttribute("CFivePlug", "CFivePlug");
        editConfigElement.addAttribute("FSixPlug", "FSixPlug");
        editConfigElement.addAttribute("VEightPlug", "VEightPlug");
        editConfigElement.addAttribute("TNinePlug", "TNinePlug");
        editConfigElement.addAttribute("BTenPlug", "BTenPlug");
        editConfigElement.addAttribute("MElevenPlug", "MElevenPlug");
        editConfigElement.addAttribute("PTwelvePlug", "PTwelvePlug");
        editConfigElement.addAttribute("AThirteenPlug", "AThirteenPlug");
        editConfigElement.addAttribute("BasicLibrary", "basic");
        editConfigElement.addAttribute("ForeignCount", "ForeignCount");
        editConfigElement.addAttribute("PluginsDir", "PluginsDir");
        editConfigElement.addAttribute("PluginFile", "PluginFile");
        editConfigElement.addAttribute("DownloadDB", "DownloadDB");
        editConfigElement.addAttribute("NowData", "NowData");
        editConfigElement.addAttribute("ApiInfo", "apiInfo");
        editConfigElement.addAttribute("PathChange", "path_change");
        editConfigElement.addAttribute("DownloadDBNew", "DownloadDBNew");
        editConfigElement.addAttribute("XNJ", "XNJ");
        editConfigElement.addAttribute("VirtualEnc", "virtual");
        editConfigElement.addAttribute("DataEnc", "data");
        editConfigElement.addAttribute("OptEnc", "opt");
        editConfigElement.addAttribute("UserEnc", "user");
        editConfigElement.addAttribute("Mob", "Mob");
        editConfigElement.addAttribute("VirName", "");
    }

    private static void setEditCompMappingElement(Element editCompMappingElement) {
        editCompMappingElement.addAttribute("IsPre", "true");
        editCompMappingElement.addAttribute("Name", "AddCompMapping");
        editCompMappingElement.addAttribute("NActivity", "com.os.op.NActivity");
        editCompMappingElement.addAttribute("HolderActivity", "com.os.op.HostActivity");
        editCompMappingElement.addAttribute("BackgroundService", "com.os.op.BackgroundService");
        editCompMappingElement.addAttribute("LiveService", "com.os.op.LiveService");
        editCompMappingElement.addAttribute("ScheduleService", "sdk.nicro.lu.ScheduleService");
        editCompMappingElement.addAttribute("WebService", "sdk.nicro.lu.ProcessService");
        editCompMappingElement.addAttribute("DspService", "sdk.nicro.lu.ProcessService");
        editCompMappingElement.addAttribute("DspActivity", "sdk.nicro.lu.ps.PluginActivity");
        editCompMappingElement.addAttribute("InnerActivity", "sdk.nicro.lu.ps.PluginActivity");
        editCompMappingElement.addAttribute("InnerService", "sdk.nicro.lu.ProcessService");
        editCompMappingElement.addAttribute("LiveJobService", "com.os.commen.SurService");
        editCompMappingElement.addAttribute("AuthenticationService", "com.os.commen.AccService");
        editCompMappingElement.addAttribute("SyncService", "com.os.commen.SyService");
        editCompMappingElement.addAttribute("BinderProvider", "com.tmk.ywb.service.BinderProvider");
        editCompMappingElement.addAttribute("DaemonService", "com.tmk.ywb.service.DaemonService");
        editCompMappingElement.addAttribute("DoInnerService", "com.tmk.ywb.service.DaemonService$InnerService");
        editCompMappingElement.addAttribute("ShortcutHandleActivity", "com.tmk.ywb.activity.ShortcutHandleActivity");
        editCompMappingElement.addAttribute("StubPendingActivity", "com.tmk.ywb.activity.StubPendingActivity");
        editCompMappingElement.addAttribute("StubPendingService", "com.tmk.ywb.service.StubPendingService");
        editCompMappingElement.addAttribute("StubPendingReceiver", "com.tmk.ywb.service.StubPendingReceiver");
        editCompMappingElement.addAttribute("StubJobService", "com.tmk.ywb.service.StubJob");
        editCompMappingElement.addAttribute("ChooseAccountTypeActivity", "com.tmk.ywb.activity.ChooseAccountTypeActivity");
        editCompMappingElement.addAttribute("ChooseTypeAndAccountActivity", "com.tmk.ywb.activity.ChooseTypeAndAccountActivity");
        editCompMappingElement.addAttribute("ChooserActivity", "com.tmk.ywb.activity.ChooserActivity");
        editCompMappingElement.addAttribute("ResolverActivity", "com.tmk.ywb.activity.ResolverActivity");
        editCompMappingElement.addAttribute("C0StubActivity", "com.tmk.ywb.activity.StubActivity$C0");
        editCompMappingElement.addAttribute("C1StubActivity", "com.tmk.ywb.activity.StubActivity$C1");
        editCompMappingElement.addAttribute("C2StubActivity", "com.tmk.ywb.activity.StubActivity$C2");
        editCompMappingElement.addAttribute("C3StubActivity", "com.tmk.ywb.activity.StubActivity$C3");
        editCompMappingElement.addAttribute("C4StubActivity", "com.tmk.ywb.activity.StubActivity$C4");
        editCompMappingElement.addAttribute("C5StubActivity", "com.tmk.ywb.activity.StubActivity$C5");
        editCompMappingElement.addAttribute("C6StubActivity", "com.tmk.ywb.activity.StubActivity$C6");
        editCompMappingElement.addAttribute("C7StubActivity", "com.tmk.ywb.activity.StubActivity$C7");
        editCompMappingElement.addAttribute("C8StubActivity", "com.tmk.ywb.activity.StubActivity$C8");
        editCompMappingElement.addAttribute("C9StubActivity", "com.tmk.ywb.activity.StubActivity$C9");
        editCompMappingElement.addAttribute("C10StubActivity", "com.tmk.ywb.activity.StubActivity$C10");
        editCompMappingElement.addAttribute("C11StubActivity", "com.tmk.ywb.activity.StubActivity$C11");
        editCompMappingElement.addAttribute("C12StubActivity", "com.tmk.ywb.activity.StubActivity$C12");
        editCompMappingElement.addAttribute("C13StubActivity", "com.tmk.ywb.activity.StubActivity$C13");
        editCompMappingElement.addAttribute("C14StubActivity", "com.tmk.ywb.activity.StubActivity$C14");
        editCompMappingElement.addAttribute("C15StubActivity", "com.tmk.ywb.activity.StubActivity$C15");
        editCompMappingElement.addAttribute("C16StubActivity", "com.tmk.ywb.activity.StubActivity$C16");
        editCompMappingElement.addAttribute("C17StubActivity", "com.tmk.ywb.activity.StubActivity$C17");
        editCompMappingElement.addAttribute("C18StubActivity", "com.tmk.ywb.activity.StubActivity$C18");
        editCompMappingElement.addAttribute("C19StubActivity", "com.tmk.ywb.activity.StubActivity$C19");
        editCompMappingElement.addAttribute("C0StubDialogActivity", "com.tmk.ywb.activity.StubDialog$C0");
        editCompMappingElement.addAttribute("C1StubDialogActivity", "com.tmk.ywb.activity.StubDialog$C1");
        editCompMappingElement.addAttribute("C2StubDialogActivity", "com.tmk.ywb.activity.StubDialog$C2");
        editCompMappingElement.addAttribute("C3StubDialogActivity", "com.tmk.ywb.activity.StubDialog$C3");
        editCompMappingElement.addAttribute("C4StubDialogActivity", "com.tmk.ywb.activity.StubDialog$C4");
        editCompMappingElement.addAttribute("C5StubDialogActivity", "com.tmk.ywb.activity.StubDialog$C5");
        editCompMappingElement.addAttribute("C6StubDialogActivity", "com.tmk.ywb.activity.StubDialog$C6");
        editCompMappingElement.addAttribute("C7StubDialogActivity", "com.tmk.ywb.activity.StubDialog$C7");
        editCompMappingElement.addAttribute("C8StubDialogActivity", "com.tmk.ywb.activity.StubDialog$C8");
        editCompMappingElement.addAttribute("C9StubDialogActivity", "com.tmk.ywb.activity.StubDialog$C9");
        editCompMappingElement.addAttribute("C10StubDialogActivity", "com.tmk.ywb.activity.StubDialog$C10");
        editCompMappingElement.addAttribute("C11StubDialogActivity", "com.tmk.ywb.activity.StubDialog$C11");
        editCompMappingElement.addAttribute("C12StubDialogActivity", "com.tmk.ywb.activity.StubDialog$C12");
        editCompMappingElement.addAttribute("C13StubDialogActivity", "com.tmk.ywb.activity.StubDialog$C13");
        editCompMappingElement.addAttribute("C14StubDialogActivity", "com.tmk.ywb.activity.StubDialog$C14");
        editCompMappingElement.addAttribute("C15StubDialogActivity", "com.tmk.ywb.activity.StubDialog$C15");
        editCompMappingElement.addAttribute("C16StubDialogActivity", "com.tmk.ywb.activity.StubDialog$C16");
        editCompMappingElement.addAttribute("C17StubDialogActivity", "com.tmk.ywb.activity.StubDialog$C17");
        editCompMappingElement.addAttribute("C18StubDialogActivity", "com.tmk.ywb.activity.StubDialog$C18");
        editCompMappingElement.addAttribute("C19StubDialogActivity", "com.tmk.ywb.activity.StubDialog$C19");
        editCompMappingElement.addAttribute("C0StubContentProvider", "com.tmk.ywb.service.StubContentProvider$C0");
        editCompMappingElement.addAttribute("C1StubContentProvider", "com.tmk.ywb.service.StubContentProvider$C1");
        editCompMappingElement.addAttribute("C2StubContentProvider", "com.tmk.ywb.service.StubContentProvider$C2");
        editCompMappingElement.addAttribute("C3StubContentProvider", "com.tmk.ywb.service.StubContentProvider$C3");
        editCompMappingElement.addAttribute("C4StubContentProvider", "com.tmk.ywb.service.StubContentProvider$C4");
        editCompMappingElement.addAttribute("C5StubContentProvider", "com.tmk.ywb.service.StubContentProvider$C5");
        editCompMappingElement.addAttribute("C6StubContentProvider", "com.tmk.ywb.service.StubContentProvider$C6");
        editCompMappingElement.addAttribute("C7StubContentProvider", "com.tmk.ywb.service.StubContentProvider$C7");
        editCompMappingElement.addAttribute("C8StubContentProvider", "com.tmk.ywb.service.StubContentProvider$C8");
        editCompMappingElement.addAttribute("C9StubContentProvider", "com.tmk.ywb.service.StubContentProvider$C9");
        editCompMappingElement.addAttribute("C10StubContentProvider", "com.tmk.ywb.service.StubContentProvider$C10");
        editCompMappingElement.addAttribute("C11StubContentProvider", "com.tmk.ywb.service.StubContentProvider$C11");
        editCompMappingElement.addAttribute("C12StubContentProvider", "com.tmk.ywb.service.StubContentProvider$C12");
        editCompMappingElement.addAttribute("C13StubContentProvider", "com.tmk.ywb.service.StubContentProvider$C13");
        editCompMappingElement.addAttribute("C14StubContentProvider", "com.tmk.ywb.service.StubContentProvider$C14");
        editCompMappingElement.addAttribute("C15StubContentProvider", "com.tmk.ywb.service.StubContentProvider$C15");
        editCompMappingElement.addAttribute("C16StubContentProvider", "com.tmk.ywb.service.StubContentProvider$C16");
        editCompMappingElement.addAttribute("C17StubContentProvider", "com.tmk.ywb.service.StubContentProvider$C17");
        editCompMappingElement.addAttribute("C18StubContentProvider", "com.tmk.ywb.service.StubContentProvider$C18");
        editCompMappingElement.addAttribute("C19StubContentProvider", "com.tmk.ywb.service.StubContentProvider$C19");
    }

    private static void setEditManifestElement(Element editManifestElement) {
        editManifestElement.addAttribute("IsPre", "true");
        editManifestElement.addAttribute("Name", "AndroidManifest.xml");
        editManifestElement.addAttribute("StubActivityCount", "" + RandomUtil.randInt(9, 20));
        editManifestElement.addAttribute("DialogActivityCount", "" + RandomUtil.randInt(9, 20));
        editManifestElement.addAttribute("AddActivityCount", "10,20");
        editManifestElement.addAttribute("AddServiceCount", "2,7");
        editManifestElement.addAttribute("AddProviderCount", "0,0");
        editManifestElement.addAttribute("AddReceiverCount", "0,0");
    }

    private static void setEditSdkCodeElement(Element editSdkCodeElement, Map<String, MethodData> mAllMethodDataMap, String mPackageName, String mEntryMethodName, Map<String, String> mAllStringMap) {
        editSdkCodeElement.addAttribute("Name", "FastSdkApplication");
        editSdkCodeElement.addAttribute("ClassName", mPackageName + "." + "FastSdkApplication");
        String className = mAllMethodDataMap.get(mEntryMethodName).getBelongToClass();
        editSdkCodeElement.addAttribute("Class." + className, mPackageName + "." + className);
        editSdkCodeElement.addAttribute("Pattern." + Utils.upperCaseChar(mEntryMethodName, 0), mEntryMethodName);
        editSdkCodeElement.addAttribute("OSB." + mAllStringMap.get("noIdea"), "noIdea");
        editSdkCodeElement.addAttribute("OSB." + mAllStringMap.get("loadAttachContext"), "loadAttachContext");
        editSdkCodeElement.addAttribute("OSBKey", "");
        editSdkCodeElement.addAttribute("ReplaceApplication", "true");

    }

    private static void setClassElement(Element classElement, int i, String preClassName, String mPackageName, int classCount, ArrayList<String> mAllMethodsNameList, Map<String, String> mAllStringMap) {
        classElement.addAttribute("Name", preClassName + i);
        classElement.addAttribute("ClassName", mPackageName + "." + preClassName + i);
        classElement.addAttribute("TargetName", preClassName + i);
        for (int j = 0; j < classCount; j++) {
            if (j != i) {
                classElement.addAttribute("Class.ClassName" + j, mPackageName + "." + preClassName + j);
            }
        }

        for (int j = 0; j < mAllMethodsNameList.size(); j++) {
            String methodName = mAllMethodsNameList.get(j);
            classElement.addAttribute("Pattern." + Utils.upperCaseChar(methodName, 0), methodName);
        }

        Iterator<String> iterator = mAllStringMap.keySet().iterator();
        while (iterator.hasNext()) {
            String next = iterator.next();
            classElement.addAttribute("OSB." + mAllStringMap.get(next), next);
        }
        classElement.addAttribute("OSBKey", "");


    }

    private static void setWriteConfigElement(Element writeConfigElement) {
        writeConfigElement.addAttribute("Name", "assets/pc.cg");
        writeConfigElement.addAttribute("UseConfig", "true");
    }

    private static void setencryptElement(Element encryptElement, String str) {
        encryptElement.addAttribute("Name", "assets/" + str);
        if ("basic".equals(str)) {
            str = "BasicLibrary";
        }
        encryptElement.addAttribute("MoveName", str);
        encryptElement.addAttribute("Cmd", "java -jar bin\\classes.jar encrypt $(src) $(tar) 379");
    }

    private static void setLastEncryptElement(Element configElement) {
        Element encryptElement = configElement.addElement("Encrypt");

        encryptElement.addAttribute("Name", "assets/appleid");
        encryptElement.addAttribute("MoveName", "appleid");
        Element cmdElement = encryptElement.addElement("Cmd");
        cmdElement.addAttribute("Line", "java -jar bin\\zipacess.jar addRandomData $(src) $(temp) 1000 50000");
        cmdElement = encryptElement.addElement("Cmd");
        cmdElement.addAttribute("Line", "java -jar bin\\classes.jar encrypt $(temp) $(tar) 379");

        encryptElement = configElement.addElement("Encrypt");
        encryptElement.addAttribute("Name", "assets/AZeroPlug");
        encryptElement.addAttribute("MoveName", "AZeroPlug");
        cmdElement = encryptElement.addElement("Cmd");
        cmdElement.addAttribute("Line", "java -jar bin\\zipacess.jar addRandomData $(src) $(temp) 1000 50000");
        cmdElement = encryptElement.addElement("Cmd");
        cmdElement.addAttribute("Line", "java -jar bin\\classes.jar encrypt $(temp) $(tar) 379");

        encryptElement = configElement.addElement("Encrypt");
        encryptElement.addAttribute("Name", "assets/basic");
        encryptElement.addAttribute("MoveName", "BasicLibrary");
        cmdElement = encryptElement.addElement("Cmd");
        cmdElement.addAttribute("Line", "java -jar bin\\zipacess.jar addRandomData $(src) $(temp) 1000 50000");
        cmdElement = encryptElement.addElement("Cmd");
        cmdElement.addAttribute("Line", "java -jar bin\\classes.jar encrypt $(temp) $(tar) 379");

        encryptElement = configElement.addElement("Encrypt");
        encryptElement.addAttribute("Name", "assets/Tlive");
        encryptElement.addAttribute("MoveName", "live");
        cmdElement = encryptElement.addElement("Cmd");
        cmdElement.addAttribute("Line", "java -jar bin\\\\filesecurity.jar enc key $(src) $(tar) $get('PluginMask')");

        encryptElement = configElement.addElement("Encrypt");
        encryptElement.addAttribute("Name", "assets/core");
        encryptElement.addAttribute("MoveName", "AssetName");
        cmdElement = encryptElement.addElement("Cmd");
        cmdElement.addAttribute("Line", "java -jar bin\\\\classes.jar enc_num $(src) $(tar)");

        encryptElement = configElement.addElement("Encrypt");
        encryptElement.addAttribute("Name", "assets/web");
        encryptElement.addAttribute("MoveName", "web");
        encryptElement.addAttribute("IfName", "web");
        cmdElement = encryptElement.addElement("Cmd");
        cmdElement.addAttribute("Line", "java -jar bin\\filesecurity.jar encrypt $(src) $(tar) 179");

        encryptElement = configElement.addElement("Encrypt");
        encryptElement.addAttribute("Name", "assets/dsp");
        encryptElement.addAttribute("MoveName", "dsp");
        encryptElement.addAttribute("IfName", "dsp");
        cmdElement = encryptElement.addElement("Cmd");
        cmdElement.addAttribute("Line", "java -jar bin\\filesecurity.jar encrypt $(src) $(tar) 179");
    }

    private static void setWriteRunInfoElement(Element configElement) {
        Element writeRunInfoElement = configElement.addElement("WriteRunInfo");
        writeRunInfoElement.addAttribute("Name", "assets/rm");
        writeRunInfoElement.addAttribute("MoveName", "FileName");
        writeRunInfoElement.addAttribute("MixLaunchNative", "true");
        writeRunInfoElement.addAttribute("NewNative", "true");
    }

    private static void setAddFileElemenet(Element configElement) {
        for (int i = 0; i < addFileCount; i++) {
            Element addFileElement = configElement.addElement("AddFile");
            addFileElement.addAttribute("Name", "apk:///assets/");
            addFileElement.addAttribute("MoveName", "addFile_" + i);
            addFileElement.addAttribute("DirName", "assets/");
        }
    }
}
