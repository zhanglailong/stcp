package org.jeecg.modules.common;

/**
 * @author zlf
 * @version V1.0
 * @date 2021/5/6
 */
public interface CommonConstant {
    public final static Integer SOCKET_REGISTER_CODE_200 = 200;
    /**
     * socket注册状态
     */
    public final static Integer SOCKET_REGISTER_CODE_201 = 201;
    /**
     * socket客户端发送数据状态
     */
    public final static Integer SOCKET_REGISTER_CODE_202 = 202;
    public final static Integer SOCKET_REGISTER_CODE_203 = 203;

    /**
     * 数据状态0
     */
    public final static String DATA_STATE_0 = "0";
    public final static Integer DATA_INT_STATE_0 = 0;
    public final static Integer DATA_INT_STATE_1 = 1;
    public final static String DATA_STR_0 = "0";
    public final static String DATA_STR_1 = "1";
    public final static String DATA_STR_2 = "2";
    public final static Integer DATA_INT_0 = 0;
    public final static Integer DATA_INT_1 = 1;
    public final static Integer DATA_INT_2 = 2;
    public final static Integer DATA_INT_3 = 3;
    public final static Integer DATA_INT_4 = 4;
    public final static Integer DATA_INT_5 = 5;
    public final static Integer DATA_INT_6 = 6;
    public final static Integer DATA_INT_7 = 7;
    public final static Integer DATA_INT_8 = 8;
    public final static Integer DATA_INT_9 = 9;
    public final static Integer DATA_INT_10 = 10;
    public final static Integer DATA_INT_12 = 12;
    public final static Integer DATA_INT_22 = 22;
    public final static Integer DATA_INT_60 = 60;
    public final static Integer DATA_INT_50 = 50;
    public final static Integer DATA_INT_200 = 200;
    public final static String DATA_STR_100 = "100";
    public final static String DATA_UNDEFINED= "undefined";
    public final static String DATA_STR_3 = "3";
    /**
     * 虚拟删除0
     */
    public final static Integer DATA_INT_IDEL_0 = 0;
    /**
     * 虚拟删除1 删除
     */
    public final static Integer DATA_INT_IDEL_1 = 1;
    /**
     * 强制虚拟删除2 删除
     */
    public final static Integer DATA_INT_IDEL_2 = 2;

    /**
     * 规划-网络type值
     */
    public final static String PLAN_TYPE_NET = "net";
    /**
     * 规划-子网type值
     */
    public final static String PLAN_TYPE_CHILDNET = "childNet";
    /**
     * 规划-虚拟机type值
     */
    public final static String PLAN_TYPE_VIRTUAL= "virtual";
    /**
     * 规划-路由type值
     */
    public final static String PLAN_TYPE_ROUTE= "route";

    /**
     * 规划-串口type值
     */
    public final static String PLAN_TYPE_PORT= "port";

    /**
     * 规划-外部工装type值
     */
    public final static String PLAN_TYPE_EXTERNALDEVICES= "externalDevices";

    /**
     * resttemplate 请求方式
     */
    public final static String REST_TEMPLATE_GET= "get";
    public final static String REST_TEMPLATE_POST= "post";
    public final static String REST_TEMPLATE_ACCESS_TOKEN= "access_token";
    public final static String REST_TEMPLATE_EXPIRES_AT= "expires_at";
    public final static String REST_TEMPLATE_RESULT_CODE= "code";
    public final static String REST_TEMPLATE_RESULT_MSG = "msg";

    /**
     *字符串
     */
    public final static String DATA_STRING_IDEL= "idel";

    /**
     * 虚拟机ip
     */
    public final static String DATA_STRING_NETWORK_ADDRESS= "network_address";

