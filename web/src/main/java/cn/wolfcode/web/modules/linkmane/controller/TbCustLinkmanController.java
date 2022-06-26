package cn.wolfcode.web.modules.linkmane.controller;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.hutool.core.collection.CollectionUtil;
import cn.wolfcode.web.commons.entity.ExcelExportEntityWrapper;
import cn.wolfcode.web.commons.entity.LayuiPage;
import cn.wolfcode.web.commons.utils.CityUtils;
import cn.wolfcode.web.commons.utils.LayuiTools;
import cn.wolfcode.web.commons.utils.PoiExportHelper;
import cn.wolfcode.web.commons.utils.SystemCheckUtils;
import cn.wolfcode.web.modules.BaseController;
import cn.wolfcode.web.modules.custinfo.entity.TbCustomer;
import cn.wolfcode.web.modules.custinfo.service.ITbCustomerService;
import cn.wolfcode.web.modules.log.LogModules;
import cn.wolfcode.web.modules.sys.entity.SysDict;
import cn.wolfcode.web.modules.sys.entity.SysUser;
import cn.wolfcode.web.modules.sys.form.LoginForm;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import cn.wolfcode.web.modules.linkmane.entity.TbCustLinkman;
import cn.wolfcode.web.modules.linkmane.service.ITbCustLinkmanService;

import link.ahsj.core.annotations.AddGroup;
import link.ahsj.core.annotations.SameUrlData;
import link.ahsj.core.annotations.SysLog;
import link.ahsj.core.annotations.UpdateGroup;
import link.ahsj.core.entitys.ApiModel;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Objects;

/**
 * @author 写代码没有出息的
 * @since 2022-06-21
 */
@Log4j2
@Controller
@RequestMapping("linkmane")
public class TbCustLinkmanController extends BaseController {

    @Autowired
    private ITbCustLinkmanService entityService;
    @Autowired
    private ITbCustomerService customerService;

    private static final String LogModule = "TbCustLinkman";

    @GetMapping("/list.html")
    public ModelAndView list(ModelAndView mv) {
        //查出客户
        List<TbCustomer> list = customerService.list();
        //返回页面
        mv.addObject("custs",list);
        mv.setViewName("user/linkmane/list");
        return mv;
//        return "user/linkmane/list";

    }

    @RequestMapping("/add.html")
    @PreAuthorize("hasAuthority('user:linkmane:add')")
    public ModelAndView toAdd(ModelAndView mv) {
        //查出客户
        List<TbCustomer> list = customerService.list();
        //返回页面
        mv.addObject("custs",list);
        mv.setViewName("user/linkmane/add");
        return mv;
    }

    @GetMapping("/{id}.html")
    @PreAuthorize("hasAuthority('user:linkmane:update')")
    public ModelAndView toUpdate(@PathVariable("id") String id, ModelAndView mv) {
        //查出客户
        List<TbCustomer> list = customerService.list();
        //返回页面
        mv.addObject("custs",list);
        mv.setViewName("user/linkmane/update");
        mv.addObject("obj", entityService.getById(id));
        mv.addObject("id", id);
        return mv;
    }

    @RequestMapping("list")
    @PreAuthorize("hasAuthority('user:linkmane:list')")
    public ResponseEntity page(LayuiPage layuiPage,String parameterName,String custId) {
        SystemCheckUtils.getInstance().checkMaxPage(layuiPage);
        IPage<TbCustLinkman> page = new Page<>(layuiPage.getPage(), layuiPage.getLimit());
        IPage<TbCustLinkman> page1 = entityService.lambdaQuery()
                .eq(StringUtils.isNotBlank(custId),TbCustLinkman::getCustId,custId)
                .like(StringUtils.isNotBlank(parameterName),TbCustLinkman::getLinkman,parameterName)
                .or()
                .like(StringUtils.isNotBlank(parameterName),TbCustLinkman::getPhone,parameterName)
                .page(page);
        //判断我的集合不空
        if(CollectionUtil.isNotEmpty(page1.getRecords())){
            page1.getRecords().forEach(obj->{
                //拿到企业客户 id
                String custId1 = obj.getCustId();
                //查询出企业客户名称
                TbCustomer customer = customerService.getById(custId1);
                //判断不等于空的时候
                if (Objects.nonNull(customer)){
                    //设置企业客户名称到我们的联系人里面去
                    obj.setCustomerName(customer.getCustomerName());
                }
            });
        }

        return ResponseEntity.ok(LayuiTools.toLayuiTableModel(page1));
    }

    @SameUrlData
    @PostMapping("save")
    @SysLog(value = LogModules.SAVE, module =LogModule)
    @PreAuthorize("hasAuthority('user:linkmane:add')")
    public ResponseEntity<ApiModel> save(@Validated({AddGroup.class}) @RequestBody TbCustLinkman entity,HttpServletRequest request) {
        SysUser loginUser = (SysUser) request.getSession().getAttribute(LoginForm.LOGIN_USER_KEY);
        entity.setInputUser(loginUser.getUserId());
        entity.setInputTime(LocalDateTime.now(ZoneOffset.of("+16")));
        entityService.save(entity);
        return ResponseEntity.ok(ApiModel.ok());
    }

    @SameUrlData
    @SysLog(value = LogModules.UPDATE, module = LogModule)
    @PutMapping("update")
    @PreAuthorize("hasAuthority('user:linkmane:update')")
    public ResponseEntity<ApiModel> update(@Validated({UpdateGroup.class}) @RequestBody TbCustLinkman entity, HttpServletRequest request) {
        SysUser loginUser = (SysUser) request.getSession().getAttribute(LoginForm.LOGIN_USER_KEY);
        entity.setInputUser(loginUser.getUserId());
        entity.setInputTime(LocalDateTime.now(ZoneOffset.of("+16")));
        entityService.updateById(entity);
        return ResponseEntity.ok(ApiModel.ok());
    }

    @SysLog(value = LogModules.DELETE, module = LogModule)
    @DeleteMapping("delete/{id}")
    @PreAuthorize("hasAuthority('user:linkmane:delete')")
    public ResponseEntity<ApiModel> delete(@PathVariable("id") String id) {
        entityService.removeById(id);
        return ResponseEntity.ok(ApiModel.ok());
    }

    @SysLog(value = LogModules.EXPORT, module = LogModule)
    @PostMapping("export")
    public void export(HttpServletResponse response, String parameterName, String custId) throws IOException {
        // 根据条件查询到数据
        List<TbCustLinkman> list = entityService
                .lambdaQuery()
                .eq(StringUtils.isNotBlank(custId), TbCustLinkman::getCustId, custId)
                .and(StringUtils.isNotBlank(parameterName), q ->
                        q.like(TbCustLinkman::getLinkman, parameterName)
                                .or()
                                .like(TbCustLinkman::getPhone, parameterName)
                )
                .list();
        log.debug("擅自的哦积分"+custId);

        // 执行文件导出
        ExportParams exportParams = new ExportParams();
        Workbook workbook = ExcelExportUtil.exportExcel(exportParams, TbCustLinkman.class, list);
        PoiExportHelper.exportExcel(response, "客户联系人列表", workbook);
    }

    @RequestMapping("listByCustomerId")
    public ResponseEntity<ApiModel> listByCustomerId(String custId){
        //没有传递客户的情况，直接返回空的数据
        if (StringUtils.isBlank(custId)){
            return ResponseEntity.ok(ApiModel.ok());
        }
        List<TbCustLinkman> list = entityService.lambdaQuery().eq(TbCustLinkman::getCustId,custId).list();
        return ResponseEntity.ok(ApiModel.data(list));
    }

}
