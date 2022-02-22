package org.jeecg.modules.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import org.apache.commons.lang.StringUtils;

/**
 * @author zlf
 */
@Getter
public enum ServerStatusEnum {

    /*
        ACTIVE	运行
        BUILD	创建
        RESIZE	调整大小或迁移
        SHUTOFF	关机
        VERIFY_RESIZE	确认调整大小
        PAUSED	暂停
        SUSPENDED	挂起
        ERROR	错误
        SUSPEND_IN_PROGRESS 挂起中
        RESUME_IN_PROGRESS 恢复挂起中

     */
    ACTIVE("ACTIVE", "运行"),
    BUILD("BUILD", "创建"),
    RESIZE("RESIZE", "调整大小或迁移"),
    SHUTOFF("SHUTOFF", "关机"),
    PAUSED("PAUSED", "暂停"),
    SUSPENDED("SUSPENDED", "挂起"),
    ERROR("ERROR", "错误"),
    SUSPEND_IN_PROGRESS("SUSPEND_IN_PROGRESS", "挂起中"),
    RESUME_IN_PROGRESS("RESUME_IN_PROGRESS", "恢复挂起中"),
    IN_PROGRESS("IN_PROGRESS","创建中"),
    RESTORE_IN_PROGRESS("RESTORE_IN_PROGRESS", "从快照恢复中"),
    BACKUP_RESTORE_IN_PROGRESS("BACKUP_RESTORE_IN_PROGRESS", "从备份恢复中"),
    BACKUP_RESTORE_COMPLETE("BACKUP_RESTORE_COMPLETE", "从备份恢复完成"),
    RESTORE_COMPLETE("RESTORE_COMPLETE", "从快照恢复完成"),
    RESTORE_FAILED("RESTORE_FAILED", "从快照恢复失败"),
    BACKUP_IN_PROGRESS("BACKUP_IN_PROGRESS", "创建备份中"),
    SNAPSHOT_IN_PROGRESS("SNAPSHOT_IN_PROGRESS", "创建快照中"),
    SNAPSHOT_COMPLETE("SNAPSHOT_COMPLETE", "创建快照完成"),
    BACKUP_COMPLETE("BACKUP_COMPLETE", "创建备份完成"),
    BACKUP_FAILED("BACKUP_FAILED", "创建备份失败"),
    SNAPSHOT_FAILED("SNAPSHOT_FAILED", "创建快照失败"),
    BACKUP_RESTORE_FAILED("BACKUP_RESTORE_FAILED", "从备份恢复失败"),
    MIGRATING("MIGRATING","迁移中"),
    MIGRATING_FAILED("MIGRATING_FAILED","迁移失败"),
    MIGRATING_COMPLETE("MIGRATING_COMPLETE","迁移完成"),
    ;

    ServerStatusEnum(String status, String desc) {
        this.status = status;
        this.desc = desc;
    }

    @EnumValue
    private final String status;
    @JsonValue
    private final String desc;

    public static ServerStatusEnum toEnum(String status) {
        if (StringUtils.isEmpty(status)) {
            return null;
        }
        for (ServerStatusEnum item : ServerStatusEnum.values()) {
            if (item.getStatus().equals(status)) {
                return item;
            }
        }
        return null;
    }

}