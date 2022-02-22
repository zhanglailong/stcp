package org.jeecg.modules.Imageotherinfo.service.impl;

import org.apache.commons.lang.StringUtils;
import org.jeecg.modules.Imageotherinfo.entity.ImageOtherInfo;
import org.jeecg.modules.Imageotherinfo.mapper.ImageOtherInfoMapper;
import org.jeecg.modules.Imageotherinfo.service.IImageOtherInfoService;
import org.jeecg.modules.common.CommonConstant;
import org.jeecg.modules.plan.entity.ImageManage;
import org.jeecg.modules.running.tools.entity.RunningToolsList;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.util.ArrayList;
import java.util.List;


/**
 * @author yeyl
 */
@Service
public class ImageOtherInfoServiceImpl extends ServiceImpl<ImageOtherInfoMapper, ImageOtherInfo> implements IImageOtherInfoService {

    @Override
    public void saveInfos(ImageManage imageManage, List<RunningToolsList> runningToolsLists) {
        try {
            List<ImageOtherInfo> imageOtherInfos = new ArrayList<>();
            String otherSoftware = imageManage.getOtherSoftware();
            String testTools = imageManage.getTestTools();
            if (StringUtils.isNotBlank(otherSoftware) && StringUtils.isNotBlank(testTools)) {
                String[] otherSoftWaresInput = otherSoftware.split(",");
                for (String s : otherSoftWaresInput) {
                    for (RunningToolsList t : runningToolsLists) {
                        ImageOtherInfo imageOtherInfo = getImageOtherInfo(imageManage);
                        imageOtherInfo.setTestToolId(t.getId());
                        imageOtherInfo.setTestToolName(t.getToolsName());
                        imageOtherInfo.setOtherSoftware(s);
                        imageOtherInfos.add(imageOtherInfo);
                    }
                }
            } else if (StringUtils.isBlank(otherSoftware) && StringUtils.isNotBlank(testTools)) {
                for (RunningToolsList t : runningToolsLists) {
                    ImageOtherInfo imageOtherInfo = getImageOtherInfo(imageManage);
                    imageOtherInfo.setTestToolId(t.getId());
                    imageOtherInfo.setTestToolName(t.getToolsName());
                    imageOtherInfos.add(imageOtherInfo);
                }

            } else if (StringUtils.isNotBlank(otherSoftware) && StringUtils.isBlank(testTools)) {
                String[] otherSoftWaresInput = otherSoftware.split(",");
                for (String s : otherSoftWaresInput) {
                    ImageOtherInfo imageOtherInfo = getImageOtherInfo(imageManage);
                    imageOtherInfo.setOtherSoftware(s);
                    imageOtherInfos.add(imageOtherInfo);
                }
            } else {
                ImageOtherInfo imageOtherInfo = getImageOtherInfo(imageManage);
                imageOtherInfos.add(imageOtherInfo);
            }
            this.saveBatch(imageOtherInfos);
        }catch (Exception e){
            log.error("批量新增表tepm_image_other_info异常,原因:"+e.getMessage());
        }
    }
    @Override
    public ImageOtherInfo getImageOtherInfo(ImageManage imageManage) {
        ImageOtherInfo imageOtherInfo = new ImageOtherInfo();
        imageOtherInfo.setIdel(CommonConstant.DATA_INT_IDEL_0);
        imageOtherInfo.setImageInnerName(imageManage.getName());
        imageOtherInfo.setImageName(imageManage.getImageName());
        imageOtherInfo.setOpenstackId(imageManage.getOpenstackId());
        imageOtherInfo.setSystemVersion(imageManage.getSystemVersion());
        return imageOtherInfo;
    }
}
