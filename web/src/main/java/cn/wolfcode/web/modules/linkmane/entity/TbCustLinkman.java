package cn.wolfcode.web.modules.linkmane.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import link.ahsj.core.annotations.AddGroup;
import link.ahsj.core.annotations.UpdateGroup;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;
import java.time.LocalDateTime;
import java.io.Serializable;

/**
 * <p>
 * 客户联系人
 * </p>
 *
 * @author 写代码没有出息的
 * @since 2022-06-21
 */
@Data
public class TbCustLinkman implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    /**
     * 所属企业
     */
    @NotBlank(message = "所属企业",groups ={AddGroup.class, UpdateGroup.class})
    @Excel(name = "所属企业")
    private String custId;

    /**
     * 联系人名字
     */
    @Length(max = 30,message = "联系人名字不能超过500个字符",groups ={AddGroup.class,UpdateGroup.class})
    @NotBlank(message = "请填写联系人名字",groups ={AddGroup.class, UpdateGroup.class})
    @Excel(name = "联系人名字")
    private String linkman;

    /**
     * 性别 1 男 0 女
     */
    @NotNull(message = "请填写性别",groups ={AddGroup.class, UpdateGroup.class})
    @Excel(name = "性别")
    private Integer sex;

    /**
     * 年龄
     */
    @Max(message = "年龄不能超过100岁",groups ={AddGroup.class, UpdateGroup.class},value = 100)
    @NotNull(message = "请填写年龄",groups ={AddGroup.class, UpdateGroup.class})
    @Excel(name = "年龄")

    private Integer age;

    /**
     * 联系人电话
     */
    @NotBlank(message = "请填写联系人名字",groups ={AddGroup.class, UpdateGroup.class})
    @Excel(name = "联系人电话")
    private String phone;

    /**
     * 职位
     */
    @Length(max = 20,message = "职位不能超过20个字符",groups ={AddGroup.class,UpdateGroup.class})
    @NotBlank(message = "职位联系人名字",groups ={AddGroup.class, UpdateGroup.class})
    @Excel(name = "职位")
    private String position;

    /**
     * 部门
     */
    @Length(max = 20,message = "部门不能超过20个字符",groups ={AddGroup.class,UpdateGroup.class})
    @NotBlank(message = "请填写部门",groups ={AddGroup.class, UpdateGroup.class})
    @Excel(name = "部门")
    private String department;

    /**
     * 备注信息
     */
    private String remark;

    /**
     * 录入人
     */
    @Excel(name = "录入人")
    private String inputUser;

    /**
     * 录入时间
     */
    private LocalDateTime inputTime;

    //任职状态
    @Excel(name = "任职状态")
    @TableField("open_status")
    private Integer openStatus;

    //企业名称
    @TableField(exist = false)
    private String customerName;



}
