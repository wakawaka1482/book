new Vue({
    el: '#app',
    data:{
        name:"admin",
        menuList:[
            {
                "path": "2",
                "title": "读者管理",
                "icon":"fa-user-md",
                "children": [
                    {
                        "path": "/2-1",
                        "title": "读者档案",
                        "linkUrl":"userList.html",
                        "children":[]
                    },
                ]
            },
            {
                "path": "3",
                "title": "图书管理",
                "icon":"fa-book",
                "children": [
                    {
                        "path": "/3-1",
                        "title": "图书列表",
                        "linkUrl":"books.html",
                        "children":[]
                    },
                    {
                        "path": "/3-2",
                        "title": "图书数量",
                        "linkUrl":"Echars.html",
                        "children":[]
                    },
                ]
            },
            {
                "path": "4",
                "title": "借还管理",
                "icon":"fa-pencil",
                "children":[
                    {
                        "path": "/4-1",
                        "title": "借还记录",
                        "linkUrl":"lend_admin.html",
                        "children":[]
                    },
                ]
            },
            {
                "path": "5",     //菜单项所对应的路由路径
                "title": "收藏管理",     //菜单项名称
                "icon":"fa-cog",
                "children":[//是否有子菜单，若没有，则为[]
                    {
                        "path": "/5-1",
                        "title": "收藏管理",
                        "linkUrl":"collect_admin.html",
                        "children":[]
                    },
                ]
            },
            {
                "path": "6",
                "title": "其他",
                "icon":"fa-dashboard",
                "children": [
                    {
                        "path": "/1-1",
                        "title": "借阅数量",
                        "linkUrl":"lend_num.html",
                        "children":[]
                    },
                ]
            },
        ],
        dialogFormVisible4Edit:false,//编辑表单是否可见
        formData: {},//表单数据
    },
    mounted(){
        var item = window.localStorage.getItem("username");
        if (item != "admin") {
            alert("非法访问！");
            window.history.go(-1);
        }
        this.formData.username=item;
        this.changename(item);
    },
    methods:{
        // 重置表单
        resetForm() {
            this.formData = {};//重置数据
        },
        index(){

            window.localStorage.removeItem("username");
            window.location.href="../index.html";
        }
    },
});
$(function() {
    var wd = 200;
    $(".el-main").css('width', $('body').width() - wd + 'px');
});