package org.jeecg.modules.access.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@SuperBuilder(toBuilder = true)
public class SyncResultVo {
    private List<String> syncCaseIds;

    private List<String> syncTaskIds;

    private List<String> syncCaseStepIds;

    private Integer syncCaseCount;

    private Integer syncTaskCount;

    private Integer syncCaseStepCount;

    private Integer skipCaseCount;

    private Integer skipTaskCount;

    private Integer skipCaseStepCount;
}
