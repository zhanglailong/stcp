package com.sqa.chn.modules.access.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import com.sqa.chn.modules.access.entity.CeShiXiangBiao;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
public class CeShiXiang4CeShiYongLiVo extends CeShiXiangBiao {
    private List<CeShiYongLi4ExistVo> ceShiYongLiList;

    private String fullName;
}
