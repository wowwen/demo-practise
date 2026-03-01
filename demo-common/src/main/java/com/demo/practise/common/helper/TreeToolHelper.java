package com.demo.practise.common.helper;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author jiangyw
 * @date 2022/2/20 15:13
 * @description 通过代码实现树结构。另外如果数据库设计的包括父子节点ID，可以直接在SQL中递归查询
 */
public class TreeToolHelper {
    /**
     * 根节点，只有一个
     */
    private List<TreeNode> rootList;
    /**
     * 所有节点数据（包括根节点）
     */
    private List<TreeNode> bodyList;

    public TreeToolHelper(List<TreeNode> rootList, List<TreeNode> bodyList) {
        this.rootList = rootList;
        this.bodyList = bodyList;
    }

    public List<TreeNode> getTree() {   //调用的方法入口
        if (bodyList != null && !bodyList.isEmpty()) {
            //声明一个map，用来过滤已操作过的数据
            Map<Integer, Integer> map = Maps.newHashMapWithExpectedSize(bodyList.size());
            rootList.forEach(beanTree -> getChild(beanTree, map));
            return rootList;
        }
        return null;
    }

    public void getChild(TreeNode beanTree, Map<Integer, Integer> map) {
        List<TreeNode> childList = Lists.newArrayList();
        bodyList.stream()
                .filter(a -> !map.containsKey(a.getId()))
                .filter(b -> b.getParentId().equals(beanTree.getId()))
                .forEach(c -> {
                    map.put(c.getId(), c.getParentId());
                    getChild(c, map);
                    childList.add(c);
                });
        beanTree.setChildren(childList);
    }

    @Data
    static class TreeNode {
        private int id;
        private Integer parentId;
        private List<TreeNode> children;
    }
}
