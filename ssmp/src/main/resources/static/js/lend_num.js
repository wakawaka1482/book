$(document).ready(function(){
    // 基于准备好的dom，初始化echarts实例
    var myChart = echarts.init(document.getElementById('main'));
    // 数据加载完之前先显示一段简单的loading动画
    myChart.showLoading();

    $.ajax({
        type : "post",
        async : true,            // 异步请求
        url : "/books/LendCountsShow", // 请求发送到该URL
        data : {},
        dataType : "json",       // 返回数据形式为json
        success : function(result) {
            var names = [];      // 横坐标数组（实际用来盛放X轴坐标值）
            var values = [];     // 纵坐标数组（实际用来盛放Y坐标值）
            result.forEach(function(item) {
                names.push(item.bookname); // 使用bookname作为X轴值
                values.push(item.count);
            });
            myChart.hideLoading(); // 隐藏加载动画
            myChart.setOption({    // 加载数据图表
                title: {
                    text: '图书借阅次数排行'
                },
                tooltip: {},
                legend: {
                    data:['借阅次数']
                },
                xAxis: {
                    type: 'category',
                    data: names,
                    axisLabel:{
                        interval: 0, // 全部显示
                        rotate: -30  // 倾斜显示
                    }
                },
                yAxis: {
                    type: 'value'
                },
                series: [{
                    name: '借阅次数',
                    type: 'bar',
                    data: values
                }]
            });
        },
        error : function(errorMsg) {
            // 请求失败时执行该函数
            alert("图表请求数据失败!");
            myChart.hideLoading();
        }
    });
});