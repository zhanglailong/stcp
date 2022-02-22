package org.jeecg.modules.mass.modle;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * Tree表 封装树结构的部门的名称的实体类
 * <p>
 *
 * @Author Steve
 * @Since 2019-01-22
 */
@Data
public class TreeIdModel implements Serializable {

    private static final long serialVersionUID = 1L;
    List<TreeIdModel> children = new ArrayList<>();
    /**
     * 主键ID
     */
    private String key;
    /**
     * 主键ID
     */
    private String value;
    /**
     * 部门名称
     */
    private String title;

    public static long getSerialVersionUid() {
        return serialVersionUID;
    }

    /**
     * 将TreeModel的部分数据放在该对象当中
     *
     * @param treeModel
     * @return
     */
    public TreeIdModel convert(MassTreeModel treeModel) {
        this.key = treeModel.getId();
        this.value = treeModel.getId();
        this.title = treeModel.getTemplateId() + "-" + treeModel.getMasterName();
        return this;
    }

    /**
     * 该方法为用户部门的实现类所使用
     *
     * @param massTreeModel
     * @return
     */
    public TreeIdModel convertByUserDepart(MassTreeModel massTreeModel) {
        this.key = massTreeModel.getId();
        this.value = massTreeModel.getId();
        this.title = massTreeModel.getTemplateId() + "-" + massTreeModel.getMasterName();
        return this;
    }
}
