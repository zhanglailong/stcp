package org.jeecg.modules.oss.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import java.io.Serializable;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity(name = "running_upload_form")
public class RunningUploadForm {

    private String id;

    private String url;

    private Integer type;

    private String name;

    private Long size;

    private Date createTime;

    private String createBy;

}
