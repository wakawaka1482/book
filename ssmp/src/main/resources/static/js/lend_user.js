var vue = new Vue({
    el: '#app',
    data:{
        pagination: {
            currentPage: 1,
            pageSize: 6,
            total: 0,
            queryString: null,
            bookname: null,
            username: ""
        },
        dataList: [],
        formData: {},
        dialogFormVisible: false,
        dialogFormVisible4Edit: false,
        rules: {
            bookname: [{ required: true, message: '图书名称为必填项', trigger: 'blur' }],
            author: [{ required: true, message: '图书作者为必填项', trigger: 'blur' }]
        }
    },
    mounted() {
        this.username = window.localStorage.getItem("username");
        this.findPage();
    },
    created() {
        this.username = window.localStorage.getItem("username");
        this.findPage();
    },
    methods: {
        returnPage() {
            let deleteAfterPage = Math.ceil((this.pagination.total - 1) / this.pagination.pageSize);
            let currentPage = this.pagination.currentPage > deleteAfterPage ? deleteAfterPage : this.pagination.currentPage;
            this.pagination.currentPage = currentPage < 1 ? 1 : currentPage;
        },
        findPage() {
            axios.post('/lend/findPageUser', {
                currentPage: this.pagination.currentPage,
                pageSize: this.pagination.pageSize,
                bookname: this.pagination.queryString,
                username: this.username
            }).then(response => {
                console.log('后端返回的数据:', response.data); // 调试信息
                if (response.data.flag) {
                    this.dataList = response.data.data.records;
                    this.pagination.total = response.data.data.total;
                } else {
                    console.error('获取数据失败:', response.data.msg);
                }
            }).catch(error => {
                console.error('API 请求错误:', error);
            });
        },
        resetForm() {
            this.formData = {};
        },
        handleCreate() {
            this.dialogFormVisible = true;
            this.resetForm();
        },
        handleUpdate(row) {
            axios.post("/lend/return", {
                lendid: row.lendid
            }).then((res) => {
                if (res.data.flag) {
                    this.$message({
                        type: 'success',
                        message: res.data.message|| '归还成功'
                    });
                    this.findPage(); // 刷新页面
                } else {
                    this.$message.error(res.data.message);
                }
            }).catch(error => {
                console.error('归还图书失败:', error);
                this.$message.error('归还图书失败');
            });
        },
        handleCurrentChange(currentPage) {
            this.pagination.currentPage = currentPage;
            this.findPage();
        }
    }
})