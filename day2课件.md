# 上日回顾

- 完成的项目代码的导入

- 修改了 demo 表结构,完成一个功能

- 自己生成一张表,叫 appDemo,并且完成 crud

  ## 教学目的

  让开发者熟悉项目的导入流程,以及整个项目的从数据库,到代码,到界面的整个过程

  让大家知道,工作需要自己去想办法解决问题,工作需要自我内驱,只要想,一切那么简单,因为现在是按照一个月薪8000的要求带领写的业务需求

# 第二天课件

## 今日目标

完成企业项目功能,企业客户管理,并且完善,时间选择,动态下拉,高级查询,数据校验等

## 修改部分项目配置

### 打开日志文件修改成 

```yaml
logging:
  level:
    cn:
      wolfcode: debug
```

![image-20220619110918942](day2课件.assets/image-20220619110918942.png)

### 打开配置修改包扫描

```yaml
mybatis-plus:
  mapper-locations: classpath*:mappers/**/*.xml
  #实体扫描，多个package用逗号或者分号分隔
  typeAliasesPackage: com.nebula.*.modules.*.entity
```

![image-20220619155006278](day2课件.assets/image-20220619155006278.png)

## 任务目标:实现需求文档,企业用户管理功能,并且完善功能,确保可用

### 任务实现要求文档地址

【金山文档】 项目需求文档
https://kdocs.cn/l/cfptB2ea8iOG

#### 1.完成企业用户管理的业务实现

要求:

- 把企业客户信息表复制到数据库

- 修改好上下文信息

  ``` java
          GeneratorUtils.generator(
                  "web",
                  "cn.wolfcode.web.modules",
                  "custinfo",
                  DbType.MYSQL,
                  "/Users/chenshiyun/Desktop/code",//自己的磁盘路径
                  // 页面上的父上下文
                  // 自己的上下文
                  "1111",//数据库里面的演示模块
                  "企业客户管理",//不用
                  null,
                  "cust",
                  "custinfo",
                  "陈天狼",
                  "127.0.0.1",
                  "3306",
  //       mysql8.0用这个         "nojoke?useSSL=false&serverTimezone=GMT%2B8&characterEncoding=utf8",//你的配置文件里面的数据库名称是什么,你就改成什么
                  "nojoke",//你的配置文件里面的数据库名称是什么,你就改成什么
                  "root",//数据库账号
                  "12345678",//数据库密码
                  new String[]{"bmd_", "mp_", "SYS_"},
                  new String[]{"tb_customer"},false
          );
  ```

- 启动代码生成器

#### 2.完成企业用户管理的代码编写

- 复制代码到工程

- 复制 sql 插入数据库

- 修改 list.html 头部信息文件

  ```
  <#assign sec=JspTaglibs["http://http://www.ahsj.link/security/tags"]/>
  ```

- 修改 comtroller 的错误文件

- 启动项目,完成 crud 操作

#### 3.完成时间插件使用

- 客户管理有关于时间相关的,故而需要使用时间选择器

- 参考网站:http://layui-doc.pearadmin.com/doc/modules/laydate.html

- 在对应的新增编辑js模块,使用laydate组件

  ![image-20220619023009988](day2课件.assets/image-20220619023009988.png)

- 然后在新增编辑 html 模块,使用laydate对应的 id 引用

- 配置好了后,进行重启工程,测试时间插件效果

- 后端需要修改一下JacksonConfig.class配置

  ```java
  @Override
  public TemplateModel wrap(Object object) throws TemplateModelException {
      if (object instanceof LocalDate) {
          return new SimpleScalar(DateTimeFormatter.ofPattern(TimeFormat.DEFAULT_DATE_FORMAT).format(((LocalDate) object)));
      }
      if (object instanceof LocalTime) {
          return new SimpleScalar(DateTimeFormatter.ofPattern(TimeFormat.DEFAULT_TIME_FORMAT).format(((LocalTime) object)));
      }
      if (object instanceof LocalDateTime) {
          return new SimpleScalar(DateTimeFormatter.ofPattern(TimeFormat.DEFAULT_DATE_TIME_FORMAT).format(((LocalDateTime) object)));
      }
      return super.wrap(object);
  }
  ```

#### 4.完成经营情况下拉框

- 读取下拉框内容

  ```html
  <div class="layui-col-lg6">
      <label class="layui-form-label">经营状态</label>
      <div class="layui-input-block">
          <select name="openStatus">
              <option value="0">开业</option>
              <option value="1">注销</option>
              <option value="2">破产</option>
          </select>
      </div>
  </div>
  ```

- 进行下拉框数据格式使用使用

- 编辑页面下拉框回显

  ```html
  <div class="layui-col-lg6">
      <label class="layui-form-label">经营状态</label>
      <div class="layui-input-block">
          <select name="openStatus">
              <option <#if obj.openStatus=0 >selected</#if> value="0">开业</option>
              <option <#if obj.openStatus=1 >selected</#if> value="1">注销</option>
              <option <#if obj.openStatus=2 >selected</#if> value="2">破产</option>
          </select>
      </div>
  </div>
  ```

Freemaker if 语法         <#if obj.openStatus=2 >selected</#if>

#### 5.完成区域下拉框

- 查询所有的区域,并且使用下拉框动态读取

  ```html
  <div class="layui-col-lg6">
    <label class="layui-form-label">所属省份</label>
    <div class="layui-input-block">
      <select name="province">
        <option value="">--请选择--</option>
        <#list citys  as cyty>
          <option value="cyty.key">${cyty.value}</option>
          </#list>
      </select>
    </div>
  </div>
  ```

- 在新增功能完成下拉框的填写操作

- 在编辑功能完成下拉框的回显操作

  ```html
  <div class="layui-col-lg6">
      <label class="layui-form-label">所属省份</label>
      <div class="layui-input-block">
          <select name="province">
              <option value="">--请选择--</option>
              <#list citys as cyty>
                  <#if cyty.key=obj.province>
                      <option selected value="${cyty.key}">${cyty.value}</option>
                  <#else>
                      <option value="${cyty.key}">${cyty.value}</option>
                  </#if>
              </#list>
          </select>
      </div>
  </div>
  ```

#### 6.完成模糊查询

- 完成客户名称,企业名称的模糊查询
- 完成省份下拉框查询
- 完成经营情况下拉查询

#### 7.完善业务细节

- 完善数据校验
- 完善id 处理
- 完善录入人
- 完善录入时间
- 修改时间

