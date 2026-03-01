package com.demo.practise.common.helper;

import cn.hutool.core.collection.CollUtil;
import com.github.pagehelper.PageInfo;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.*;

/**
 * @author owen
 * @date 2024/11/14 20:07
 * @description 内存分页方法
 */
public class PageCacheHelper {
    /**
     * 分页方法
     * @param data
     * @param paging
     * @param <T>
     * @return
     */
    public static <T> PageInfo<T> pageCache(List<T> data, Paging paging) {
        //要处理为空的时候的情况
        if (CollUtil.isEmpty(data)) {
            PageInfo<T> objectPageInfo = new PageInfo<>();
            objectPageInfo.setPageSize(paging.getPageSize());
            objectPageInfo.setTotal(0);
            objectPageInfo.setPages(1);
            objectPageInfo.setPageNum(paging.getPageNum());
            objectPageInfo.setList(Collections.emptyList());
            return objectPageInfo;
        }
        //处理请求范围外的分页情况
        if(paging.getPageSize() * (paging.getPageNum()-1) >= data.size()){
            PageInfo<T> objectPageInfo = new PageInfo<>();
            objectPageInfo.setPageSize(paging.getPageSize());
            objectPageInfo.setTotal(0);
            objectPageInfo.setPages(1);
            objectPageInfo.setPageNum(paging.getPageNum());
            objectPageInfo.setList(Collections.emptyList());
            return objectPageInfo;
        }
        //处理请求limie=0时（全部数据）
        if(0 == paging.getPageSize()){
            PageInfo<T> objectPageInfo = new PageInfo<>();
            objectPageInfo.setPageSize(data.size());
            objectPageInfo.setTotal(data.size());
            objectPageInfo.setPages(1);
            objectPageInfo.setPageNum(1);
            objectPageInfo.setList(data);
            return objectPageInfo;
        }
        PageInfo<T> pageInfo = new PageInfo<>(data);
        List<T> temps = new ArrayList<>();
        int pageNum = paging.getPageNum();
        Map<Integer, List<T>> map = new HashMap<>();
        int index = 1;
        for (int i = 0; i < data.size(); i++) {
            temps.add(data.get(i));
            if ((i + 1) % paging.getPageSize() == 0) {
                map.put(index++, temps);
                temps = new ArrayList<>();
            }
            if (i == (data.size() - 1)) {
                map.put(index, temps);
            }
        }
        if (map.get(pageNum).size() == 0) {
            pageInfo.setPageSize(paging.getPageSize());
            pageInfo.setTotal(data.size());
            pageInfo.setPages(--index);
            pageInfo.setPageNum(--pageNum);
            pageInfo.setList(map.get(pageNum));
        } else {
            pageInfo.setPageSize(paging.getPageSize());
            pageInfo.setTotal(data.size());
            pageInfo.setPages(index);
            pageInfo.setPageNum(pageNum);
            pageInfo.setList(map.get(pageNum));
        }
        return pageInfo;
    }
}

@Data
@Builder
class Paging implements Serializable {
    private int pageNum;
    private int pageSize;
    private String condition;
    private String sort;
}
