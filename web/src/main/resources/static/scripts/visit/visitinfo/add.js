layui.use(['form', 'layer'], function () {
    var form = layui.form,
        layer = layui.layer,
        $ = layui.jquery;
//监听企业客户选址事件
    form.on('select(customerSelect)', function(data){
        // data.value //得到被选中的值
        //请求接口拿到数据
        $.ajax({
            url: web.rootPath() + "linkmane/listByCustomerId?custId="+data.value,
            type: "post",
            contentType: "application/json",
            data: JSON.stringify(data.field),
            dataType: 'json',
            traditional: true,
            success: function (data) {
               //清空原来的内容
                $("#linkman").empty();
                //读取返回的数据进行渲染
                var optionHtml='<option value="">--请选择--</option>'
                if (data.data.length > 0){
                    data.data.forEach(item =>{
                        optionHtml += `<option value="${item.id}">${item.linkman}</option>`
                    })
                }
                //设置选择信息
                $("#linkman").html(optionHtml)
                //渲染表单
                form.render("select",'component-form-element')
            },
            error: function (e) {
                layer.msg(e.responseJSON.message, {icon: 2});
            }

        });

    });

    form.on('submit(Add-filter)', function (data) {
        $.ajax({
            url: web.rootPath() + "visitinfo/save",
            type: "post",
            contentType: "application/json",
            data: JSON.stringify(data.field),
            dataType: 'json',
            traditional: true,
            success: function (data) {
                layer.msg("操作成功", {
                    icon: 1,
                    success: function () {
                        reloadTb("Save-frame", "#SearchBtn");
                    }
                });
            },
            error: function (e) {
                layer.msg(e.responseJSON.message, {icon: 2});
            }

        });
        return false;
    });



});
