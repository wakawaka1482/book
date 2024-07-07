var vue = new Vue({
    el: '#app',
    data: {
        dataList: [],
        dialogFormVisible: false,
        dialogFormVisible4Edit: false,
        formData: {},
        selectedFile: null,  // 用于存储选中的文件
        rules: {
            type: [{ required: true, message: '图书类别为必填项', trigger: 'blur' }],
            name: [{ required: true, message: '图书名称为必填项', trigger: 'blur' }]
        },
        pagination: {
            currentPage: 1,
            pageSize: 10,
            total: 0,
            name: "",
            type: "",
            description: ""
        },
    },
    created() {
        this.getAll();
    },
    methods: {
        getAll() {
            let param = "?name=" + this.pagination.name;
            param += "&type=" + this.pagination.type;
            param += "&description=" + this.pagination.description;

            axios.get("/books/" + this.pagination.currentPage + "/" + this.pagination.pageSize + param)
                .then((res) => {
                    this.pagination.total = res.data.data.total;
                    this.pagination.currentPage = res.data.data.current;
                    this.pagination.pagesize = res.data.data.size;
                    this.dataList = res.data.data.records;
                });
        },
        handleCreate() {
            this.dialogFormVisible = true;
            this.resetForm();
        },
        resetForm() {
            this.formData = {};
            this.selectedFile = null;  // 重置选中的文件
        },
        resetInput() {
            this.pagination.name = "";
            this.pagination.type = "";
            this.pagination.description = "";
            this.getAll();
        },
        handleFileChange(event) {
            this.selectedFile = event.target.files[0];  // 获取选中的文件
        },
        handleAdd() {
            const formData = new FormData();
            formData.append('book', JSON.stringify(this.formData));
            formData.append('image', this.selectedFile);

            axios.post('/books', formData, {
                headers: {
                    'Content-Type': 'multipart/form-data'
                }
            }).then(res => {
                if (res.data.flag) {
                    this.dialogFormVisible = false;
                    this.$message.success(res.data.msg);
                } else {
                    this.$message.error(res.data.msg);
                }
            }).finally(() => {
                this.getAll();
            });
        },
        cancel() {
            this.dialogFormVisible = false;
            this.dialogFormVisible4Edit = false;
            this.$message.info("操作取消");
        },
        handleDelete(row) {
            this.$confirm("确认删除当前信息？", "提示", {type: "info"}).then(() => {
                axios.delete("/books/" + row.id).then((res) => {
                    if (res.data.flag) {
                        this.$message({
                            message: '删除成功',
                            type: 'success'
                        });
                    } else {
                        this.$message.error('删除失败');
                    }
                }).finally(() => {
                    this.getAll();
                });
            }).catch(() => {
                this.$message.info("取消操作");
            });
        },
        handleUpdate(row) {
            axios.get("/books/" + row.id).then((res) => {
                if (res.data.flag) {
                    this.formData = res.data.data;
                    this.dialogFormVisible4Edit = true;
                } else {
                    this.$message.error("数据同步失败，自动刷新");
                }
            }).finally(() => {
                this.getAll();
            })
        },
        handleEdit() {
            const formData = new FormData();
            formData.append('book', JSON.stringify(this.formData));
            if (this.selectedFile) {
                formData.append('image', this.selectedFile);
            }

            axios.put('/books', formData, {
                headers: {
                    'Content-Type': 'multipart/form-data'
                }
            }).then(res => {
                if (res.data.flag) {
                    this.dialogFormVisible4Edit = false;
                    this.$message.success("修改成功");
                } else {
                    this.$message.error("修改失败，请重试");
                }
            }).finally(() => {
                this.getAll();
            });
        },
        handleCurrentChange(currentPage) {
            this.pagination.currentPage = currentPage;
            this.getAll();
        },
        getImageUrl(imageData) {
            return 'data:image/jpeg;base64,' + imageData;
        },
    }
})