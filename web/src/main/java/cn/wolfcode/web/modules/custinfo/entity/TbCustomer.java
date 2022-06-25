package cn.wolfcode.web.modules.custinfo.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import link.ahsj.core.annotations.AddGroup;
import link.ahsj.core.annotations.UpdateGroup;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.io.Serializable;

/**
 * <p>
 * 客户信息
 * </p>
 *
 * @author 写代码没有出息的
 * @since 2022-06-20
 */
public class TbCustomer implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    /**
     * 企业名称
     */
    @NotBlank(message = "请填写企业名称",groups ={AddGroup.class,UpdateGroup.class})
    @Length(max = 100,message = "企业名称不能超过30个字符",groups ={AddGroup.class,UpdateGroup.class})
    private String customerName;

    /**
     * 法定代表人
     */
    @NotBlank(message = "请填写法定代表人",groups ={AddGroup.class,UpdateGroup.class})
    @Length(max = 30,message = "法定代表人不能超过30个字符",groups ={AddGroup.class,UpdateGroup.class})

    private String legalLeader;

    /**
     * 成立时间
     */
    @NotNull(message = "请填写成立时间",groups = {AddGroup.class,UpdateGroup.class})
    private LocalDate registerDate;

    /**
     * 经营状态, 0 开业、1 注销、2 破产
     */
    @NotNull(message = "没输入经营状态",groups ={AddGroup.class,UpdateGroup.class})
    private Integer openStatus;

    /**
     * 所属地区省份
     */
    @NotBlank(message = "请填写所属地区省份",groups ={AddGroup.class,UpdateGroup.class})
    private String province;

    /**
     * 注册资本,(万元)
     */
    @NotBlank(message = "请填写注册资本",groups ={AddGroup.class,UpdateGroup.class})
    @Length(max = 20,message = "注册资本不能超过20个字符",groups ={AddGroup.class,UpdateGroup.class})

    private String regCapital;

    /**
     * 所属行业
     */
    @NotBlank(message = "请填写所属行业",groups ={AddGroup.class,UpdateGroup.class})
    @Length(max = 30,message = "所属行业不能超过30个字符",groups ={AddGroup.class,UpdateGroup.class})

    private String industry;

    /**
     * 经营范围
     */

    @NotBlank(message = "请填写经营范围",groups ={AddGroup.class,UpdateGroup.class})
    @Length(max = 500,message = "经营范围不能超过30个字符",groups ={AddGroup.class,UpdateGroup.class})
    private String scope;

    @TableField(exist = false)
    private String provinceName;


    /**
     * 注册地址
     */
    @Length(max = 500,message = "注册地址不能超过500个字符",groups ={AddGroup.class,UpdateGroup.class})
    @NotBlank(message = "请填写经营范围",groups ={AddGroup.class,UpdateGroup.class})
    private String regAddr;

    /**
     * 录入时间
     */
    private LocalDateTime inputTime;

    /**
     * 修改时间
     */
    private LocalDateTime updateTime;

    /**
     * 录入人
     */
    private String inputUserId;


    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
    public String getLegalLeader() {
        return legalLeader;
    }

    public void setLegalLeader(String legalLeader) {
        this.legalLeader = legalLeader;
    }
    public LocalDate getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(LocalDate registerDate) {
        this.registerDate = registerDate;
    }
    public Integer getOpenStatus() {
        return openStatus;
    }

    public void setOpenStatus(Integer openStatus) {
        this.openStatus = openStatus;
    }
    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }
    public String getRegCapital() {
        return regCapital;
    }

    public void setRegCapital(String regCapital) {
        this.regCapital = regCapital;
    }
    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }
    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }
    public String getRegAddr() {
        return regAddr;
    }

    public void setRegAddr(String regAddr) {
        this.regAddr = regAddr;
    }
    public LocalDateTime getInputTime() {
        return inputTime;
    }

    public void setInputTime(LocalDateTime inputTime) {
        this.inputTime = inputTime;
    }
    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }
    public String getInputUserId() {
        return inputUserId;
    }

    public void setInputUserId(String inputUserId) {
        this.inputUserId = inputUserId;
    }

    @Override
    public String toString() {
        return "TbCustomer{" +
            "id=" + id +
            ", customerName=" + customerName +
            ", legalLeader=" + legalLeader +
            ", registerDate=" + registerDate +
            ", openStatus=" + openStatus +
            ", province=" + province +
            ", regCapital=" + regCapital +
            ", industry=" + industry +
            ", scope=" + scope +
            ", regAddr=" + regAddr +
            ", inputTime=" + inputTime +
            ", updateTime=" + updateTime +
            ", inputUserId=" + inputUserId +
        "}";
    }
}
