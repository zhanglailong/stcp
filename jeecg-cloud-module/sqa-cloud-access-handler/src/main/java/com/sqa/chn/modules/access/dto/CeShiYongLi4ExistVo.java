package com.sqa.chn.modules.access.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import com.sqa.chn.modules.access.entity.CeShiYongLi;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CeShiYongLi4ExistVo extends CeShiYongLi {
    private Boolean exist;
}
