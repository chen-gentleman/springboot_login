function deleteConfirm(obj) {
    var o = $(obj);
    layer.confirm(o.attr('data-confirm'), {icon: 3, title:'提示'}, function(index){
        $.ajax({
            url: o.attr('href'),
            type: "post",
            traditional: true,
            success: function(data) {
                if(data.state=="ok"){
                    LayerMsgBox.success(data.msg,1500,function(){
                        location.reload();
                        parent.layer.closeAll();
                    });
                }else{
                    LayerMsgBox.error(data.msg);
                }
            }
        });
        layer.close(index);
        return true;
    });
    return false;
}

function openDialog(obj) {
    var o = $(obj);
    var dataArea = o.attr("data-area");
    var arr = dataArea.split(',');
    var w = arr[0];
    var h = arr[1];
    var type = 'auto';
    layer.open({
        type: 2
        ,title: o.attr("dialog-title")//false不显示标题栏
        ,offset: type //具体配置参考：http://www.layui.com/doc/modules/layer.html#offset
        ,id: 'layerDemo'+type //防止重复弹出
        ,content: o.attr("href")
        ,btn: ['确定','关闭']
        ,area: [w+'px', h+'px']
        ,btnAlign: 'r' ////按钮 c居中 r居右 l居左
        ,shade: 0 //不显示遮罩
        ,no: function(layero){
            layer.closeAll();
        }
        ,yes: function(layero){
            var form = $(".layui-form");
            s();
            layer.closeAll();
        }
    });
    return false;
}

