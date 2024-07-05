var vue = new Vue({
    el: '#app',
    data:{
        pagination: {//分页相关模型数据
            currentPage: 1,//当前页码
            pageSize: 8,//每页显示的记录数
            total: 0,//总记录数
            queryString: null//查询条件
        },
        dataList: [],//当前页要展示的分页列表数据
        formData: {},//表单数据
        dialogFormVisible: false,//增加表单是否可见
        dialogFormVisible4Edit: false,//编辑表单是否可见
        rules: {//校验规则
            username: [{ required: true, message: '姓名为必填项', trigger: 'blur' }],
            password: [{ required: true, message: '密码为必填项', trigger: 'blur' }],
            phone: [
                { required: true, message: '手机号不可为空', trigger: 'blur' },
                { min: 11, max: 11, message: '长度为11个字符', trigger: 'blur' }
            ]
        }
    },
    //钩子函数，VUE对象初始化完成后自动执行
    created() {
        this.findPage();//VUE对象初始化完成后调用分页查询方法，完成分页查询
    },
    methods: {
        returnPage() {
            // 为了在删除最后一页的最后一条数据时能成功跳转回最后一页的上一页
            let deleteAfterPage = Math.ceil((this.pagination.total - 1) / this.pagination.pageSize)
            let currentPage = this.pagination.currentPage > deleteAfterPage ? deleteAfterPage : this.pagination.currentPage
            this.pagination.currentPage = currentPage < 1 ? 1 : currentPage
        },
        //添加
        handleAdd() {

            //进行表单校验
            this.$refs['dataAddForm'].validate((valid) => {
                if (valid) {
                    //表单校验通过，发生ajax请求，将录入的数据提交到后台进行处理
                    axios.post("/auth/register", this.formData).then((res) => {
                        //关闭新增窗口
                        this.dialogFormVisible = false;
                        if (res.data.flag) {//执行成功
                            //新增成功后，重新调用分页查询方法，查询出最新的数据
                            this.findPage();
                            //弹出提示信息
                            this.$message({
                                message: res.data.msg,
                                type: 'success'
                            });
                        } else {//执行失败
                            //弹出提示
                            this.$message.error(res.data.msg);
                        }
                    })
                } else {
                    //校验不通过
                    this.$message.error("数据校验失败，请检查你的输入信息是否正确！");
                    return false;
                }
            });
        },
        //分页查询
        findPage() {
            var param = {
                currentPage: this.pagination.currentPage,
                pageSize: this.pagination.pageSize,
                queryString: this.pagination.queryString
            };
            axios.post("/auth/users", param).then((res) => {

                if (res.data.flag) {
                    this.pagination.total = res.data.data.total;
                    this.dataList = res.data.data.records;
                }
            });
        },

        // 重置表单
        resetForm() {
            this.formData = {};//重置数据
            console.log("Form reset.");
        },
        // 弹出添加窗口
        handleCreate() {
            //弹出新增窗口
            this.dialogFormVisible = true;
            this.resetForm();
        },
        handleUpdate(row) {
            // 弹出编辑窗口
            this.dialogFormVisible4Edit = true;

            // 发送GET请求根据ID查询当前用户数据
            axios.get(`/auth/update/${row.id}`).then((res) => {
                if (res.data.flag) {
                    this.formData = res.data.data;
                } else {
                    this.$message.error(res.data.msg);
                }
            }).catch((error) => {
                this.$message.error("Failed to fetch user data.");
            });
        },

        handleEdit() {
            // 提交更新后的用户数据
            axios.post('/auth/update', this.formData).then((res) => {
                if (res.data.flag) {
                    this.$message.success("用户更新成功");
                    this.dialogFormVisible4Edit = false;

                    this.findPage();
                } else {
                    this.$message.error(res.data.msg);
                }
            }).catch((error) => {
                this.$message.error("Failed to update user data.");
            });
        },
        //切换页码
        handleCurrentChange(currentPage) {
            //设置最新的页码
            this.pagination.currentPage = currentPage;
            //重新调用findPage方法进行分页查询
            this.findPage();
        },
        // 删除
        handleDelete(row) {
            this.$confirm("你确定要删除当前数据吗？", "提示", {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                // 用户点击确定按钮，发送ajax请求，将检查项ID提交到Controller进行处理
                axios.delete(`/auth/delete/${row.id}`).then((res) => {
                    if (res.data.flag) {
                        // 执行成功
                        // 弹出成功提示信息
                        this.$message({
                            type: 'success',
                            message: res.data.msg||'删除成功'
                        });
                        // 重新进行分页查询
                        this.returnPage();
                        this.findPage();
                    } else {
                        // 执行失败
                        this.$message.error(res.data.msg);
                    }
                })
            }).catch(() => {
                this.$message({
                    type: 'info',
                    message: '操作已取消'
                });
            });
        },
    }
})