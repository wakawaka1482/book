var vue = new Vue({
    el: '#app',
    data: {
        pagination: { // 分页相关模型数据
            currentPage: 1, // 当前页码
            pageSize: 6, // 每页显示的记录数
            total: 0, // 总记录数
            queryString: null // 查询条件
        },
        dataList: [], // 当前页要展示的分页列表数据
        formData: {}, // 表单数据
        dialogFormVisible: false, // 增加表单是否可见
        dialogFormVisible4Edit: false, // 编辑表单是否可见
    },
    // 钩子函数，VUE对象初始化完成后自动执行
    created() {
        this.findPage(); // VUE对象初始化完成后调用分页查询方法，完成分页查询
    },
    methods: {
        returnPage() {
            // 为了在删除最后一页的最后一条数据时能成功跳转回最后一页的上一页
            let deleteAfterPage = Math.ceil((this.pagination.total - 1) / this.pagination.pageSize);
            let currentPage = this.pagination.currentPage > deleteAfterPage ? deleteAfterPage : this.pagination.currentPage;
            this.pagination.currentPage = currentPage < 1 ? 1 : currentPage;
        },
        // 分页查询
        findPage() {
            var param = {
                currentPage: this.pagination.currentPage,
                pageSize: this.pagination.pageSize,
                queryString: this.pagination.queryString
            };
            axios.post("/lend/findPageAll", param).then((res) => {
                // 解析Controller响应回的数据，为模型数据赋值
                this.dataList = res.data.data.rows; // 这里修改为访问 data 中的 rows
                this.pagination.total = res.data.data.total;
            }).catch(error => {
                console.error('API 请求错误:', error);
            });
        },
        // 重置表单
        resetForm() {
            this.formData = {}; // 重置数据
            console.log('重置表单:', this.formData);
        },
        // 切换页码
        handleCurrentChange(currentPage) {
            // 设置最新的页码
            this.pagination.currentPage = currentPage;
            // 重新调用findPage方法进行分页查询
            this.findPage();
        },
        // 删除
        handleDelete(row) {
            this.$confirm("你确定要删除当前数据吗？", "提示", {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                // 发送ajax请求，将图书id提交到Controller进行处理
                axios.get("/lend/delete", {params: {lendid: row.lendid}}).then((res) => {
                    if (res.data.flag) {
                        // 执行成功
                        this.$message({
                            type: 'success',
                            message: res.data.message||'删除成功！'
                        });
                        // 重新进行分页查询
                        this.returnPage();
                        this.findPage();
                    } else {
                        // 执行失败
                        this.$message.error(res.data.message);
                    }
                }).catch(error => {
                    console.error('删除图书失败:', error);
                    this.$message.error('删除图书失败');
                });
            }).catch(() => {
                this.$message({
                    type: 'info',
                    message: '操作已取消'
                });
            });
        }
    }
});