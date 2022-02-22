<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form :form="form" slot="detail">
        <a-row>
          <a-col :span="24">
            <a-form-item label="项目Id" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['projectId']" placeholder="请输入项目Id"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="项目名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['projectName']" placeholder="请输入项目名称"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="规划id" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['planId']" placeholder="请输入规划id"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="规划名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['planName']" placeholder="请输入规划名称"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="环境id" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['envId']" placeholder="请输入环境id"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="环境名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['envName']" placeholder="请输入环境名称"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="设备名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['equipmentName']" placeholder="请输入设备名称"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="设备类型 0串口 1 外部工装" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['equipmentType']" placeholder="请输入设备类型 0串口 1 外部工装"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="虚拟机id" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['vmId']" placeholder="请输入虚拟机id"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="虚拟机名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['vmName']" placeholder="请输入虚拟机名称"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="状态0 绑定 1 未绑定" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['status']" placeholder="请输入状态0 绑定 1 未绑定"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="服务模式0 客户端 1 服务端" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['serviceMode']" placeholder="请输入服务模式0 客户端 1 服务端"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="目标地址（串口地址）" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['desAddress']" placeholder="请输入目标地址（串口地址）"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="端口号（虚拟机端口号）" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['vmPort']" placeholder="请输入端口号（虚拟机端口号）"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="被绑定usb(外部工装有的)" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['openstackBoundUsb']" placeholder="请输入被绑定usb(外部工装有的)"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="主机" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['openstackHost']" placeholder="请输入主机"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col v-if="showFlowSubmitButton" :span="24" style="text-align: center">
            <a-button @click="submitForm">提 交</a-button>
          </a-col>
        </a-row>
      </a-form>
    </j-form-container>
  </a-spin>
</template>

<script>

  import { httpAction, getAction } from '@/api/manage'
  import pick from 'lodash.pick'
  import { validateDuplicateValue } from '@/utils/util'
  import JFormContainer from '@/components/jeecg/JFormContainer'

  export default {
    name: 'VmSerportExdevRelationForm',
    components: {
      JFormContainer,
    },
    props: {
      //流程表单data
      formData: {
        type: Object,
        default: ()=>{},
        required: false
      },
      //表单模式：true流程表单 false普通表单
      formBpm: {
        type: Boolean,
        default: false,
        required: false
      },
      //表单禁用
      disabled: {
        type: Boolean,
        default: false,
        required: false
      }
    },
    data () {
      return {
        form: this.$form.createForm(this),
        model: {},
        labelCol: {
          xs: { span: 24 },
          sm: { span: 5 },
        },
        wrapperCol: {
          xs: { span: 24 },
          sm: { span: 16 },
        },
        confirmLoading: false,
        validatorRules: {
        },
        url: {
          add: "/vmserportexdevrelation/vmSerportExdevRelation/add",
          edit: "/vmserportexdevrelation/vmSerportExdevRelation/edit",
          queryById: "/vmserportexdevrelation/vmSerportExdevRelation/queryById"
        }
      }
    },
    computed: {
      formDisabled(){
        if(this.formBpm===true){
          if(this.formData.disabled===false){
            return false
          }
          return true
        }
        return this.disabled
      },
      showFlowSubmitButton(){
        if(this.formBpm===true){
          if(this.formData.disabled===false){
            return true
          }
        }
        return false
      }
    },
    created () {
      //如果是流程中表单，则需要加载流程表单data
      this.showFlowData();
    },
    methods: {
      add () {
        this.edit({});
      },
      edit (record) {
        this.form.resetFields();
        this.model = Object.assign({}, record);
        this.visible = true;
        this.$nextTick(() => {
          this.form.setFieldsValue(pick(this.model,'projectId','projectName','planId','planName','envId','envName','equipmentName','equipmentType','vmId','vmName','status','serviceMode','desAddress','vmPort','openstackBoundUsb','openstackHost'))
        })
      },
      //渲染流程表单数据
      showFlowData(){
        if(this.formBpm === true){
          let params = {id:this.formData.dataId};
          getAction(this.url.queryById,params).then((res)=>{
            if(res.success){
              this.edit (res.result);
            }
          });
        }
      },
      submitForm () {
        const that = this;
        // 触发表单验证
        this.form.validateFields((err, values) => {
          if (!err) {
            that.confirmLoading = true;
            let httpurl = '';
            let method = '';
            if(!this.model.id){
              httpurl+=this.url.add;
              method = 'post';
            }else{
              httpurl+=this.url.edit;
               method = 'put';
            }
            let formData = Object.assign(this.model, values);
            console.log("表单提交数据",formData)
            httpAction(httpurl,formData,method).then((res)=>{
              if(res.success){
                that.$message.success(res.message);
                that.$emit('ok');
              }else{
                that.$message.warning(res.message);
              }
            }).finally(() => {
              that.confirmLoading = false;
            })
          }
         
        })
      },
      popupCallback(row){
        this.form.setFieldsValue(pick(row,'projectId','projectName','planId','planName','envId','envName','equipmentName','equipmentType','vmId','vmName','status','serviceMode','desAddress','vmPort','openstackBoundUsb','openstackHost'))
      },
    }
  }
</script>