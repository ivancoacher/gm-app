package com.jsnjwj.facade.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class GroupingDetailDto {

    private Long gameId;

    private List<GroupingItem> groupItemList = new ArrayList<>();

    @Data
    public static class GroupingItem {

        private Long itemId;

        private String itemName;

        private List<GroupingItemSign> groupItemSignList;

    }

    @Data
    public static class GroupingItemSign {

        private Long signId;

        private Integer sort;

        private String name;

        private String team;

        private String sex;

    }

}
