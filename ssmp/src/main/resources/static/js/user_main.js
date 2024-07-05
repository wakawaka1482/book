new Vue({
    el: '#app',
    data:{
        name: "dss",
        head_img: "../img/us.jpg",
        menuList: [
            {
                "path": "3",
                "title": "图书管理",
                "icon": "fa-book",
                "children": [
                    {
                        "path": "/3-1",
                        "title": "图书列表",
                        "linkUrl": "bookList.html",
                        "children": []
                    },
                ]
            },
            {
                "path": "4",
                "title": "借还管理",
                "icon": "fa-pencil",
                "children": [
                    {
                        "path": "/4-2",
                        "title": "借还记录",
                        "linkUrl": "lend_user.html",
                        "children": []
                    },
                ]
            },
            {
                "path": "5",
                "title": "收藏管理",
                "icon": "fa-cog",
                "children": [
                    {
                        "path": "/5-2",
                        "title": "收藏管理",
                        "linkUrl": "collect_user.html",
                        "children": []
                    },
                ]
            },
        ],
        dialogFormVisible4Edit: false, //编辑表单是否可见
        rules: {
            oldpwd: [
                {required: true, message: '旧密码不可为空', trigger: 'blur'},
            ],
            newpwd: [
                {required: true, message: '新密码不可为空', trigger: 'blur'},
            ],
            confirm: [
                {required: true, message: '请确认密码', trigger: 'blur'},
            ]
        },
        formData: {}, //表单数据
    },
    created(){
    },
    mounted(){
        var item = window.localStorage.getItem("username");
        if (!item) {
            var s = this.GetQueryString("username");
            localStorage.setItem("username", s);
            var s2 = this.GetQueryString("head_img");
            this.formData.username = s;
            this.changename(s);
            this.changehead_img(s2);
        } else {
            this.formData.username = item;
            this.changename(item);
        }
        // 更新链接地址
        this.updateMenuLinks();
    },
    methods: {
        GetQueryString(key) {// 获取参数
            var url = window.location.search;
            // 正则筛选地址栏
            var reg = new RegExp("(^|&)" + key + "=([^&]*)(&|$)");
            // 匹配目标参数
            var result = url.substr(1).match(reg);
            // 返回参数值
            return result ? decodeURIComponent(result[2]) : null;
        },
        changehead_img(item) {
            this.head_img = item;
        },
        changename(item) {
            this.name = item;
        },
        updateMenuLinks() {
            // 获取用户名
            const username = this.formData.username;
            // 更新菜单链接
            this.menuList.forEach(menu => {
                menu.children.forEach(child => {
                    if (child.linkUrl.includes('bookList.html') || child.linkUrl.includes('collect_user.html')) {
                        child.linkUrl += '?username=' + encodeURIComponent(username);
                    }
                });
            });
        },
        logout() {
            this.$confirm("确定退出吗？", "提示", { // 确认框
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                // 用户点击确定按钮，发送ajax请求
                axios.get("/user/logout").then(res => {
                    if (res.data.flag) {// 执行成功
                        // 弹出成功提示信息
                        this.$message({
                            type: 'success',
                            message: res.data.message,
                        });
                    } else {
                        // 执行失败
                        this.$message.error(res.data.message);
                    }
                });
            }).catch(() => {
                this.$message({
                    type: 'info',
                    message: '操作已取消'
                });
            }).finally(() => {
                this.index();
            });
        },
        // 重置表单
        resetForm() {
            this.formData = {}; // 重置数据
        },
        updatepassword() {
            // 弹出编辑窗口
            this.dialogFormVisible4Edit = true;
            this.resetForm();
            var item = window.localStorage.getItem("username");
            this.formData.username = item;
        },
        handleEdit() {
            if (this.formData.newpwd !== this.formData.confirm) {
                this.$message({
                    type: "error",
                    message: '2次密码输入不一致！'
                });
                return;
            }
            this.$refs['dataEditForm'].validate((valid) => {
                if (valid) {
                    const payload = {
                        username: this.formData.username,
                        oldpwd: this.formData.oldpwd,
                        newpwd: this.formData.newpwd
                    };
                    console.log("Sending password update request with payload:", payload); // 调试信息
                    axios.post("/auth/updatepwd", payload).then(res => {
                        console.log("Response from server:", res); // 调试信息
                        if (res.data.flag) {
                            this.$message({
                                type: 'success',
                                message: res.data.message||'修改密码成功'
                            });
                        } else {
                            this.$message({
                                type: 'error',
                                message: res.data.message
                            });
                        }
                        this.dialogFormVisible4Edit = false;
                    }).catch(error => {
                        this.$message({
                            type: 'error',
                            message: '密码更新失败'
                        });
                        console.error("Error updating password:", error); // 调试信息
                    });
                } else {
                    this.$message.error("数据校验失败，请检查你的输入信息是否正确！");
                    return false;
                }
            });
        },

        index() {
            window.localStorage.removeItem("username");
            window.location.href = "../index.html";
        }
    },
});
$(function() {
    var wd = 200;
    $(".el-main").css('width', $('body').width() - wd + 'px');
});
