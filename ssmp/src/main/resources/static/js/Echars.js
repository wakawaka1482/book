$(document).ready(function(){
    // 基于准备好的dom，初始化echarts实例
    var myChart = echarts.init(document.getElementById('main'));
    //数据加载完之前先显示一段简单的loading动画
    myChart.showLoading();

    $.ajax({
        type : "post",
        async : true,            //异步请求（同步请求将会锁住浏览器，用户其他操作必须等待请求完成才可以执行）
        url : "/books/EcharsShow",    //请求发送到dataActiont处
        data : {},
        dataType : "json",        //返回数据形式为json
        success : function(result) {
            var names=[];    //横坐标数组（实际用来盛放X轴坐标值）
            var values=[];    //纵坐标数组（实际用来盛放Y坐标值）
            result.forEach(function(item) {
                names.push(item.type);
                values.push(item.count);
            });
            myChart.hideLoading();    //隐藏加载动画
            myChart.setOption({        //加载数据图表
                title: {
                    text: '图书分类排行'
                },
                tooltip: {},
                legend: {
                    data:['图书数量']
                },
                xAxis: {
                    type: 'category',
                    data: names,
                    axisLabel:{
                        interval:0,//0：全部显示，1：间隔为1显示对应类目，2：依次类推，（简单试一下就明白了，这样说是不是有点抽象）
                        rotate:-30,//倾斜显示，-：顺时针旋转，+或不写：逆时针旋转
                    }
                },
                yAxis: {
                    type: 'value'
                },
                series: [{
                    // 根据名字对应到相应的系列
                    name: '图书数量',
                    type: 'bar',
                    data: values
                }]
            });
        },
        error : function(errorMsg) {
            //请求失败时执行该函数
            alert("图表请求数据失败!");
            myChart.hideLoading();
        }
    })
})