package org.jeecg.modules.plan.service.impl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.commons.lang.StringUtils;
import org.jeecg.modules.Imageotherinfo.entity.ImageOtherInfo;
import org.jeecg.modules.Imageotherinfo.service.IImageOtherInfoService;
import org.jeecg.modules.common.CommonConstant;
import org.jeecg.modules.common.enums.ImageTypeEnum;
import org.jeecg.modules.openstack.entity.ImageList;
import org.jeecg.modules.openstack.service.IStackQueueService;
import org.jeecg.modules.openstack.service.ext.OpenStackServiceExt;
import org.jeecg.modules.plan.entity.ImageManage;
import org.jeecg.modules.plan.mapper.ImageManageMapper;
import org.jeecg.modules.plan.service.IImageManageService;
import org.jeecg.modules.running.tools.entity.RunningToolsList;
import org.jeecg.modules.running.tools.service.IRunningToolsListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


/**
 * @author yeyl
 */
@Service
public class ImageManageServiceImpl extends ServiceImpl<ImageManageMapper, ImageManage> implements IImageManageService {

    @Resource
    private IImageOtherInfoService iImageOtherInfoService;
    @Resource
    private  IRunningToolsListService iRunningToolsListService;

    @Resource
    ImageManageMapper imageManageMapper;
    @Resource
    OpenStackServiceExt openStackServiceExt;

    @Resource
    private IStackQueueService iStackQueueService;



