var vue = new Vue({
    el: '#app',
    data:{
        pagination: {//分页相关模型数据
            currentPage: 1,//当前页码
            pageSize:5,//每页显示的记录数
            total:0,//总记录数
            queryString:null//查询条件
        },
        dataList: [],//当前页要展示的分页列表数据
        formData: {},//表单数据
        dialogFormVisible: false,//增加表单是否可见
        dialogFormVisible4Edit:false,//编辑表单是否可见
        isAdmin: true,// 标识是否为管理员
        rules: {//校验规则
            bookname: [{ required: true, message: '图书名称为必填项', trigger: 'blur' }],
            author: [{ required: true, message: '图书作者为必填项', trigger: 'blur' }]
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

        //分页查询
        findPage() {
            //发送ajax请求，提交分页相关请求参数（页码、每页显示记录数、查询条件）
            var param = {
                currentPage: this.pagination.currentPage,
                pageSize: this.pagination.pageSize,
                bookname: this.pagination.queryString
            };
            let url = this.isAdmin ? "/collect/findPageAll" : "/collect/findPage";
            axios.post(url, param).then((res) => {
                //解析Controller响应回的数据，为模型数据赋值
                this.pagination.total = res.data.data.total;
                this.dataList = res.data.data.records;
            }).catch((error) => {
                this.$message.error('加载数据失败: ' + error.message);
            });
        },
        // 重置表单
        resetForm() {
            this.formData = {};//重置数据
        },
        // 弹出添加窗口
        handleCreate() {
            //弹出新增窗口
            this.dialogFormVisible = true;
            this.resetForm();
        },

        //切换页码
        handleCurrentChange(currentPage) {
            //设置最新的页码
            this.pagination.currentPage = currentPage;
            //重新调用findPage方法进行分页查询
            this.findPage();
        },
        // 删除收藏
        handleDelete(row) {//row其实是一个json对象，
            this.$confirm("你确定要删除此收藏吗？", "提示", {//确认框
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                axios.post('/collect/delete', { collectrecordid: row.collectrecordid })
                    .then(response => {
                        if (response.data.flag) {
                            this.findPage();
                        }
                    });
            }).catch(() => {
                this.$message({
                    type: 'info',
                    message: '操作已取消'
                });
            });
        },
        // 获取图片URL
        getImageUrl(base64String) {
            return base64String;
        },
    }
});