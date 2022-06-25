package cn.wolfcode.web.modules.visitinfo.controller;

import cn.hutool.core.collection.CollectionUtil;
import cn.wolfcode.web.commons.entity.LayuiPage;
import cn.wolfcode.web.commons.utils.LayuiTools;
import cn.wolfcode.web.commons.utils.SystemCheckUtils;
import cn.wolfcode.web.modules.BaseController;
import cn.wolfcode.web.modules.custinfo.entity.TbCustomer;
import cn.wolfcode.web.modules.custinfo.service.ITbCustomerService;
import cn.wolfcode.web.modules.linkmane.entity.TbCustLinkman;
import cn.wolfcode.web.modules.linkmane.service.ITbCustLinkmanService;
import cn.wolfcode.web.modules.log.LogModules;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import cn.wolfcode.web.modules.visitinfo.entity.TbVisit;
import cn.wolfcode.web.modules.visitinfo.service.ITbVisitService;

import link.ahsj.core.annotations.AddGroup;
import link.ahsj.core.annotations.SameUrlData;
import link.ahsj.core.annotations.SysLog;
import link.ahsj.core.annotations.UpdateGroup;
import link.ahsj.core.entitys.ApiModel;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;

/**
 * @author 写代码没有出息的
 * @since 2022-06-22
 */
@Controller
@RequestMapping("visitinfo")
public class TbVisitController extends BaseController {

    @Autowired
    private ITbVisitService entityService;
    @Autowired
    private ITbCustomerService customerService;
    @Autowired
    private ITbCustLinkmanService custLinkmanService;



    private static final String LogModule = "TbVisit";

    @GetMapping("/list.html")
    public String list() {
        return "visit/visitinfo/list";
    }

    @RequestMapping("/add.html")
    @PreAuthorize("hasAuthority('visit:visitinfo:add')")
    public ModelAndView toAdd(ModelAndView mv, HttpServletRequest request) {
        //查出客户
        List<TbCustomer> list = customerService.list();
        //返回页面
        mv.addObject("custs",list);
        mv.setViewName("visit/visitinfo/add");
        return mv;
    }

    @GetMapping("/{id}.html")
    @PreAuthorize("hasAuthority('visit:visitinfo:update')")
    public ModelAndView toUpdate(@PathVariable("id") String id, ModelAndView mv) {
        //查出客户
        List<TbCustomer> list = customerService.list();
        //返回页面
        mv.addObject("custs",list);
        mv.setViewName("visit/visitinfo/update");
        mv.addObject("obj", entityService.getById(id));
        mv.addObject("id", id);
        return mv;
    }

    @RequestMapping("list")
    @PreAuthorize("hasAuthority('visit:visitinfo:list')")
    public ResponseEntity page(LayuiPage layuiPage,String parameterName) {
        SystemCheckUtils.getInstance().checkMaxPage(layuiPage);
        IPage<TbVisit> page = new Page<>(layuiPage.getPage(), layuiPage.getLimit());
        IPage<TbVisit> page1 = entityService.lambdaQuery()
                .like(StringUtils.isNotBlank(parameterName), TbVisit::getContent, parameterName)
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
            page1.getRecords().forEach(obj->{
                //拿到联系人id
                String linkmanId = obj.getLinkmanId();
                //查询出企业客户名称
                TbCustLinkman custLinkman = custLinkmanService.getById(linkmanId);
                if (Objects.nonNull(custLinkman)){
                    //设置企业客户名称到我们的联系人里面去
                    obj.setLinkman(custLinkman.getLinkman());
                }
            });
        }
        return ResponseEntity.ok(LayuiTools.toLayuiTableModel(page1));
//        return ResponseEntity.ok(LayuiTools.toLayuiTableModel(entityService.page(page)));
    }

    @SameUrlData
    @PostMapping("save")
    @SysLog(value = LogModules.SAVE, module =LogModule)
    @PreAuthorize("hasAuthority('visit:visitinfo:add')")
    public ResponseEntity<ApiModel> save(@Validated({AddGroup.class}) @RequestBody TbVisit entity) {
        entityService.save(entity);
        return ResponseEntity.ok(ApiModel.ok());
    }

    @SameUrlData
    @SysLog(value = LogModules.UPDATE, module = LogModule)
    @PutMapping("update")
    @PreAuthorize("hasAuthority('visit:visitinfo:update')")
    public ResponseEntity<ApiModel> update(@Validated({UpdateGroup.class}) @RequestBody TbVisit entity) {
        entityService.updateById(entity);
        return ResponseEntity.ok(ApiModel.ok());
    }

    @SysLog(value = LogModules.DELETE, module = LogModule)
    @DeleteMapping("delete/{id}")
    @PreAuthorize("hasAuthority('visit:visitinfo:delete')")
    public ResponseEntity<ApiModel> delete(@PathVariable("id") String id) {
        entityService.removeById(id);
        return ResponseEntity.ok(ApiModel.ok());
    }

}
