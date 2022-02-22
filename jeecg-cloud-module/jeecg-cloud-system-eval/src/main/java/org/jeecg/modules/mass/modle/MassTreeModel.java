package org.jeecg.modules.mass.modle;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import lombok.Data;
import org.jeecg.modules.mass.vo.CustomSystemTreeVO;

;

/**
 * @program: ht
 * @description: testtree表  存储树结构数据的实体类
 * @author: Sunshine
 * @create: 2020-12-24 13:16
 */
@Data
public class MassTreeModel implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 对应testtree表中的id字段,前端数据树中的key
     */
    private String key;

    /**
     * 对应testtree表中的id字段,前端数据树中的value
     */
    private String value;

    /**
     * 对应depart_name字段,前端数据树中的title
     */
    private String title;


    private boolean isLeaf;

    /**
     * 以下字段与testtree表中字段一致
     */

    private String id;

    private String templateId;
    
    private String templateName;
    
    private String masterName;

    private String customWeight;

    private String elementLevel;

    private String pid;
    
    private String resultId;
    
    private String score;
    
    private String needFlag;
    
    private String checked;

    private List<MassTreeModel> children = new ArrayList<>();

    /**
     * 将TestTree对象转换成MassTreeModel对象
     *
     * @param vo
     */
    public MassTreeModel(CustomSystemTreeVO vo) {
        this.key = String.valueOf(vo.getId());
        this.value = String.valueOf(vo.getId());
        this.title = vo.getMasterName();
        this.id = String.valueOf(vo.getId());
        this.templateId = vo.getTemplateId();
        this.templateName = vo.getTemplateName();
        this.masterName = vo.getMasterName();
        this.customWeight = vo.getCustomWeight();
        this.elementLevel = vo.getElementLevel();
        this.pid = String.valueOf(vo.getPid());
        this.resultId = String.valueOf(vo.getResultId());
        this.score = String.valueOf(vo.getScore());
        this.needFlag = vo.getNeedFlag();
        this.checked = vo.getChecked();
    }

	/**
     * 重写equalss方法
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MassTreeModel that = (MassTreeModel) o;
        return isLeaf == that.isLeaf &&
                Objects.equals(key, that.key) &&
                Objects.equals(value, that.value) &&
                Objects.equals(title, that.title) &&
                Objects.equals(id, that.id) &&
                Objects.equals(pid, that.pid) &&
                Objects.equals(children, that.children);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, value, title, isLeaf, id, pid, children);
    }
}