    public final static String DATA_STRING_ID= "id";
    public final static String DATA_STRING_CREATE_BY= "create_by";
    public final static String DATA_STRING_IDS= "ids";
    public final static String DATA_STRING_STATE= "state";
    public final static String DATA_STRING_DATA= "data";
    public final static String DATA_STRING_URL="url";
    public final static String DATA_STRING_STACK= "stack";
    public final static String DATA_STRING_STACK_STATUS= "stack_status";
    public final static String DATA_STRING_STACK_STATUS_REASON= "stack_status_reason";
    public final static String DATA_STRING_STACK_ID= "{stack_id}";
    public final static String DATA_STRING_DEVICE_ID= "{id}";
    public final static String DATA_STRING_STACK_SERVER_ID= "{server_id}";
    public final static String DATA_STRING_STACK_TEST_ID= "{test_id}";
    public final static String DATA_STRING_STACK_DEVICE_ID= "{device_id}";
    public final static String DATA_STRING_STACK_PLAN_ID= "plan_id";
    public final static String DATA_STRING_VM_ID= "vm_id";
    public final static String DATA_STRING_OS_NOVA_SERVER= "OS::Nova::Server";
    public final static String DATA_STRING_VIR_SERVER= "Server-";
    public final static String DATA_STRING_SERVERS_SERVER= "server";
    public final static String DATA_STRING_SERVER_IP= "private_v4";
    public final static String DATA_STRING_ADDRESS= "addresses";
    public final static String DATA_STRING_SERVER_STATUS= "status";
    public final static String DATA_STRING_SERVER_MODE= "server_mode";
    public final static String DATA_STRING_DOMAIN_ID= "domain_id";
    public final static String DATA_STRING_DOMAIN_NAME= "domain_name";
    public final static String DATA_STRING_CREATED_AT= "created_at";
    public final static String DATA_STRING_OPENSTACK_DEVICE_ID= "openstack_device_id";
    public final static String DATA_STRING_EQUIPMENT_TYPE= "equipment_type";
    public final static String DATA_STRING_SERIAL= "serial";
    public final static String DATA_STRING_HOST= "host";
    public final static String DATA_STRING_PORT= "port";
    public final static String DATA_STRING_SUCCESS= "success";
    public final static String DATA_STRING_STACK_NAME_S= "s-";
    public final static String DATA_STRING_SERVER_SNAPSHOT = "snapshot";
    public final static String DATA_STRING_SERVER_NAME = "name";
    public final static String DATA_STRING_SERVER_STATUS_REASON= "status_reason";
    public final static String DATA_STRING_SERVER_FIXED = "fixed";
    public final static String DATA_STRING_SERVER_FLOATING = "floating";
    public final static String DATA_STRING_STATS = "stats";
    public final static String DATA_STRING_STATUS = "{status}";
    public final static String DATA_STRING_SNAPSHOT_ID = "{snapshot_id}";
    public final static String DATA_STRING_IMAGE = "image";
    public final static String DATA_STRING_FLAVOR = "flavor";
    public final static String DATA_STRING_ORIGINAL_NAME = "original_name";
    public final static String DATA_STRING_TEST_ID = "test_id";
    public final static String SEPARATOR = "_";
    public final static String REGULAR_EXPRESSION= "[a-zA-Z]";
    public final static String STACKID = "stackId";
    public final static String EVN_ID = "env_id";
    public final static String SNAPSHOT_ID = "snapshot_id";
    public final static String STRING_STACK_ID = "stack_id";
    public final static String OPENSTACK_ID = "openstack_id";
    public final static String SERVER_ID= "server_id";
    public final static String VM_CODE= "vm_code";
    public final static String DATA_CREATE_TIME = "create_time";
    public final static String JMETERHOME = "E:\\apache-jmeter-5.4.1";
    public final static String PATHJMX = "E:\\home\\tools\\";
    public final static String REPLAYLOGPATH = "";
    public final static String SYMBOL = "\\";
    public final static String JMX = ".jmx";
    public final static String JTL = ".jtl";
    public final static String JMETER_N_T = "\\jmeter.bat -n -t ";
    public final static String JMETER_N_T_S = "\\jmeter -n -t ";
    public final static String JMETER__L = " -l ";
    public final static String JMETER_E_O = " -e -o ";
    public final static String WLRUN_RUN ="wlrun.exe -Run -TestPath ";
    public final static String RESULTNAME = "-ResultName";
    public final static String CMD_EXE_START = "cmd.exe /c start ";
    public final static String LRS = ".lrs";
    public final static String LRR = ".lrr";
    public final static String RESULTPATH = "AnalysisUI.exe -RESULTPATH ";
    public final static String HTML_NAME = " html";
    public final static String SEPARATORS = " -TEMPLATENAME";
    public final static String RUN_DONTCLOSE = " -Run -DontClose";
    public final static String TOOL_NAME = "jmeter";
    public final static String CMD_EXIT = " exit";
    public final static String Y_M_D = "yyyy-MM-dd";
    public final static String WINDOWS = "windows";
    public final static String HOME = "/home/";
    public final static String C_PATH = "c:/";
    public final static String OS_NAME = "os.name";
    public final static String ZIP = ".zip";
    public final static String APP_SCAN = "appscancmd e /su ";
    public final static String APP_SCAN_PATH = "appscan";
    public final static String UNDER_STAND = "underStand";
    public final static String SCAN = "\\test.scan";
    public final static String REPORT_FILE = " /rt pdf /report_file ";
    public final static String PDF_SCAN = "\\test.pdf";
    public final static String UND_CREATE = "und create -languages c++ ";
    public final static String MYDB_UDB = "myDb.udb";
    public final static String UND_ADD = "und add ";
    public final static String UND_ANALYZE = "und analyze -all ";
    public final static String UND_REPORT = "und report ";
    public final static String D_D = " /d ";
    public final static String DEL_FLAG = "del_flag";
    public final static String COMMA = ",";
    public final static String HOST_NAME = "host_name";
    public final static String OS_EXT_SRV_ATTR_HOST = "OS-EXT-SRV-ATTR:host";
    public final static String SERVICE_ID = "{service_id}";
    public final static String SCHEDULED_QUEUE_LOCK= "scheduled_queue_lock";
    public final static String RESOURCES = "resources";
    public final static String LOGICAL_RESOURCE_ID = "logical_resource_id";
    public final static String OPEN_VM_ID = "open_vm_id";
    public final static String RESOURCE_STATUS = "resource_status";
    public final static String IMAGE_ID = "{image_id}";
    public final static String BLOCK_DEVICE_MAPPING = "block_device_mapping";
    public final static String VOLUME_SIZE = "volume_size";
    public final static String ACTION = "{action}";
    public final static String SNAPSHOT = "SNAPSHOT";
    public final static String COMPLETE = "COMPLETE";
    public final static String SNAPSHOT_COMPLETE = "SNAPSHOT_COMPLETE";
    public final static String BACKUP_COMPLETE = "BACKUP_COMPLETE";
    public final static String CREATION_TIME = "creation_time";
    public final static String VM_ID = "vm_id";
    public final static String BACKUP_RESTORE_COMPLETE = "BACKUP_RESTORE_COMPLETE";
    public final static String RESTORE_COMPLETE = "RESTORE_COMPLETE";
    public final static String RESTORE = "RESTORE";
    //裸机标识
    public final static String BARE_VIRTUAL = "bareVirtual";
    public final static String NODE_LIST = "nodeList";
    public final static String TYPE = "type";
    public final static String MIRROR = "mirror";
    public final static String VIR_INNER = "virInner";
    public final static String VIR_CPU = "virCpu";
    public final static String VIR_DISK = "virDisk";
    public final static String NAME = "name";
    public final static String TRUE = "true";
    public final static String RANDOM = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public final static String LIVE_MIGRATION = "live-migration";
    public final static String CONFIRM_RESIZE = "confirmResize";
    public final static String MIGRATE = "migrate";
    public final static String ACTIONS = "actions";
    public final static String ACTIONL = "action";
    public final static String INSTANCE_UUID = "instance_uuid";
    public final static String START_TIME = "start_time";
    public final static String UPDATED_AT = "updated_at";
    public final static String HOSTROUTES = "[]";
    public final static String SUBNET_ID = "{subnet_id}";
    public final static Long TIME_STEP_SIZE = 8 * 60 * 60 * 1000L;
}
