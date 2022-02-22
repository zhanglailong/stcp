package org.jeecg.modules.eval.util;

import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.mass.modle.MassTreeModel;
import org.jeecg.modules.mass.modle.TreeIdModel;
import org.jeecg.modules.mass.vo.CustomSystemTreeVO;

import java.util.ArrayList;
import java.util.List;

/**
 * @program: ht
 * @description: 测试
 * @author: Sunshine
 * @create: 2020-12-24 14:46
 */
public class EvalFindsTestChildrenUtil {

    /**
     * queryTreeList的子方法 ====1=====
     * 该方法是s将SysDepart类型的list集合转换成SysDepartTreeModel类型的集合
     */
    public static List<MassTreeModel> wrapTreeDataToTreeList(List<CustomSystemTreeVO> recordList, String templateId) {
        // 在该方法每请求一次,都要对全局list集合进行一次清理
        List<TreeIdModel> idList = new ArrayList<TreeIdModel>();
        List<MassTreeModel> records = new ArrayList<>();
        for (int i = 0; i < recordList.size(); i++) {
        	CustomSystemTreeVO node = recordList.get(i);
            records.add(new MassTreeModel(node));
        }
        List<MassTreeModel> tree = findChildren(records, idList);
        setEmptyChildrenAsNull(tree);
        return tree;
    }

    /**
     * queryTreeList的子方法 ====2=====
     * 该方法是找到并封装顶级父类的节点到TreeList集合
     */
    private static List<MassTreeModel> findChildren(List<MassTreeModel> recordList,
                                                    List<TreeIdModel> masterIdList) {

        List<MassTreeModel> treeList = new ArrayList<>();
        for (int i = 0; i < recordList.size(); i++) {
            MassTreeModel branch = recordList.get(i);
            if (oConvertUtils.isEmpty(branch.getPid())) {
                treeList.add(branch);
                TreeIdModel masterIdModel = new TreeIdModel().convert(branch);
                masterIdList.add(masterIdModel);
            }
        }
        getGrandChildren(treeList, recordList, masterIdList);

        //idList = departIdList;
        return treeList;
    }

    /**
     * queryTreeList的子方法====3====
     * 该方法是找到顶级父类下的所有子节点集合并封装到TreeList集合
     */
    private static void getGrandChildren(List<MassTreeModel> treeList, List<MassTreeModel> recordList, List<TreeIdModel> idList) {

        for (int i = 0; i < treeList.size(); i++) {
            MassTreeModel model = treeList.get(i);
            TreeIdModel idModel = idList.get(i);
            for (int i1 = 0; i1 < recordList.size(); i1++) {
                MassTreeModel m = recordList.get(i1);
                if (m.getPid() != null && m.getPid().equals(model.getId())) {
                    model.getChildren().add(m);
                    TreeIdModel dim = new TreeIdModel().convert(m);
                    idModel.getChildren().add(dim);
                }
            }
            getGrandChildren(treeList.get(i).getChildren(), recordList, idList.get(i).getChildren());
        }

    }


    /**
     * queryTreeList的子方法 ====4====
     * 该方法是将子节点为空的List集合设置为Null值
     */
    private static void setEmptyChildrenAsNull(List<MassTreeModel> treeList) {

        for (int i = 0; i < treeList.size(); i++) {
            MassTreeModel model = treeList.get(i);
            if (model.getChildren().size() == 0) {
                model.setChildren(null);
                model.setLeaf(true);
            } else {
                setEmptyChildrenAsNull(model.getChildren());
                model.setLeaf(false);
            }
        }
        // sysDepartTreeList = treeList;
    }

}