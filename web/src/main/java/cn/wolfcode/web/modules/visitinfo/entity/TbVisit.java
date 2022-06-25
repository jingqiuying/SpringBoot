package cn.wolfcode.web.modules.visitinfo.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <p>
 * 拜访信息表
 * </p>
 *
 * @author 写代码没有出息的
 * @since 2022-06-22
 */
@Data
public class TbVisit implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 唯一id
     */
    private String id;

    /**
     * 客户id
     */
    private String custId;

    @TableField(exist = false)
    private String customerName;

    /**
     * 联系人id
     */
    private String linkmanId;

    @TableField(exist = false)
    private String linkman;

    /**
     * 拜访方式, 1 上门走访, 2 电话拜访
     */
//    private Boolean visitType;
    private Integer visitType;
    /**
     * 拜访原因
     */
    private String visitReason;

    /**
     * 交流内容
     */
    private String content;

    /**
     * 拜访时间
     */
    private LocalDate visitDate;

    /**
     * 录入人
     */
    private String inputUser;

    /**
     * 录入时间
     */
    private LocalDateTime inputTime;


}
