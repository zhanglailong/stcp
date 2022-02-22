<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
        </a-row>
      </a-form>
    </div>
    <!-- 查询区域-END -->

    <!-- 操作按钮区域 -->
    <div class="table-operator">
      <a-button @click="handleAdd" type="primary" icon="plus">新增</a-button>
      <a-button type="primary" icon="download" @click="handleExportXls('虚拟机串口外部工装关系表')">导出</a-button>
      <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl" @change="handleImportExcel">
        <a-button type="primary" icon="import">导入</a-button>
      </a-upload>
      <!-- 高级查询区域 -->
      <j-super-query :fieldList="superFieldList" ref="superQueryModal" @handleSuperQuery="handleSuperQuery"></j-super-query>
      <a-dropdown v-if="selectedRowKeys.length > 0">
        <a-menu slot="overlay">
          <a-menu-item key="1" @click="batchDel"><a-icon type="delete"/>删除</a-menu-item>
        </a-menu>
        <a-button style="margin-left: 8px"> 批量操作 <a-icon type="down" /></a-button>
      </a-dropdown>
    </div>

    <!-- table区域-begin -->
    <div>
      <div class="ant-alert ant-alert-info" style="margin-bottom: 16px;">
        <i class="anticon anticon-info-circle ant-alert-icon"></i> 已选择 <a style="font-weight: 600">{{ selectedRowKeys.length }}</a>项
        <a style="margin-left: 24px" @click="onClearSelected">清空</a>
      </div>

      <a-table
        ref="table"
        size="middle"
        :scroll="{x:true}"
        bordered
        rowKey="id"
        :columns="columns"
        :dataSource="dataSource"
        :pagination="ipagination"
        :loading="loading"
        :rowSelection="{selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
        class="j-table-force-nowrap"
        @change="handleTableChange">

        <template slot="htmlSlot" slot-scope="text">
          <div v-html="text"></div>
        </template>
        <template slot="imgSlot" slot-scope="text">
          <span v-if="!text" style="font-size: 12px;font-style: italic;">无图片</span>
          <img v-else :src="getImgView(text)" height="25px" alt="" style="max-width:80px;font-size: 12px;font-style: italic;"/>
        </template>
        <template slot="fileSlot" slot-scope="text">
          <span v-if="!text" style="font-size: 12px;font-style: italic;">无文件</span>
          <a-button
            v-else
            :ghost="true"
            type="primary"
            icon="download"
            size="small"
            @click="downloadFile(text)">
            下载
          </a-button>
        </template>

        <span slot="action" slot-scope="text, record">
          <a @click="handleEdit(record)">编辑</a>

          <a-divider type="vertical" />
          <a-dropdown>
            <a class="ant-dropdown-link">更多 <a-icon type="down" /></a>
            <a-menu slot="overlay">
              <a-menu-item>
                <a @click="handleDetail(record)">详情</a>
              </a-menu-item>
              <a-menu-item>
                <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">
                  <a>删除</a>
                </a-popconfirm>
              </a-menu-item>
            </a-menu>
          </a-dropdown>
        </span>

      </a-table>
    </div>

    <vm-serport-exdev-relation-modal ref="modalForm" @ok="modalFormOk"></vm-serport-exdev-relation-modal>
  </a-card>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import VmSerportExdevRelationModal from './modules/VmSerportExdevRelationModal'
  import JSuperQuery from '@/components/jeecg/JSuperQuery.vue'

  export default {
    name: 'VmSerportExdevRelationList',
    mixins:[JeecgListMixin, mixinDevice],
    components: {
      VmSerportExdevRelationModal,
      JSuperQuery,
    },
    data () {
      return {
        description: '虚拟机串口外部工装关系表管理页面',
        // 表头
        columns: [
          {
            title: '#',
            dataIndex: '',
            key:'rowIndex',
            width:60,
            align:"center",
            customRender:function (t,r,index) {
              return parseInt(index)+1;
            }
          },
          {
            title:'项目Id',
            align:"center",
            dataIndex: 'projectId'
          },
          {
            title:'项目名称',
            align:"center",
            dataIndex: 'projectName'
          },
          {
            title:'规划id',
            align:"center",
            dataIndex: 'planId'
          },
          {
            title:'规划名称',
            align:"center",
            dataIndex: 'planName'
          },
          {
            title:'环境id',
            align:"center",
            dataIndex: 'envId'
          },
          {
            title:'环境名称',
            align:"center",
            dataIndex: 'envName'
          },
          {
            title:'设备名称',
            align:"center",
            dataIndex: 'equipmentName'
          },
          {
            title:'设备类型 0串口 1 外部工装',
            align:"center",
            dataIndex: 'equipmentType'
          },
          {
            title:'虚拟机id',
            align:"center",
            dataIndex: 'vmId'
          },
          {
            title:'虚拟机名称',
            align:"center",
            dataIndex: 'vmName'
          },
          {
            title:'状态0 绑定 1 未绑定',
            align:"center",
            dataIndex: 'status'
          },
          {
            title:'服务模式0 客户端 1 服务端',
            align:"center",
            dataIndex: 'serviceMode'
          },
          {
            title:'目标地址（串口地址）',
            align:"center",
            dataIndex: 'desAddress'
          },
          {
            title:'端口号（虚拟机端口号）',
            align:"center",
            dataIndex: 'vmPort'
          },
          {
            title:'被绑定usb(外部工装有的)',
            align:"center",
            dataIndex: 'openstackBoundUsb'
          },
          {
            title:'主机',
            align:"center",
            dataIndex: 'openstackHost'
          },
          {
            title: '操作',
            dataIndex: 'action',
            align:"center",
            fixed:"right",
            width:147,
            scopedSlots: { customRender: 'action' }
          }
        ],
        url: {
          list: "/vmserportexdevrelation/vmSerportExdevRelation/list",
          delete: "/vmserportexdevrelation/vmSerportExdevRelation/delete",
          deleteBatch: "/vmserportexdevrelation/vmSerportExdevRelation/deleteBatch",
          exportXlsUrl: "/vmserportexdevrelation/vmSerportExdevRelation/exportXls",
          importExcelUrl: "vmserportexdevrelation/vmSerportExdevRelation/importExcel",
          
        },
        dictOptions:{},
        superFieldList:[],
      }
    },
    created() {
    this.getSuperFieldList();
    },
    computed: {
      importExcelUrl: function(){
        return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`;
      },
    },
    methods: {
      initDictConfig(){
      },
      getSuperFieldList(){
        let fieldList=[];
        fieldList.push({type:'string',value:'projectId',text:'项目Id',dictCode:''})
        fieldList.push({type:'string',value:'projectName',text:'项目名称',dictCode:''})
        fieldList.push({type:'string',value:'planId',text:'规划id',dictCode:''})
        fieldList.push({type:'string',value:'planName',text:'规划名称',dictCode:''})
        fieldList.push({type:'string',value:'envId',text:'环境id',dictCode:''})
        fieldList.push({type:'string',value:'envName',text:'环境名称',dictCode:''})
        fieldList.push({type:'string',value:'equipmentName',text:'设备名称',dictCode:''})
        fieldList.push({type:'string',value:'equipmentType',text:'设备类型 0串口 1 外部工装',dictCode:''})
        fieldList.push({type:'string',value:'vmId',text:'虚拟机id',dictCode:''})
        fieldList.push({type:'string',value:'vmName',text:'虚拟机名称',dictCode:''})
        fieldList.push({type:'string',value:'status',text:'状态0 绑定 1 未绑定',dictCode:''})
        fieldList.push({type:'string',value:'serviceMode',text:'服务模式0 客户端 1 服务端',dictCode:''})
        fieldList.push({type:'string',value:'desAddress',text:'目标地址（串口地址）',dictCode:''})
        fieldList.push({type:'string',value:'vmPort',text:'端口号（虚拟机端口号）',dictCode:''})
        fieldList.push({type:'string',value:'openstackBoundUsb',text:'被绑定usb(外部工装有的)',dictCode:''})
        fieldList.push({type:'string',value:'openstackHost',text:'主机',dictCode:''})
        this.superFieldList = fieldList
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>