    @Override
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public void edit(ImageManage imageManage) {
        try {
            //原来的
            ImageManage sourceImageManage = this.getById(imageManage.getId());
            //修改镜像表
             String otherSoftware = imageManage.getOtherSoftware();
             if (StringUtils.isNotBlank(otherSoftware)) {
                 String dealCommaString = this.getDealCommaString(otherSoftware);
                 if (StringUtils.isNotBlank(dealCommaString)){
                     sourceImageManage.setOtherSoftware(dealCommaString);
                 }
             }
            List<RunningToolsList> runningToolsLists=new ArrayList<>();
            runningToolsLists = getRunningToolsLists(imageManage, runningToolsLists);
            sourceImageManage.setTestTools(imageManage.getTestTools());
            sourceImageManage.setTestToolNames(imageManage.getTestToolNames());
            sourceImageManage.setSystemVersion(imageManage.getSystemVersion());
            sourceImageManage.setSysType(imageManage.getSysType());
            sourceImageManage.setName(imageManage.getName());
            sourceImageManage.setRemark(imageManage.getRemark());
            if (!this.updateById(sourceImageManage)) {
                throw new UnsupportedOperationException("修改失败!");
            } else {
                //批量修改 测试工具，其他软件表关联表
                //先删除
                QueryWrapper<ImageOtherInfo> queryImageOtherWrapper = new QueryWrapper<>();
                queryImageOtherWrapper.eq(CommonConstant.DATA_STRING_IDEL, CommonConstant.DATA_INT_IDEL_0);
                queryImageOtherWrapper.eq(CommonConstant.OPENSTACK_ID, imageManage.getOpenstackId());
                List<ImageOtherInfo> imageOtherInfos = iImageOtherInfoService.list(queryImageOtherWrapper);
                if (imageOtherInfos.size()!=0) {
                    iImageOtherInfoService.removeByIds(imageOtherInfos.stream().map(ImageOtherInfo::getId).collect(Collectors.toList()));
                }
                //批量新增
                iImageOtherInfoService.saveInfos(sourceImageManage,runningToolsLists);
            }
        }catch (Exception e){
            log.error("修改异常,原因:"+e.getMessage());
        }

    }
    @Override
    public List<RunningToolsList> getRunningToolsLists(ImageManage imageManage, List<RunningToolsList> runningToolsLists) {
        if (StringUtils.isNotBlank(imageManage.getTestTools())) {
            List<String> tools = Arrays.asList(imageManage.getTestTools().split(","));
            QueryWrapper<RunningToolsList> queryWrapper = new QueryWrapper<>();
            queryWrapper.in(CommonConstant.DATA_STRING_ID, tools);
            queryWrapper.eq(CommonConstant.DEL_FLAG, CommonConstant.DATA_INT_IDEL_0);
            runningToolsLists = iRunningToolsListService.list(queryWrapper);
            if (runningToolsLists.size() < tools.size()) {
                throw new UnsupportedOperationException("有测试工具不存在或者被删除，请刷新重试");
            }
            List<String> runningToolNames = runningToolsLists.stream().map(RunningToolsList::getToolsName).collect(Collectors.toList());
            String nameString = String.join(",", runningToolNames);
            List<String> runningToolIds = runningToolsLists.stream().map(RunningToolsList::getId).collect(Collectors.toList());
            String idString = String.join(",", runningToolIds);
            imageManage.setTestTools(idString);
            imageManage.setTestToolNames(nameString);
        }else{
            imageManage.setTestTools(null);
            imageManage.setTestToolNames(null);
        }
        return runningToolsLists;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public boolean deleteBatch(List<String> stringList) {
        try {
            stringList.forEach(s -> {
                ImageManage im = this.getById(s);
                if (im == null) {
                    throw new UnsupportedOperationException("有镜像不存在或者被删除，请刷新重试");
                }
                if(!iStackQueueService.deleteImages(im.getId(), im.getOpenstackId())){
                    throw new UnsupportedOperationException("批量删除失败，请重试！");
                }
            });
            return true;
        }catch (Exception e){
            log.error("批量删除异常"+e.getMessage());
            return false;
        }
    }

    @Override
    public List<ImageManage> getImageManagesByOther(String otherSoftware, String testTools, String systemVersion,String sysType) {
        QueryWrapper<ImageManage> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq(CommonConstant.DATA_STRING_IDEL, CommonConstant.DATA_INT_IDEL_0);
        String dealCommaOtherSoftware = this.getDealCommaString(otherSoftware);
        List<ImageManage> imageManages = this.list(queryWrapper);
        List<ImageManage> isConformImas=new ArrayList<>();
        imageManages.forEach(imageManage -> {
            boolean isConform=true;
            if(StringUtils.isNotBlank(testTools)) {
                List<String> testToolsInput = Arrays.asList(testTools.split(","));
                if (StringUtils.isNotBlank(imageManage.getTestTools())) {
                    List<String> testToolStrings = Arrays.asList(imageManage.getTestTools().split(","));
                    if (!testToolStrings.containsAll(testToolsInput)) {
                        isConform = false;
                    }
                }else{
                    isConform = false;
                }
            }
            if(StringUtils.isNotBlank(dealCommaOtherSoftware)) {
                List<String> otherSoftWaresInput = Arrays.asList(dealCommaOtherSoftware.split(","));
                if (StringUtils.isNotBlank(imageManage.getOtherSoftware())) {
                    List<String> otherSoftwareStrings = Arrays.asList(imageManage.getOtherSoftware().split(","));
                    if (!otherSoftwareStrings.containsAll(otherSoftWaresInput)) {
                        isConform = false;
                    }
                }else{
                    isConform = false;
                }
            }
            if(StringUtils.isNotBlank(systemVersion)) {
                if (!systemVersion.equals(imageManage.getSystemVersion())){
                    isConform=false;
                }
            }
            if(StringUtils.isNotBlank(sysType)) {
                if (!sysType.equals(imageManage.getSysType())){
                    isConform=false;
                }
            }
            if (isConform){
                isConformImas.add(imageManage);
            }
        });
        return isConformImas;
    }

    @Override
    public ImageManage getImageByOpenstackId(String openstackId) {
        QueryWrapper<ImageManage> queryWrapper=new QueryWrapper<ImageManage>();
        queryWrapper.eq(CommonConstant.DATA_STRING_IDEL, CommonConstant.DATA_INT_IDEL_0);
        queryWrapper.eq("openstack_id", openstackId);
        return imageManageMapper.selectOne(queryWrapper);
    }

    @Override
    public ImageManage getByOpenstackId(String openstackId) {
        try {
            if (StringUtils.isBlank(openstackId)){
                throw new UnsupportedOperationException("openstackId不可以为空");
            }
            QueryWrapper<ImageManage> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq(CommonConstant.DATA_STRING_IDEL, CommonConstant.DATA_INT_IDEL_0);
            queryWrapper.eq(CommonConstant.OPENSTACK_ID, openstackId);
            return this.getOne(queryWrapper);
        }catch (Exception e){
            log.error("查询异常,原因:"+e.getMessage());
        }
        return null;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public void sync() {
        try {
            ImageList imageList = openStackServiceExt.getImageList();
            if (imageList != null && imageList.getCode() == CommonConstant.DATA_INT_0 && imageList.getDatas() != null){
                List<ImageList.Images> images = imageList.getDatas().getImages();
                if (images != null && images.size() >0){
                    List<ImageManage> addImages=new ArrayList<>();
                    List<ImageOtherInfo> addImageOtherInfos = new ArrayList<>();
                    //获取镜像 遍历镜像查看是否存在
                    images.forEach(im-> {
                        QueryWrapper<ImageManage> queryWrapper = new QueryWrapper<>();
                        queryWrapper.eq(CommonConstant.OPENSTACK_ID,im.getId());
                        queryWrapper.eq(CommonConstant.DATA_STRING_IDEL, CommonConstant.DATA_INT_IDEL_0);
                        List<ImageManage> imageManages = this.list(queryWrapper);
                        if (imageManages == null||imageManages.size()==0) {
                            ImageManage imageManage = new ImageManage();
                            imageManage.setIdel(CommonConstant.DATA_INT_IDEL_0);
                            imageManage.setOpenstackId(im.getId());
                            imageManage.setImageName(im.getName());
                            if (im.getProperties()!=null&&StringUtils.isNotBlank(im.getProperties().getOsDistribution())) {
                                imageManage.setSysType(im.getProperties().getOsDistribution());
                            }
                            imageManage.setImageStatus(ImageTypeEnum.toEnum(im.getStatus().toUpperCase()));
                            addImages.add(imageManage);
                            ImageOtherInfo imageOtherInfo = iImageOtherInfoService.getImageOtherInfo(imageManage);
                            addImageOtherInfos.add(imageOtherInfo);
                        }
                    });
                    this.saveBatch(addImages);
                    //同步镜像与系统版本 测试工具 其他软件表
                    iImageOtherInfoService.saveBatch(addImageOtherInfos);
                    //多的同步删除
                    List<ImageManage> dueImages=new ArrayList<>();
                    QueryWrapper<ImageManage> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq(CommonConstant.DATA_STRING_IDEL, CommonConstant.DATA_INT_IDEL_0);
                    List<ImageManage> imageManages = this.list(queryWrapper);
                    imageManages.forEach(imageManage -> {
                        List<String> imagesIds = images.stream().filter(im -> StringUtils.isNotBlank(im.getId())).map(ImageList.Images::getId).collect(Collectors.toList());
                        if (!imagesIds.contains(imageManage.getOpenstackId())){
                            //如果接口里面不含有数据库这条数据 增加到删除
                            dueImages.add(imageManage);
                        }
                    });
                    if (dueImages.size()!=0) {
                        this.removeByIds(dueImages.stream().map(ImageManage::getId).collect(Collectors.toList()));
                        //删除镜像与系统版本 测试工具 其他软件表
                        QueryWrapper<ImageOtherInfo> otherInfoQueryWrapper = new QueryWrapper<>();
                        otherInfoQueryWrapper.eq(CommonConstant.DATA_STRING_IDEL, CommonConstant.DATA_INT_IDEL_0);
                        otherInfoQueryWrapper.in(CommonConstant.OPENSTACK_ID, dueImages.stream().map(ImageManage::getOpenstackId).collect(Collectors.toList()));
                        List<ImageOtherInfo> dueOtherList = iImageOtherInfoService.list(otherInfoQueryWrapper);
                        iImageOtherInfoService.removeByIds(dueOtherList.stream().map(ImageOtherInfo::getId).collect(Collectors.toList()));
                    }
                }
            }
        }catch (Exception e){
            log.error("镜像同步Service异常,原因:"+e.getMessage());
            throw new UnsupportedOperationException("获取失败异常,请联系管理员！");
        }
    }


    @Override
    public String getDealCommaString(String otherSoftware) {
        List<String> otherSoftwareList = Arrays.asList(otherSoftware.split(","));
        List<String> otherSoftwares = otherSoftwareList.stream().filter(StringUtils::isNotBlank).collect(Collectors.toList());
        return String.join(",", otherSoftwares);
    }

}
